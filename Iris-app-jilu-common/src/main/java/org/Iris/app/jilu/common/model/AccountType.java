package org.Iris.app.jilu.common.model;

public enum AccountType {

	MOBILE(0),
	EMAIL(1),
	WECHAT(2);
	
	private int mark;
	
	private AccountType(int mark) {
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
	
	public static final AccountType match(int type) {
		for (AccountType at : AccountType.values()) {
			if (at.mark != type)
				continue;
			return at;
		}
		return null;
	}
}
