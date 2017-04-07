package org.Iris.app.jilu.service.realm.igt.domain;

public class PushOrderTransformParam extends PushCommonParam{
	private String name;
	private String orderId;
	private int created;
	public PushOrderTransformParam(String name, String orderId, int created) {
		title = "转单";
		content = "订单被转单";
		this.name = name;
		this.orderId = orderId;
		this.created = created;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getCreated() {
		return created;
	}
	public void setCreated(int created) {
		this.created = created;
	}
	
}
