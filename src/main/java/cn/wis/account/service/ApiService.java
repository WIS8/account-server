package cn.wis.account.service;

import java.util.List;

import cn.wis.account.base.BaseService;
import cn.wis.account.model.dto.ApiConfirmResult;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.request.feign.ApiConfirmRequest;
import cn.wis.account.model.vo.ApiVo;

public interface ApiService extends BaseService {

	ApiConfirmResult confirm(ApiConfirmRequest request);

	List<ApiVo> searchByProvider(Page page);

}
