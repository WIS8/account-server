package cn.wis.account.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.bean.BeanUtil;
import cn.wis.account.mapper.MemberMapper;
import cn.wis.account.mapper.RoleMapper;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.request.RoleAddRequest;
import cn.wis.account.model.request.RoleUpdateRequest;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.table.Role;
import cn.wis.account.model.vo.PageVo;
import cn.wis.account.model.vo.RoleVo;
import cn.wis.account.service.RoleService;
import cn.wis.account.support.RoleSup;
import cn.wis.account.util.ResultHelper;

@Service
public class RoleServiceImpl implements RoleService {

	@Resource
	private HttpServletRequest http;

	@Resource
	private MemberMapper memberMapper;

	@Resource
	private RoleMapper roleMapper;

	@Resource
	private RoleSup roleSup;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void add(RoleAddRequest request) {
		Role role = new Role();
		BeanUtil.copyProperties(request, role);
		role.setCreateFields(getLoginMember(http).getId());
		if (roleMapper.insert(role) != 1) {
			throw new ServiceException(ResultEnum.INSERT_ERROR);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(String roleId) {
		checkForeignKeyInDB(roleId);
		if (roleMapper.deleteById(roleId) != 1) {
			throw new ServiceException(ResultEnum.DELETE_ERROR);
		}
		roleSup.removeFromCache(roleId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(RoleUpdateRequest request) {
		Role role = new Role();
		role.setId(request.getId());
		role.setMaximum(request.getMaximum());
		role.setAppellation(request.getAppellation());
		role.setDescription(request.getDescription());
		role.setUpdateFields(getLoginMember(http).getId());
		if (roleMapper.updateById(role) != 1) {
			throw new ServiceException(ResultEnum.UPDATE_ERROR);
		}
		roleSup.flushCache(request.getId(), request.getAppellation());
	}

	@Override
	public PageVo search(Page page) {
		return ResultHelper.translate(roleMapper.selectByPage(page), this::getRoleVo);
	}

	private void checkForeignKeyInDB(String roleId) {
		int count = memberMapper.countRoleMemberNumber(roleId);
		if (count > 0) {
			throw new ServiceException(ResultEnum.DELETE_ERROR, "roleId存在外键");
		}
	}

	private RoleVo getRoleVo(Role role) {
		RoleVo roleVo = new RoleVo();
		BeanUtil.copyProperties(role, roleVo);
		roleVo.setMemberNumber(memberMapper.countRoleMemberNumber(role.getId()));
		return roleVo;
	}

}
