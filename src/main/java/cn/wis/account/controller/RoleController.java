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

import cn.wis.account.config.Constant;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.request.RoleAddRequest;
import cn.wis.account.model.request.RoleUpdateRequest;
import cn.wis.account.model.result.Result;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.service.RoleService;
import cn.wis.account.util.ParamHelper;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

	@Resource
	private RoleService roleService;

	@PutMapping("/add")
	public Result add(@RequestBody RoleAddRequest request) {
		ParamHelper.checkValue(request.getMaximum(), 0, Constant.ROLE_MAX_COUNT);
		ParamHelper.checkValue(request.getAppellation(), Constant.REGEX_IDENTIFIER);
		roleService.add(request);
		return Result.trueResult();
	}

	@DeleteMapping("/delete")
	public Result delete(@RequestParam("roleId") String roleId) {
		ParamHelper.checkValue(roleId);
		roleService.delete(roleId);
		return Result.trueResult();
	}

	@PostMapping("/update")
	public Result update(@RequestBody RoleUpdateRequest request) {
		checkUpdateFields(request);
		roleService.update(request);
		return Result.trueResult();
	}

	@GetMapping("/search")
	public Result search(@RequestParam(name = "index", required = false) Integer index,
			@RequestParam(name = "count", required = false) Integer count,
			@RequestParam(name = "keyWord", required = false) String keyWord) {
		Page page = new Page(index, count, keyWord);
		ParamHelper.checkOrSetDefaultValue(page);
		return Result.trueResult(roleService.search(page));
	}

	private void checkUpdateFields(RoleUpdateRequest request) {
		ParamHelper.checkValue(request.getId());
		int count = 0;
		if (request.getAppellation() != null) {
			count++;
			ParamHelper.checkValue(request.getAppellation(), Constant.REGEX_IDENTIFIER);
		}
		if (request.getDescription() != null) {
			count++;
			ParamHelper.checkValue(request.getDescription(), Constant.TEXT_MAX_LENGTH);
		}
		if (request.getMaximum() != null) {
			count++;
			ParamHelper.checkValue(request.getMaximum(), 0, Constant.ROLE_MAX_COUNT);
		}
		if (count == 0) {
			throw new ServiceException(ResultEnum.ILLEGAL);
		}
	}

}
