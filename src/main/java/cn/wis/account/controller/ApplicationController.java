package cn.wis.account.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.wis.account.aop.Rule;
import cn.wis.account.config.Constant;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.request.app.ApplicationUpdateRequest;
import cn.wis.account.model.request.feign.ApplicationConfirmRequest;
import cn.wis.account.model.result.Result;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.service.ApplicationService;
import cn.wis.account.util.ParamHelper;

@RestController
@RequestMapping("/api/v1/app")
public class ApplicationController {

	@Resource
	private ApplicationService appService;

	@PutMapping("/confirm")
	public Result confirm(@RequestBody ApplicationConfirmRequest request) {
		ParamHelper.checkValue(request.getName(), Constant.REGEX_GENERAL_NAME);
		return Result.trueResult(appService.confirm(request));
	}

	@Rule
	@DeleteMapping("/delete")
	public Result delete(@RequestParam("appId") String appId) {
		ParamHelper.checkValue(appId);
		appService.delete(appId);
		return Result.trueResult();
	}

	@PostMapping("/update")
	public Result update(@RequestBody ApplicationUpdateRequest request) {
		checkUpdateFields(request);
		appService.update(request);
		return Result.trueResult();
	}

	@GetMapping("/search")
	public Result search(@RequestParam(name = "index", required = false) Integer index,
			@RequestParam(name = "count", required = false) Integer count,
			@RequestParam(name = "keyWord", required = false) String keyWord) {
		Page page = new Page(index, count, keyWord);
		ParamHelper.checkOrSetDefaultValue(page);
		return Result.trueResult(appService.search(page));
	}

	@GetMapping("/get-all-mine-app")
	public Result searchAllMineApp() {
		return Result.trueResult(appService.searchAllMineApp());
	}

	private void checkUpdateFields(ApplicationUpdateRequest request) {
		ParamHelper.checkValue(request.getId());
		int count = 0;
		if (request.getName() != null) {
			count++;
			ParamHelper.checkValue(request.getName(), Constant.REGEX_GENERAL_NAME);
		}
		if (request.getPluginName() != null) {
			count++;
			ParamHelper.checkValue(request.getPluginName(), Constant.REGEX_GENERAL_NAME);
		}
		if (request.getDescription() != null) {
			count++;
			ParamHelper.checkValue(request.getDescription(), Constant.TEXT_MAX_LENGTH);
		}
		if (count == 0) {
			throw new ServiceException(ResultEnum.ILLEGAL);
		}
	}

}
