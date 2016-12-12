package org.Iris.app.jilu.common.bean.model;

import org.Iris.app.jilu.common.model.AccountType;

public class AccountModel {

	private int type;
	private String account;
	
	public AccountModel() {}
	
	public AccountModel(AccountType type, String account) {
		this.type = type.mark();
		this.account = account;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
