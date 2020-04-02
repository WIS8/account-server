package cn.wis.account.model.result;

public enum ResultEnum {

	SUCCESS(true, "account-10000", "成功"),
	FAILURE(false, "account-9999", "失败"),
	INHIBIT(false, "account-1111", "权限不足"),
	UNKNOWN(false, "account-5555", "未知错误"),
	ILLEGAL(false, "account-2222", "参数非法"),
	SERVICE(false, "account-4444", "服务异常"),

	INSERT_ERROR(false, "account-10001", "数据库插入错误"),
	UPDATE_ERROR(false, "account-10002", "数据库更新错误"),
	DELETE_ERROR(false, "account-10003", "数据库删除错误"),
	REPEAT_ERROR(false, "account-10004", "数据库键值冲突"),
	NO_KEY_ERROR(false, "account-10005", "数据库键值无效"),
	ACTION_ERROR(false, "account-10006", "数据库操作错误"),

	PAUSED_REGISTER(false, "account-10010", "注册数已满"),
	REPEAT_NICKNAME(false, "account-10011", "昵称重复"),
	NO_SUCH_ACCOUNT(false, "account-10012", "账号不存在"),
	BAD_LOGIN_INFOS(false, "account-10013", "密码不匹配"),
	MAX_LOGIN_COUNT(false, "account-10014", "登录用户数溢出"),
	EXPIRED_COOKIE(false, "account-10015", "cookie过期"),
	INVALID_COOKIE(false, "account-10016", "cookie无效"),
	WITHOUT_COOKIE(false, "account-10017", "cookie缺失"),
	REPEAT_IN_LOGIN(false, "account-10018", "账号重复登录"),

	;

	private final Boolean success;

	private final String code;

	private final String message;

	private ResultEnum(Boolean success, String code, String message) {
		this.success = success;
		this.code = code;
		this.message = message;
	}

	public Boolean getSuccess() {
		return success;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
