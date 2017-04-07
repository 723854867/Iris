package org.Iris.app.jilu.service.realm.igt.domain;

public class PushOrderCancelParam extends PushCommonParam{

	private String orderId;
	private String superOrderId;
	public PushOrderCancelParam(String orderId, String superOrderId) {
		super();
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
