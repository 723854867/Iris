package com.busap.vcs.data.entity;
import java.io.Serializable;
/**
 * @author dmsong
 * 私信记录
 */
import java.util.Date;

public class PrivateMsg implements Serializable {

	private static final long serialVersionUID = 782378839242168611L;
	
	private String id;			//消息Id
	private Long sender;		//发送者Id
	private String senderName;	//发送者昵称
	private Long reciever;		//接收者Id
	private Date createTime;	//发送时间
	private Date updateTime;	//状态更新时间
	private String content;		//消息内容
	private String contentType;	//消息类型，文字、图片、音频、视频等
	private Integer status;		//状态
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getSender() {
		return sender;
	}
	public void setSender(Long sender) {
		this.sender = sender;
	}
	public Long getReciever() {
		return reciever;
	}
	public void setReciever(Long reciever) {
		this.reciever = reciever;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
}
