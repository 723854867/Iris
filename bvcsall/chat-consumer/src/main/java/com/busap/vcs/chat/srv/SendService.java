package com.busap.vcs.chat.srv;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.busap.vcs.base.WsMessage;
import com.busap.vcs.chat.srv.wsclient.MyWebSocketClient;
import com.busap.vcs.chat.util.GZipUtil;
import com.busap.vcs.chat.util.LiveCheckThread;
import com.busap.vcs.chat.util.MessageMergeUtil;
import com.busap.vcs.chat.util.RoomUtil;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.MessageConst;

@Service("sendService")
public class SendService {

	private static Logger logger = LoggerFactory.getLogger(SendService.class);
		
	private static MyWebSocketClient wsClient = MyWebSocketClient.getInstance();
		
	@Value("#{configProperties['client_id']}")
	private String clientId; // can be setting by spring
	
	@Resource(name = "p2pService")
	private P2pService p2pService;
	
	@Resource(name = "liveService")
	private LiveService liveService;

	@Resource(name = "jedisService")
	private JedisService jedisService;

	public void sendMessage(WsMessage message) throws IOException {
		
		switch(message.getCode()){
			case MessageConst.CHAT:			//收到聊天指令
				doChatParse(message);
				break;
			case MessageConst.LIVE:			//收到直播指令
				doLiveParse(message);
				break;
			case MessageConst.COMMEN:		//收到系统指令
				doCommenParse(message);
				break;
			case MessageConst.BROADCAST:	//收到广播指令
				doBroadcastParse(message);
				break;
			case MessageConst.ADMIN:		//收到管理指令
				doAdminParse(message);
				break;
			case MessageConst.PROPS:		//收到道具指令
				doPropsParse(message);
				break;
			case MessageConst.P2P:			//点对点私聊消息
				doP2PParse(message);
				break;
			case MessageConst.CONNMK:		//连麦消息
				doConnMK(message);
				break;
				
			default:
				logger.info("未知指令：" + message);
		}
		
	}
	
