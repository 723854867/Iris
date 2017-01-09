package com.busap.vcs.websocket.service;

import com.busap.vcs.websocket.bean.ParamConst;
import com.busap.vcs.websocket.mess.MessageAdapter;
import com.busap.vcs.websocket.mess.MessageConst;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class CheckTaskService {
	private ScheduledExecutorService service = Executors.newScheduledThreadPool(20);
	private static final Logger logger = Logger.getLogger(CheckTaskService.class);
	
	@Value("${checkall_time}")
	private Integer checkAllTime;
	
	@Autowired
	private JedisService jedisService;

	public void run() {
		logger.info("Scheduled task run start............");
		
		// 定时查询未审核视频，有则推送提示信息
		service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				logger.info("schedule check uncheck video started.");
				long now = System.currentTimeMillis() - 1000 * 60;
				long num = jedisService.zCount(ParamConst.VIDEO_CHECK, now, 0);
				if (num > 0) {
					logger.info("has uncheck video :"+num);
					MessageAdapter.sendToChecker(MessageConst.CHECK_MESS);
				}
			}
		}, 1,checkAllTime,TimeUnit.SECONDS);
		
		// 定时查询未处理直播投诉，有则推送提示信息
		service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				logger.info("schedule check undeal live complaints.");
				long now = System.currentTimeMillis() - 1000 * 60;
				String num = jedisService.get(ParamConst.LC_CHECK);
				if(num==null||num.equals("")) {
					num="0";
				}
				if (Long.valueOf(num) > 0) {
					logger.info("has uncheck live complaints :"+num);
					MessageAdapter.sendToChecker(MessageConst.LIVE_COMPLAINTS_MESS);
				}
			}
		}, 1,checkAllTime,TimeUnit.SECONDS);
		//定时查询是否有新直播或审核人员退出系统
		service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				//之前在线审核组人员数量
				logger.info("schedule check new live.");
				String cgCount = jedisService.get(ParamConst.CHECK_GROUP_COUNT);
				long checkGroupCount = 0;
				if(StringUtils.isNotBlank(cgCount)){
					checkGroupCount = Long.parseLong(cgCount);
				}

				//当前审核组人员数量
				long checkGroupCountNow = jedisService.getSortedSetSizeFromShard(ParamConst.CHECK_GROUP);
				//取当前最新直播的房间ID
				String newRoom = jedisService.get(ParamConst.NEWEST_LIVE_ROOM);
				//取之前存入缓存的最新直播房间ID
				String liveRoom = jedisService.get(ParamConst.NEW_LIVE_ROOM);
				if(StringUtils.isNotBlank(newRoom) && StringUtils.isNotBlank(liveRoom)) {
					if (checkGroupCount > checkGroupCountNow || Integer.valueOf(newRoom) > Integer.valueOf(liveRoom)) {
						MessageAdapter.sendToChecker(MessageConst.NEW_LIVE);
						jedisService.set(ParamConst.CHECK_GROUP_COUNT, String.valueOf(checkGroupCountNow));
					}
				}
			}
		}, 1,checkAllTime,TimeUnit.SECONDS);

		
//		//心跳，保持与客户端的链接
//		service.scheduleAtFixedRate(new Runnable() {
//			@Override
//			public void run() {
//				
//			}
//		}, 1,100,TimeUnit.SECONDS);
	}

	public JedisService getJedisService() {
		return jedisService;
	}

	public void setJedisService(JedisService jedisService) {
		this.jedisService = jedisService;
	}
}
