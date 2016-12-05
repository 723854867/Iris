package org.Iris.app.jilu.storage.redis;

import java.text.MessageFormat;

import org.Iris.app.jilu.model.AccountType;

public class RedisKeyGenerator {

	private static final String LOCK_USER							= "string:lock:user:{0}";					// 用户分布式锁
	private static final String CAPTCHA								= "string:{0}:captcha:{1}";					// 手机/邮箱 - 验证码
	private static final String CAPTCHA_COUNT						= "string:{0}:captcha:count:{1}";			// 手机/邮箱 - 验证码获取次数
	private static final String GLOBAL_USER_ID						= "string:global-user-id";					// 用户 UID 记录
	
	private static final String MEM_ACCOUNT_DATA					= "hash:db:mem:account:{0}";				
	private static final String MEM_USER_DATA						= "hash:db:mem:user:{0}";
	
	public static String getUserLockKey(long uid) { 
		return MessageFormat.format(LOCK_USER, String.valueOf(uid));
	}
	
	public static String getCaptchaKey(AccountType type, String account) {
		return MessageFormat.format(CAPTCHA, type.name().toLowerCase(), account);
	}
	
	public static String getCaptchaCountKey(AccountType type, String account) { 
		return MessageFormat.format(CAPTCHA_COUNT, type.name().toLowerCase(), account);
	}
	
	public static String getGlobalUserIdKey(){
		return GLOBAL_USER_ID;
	}
	
	public static String getMemUserKey(long uid) { 
		return MessageFormat.format(MEM_USER_DATA, String.valueOf(uid));
	}
}