	/**
	 * 连麦处理
	 * @param message
	 */
	private void doConnMK(WsMessage message) {
		switch(message.getChildCode()){
			case MessageConst.CONNMK_ACCEDE:
				String anchorId = message.getSenderId();
				String recieverId = message.getRecieverId();
				String position = String.valueOf(message.getExtra().get("position"));//连麦位置，取值1或2，其他取值无效
				String streamId = (String)message.getExtra().get("streamId");	//主播流ID
				if(StringUtils.isNotBlank(anchorId) && StringUtils.isNotBlank(recieverId) 
						&& StringUtils.isNotBlank(position) && StringUtils.isNotBlank(streamId)){
					
					String pullUrlMain = jedisService.get(BicycleConstants.LINK_MIC_PULL_MAIN);
					String pwd = jedisService.get(BicycleConstants.LINK_MIC_PULL_PWD);
					String pullUrl1 = null;
					String pullUrl2 = null;
					String pushUrl1 = null;
					String pushUrl2 = null;
					
					if(StringUtils.isNotBlank(pullUrlMain)){
						pullUrlMain = MessageFormat.format(pullUrlMain, streamId);
						message.getExtra().put("pullUrlMain", pullUrlMain);	//拉主播流地址
					} else {
						logger.warn("anchor Stream url is null.{}",message);
						return;
					}
					
					if("1".equals(position)){
						pullUrl1 = jedisService.get(BicycleConstants.LINK_MIC_PULL_ONE);
						pullUrl1 = MessageFormat.format(pullUrl1, streamId) ;
						message.getExtra().put("pullUrl1", pullUrl1);
						
						pushUrl1 = jedisService.get(BicycleConstants.LINK_MIC_PUSH_ONE);
						pushUrl1 = MessageFormat.format(pushUrl1, streamId);
						message.getExtra().put("pushUrl1", pushUrl1);
						
						String anthorId = jedisService.getValueFromMap(BicycleConstants.LINK_MIC_PU + anchorId, "2");
						if(StringUtils.isNotBlank(anthorId)){
							pushUrl2 = jedisService.get(BicycleConstants.LINK_MIC_PUSH_SECOND);
							pushUrl2 = MessageFormat.format(pushUrl2, streamId);
							message.getExtra().put("pushUrl2", pushUrl2);
							
							sendToUser(anthorId,message);//发给另一个连麦者
						}
						jedisService.setValueToMap(BicycleConstants.LINK_MIC_PU + anchorId, "1", recieverId);
						
					} else if("2".equals(position)){
						pullUrl2 = jedisService.get(BicycleConstants.LINK_MIC_PULL_SECOND);
						pullUrl2 = MessageFormat.format(pullUrl2, streamId) ;
						message.getExtra().put("pullUrl2", pullUrl2);
						
						pushUrl2 = jedisService.get(BicycleConstants.LINK_MIC_PUSH_SECOND);
						pushUrl2 = MessageFormat.format(pushUrl2, streamId);
						message.getExtra().put("pushUrl2", pushUrl2);
						
						String anthorId = jedisService.getValueFromMap(BicycleConstants.LINK_MIC_PU + anchorId, "1");
						if(StringUtils.isNotBlank(anthorId)){
							pushUrl1 = jedisService.get(BicycleConstants.LINK_MIC_PUSH_ONE);
							pushUrl1 = MessageFormat.format(pushUrl1, streamId);
							message.getExtra().put("pushUrl1", pushUrl1);
							
							sendToUser(anthorId,message);//发给另一个连麦者
						}
					
						jedisService.setValueToMap(BicycleConstants.LINK_MIC_PU + anchorId, "2", recieverId);
					} else {
						logger.info("access invalid position:{}",message);
						return;
					}
					sendToUser(anchorId,message);		//发给主播
					sendToUser(recieverId,message);		//发给连麦者
					
//					message.getExtra().put("pullUrl1", pullUrl1);		//拉位置1流地址
//					message.getExtra().put("pullUrl2", pullUrl2);		//拉位置2流地址
//					message.getExtra().put("pushUrl1", pushUrl1);		//位置1推流地址
//					message.getExtra().put("pushUrl2", pushUrl2);		//位置2推流地址
				}
				break;
			case MessageConst.CONNMK_STOP:
				String senderId = message.getSenderId();
				anchorId = message.getRecieverId();
				position = String.valueOf(message.getExtra().get("position"));//连麦位置，取值1或2，其他取值无效
				if("1".equals(position)){
					jedisService.deleteValueFromMap(BicycleConstants.LINK_MIC_PU + senderId, "1");
					String anthorId = jedisService.getValueFromMap(BicycleConstants.LINK_MIC_PU + anchorId, "2");
					if(StringUtils.isNotBlank(anthorId)){
						sendToUser(anthorId,message);//发给另一个连麦者
					}
				} else if("2".equals(position)){
					jedisService.deleteValueFromMap(BicycleConstants.LINK_MIC_PU + anchorId, "2");
					String anthorId = jedisService.getValueFromMap(BicycleConstants.LINK_MIC_PU + anchorId, "1");
					if(StringUtils.isNotBlank(anthorId)){
						sendToUser(anthorId,message);//发给另一个连麦者
					}
				} else {
					logger.info("stop link mic invalid position:{}",message);
					return;
				}
				sendToUser(anchorId,message);	
				sendToUser(senderId,message);
				break;
			case MessageConst.CONNMK_SHUTDOWN:
				anchorId = message.getSenderId();
				recieverId = message.getRecieverId();
				position = String.valueOf(message.getExtra().get("position"));//连麦位置，取值1或2，其他取值无效
				if("1".equals(position)){
					jedisService.deleteValueFromMap(BicycleConstants.LINK_MIC_PU + anchorId, "1");
					String anthorId = jedisService.getValueFromMap(BicycleConstants.LINK_MIC_PU + anchorId, "2");
					if(StringUtils.isNotBlank(anthorId)){
						sendToUser(anthorId,message);//发给另一个连麦者
					}
				} else if("2".equals(position)){
					jedisService.deleteValueFromMap(BicycleConstants.LINK_MIC_PU + anchorId, "2");
					String anthorId = jedisService.getValueFromMap(BicycleConstants.LINK_MIC_PU + anchorId, "1");
					if(StringUtils.isNotBlank(anthorId)){
						sendToUser(anthorId,message);//发给另一个连麦者
					}
				} else {
					logger.info("stop link mic invalid position:{}",message);
					return;
				}
				sendToUser(anchorId,message);	
				sendToUser(recieverId,message);
				break;
			case MessageConst.CONNMK_REQUEST:
				sendToUser(message.getRecieverId(),message);
				break;
			case MessageConst.CONNMK_CANCLE:
				sendToUser(message.getRecieverId(),message);
				break;
		}
				
	}

