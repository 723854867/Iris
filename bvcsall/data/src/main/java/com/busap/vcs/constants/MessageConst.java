package com.busap.vcs.constants;

public class MessageConst {
	/**
	 * 系统指令
	 */
	public static final String CHECK_MESS = "000";
	/**
	 * 聊天互动指令
	 */
	public static final String CHAT = "100";
	//公聊
	public static final String CHAT_PUBLIC = "1001";
	//私聊
	public static final String CHAT_PRIVATE = "1002";
	//点赞
	public static final String CHAT_PRAISE = "1003";
	//弹幕
	public static final String CHAT_BARRAGE = "1004";
	
	/**
	 * 直播指令
	 */
	public static final String LIVE = "200";
	//直播开始指令
	public static final String LIVE_START = "2001";
	//直播结束指令
	public static final String LIVE_END = "2002";
	//直播暂停
	public static final String LIVE_TIMEOUT = "2003";
	//直播继续
	public static final String LIVE_GOON = "2004";
	//直播掉线
	public static final String LIVE_DOWN = "2005";
	//观看进入直播
	public static final String LIVE_ANCHORLEAVE = "2006";
	/**
	 * 广播指令
	 */
	public static final String BROADCAST = "300";
	//系统公告
	public static final String BROADCAST_SYS = "3001";
	//房间公告
	public static final String BROADCAST_ROOM = "3002";
	
	/**
	 * 管理指令
	 */
	public static final String ADMIN = "400";
	//禁止用户发言
	public static final String ADMIN_BAN = "4001";
	//把用户踢出房间
	public static final String ADMIN_KICK = "4002";
	//管理员终止直播
	public static final String ADMIN_ENDLIVE = "4003";
	
	/**
	 * 道具指令
	 */
	public static final String PROPS = "500";
	//发送礼物
	public static final String PROPS_GIFT = "5001";
	//礼物贡献榜前三名
	public static final String PROPS_CONTRIBUTION = "5002";
	
	/**
	 * 点对点单聊指令
	 */
	public static final String P2P = "600";
	//通知消息
	public static final String P2P_NOTICE = "6001";
	//单聊回调消息
	public static final String P2P_CALLBACK = "6002";
	//单聊文字信息
	public static final String P2P_TEXT = "6003";
	//单聊图片信息
	public static final String P2P_IMG = "6004";
	//单聊语音信息
	public static final String P2P_AUDIO = "6005";
	//单聊视频信息
	public static final String P2P_VIDEO = "6006";
	
	/**
	 * 常规系统消息
	 */
	public static final String COMMEN = "000";
	//新进入用户信息
	public static final String COMMEN_USERADD = "0001";
	//房间用户人数
	public static final String COMMEN_USERNUM = "0002";
	//房间点赞数
	public static final String COMMEN_PRAISENUM = "0003";
	//心跳
	public static final String COMMEN_HB = "0004";
	//用户离开房间信息
	public static final String COMMEN_USERREM = "0005";
	//提示性信息
	public static final String COMMEN_NOTICE = "0006";
	//客户端主动请求断开
	public static final String COMMEN_LEAVE = "0007";
	//账号重复登录
	public static final String COMMEN_RELOGIN = "0008";
	//连接失败提示
	public static final String COMMEN_CONNFAIL = "0009";
	//用户关注提示
	public static final String COMMEN_ATTENTION = "0010";
	//用户连接成功消息
	public static final String COMMEN_USERCONN = "0011";
	//用户断开连接消息
	public static final String COMMEN_DISCONN = "0012";
	
	/**
	 * 连麦
	 */
	public static final String CONNMK = "700";
	//用户发起连麦
	public static final String CONNMK_REQUEST = "7001";
	//用户取消连麦
	public static final String CONNMK_CANCLE = "7002";
	//主播同意连麦
	public static final String CONNMK_ACCEDE = "7003";
	//用户关闭连麦
	public static final String CONNMK_STOP = "7004";
	//主播强制结束已连接的用户
	public static final String CONNMK_SHUTDOWN = "7005";
	
}
