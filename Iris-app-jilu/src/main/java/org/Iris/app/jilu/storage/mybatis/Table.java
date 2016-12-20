package org.Iris.app.jilu.storage.mybatis;

public enum Table {

	MEM_MERCHANT("mem_merchant"),
	MEM_ACCOUNT("mem_account"),
	MEM_CUSTOMER("mem_customer"),
	MEM_ORDER("mem_order"),
	MEM_GOODS("mem_order"),
	MEM_ORDER_GOODS("mem_order_goods"),
	MEM_PACKET("mem_packet");
	
	private String mark;
	
	private Table(String mark) {
		this.mark = mark;
	}
	
	public String mark() {
		return mark;
	}
}