	private void doCommenParse(WsMessage message) {
		switch(message.getChildCode()){
			case MessageConst.COMMEN_USERCONN://用户连接成功
				String uid = message.getSenderId();
				if(StringUtils.isNotBlank(uid)){
					Integer count = p2pService.getUnreadCount(uid);
					WsMessage m = new WsMessage();
					m.setCode(MessageConst.P2P);
					m.setChildCode(MessageConst.P2P_NOTICE);
					m.setRecieverId(uid);
					m.setContent(count.toString());
					sendToUser(uid,m);
				}
				break;
			case MessageConst.COMMEN_USERADD://用户进入房间
				String roomId = message.getRoomId();
				uid = message.getSenderId();
				if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(roomId)){
					//TODO save DB
//					RoomUtil.sendToRoom(message,clientId);
					MessageMergeUtil.addMessage(roomId, message);
					
					RoomUtil.incUserCount(roomId);

					jedisService.setValueToSetInShard("ROOM_USER_ALL_"+roomId, uid);//记录UV
						
					long realUser = jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "realUserCount");
					
					String anchorId = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "creatorId");
					if(StringUtils.isNotBlank(anchorId)){
						String liveSetting = jedisService.getValueFromMap(BicycleConstants.USER_INFO+anchorId, "liveSetting");
						if(StringUtils.isBlank(liveSetting) || "-1".equals(liveSetting) || 
								"1".equals(jedisService.getValueFromMap(BicycleConstants.LIVE_SETTING+liveSetting, "status"))){//未绑定机器人设置，使用通用设置初始化
							if(jedisService.keyExists(BicycleConstants.LIVE_SETTING)){
								Map<String,String> commenSetting = jedisService.getMapByKey(BicycleConstants.LIVE_SETTING);
								String stat = commenSetting.get("commenStat");
								if("0".equals(stat)){
									String minMajia = commenSetting.get("minMajiaCount");
									String minReal = commenSetting.get("minRealUser");
									String period = commenSetting.get("period");
									String maxPeriod = commenSetting.get("maxPeriod");
									String maxReal = commenSetting.get("maxRealUser");
									String step = commenSetting.get("step");
									String maxStep = commenSetting.get("maxStep");
									int stp = 0;
									realUser = realUser - 1;//去掉主播
									if(realUser < Integer.parseInt(minReal) && !jedisService.isSetMemberInShard("CommenSettingInit", roomId)){
										stp = Integer.parseInt(minMajia);
										jedisService.setValueToSetInShard("CommenSettingInit", roomId);
										logger.info("init commen setting in room:{}",roomId);
									}else if(realUser>=Integer.parseInt(minReal) && realUser<=Integer.parseInt(maxReal)){
										stp = Integer.parseInt(step);
									}else if(realUser>Integer.parseInt(maxReal)){
										stp = Integer.parseInt(maxStep);
									}
									if(stp>0 && StringUtils.isNumeric(period) && StringUtils.isNumeric(maxPeriod)){
										logger.info("commen majia setting excuting add.realuser:{},setting:{}",realUser,commenSetting);
										MajiaCommenService commen = new MajiaCommenService(roomId,stp,Integer.parseInt(period),Integer.parseInt(maxPeriod),Boolean.TRUE,jedisService);
										new Thread(commen).start();
									}
								}else{
									logger.info("默认马甲设置未生效,setting:{}",commenSetting);
								}
							}
						}
					}
				}
				break;
			case MessageConst.COMMEN_USERREM://用户离开房间
				roomId = message.getRoomId();
				uid = message.getSenderId();
				if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(roomId)){
					//TODO save DB
//					RoomUtil.sendToRoom(message, uid);
					MessageMergeUtil.addMessage(roomId, message);
					
					RoomUtil.decUserCount(roomId);
					
					long realUser = jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "realUserCount",-1);
					
					String anchorId = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "creatorId");
					if(StringUtils.isNotBlank(anchorId)){
						String liveSetting = jedisService.getValueFromMap(BicycleConstants.USER_INFO+anchorId, "liveSetting");
						if(StringUtils.isBlank(liveSetting) || "-1".equals(liveSetting)|| 
								"1".equals(jedisService.getValueFromMap(BicycleConstants.LIVE_SETTING+liveSetting, "status"))){//未绑定机器人设置，使用通用设置初始化
							if(jedisService.keyExists(BicycleConstants.LIVE_SETTING)){
								Map<String,String> commenSetting = jedisService.getMapByKey(BicycleConstants.LIVE_SETTING);
								String stat = commenSetting.get("commenStat");
								if("0".equals(stat)){
									String minReal = commenSetting.get("minRealUser");
									String period = commenSetting.get("period");
									String maxPeriod = commenSetting.get("maxPeriod");
									String maxReal = commenSetting.get("maxRealUser");
									String step = commenSetting.get("step");
									String maxStep = commenSetting.get("maxStep");
									int stp = 0;
									if(realUser>=Integer.parseInt(minReal) && realUser<=Integer.parseInt(maxReal)){
										stp = Integer.parseInt(step);
									}else if(realUser>Integer.parseInt(maxReal)){
										stp = Integer.parseInt(maxStep);
									}
									if(stp>0 && StringUtils.isNumeric(period) && StringUtils.isNumeric(maxPeriod)){
										logger.info("commen majia setting excuting remove.realuser:{},setting:{}",realUser,commenSetting);
										MajiaCommenService commen = new MajiaCommenService(roomId,stp,Integer.parseInt(period),Integer.parseInt(maxPeriod),Boolean.FALSE,jedisService);
										new Thread(commen).start();
									}
								}else{
									logger.info("默认马甲设置未生效,setting:{}",commenSetting);
								}
							}
						}
					}
				}
				break;
			case MessageConst.COMMEN_ATTENTION:
				String reciever = message.getRecieverId();
				if(StringUtils.isNotBlank(reciever)){
					sendToUser(message.getRecieverId(),message);
				}
				break;
				
			default:
