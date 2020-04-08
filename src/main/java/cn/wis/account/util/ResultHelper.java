package cn.wis.account.util;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.table.AbstractTable;
import cn.wis.account.model.vo.PageVo;

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

	@SuppressWarnings("unchecked")
	public static <T> PageVo translate(List<List<?>> result, Function<T, ?> conveter) {
		PageVo pageVo = new PageVo();
		if (result == null || result.size() < 2) {
			pageVo.setData(Collections.EMPTY_LIST);
			pageVo.setTotal(0);
		} else {
			pageVo.setData(((List<T>) result.get(0))
					.stream().map(conveter).collect(Collectors.toList()));
			pageVo.setTotal((Integer) result.get(1).get(0));
		}
		return pageVo;
	}

	private ResultHelper() {
		throw new RuntimeException("There isn't instance of result util for you");
	}

}
