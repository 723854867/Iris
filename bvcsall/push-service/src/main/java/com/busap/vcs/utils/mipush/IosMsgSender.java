package com.busap.vcs.utils.mipush;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.consumer.Consumer;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Message.IOSBuilder;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;

public class IosMsgSender {
	private static final Logger logger = LoggerFactory.getLogger(IosMsgSender.class);
		
	public static boolean USE_TEST = false;
	public static void init(String isTest){
		logger.info("ios istest "+isTest);
		//生产,android测试时候要用这个，ios测试时候用下面的，生产环境全用这个
		if (StringUtils.isNotBlank(isTest) && Boolean.valueOf(isTest)) {
			//测试，小米只支持ios
			USE_TEST = true;
			Constants.useSandbox();
			logger.info("ios sender init sandbox.");
		} else {
			USE_TEST = false;
			Constants.useOfficial();
			logger.info("ios sender init official.");
		}
		
	}
	
	public static void sendMsg(String key,String description,String extra,String alias, String targetId,String rtmpLiveUrl) throws IOException, ParseException {
		
		if (USE_TEST){  //测试环境，每次发送的时候都设置为沙箱，正式环境，不执行此设置
			Constants.useSandbox();
		}
		Sender sender = new Sender(key);
		IOSBuilder builder = new Message.IOSBuilder()
            .description(description)
            .soundURL("default")    // 消息铃声
            //.badge(1)               // 数字角标
            //.category("action")     // 快速回复类别
            //.extra("key", "value")  // 自定义键值对
            ;
		if (StringUtils.isNotBlank(extra)) {
			builder.extra("type", extra);
		}
		if (StringUtils.isNotBlank(targetId)) {
			builder.extra("targetId", targetId);
		}
		if (StringUtils.isNotBlank(rtmpLiveUrl)) {
			builder.extra("rtmpLiveUrl", rtmpLiveUrl);
		}

		Message message = builder.build();
		Result result = sender.sendToAlias(message, alias, 0);
		if (StringUtils.isBlank(result.getMessageId())) {
			logger.error("{}================ios send msg error,error msg is {},ios key is {}",extra,result.getReason(),key);
		}else {
			logger.info("======ios send {} msg success, msg id is {} ,alias is {},desc is {},extra is {},ios key is {}",extra,result.getMessageId(),alias,description,extra,key);
		}
	}
}
