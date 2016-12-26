package org.Iris.app.jilu.storage.redis;

import java.text.MessageFormat;

import org.Iris.app.jilu.common.model.AccountType;

/**
 * 和商户相关的所有主键，主键中有商户的 ID
 * 
 * @author Ahab
 */
public final class MerchantKeyGenerator {
	
	private static final String MERCHANT_LOCK							= "string:cache:merchant:{0}:lock";								// 商户操作锁
	
	private static final String ACCOUNT_MERCHANT_MAP					= "hash:cache:account:{0}:merchant";							// account - merchant 映射
	private static final String TOKEN_MERCHANT_MAP						= "hash:cache:token:merchant";									// token - merchant 映射
	
	private static final String MERCHANT_DATA							= "hash:db:merchant:{0}";
	private static final String MERCHANT_ACCOUNT_DATA					= "hash:db:merchant:{0}:account";
	
	public static final String merchantLockKey(long merchantId) {
		return MessageFormat.format(MERCHANT_LOCK, String.valueOf(merchantId));
	}
	
	// **********************************************************************************************
	
	public static final String accountMerchantMapKey(AccountType type) { 
		return MessageFormat.format(ACCOUNT_MERCHANT_MAP, type.name());
	}
	
	public static final String tokenMerchantMapKey() { 
		return TOKEN_MERCHANT_MAP;
	}
	
	public static final String merchantDataKey(long merchantId) { 
		return MessageFormat.format(MERCHANT_DATA, String.valueOf(merchantId));
	}
	
	public static final String accountDataKey(long merchantId) { 
		return MessageFormat.format(MERCHANT_ACCOUNT_DATA, String.valueOf(merchantId));
	}
	
	private static final String CUSTOMER_LIST_PURCHASE_FREQUENCY		= "zset:tmp:merchant:{0}:customer:list:purchase:frequency";		// 商户所属客户列表 - 购买频率排序
	private static final String CUSTOMER_LIST_PURCHASE_SUM				= "zset:cache:merchant:{0}:customer:list:purchase:sum";			// 商户所属客户列表 - 购物总金额排序
	private static final String CUSTOMER_LIST_PURCHASE_RECENT			= "zset:cache:merchant:{0}:customer:list:purchase:recent";		// 商户所属客户列表 - 最近购物时间排序
	private static final String CUSTOMER_LIST_NAME						= "zset:cache:merchant:{0}:customer:list:name";					// 商户所属客户列表 - 名字排序列表

	private static final String MERCHANT_CUSTOMER_DATA					= "hash:db:merchant:{0}:customer:{1}";
	private static final String MERCHANT_ORDER_DATA						= "hash:db:merchant:{0}:order:{1}";
	private static final String MERCHANT_ORDER_GOODS_DATA				= "hash:db:merchant:order:{0}:goods:{1}";
	private static final String MERCHANT_ORDER_PACKET_DATA				= "hash:db:merchant:{0}:order:packet:{1}";
	
	
	private static final String ALIYUN_STS_TOKEN_DATA					= "hash:tmp:merchant:{0}:aliyun:sts:info";				// 阿里云 sts 缓存的临时 token 信息
	
	public static final String customerListPurchaseFrequencyKey(long merchantId) { 
		return MessageFormat.format(CUSTOMER_LIST_PURCHASE_FREQUENCY, String.valueOf(merchantId));
	}
	
	public static final String customerListPurchaseSumKey(long merchantId) { 
		return MessageFormat.format(CUSTOMER_LIST_PURCHASE_SUM, String.valueOf(merchantId));
	}
	
	public static final String customerListPurchaseRecentKey(long merchantId) { 
		return MessageFormat.format(CUSTOMER_LIST_PURCHASE_RECENT, String.valueOf(merchantId));
	}
	
	public static final String customerListNameKey(long merchantId) { 
		return MessageFormat.format(CUSTOMER_LIST_NAME, String.valueOf(merchantId));
	}
	
	// **********************************************************************************************
	
	public static final String merchantCustomerDataKey(long merchantId, long customerId) { 
		return MessageFormat.format(MERCHANT_CUSTOMER_DATA, String.valueOf(merchantId), String.valueOf(customerId));
	}
	
	public static final String merchantOrderDataKey(long merchantId, String orderId) { 
		return MessageFormat.format(MERCHANT_ORDER_DATA, String.valueOf(merchantId), String.valueOf(orderId));
	}
	
	public static final String merchantOrderGoodsDataKey(String orderId, long goodsId) { 
		return MessageFormat.format(MERCHANT_ORDER_GOODS_DATA, orderId, String.valueOf(goodsId));
	}
	
	public static final String merchantOrderPacketDataKey(long merchantId, String packetId) { 
		return MessageFormat.format(MERCHANT_ORDER_PACKET_DATA, String.valueOf(merchantId), packetId);
	}
	
	public static String aliyunStsTokenDataKey(long merchantId) { 
		return MessageFormat.format(ALIYUN_STS_TOKEN_DATA, String.valueOf(merchantId));
	}
}
