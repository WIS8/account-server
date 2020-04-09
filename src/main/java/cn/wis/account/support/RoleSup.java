package cn.wis.account.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import cn.wis.account.mapper.RoleMapper;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.table.Role;
import cn.wis.account.util.ResultHelper;

@Component
public class RoleSup {

	@Resource
	private RoleMapper roleMapper;

	private Map<String, String> map;

	public RoleSup() {
		map = new ConcurrentHashMap<String, String>();
	}

	public Role checkRoleName(String roleName) {
		Role role = roleMapper.selectByName(roleName);
		if (ResultHelper.notAn(role)) {
			throw new ServiceException(ResultEnum.NO_KEY_ERROR);
		}
		return role;
	}

	public Role checkRoleId(String roleId) {
		Role role = roleMapper.selectById(roleId);
		if (ResultHelper.notAn(role)) {
			throw new ServiceException(ResultEnum.NO_KEY_ERROR);
		}
		return role;
	}

	public String checkRoleIdInCache(String roleId) {
		if (map.containsKey(roleId)) {
			return map.get(roleId);
		} else {
			Role role = checkRoleId(roleId);
			map.put(roleId, role.getAppellation());
			return role.getAppellation();
		}
	}

	public void flushCache(String roleId, String name) {
		if (map.containsKey(roleId) && StrUtil.isNotEmpty(name)) {
			map.put(roleId, name);
		}
	}

	public void removeFromCache(String roleId) {
		map.remove(roleId);
	}

}
