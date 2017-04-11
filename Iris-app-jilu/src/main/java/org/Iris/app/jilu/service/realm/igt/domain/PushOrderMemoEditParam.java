package org.Iris.app.jilu.service.realm.igt.domain;

import java.text.MessageFormat;

import org.Iris.app.jilu.common.bean.enums.IgtPushType;

public class PushOrderMemoEditParam extends PushCommonParam{
	private String fromOrderId;
	private String toOrderId;
	private String memo;
	
	public PushOrderMemoEditParam(String fromOrderId, String toOrderId, String memo) {
		title = IgtPushType.ORDER_MEMO_EDIT.getTitle();
		content = MessageFormat.format(IgtPushType.ORDER_MEMO_EDIT.getContent(), toOrderId);
		this.fromOrderId = fromOrderId;
		this.toOrderId = toOrderId;
		this.memo = memo;
	}
	
	public String getFromOrderId() {
		return fromOrderId;
	}

	public void setFromOrderId(String fromOrderId) {
		this.fromOrderId = fromOrderId;
	}

	public String getToOrderId() {
		return toOrderId;
	}

	public void setToOrderId(String toOrderId) {
		this.toOrderId = toOrderId;
	}

	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
}
