package cn.wis.account.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.wis.account.config.Constant;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.request.param.ParamUpdateRequest;
import cn.wis.account.model.result.Result;
import cn.wis.account.service.ParameterService;
import cn.wis.account.util.ParamHelper;

@RestController
@RequestMapping("/api/v1/param")
public class ParameterController {

	@Resource
	private ParameterService paramService;

	@PostMapping("/update")
	public Result update(@RequestBody ParamUpdateRequest request) {
		checkUpdateFields(request);
		paramService.update(request);
		return Result.trueResult();
	}

	@GetMapping("/confirm")
	public Result confirm(@RequestParam("providerId") String providerId) {
		ParamHelper.checkValue(providerId);
		return Result.trueResult(paramService.confirm(providerId));
	}

	@GetMapping("/search")
	public Result search(@RequestParam(name = "index", required = false) Integer index,
			@RequestParam(name = "count", required = false) Integer count,
			@RequestParam(name = "keyWord", required = false) String keyWord) {
		Page page = new Page(index, count, keyWord);
		ParamHelper.checkOrSetDefaultValue(page);
		return Result.trueResult(paramService.search(page));
	}

	private void checkUpdateFields(ParamUpdateRequest request) {
		ParamHelper.checkValue(request.getId());
		int count = 0;
		if (request.getValue() != null) {
			count++;
			ParamHelper.checkValue(request.getValue(), Constant.TEXT_MAX_LENGTH);
		}
		if (request.getDescription() != null) {
			count++;
			ParamHelper.checkValue(request.getDescription(), Constant.TEXT_MAX_LENGTH);
		}
		ParamHelper.checkValue(count, 1, 2);
	}

}
