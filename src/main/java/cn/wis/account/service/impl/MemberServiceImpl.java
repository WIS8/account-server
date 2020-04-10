package cn.wis.account.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.bean.BeanUtil;
import cn.wis.account.component.TokenManager;
import cn.wis.account.config.Constant;
import cn.wis.account.mapper.MemberMapper;
import cn.wis.account.mapper.RoleMapper;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.entity.Token;
import cn.wis.account.model.request.LoginRequest;
import cn.wis.account.model.request.PasswordUpdateRequest;
import cn.wis.account.model.request.RegisterRequest;
import cn.wis.account.model.request.RoleModifyRequest;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.table.Member;
import cn.wis.account.model.table.Role;
import cn.wis.account.model.vo.MemberVo;
import cn.wis.account.model.vo.PageVo;
import cn.wis.account.service.MemberService;
import cn.wis.account.support.RoleSup;
import cn.wis.account.util.ResultHelper;
import cn.wis.account.util.SecureHelper;

@Service
public class MemberServiceImpl implements MemberService {

	@Resource
	private HttpServletRequest http;

	@Resource
	private MemberMapper memberMapper;

	@Resource
	private RoleMapper roleMapper;

	@Resource
	private RoleSup roleSup;

	@Resource
	private TokenManager tokenManager;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void register(RegisterRequest request) {
		checkRoleMaxCount(Constant.PLAIN_ROLE);
		if (memberMapper.insert(createMemberInfoFor(request)) != 1) {
			throw new ServiceException(ResultEnum.INSERT_ERROR);
		}
	}

	@Override
	public Token login(LoginRequest request) {
		return tokenManager.createToken(checkNicknameAndPassword(request));
	}

	@Override
	public MemberVo check() {
		return getLoginMember(http);
	}

	@Override
	public boolean logout() {
		return tokenManager.removeToken(getLoginMember(http).getId());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void modifyPassword(PasswordUpdateRequest request) {
		Member member = checkNicknameAndPassword(getLoginMember(http)
				.getNickname(), request.getOldPassword());
		encryptPasswordForMember(member, request.getNewPassword());
		if (memberMapper.updateById(member) < 1) {
			throw new ServiceException(ResultEnum.UPDATE_ERROR);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void modifyRole(RoleModifyRequest request) {
		Role role = roleMapper.selectByName(request.getRoleName());
		if (ResultHelper.notAn(role)) {
			throw new ServiceException(ResultEnum.NO_KEY_ERROR, "roleName:" + request.getRoleName());
		}
		checkRoleMaxCount(role.getId());
		Member member = new Member();
		member.setId(request.getMemberId());
		member.setRoleId(role.getId());
		member.setUpdateFields(getLoginMember(http).getId());
		if (memberMapper.updateById(member) != 1) {
			throw new ServiceException(ResultEnum.UPDATE_ERROR);
		}
		Token token = tokenManager.getTokenByMemberId(request.getMemberId());
		if (token != null && token.getCookie() != null) {
			token.getMemberVo().setRoleId(role.getId());
		}
	}

	@Override
	public PageVo search(Page page) {
		return ResultHelper.translate(memberMapper.selectByPage(page), this::useRoleIdAsRoleName);
	}

	@Override
	public MemberVo view(String memberId) {
		return getMemberVo(memberMapper.selectById(memberId));
	}

	@Override
	public Integer getOnlineInfo() {
		return tokenManager.getOnlineNumber();
	}

	private void checkRoleMaxCount(String roleId) {
		int count = memberMapper.countRoleMemberNumber(roleId);
		Role role = roleMapper.selectById(roleId);
		if (role.getMaximum() <= count) {
			throw new ServiceException(ResultEnum.PAUSED_REGISTER);
		}
	}

	private Member createMemberInfoFor(RegisterRequest request) {
		Member member = new Member();
		member.setRoleId(Constant.PLAIN_ROLE);
		member.setNickname(request.getNickname());
		member.setCreateFields(Constant.VISITOR_ID);
		encryptPasswordForMember(member, request.getPassword());
		return member;
	}

	private void encryptPasswordForMember(Member member, String password) {
		member.setAuthentication(SecureHelper.encryptPassword(password));
	}

	private MemberVo checkNicknameAndPassword(LoginRequest request) {
		Member member = checkNicknameAndPassword(request.getNickname(), request.getPassword());
		return getMemberVo(member);
	}

	private Member checkNicknameAndPassword(String nickname, String password) {
		Member member = memberMapper.selectByNickname(nickname);
		if (ResultHelper.notAn(member)) {
			throw new ServiceException(ResultEnum.NO_SUCH_ACCOUNT);
		}
		if (SecureHelper.isDifferent(member.getAuthentication(), password)) {
			throw new ServiceException(ResultEnum.BAD_LOGIN_INFOS);
		}
		return member;
	}

	private MemberVo getMemberVo(Member member) {
		MemberVo vo = new MemberVo();
		BeanUtil.copyProperties(member, vo);
		vo.setRegisterTime(member.getCreateTime());
		return vo;
	}

	private MemberVo useRoleIdAsRoleName(Member member) {
		MemberVo vo = getMemberVo(member);
		vo.setRoleId(roleSup.checkRoleIdInCache(member.getRoleId()));
		return vo;
	}

}
