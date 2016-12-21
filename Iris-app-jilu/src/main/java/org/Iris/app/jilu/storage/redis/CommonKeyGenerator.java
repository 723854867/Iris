package org.Iris.app.jilu.storage.redis;

import java.text.MessageFormat;

import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.realm.unit.UnitType;

public final class CommonKeyGenerator {
	
	private static final String ACCOUNT_CAPTCHA						= "string:tmp:account:{0}:{1}:captcha";					// 账号 - 验证码 对应关系；0-表示账号类型，1-表示账号值
	private static final String ACCOUNT_CAPTCHA_COUNT				= "string:tmp:account:{0}:{1}:captcha:count";			// 账号 - 验证码获取次数 对应关系；0-表示账号类型，1-表示账号值
	private static final String TOKEN_MERCHANT_ID					= "string:cache:token:{0}:merchant_id";					// token - merchantId 对应关系
	private static final String CUSTOMER_LIST_LOAD_TIME				= "string:cache:merchant:{0}:customer:list:load:time";	// 商户客户列表刷新时间
	
	private static final String ACCOUNT_MERCHANT_ID_MAP				= "hash:cache:account:{0}:merchant_id";					// account - merchant 映射
	private static final String CREATE_MARK_DATA					= "hash:tmp:create:{0}:mark";							// 登录失败之后产生一个临时token，有效期三分钟，token保存该登录的账号信息,在创建商户时使用		
	
	public static final String accountCaptchaKey(AccountType type, String account) {
		return MessageFormat.format(ACCOUNT_CAPTCHA, type.name().toLowerCase(), account);
	}
	
	public static final String accountCaptchaCountKey(AccountType type, String account){
		return MessageFormat.format(ACCOUNT_CAPTCHA_COUNT, type.name().toLowerCase(), account);
	}
	
	public static final String tokenMerchantIdKey(String token) {
		return MessageFormat.format(TOKEN_MERCHANT_ID, token);
	}
	
	public static String customerListLoadTimeKey(long merchantId) {
		return MessageFormat.format(CUSTOMER_LIST_LOAD_TIME, String.valueOf(merchantId));
	}
	
	// *********************************************************************************************
	
	public static final String accountMerchantIdMapKey(AccountType type) { 
		return MessageFormat.format(ACCOUNT_MERCHANT_ID_MAP, type.name().toLowerCase());
	}
	
	public static String createMarkDataKey(String token) { 
		return MessageFormat.format(CREATE_MARK_DATA, token);
	}
	
	private static final String LOCK_UNIT							= "string:unit:{0}:{1}:lock";				// 用户分布式锁；0-表示用户类型，1-表示用户Id
	
	private static final String MEM_ACCOUNT_DATA					= "hash:db:mem:{0}:account";	
	
	private static final String MEM_ORDER_DATA						= "hash:db:mem:{0}:order";
	private static final String MEM_ORDER_GOODS						= "hash:db:mem:{0}:{1}:order:goods";			//0代表 orderId 1 代表goodsId
	private static final String MEM_ORDER_GOODS_SET					= "list:db:mem:{0}:order:goods";				//订单 id与商品id对应关系 一对多
	private static final String MEM_GOODS							= "hash:db:mem:{0}:goods";					
	
	public static String getUnitLockKey(UnitType type, long uid) { 
		return MessageFormat.format(LOCK_UNIT, type.name(), String.valueOf(uid));
	}
	
	public static String getMemAccountDataKey(String account) { 
		return MessageFormat.format(MEM_ACCOUNT_DATA, account);
	}
	
	public static String getMemOrderDataKey(String orderId){
		return MessageFormat.format(MEM_ORDER_DATA, orderId);
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
