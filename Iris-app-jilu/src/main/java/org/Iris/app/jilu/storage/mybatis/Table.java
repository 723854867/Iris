package org.Iris.app.jilu.storage.mybatis;

public enum Table {
	
	CFG_GOODS("cfg_goods"),

	MEM_MERCHANT("mem_merchant"),
	MEM_ACCOUNT("mem__account"),
	MEM_CUSTOMER("mem__customer"),
	MEM_ORDER("mem_order"),
	MEM_ORDER_GOODS("mem_order_goods"),
	MEM_ORDER_PACKET("mem_order_packet"),
	
	RELATION("relation");
	
	private String mark;
	
	private Table(String mark) {
		this.mark = mark;
	}
	
	public String mark() {
		return mark;
	}
}
