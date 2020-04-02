package cn.wis.account.service.impl;

import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.bean.BeanUtil;
import cn.wis.account.component.AccountInitializer;
import cn.wis.account.mapper.ApiMapper;
import cn.wis.account.mapper.ApplicationMapper;
import cn.wis.account.mapper.ParameterMapper;
import cn.wis.account.mapper.ProviderMapper;
import cn.wis.account.model.dto.ApplicationDto;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.request.app.ApplicationUpdateRequest;
import cn.wis.account.model.request.feign.ApplicationConfirmRequest;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.table.Application;
import cn.wis.account.model.vo.ApplicationVo;
import cn.wis.account.model.vo.PageVo;
import cn.wis.account.service.ApplicationService;
import cn.wis.account.support.ApplicationSup;
import cn.wis.account.util.ParamHelper;
import cn.wis.account.util.ResultHelper;

@Service
public class ApplicationServiceImpl implements ApplicationService {

	@Resource
	private HttpServletRequest http;

	@Resource
	private ApplicationMapper appMapper;

	@Resource
	private ApiMapper apiMapper;

	@Resource
	private ProviderMapper providerMapper;

	@Resource
	private ParameterMapper parameterMapper;

	@Resource
	private AccountInitializer initializer;

	@Resource
	private ApplicationSup appSup;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ApplicationDto confirm(ApplicationConfirmRequest request) {
		ApplicationDto dto = getDtoIfAppInDB(request.getName());
		if (dto == null) {
			ParamHelper.checkValue(request.getPluginName());
			dto = getDtoIfAppIsOk(increaseNewApp(createAppFrom(request)));
		}
		return dto;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(String appId) {
		checkForeignKeyInDB(appId);
		if (appMapper.deleteById(appId) != 1) {
			throw new ServiceException(ResultEnum.DELETE_ERROR);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(ApplicationUpdateRequest request) {
		Application app = new Application();
		app.setId(request.getId());
		app.setAppellation(request.getName());
		app.setPluginName(request.getPluginName());
		app.setDescription(request.getDescription());
		app.setUpdateFields(getLoginMember(http).getId());
		if (appMapper.updateById(app) != 1) {
			throw new ServiceException(ResultEnum.UPDATE_ERROR);
		}
	}

	@Override
	public PageVo search(Page page) {
		PageVo pageVo = new PageVo();
		pageVo.setData(appMapper.selectPage(page.getIndex(), page.getSize())
				.parallelStream().map(this::getAppVo).collect(Collectors.toList()));
		pageVo.setTotal(appMapper.countByKeyWord(page.getKeyWord()));
		return null;
	}

	private ApplicationDto getDtoIfAppInDB(String appName) {
		Application app = appMapper.selectByName(appName);
		return ResultHelper.notAn(app) ? null : getDtoIfAppIsOk(app);
	}

	private ApplicationDto getDtoIfAppIsOk(Application app) {
		if (ResultHelper.notAn(app)) {
			throw new ServiceException(ResultEnum.UNKNOWN);
		}
		ApplicationDto dto = new ApplicationDto();
		BeanUtil.copyProperties(app, dto);
		return dto;
	}

	private Application createAppFrom(ApplicationConfirmRequest request) {
		Application app = new Application();
		app.setAppellation(request.getName());
		app.setPluginName(request.getPluginName());
		app.setCreateFields(initializer.getUrl(http));
		return app;
	}

	private Application increaseNewApp(Application app) {
		int result = 0;
		try {
			result = appMapper.insert(app);
		} catch (Exception e) {
			result = 0;
		}
		return result == 0 ? appMapper.selectByName(app.getAppellation()) : app;
	}

	private void checkForeignKeyInDB(String appId) {
		int count = apiMapper.countByApp(appId);
		count += providerMapper.countByApp(appId);
		count += parameterMapper.countByApp(appId);
		if (count != 0) {
			throw new ServiceException(ResultEnum.DELETE_ERROR, "appId存在外键：" + count);
		}
	}

	private ApplicationVo getAppVo(Application app) {
		ApplicationVo appVo = new ApplicationVo();
		BeanUtil.copyProperties(app, appVo);
		appVo.setApiNumber(apiMapper.countByApp(app.getId()));
		appVo.setProviderNumber(providerMapper.countByApp(app.getId()));
		appVo.setParameterNumber(parameterMapper.countByApp(app.getId()));
		return appVo;
	}

}
