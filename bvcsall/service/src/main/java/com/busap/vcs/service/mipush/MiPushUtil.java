package com.busap.vcs.service.mipush;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.data.entity.SystemMess;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Message.Builder;
import com.xiaomi.xmpush.server.Message.IOSBuilder;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;

public class MiPushUtil {

	private static final Logger logger = LoggerFactory.getLogger(MiPushUtil.class);
	
	private static Integer notifyid = 0;
	
	public static void sendMessage(SystemMess mess) throws IOException, ParseException {
		Constants.useOfficial();
		
	    if(StringUtils.isBlank(mess.getDestUser()) || "all".equals(mess.getDestUser())){
			broadcastAll(mess);
		}else{
			sendTargetMess(mess);
		}
		
	}
	
	/**
	 * 指定用户范围消息
	 * @param mess
	 * @throws ParseException 
	 * @throws IOException 
	 */
	private static void sendTargetMess(SystemMess mess) throws IOException, ParseException {
		sendToAndroid(mess);
		sendToIOS(mess);//推送给appstore正式版
		if(MipushConfig.IOS_APP_SECRET2 != null){
			sendToIOS_2(mess);//推送给工程版
		}
	}
	
	private static void sendToIOS(SystemMess mess) throws IOException, ParseException {
		if(MipushConfig.IOS_ISTEST){
			Constants.useSandbox();
		}
		Message message = buildIOSMessage(mess);
		Sender sender = new Sender(MipushConfig.IOS_APP_SECRET);
		String[] targetUsers = mess.getDestUser().split(";"); 
		int length = targetUsers.length;
		for(int i=0;i<targetUsers.length;i=i+1000){//sendToAlias每次只支持1000个别名
			String[] temp = null;
			if(length>1000){
				temp = new String[1000];
				System.arraycopy(targetUsers, i, temp, 0, 1000);
				length = length -1000;
			}else{
				temp = new String[length];
				System.arraycopy(targetUsers, i, temp, 0, length);
			}
			List<String> alias = Arrays.asList(temp);
			
			Result rs = sender.sendToAlias(message, alias, MipushConfig.RETRIES);
			logger.info("ios message::message push end.rs[id:"+rs.getMessageId()+",data:"+rs.getData()+",code:"+rs.getErrorCode().getName()+"]");
		}
		
	}

	private static void sendToIOS_2(SystemMess mess) throws IOException, ParseException {
		if(MipushConfig.IOS_ISTEST){
			Constants.useSandbox();
		}
		Message message = buildIOSMessage(mess);
		Sender sender = new Sender(MipushConfig.IOS_APP_SECRET2);
		String[] targetUsers = mess.getDestUser().split(";"); 
		int length = targetUsers.length;
		for(int i=0;i<targetUsers.length;i=i+1000){//sendToAlias每次只支持1000个别名
			String[] temp = null;
			if(length>1000){
				temp = new String[1000];
				System.arraycopy(targetUsers, i, temp, 0, 1000);
				length = length -1000;
			}else{
				temp = new String[length];
				System.arraycopy(targetUsers, i, temp, 0, length);
			}
			List<String> alias = Arrays.asList(temp);
			
			Result rs = sender.sendToAlias(message, alias, MipushConfig.RETRIES);
			logger.info("ios message::message push end.rs[id:"+rs.getMessageId()+",data:"+rs.getData()+",code:"+rs.getErrorCode().getName()+"]");
		}
		
	}

	private static void sendToAndroid(SystemMess mess) throws IOException, ParseException {
		Message message = buildMessage(mess);
		Sender sender = new Sender(MipushConfig.ANDROID_APP_SECRET);
		String[] targetUsers = mess.getDestUser().split(";"); 
		int length = targetUsers.length;
		for(int i=0;i<targetUsers.length;i=i+1000){//sendToAlias每次只支持1000个别名
			String[] temp = null;
			if(length>1000){
				temp = new String[1000];
				System.arraycopy(targetUsers, i, temp, 0, 1000);
				length = length -1000;
			}else{
				temp = new String[length];
				System.arraycopy(targetUsers, i, temp, 0, length);
			}
			List<String> alias = Arrays.asList(temp);
			
			Result rs = sender.sendToAlias(message, alias, MipushConfig.RETRIES);
			logger.info("android message::message push end.rs[id:"+rs.getMessageId()+",data:"+rs.getData()+",code:"+rs.getErrorCode().getName()+"]");
		}
	}

	/**
	 * 广播消息，推送所有人
	 * @param mess
	 * @throws ParseException 
	 * @throws IOException 
	 */
	private static void broadcastAll(SystemMess mess) throws IOException, ParseException {
		broadcastToAndroid(mess);
		broadcastToIOS(mess);//推送给appstore正式版
		if(MipushConfig.IOS_APP_SECRET2 != null){
			broadcastToIOS_2(mess);//推送给工程版
		}
	}

