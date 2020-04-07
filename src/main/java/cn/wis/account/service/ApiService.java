package cn.wis.account.service;

import cn.wis.account.base.BaseService;
import cn.wis.account.model.dto.ApiConfirmResult;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.request.feign.ApiConfirmRequest;
import cn.wis.account.model.vo.PageVo;

public interface ApiService extends BaseService {

	ApiConfirmResult confirm(ApiConfirmRequest request);

	PageVo search(Page page);

}
