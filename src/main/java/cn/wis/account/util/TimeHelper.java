package cn.wis.account.util;

import cn.wis.account.config.Constant;

public final class TimeHelper {

	public static long getDeltaTime() {
		return TimeUnit.HOUR.millis(Constant.TIME_ZONE);
	}

	public static long millis(long time, TimeUnit unit) {
		if (unit == null) {
			throw new NullPointerException("Time unit is null");
		}
		if (time <= 0) {
			throw new IllegalArgumentException("Time must be positive");
		}
		long temp = unit.millis(time);
		if (temp < time) {
			throw new RuntimeException("Value overflows when converting to millis");
		}
		return temp;
	}

	public static long number(long time, TimeUnit unit) {
		if (unit == null) {
			throw new NullPointerException("Time unit is null");
		}
		if (time <= 0) {
			throw new IllegalArgumentException("Time must be positive");
		}
		return unit.convert(time);
	}

	private TimeHelper() {
		throw new RuntimeException("There isn't instance of time util for you");
	}

}
