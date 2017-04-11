package org.Iris.app.jilu.service.realm.igt.domain;

import org.Iris.app.jilu.common.bean.enums.IgtPushType;

public class PushOrderReceiveParam extends PushCommonParam{

	private String orderId;
	private String superOrderId;
	
	public PushOrderReceiveParam(String orderId, String superOrderId) {
		title = IgtPushType.ORDER_RECEIVE.getTitle();
		content = IgtPushType.ORDER_RECEIVE.getContent();
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
