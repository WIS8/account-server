package cn.wis.account.service;

import java.util.Map;

import cn.wis.account.base.BaseService;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.request.param.ParamUpdateRequest;
import cn.wis.account.model.vo.PageVo;

public interface ParameterService extends BaseService {

	void update(ParamUpdateRequest request);

	Map<String, String> confirm(String providerId);

	PageVo search(Page page);

}
