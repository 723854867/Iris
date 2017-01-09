package com.busap.vcs.chat.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.base.WsMessage;
import com.busap.vcs.chat.bean.BinaryMessage;
import com.busap.vcs.chat.bean.PubParameter;
import com.busap.vcs.chat.service.JedisService;
import com.busap.vcs.chat.service.kafka.producer.KafkaProducer;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.MessageConst;

public class MessUtil {
	private static Logger logger = LoggerFactory.getLogger(MessUtil.class);
	private static Map<String,String> praiseNotice;
	private static Map<String,String> userNumNotice;
	
	private static long PRAISE_LOAD_TIME = 0;	
	private static long USERNUM_LOAD_TIME = 0;
	
	private static KafkaProducer producer;
	
	private static JedisService jedisService;
	
	static{
		producer = (KafkaProducer)SpringUtils.getBean("chatProducer");
		jedisService = (JedisService)SpringUtils.getBean("jedisService");
	}
	
	public static Map<String,String> getPraiseNotice(){
		long now = System.currentTimeMillis();
		if(PRAISE_LOAD_TIME == 0 || (now-PRAISE_LOAD_TIME)>300000){
			praiseNotice = jedisService.getMapByKey(BicycleConstants.LIVE_PRAISE_COUNT);
			PRAISE_LOAD_TIME = now;
		}
		
		return praiseNotice;
	}
	
	public static Map<String,String> getUserNumNotice(){
		long now = System.currentTimeMillis();
		if(USERNUM_LOAD_TIME == 0 || (now-USERNUM_LOAD_TIME)>300000){
			userNumNotice = jedisService.getMapByKey(BicycleConstants.LIVE_VIEWER_COUNT);
			USERNUM_LOAD_TIME = now;
		}
		
		return userNumNotice;
	}
	/**
	 * 发送字符串
	 */
	public static void send(Channel chn, WsMessage message) {
		if (chn != null && chn.isOpen()) {
			chn.write(new TextWebSocketFrame(message.toString()));
			chn.flush();
		} else {
			ChatUtil.removeUser(chn);
		}
	}
	/**
	 * 发送二进制
	 * @param chn
	 * @param buf
	 */
	public static void sendBinary(Channel chn,ByteBuf buf){
		if (chn != null && chn.isOpen()) {
			chn.write(new BinaryWebSocketFrame(buf));
			chn.flush();
		} else {
			ChatUtil.removeUser(chn);
		}
	}
	//推送用户进入房间信息
		public static void sendUserAdd(String uid,String roomId,Map<String,String> userInfo){
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
			
			sendToMessageQueue(m);
			logger.debug("用户 {} 进入房间，{}",uid,m.toString());
		}
		
		public static void sendUserLeave(String uid,String roomId,Map<String,String> userInfo){
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
			
			sendToMessageQueue(m);
			logger.debug("用户 {} 离开房间，{}",uid,m.toString());
		}
	
	/**
	 * 发送到对应房间消息队列
	 * @param message
	 */
	public static void sendToMessageQueue(WsMessage message){
		
		producer.send(message);
		
		PubParameter.recieveLog.info("send to kafka message adapter:"+message);
	}
	
	/**
	 * 接收消息中间件消息，进行分发
	 * @param message
	 */
	public static void recieveMessage(WsMessage message){
		switch(message.getCode()){
			case MessageConst.CHAT:			//聊天互动
				recieveChatMess(message);
				break;
			case MessageConst.LIVE:			//直播解析
				recieveLiveMess(message);
				break;
			case MessageConst.ADMIN:			//管理指令
				recieveAdminMess(message);
				break;
			case MessageConst.COMMEN:			//系统指令
				recieveCommentMess(message);
				break;
			case MessageConst.BROADCAST:			//广播指令
				recieveBroadcastMess(message);
				break;
			case MessageConst.PROPS:			//礼物道具指令
				recievePropsMess(message);
				break;
			case MessageConst.P2P:			//礼物道具指令
				recieveP2pMess(message);
				break;
			default:
				logger.info("未知指令：" + message.toString());
		}
	}

