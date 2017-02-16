package org.Iris.app.jilu.storage.mybatis;

public enum Table {
	
	CFG_GOODS("cfg_goods"),

	MEM_MERCHANT("mem_merchant"),
	MEM_ACCOUNT("mem_account"),
	MEM_CUSTOMER("mem_customer"),
	MEM_ORDER("mem_order"),
	MEM_ORDER_GOODS("mem_order_goods"),
	MEM_ORDER_PACKET("mem_order_packet"),
	MEM_GOODS_STORE("mem_goods_store"),
	MEM_ORDER_STATUS("mem_order_status"),
	MEM_CID("mem_cid"),
	MEM_ACCID("mem_accid"),
	
	RELATION("pub_relation");
	
	private String mark;
	
	private Table(String mark) {
		this.mark = mark;
	}
	
	public String mark() {
		return mark;
	}
}
