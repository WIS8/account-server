package cn.wis.account.util;

public enum TimeUnit {

	MILLIS(1, "ms"),

	SECOND(1000, "s"),

	MINUTE(1000 * 60, "m"),

	HOUR(1000 * 60 * 60, "h"),

	DAY(1000 * 60 * 60 * 24, "d"),

	MONTH(1000 * 60 * 60 * 24 * 30, "M"),

	YEAR(1000 * 60 * 60 * 24 * 365, "y");

	private final long size;

	private final String unit;

	private TimeUnit(int size, String unit) {
		this.size = size;
		this.unit = unit;
	}

	public long convert(long millis) {
		return millis / size;
	}

	public long millis(long time) {
		return time * size;
	}

	public String getUnit() {
		return unit;
	}

	public long one() {
		return size;
	}

}
