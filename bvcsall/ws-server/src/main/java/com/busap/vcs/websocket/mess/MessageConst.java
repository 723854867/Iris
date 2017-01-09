package com.busap.vcs.websocket.mess;

public class MessageConst {
	/**
	 * 系统指令
	 */
	public static final String CHECK_MESS = "000";
	
	/**
	 * 直播投诉指令
	 */
	public static final String LIVE_COMPLAINTS_MESS = "010";
	/**
	 * 聊天指令
	 */
	public static final String CHAT = "100";
	//公聊
	public static final String CHAT_PUBLIC = "1001";
	//私聊
	public static final String CHAT_PRIVATE = "1002";
	//点赞
	public static final String CHAT_PRAISE = "1003";
	
	/**
	 * 直播指令
	 */
	public static final String LIVE = "200";
	//直播开始指令
	public static final String LIVE_START = "2001";
	//直播结束指令
	public static final String LIVE_END = "2002";
	
	/**
	 * 广播指令
	 */
	public static final String BROADCAST = "300";

	/**
	 * 截图涉黄指令
	 */
	public static final String CUTIMAGE_IRREGULARITY_MESS = "020";

	/**
	 * 新直播指令
	 */
	public static final String NEW_LIVE = "030";

}
