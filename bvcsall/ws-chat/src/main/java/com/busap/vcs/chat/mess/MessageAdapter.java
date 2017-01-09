package com.busap.vcs.chat.mess;

import io.netty.channel.Channel;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.busap.vcs.base.WsMessage;
import com.busap.vcs.chat.service.JedisService;
import com.busap.vcs.chat.util.ChatUtil;
import com.busap.vcs.chat.util.LiveUtil;
import com.busap.vcs.chat.util.MessUtil;
import com.busap.vcs.chat.util.SensitiveWordUtil;
import com.busap.vcs.chat.util.SpringUtils;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.MessageConst;

public class MessageAdapter {
	private static Logger logger = LoggerFactory.getLogger(MessageAdapter.class);
		
	private static JedisService jedisService;
	
	static{
		jedisService = (JedisService)SpringUtils.getBean("jedisService");
	}

	public static void parseMessage(String mess,Channel chn){
		if(StringUtils.isBlank(mess)){
			logger.info("不合法的消息.null");
			return;
		}
		
		WsMessage message = build(mess);
		if(message == null || StringUtils.isBlank(message.getCode())){
			logger.info("无法解析的消息格式." + mess);
			return;
		}
		
		String uid = ChatUtil.channelUid.get(chn);
		String roomId = ChatUtil.channelRoom.get(chn);
		logger.info("合法的消息，发送者：{} 房间：{}",uid,roomId);
		
		switch(message.getCode()){
			case MessageConst.CHAT:			//收到聊天指令
				doChatParse(chn,uid,roomId,message);
				break;
			case MessageConst.LIVE:			//收到直播指令
				doLiveParse(message);
				break;
			case MessageConst.COMMEN:		//收到系统指令
				doCommenParse(chn,message);
				break;
			case MessageConst.BROADCAST:	//收到广播指令
				doBroadcastParse(message);
				break;
			case MessageConst.ADMIN:		//收到管理指令
				doAdminParse(chn,roomId,message);
				break;
			case MessageConst.PROPS:		//收到道具指令
				doPropsParse(roomId,message);
				break;
			case MessageConst.P2P:			//点对点私聊消息
				doP2PParse(message);
				break;
			case MessageConst.CONNMK:			//点对点私聊消息
				doConnMK(message);
				break;
				
			default:
				logger.info("未知指令：" + mess);
		}
	}
	
	/**
	 * 连麦消息
	 * @param message
	 */
	private static void doConnMK(WsMessage message) {
		if(MessageConst.CONNMK_REQUEST.equals(message.getChildCode()) && jedisService.isSetMemberInShard(BicycleConstants.BLACK_LIST_USER_ID+message.getRecieverId(), message.getSenderId())){
			logger.info("用户{}在主播{}的黑名单中，不能连麦",message.getSenderId(),message.getRecieverId());
			return;
		}
		MessUtil.sendToMessageQueue(message);
	}

	/**
	 * 解析单聊消息
	 * @param message
	 */
	private static void doP2PParse(WsMessage message) {
		switch(message.getChildCode()){
//			case MessageConst.P2P_NOTICE: 	//私聊通知
//				MessUtil.sendToMessageQueue(message);
//				break;
			case MessageConst.P2P_CALLBACK:
				MessUtil.sendToMessageQueue(message);
				break;
			case MessageConst.P2P_TEXT:
				MessUtil.sendToMessageQueue(message);
				break;
			case MessageConst.P2P_AUDIO:
				MessUtil.sendToMessageQueue(message);
				break;
			case MessageConst.P2P_IMG:
				MessUtil.sendToMessageQueue(message);
				break;
			case MessageConst.P2P_VIDEO:
				MessUtil.sendToMessageQueue(message);
				break;
		}
	}

	/**
	 * 处理送礼物等道具信息
	 * @param roomId
	 * @param message
	 */
	private static void doPropsParse(String roomId, WsMessage message) {
		switch(message.getChildCode()){
			case MessageConst.PROPS_GIFT: 	//送礼物信息推送
				MessUtil.sendToMessageQueue(message);
				break;
		}
		
	}

