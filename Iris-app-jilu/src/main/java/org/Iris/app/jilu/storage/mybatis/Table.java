package org.Iris.app.jilu.storage.mybatis;

public enum Table {
	
	CFG_GOODS("cfg_goods"),

	MERCHANT("merchant"),
	MERCHANT_ACCOUNT("merchant_account"),
	MERCHANT_CUSTOMER("merchant_customer"),
	MERCHANT_ORDER("merchant_order"),
	MERCHANT_ORDER_GOODS("merchant_order_goods"),
	MERCHANT_PACKET("merchant_packet"),
	
	RELATION("relation");
	
	private String mark;
	
	private Table(String mark) {
		this.mark = mark;
	}
	
	public String mark() {
		return mark;
	}
}
