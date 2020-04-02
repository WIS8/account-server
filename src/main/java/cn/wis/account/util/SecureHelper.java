package cn.wis.account.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

public final class SecureHelper {

	public static String encryptPassword(String password) {
		if (StrUtil.isEmpty(password)) {
			throw new IllegalArgumentException("The password is empty");
		}
		return SecureUtil.md5(password);
	}

	public static boolean isDifferent(String cipher, String password) {
		if (StrUtil.isEmpty(cipher)) {
			throw new IllegalArgumentException("The cipher is empty");
		}
		return cipher.equals(SecureUtil.md5(password)) == false;
	}

	private SecureHelper() {
		throw new RuntimeException("There isn't instance of secure util for you");
	}

}
