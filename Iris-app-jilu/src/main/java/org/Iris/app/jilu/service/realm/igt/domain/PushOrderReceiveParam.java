package org.Iris.app.jilu.service.realm.igt.domain;

public class PushOrderReceiveParam extends PushCommonParam{

	private String orderId;
	private String superOrderId;
	
	public PushOrderReceiveParam(String orderId, String superOrderId) {
		title = "订单接收";
		content = "订单接收";
		this.orderId = orderId;
		this.superOrderId = superOrderId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getSuperOrderId() {
		return superOrderId;
	}
	public void setSuperOrderId(String superOrderId) {
		this.superOrderId = superOrderId;
	}
	
	
}
