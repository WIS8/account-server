package cn.wis.account.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.wis.account.mapper.ApplicationMapper;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.table.Application;
import cn.wis.account.util.ResultHelper;

@Component
public class ApplicationSup {

	@Resource
	private ApplicationMapper appMapper;

	private Map<String, Application> map;

	public ApplicationSup() {
		map = new ConcurrentHashMap<String, Application>();
	}

	public Application checkAppName(String appName) {
		Application app = appMapper.selectByName(appName);
		if (ResultHelper.notAn(app)) {
			throw new ServiceException(ResultEnum.NO_KEY_ERROR, "appName: " + appName);
		}
		return app;
	}

	public Application checkAppId(String appId) {
		Application app = appMapper.selectById(appId);
		if (ResultHelper.notAn(app)) {
			throw new ServiceException(ResultEnum.NO_KEY_ERROR, "appId: " + appId);
		}
		return app;
	}

	public Application checkAppIdInCache(String appId) {
		if (map.containsKey(appId)) {
			return map.get(appId);
		} else {
			Application app = checkAppId(appId);
			map.put(appId, app);
			return app;
		}
	}

}
