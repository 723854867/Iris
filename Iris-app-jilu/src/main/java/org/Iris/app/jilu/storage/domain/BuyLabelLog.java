package org.Iris.app.jilu.storage.domain;

import org.Iris.util.lang.DateUtils;

public class BuyLabelLog {

	private long id;
	private long merchantId;
	private long count;
	private int created;//申请购买时间
	private int status;
	private int sendTime;//发货时间
	
	public BuyLabelLog(long merchantId, long count) {
		this.merchantId = merchantId;
		this.count = count;
		this.created = DateUtils.currentTime();
	}

	public BuyLabelLog() {
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSendTime() {
		return sendTime;
	}

	public void setSendTime(int sendTime) {
		this.sendTime = sendTime;
	}
}