	/**
	 * 解析单聊消息
	 * @param message
	 */
	private static void recieveP2pMess(WsMessage message) {
		String receiver = message.getRecieverId();
		Channel chn = ChatUtil.uidChannel.get(receiver);
		if(chn == null){
			return;
		}
		switch(message.getChildCode()){
			case MessageConst.P2P_NOTICE: 	//私聊通知
				send(chn, message);
				break;
			case MessageConst.P2P_TEXT:
				send(chn, message);
				break;
			case MessageConst.P2P_AUDIO:
				send(chn, message);
				break;
			case MessageConst.P2P_IMG:
				send(chn, message);
				break;
			case MessageConst.P2P_VIDEO:
				send(chn, message);
				break;
		}
	}

	private static void recievePropsMess(WsMessage message) {
		switch(message.getChildCode()){
			case MessageConst.PROPS_GIFT: 	//送礼物
				ChatUtil.messageSender.getMessageQueue().add(message);
				break;
			case MessageConst.PROPS_CONTRIBUTION: 	//发送贡献榜前三名
				ChatUtil.messageSender.getMessageQueue().add(message);
				break;
		}
		
	}

	/**
	 * 系统公告、房间公告
	 * @param message
	 */
	private static void recieveBroadcastMess(WsMessage message) {
		ChatUtil.messageSender.getMessageQueue().add(message);
	}

	/**
	 * 处理收到的系统指令
	 * @param message
	 */
	private static void recieveCommentMess(WsMessage message) {
		switch(message.getChildCode()){
			case MessageConst.COMMEN_USERADD://添加新用户进入房间
				ChatUtil.messageSender.getMessageQueue().add(message);
				
				break;
			case MessageConst.COMMEN_PRAISENUM://点赞数
				ChatUtil.messageSender.getMessageQueue().add(message);
				break;
			case MessageConst.COMMEN_USERNUM://房间用户数
				ChatUtil.messageSender.getMessageQueue().add(message);
				break;
			case MessageConst.COMMEN_USERREM:
				ChatUtil.messageSender.getMessageQueue().add(message);
				break;
			case MessageConst.COMMEN_NOTICE:
				ChatUtil.messageSender.getMessageQueue().add(message);
				break;
			case MessageConst.COMMEN_ATTENTION:
				if(StringUtils.isNotBlank(message.getRoomId()) 
					&& StringUtils.isNotBlank(message.getRecieverId())){
					ChatUtil.messageSender.getMessageQueue().add(message);
				}
				break;
			case MessageConst.COMMEN_RELOGIN:
				String uid = message.getSenderId();
				if(ChatUtil.uidChannel.containsKey(uid)){
					Channel chn = ChatUtil.uidChannel.get(uid);
					send(chn,message);
				}
				break;
		}
		
	}
	/**
	 * 房主及管理员管理直播用户
	 * @param message
	 */
	private static void recieveAdminMess(WsMessage message) {
		switch(message.getChildCode()){
			case MessageConst.ADMIN_BAN://禁言
				ChatUtil.messageSender.getMessageQueue().add(message);
				break;
			case MessageConst.ADMIN_KICK://踢人
				ChatUtil.messageSender.getMessageQueue().add(message);
//				if(ChatUtil.uidChannel.containsKey(message.getRecieverId())){
//					logger.debug("用户 {}被主播{}踢出房间{}",message.getRecieverId(),message.getSenderId(),message.getRoomId());
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					ChatUtil.removeUser(ChatUtil.uidChannel.get(message.getRecieverId()));
//				}
				break;
			case MessageConst.ADMIN_ENDLIVE://终结直播
				String uid = message.getRecieverId();
				if(StringUtils.isNotBlank(uid)){
					jedisService.deleteSortedSetItemFromShard(BicycleConstants.ROOM_ORDER, message.getRoomId());
					send(ChatUtil.uidChannel.get(uid), message);
					logger.info("管理员结束房间{}直播",message.getRoomId());
				}
				break;
		}
		
	}

