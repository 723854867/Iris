package org.Iris.app.jilu.common.bean.form;

import java.util.ArrayList;
import java.util.List;

import org.Iris.app.jilu.common.JiLuResourceUtil;
import org.Iris.app.jilu.storage.domain.MemGoodsStore;

public class GoodsStoreForm {
	
	private long merchantId;
	private String merchantName;
	private long goodsId;
	private String goodsName;
	private long count;
	private float price;
	private String memo;
	private String sign;
	private String goodsImage;
	
	public GoodsStoreForm(){
	}
	
	public GoodsStoreForm(MemGoodsStore store){
		this.merchantId = store.getMerchantId();
		this.merchantName = store.getMerchantName();
		this.goodsId = store.getGoodsId();
		this.goodsName = store.getGoodsName();
		this.count = store.getCount();
		this.price = store.getPrice();
		this.memo = store.getMemo();
		this.goodsImage = JiLuResourceUtil.goodsImageUri(store);
	}
	
	public static List<GoodsStoreForm> getGoodsStoreFormList(List<MemGoodsStore> mList){
		List<GoodsStoreForm> list = new ArrayList<GoodsStoreForm>();
		for(MemGoodsStore store : mList)
			list.add(new GoodsStoreForm(store));
		return list;
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
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getGoodsImage() {
		return goodsImage;
	}
	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}
	
}
