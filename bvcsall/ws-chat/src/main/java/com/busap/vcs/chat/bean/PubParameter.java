package com.busap.vcs.chat.bean;

import org.apache.log4j.Logger;


public class PubParameter {
	/**
	 * 聊天路径名称
	 */
	public static String CHAT_PATH = "chat";
	/**
	 * 后台管理路径名称
	 */
	public static String ADMIN_PATH = "admin";
	/**
	 * 消息通信
	 */
	public static String MESSAGE_PATH = "message";
	/**
	 * 消息通信
	 */
	public static String CONSUME_PATH = "consume";
	
	/**
	 * 收到聊天信息日志文件
	 */
	public static Logger recieveLog = Logger.getLogger("recieve_bak");
	
	/**
	 * 发送聊天信息日志文件
	 */
	public static Logger sendLog = Logger.getLogger("send_bak");
}
