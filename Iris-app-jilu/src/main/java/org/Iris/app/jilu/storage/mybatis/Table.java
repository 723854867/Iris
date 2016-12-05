package org.Iris.app.jilu.storage.mybatis;

public enum Table {

	MEM_USER("mem_merchant");
	
	private String mark;
	
	private Table(String mark) {
		this.mark = mark;
	}
	
	public String mark() {
		return mark;
	}
}
