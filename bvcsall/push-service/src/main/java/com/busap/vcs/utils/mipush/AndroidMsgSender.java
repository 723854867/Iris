package com.busap.vcs.utils.mipush;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Message.Builder;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;

public class AndroidMsgSender {
	private static final Logger logger = LoggerFactory.getLogger(AndroidMsgSender.class);
	
	static {
		//生产,android测试时候要用这个，ios测试时候用下面的，生产环境全用这个
		Constants.useOfficial();
		//测试，小米只支持ios
		//Constants.useSandbox();
	}
	
	public static void sendMsg(String key,String title, String description,String extra,String alias,boolean isPassThrough, String targetId,String rtmpLiveUrl) throws IOException, ParseException {
		if (IosMsgSender.USE_TEST) { //测试环境时，android每次发送设置为正式环境，正式环境时，不执行此设置
			Constants.useOfficial();
		}
		Sender sender = new Sender(key);
			Builder builder = new Message.Builder().title(title).description(description)
			.restrictedPackageName("com.busap.myvideo").notifyType(1).payload("11");
			if (isPassThrough) {
				builder.passThrough(1);
			}else {
				builder.passThrough(0);
			}
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
				logger.error("{}================android send msg error,error msg is {},android key is {}",extra,result.getReason(),key);
			}else {
				logger.info("=====android send {} msg success, msg id is {} ,alias is {},desc is {},extra is {},android key is {}",extra,result.getMessageId(),alias,description,extra,key);
			}
	}
}
