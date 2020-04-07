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
import cn.wis.account.model.entity.Token;
import cn.wis.account.model.request.LoginRequest;
import cn.wis.account.model.request.PasswordUpdateRequest;
import cn.wis.account.model.request.RegisterRequest;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.table.Member;
import cn.wis.account.model.table.Role;
import cn.wis.account.model.vo.MemberVo;
import cn.wis.account.service.MemberService;
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
	public void modifyPassword(PasswordUpdateRequest request) {
		Member member = checkNicknameAndPassword(getLoginMember(http)
				.getNickname(), request.getOldPassword());
		encryptPasswordForMember(member, request.getNewPassword());
		if (memberMapper.updateById(member) < 1) {
			throw new ServiceException(ResultEnum.UPDATE_ERROR);
		}
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
		MemberVo memberVo = new MemberVo();
		BeanUtil.copyProperties(member, memberVo);
		memberVo.setRegisterTime(member.getCreateTime());
		return memberVo;
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

}
