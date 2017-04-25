package org.Iris.app.jilu.service.realm.igt.domain;

import org.Iris.app.jilu.common.bean.enums.IgtPushType;

public class PushOrderRefuseParam extends PushCommonParam{

	private String orderId;
	private String superOrderId;
	public PushOrderRefuseParam(String orderId, String superOrderId) {
		title = IgtPushType.ORDER_REFUSE.getTitle();
		content = IgtPushType.ORDER_REFUSE.getContent();
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