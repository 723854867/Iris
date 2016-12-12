package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.redis.RedisHashBean;

public class MemAccount implements RedisHashBean {

	private String account;
	private int type;
	private long merchantId;
	private long created;
	private int updated;
	
	public MemAccount() {}
	
	public MemAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public int getUpdated() {
		return updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}

	@Override
	public String redisKey() {
		return RedisKeyGenerator.getMemAccountDataKey(account);
	}
}
