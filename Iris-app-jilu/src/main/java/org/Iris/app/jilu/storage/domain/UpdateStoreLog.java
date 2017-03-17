package org.Iris.app.jilu.storage.domain;

import org.Iris.util.lang.DateUtils;

/**
 * 修改库存日志实体
 * @author 樊水东
 * 2017年3月17日
 */
public class UpdateStoreLog {

	private long id;
	private long goodsId;
	private String goodsName;
	private long merchantId;
	private String merchantName;
	private float oldPrice;
	private float newPrice;
	private long oldCount;
	private long newCount;
	private String oldMemo;
	private String newMemo;
	private String operation;
	private int created;
	private int updated;
	
	public UpdateStoreLog(MemGoodsStore store,float newPrice,int newCount,String newMemo,String operation){
		this.goodsId = store.getGoodsId();
		this.goodsName = store.getGoodsName();
		this.merchantId = store.getMerchantId();
		this.merchantName = store.getMerchantName();
		this.oldPrice = store.getPrice();
		this.oldCount = store.getCount();
		this.oldMemo = store.getMemo();
		this.newPrice = newPrice;
		this.newCount = newCount;
		this.newMemo = newMemo;
		this.operation = operation;
		int time = DateUtils.currentTime();
		this.created = time;
		this.updated = time;
	}
	
	public UpdateStoreLog() {
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public float getOldPrice() {
		return oldPrice;
	}
	public void setOldPrice(float oldPrice) {
		this.oldPrice = oldPrice;
	}
	public float getNewPrice() {
		return newPrice;
	}
	public void setNewPrice(float newPrice) {
		this.newPrice = newPrice;
	}
	public long getOldCount() {
		return oldCount;
	}
	public void setOldCount(long oldCount) {
		this.oldCount = oldCount;
	}
	public long getNewCount() {
		return newCount;
	}
	public void setNewCount(long newCount) {
		this.newCount = newCount;
	}
	public String getOldMemo() {
		return oldMemo;
	}
	public void setOldMemo(String oldMemo) {
		this.oldMemo = oldMemo;
	}
	public String getNewMemo() {
		return newMemo;
	}
	public void setNewMemo(String newMemo) {
		this.newMemo = newMemo;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
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
	
	
}