//				RoomUtil.sendToRoom(message,clientId);
				MessageMergeUtil.addMessage(message.getRoomId(), message);
		}
				
	}

	private void doLiveParse(WsMessage message) {
		switch(message.getChildCode()){			
			case MessageConst.LIVE_START:
				String uid = message.getSenderId();
				String roomId = message.getRoomId();
				if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(roomId)){
					liveService.doLiveStart(uid, roomId);
				}
				break;
			case MessageConst.LIVE_END:
				uid = message.getSenderId();
				roomId = message.getRoomId();
				if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(roomId)){
//					RoomUtil.sendToRoom(message,clientId);
					byte[] buf = GZipUtil.buildRoomChat(roomId, message);
					RoomUtil.sendBinaryToRoom(roomId, buf);
					
					jedisService.delete(BicycleConstants.ROOM_SERVERURLS+roomId);
//	回调http				
//					liveService.doLiveEnd(uid, roomId);
					logger.info("用户 {} 房间{}直播已结束",uid,roomId);
				}
				break;
			case MessageConst.LIVE_TIMEOUT:
//				RoomUtil.sendToRoom(message,clientId);
				uid = message.getSenderId();
				roomId = message.getRoomId();
				if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(roomId)){
					byte[] buf = GZipUtil.buildRoomChat(roomId, message);
					RoomUtil.sendBinaryToRoom(roomId, buf);
				}
				break;
			case MessageConst.LIVE_GOON:
//				RoomUtil.sendToRoom(message,clientId);
				uid = message.getSenderId();
				roomId = message.getRoomId();
				if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(roomId)){
					byte[] buf = GZipUtil.buildRoomChat(roomId, message);
					RoomUtil.sendBinaryToRoom(roomId, buf);
				}
				break;
			case MessageConst.LIVE_ANCHORLEAVE:
				uid = message.getSenderId();
				roomId = message.getRoomId();
				if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(roomId)){
					LiveCheckThread lct = new LiveCheckThread(uid,roomId,jedisService,liveService);
					new Thread(lct).start();

					WsMessage m = new WsMessage();
					m.setSenderId(uid);
					m.setCode(MessageConst.LIVE);
					m.setChildCode(MessageConst.LIVE_TIMEOUT);
					m.setRoomId(roomId);
					
					byte[] buf = GZipUtil.buildRoomChat(roomId, message);
					RoomUtil.sendBinaryToRoom(roomId, buf);
				}
				break;
		}
		
	}

	private void doChatParse(WsMessage message) {
		//TODO save to db
//		RoomUtil.sendToRoom(message,clientId);
		MessageMergeUtil.addMessage(message.getRoomId(), message);
		if(MessageConst.CHAT_PUBLIC.equals(message.getChildCode()) || MessageConst.CHAT_BARRAGE.equals(message.getChildCode())){
			if(message.getSenderId()!=null 
					&& message.getSenderId().equals(jedisService.getValueFromMap(BicycleConstants.ROOM_+message.getRoomId(), "creatorId"))){
				jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+message.getRoomId(), "anchorChatCount");
			} else {
				jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+message.getRoomId(), "chatCount");
			}
		}
	}

	private void doBroadcastParse(WsMessage message) {
		//TODO save to db
//		RoomUtil.sendToRoom(message,clientId);
		MessageMergeUtil.addMessage(message.getRoomId(), message);
	}

	private void doAdminParse(WsMessage message) {
		String roomId = message.getRoomId();
		
		switch(message.getChildCode()){
			case MessageConst.ADMIN_BAN:
				//验证禁言者身份、是主播才可以
				if(StringUtils.isNotBlank(message.getSenderId()) && message.getSenderId().equals(jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "creatorId")) 
						&& !jedisService.isSetMemberInShard(BicycleConstants.SUPERADMINS, message.getRecieverId())){
					String key = "RoomBan_"+roomId+"_"+message.getRecieverId();
					jedisService.set(key, "1");
					jedisService.expire(key, 300);
					sendToUser(message.getRecieverId(),message);
				}else{
					logger.info("禁言失败，没有权限 ,主播：{} 用户：{} 房间：{}",message.getSenderId(),message.getRecieverId(),roomId);
				}
				break;
			case MessageConst.ADMIN_KICK:
				//验证禁言者身份、是主播才可以
				if(StringUtils.isNotBlank(message.getSenderId()) && message.getSenderId().equals(jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "creatorId"))
						&& !jedisService.isSetMemberInShard(BicycleConstants.SUPERADMINS, message.getRecieverId())){
					message.setContent("成功将"+ message.getRecieverName() +"请离直播间");
					sendToUser(message.getRecieverId(),message);
				}else{
					logger.info("踢人失败，没有权限，主播：{} 用户：{} 房间： {}",message.getSenderId(),message.getRecieverId(),roomId);
				}
				break;
			case MessageConst.ADMIN_ENDLIVE:
				roomId = message.getRoomId();
				String anchorId = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "creatorId");
				if(StringUtils.isNotBlank(anchorId)){
					sendToUser(anchorId,message);
				}
				break;
		}
	}

	private void doPropsParse(WsMessage message) {
		//TODO save to db
//		RoomUtil.sendToRoom(message,clientId);
		String roomId = message.getRoomId();
		byte[] buf = GZipUtil.buildRoomChat(roomId, message);
		RoomUtil.sendBinaryToRoom(roomId, buf);
	}

	private void doP2PParse(WsMessage message) {
		switch(message.getChildCode()){
			case MessageConst.P2P_CALLBACK:
				p2pService.readMsg(message);
				break;
			case MessageConst.P2P_TEXT:
				message.setMessageId(UUID.randomUUID().toString());
				p2pService.saveMsg(message);
				sendToUser(message.getRecieverId(),message);
				break;
			case MessageConst.P2P_IMG:
				message.setMessageId(UUID.randomUUID().toString());
				p2pService.saveMsg(message);
				sendToUser(message.getRecieverId(),message);
				break;
			case MessageConst.P2P_AUDIO:
				message.setMessageId(UUID.randomUUID().toString());
				p2pService.saveMsg(message);
				sendToUser(message.getRecieverId(),message);
				break;
			case MessageConst.P2P_VIDEO:
				message.setMessageId(UUID.randomUUID().toString());
				p2pService.saveMsg(message);
				sendToUser(message.getRecieverId(),message);
				break;
		}
		
	}



	private void sendToUser(String uid,WsMessage message){
		if(StringUtils.isBlank(uid)){
			logger.info("reciever not found.message:",message);
			return;
		}
		String url = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "chaturl");
		if(StringUtils.isBlank(url)){
			logger.info("unknown chat url for user {}.message:",uid,message);
			return;
		}
		try{
			logger.info("send p2p message:{}",message);
			byte data[] = GZipUtil.buildUserChat(uid, message);
			wsClient.sendBinary(url, data, clientId);
//			wsClient.sendMessage(url, message.toString(),clientId);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
