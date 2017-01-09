package com.busap.vcs.chat.util;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.busap.vcs.base.WsMessage;
import com.busap.vcs.chat.srv.JedisService;
import com.busap.vcs.chat.srv.wsclient.MyWebSocketClient;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.MessageConst;
@Component("roomUtil")
public class RoomUtil {
	private static Logger logger = LoggerFactory.getLogger(RoomUtil.class);
	
	private static MyWebSocketClient wsClient = MyWebSocketClient.getInstance();
	
	private static JedisService jedisService;
	
	private static Map<String,String> userNumNotice;
	
	private static String clientId;
	
	private static long USERNUM_LOAD_TIME = 0;
	
	public static void init (JedisService dsService,String clientId){
		MessageMergeUtil.roomMessage.isEmpty();
		jedisService=dsService;
		clientId = clientId;
	}
	
	//推送用户进入房间信息
	public static void sendUserAdd(String uid,String roomId){
		Map<String,String> userInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO+uid);
		long t = System.currentTimeMillis();
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.COMMEN);
		m.setChildCode(MessageConst.COMMEN_USERADD);
		m.setSenderId(uid);
		m.setSenderName(userInfo.get("name"));
		m.setRoomId(roomId);
		m.setContent("来了");
		
		m.getExtra().put("userid", uid);
		m.getExtra().put("sex", userInfo.get("sex"));
		m.getExtra().put("name", userInfo.get("name"));
		m.getExtra().put("username", userInfo.get("username"));
		m.getExtra().put("signature", userInfo.get("signature"));
		m.getExtra().put("vipStat", userInfo.get("vipStat"));
		m.getExtra().put("pic", userInfo.get("pic"));
		m.getExtra().put("loginTime", userInfo.get("loginTime"));
		if(userInfo.containsKey("isMajia")){
			m.getExtra().put("isMajia", "1");
			t = t >> 1;
			jedisService.setValueToSortedSetInShard(BicycleConstants.ROOM_USERS+roomId, t, uid);
		} else if (jedisService.isSetMemberInShard(BicycleConstants.ROOM_USERS_TOPLIST, uid)){
			m.getExtra().put("isTop", "1");
			t = t << 1;
			jedisService.setValueToSortedSetInShard(BicycleConstants.ROOM_USERS+roomId, t, uid);
		} else {
			jedisService.setValueToSortedSetInShard(BicycleConstants.ROOM_USERS+roomId, t, uid);
		}
//		sendToRoom(m,clientId);
		MessageMergeUtil.addMessage(roomId, m);
		logger.info("用户 {} 进入房间，{}",uid,m.toString());
	}
	
	/**
	 * 用户进入房间，加房间在线人数和总人数
	 * @param roomId
	 */
	public static void incUserCount(String roomId) {
		Double scoreValue = jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, roomId);
		if(scoreValue!=null && scoreValue>=0){//只更新在线房间
			long step = 1;// onlineNum>=100?5:1;
			
			long maxAccess = jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "maxAccessNumber",step);
			
			Map<String,String> userNumNotice = getUserNumNotice();
			if(userNumNotice!=null){//房间人数达到固定阀值通知
				String notice = userNumNotice.get(maxAccess + "");
				if(StringUtils.isNotBlank(notice)){
					sendNotice(roomId,notice,"提示",clientId);
				}
			}
			
			jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "onlineNumber",step);
			
			jedisService.zincrByScore(BicycleConstants.ROOM_ORDER, roomId, 1d);
		}
	}
	
	public static Map<String,String> getUserNumNotice(){
		long now = System.currentTimeMillis();
		if(USERNUM_LOAD_TIME == 0 || (now-USERNUM_LOAD_TIME)>300000){
			userNumNotice = jedisService.getMapByKey(BicycleConstants.LIVE_VIEWER_COUNT);
			USERNUM_LOAD_TIME = now;
		}
		
		return userNumNotice;
	}
	
	public static void sendNotice(String roomId, String notice,String title,String clientId) {
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.COMMEN);
		m.setChildCode(MessageConst.COMMEN_NOTICE);
		m.setRoomId(roomId);
		m.setContent(notice);
		m.setTitle(title);
		
