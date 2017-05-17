package org.Iris.app.jilu.storage.domain;

public class MemJbDetail {
	
	private long id;
	private long merchantId;
	private int jb;
	private int time;
	private int type;
	private String orderId;
	
	public MemJbDetail() {
		super();
	}
	public MemJbDetail(long merchantId, int jb, int time, int type, String orderId) {
		super();
		this.merchantId = merchantId;
		this.jb = jb;
		this.time = time;
		this.type = type;
		this.orderId = orderId;
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
	public int getJb() {
		return jb;
	}
	public void setJb(int jb) {
		this.jb = jb;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
