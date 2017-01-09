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
public class PraiseService implements IHandlerMsgService{
	private static final Logger logger = LoggerFactory.getLogger(PraiseService.class);
	@Autowired
	JedisService jedisService;
	@Autowired
	ObjectMapper mapper;

	@Value("#{configProperties['user_msg_key_prefix']}")
	private String user_push_msg_key;
	
	
	private static final String PRAISEKEY = "praise";
	
	@Value("#{configProperties['android_key']}")
	private String ANDROID_KEY = "";
	
	@Value("#{configProperties['ios_key']}")
	private String IOS_KEY = "";
	
	@Value("#{configProperties['ios_key2']}")
	private String IOS_KEY2 = "";
	
	@Value("#{configProperties['ios_app_key']}")
	private String IOS_APP_KEY = "";
	
	
	@Value("#{configProperties['user_info_key']}")
	private String user_info_key = "USER_INFO_";
	
	public void dealMsg(Message msg) {
		try {
			switch (msg.getAction()) {
			case INSERT:
					Map<String, Object> dataMap = msg.getDataMap();
					//赞的人
					String uid = (String)dataMap.get("uid");
					//视频所有人
					String spuid = (String) dataMap.get("pid");
					long puid = Long.parseLong(spuid);
					//视频id
					String vid = (String) dataMap.get("vid");
					
					String userName = jedisService.getValueFromMap(user_info_key + uid, "name");
					String platform = jedisService.getValueFromMap(user_info_key + puid, "platform");
					String platformed = jedisService.getValueFromMap(user_info_key + uid, "platform");
					
					String deviceUUID = jedisService.getValueFromMap(BicycleConstants.USER_INFO+spuid, "deviceUuid");
					String alias = spuid;
					if (StringUtils.isNotBlank(deviceUUID)){
						alias = alias + deviceUUID ;
					}
					
					String deviceUUID2 = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "deviceUuid");
					String alias2 = uid;
					if(StringUtils.isNotBlank(deviceUUID2)){
						alias2 = alias2 + deviceUUID2 ;
					}
					//如果赞方名字或被赞方platform不存在，则不发送
					if (StringUtils.isEmpty(userName) || StringUtils.isBlank(platform)) {
						logger.error("praise msg is not send,uid is {},username is {},puid is {},platform is {}",uid,userName,puid,platform);
						return;
					}
					if (dataMap.containsKey("shareUid")) {
						String checkuid=(String)dataMap.get("checkuid");
						String checkshareuid=(String)dataMap.get("checkshareuid");
						if ("android".equals(platform)) {
							if (checkshareuid!=null&&checkshareuid.equals("true")) {
								AndroidMsgSender.sendMsg(ANDROID_KEY,"LIVE", userName+"通过网页赞了你的视频，你获得100积分", "praise", alias, true, vid,null);
							} else {
								AndroidMsgSender.sendMsg(ANDROID_KEY,"LIVE", userName+"在LIVE中狠狠的赞了你，赶快回应TA一下吧", "praise", alias, true, vid,null);
							}
						}
						if ("ios".equals(platform)) {
							if(checkshareuid!=null&&checkshareuid.equals("true")) {
								IosMsgSender.sendMsg(IOS_KEY,userName+"通过网页赞了你的视频，你获得100积分", "praise", alias, vid,null);
								IosMsgSender.sendMsg(IOS_KEY2,userName+"通过网页赞了你的视频，你获得100积分", "praise", alias, vid,null);
								IosMsgSender.sendMsg(IOS_APP_KEY,userName+"通过网页赞了你的视频，你获得100积分", "praise", alias, vid,null);
							}else{
								IosMsgSender.sendMsg(IOS_KEY,userName+"在LIVE中狠狠的赞了你，赶快回应TA一下吧", "praise", alias, vid,null);
								IosMsgSender.sendMsg(IOS_KEY2,userName+"在LIVE中狠狠的赞了你，赶快回应TA一下吧", "praise", alias, vid,null);
								IosMsgSender.sendMsg(IOS_APP_KEY,userName+"在LIVE中狠狠的赞了你，赶快回应TA一下吧", "praise", alias, vid,null);
							}
						}
						if ("android".equals(platformed)) {
							if(checkuid!=null&&checkuid.equals("true")) {
								AndroidMsgSender.sendMsg(ANDROID_KEY,"LIVE","您通过网页分享点赞了视频，获得100积分", "praise",alias2, true, vid,null);
							}
							return;
						}
						if ("ios".equals(platformed)) {
							if(checkuid!=null&&checkuid.equals("true")) {
								IosMsgSender.sendMsg(IOS_KEY,"您通过网页分享点赞了视频，获得100积分", "praise",alias2, vid,null);
								IosMsgSender.sendMsg(IOS_KEY2,"您通过网页分享点赞了视频，获得100积分", "praise",alias2, vid,null);
								IosMsgSender.sendMsg(IOS_APP_KEY,"您通过网页分享点赞了视频，获得100积分", "praise",alias2, vid,null);
							}
							return;
						}
					} else {
                        if ("android".equals(platform)) {
                            AndroidMsgSender.sendMsg(ANDROID_KEY,"LIVE", userName+"在LIVE中狠狠的赞了你，赶快回应TA一下吧", "praise", alias, true, vid,null);
                            return;
                        }
                        if ("ios".equals(platform)) {
                            IosMsgSender.sendMsg(IOS_KEY,userName+"在LIVE中狠狠的赞了你，赶快回应TA一下吧", "praise", alias, vid,null);
                            IosMsgSender.sendMsg(IOS_KEY2,userName+"在LIVE中狠狠的赞了你，赶快回应TA一下吧", "praise", alias, vid,null);
                            IosMsgSender.sendMsg(IOS_APP_KEY,userName+"在LIVE中狠狠的赞了你，赶快回应TA一下吧", "praise", alias, vid,null);
                            return;
                        }
					}
					if (StringUtils.isNotBlank(userName)) {
						Map<String, Object> vMap = new HashMap<String, Object>();
						vMap.put("title", userName+"在LIVE中狠狠的赞了你，赶快回应TA一下吧");
						//type:1评论，2赞，3关注，4系统消息
						vMap.put("type", 2);
						//tag:1视频，2活动，3列表
						vMap.put("tag", 1);
						vMap.put("targetid", vid);
						String content = mapper.writeValueAsString(vMap);
						jedisService.setValueToMap(user_push_msg_key+puid, PRAISEKEY, content);
					}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error msg {}|{}|{}|{}|{}",e.getMessage(),msg.getModule(),msg.getAction(),msg.getDataMap(),msg.getConditionMap());
		}
	}
}
