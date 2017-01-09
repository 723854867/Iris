package com.busap.vcs.chat.bean;

import io.netty.buffer.ByteBuf;


public class BinaryMessage {

	private String roomId;		//房间Id
	
	private String recieverId;	//接受者Id
		
	private String type;		//类型 1群聊 0单聊
	
	private byte[] content;	//内容

	
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRecieverId() {
		return recieverId;
	}

	public void setRecieverId(String recieverId) {
		this.recieverId = recieverId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
	
}
