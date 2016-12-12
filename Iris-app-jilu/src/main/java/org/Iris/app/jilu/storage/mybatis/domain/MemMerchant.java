package org.Iris.app.jilu.storage.mybatis.domain;

import org.Iris.redis.RedisHashBean;

public class MemMerchant implements RedisHashBean {

	private long merchatId;
	private int statusMod;
	private String name;
	private String mobile;
	private String address;
	private String avatar;
	private String QRCode;
	private int lastLoginTime;
	private int lastPurchaseTime;
	private int created;
	private int updated;

	public long getMerchatId() {
		return merchatId;
	}

	public void setMerchatId(long merchatId) {
		this.merchatId = merchatId;
	}

	public int getStatusMod() {
		return statusMod;
	}

	public void setStatusMod(int statusMod) {
		this.statusMod = statusMod;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getQRCode() {
		return QRCode;
	}

	public void setQRCode(String qRCode) {
		QRCode = qRCode;
	}

	public int getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(int lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public int getLastPurchaseTime() {
		return lastPurchaseTime;
	}

	public void setLastPurchaseTime(int lastPurchaseTime) {
		this.lastPurchaseTime = lastPurchaseTime;
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

	@Override
	public String redisKey() {
		return null;
	}
}
