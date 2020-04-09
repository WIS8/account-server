package cn.wis.account.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollectionUtil;
import cn.wis.account.mapper.AuthorityMapper;
import cn.wis.account.model.request.AuthAddRequest;
import cn.wis.account.model.request.AuthDeleteRequest;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.table.Authority;
import cn.wis.account.model.table.Role;
import cn.wis.account.model.vo.AuthVo;
import cn.wis.account.service.AuthorityService;
import cn.wis.account.support.ApiSup;
import cn.wis.account.support.RoleSup;
import cn.wis.account.util.ResultHelper;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Resource
	private HttpServletRequest http;

	@Resource
	private AuthorityMapper authMapper;

	@Resource
	private ApiSup apiSup;

	@Resource
	private RoleSup roleSup;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String add(AuthAddRequest request) {
		apiSup.checkAuthApi(request.getApiId());
		Role role = roleSup.checkRoleName(request.getRoleName());
		Authority auth = authMapper.selectByRoleAndApi(role.getId(), request.getApiId());
		if (ResultHelper.exist(auth)) {
			throw new ServiceException(ResultEnum.AUTH_ALREADY_IN);
		}
		auth = new Authority();
		auth.setApiId(request.getApiId());
		auth.setRoleId(role.getId());
		auth.setCreateFields(getLoginMember(http).getId());
		if (authMapper.insert(auth) != 1) {
			throw new ServiceException(ResultEnum.INSERT_ERROR);
		}
		return role.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void remove(AuthDeleteRequest request) {
		if (authMapper.deleteByRoleAndApi(request.getRoleId(), request.getApiId()) != 1) {
			throw new ServiceException(ResultEnum.DELETE_ERROR);
		}
	}

	@Override
	public List<AuthVo> search(String apiId) {
		List<Authority> authes = authMapper.selectAllByApi(apiId);
		if (CollectionUtil.isEmpty(authes)) {
			return Collections.emptyList();
		}
		return authes.parallelStream().map(this::getAuthVo).collect(Collectors.toList());
	}

	private AuthVo getAuthVo(Authority auth) {
		AuthVo vo = new AuthVo();
		vo.setRoleId(auth.getRoleId());
		vo.setRoleName(roleSup.checkRoleIdInCache(auth.getRoleId()));
		return vo;
	}

}
