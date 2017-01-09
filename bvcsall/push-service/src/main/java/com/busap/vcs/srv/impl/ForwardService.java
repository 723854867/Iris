package com.busap.vcs.srv.impl;

import com.busap.vcs.base.Message;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.srv.IHandlerMsgService;
import com.busap.vcs.srv.JedisService;
import com.busap.vcs.utils.mipush.AndroidMsgSender;
import com.busap.vcs.utils.mipush.IosMsgSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 转发视频 service - 米Push
 * Created by Yangxinyu on 15/9/17.
 */
@Service
public class ForwardService implements IHandlerMsgService {

    private static final Logger logger = LoggerFactory.getLogger(ForwardService.class);

    @Autowired
    ObjectMapper mapper;

    @Value("#{configProperties['user_msg_key_prefix']}")
    private String user_push_msg_key;

    private static final String PRAISE_KEY = "praise";

    @Value("#{configProperties['android_key']}")
    private String ANDROID_KEY = "";

    @Value("#{configProperties['ios_key']}")
    private String IOS_KEY = "";

    @Value("#{configProperties['ios_app_key']}")
    private String IOS_APP_KEY = "";

    @Value("#{configProperties['user_info_key']}")
    private String user_info_key = "USER_INFO_";

    private static final String FORWARD_KEY = "forward";

    @Autowired
    private JedisService jedisService;

    @Override
    public void dealMsg(Message msg) {
        try {
            switch (msg.getAction()) {
                case INSERT:
                    Map<String, Object> dataMap = msg.getDataMap();
                    // 转发的人
                    String uid = (String)dataMap.get("uid");
                    // 视频创建人
                    String creatorIdStr = (String) dataMap.get("pid");
                    long creatorId = Long.parseLong(creatorIdStr);
                    // 视频id
                    String vid = (String) dataMap.get("vid");

                    String userName = jedisService.getValueFromMap(user_info_key + uid, "name");
                    String platform = jedisService.getValueFromMap(user_info_key + creatorId, "platform");

                    String deviceUUID = jedisService.getValueFromMap(BicycleConstants.USER_INFO + creatorIdStr, "deviceUuid");
                    String alias = creatorIdStr;

                    if (StringUtils.isNotBlank(deviceUUID)) {
                        alias = alias + deviceUUID ;
                    }

                    // 如果转发人名字或被转发人platform不存在或视频id不存在，则不发送
                    if (StringUtils.isEmpty(userName) || StringUtils.isBlank(platform) || StringUtils.isBlank(vid)) {
                        logger.error("forward msg is not send,uid is {},username is {}, creatorId is {},platform is {},vid is {}",
                                uid, userName, creatorId, platform, vid);
                        return;
                    }

                    if ("android".equals(platform)) {
                        AndroidMsgSender.sendMsg(ANDROID_KEY, "LIVE", userName + "在LIVE中转发了你的视频，赶快回应TA一下吧", FORWARD_KEY, alias, true, vid,null);
                        return;
                    }
                    if ("ios".equals(platform)) {
                        IosMsgSender.sendMsg(IOS_KEY, userName + "在LIVE中转发了你的视频，赶快回应TA一下吧", FORWARD_KEY, alias, vid,null);
                        IosMsgSender.sendMsg(IOS_APP_KEY, userName + "在LIVE中转发了你的视频，赶快回应TA一下吧", FORWARD_KEY, alias, vid,null);
                        return;
                    }
                    if (StringUtils.isNotBlank(userName)) {
                        Map<String, Object> vMap = new HashMap<>();
                        vMap.put("title", userName + "在LIVE中转发了你的视频，赶快回应TA一下吧");
                        // type:1评论，2赞，3关注，4系统消息，5转发
                        vMap.put("type", 5);
                        // tag:1视频，2活动，3列表
                        vMap.put("tag", 1);
                        vMap.put("targetid", vid);
                        String content = mapper.writeValueAsString(vMap);
                        jedisService.setValueToMap(user_push_msg_key + creatorId, PRAISE_KEY, content);
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
