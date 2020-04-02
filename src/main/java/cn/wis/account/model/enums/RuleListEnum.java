package cn.wis.account.model.enums;

public enum RuleListEnum {

	BLACK(0, "black-list", false),

	WHITE(1, "white-list", true),

	BLANK(2, "blank-list", true);

	private final int code;

	private final String name;

	private final boolean allow;

	private RuleListEnum(int code, String name, boolean allow) {
		this.code = code;
		this.name = name;
		this.allow = allow;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public boolean isAllow(boolean inList) {
		return allow ? inList : inList == false;
	}

}
