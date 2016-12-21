package org.Iris.app.jilu.storage.redis;

import java.text.MessageFormat;

/**
 * 和商户相关的所有主键，主键中有商户的 ID
 * 
 * @author Ahab
 */
public final class MerchantKeyGenerator {

	private static final String MERCHANT_DATA							= "hash:db:merchant:{0}";
	private static final String MERCHANT_ACCOUNT_DATA					= "hash:db:merchant:{0}:account:{1}";
	private static final String MERCHANT_CUSTOMER_DATA					= "hash:db:merchant:{0}:customer:{1}";
	private static final String MERCHANT_ORDER_DATA						= "hash:db:merchant:{0}:order:{1}";
	private static final String MERCHANT_ORDER_GOODS_DATA				= "hash:db:merchant:{0}:order:goods:{1}";
	private static final String MERCHANT_ORDER_PACKET_DATA				= "hash:db:merchant:{0}:order:packet:{1}";
	
	public static final String merchantDataKey(long merchantId) { 
		return MessageFormat.format(MERCHANT_DATA, String.valueOf(merchantId));
	}
	
	public static final String merchantAccountDataKey(long merchantId, String account) { 
		return MessageFormat.format(MERCHANT_ACCOUNT_DATA, String.valueOf(merchantId), account);
	}
	
	public static final String merchantCustomerDataKey(long merchantId, long customerId) { 
		return MessageFormat.format(MERCHANT_CUSTOMER_DATA, String.valueOf(merchantId), String.valueOf(customerId));
	}
	
	public static final String merchantOrderDataKey(long merchantId, String orderId) { 
		return MessageFormat.format(MERCHANT_ORDER_DATA, String.valueOf(merchantId), String.valueOf(orderId));
	}
	
	public static final String merchantOrderGoodsDataKey(long merchantId, long goodsId) { 
		return MessageFormat.format(MERCHANT_ORDER_GOODS_DATA, String.valueOf(merchantId), String.valueOf(goodsId));
	}
	
	public static final String merchantOrderPacketDataKey(long merchantId, long packetId) { 
		return MessageFormat.format(MERCHANT_ORDER_PACKET_DATA, String.valueOf(merchantId), String.valueOf(packetId));
	}
}
