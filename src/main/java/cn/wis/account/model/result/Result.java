package cn.wis.account.model.result;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class Result {

    public static Result getResult(ResultEnum resultEnum, String message) {
    	Result result = new Result();
		result.success = resultEnum.getSuccess();
		result.code = resultEnum.getCode();
		result.message = StrUtil.isEmpty(message)
				? resultEnum.getMessage() : message;
        return result;
    }

	public static Result getResult(ResultEnum resultEnum) {
		Result result = new Result();
		result.success = resultEnum.getSuccess();
		result.code = resultEnum.getCode();
		result.message = resultEnum.getMessage();
		return result;
	}

    public static Result trueResult(Object data) {
    	Result result = new Result();
		result.success = ResultEnum.SUCCESS.getSuccess();
		result.code = ResultEnum.SUCCESS.getCode();
		result.message = ResultEnum.SUCCESS.getMessage();
		result.data = data;
		return result;
    }

    public static Result trueResult() {
		return getResult(ResultEnum.SUCCESS);
    }

	private Boolean success;

	private String code;

	private String message;

	private Object data;

	private Result() {}

}
