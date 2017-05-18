package org.Iris.app.jilu.storage.redis;

import java.text.MessageFormat;

import org.Iris.app.jilu.common.model.AccountType;

public final class WebKeyGenerator {
	
	private static final String ACCOUNT_CAPTCHA						= "string:tmp:account:web:{0}:{1}:captcha";					// 账号 - 验证码 对应关系；0-表示账号类型，1-表示账号值
	private static final String ACCOUNT_CAPTCHA_COUNT				= "string:tmp:account:web:{0}:{1}:captcha:count";			// 账号 - 验证码获取次数 对应关系；0-表示账号类型，1-表示账号值
	private static final String MERCHANT_DATA						= "hash:db:merchant:web:{0}";
	private static final String TOKEN_MERCHANT_MAP					= "hash:cache:token:merchant:web";							// token - merchant 映射
	
	public static final String accountCaptchaKey(AccountType type, String account) {
		return MessageFormat.format(ACCOUNT_CAPTCHA, type.name().toLowerCase(), account);
	}
	
	public static final String accountCaptchaCountKey(AccountType type, String account){
		return MessageFormat.format(ACCOUNT_CAPTCHA_COUNT, type.name().toLowerCase(), account);
	}
	
	public static final String merchantDataKey(long merchantId) { 
		return MessageFormat.format(MERCHANT_DATA, String.valueOf(merchantId));
	}
	
	public static final String tokenMerchantMapKey() { 
		return TOKEN_MERCHANT_MAP;
	}
	
}