	private static void recieveLiveMess(WsMessage message) {
		switch(message.getChildCode()){
			case MessageConst.LIVE_START:
				ChatUtil.messageSender.getMessageQueue().add(message);
				break;
			case MessageConst.LIVE_END:
				ChatUtil.messageSender.getMessageQueue().add(message);
	//			RoomUtil.destroyRoom(message.getRoomId());
				break;
			case MessageConst.LIVE_TIMEOUT:
				ChatUtil.messageSender.getMessageQueue().add(message);
				break;
			case MessageConst.LIVE_GOON:
				ChatUtil.messageSender.getMessageQueue().add(message);
				break;
		}
		
	}

	/**
	 * 聊天及点赞处理
	 * @param message
	 */
	private static void recieveChatMess(WsMessage message) {
		ChatUtil.messageSender.getMessageQueue().add(message);	
	}
	
	public static void sendNotice(String roomId, String notice,String title,Channel chn) {
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.COMMEN);
		m.setChildCode(MessageConst.COMMEN_NOTICE);
		m.setRoomId(roomId);
		m.setContent(notice);
		m.setTitle(title);
				
		if(chn != null){
			send(chn,m);
		} else {
			sendToMessageQueue(m);
		}
		logger.debug("房间{}提示消息，{}",roomId,m.toString());
	}
	
	public static void sendLoopNotice(String roomId, String notice,String title) {
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.BROADCAST);
		m.setChildCode(MessageConst.BROADCAST_ROOM);
		m.setRoomId(roomId);
		m.setContent(notice);
		m.setTitle(title);
		
		sendToMessageQueue(m);
		
		logger.debug("房间{}提示消息，{}",roomId,m.toString());
	}
	/**
	 * 推送房间的在线人数
	 * @param roomId
	 */
	public static void sendRoomUserCount(String roomId,Channel chn) {
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
		if(onlineNum == 0 && maxNum == 0){
			return;
		}
		
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.COMMEN);
		m.setChildCode(MessageConst.COMMEN_USERNUM);
		m.setRoomId(roomId);
		m.getExtra().put("maxAccessNumber", maxNum);
		m.getExtra().put("onlineNumber", onlineNum<0?0:onlineNum);
		if(chn != null){
			send(chn,m);
		} else {
			List<Channel> channels = ChatUtil.roomChannels.get(roomId);
			if(channels != null && channels.size()>0){
				for(Channel channel:channels){
					try{
						send(channel,m);
					}catch(Exception ex){
						logger.error("send user count message error."+ex.getStackTrace(),ex);
					}
				}
			}
		}
	}

	/**
	 * 发送点赞总数
	 * @param uid
	 * @param roomId
	 */
	public static void sendPraisNumber(Channel chn,String roomId) {
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.COMMEN);
		m.setChildCode(MessageConst.COMMEN_PRAISENUM);
		m.setRoomId(roomId);
		m.getExtra().put("praiseNumber",jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "praiseNumber"));
		
