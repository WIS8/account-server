package cn.wis.account.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.wis.account.model.result.Result;
import cn.wis.account.service.ParameterService;
import cn.wis.account.util.ParamHelper;

@RestController
@RequestMapping("/api/v1/parameter")
public class ParameterController {

	@Resource
	private ParameterService parameterService;

	@GetMapping("/confirm")
	public Result confirm(@RequestParam("providerId") String providerId) {
		ParamHelper.checkValue(providerId);
		return Result.trueResult(parameterService.confirm(providerId));
	}

}
