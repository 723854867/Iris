package org.Iris.app.jilu.service.realm.igt.domain;

import java.text.MessageFormat;

import org.Iris.app.jilu.common.bean.enums.IgtPushType;
import org.Iris.app.jilu.common.bean.enums.OrderStatus;

public class PushOrderStatusChangeParam extends PushCommonParam{

	private String orderId;
	private String childOrderId;
	private int status;
	public PushOrderStatusChangeParam(String orderId, String childOrderId, int status) {
		title = IgtPushType.ORDER_STATUS_CHANGE.getTitle();
		content = MessageFormat.format(IgtPushType.ORDER_STATUS_CHANGE.getContent(), orderId,OrderStatus.getDesc(status));
		this.orderId = orderId;
		this.childOrderId = childOrderId;
		this.status = status;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getChildOrderId() {
		return childOrderId;
	}
	public void setChildOrderId(String childOrderId) {
		this.childOrderId = childOrderId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
