package cn.wis.account.config;

import cn.wis.account.util.TimeUnit;

public interface Constant {

	public final static int DEFAULT_MAX_COUNT_OF_TOKEN = 128;
	public final static long DEFAULT_DURATION_OF_TOKEN = TimeUnit.MINUTE.millis(10);
	public final static long MINIMUM_DURATION_OF_TOKEN = TimeUnit.MINUTE.millis(5);

	// GMT(h)
	public final static int TIME_ZONE = 8;

	public final static int COOKIE_SIZE = 32;

	public final static int ROLE_MAX_COUNT = 1024;

	public final static int PAGE_MAX_SIZE = 256;
	public final static int PAGE_DEFAULT_SIZE = 10;

	public final static int TEXT_MAX_LENGTH = 255;
	public final static int NAME_MAX_LENGTH = 32;

	public final static String PROVIDER_ID = "0000";

	public final static String COOKIE_NAME = "cookie";
	public final static String MEMBER_INFO = "login-member";

	public final static String VISIT_ROLE = "0000";
	public final static String PLAIN_ROLE = "1111";
	public final static String VISITOR_ID = "----";

	public final static String REGIX_NICKNAME = "^[a-zA-Z0-9_\u4e00-\u9fa5]{1,16}$";
	public final static String REGIX_PASSWORD = "^[a-zA-Z0-9_]{6,16}$";

	public final static String REGEX_GENERAL_NAME = "^[\\w\\-\\.#]{1,32}$";
	public final static String REGEX_GENERAL_NAME_ELASTIC = "^[\\w\\-\\.#]+$";
	public final static String REGEX_IDENTIFIER = "^\\w{1,32}$";
	public final static String REGEX_IDENTIFIER_ELASTIC = "^\\w+$";
	public final static String REGEX_ID = "^[0-9a-f]{8,32}$";
	public final static String REGEX_ID_ELASTIC = "^[0-9a-f]+$";

}
