package cn.wis.account.util;

import cn.hutool.core.util.StrUtil;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.table.AbstractTable;

public final class ResultHelper {

	public static boolean notAn(AbstractTable entity) {
		return entity == null || StrUtil.isEmpty(entity.getId());
	}

	public static boolean exist(AbstractTable entity) {
		return entity != null && StrUtil.isNotEmpty(entity.getId());
	}

	public static void hope(boolean result, boolean wish) {
		if (result != wish) {
			throw new ServiceException(ResultEnum.SERVICE);
		}
	}

	public static boolean fail(boolean result) {
		return result == false;
	}

	private ResultHelper() {
		throw new RuntimeException("There isn't instance of result util for you");
	}

}
