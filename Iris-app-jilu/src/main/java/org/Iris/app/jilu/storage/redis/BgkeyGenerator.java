package org.Iris.app.jilu.storage.redis;

public final class BgkeyGenerator {

	private static final String BG_USER_DATA						= "bg:hash:user:data";					// 后台用户
	
	public static final String bgUserDataKey() {
		return BG_USER_DATA;
	}
}
