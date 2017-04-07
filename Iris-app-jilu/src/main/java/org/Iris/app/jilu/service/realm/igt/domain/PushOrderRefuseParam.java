package org.Iris.app.jilu.service.realm.igt.domain;

public class PushOrderRefuseParam extends PushCommonParam{

	private String orderId;
	private String superOrderId;
	public PushOrderRefuseParam(String orderId, String superOrderId) {
		title = "订单拒绝";
		content = "订单拒绝";
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
