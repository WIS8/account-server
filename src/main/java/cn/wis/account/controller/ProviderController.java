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
import cn.wis.account.model.request.feign.ProviderConfirmRequest;
import cn.wis.account.model.request.provider.ProviderUpdateRequest;
import cn.wis.account.model.result.Result;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.service.ProviderService;
import cn.wis.account.util.ParamHelper;

@RestController
@RequestMapping("/api/v1/provider")
public class ProviderController {

	@Resource
	private ProviderService providerService;

	@PutMapping("/confirm")
	public Result confirm(@RequestBody ProviderConfirmRequest request) {
		ParamHelper.checkValue(request.getUrl(), Constant.TEXT_MAX_LENGTH);
		ParamHelper.checkValue(request.getAppId());
		return Result.trueResult(providerService.confirm(request));
	}

	@Rule
	@DeleteMapping("/delete")
	public Result delete(@RequestParam("providerId") String providerId) {
		ParamHelper.checkValue(providerId);
		providerService.delete(providerId);
		return Result.trueResult();
	}

	@PostMapping("/update")
	public Result update(@RequestBody ProviderUpdateRequest request) {
		checkUpdateFields(request);
		providerService.update(request);
		return Result.trueResult();
	}

	@GetMapping("/search")
	public Result search(@RequestBody Page page) {
		ParamHelper.checkOrSetDefaultValue(page);
		return Result.trueResult(providerService.search(page));
	}

	private void checkUpdateFields(ProviderUpdateRequest request) {
		ParamHelper.checkValue(request.getId());
		int count = 0;
		if (request.getIdentifier() != null) {
			count++;
			ParamHelper.checkValue(request.getIdentifier(), Constant.REGEX_GENERAL_NAME);
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
