package com.busap.vcs.srv.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.busap.vcs.base.Message;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.srv.IHandlerMsgService;
import com.busap.vcs.srv.JedisService;
import com.busap.vcs.utils.mipush.AndroidMsgSender;
import com.busap.vcs.utils.mipush.IosMsgSender;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class BerryService implements IHandlerMsgService{
	private static final Logger logger = LoggerFactory.getLogger(BerryService.class);
	@Autowired
	JedisService jedisService;
	@Autowired
	ObjectMapper mapper;

	@Value("#{configProperties['user_msg_key_prefix']}")
	private String user_push_msg_key;
	
	@Value("#{configProperties['user_info_key']}")
	private String user_info_key = "USER_INFO_";
	
	@Value("#{configProperties['android_key']}")
	private String ANDROID_KEY = "";
	
	@Value("#{configProperties['ios_key']}")
	private String IOS_KEY = "";
	
	@Value("#{configProperties['ios_key2']}")
	private String IOS_KEY2 = "";
	
	@Value("#{configProperties['ios_app_key']}")
	private String IOS_APP_KEY = "";
	
	public void dealMsg(Message msg) {
		try {
			switch (msg.getAction()) {
			case INSERT:
				Map<String, Object> dataMap = msg.getDataMap();
				//种草莓人
				String uidStr = (String) dataMap.get("uid");
				long uid = Long.parseLong(uidStr);
				//被种草莓人
				String spuid = (String) dataMap.get("pid");
				long puid = Long.parseLong(spuid);
				
				String userName = jedisService.getValueFromMap(user_info_key + uid, "name");
				
				String platform = jedisService.getValueFromMap(user_info_key + puid, "platform");
				
				String deviceUUID = jedisService.getValueFromMap(BicycleConstants.USER_INFO+spuid, "deviceUuid");
				String alias = spuid;
				if (StringUtils.isNotBlank(deviceUUID)) {
					alias = alias + deviceUUID ;
				}
				//如果关注方名字或被关注方platform不存在，则不发送
				if (StringUtils.isEmpty(userName) || StringUtils.isBlank(platform)) {
					logger.error("berry msg is not send,uid is {},username is {},puid is {},platform is {}", uid, userName, puid, platform);
					return;
				}
				
				if ("android".equals(platform)) {
					AndroidMsgSender.sendMsg(ANDROID_KEY,"LIVE", "有人偷偷对你种了颗草莓，快来看看吧~", "berry", alias, true, uidStr,null);
					return;
				}
				if ("ios".equals(platform)) {
					IosMsgSender.sendMsg(IOS_KEY,"有人偷偷对你种了颗草莓，快来看看吧~", "berry", alias, uidStr,null);
					IosMsgSender.sendMsg(IOS_KEY2,"有人偷偷对你种了颗草莓，快来看看吧~", "berry", alias, uidStr,null);
					IosMsgSender.sendMsg(IOS_APP_KEY,"有人偷偷对你种了颗草莓，快来看看吧~", "berry", alias, uidStr,null);
					return;
				}
				
				if (StringUtils.isNotBlank(userName)) {
					Map<String, Object> vMap = new HashMap<>();
					vMap.put("title", "有人偷偷对你种了颗草莓，快来看看吧~");
					//type:1评论，2赞，3关注，4系统消息，5转发,6开启直播7直播预告，8种草莓
					vMap.put("type", 8);
					//tag:1视频，2活动，3列表，4直播房间，5种草莓
					vMap.put("tag", 5);
					vMap.put("targetid", "");
					String content = mapper.writeValueAsString(vMap);
					jedisService.setValueToMap(user_push_msg_key+puid, "berry", content);
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("error msg {}|{}|{}|{}|{}",e.getMessage(),msg.getModule(),msg.getAction(),msg.getDataMap(),msg.getConditionMap());
		}
		
	}
}
