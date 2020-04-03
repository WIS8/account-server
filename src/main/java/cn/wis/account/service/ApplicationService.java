package cn.wis.account.service;

import java.util.List;

import cn.wis.account.base.BaseService;
import cn.wis.account.model.dto.ApplicationDto;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.request.app.ApplicationUpdateRequest;
import cn.wis.account.model.request.feign.ApplicationConfirmRequest;
import cn.wis.account.model.vo.AppVo;
import cn.wis.account.model.vo.PageVo;

public interface ApplicationService extends BaseService {

	ApplicationDto confirm(ApplicationConfirmRequest request);

	void delete(String appId);

	void update(ApplicationUpdateRequest request);

	PageVo search(Page page);

	List<AppVo> searchAllMineApp();

}
