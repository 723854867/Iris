package com.busap.vcs.srv.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.busap.vcs.constants.BicycleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.busap.vcs.base.Message;
import com.busap.vcs.bean.VideoTaskItem;
import com.busap.vcs.consumer.Consumer;
import com.busap.vcs.srv.JedisService;


@Service
public class MessageService{
	private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
	public static final String REDIS_USER_ATTENTION_KEY = "REDIS_USER_ATTENTION_KEY_";
	@Autowired
	JedisService jedisService;
	@Autowired
	IntegralService integralService;

	public void dealMsg(Message msg) {
		try {
			switch (msg.getAction()) {
                case AUDIT: {
                    Map<String, Object> dataMap = msg.getDataMap();
                    String svid = (String) dataMap.get("vid");
                    String suid = (String) dataMap.get("uid");
                    long pubDate = (Long) dataMap.get("pubDate");
                    long vid = Long.parseLong(svid);
                    long uid = Long.parseLong(suid);
                    VideoTaskItem item = new VideoTaskItem();
                    item.setFcount(0);
                    item.setPcount(0);
                    item.setUid(uid);
                    item.setVid(vid);
                    item.setPubDate(pubDate);
                    // 视频审核通过
                    Consumer.focuslist.add(item);
                    Consumer.praiselist.add(item);

                    /** put video values in redis for auto comment **/
                    Map<String, String> videoMap = new HashMap<>();
                    videoMap.put("vid", svid);
                    videoMap.put("uid", suid);
                    videoMap.put("pubDate", String.valueOf(pubDate));
                    jedisService.setValueToMap(BicycleConstants.AUTO_COMMENT_VID + svid, videoMap);
                    jedisService.setValueToSetInShard(BicycleConstants.NEW_VIDEO_AUTO_COMMENT, BicycleConstants.AUTO_COMMENT_VID + svid);
                    break;
                }
                case INTEGRAL: {
                    try {
                        Map<String, Object> dataMap = msg.getDataMap();
                        String svid = (String) dataMap.get("vid");
                        String suid = (String) dataMap.get("uid");
                        long vid = Long.parseLong(svid);
                        long uid = Long.parseLong(suid);
//                        logger.info("get integral from video: UserId=" + uid);
                        /** 参与活动发视频完成任务 **/
                        integralService.getIntegralFromActivity(uid, vid);

                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("integral from video error! exception=" + e.getMessage());
                    }
                    break;
                }
                default:
                    break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("============error {}|{}|{}|{}",msg.getModule(),msg.getAction(),msg.getDataMap(),msg.getConditionMap());
		}
	}

}
