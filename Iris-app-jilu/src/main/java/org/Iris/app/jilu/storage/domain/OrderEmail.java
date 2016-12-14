package org.Iris.app.jilu.storage.domain;

import org.Iris.redis.RedisHashBean;
import org.Iris.util.lang.DateUtils;

public class OrderEmail implements RedisHashBean {

	private String orderId;
	private int emailCode;
	private String note;
	private float price;
	private int created;

	public OrderEmail() {
	}

	public OrderEmail(String orderId, int emailCode, String note, float price) {
		this.orderId = orderId;
		this.emailCode = emailCode;
		this.note = note;
		this.price = price;
		this.created = DateUtils.currentTime();
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getEmailCode() {
		return emailCode;
	}

	public void setEmailCode(int emailCode) {
		this.emailCode = emailCode;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	@Override
	public String redisKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
