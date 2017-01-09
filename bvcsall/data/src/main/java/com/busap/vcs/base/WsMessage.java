package com.busap.vcs.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class WsMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2153574530484473836L;
	
	private String messageId;	//消息ID
	private String code;		//消息分类代号
	private String childCode;	//消息子分类代号
	private String roomId;		//频道编号
	private String senderId;	//发送者ID
	private String senderName;	//发送者名称
	private String senderType = "0";	//发言用户类型,默认0 普通用户
	private String recieverId;	//接受者ID
	private String recieverName;//接受者名称
	private String title;		//消息标题
	private String content;		//消息内容
	//额外信息列表
	private Map<String,Object> extra = new HashMap<String,Object>();
	
	public WsMessage(){
		if(extra.get("createTime") == null){
			extra.put("createTime", System.currentTimeMillis());
		}
	}
	
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getChildCode() {
		return childCode;
	}
	public void setChildCode(String childCode) {
		this.childCode = childCode;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String chanelId) {
		this.roomId = chanelId;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderType() {
		return senderType;
	}
	public void setSenderType(String senderType) {
		this.senderType = senderType;
	}
	public String getRecieverId() {
		return recieverId;
	}
	public void setRecieverId(String recieverId) {
		this.recieverId = recieverId;
	}
	public String getRecieverName() {
		return recieverName;
	}
	public void setRecieverName(String recieverName) {
		this.recieverName = recieverName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Map<String, Object> getExtra() {
		return extra;
	}
	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}

	public String toString(){
		return JSONObject.toJSONString(this);
	}

}
