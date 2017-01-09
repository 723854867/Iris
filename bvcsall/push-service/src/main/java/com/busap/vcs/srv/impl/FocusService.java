package com.busap.vcs.srv.impl;

import java.util.*;

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
public class FocusService implements IHandlerMsgService{
	private static final Logger logger = LoggerFactory.getLogger(FocusService.class);
	@Autowired
	JedisService jedisService;
	@Autowired
	ObjectMapper mapper;

	@Value("#{configProperties['user_msg_key_prefix']}")
	private String user_push_msg_key;
	
	private static final String FOCUS_KEY = "focus";
	
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
				//关注人
				String uidStr = (String) dataMap.get("uid");
				long uid = Long.parseLong(uidStr);
				//被关注的人
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
					logger.error("focus msg is not send,uid is {},username is {},puid is {},platform is {}", uid, userName, puid, platform);
					return;
				}
				
				if ("android".equals(platform)) {
					AndroidMsgSender.sendMsg(ANDROID_KEY,"LIVE", userName+"关注了你，要不要现在就去回应一下？", "focus", alias, true, uidStr,null);
					return;
				}
				if ("ios".equals(platform)) {
					IosMsgSender.sendMsg(IOS_KEY,userName+"关注了你，要不要现在就去回应一下？", "focus", alias, uidStr,null);
					IosMsgSender.sendMsg(IOS_KEY2,userName+"关注了你，要不要现在就去回应一下？", "focus", alias, uidStr,null);
					IosMsgSender.sendMsg(IOS_APP_KEY,userName+"关注了你，要不要现在就去回应一下？", "focus", alias, uidStr,null);
					return;
				}
				
				if (StringUtils.isNotBlank(userName)) {
					Map<String, Object> vMap = new HashMap<>();
					vMap.put("title", userName+"关注了你，要不要现在就去回应一下？");
					//type:1评论，2赞，3关注，4系统消息
					vMap.put("type", 3);
					//tag:1视频，2活动，3列表
					vMap.put("tag", 3);
					vMap.put("targetid", "");
					String content = mapper.writeValueAsString(vMap);
					jedisService.setValueToMap(user_push_msg_key+puid, FOCUS_KEY, content);
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
