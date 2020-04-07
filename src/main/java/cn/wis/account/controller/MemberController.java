package cn.wis.account.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.util.StrUtil;
import cn.wis.account.config.Constant;
import cn.wis.account.model.request.LoginRequest;
import cn.wis.account.model.request.PasswordUpdateRequest;
import cn.wis.account.model.request.RegisterRequest;
import cn.wis.account.model.result.Result;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.service.MemberService;
import cn.wis.account.util.ParamHelper;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

	@Resource
	private MemberService memberService;

	@PostMapping("/register")
	public Result register(@RequestBody RegisterRequest request) {
		ParamHelper.checkValue(request.getNickname(), Constant.REGIX_NICKNAME);
		ParamHelper.checkValue(request.getPassword(), Constant.REGIX_PASSWORD);
		memberService.register(request);
		return Result.trueResult();
	}

	@PostMapping("/login")
	public Result login(@RequestBody LoginRequest request) {
		if (StrUtil.isEmpty(request.getNickname())
				|| StrUtil.isEmpty(request.getPassword())) {
			throw new ServiceException(ResultEnum.ILLEGAL);
		}
		return Result.trueResult(memberService.login(request));
	}

	@GetMapping("/check")
	public Result check() {
		return Result.trueResult(memberService.check());
	}

	@PostMapping("/logout")
	public Result logout() {
		return Result.trueResult(memberService.logout());
	}

	@PostMapping("/modify-password")
	public Result modifyPassword(@RequestBody PasswordUpdateRequest request) {
		ParamHelper.checkValue(request.getNewPassword(), Constant.REGIX_PASSWORD);
		ParamHelper.checkValue(request.getOldPassword());
		memberService.modifyPassword(request);
		return Result.trueResult();
	}

}
