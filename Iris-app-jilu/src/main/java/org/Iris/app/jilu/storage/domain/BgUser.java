package org.Iris.app.jilu.storage.domain;

import org.Iris.util.lang.DateUtils;

public class BgUser {

	private long id;
	private String account;
	private String password;
	private int lastLoginTime;
	private int created;
	private int updated;
	
	public BgUser() {
	}
	
	public BgUser(String account, String password) {
		int time = DateUtils.currentTime();
		this.account = account;
		this.password = password;
		this.created = time;
		this.updated = time;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(int lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
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
	
	
}