//		sendToRoom(m,clientId);
		MessageMergeUtil.addMessage(roomId, m);
		logger.info("房间{}提示消息，{}",roomId,m.toString());
	}
	
	public static void sendUserLeave(String uid,String roomId){
		Map<String,String> userInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO+uid);
		//从房间用户列表移除用户
		jedisService.deleteSortedSetItemFromShard(BicycleConstants.ROOM_USERS+roomId, uid);
		
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.COMMEN);
		m.setChildCode(MessageConst.COMMEN_USERREM);
		m.setSenderId(uid);
		m.setSenderName(userInfo.get("name"));
		m.setRoomId(roomId);
		m.setContent("离开了");
		
		m.getExtra().put("userid", uid);
		m.getExtra().put("name", userInfo.get("name"));
		m.getExtra().put("username", userInfo.get("username"));
		m.getExtra().put("signature", userInfo.get("signature"));
		m.getExtra().put("vipStat", userInfo.get("vipStat"));
		m.getExtra().put("pic", userInfo.get("pic"));
		if("majia".equals(userInfo.get("type"))){
			m.getExtra().put("isMajia", "1");
		} else if(jedisService.isSetMemberInShard(BicycleConstants.ROOM_USERS_TOPLIST, uid)) {
			m.getExtra().put("isTop", "1");
		}
		
//		sendToRoom(m,clientId);
		MessageMergeUtil.addMessage(roomId, m);
		logger.info("用户 {} 离开房间，{}",uid,m.toString());
	}
	
	/**
	 * 推送房间的在线人数
	 * @param roomId
	 */
	public static void sendRoomUserCount(String roomId) {
		String max = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "maxAccessNumber");
		String online = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "onlineNumber");
		Integer maxNum = 0;
		if(StringUtils.isNumeric(max)){
			maxNum = Integer.parseInt(max);
		}
		Integer onlineNum = 0;
		if(StringUtils.isNumeric(online)){
			onlineNum = Integer.parseInt(online);
		}
		logger.info("room {} has {} user access,{} online right now.",roomId,maxNum,onlineNum);
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.COMMEN);
		m.setChildCode(MessageConst.COMMEN_USERNUM);
		m.setRoomId(roomId);
		m.getExtra().put("maxAccessNumber", maxNum);
		m.getExtra().put("onlineNumber", onlineNum);
		
//		sendToRoom(m,clientId);
		MessageMergeUtil.addMessage(roomId, m);
	}
	
	/**
	 * 用户退出房间，减房间在线人数
	 * @param roomId
	 */
	public static void decUserCount(String roomId) {
		Double scoreValue = jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, roomId);
		if(scoreValue!=null && scoreValue>0){//不在线房间不更新
			long step = -1; 
			
			long onlineNum =jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "onlineNumber", step);
			if(onlineNum<0){
				jedisService.setValueToMap(BicycleConstants.ROOM_+roomId, "onlineNumber", "0");
			}
					
			jedisService.zincrByScore(BicycleConstants.ROOM_ORDER, roomId, -1d);
		}
	}
	
	/**
	 * 推送直播结束指令
	 * @param roomId
	 * @param uid
	 */
	public static void sendLiveEnd(String roomId,String uid,String points,String giftNum){
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.LIVE);
		m.setChildCode(MessageConst.LIVE_END);
		m.setRoomId(roomId);
		m.setSenderId(uid);
		m.setContent("直播结束");
		m.getExtra().put("points", points);
		m.getExtra().put("giftNum", giftNum);
		
		MessageMergeUtil.addMessage(roomId, m);
	}
	
	/**
	 * 群聊
	 * @param roomId
	 * @param message
	 */
	public static void sendToRoom(WsMessage message){
		String roomId = message.getRoomId();
		if(StringUtils.isBlank(roomId)){
			logger.warn("not have a roomId in message:{}",message.toString());
			return;
		}
		Set<String> urls = jedisService.getSetFromShard(BicycleConstants.ROOM_SERVERURLS+roomId);

		logger.info("send to urls:{}",urls);
		if(urls != null && !urls.isEmpty()){
			for(String url:urls){
				try{
					wsClient.sendMessage(url, message.toString(),clientId);
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 发送二进制格式消息群聊
	 * @param roomId
	 * @param buf
	 */
	public static void sendBinaryToRoom(String roomId,byte[] buf){
		if(StringUtils.isNumeric(roomId) && buf!= null && buf.length>32){
			
			Set<String> urls = jedisService.getSetFromShard(BicycleConstants.ROOM_SERVERURLS+roomId);

			logger.info("send binary to urls:{}",urls);
			if(urls != null && !urls.isEmpty()){
				for(String url:urls){
					try{
						byte[] bytes = new byte[buf.length];
						System.arraycopy(buf, 0, bytes, 0, buf.length);
						wsClient.sendBinary(url, bytes, clientId);
					} catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}
}
