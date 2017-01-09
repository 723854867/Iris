package com.busap.vcs.websocket.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2153574530484473836L;
	
	private String code;		//消息分类代号
	private String childCode;	//消息子分类代号
	private Long chanelId;		//频道编号
	private Long senderId;		//发送者编号
	private String senderName;	//发送者名称
	private Long recieverId;	//接受者编号
	private String recieverName;//接受者名称
	private String title;		//消息标题
	private String content;		//消息内容
	//额外信息列表
	private Map<String,Object> extra = new HashMap<String,Object>();
	
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
	public Long getChanelId() {
		return chanelId;
	}
	public void setChanelId(Long chanelId) {
		this.chanelId = chanelId;
	}
	public Long getSenderId() {
		return senderId;
	}
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public Long getRecieverId() {
		return recieverId;
	}
	public void setRecieverId(Long recieverId) {
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

	
}
