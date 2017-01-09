package com.busap.vcs.srv.impl;

import com.busap.vcs.base.Message;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.srv.IHandlerMsgService;
import com.busap.vcs.srv.JedisService;
import com.busap.vcs.utils.mipush.AndroidMsgSender;
import com.busap.vcs.utils.mipush.IosMsgSender;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 直播预告，通知粉丝
 * 
 */
@Service
public class LiveNoticeService implements IHandlerMsgService {

    private static final Logger logger = LoggerFactory.getLogger(LiveNoticeService.class);

    @Autowired
    ObjectMapper mapper;

    @Value("#{configProperties['user_msg_key_prefix']}")
    private String user_push_msg_key;

    private static final String LIVE_KEY = "live_notice";

    @Value("#{configProperties['android_key']}")
    private String ANDROID_KEY = "";

    @Value("#{configProperties['ios_key']}")
    private String IOS_KEY = "";

    @Value("#{configProperties['ios_app_key']}")
    private String IOS_APP_KEY = "";

    @Value("#{configProperties['user_info_key']}")
    private String user_info_key = "USER_INFO_";

    @Autowired
    private JedisService jedisService;

    @Override
    public void dealMsg(Message msg) {
        try {
            switch (msg.getAction()) {
                case INSERT:
                    Map<String, Object> dataMap = msg.getDataMap();
                    // 直播人id
                    String uid = (String)dataMap.get("uid");
                    // 粉丝id
                    String fansId = (String) dataMap.get("fansId");
                    //直播预告时间
                    String showTime = (String) dataMap.get("showTime");

                    String userName = jedisService.getValueFromMap(user_info_key + uid, "name");
                    String platform = jedisService.getValueFromMap(user_info_key + fansId, "platform");

                    String deviceUUID = jedisService.getValueFromMap(BicycleConstants.USER_INFO + fansId, "deviceUuid");
                    String alias = fansId;

                    if (StringUtils.isNotBlank(deviceUUID)) {
                        alias = alias + deviceUUID ;
                    }

                    // 如果创建直播人名字为空或者平台信息为空，或者房间id为空，不发送
                    if (StringUtils.isEmpty(userName) || StringUtils.isBlank(platform) ) {
                        logger.error("create liveshow msg is not send,uid is {},username is {}, fansId is {},platform is {}",
                                uid, userName, fansId, platform);
                        return;
                    }

                    if ("android".equals(platform)) {
                    	sendMsg("android", userName, alias,showTime,uid);
                        return;
                    }
                    if ("ios".equals(platform)) {
                    	sendMsg("ios", userName, alias,showTime,uid);
                        return;
                    }
                    if (StringUtils.isNotBlank(userName)) {
                        Map<String, Object> vMap = new HashMap<>();
                        vMap.put("title", userName + "：我的直播即将开始啦~");
                        // type:1评论，2赞，3关注，4系统消息，5转发,6开启直播7直播预告
                        vMap.put("type", 7);
                        // tag:1视频，2活动，3列表，4直播房间
                        vMap.put("tag", 4);
//                        vMap.put("targetid", roomId);
                        String content = mapper.writeValueAsString(vMap);
                        jedisService.setValueToMap(user_push_msg_key + fansId, LIVE_KEY, content);
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
    
    private void sendMsg(String type,final String userName,final String alias,final String showTime,String uid){
    	if ("ios".equals(type)){
    		try {
				IosMsgSender.sendMsg(IOS_KEY, userName + " :我将在   "+showTime+" 直播", LIVE_KEY, alias, uid,"");
				IosMsgSender.sendMsg(IOS_APP_KEY, userName + " :我将在   "+showTime+" 直播", LIVE_KEY, alias, uid,"");
			} catch (Exception e) {
				e.printStackTrace();
			}
    	} else {
    		try {
				AndroidMsgSender.sendMsg(ANDROID_KEY, "直播预告，大家都来呦", userName + " :我将在   "+showTime+" 直播", LIVE_KEY, alias, true, uid,"");
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
}