	/**
	 * 用户发言处理
	 * @param roomId
	 * @param message
	 */
	private static void doChatParse(Channel chn,String uid,String roomId, WsMessage message) {
		switch(message.getChildCode()){
		case MessageConst.CHAT_PUBLIC:
			if(StringUtils.isNotBlank(uid)){
				if(jedisService.keyExists("RoomBan_"+roomId+"_"+uid)){
					MessUtil.sendAntiTalk(chn,uid,roomId);//通知用户已经被禁言
					logger.info("用户{}在房间{}处于禁言中",uid,roomId);
				} else if(!jedisService.isSetMemberInShard(BicycleConstants.BLACK_LIST_USER_ID+message.getRecieverId(), message.getSenderId())){
					String content = message.getContent();
					if(content != null){
						try {
							content = new String(content.getBytes(),"UTF-8");
							message.setContent(SensitiveWordUtil.replaceSensitiveWord(content, SensitiveWordUtil.maxMatchType));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					MessUtil.sendToMessageQueue(message);
					
//					addChatCount(roomId,uid);
				}
			}
			break;
		case MessageConst.CHAT_PRIVATE:
			if(jedisService.keyExists("RoomBan_"+roomId+"_"+message.getSenderId())){
				logger.info("用户{}在房间{}处于禁言中",message.getSenderId(),roomId);
			} else {
				MessUtil.sendToMessageQueue(message);
			}
			break;
		case MessageConst.CHAT_PRAISE:
			if(StringUtils.isNotBlank(uid) && !jedisService.isSetMemberInShard(BicycleConstants.BLACK_LIST_USER_ID+message.getRecieverId(), uid)){
				MessUtil.sendToMessageQueue(message);
				
				long praiseNum = jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+message.getRoomId(), "praiseNumber");//.setValueToMap(BicycleConstants.ROOM_+message.getRoomId(), "praiseNumber", praise.toString());

				Map<String,String> pNotice = MessUtil.getPraiseNotice();
				if(pNotice!=null){
					String notice = pNotice.get(praiseNum + "");
					if(StringUtils.isNotBlank(notice)){
						MessUtil.sendNotice(message.getRoomId(),notice,"提示",null);
					}
				}
				
			}
			break;
		}
		
	}

//	private static void addChatCount(String roomId, String uid) {
//		if(LiveUtil.roomLiveUser.containsKey(roomId)){
//			if(uid.equals(LiveUtil.roomLiveUser.get(roomId))){
//				jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "anchorChatCount");
//			} else {
//				jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "chatCount");
//			}
//		}else{			
//			jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "chatCount");
//		}
//	}

	/**
	 * 管理指令，禁言、踢人、终结直播
	 * @param roomId
	 * @param message
	 */
	private static void doAdminParse(Channel chn,String roomId,WsMessage message) {
		MessUtil.sendToMessageQueue(message);
//		switch(message.getChildCode()){
//			case MessageConst.ADMIN_BAN:
//				//验证禁言者身份、是主播才可以
//				if(StringUtils.isNotBlank(message.getSenderId()) && message.getSenderId().equals(jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "creatorId")) 
//						&& !jedisService.isSetMemberInShard(BicycleConstants.SUPERADMINS, message.getRecieverId())){
//					String key = "RoomBan_"+roomId+"_"+message.getRecieverId();
//					jedisService.set(key, "1");
//					jedisService.expire(key, 300);
//					MessUtil.sendToMessageQueue(message);
//					logger.info("用户{}在房间{}被禁言",message.getRecieverId(),roomId);
//				}else{
//					logger.info("禁言失败，没有权限 ,主播：{} 用户：{} 房间：{}",message.getSenderId(),message.getRecieverId(),roomId);
//				}
//				break;
//			case MessageConst.ADMIN_KICK:
//				//验证禁言者身份、是主播才可以
//				if(StringUtils.isNotBlank(message.getSenderId()) && message.getSenderId().equals(jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "creatorId"))
//						&& !jedisService.isSetMemberInShard(BicycleConstants.SUPERADMINS, message.getRecieverId())){
//					message.setContent("成功将"+ message.getRecieverName() +"请离直播间");
//					MessUtil.sendToMessageQueue(message);
//				}else{
//					logger.info("踢人失败，没有权限，主播：{} 用户：{} 房间： {}",message.getSenderId(),message.getRecieverId(),roomId);
//				}
//				break;
//			case MessageConst.ADMIN_ENDLIVE:
//				if(AdminUtil.adminChannel.containsKey(chn)){//管理员链接有权操作
//					MessUtil.sendToMessageQueue(message);
//				}
//				break;
//		}
	}


	private static void doBroadcastParse(WsMessage message) {
		// TODO Auto-generated method stub
		
	}

/**
 * 心跳指令直接返回当前时间戳
 * @param chn
 * @param message
 */
	private static void doCommenParse(Channel chn,WsMessage message) {
		switch(message.getChildCode()){
			case MessageConst.COMMEN_HB://收到心跳指令,非登录用户也有
				long online = System.currentTimeMillis();
				String uid = ChatUtil.channelUid.get(chn);
				if(StringUtils.isNotBlank(uid)){
					jedisService.setValueToSortedSetInShard(BicycleConstants.USER_LAST_HARTBEAT, online, uid);
				}
				message.getExtra().put("online",online);
				MessUtil.send(chn, message);

				break;
			case MessageConst.COMMEN_LEAVE:
				uid = message.getSenderId();
				String roomId = message.getRoomId();
				if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(roomId)){
					ChatUtil.leaveRoom(chn,uid, roomId);
				}
				break;
			case MessageConst.COMMEN_USERADD:
				uid = message.getSenderId();
				roomId = message.getRoomId();
				if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(roomId)){
					ChatUtil.checkinRoom(chn, uid, roomId);
				}
				break;
			case MessageConst.COMMEN_ATTENTION:
				MessUtil.sendToMessageQueue(message);
				break;
		}
		
	}

	private static void doLiveParse(WsMessage message) {
		switch(message.getChildCode()){
			case MessageConst.LIVE_START:
				LiveUtil.startLive(message);
				break;
			case MessageConst.LIVE_END:
				String uid = message.getSenderId();
				String roomId = message.getRoomId();
				if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(roomId)){
					String liveRoom = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "liveRoom");
					//验证退出指令发送者是否主播，只主播可以使用该指令
					if(roomId.equals(liveRoom)){
						LiveUtil.endLive(uid,roomId);
//						MessUtil.sendToMessageQueue(message);
					}
				}
				break;
			case MessageConst.LIVE_TIMEOUT:
				jedisService.setValueToMap(BicycleConstants.ROOM_+message.getRoomId(), "liveStatus", "2");//暂停状态
				message.setContent("我马上就回来，等我一下下哦~");
				MessUtil.sendToMessageQueue(message);
				break;
			case MessageConst.LIVE_GOON:
				jedisService.setValueToMap(BicycleConstants.ROOM_+message.getRoomId(), "liveStatus", "1");;//恢复状态
				MessUtil.sendToMessageQueue(message);
				break;
		}
		
	}

	private static WsMessage build(String mess) {
		try{
			return (WsMessage)JSONObject.parseObject(mess, WsMessage.class);
		}catch(Exception ex){
			logger.error("message build error:"+mess, ex);
			return null;
		}
	}

	public static void parseAdminMessage(String mess, Channel chn) {
		if(StringUtils.isBlank(mess)){
			logger.info("管理员信息-不合法的消息.null");
			return;
		}
		
		WsMessage message = build(mess);
		if(message == null || StringUtils.isBlank(message.getCode())){
			logger.info("管理员信息-无法解析的消息格式." + mess);
			return;
		}
		
		switch(message.getCode()){
			case MessageConst.BROADCAST:	//收到广播指令
				doBroadcastParse(message);
				break;
			case MessageConst.ADMIN:		//收到管理指令
				doAdminParse(chn,message.getRoomId(),message);
				break;
				
			default:
				logger.info("未知指令：" + mess);
		}		
	}

	public static void recieveBinaryMessage(byte[] array) {
		// TODO Auto-generated method stub
		
	}
	
}
