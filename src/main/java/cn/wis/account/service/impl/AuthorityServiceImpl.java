package cn.wis.account.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.wis.account.mapper.AuthorityMapper;
import cn.wis.account.service.AuthorityService;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Resource
	private AuthorityMapper authorityMapper;

}
