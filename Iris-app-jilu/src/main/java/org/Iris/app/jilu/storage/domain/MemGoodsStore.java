package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.redis.RedisHashBean;
import org.Iris.util.lang.DateUtils;

public class MemGoodsStore implements RedisHashBean {

	private long id;
	private long merchantId;
	private String merchantName;
	private long goodsId;
	private String goodsCode;
	private String goodsName;
	private long count;
	private long waitCount;
	private float price;
	private String memo;
	private int statusMod;// 后期用于标记是否已经上传商品图片
	private int created;
	private int updated;

	public MemGoodsStore(CfgGoods cfgGoods, long count, long waitCount, float price, String memo) {
		this.merchantId = Long.valueOf(cfgGoods.getSource());
		this.merchantName = cfgGoods.getMerchantName();
		this.goodsId = cfgGoods.getGoodsId();
		this.goodsCode = cfgGoods.getGoodsCode();
		this.goodsName = cfgGoods.getZhName();
		this.price = price;
		this.memo = memo;
		this.count = count;
		this.waitCount = waitCount;
		int time = DateUtils.currentTime();
		this.created = time;
		this.updated = time;
	}

	public MemGoodsStore() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getWaitCount() {
		return waitCount;
	}

	public void setWaitCount(long waitCount) {
		this.waitCount = waitCount;
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

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getStatusMod() {
		return statusMod;
	}

	public void setStatusMod(int statusMod) {
		this.statusMod = statusMod;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Override
	public String redisKey() {
		return MerchantKeyGenerator.merchantGoodsStoreDataKey(merchantId, goodsId);
	}

}
