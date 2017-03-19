package org.Iris.app.jilu.common.bean.form;

import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.MemGoodsStore;

public class GoodsStoreSearchForm {

	private int isCreated;
	private long merchantId;
	private String merchantName;
	private long goodsId;
	private String goodsName;
	private long id;
	private long count;
	private float price;
	private String memo;
	private int sign;
	private String goodsImage;
	
	public GoodsStoreSearchForm(CfgGoods cfgGoods){
		this.isCreated = 0;
		this.merchantId = Long.valueOf(cfgGoods.getSource());
		this.merchantName = cfgGoods.getMerchantName();
		this.goodsId = cfgGoods.getGoodsId();
		this.goodsName = cfgGoods.getZhName();
	}
	
	public GoodsStoreSearchForm(MemGoodsStore store) {
		this.id = store.getId();
		this.isCreated = 1;
		this.merchantId = store.getMerchantId();
		this.merchantName = store.getMerchantName();
		this.goodsId = store.getGoodsId();
		this.goodsName = store.getGoodsName();
		this.count = store.getCount();
		this.price = store.getPrice();
		this.memo = store.getMemo();
		this.sign = store.getSign();
		//this.goodsImage = JiLuResourceUtil.goodsImageUri(store);
	}
	public int getIsCreated() {
		return isCreated;
	}
	public void setIsCreated(int isCreated) {
		this.isCreated = isCreated;
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public String getGoodsImage() {
		return goodsImage;
	}
	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}
	
	
}
