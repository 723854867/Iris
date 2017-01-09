//package com.busap.vcs.chat.service;
//
//import java.util.Set;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import redis.clients.jedis.Tuple;
//
//import com.alibaba.fastjson.JSONObject;
//import com.busap.vcs.base.WsMessage;
//import com.busap.vcs.chat.util.ChatUtil;
//import com.busap.vcs.chat.util.MessUtil;
//import com.busap.vcs.chat.util.SpringUtils;
//import com.busap.vcs.constants.BicycleConstants;
//
//public class ConsumerService {
//	private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);
//	//线程池
//	private ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
//	private static JedisService jedisService;
//	
//	static{
//		jedisService = (JedisService)SpringUtils.getBean("jedisService");
//	}
//	
//	
//	
//	public void runConsumer() {
//		logger.info("消息消费者启动");
//		//聊天消息消费
//		service.execute(new Runnable(){
//			@Override
//			public void run() {
//				logger.info("聊天消息消费启动");
//				long start = jedisService.incr(BicycleConstants.LIVE_MESSAGE_COUNT);
//				while(true){
//					try{
//						if(!ChatUtil.roomChannels.isEmpty()){//查找当前在线房间
//							Set<String> roomIds = ChatUtil.roomChannels.keySet();
//							for(String roomId:roomIds){
//								if(!ChatUtil.roomChannels.get(roomId).isEmpty()){
//									Set<Tuple> messList = jedisService.zrangeByScoreWithScores(BicycleConstants.LIVE_MESSAGE_QUEUE+roomId, start+1,Long.MAX_VALUE);
//									if(messList!=null && messList.size()>0){
//										for(Tuple tuple:messList){
//											start = (long)tuple.getScore();
//											String messageText = tuple.getElement();
//											WsMessage message = (WsMessage)JSONObject.parseObject(messageText, WsMessage.class);
//											MessUtil.recieveMessage(message);
//										}
//									}
//								}
//							}
//						}
//					}catch(Exception ex){
//						logger.error("聊天获取消息列表错误", ex);
//						ex.printStackTrace();
//						continue;
//					}
//					
//					try {
//						Thread.sleep(1L);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//		
//		//点赞消息消费
//		service.execute(new Runnable(){
//			@Override
//			public void run() {
//				logger.info("点赞消息消费启动");
//				long start = jedisService.incr(BicycleConstants.LIVE_PRAISE_MESSAGE_COUNT);
//				while(true){
//					try{
//						if(!ChatUtil.roomChannels.isEmpty()){//查找当前在线房间
//							Set<String> roomIds = ChatUtil.roomChannels.keySet();
//							for(String roomId:roomIds){
//								if(!ChatUtil.roomChannels.get(roomId).isEmpty()){
//									Set<Tuple> messList = jedisService.zrangeByScoreWithScores(BicycleConstants.LIVE_PRAISE_MESSAGE_QUEUE+roomId, start+1,Long.MAX_VALUE);
//									if(messList!=null && messList.size()>0){
//										for(Tuple tuple:messList){
//											start = (long)tuple.getScore();
//											String messageText = tuple.getElement();
//											WsMessage message = (WsMessage)JSONObject.parseObject(messageText, WsMessage.class);
//											MessUtil.recieveMessage(message);
//										}
//									}
//								}
//							}
//						}
//					}catch(Exception ex){
//						logger.error("点赞获取消息列表错误", ex);
//						ex.printStackTrace();
//						continue;
//					}
//					
//					try {
//						Thread.sleep(1L);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//		
//		//礼物消息消费
//		service.execute(new Runnable(){
//			@Override
//			public void run() {
//				logger.info("礼物消息消费启动");
//				long start = jedisService.incr(BicycleConstants.LIVE_GIFT_MESSAGE_COUNT);
//				while(true){
//					try{
//						if(!ChatUtil.roomChannels.isEmpty()){//查找当前在线房间
//							Set<String> roomIds = ChatUtil.roomChannels.keySet();
//							for(String roomId:roomIds){
//								if(!ChatUtil.roomChannels.get(roomId).isEmpty()){
//									Set<Tuple> messList = jedisService.zrangeByScoreWithScores(BicycleConstants.LIVE_GIFT_MESSAGE_QUEUE+roomId, start+1,Long.MAX_VALUE);
//									if(messList!=null && messList.size()>0){
//										for(Tuple tuple:messList){
//											start = (long)tuple.getScore();
//											String messageText = tuple.getElement();
//											WsMessage message = (WsMessage)JSONObject.parseObject(messageText, WsMessage.class);
//											MessUtil.recieveMessage(message);
//										}
//									}
//								}
//							}
//						}
//					}catch(Exception ex){
//						logger.error("礼物获取消息列表错误", ex);
//						ex.printStackTrace();
//						continue;
//					}
//					
//					try {
//						Thread.sleep(1L);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//		
//	}
//
//}
