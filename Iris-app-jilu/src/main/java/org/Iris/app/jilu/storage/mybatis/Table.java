package org.Iris.app.jilu.storage.mybatis;

public enum Table {
	
	BG_CONFIG("bg_config"),
	BG_USER("bg_user"),
	BG_VERSION("bg_version"),
	BG_LABEL("bg_label"),
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
	MEM_LABEL_BIND("mem_label_bind"),
	
	RELATION("pub_relation"),
	LOG_STOCK_STORE("log_stock_store"),
	LOG_BUY_LABEL("log_buy_label"),
	
	LOG_UPDATE_STORE("log_update_store"),
	MEM_WAIT_STORE("mem_wait_store"),
	MEM_PAY_INFO("mem_pay_info");

	
	private String mark;
	
	private Table(String mark) {
		this.mark = mark;
	}
	
	public String mark() {
		return mark;
	}
}
