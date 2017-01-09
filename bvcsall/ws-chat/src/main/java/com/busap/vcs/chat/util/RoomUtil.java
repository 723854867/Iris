//package com.busap.vcs.chat.util;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//import com.busap.vcs.chat.service.ConsumerService;
//
//public class RoomUtil {
//	private static Logger logger = Logger.getLogger(RoomUtil.class);
//	
//	public static List<String> rooms = new ArrayList<String>();
//	
//	private static ConsumerService consumerService;
//	
//	static{
//		consumerService = (ConsumerService)SpringUtils.getBean("kafkaConsumer");
//	}
//	/**
//	 * 初始化房间，启动kafka消息接收
//	 * @param roomId
//	 */
//	public static void initRoom(String roomId){
//		if(rooms.contains(roomId)){
//			logger.info("房间已存在！");
//			return;
//		}
//		rooms.add(roomId);
//		//初始化房间kafka消费者
//		consumerService.createKafkaConsumer(roomId);
//		try {
//			Thread.sleep(1000);//等待1秒，等待kafka消费者初始化完成
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * 销毁房间，关闭房间kafka消费者，清理房间用户连接
//	 * @param roomId
//	 */
//	public static void destroyRoom(String roomId){
//		consumerService.stopCosumer(roomId);
//		try {
//			Thread.sleep(2000);//
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		if(ChatUtil.roomChannels.containsKey(roomId)){
////			while(ChatUtil.roomChannels.get(roomId)!=null && ChatUtil.roomChannels.get(roomId).size()>0){
////				ChatUtil.removeUser(ChatUtil.roomChannels.get(roomId).get(0));
////			}
////			ChatUtil.roomChannels.remove(roomId);
////		}
//		rooms.remove(roomId);
//	}
//}
