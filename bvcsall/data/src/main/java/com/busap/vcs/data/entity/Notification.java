package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/*
 * baseEntity 中的creator_id 代表发送给的某个用户，而不是创建者，为空则发送给所有用户
 */
@Entity
@Table(name = "notification")
public class Notification extends BaseEntity {
	private static final long serialVersionUID = 2695251157657004508L;
	
	/**
	 * 消息ID
	 */
	@Column
	private Long msgid;

	/**
	 * 输入消息内容
	 */
	@Column(length = 234, nullable = false)
	private String content;

	/**
	 * 发送类型 (message, notification)
	 */
	@Column
	private String type;

	/**
	 * 发送结果，（1= 成功，0=失败）
	 */
	@Column(length = 1)
	private int result;
	/**
	 * 发送序号
	 */
	@Column
	private int sendno;

	/**
	 * 极光返回发送异常信息
	 */
	@Column
	private String error;
	
	public Long getMsgid() {
		return msgid;
	}

	public void setMsgid(Long msgid) {
		this.msgid = msgid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getSendno() {
		return sendno;
	}

	public void setSendno(int sendno) {
		this.sendno = sendno;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
