package org.Iris.app.jilu.common.bean.enums;

import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;

/**
 * 客户列表类型
 * 
 * @author ahab
 */
public enum CustomerListType {

	NAME(0) {
		@Override
		public String redisCustomerListKey(long merchantId) {
			return RedisKeyGenerator.getCustomerListNameKey(merchantId);
		}
	},
	
	PURCHASE_SUM(1) {
		@Override
		public String redisCustomerListKey(long merchantId) {
			return RedisKeyGenerator.getCustomerListPurchaseSumKey(merchantId);
		}
	},
	
	PURCHASE_RECENT(2) {
		@Override
		public String redisCustomerListKey(long merchantId) {
			return RedisKeyGenerator.getCustomerListPurchaseRecentKey(merchantId);
		}
	},
	
	PURCHASE_FREQUENCY(3) {
		@Override
		public String redisCustomerListKey(long merchantId) {
			return RedisKeyGenerator.getCustomerListPurchaseFrequencyKey(merchantId);
		}
	};
	
	private int mark;
	
	private CustomerListType(int mark) {
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
	
	public abstract String redisCustomerListKey(long merchantId);
	
	public static final CustomerListType match(int type) {
		for (CustomerListType listType : CustomerListType.values()) {
			if (listType.mark != type)
				continue;
			return listType;
		}
		return null;
	}
}
