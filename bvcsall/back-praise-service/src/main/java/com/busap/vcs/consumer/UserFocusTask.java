package com.busap.vcs.consumer;

import java.text.SimpleDateFormat;
import java.util.*;

import com.busap.vcs.constants.BicycleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.bean.VideoTaskItem;
import com.busap.vcs.srv.JedisService;
import com.busap.vcs.srv.impl.MessageService;
import com.busap.vcs.srv.impl.UserFocusService;

@Service
public class UserFocusTask extends TimerTask{
	private static final Logger logger = LoggerFactory.getLogger(UserFocusTask.class);
	public static final String FOCUS_USER_KEY = "USER_FUCOS_NUMBER_REDIS_";
	@Autowired
	private UserFocusService videoFocusService;
	@Autowired
	JedisService jedisService;
	private static final int MAX_FOCUS_TIMES_NUMBER = 2;
	/**
	 * 1、	审核通过后，60分钟内完成第一批次自动关注，普通用户涨粉10-30，V用户涨粉20-40；
	 * 2、	审核通过后，120分钟后完成第二批次自动关注，普通用户涨粉5-10，V用户涨粉10-30。
	 */
	@Override
	public void run(){
		try{ 
			List<VideoTaskItem>  delList = new ArrayList<VideoTaskItem>();//要清除的关注视频消息
			
			logger.debug("Auto Focus by publish video---------vList.size()-----------------------" + Consumer.focuslist.size());

			for (VideoTaskItem vti : Consumer.focuslist) {
				this.checkRedis(vti.getUid());

				if (!this.checkDay(vti.getPubDate())) {
					//大于24小时视频不进行用户关注处理
					delList.add(vti);
					continue;
				}
				// 机器人自动关注
				int fun = vti.getFun();
				if (vti.getFcount() < UserFocusTask.MAX_FOCUS_TIMES_NUMBER && videoFocusService.doFocus(vti)) {
					if (vti.getFun() - fun>0) {
						// 如果关注用户数有增加，则关注批数+1
						vti.setFcount(vti.getFcount()+1);
					}
				}
				if (vti.getFun() > 0 && vti.getFcount() >= UserFocusTask.MAX_FOCUS_TIMES_NUMBER) {
					// 如果关注用户数>0且关注批数>=最大值，将此次任务移除
					delList.add(vti);
				}
				if (vti.getFun() == 0 && vti.getFcount() == UserFocusTask.MAX_FOCUS_TIMES_NUMBER) {
					// 如果关注用户数为0且关注批数等于最大值，将关注批数设为0
					vti.setFcount(0);
				}

			}
			logger.info("Auto Focus by publish video---deleteList.size=" + delList.size());
			for (VideoTaskItem vti : delList) {
				//清除完成任务关注消息
				Consumer.focuslist.remove(vti);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询此用户今天是否有自动关注信息，如果有信息，时间不是今天更新成今天
	 * 若没有信息 新增一条缓存记录
	 * @param uid 用户id
	 */
	private void checkRedis(long uid){
		String date = jedisService.get(MessageService.REDIS_USER_ATTENTION_KEY+uid);
		if (null != date && !"".equals(date)) {
			String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			if (!now.equals(date)) {
				jedisService.set(MessageService.REDIS_USER_ATTENTION_KEY + uid, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			}
		} else {
			jedisService.set(MessageService.REDIS_USER_ATTENTION_KEY + uid, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		}
	}

	private boolean checkDay(long pubDate){
		return (System.currentTimeMillis() - pubDate) < 24 * 60 * 60 * 1000;
	}
}