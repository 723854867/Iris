package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.redis.RedisHashBean;
import org.Iris.util.lang.DateUtils;

public class Order implements RedisHashBean{

	private String orderId;
	private String receiveId;
	private String parentOrderId;
	private String note;
	private int created;
	private int updated;
	
	// 只存在 redis 的字段 
	/*
	 * status 1 表示待确认 0表示已确认
	 */
	private int status;

	public Order() {
	}

	public Order(String orderId) {
		this.orderId = orderId;
	}

	public Order(String orderId, String receiveId, String note) {
		super();
		this.orderId = orderId;
		this.receiveId = receiveId;
		this.note = note;
		this.created = DateUtils.currentTime();
		this.updated = DateUtils.currentTime();
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public String getParentOrderId() {
		return parentOrderId;
	}

	public void setParentOrderId(String parentOrderId) {
		this.parentOrderId = parentOrderId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String redisKey() {
		// TODO Auto-generated method stub
		return RedisKeyGenerator.getOrderDataKey(orderId);
	}

}
