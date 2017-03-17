package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.redis.RedisHashBean;
import org.Iris.util.lang.DateUtils;

/**
 * 订单待出库记录
 * @author 樊水东
 * 2017年3月17日
 */
public class MemWaitStore implements RedisHashBean{
	private long id;
	private String orderId;
	private long goodsId;
	private String goodsName;
	private long merchantId;
	private String merchantName;
	private int count;
	private int created;
	private int updated;
	
	public MemWaitStore(String orderId, long goodsId, String goodsName, long merchantId, String merchantName,
			int count) {
		this.orderId = orderId;
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.merchantId = merchantId;
		this.merchantName = merchantName;
		this.count = count;
		int time = DateUtils.currentTime();
		this.created = time;
		this.updated = time;
	}

	public MemWaitStore() {
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getCreated() {
		return created;
	}
	public void setCreated(int created) {
		this.created = created;
	}
	public int getUpdated() {
		return updated;
	}
	public void setUpdated(int updated) {
		this.updated = updated;
	}

	@Override
	public String redisKey() {
		// TODO Auto-generated method stub
		return MerchantKeyGenerator.merchantWaitStoreDataKey(orderId, merchantId, goodsId);
	}
	
	
}
