package org.Iris.app.jilu.storage.redis;

import java.text.MessageFormat;

import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.realm.unit.UnitType;

public class RedisKeyGenerator {

	private static final String LOCK_UNIT							= "string:lock:unit:{0}:{1}";				// 用户分布式锁；0-表示用户类型，1-表示用户Id
	private static final String ACCOUNT_CAPTCHA						= "string:captcha:account:{0}:{1}";			// 账号 - 验证码 对应关系；0-表示账号类型，1-表示账号值
	private static final String ACCOUNT_CAPTCHA_COUNT				= "string:captcha:account:count:{0}:{1}";	// 账号 - 验证码获取次数 对应关系；0-表示账号类型，1-表示账号值
	private static final String TOKEN_UID							= "string:token:uid:{0}";					// token uid 对应关系
	
	private static final String TOKEN_ACCOUNT						= "hash:token:account:{0}";					
	private static final String ALIYUN_STS_DATA						= "hash:aliyun:sts:token:{0}";				// 阿里云 sts 缓存的临时 token 信息
	
	private static final String MEM_MERCHANT_DATA					= "hash:db:mem:merchant:{0}";
	private static final String MEM_ACCOUNT_DATA					= "hash:db:mem:account:{0}";				
	
	private static final String ORDER_DATA							= "hash:db:order:{0}";
	private static final String ORDER_GOODS							= "hash:db:order:goods:{0}:{1}";			//0代表 orderId 1 代表goodsId
	private static final String ORDER_GOODS_SET						= "list:db:order:goods:{0}";				//订单 id与商品id对应关系 一对多
	
	public static String getUnitLockKey(UnitType type, long uid) { 
		return MessageFormat.format(LOCK_UNIT, type.name(), String.valueOf(uid));
	}
	
	public static String getAccountCaptchaKey(AccountType type, String account) {
		return MessageFormat.format(ACCOUNT_CAPTCHA, type.name().toLowerCase(), account);
	}
	
	public static String getAccountCaptchaCountKey(AccountType type, String account) { 
		return MessageFormat.format(ACCOUNT_CAPTCHA_COUNT, type.name().toLowerCase(), account);
	}
	
	public static String getTokenUidKey(String token) { 
		return MessageFormat.format(TOKEN_UID, token);
	}
	
	public static String getTokenAccountKey(String token) { 
		return MessageFormat.format(TOKEN_ACCOUNT, token);
	}
	
	public static String getAliyunStsDataKey(long uid) { 
		return MessageFormat.format(ALIYUN_STS_DATA, String.valueOf(uid));
	}
	
	public static String getMemMerchantDataKey(long merchantId) { 
		return MessageFormat.format(MEM_MERCHANT_DATA, String.valueOf(merchantId));
	}
	
	public static String getMemAccountDataKey(String account) { 
		return MessageFormat.format(MEM_ACCOUNT_DATA, account);
	}
	
	public static String getOrderDataKey(String orderId){
		return MessageFormat.format(ORDER_DATA, orderId);
	}
	
	public static String getOrderGoodsDataKey(String orderId,long goodsId){
		return MessageFormat.format(ORDER_GOODS, orderId,String.valueOf(goodsId));
	}
	
	public static String getOrderGoodsSetKey(String orderId){
		return MessageFormat.format(ORDER_GOODS_SET, orderId);
	}
}
