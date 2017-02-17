package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.redis.RedisHashBean;
import org.Iris.util.lang.DateUtils;

public class MemGoodsStore implements RedisHashBean{

	private long merchantId;
	private long goodsId;
	private String goodsCode;
	private String goodsName;
	private long count;
	private int created;
	private int updated;
	
	public MemGoodsStore(CfgGoods cfgGoods , long count){
		this.merchantId = Long.valueOf(cfgGoods.getSource());
		this.goodsId = cfgGoods.getGoodsId();
		this.goodsCode = cfgGoods.getGoodsCode();
		this.goodsName = cfgGoods.getZhName();
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
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
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
	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	@Override
	public String redisKey() {
		return MerchantKeyGenerator.merchantGoodsStoreDataKey(merchantId, goodsId);
	}
	
	
}
