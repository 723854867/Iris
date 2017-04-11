package org.Iris.app.jilu.service.realm.igt.domain;

import java.text.MessageFormat;

import org.Iris.app.jilu.common.bean.enums.IgtPushType;

public class PushFriendApplyReplyParam extends PushCommonParam{

	private long merchantId;
	private String name;
	private int reply;//处理结果
	public PushFriendApplyReplyParam(long merchantId, String name, int reply) {
		title = IgtPushType.FRIEND_APPLY_REPLY.getTitle();
		content = MessageFormat.format(IgtPushType.FRIEND_APPLY_REPLY.getContent(), name,reply==0?"通过":"被拒绝");
		this.merchantId = merchantId;
		this.name = name;
		this.reply = reply;
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
	public int getReply() {
		return reply;
	}
	public void setReply(int reply) {
		this.reply = reply;
	}
	
	
}
