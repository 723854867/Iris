package org.Iris.app.jilu.common.bean.form;

import org.Iris.app.jilu.storage.domain.MemOrderPacket;

public class OrderPacketForm {

	private String packetId;
	private String orderId;
	private long merchantId;
	private String express;
	private String memo;
	private String expressCode;
	private String postage;
	private String label;
	
	public OrderPacketForm(MemOrderPacket packet){
		this.packetId = packet.getPacketId();
		this.orderId = packet.getOrderId();
		this.merchantId = packet.getMerchantId();
		this.express = packet.getExpress();
		this.memo = packet.getMemo();
		this.expressCode = packet.getExpressCode();
		this.postage = packet.getPostage();
		this.label = packet.getLabel();
	}
	
	public String getPacketId() {
		return packetId;
	}
	public void setPacketId(String packetId) {
		this.packetId = packetId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	public String getExpress() {
		return express;
	}
	public void setExpress(String express) {
		this.express = express;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getExpressCode() {
		return expressCode;
	}
	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}
	public String getPostage() {
		return postage;
	}
	public void setPostage(String postage) {
		this.postage = postage;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	
	
}
