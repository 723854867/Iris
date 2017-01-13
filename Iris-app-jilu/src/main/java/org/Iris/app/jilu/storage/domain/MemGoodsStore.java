package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.redis.RedisHashBean;
import org.Iris.util.lang.DateUtils;

public class MemGoodsStore implements RedisHashBean{

	private long merchantId;
	private long goodsId;
	private String goodsName;
	private int count;
	private int created;
	private int updated;
	
	public MemGoodsStore(long merchantId, long goodsId,String goodsName ,int count) {
		this.merchantId = merchantId;
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.count = count;
		int time = DateUtils.currentTime();
		this.created = time;
		this.updated = time;
	}
	public MemGoodsStore() {
	}
	public long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
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
		return MerchantKeyGenerator.merchantGoodsStoreDataKey(merchantId, goodsId);
	}
	
	
}
