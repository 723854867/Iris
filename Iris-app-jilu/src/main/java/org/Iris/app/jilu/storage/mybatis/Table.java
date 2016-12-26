package org.Iris.app.jilu.storage.mybatis;

public enum Table {
	
	CFG_GOODS("cfg_goods"),

	MERCHANT("mem_merchant"),
	MERCHANT_ACCOUNT("mem_account"),
	MERCHANT_CUSTOMER("mem_customer"),
	MERCHANT_ORDER("mem_order"),
	MERCHANT_ORDER_GOODS("mem_order_goods"),
	MERCHANT_PACKET("mem_packet"),
	
	RELATION("relation");
	
	private String mark;
	
	private Table(String mark) {
		this.mark = mark;
	}
	
	public String mark() {
		return mark;
	}
}
