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
public class CommentService implements IHandlerMsgService{
	private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
	@Autowired
	JedisService jedisService;
	@Autowired
	ObjectMapper mapper;

	@Value("#{configProperties['user_msg_key_prefix']}")
	private String user_push_msg_key;
	
	private static final String COMMENTKEY = "comment";
	
	
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
				//评论的人
				long uid = Long.parseLong((String) dataMap.get("uid"));
				//和产品确认如果是回复则则通知回复人
				//被回复人id或视频所有人id
				String spuid = (String) dataMap.get("pid");
				//视频id
				String vid = (String) dataMap.get("vid");
				String userName = jedisService.getValueFromMap(user_info_key+uid, "name");
				if (StringUtils.isNotBlank(spuid)) {
					long puid = Long.parseLong(spuid);

					String platform = jedisService.getValueFromMap(user_info_key+puid, "platform");
					String deviceUUID = jedisService.getValueFromMap(BicycleConstants.USER_INFO+spuid, "deviceUuid");
					String alias = spuid;
					if(StringUtils.isNotBlank(deviceUUID)){
						alias = alias + deviceUUID ;
					}
					//如果评论方名字或被评论方platform不存在，则不发送
					if (StringUtils.isEmpty(userName) || StringUtils.isBlank(platform)) {
						logger.error("comment msg is not send,uid is {},username is {},puid is {},platform is {}",uid,userName,puid,platform);
						return;
					}
					
					if ("android".equals(platform)) {
						AndroidMsgSender.sendMsg(ANDROID_KEY,"LIVE", userName+"在LIVE中评论了你，看看TA说了什么吧", "comment", alias, true, vid,null);
						return;
					}
					if ("ios".equals(platform)) {
						IosMsgSender.sendMsg(IOS_KEY,userName+"在LIVE中评论了你，看看TA说了什么吧", "comment", alias, vid,null);
						IosMsgSender.sendMsg(IOS_KEY2,userName+"在LIVE中评论了你，看看TA说了什么吧", "comment", alias, vid,null);
						IosMsgSender.sendMsg(IOS_APP_KEY,userName+"在LIVE中评论了你，看看TA说了什么吧", "comment", alias, vid,null);
						return;
					}
					return;
				}
				if (StringUtils.isNotBlank(userName)) {
					Map<String, Object> vMap = new HashMap<>();
					vMap.put("title", userName + "在LIVE中评论了你，看看TA说了什么吧");
					//type:1评论，2赞，3关注，4系统消息
					vMap.put("type", 1);
					//tag:1视频，2活动，3列表
					vMap.put("tag", 1);
					vMap.put("targetid", vid);
					String content = mapper.writeValueAsString(vMap);
					jedisService.setValueToMap(user_push_msg_key + spuid, COMMENTKEY, content);
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
