package org.Iris.app.jilu.storage.mybatis;

public enum Table {

	MEM_MERCHANT("mem_merchant"),
	MEM_ACCOUNT("mem_account");
	
	private String mark;
	
	private Table(String mark) {
		this.mark = mark;
	}
	
	public String mark() {
		return mark;
	}
}
