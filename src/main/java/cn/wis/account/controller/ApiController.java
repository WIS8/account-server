package cn.wis.account.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.wis.account.model.entity.Page;
import cn.wis.account.model.request.feign.ApiConfirmRequest;
import cn.wis.account.model.result.Result;
import cn.wis.account.service.ApiService;
import cn.wis.account.util.ParamHelper;

@RestController
@RequestMapping("/api/v1/api")
public class ApiController {

	@Resource
	private ApiService apiService;

	@PutMapping("/confirm")
	public Result confirm(@RequestBody ApiConfirmRequest request) {
		ParamHelper.checkValue(request.getAppId());
		ParamHelper.checkValue(request.getApis());
		return Result.trueResult(apiService.confirm(request));
	}

	@GetMapping("/search")
	public Result search(@RequestParam(name = "index", required = false) Integer index,
			@RequestParam(name = "count", required = false) Integer count,
			@RequestParam(name = "keyWord", required = false) String keyWord) {
		Page page = new Page(index, count, keyWord);
		ParamHelper.checkOrSetDefaultValue(page);
		return Result.trueResult(apiService.search(page));
	}

}