//		sendToMessageQueue(roomId, m);
		send(chn, m);		
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
		
		sendToMessageQueue(m);
	}
	
	/**
	 * 已禁言用户再次发言时返回提示
	 * @param uid
	 * @param roomId
	 */
	public static void sendAntiTalk(Channel chn,String uid, String roomId) {
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.ADMIN);
		m.setChildCode(MessageConst.ADMIN_BAN);
		m.setRoomId(roomId);
		m.setRecieverId(uid);
		m.setContent("已被禁言");
		
		send(chn,m);
	}
	/**
	 * 用户在其他设备重复登录，通知用户并结束正在进行的直播
	 * @param channel
	 * @param uid
	 */
	public static void sendRelogin(String oldUri, String uid) {
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.COMMEN);
		m.setChildCode(MessageConst.COMMEN_RELOGIN);
		m.setRecieverId(uid);
		m.setContent("您的账号已经在其他设备上登录,当前不能重复使用。");
		m.getExtra().put("oldUri", oldUri);
		
		sendToMessageQueue(m);
		
	}
	/**
	 * 房间已下线提示
	 * @param chn
	 * @param roomId
	 */
	public static void sendConnFail(Channel chn, String roomId) {
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.COMMEN);
		m.setChildCode(MessageConst.COMMEN_CONNFAIL);
		m.setRoomId(roomId);
		m.setContent("正在观看的直播已结束");
		m.setTitle("提示");
		
		send(chn,m);
	}

	public static void sendPraiseIncrement(String roomId, Long praiseNum) {
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.CHAT);
		m.setChildCode(MessageConst.CHAT_PRAISE);
		m.setRoomId(roomId);
		m.getExtra().put("increment", praiseNum);
		logger.info("praise increment:{}",m.toString());
		sendToMessageQueue(m);
	}

	public static void sendLiveTimeout(String roomId, Channel chn) {
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.LIVE);
		m.setChildCode(MessageConst.LIVE_TIMEOUT);
		m.setRoomId(roomId);
		m.setContent("我马上就回来，等我一下下哦~");
		m.setTitle("提示");
		
		send(chn,m);
	}
	
	/**
	 * 直播掉线
	 * @param roomId
	 * @param chn
	 */
	public static void sendLiveDown(String roomId, Channel chn) {
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.LIVE);
		m.setChildCode(MessageConst.LIVE_DOWN);
		m.setRoomId(roomId);
		m.setContent("网络不给力,直播已掉线,请重新开始直播!");
		m.setTitle("提示");
		
		send(chn,m);
	}
	/**
	 * 分发到房间
	 * @param roomId
	 * @param message
	 */
	public static void sendToRoom(String roomId,WsMessage message){
		List<Channel> channels = ChatUtil.roomChannels.get(roomId);
		if(channels != null && channels.size()>0){
			for(Channel chn:channels){
				send(chn,message);
			}
		}
	}

	/**
	 * 用户连接消息
	 * @param uid
	 */
	public static void sendUserConn(String uid) {
		WsMessage m = new WsMessage();
		m.setSenderId(uid);
		m.setCode(MessageConst.COMMEN);
		m.setChildCode(MessageConst.COMMEN_USERCONN);
		sendToMessageQueue(m);
	}

	public static void recieveBinaryMessage(ByteBuf buf) {
		byte array[] = new byte[buf.readableBytes()];
		buf.readBytes(array, 0, buf.readableBytes());//.readBytes(array);//.array();
		if(array != null && array.length>32){
			byte b1[] = new byte[4];
			System.arraycopy(array, 0, b1, 0, 4);
			int type = ByteBuffer.wrap(b1).getInt();
			if(type == 1){
				byte rid[] = new byte[8];
				System.arraycopy(array, 12, rid, 0, 8);
				long roomId = ByteBuffer.wrap(rid).getLong();
//				byte data[] = new byte[array.length-32];
//				System.arraycopy(array, 32, data, 0, array.length-32);
//				
//				String s = GZipUtil.readCompressObject(data);
//				System.out.println("unzip data:"+s);
//				
				BinaryMessage bm = new BinaryMessage();
				bm.setContent(array);
				bm.setRoomId(roomId+"");
				bm.setType("1");
				
				ChatUtil.binarySender.getMessageQueue().add(bm);
			} else if(type == 0){
				byte uid[] = new byte[8];
				System.arraycopy(array, 12, uid, 0, 8);
				long userId = ByteBuffer.wrap(uid).getLong();
				
				Channel chn = ChatUtil.uidChannel.get(String.valueOf(userId));
				
				sendBinary(chn, Unpooled.copiedBuffer(array));
			}
		} else{
			logger.warn("解析二进制消息错误.");
		}
		
	}

	public static void sendLiveGoon(String uid, String roomId) {
		WsMessage m = new WsMessage();
		m.setSenderId(uid);
		m.setCode(MessageConst.LIVE);
		m.setChildCode(MessageConst.LIVE_GOON);
		m.setRoomId(roomId);
		sendToMessageQueue(m);
	}
	
}