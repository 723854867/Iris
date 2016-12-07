package org.Iris.app.jilu.storage.mybatis.domain;

import org.Iris.redis.RedisHashBean;

public class MemMerchant implements RedisHashBean {

	private long merchat_id;
	private int status_mod;
	private String name;
	private String mobile;
	private String address;
	private String avatar;
	private String QR_code;
	private int last_login_time;
	private int last_purchase_time;
	private int created;
	private int updated;

	public long getMerchat_id() {
		return merchat_id;
	}

	public void setMerchat_id(long merchat_id) {
		this.merchat_id = merchat_id;
	}

	public int getStatus_mod() {
		return status_mod;
	}

	public void setStatus_mod(int status_mod) {
		this.status_mod = status_mod;
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

	public String getQR_code() {
		return QR_code;
	}

	public void setQR_code(String qR_code) {
		QR_code = qR_code;
	}

	public int getLast_login_time() {
		return last_login_time;
	}

	public void setLast_login_time(int last_login_time) {
		this.last_login_time = last_login_time;
	}

	public int getLast_purchase_time() {
		return last_purchase_time;
	}

	public void setLast_purchase_time(int last_purchase_time) {
		this.last_purchase_time = last_purchase_time;
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
