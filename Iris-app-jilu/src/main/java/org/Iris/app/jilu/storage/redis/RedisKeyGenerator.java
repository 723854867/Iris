package org.Iris.app.jilu.storage.redis;

import java.text.MessageFormat;

import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.realm.unit.UnitType;

public class RedisKeyGenerator {

	private static final String LOCK_UNIT							= "string:unit:{0}:{1}:lock";				// 用户分布式锁；0-表示用户类型，1-表示用户Id
	private static final String ACCOUNT_CAPTCHA						= "string:account:{0}:{1}:captcha";			// 账号 - 验证码 对应关系；0-表示账号类型，1-表示账号值
	private static final String ACCOUNT_CAPTCHA_COUNT				= "string:account:{0}:{1}:captcha:count";	// 账号 - 验证码获取次数 对应关系；0-表示账号类型，1-表示账号值
	private static final String TOKEN_MERCHANT_ID					= "string:merchant:{0}:token";				// token merchantId 对应关系
	
	private static final String REGISTER_TOKEN_DATA					= "hash:account:{0}:create:token";			// 登录失败之后产生一个临时token，有效期三分钟，token保存该登录的账号信息,在创建商户时使用		
	private static final String ALIYUN_STS_DATA						= "hash:merchant:{0}:aliyun:sts:info";		// 阿里云 sts 缓存的临时 token 信息
	
	private static final String MEM_MERCHANT_DATA					= "hash:db:mem:{0}:merchant";
	private static final String MEM_ACCOUNT_DATA					= "hash:db:mem:{0}:account";	
	private static final String MEM_CUSTOMER_DATA					= "hash:db:mem:{0}:customer";
	
	private static final String MEM_ORDER_DATA						= "hash:db:mem:{0}:order";
	private static final String MEM_ORDER_GOODS						= "hash:db:mem:{0}:{1}:order:goods";			//0代表 orderId 1 代表goodsId
	private static final String MEM_ORDER_GOODS_SET					= "list:db:mem:{0}:order:goods";				//订单 id与商品id对应关系 一对多
	private static final String MEM_GOODS							= "hash:db:mem:{0}:goods";					
	private static final String CUSTOMER_LIST_PURCHASE_FREQUENCY	= "zset:merchant:{0}:customer:purchase:frequency";			// 商户所属客户列表 - 购买频率排序
	private static final String CUSTOMER_LIST_PURCHASE_SUM			= "zset:merchant:{0}:customer:purchase:sum";				// 商户所属客户列表 - 购物总金额排序
	private static final String CUSTOMER_LIST_PURCHASE_RECENT		= "zset:merchant:{0}:customer:list:purchase:recent";		// 商户所属客户列表 - 最近购物时间排序
	private static final String CUSTOMER_LIST_NAME					= "zset:merchant:{0}:customer:list:name";					// 商户所属客户列表 - 名字排序列表

	
	public static String getUnitLockKey(UnitType type, long uid) { 
		return MessageFormat.format(LOCK_UNIT, type.name(), String.valueOf(uid));
	}
	
	public static String getAccountCaptchaKey(AccountType type, String account) {
		return MessageFormat.format(ACCOUNT_CAPTCHA, type.name().toLowerCase(), account);
	}
	
	public static String getAccountCaptchaCountKey(AccountType type, String account) { 
		return MessageFormat.format(ACCOUNT_CAPTCHA_COUNT, type.name().toLowerCase(), account);
	}
	
	public static String getMerchantTokenKey(String token) { 
		return MessageFormat.format(TOKEN_MERCHANT_ID, token);
	}
	
	public static String getRegisterTokenDataKey(String token) { 
		return MessageFormat.format(REGISTER_TOKEN_DATA, token);
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
	
	public static String getMemCustomerDataKey(long customerId) {
		return MessageFormat.format(MEM_CUSTOMER_DATA, String.valueOf(customerId));
	}
	
	public static String getMemOrderDataKey(String orderId){
		return MessageFormat.format(MEM_ORDER_DATA, orderId);
	}
	public static String getCustomerListPurchaseFrequencyKey(long merchantId) { 
		return MessageFormat.format(CUSTOMER_LIST_PURCHASE_FREQUENCY, String.valueOf(merchantId));
	}
	
	public static String getCustomerListPurchaseSumKey(long merchantId) { 
		return MessageFormat.format(CUSTOMER_LIST_PURCHASE_SUM, String.valueOf(merchantId));
	}
	
	public static String getCustomerListPurchaseRecentKey(long merchantId) { 
		return MessageFormat.format(CUSTOMER_LIST_PURCHASE_RECENT, String.valueOf(merchantId));
	}
	
	public static String getCustomerListNameKey(long merchantId) { 
		return MessageFormat.format(CUSTOMER_LIST_NAME, String.valueOf(merchantId));
	}
	
	public static String getMemOrderGoodsDataKey(String orderId,long goodsId){
		return MessageFormat.format(MEM_ORDER_GOODS, orderId, String.valueOf(goodsId));
	}
	
	public static String getMemOrderGoodsSetKey(String orderId){
		return MessageFormat.format(MEM_ORDER_GOODS_SET, orderId);
	}
	
	public static String getMemGoodsKey(long goodsId){
		return MessageFormat.format(MEM_GOODS, goodsId);
	}
}
