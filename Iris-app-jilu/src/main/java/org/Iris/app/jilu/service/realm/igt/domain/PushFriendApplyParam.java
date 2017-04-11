package org.Iris.app.jilu.service.realm.igt.domain;

import java.text.MessageFormat;

import org.Iris.app.jilu.common.bean.enums.IgtPushType;

public class PushFriendApplyParam extends PushCommonParam{

	private long merchantId;
	private String name;
	private String memo;
	
	public PushFriendApplyParam(long merchantId, String name, String memo) {
		title = IgtPushType.FRIEND_APPLY.getTitle();
		content = MessageFormat.format(IgtPushType.FRIEND_APPLY.getContent(), name);
		this.merchantId = merchantId;
		this.name = name;
		this.memo = memo;
	}
	public long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
}