	/**
	 * IOS 系统广播
	 * @param mess
	 * @throws ParseException 
	 * @throws IOException 
	 */
	private static void broadcastToIOS(SystemMess mess) throws IOException, ParseException {
		if(MipushConfig.IOS_ISTEST){
			Constants.useSandbox();
		}
		Message message = buildIOSMessage(mess);
		Sender sender = new Sender(MipushConfig.IOS_APP_SECRET);

		Result rs = sender.broadcastAll(message, MipushConfig.RETRIES);
		logger.info("ios broadcase::message push end.rs[id:"+rs.getMessageId()+",data:"+rs.getData()+",code:"+rs.getErrorCode().getName()+"]");
		
	}
	
	private static void broadcastToIOS_2(SystemMess mess) throws IOException, ParseException {
		if(MipushConfig.IOS_ISTEST){
			Constants.useSandbox();
		}
		Message message = buildIOSMessage(mess);
		Sender sender = new Sender(MipushConfig.IOS_APP_SECRET2);

		Result rs = sender.broadcastAll(message, MipushConfig.RETRIES);
		logger.info("ios broadcase::message push end.rs[id:"+rs.getMessageId()+",data:"+rs.getData()+",code:"+rs.getErrorCode().getName()+"]");
		
	}


	/**
	 * android 系统广播
	 * @param mess
	 * @throws ParseException 
	 * @throws IOException 
	 */
	private static void broadcastToAndroid(SystemMess mess) throws IOException, ParseException {
		Message message = buildMessage(mess);
		Sender sender = new Sender(MipushConfig.ANDROID_APP_SECRET);
		
		Result rs = sender.broadcastAll(message, MipushConfig.RETRIES);
		logger.info("android broadcase::message push end.rs[id:"+rs.getMessageId()+",data:"+rs.getData()+",code:"+rs.getErrorCode().getName()+"]");
		
	}

	/**
	 * 创建android版消息
	 * @param mess
	 * @return
	 */
	private static Message buildMessage(SystemMess mess) {
		Builder builder = new Message.Builder();
		builder.title(mess.getTitle())
        .description(mess.getContent().length()>120?mess.getContent().substring(0,120):mess.getContent())
        .payload(mess.getContent())
        .restrictedPackageName(MipushConfig.ANDROID_PACKAGE)
        .notifyType(1)     // 使用默认提示音提示
        .notifyId(notifyid++);
		if(mess.getLiveTime()!=null && mess.getLiveTime().intValue()>0){
			long time = mess.getLiveTime()*60*1000L;
			builder.timeToLive(time);
		}
		builder.extra("op", mess.getOperation());
		if(!"app".equals(mess.getOperation())){
			if("h5".equals(mess.getOperation())){
				builder.extra("targetUrl", mess.getTargetUrl());
			}else {
				builder.extra("targetid", mess.getTargetid().toString());
			}
		}
		
		return builder.build();
	}

	/**
	 * 创建IOS版消息
	 * @param mess
	 * @return
	 */
	private static Message buildIOSMessage(SystemMess mess) {
		IOSBuilder builder = new Message.IOSBuilder();
		builder.description(mess.getContent().length()>120?mess.getContent().substring(0,120):mess.getContent())
        .soundURL("default");     // 使用默认提示音提示
		builder.extra("op", mess.getOperation());

		if(mess.getLiveTime()!=null && mess.getLiveTime().intValue()>0){
			long time = mess.getLiveTime()*60*1000L;
			builder.timeToLive(time);
		}
		if(!"app".equals(mess.getOperation())){
			if("h5".equals(mess.getOperation())){
				builder.extra("title", mess.getTitle());
				builder.extra("targetUrl", mess.getTargetUrl());
			}else {
				builder.extra("targetid", mess.getTargetid().toString());
			}
		}
		
		return builder.build();
	}
	
//	@SuppressWarnings("static-access")
//	public static void main(String arg[]){
//		SystemMess mess = new SystemMess();
//		SystemMess mess1 = new SystemMess();
//		SystemMess mess2 = new SystemMess();
//		mess.setTitle("mipush测试1");
//		mess.setContent("mipush测试打开应用");
//		mess.setOperation("app");
//		mess.setDestUser("330");
//		
//		mess1.setTitle("mipush测试2");
//		mess1.setContent("mipush测试打开视频详情");
//		mess1.setOperation("video");
//		mess1.setTargetid(21890L);
//		mess1.setDestUser("330");
//		
//		mess2.setTitle("mipush测试3");
//		mess2.setContent("mipush测试打开活动详情");
//		mess2.setOperation("activity");
//		mess2.setTargetid(1L);
//		
//		while(true){
//			try {
//				MiPushUtil.sendMessage(mess);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			try {
//				Thread.currentThread().sleep(10000);
//			} catch (InterruptedException e2) {
//				// TODO Auto-generated catch block
//				e2.printStackTrace();
//			}
//			
//			try {
//				MiPushUtil.sendMessage(mess1);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			try {
//				Thread.currentThread().sleep(10000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
////			try {
////				MiPushUtil.sendMessage(mess2);
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			} catch (ParseException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//			
//			try {
//				Thread.currentThread().sleep(10000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
//	}
	
}
