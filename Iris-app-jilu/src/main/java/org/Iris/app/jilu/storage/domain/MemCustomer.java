package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.redis.RedisHashBean;

public class MemCustomer implements RedisHashBean {

	private long customerId;
	private long merchantId;
	private String name;
	private String mobile;
	private String address;
	private String memo;
	private String IDNumber;
	private int created;
	private int updated;
	private int deleted;

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	
	public long getMerchantId() {
		return merchantId;
	}
	
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getIDNumber() {
		return IDNumber;
	}

	public void setIDNumber(String iDNumber) {
		IDNumber = iDNumber;
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

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	@Override
	public String redisKey() {
		return RedisKeyGenerator.getMemCustomerDataKey(this.customerId);
	}
}
