package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

//直播审核日志
@Entity
@Table(name = "live_check_log")
public class LiveCheckLog extends BaseEntity{  
	 
	
	private static final long serialVersionUID = 5935636827672330801L;
	
	private String operate; //操作
	
	private String reason;  //原因
	
	private Long roomId;  //房间id
	
	private Long userId;   //被操作人id
	
	private String type; //类型：live：直播，head：头像，home:背景图

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
