package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.redis.RedisHashBean;

import com.mysql.cj.api.io.PacketReceivedTimeHolder;

public class MemMerchant implements RedisHashBean {

	private long merchantId;
	private int statusMod;
	private String name;
	private String address;
	private String QRCode;
	private int lastLoginTime;
	private int lastPurchaseTime;
	private int money;
	private int created;
	private int updated;
	//寄件人信息
	private String sendName;
	private String sendAddress;
	private String sendMobile;
	
	
	// 只存在 redis 的字段
	private String token;
	
	public MemMerchant() {}
	
	public MemMerchant(long merchantId) {
		this.merchantId = merchantId;
	}
	
	public MemMerchant(String name, String address) {
		this.name = name;
		this.address = address;
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

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
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

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}

	public String getSendMobile() {
		return sendMobile;
	}

	public void setSendMobile(String sendMobile) {
		this.sendMobile = sendMobile;
	}

	@Override
	public String redisKey() {
		return MerchantKeyGenerator.merchantDataKey(merchantId);
	}
}
