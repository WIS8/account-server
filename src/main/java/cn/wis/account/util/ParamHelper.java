package cn.wis.account.util;

import java.util.Collection;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.wis.account.config.Constant;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;

public final class ParamHelper {

	public static void checkValue(String value) {
		if (StrUtil.isEmpty(value)) {
			throw new ServiceException(ResultEnum.ILLEGAL);
		}
	}

	public static void checkValue(String value, String regex) {
		if (StrUtil.isEmpty(value)
				|| StrUtil.isEmpty(regex)
				|| value.matches(regex) == false) {
			throw new ServiceException(ResultEnum.ILLEGAL);
		}
	}

	public static void checkValue(String value, int length) {
		if (StrUtil.length(value) > length) {
			throw new ServiceException(ResultEnum.ILLEGAL);
		}
	}

	public static void checkValue(Integer value, int min, int max) {
		if (value == null || min > max || value < min || value > max) {
			throw new ServiceException(ResultEnum.ILLEGAL);
		}
	}

	public static <T> void checkValue(Collection<T> value) {
		if (CollectionUtil.isEmpty(value)) {
			throw new ServiceException(ResultEnum.ILLEGAL);
		}
	}

	public static void checkOrSetDefaultValue(Page page) {
		if (page.getIndex() == null) {
			page.setIndex(1);
		} else {
			checkValue(page.getIndex(), 1, Integer.MAX_VALUE);
		}
		if (page.getSize() == null) {
			page.setSize(Constant.PAGE_DEFAULT_SIZE);
		} else {
			checkValue(page.getSize(), 1, Constant.PAGE_MAX_SIZE);
		}
	}

	private ParamHelper() {
		throw new RuntimeException("There isn't instance of parameter util for you");
	}

}
