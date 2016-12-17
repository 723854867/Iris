package org.Iris.app.jilu.storage.mybatis;

public enum Table {

	MEM_MERCHANT("mem_merchant"),
	MEM_ACCOUNT("mem_account"),
	MEM_CUSTOMER("mem_customer"),
	ORDER_BASEINFO("order_baseinfo"),
	GOODS("goods"),
	ORDER_GOODS("order_goods"),
	ORDER_EMAIL("order_email");
	
	private String mark;
	
	private Table(String mark) {
		this.mark = mark;
	}
	
	public String mark() {
		return mark;
	}
}
