package cn.wis.account.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.wis.account.model.request.AuthAddRequest;
import cn.wis.account.model.request.AuthDeleteRequest;
import cn.wis.account.model.result.Result;
import cn.wis.account.service.AuthorityService;
import cn.wis.account.util.ParamHelper;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthorityController {

	@Resource
	private AuthorityService authService;

	@PutMapping("/add")
	public Result add(@RequestBody AuthAddRequest request) {
		ParamHelper.checkValue(request.getApiId());
		ParamHelper.checkValue(request.getRoleName());
		return Result.trueResult(authService.add(request));
	}

	@DeleteMapping("/remove")
	public Result remove(@RequestParam("apiId") String apiId,
			@RequestParam("roleId") String roleId) {
		ParamHelper.checkValue(apiId);
		ParamHelper.checkValue(roleId);
		authService.remove(new AuthDeleteRequest(roleId, apiId));
		return Result.trueResult();
	}

	@GetMapping("/search")
	public Result search(@RequestParam("apiId") String apiId) {
		ParamHelper.checkValue(apiId);
		return Result.trueResult(authService.search(apiId));
	}

}
