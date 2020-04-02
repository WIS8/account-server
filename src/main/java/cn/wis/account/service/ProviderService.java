package cn.wis.account.service;

import cn.wis.account.base.BaseService;
import cn.wis.account.model.dto.ProviderDto;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.request.feign.ProviderConfirmRequest;
import cn.wis.account.model.request.provider.ProviderUpdateRequest;
import cn.wis.account.model.vo.PageVo;

public interface ProviderService extends BaseService {

	ProviderDto confirm(ProviderConfirmRequest request);

	void delete(String providerId);

	void update(ProviderUpdateRequest request);

	PageVo search(Page page);

}
