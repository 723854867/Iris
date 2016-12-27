package org.Iris.app.jilu.storage.redis;

import java.text.MessageFormat;

import org.Iris.app.jilu.common.model.AccountType;

public final class CommonKeyGenerator {
	
	private static final String ACCOUNT_CAPTCHA						= "string:tmp:account:{0}:{1}:captcha";					// 账号 - 验证码 对应关系；0-表示账号类型，1-表示账号值
	private static final String ACCOUNT_CAPTCHA_COUNT				= "string:tmp:account:{0}:{1}:captcha:count";			// 账号 - 验证码获取次数 对应关系；0-表示账号类型，1-表示账号值

	private static final String CREATE_MARK_DATA					= "hash:tmp:create:{0}:mark";							// 登录失败之后产生一个临时token，有效期三分钟，token保存该登录的账号信息,在创建商户时使用		
	
	public static final String accountCaptchaKey(AccountType type, String account) {
		return MessageFormat.format(ACCOUNT_CAPTCHA, type.name().toLowerCase(), account);
	}
	
	public static final String accountCaptchaCountKey(AccountType type, String account){
		return MessageFormat.format(ACCOUNT_CAPTCHA_COUNT, type.name().toLowerCase(), account);
	}
	
	// *********************************************************************************************

	public static final String createMarkDataKey(String token) { 
		return MessageFormat.format(CREATE_MARK_DATA, token);
	}
	
	private static final String RELATION_LOCK_KEY					= "string:relation:{0}:lock";							// 用户关系锁，关系锁是双方共有
	
	private static final String RELATION_DATA						= "hash:cache:relation:{0}";							// 所有关系数据
	
	public static final String relationLockKey(String id) {
		return MessageFormat.format(RELATION_LOCK_KEY, id);
	}
		
	// *********************************************************************************************
	
	public static final String relationDataKey(String relationId) { 
		return MessageFormat.format(RELATION_DATA, relationId);
	}
	
	private static final String MEM_GOODS							= "hash:db:mem:{0}:goods";					
	
	public static String getMemGoodsKey(long goodsId){
		return MessageFormat.format(MEM_GOODS, goodsId);
	}
}
