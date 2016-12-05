package org.Iris.redis;

public class RedisMsgUtil {

	public static boolean isOK(String value) {
		return null == value ? null : value.equals("OK");
	}
}
