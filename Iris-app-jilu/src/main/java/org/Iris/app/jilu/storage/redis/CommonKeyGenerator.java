package org.Iris.app.jilu.storage.redis;

import java.text.MessageFormat;

import org.Iris.app.jilu.common.model.AccountType;

public final class CommonKeyGenerator {
	
	private static final String ACCOUNT_CAPTCHA						= "string:tmp:account:{0}:{1}:captcha";					// 账号 - 验证码 对应关系；0-表示账号类型，1-表示账号值
	private static final String ACCOUNT_CAPTCHA_COUNT				= "string:tmp:account:{0}:{1}:captcha:count";			// 账号 - 验证码获取次数 对应关系；0-表示账号类型，1-表示账号值
	private static final String RELATION_LOCK_KEY					= "string:tmp:relation:{0}:lock";						// 
	
	private static final String RELATION_MAP						= "hash:cache:relation:map";							//  
	
	private static final String WEIXIN_ACCESS_TOKEN					= "string:wenxin:access:token";							// 微信应用access_token
	private static final String WEIXIN_REFRESH_TOKEN				= "string:wenxin:refresh:token";						// 微信应用refresh_token 用于刷新access_token
	
	public static final String accountCaptchaKey(AccountType type, String account) {
		return MessageFormat.format(ACCOUNT_CAPTCHA, type.name().toLowerCase(), account);
	}
	
	public static final String accountCaptchaCountKey(AccountType type, String account){
		return MessageFormat.format(ACCOUNT_CAPTCHA_COUNT, type.name().toLowerCase(), account);
	}
	
	public static final String relationLockKey(String id) {
		return MessageFormat.format(RELATION_LOCK_KEY, id);
	}
	
	// *********************************************************************************************

	public static final String relationMapKey() {
		return RELATION_MAP;
	}
	
	public static final String weiXinAccessTokenKey(){
		return WEIXIN_ACCESS_TOKEN;
	}
	
	public static final String weiXinRefreshTokenKey(){
		return WEIXIN_REFRESH_TOKEN;
	}
	
	private static final String MEM_GOODS							= "hash:db:mem:{0}:goods";					
	
	public static String getMemGoodsKey(long goodsId){
		return MessageFormat.format(MEM_GOODS, goodsId);
	}
}
