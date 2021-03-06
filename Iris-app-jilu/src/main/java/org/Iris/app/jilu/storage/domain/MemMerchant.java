package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.service.realm.unit.Unit;
import org.Iris.app.jilu.service.realm.unit.UnitType;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.redis.RedisHashBean;

public class MemMerchant implements RedisHashBean, Unit {

	private long merchantId;
	private int statusMod;
	private String name;
	private String address;
	private String avatar;
	private String QRCode;
	private int lastLoginTime;
	private int lastPurchaseTime;
	private int created;
	private int updated;
	
	// 只存在 redis 的字段
	private String token;
	
	public MemMerchant() {}
	
	public MemMerchant(long merchantId) {
		this.merchantId = merchantId;
	}
	
	public MemMerchant(String name, String address, String avatar) {
		this.name = name;
		this.address = address;
		this.avatar = avatar;
	}

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
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
	
	// ***************************************************************
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String redisKey() {
		return RedisKeyGenerator.getMemMerchantDataKey(merchantId);
	}
	
	@Override
	public long uid() {
		return merchantId;
	}
	
	@Override
	public UnitType unitType() {
		return UnitType.MERCHANT;
	}
}
