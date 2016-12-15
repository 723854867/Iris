package org.Iris.app.jilu.common;

public class AliyunUtils {
	
	private static final String AVATAR_PATH						= "common/avatar";

	public static String getAvatar(String avatar) { 
		StringBuilder builder = new StringBuilder();
		builder.append(AppConfig.getAliyunOssEndpoint())
				.append(AppConfig.getAliyunOssBucket())
				.append(AVATAR_PATH).append(avatar);
		return builder.toString();
	}
}
