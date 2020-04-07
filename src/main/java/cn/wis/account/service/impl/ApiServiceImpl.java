package cn.wis.account.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.wis.account.component.AccountInitializer;
import cn.wis.account.config.Constant;
import cn.wis.account.mapper.ApiMapper;
import cn.wis.account.model.dto.ApiConfirmResult;
import cn.wis.account.model.dto.ApiDto;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.enums.RuleListEnum;
import cn.wis.account.model.request.feign.ApiConfirmRequest;
import cn.wis.account.model.request.feign.ApiMap;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.table.Api;
import cn.wis.account.model.table.Application;
import cn.wis.account.model.vo.ApiVo;
import cn.wis.account.model.vo.PageVo;
import cn.wis.account.service.ApiService;
import cn.wis.account.support.ApplicationSup;
import cn.wis.account.util.ParamHelper;
import cn.wis.account.util.ResultHelper;

@Service
public class ApiServiceImpl implements ApiService {

	@Resource
	private HttpServletRequest http;

	@Resource
	private ApiMapper apiMapper;

	@Resource
	private AccountInitializer initializer;

	@Resource
	private ApplicationSup appSup;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ApiConfirmResult confirm(ApiConfirmRequest request) {
		Application app = appSup.checkAppId(request.getAppId());
		List<Api> apis = CollectionUtil.emptyIfNull(apiMapper.selectAllByApp(app.getId()));
		Map<String, Api> older = apis.stream().collect(Collectors.toMap(Api::getAppellation, api -> api));
		List<Api> creators = new ArrayList<Api>();
		List<Api> updaters = new ArrayList<Api>();
		List<String> deleters = new ArrayList<String>();
		checkApiInfo(request.getApis(), older, app.getId(), creators, updaters, deleters);
		if (creators.size() != 0 && apiMapper.batchInsert(creators) != creators.size()) {
			throw new ServiceException(ResultEnum.INSERT_ERROR);
		}
		if (updaters.size() != 0) {
			int count = 0;
			for (Api api : updaters) {
				count += apiMapper.updateById(api);
			}
			if (count != updaters.size()) {
				throw new ServiceException(ResultEnum.UPDATE_ERROR);
			}
		}
		if (deleters.size() != 0 && apiMapper.deleteBatchIds(deleters) != deleters.size()) {
			throw new ServiceException(ResultEnum.DELETE_ERROR);
		}
		ApiConfirmResult result = new ApiConfirmResult();
		result.setApis(apiMapper.selectAllByApp(app.getId()).stream()
				.map(api -> new ApiMap(api.getId(), api.getAppellation()))
				.collect(Collectors.toList()));
		result.setCreateNumber(creators.size());
		result.setUpdateNumber(updaters.size());
		result.setDeleteNumber(deleters.size());
		return result;
	}

	@Override
	public PageVo search(Page page) {
		return ResultHelper.translate(apiMapper.selectByPage(page), this::getApiVo);
	}

	private void checkApiInfo(List<ApiDto> dtos, Map<String, Api> older,
			String appId, List<Api> creators, List<Api> updaters, List<String> deleters) {
		String operatorId = initializer.getUrl(http);
		dtos.stream().forEach(dto -> {
			checkApiDto(dto);
			if (older.containsKey(dto.getAppellation())) {
				Api api = older.get(dto.getAppellation());
				if (needUpdate(api, dto)) {
					api.setUpdateFields(operatorId);
					updaters.add(api);
				}
				older.remove(dto.getAppellation());
			} else {
				creators.add(createApiFromDto(dto, appId, operatorId));
			}
		});
		older.forEach((name, api) -> deleters.add(api.getId()));
	}

	private void checkApiDto(ApiDto dto) {
		ParamHelper.checkValue(dto.getRouter(), Constant.TEXT_MAX_LENGTH);
		ParamHelper.checkValue(dto.getAccessRule(),
				RuleListEnum.BLACK.getCode(), RuleListEnum.BLANK.getCode());
		ParamHelper.checkValue(dto.getAppellation(), Constant.TEXT_MAX_LENGTH);
	}

	private boolean needUpdate(Api api, ApiDto dto) {
		boolean update = false;
		if (dto.getRouter().equals(api.getRouter()) == false) {
			api.setRouter(dto.getRouter());
			update = true;
		}
		if (dto.getAccessRule().equals(api.getAccessRule()) == false) {
			api.setAccessRule(dto.getAccessRule());
			update = true;
		}
		if (update) {
			api.setAppId(null);
			api.setCreatorId(null);
			api.setCreateTime(null);
			api.setAppellation(null);
			api.setDescription(null);
		}
		return update;
	}

	private Api createApiFromDto(ApiDto dto, String appId, String creatorId) {
		Api api = new Api();
		api.setAppId(appId);
		api.setRouter(dto.getRouter());
		api.setAccessRule(dto.getAccessRule());
		api.setAppellation(dto.getAppellation());
		api.setCreateFields(creatorId);
		return api;
	}

	private ApiVo getApiVo(Api api) {
		ApiVo vo = new ApiVo();
		BeanUtil.copyProperties(api, vo);
		return vo;
	}

}
