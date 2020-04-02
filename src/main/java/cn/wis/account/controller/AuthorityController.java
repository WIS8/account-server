package cn.wis.account.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.wis.account.service.AuthorityService;

@RestController
@RequestMapping("/api/v1/authority")
public class AuthorityController {

	@Resource
	private AuthorityService authorityService;

}
