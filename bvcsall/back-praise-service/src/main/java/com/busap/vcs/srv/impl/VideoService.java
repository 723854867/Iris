package com.busap.vcs.srv.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.busap.vcs.base.Message;
import com.busap.vcs.consumer.VideoPraiseTask;
import com.busap.vcs.consumer.VideoPraiseTask.PraiseTaskItem;
import com.busap.vcs.dao.VideoCheckDao;
import com.busap.vcs.job.PraiseJob;
import com.busap.vcs.srv.IHandlerMsgService;
import com.busap.vcs.srv.IVideoPraiseService;
import com.busap.vcs.srv.JedisService;


@Service
public class VideoService implements IHandlerMsgService,IVideoPraiseService{
	private static final Logger logger = LoggerFactory.getLogger(VideoService.class);
	
	@Autowired
	VideoCheckDao videoCheckDao;
	@Autowired
	JedisService jedisService;
	@Autowired
	private SchedulerFactoryBean schedulerFactory;
	private static final String KEY_MAJIA_UID = "MAJIA_UID";
	private static final int MIN_PRAISE_COUNT = 10;
	private static final Integer[] later = {3,4,5,6,7,8,9,10,11,12,13,14,15};
	@Value("#{configProperties['max_handle_count']}")
	private  int maxHandleCount = 4;//最大的处理次数，比如一个视频可以在四个时间点（使用Quarts的Trigger来配置）来处理
	private Random random = new Random();
	
	public void dealMsg(Message msg) {
		try {
			switch (msg.getAction()) {
			case AUDIT:
					Map<String, Object> dataMap = msg.getDataMap();
					//long uid = Long.parseLong((String) dataMap.get("uid"));
					String svid = (String) dataMap.get("vid");
					logger.debug("===========receive video id is {}",svid);
					//首先触发一次点赞操作
					long vid = Long.parseLong(svid);
					//doPraise(vid);
					
					
					Random minuteRandom = new Random();
					List<Integer> tmp = new ArrayList<Integer>(later.length);
					Calendar calendar = Calendar.getInstance();
					
					Collections.addAll(tmp, later);
					int index = minuteRandom.nextInt(tmp.size()-1);
					Integer first = tmp.get(index);
					tmp.remove(first);
					calendar.add(Calendar.MINUTE, first);
					scheduler(calendar, vid,"1");
					
					
					index = minuteRandom.nextInt(tmp.size()-1);
					Integer second = tmp.get(index);
					tmp.remove(second);
					calendar.add(Calendar.MINUTE, -first+second);
					scheduler(calendar, vid,"2");
					
					
					index = minuteRandom.nextInt(tmp.size()-1);
					Integer third = tmp.get(index);
					calendar.add(Calendar.MINUTE, first-second+third);
					scheduler(calendar, vid,"3");
					
					
					
					PraiseTaskItem item = new PraiseTaskItem();
					item.setId(vid);
					item.setCount(1);
					VideoPraiseTask.addTaskItem(item);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("============error {}|{}|{}|{}",msg.getModule(),msg.getAction(),msg.getDataMap(),msg.getConditionMap());
		}
		
	}
	
	private void scheduler(Calendar calendar,long vid,String batch) throws SchedulerException{
		String group = "group"+vid+calendar.getTimeInMillis();
		String name = "name"+vid+calendar.getTimeInMillis();
		SimpleTrigger trigger = new SimpleTrigger();
		trigger.setGroup(group);
		trigger.setName(name);
		trigger.setStartTime(calendar.getTime());
		trigger.setRepeatCount(0);
		trigger.setRepeatInterval(1000);
		JobDetail jobDetail = new JobDetail();
		jobDetail.setGroup(group);
		jobDetail.setName(name);
		jobDetail.setJobClass(PraiseJob.class);
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("vid", vid);
		jobDataMap.put("info", batch);
		jobDetail.setJobDataMap(jobDataMap);
		schedulerFactory.getScheduler().scheduleJob(jobDetail, trigger);
		logger.debug("video id is {} job,batch is {} will start at {}",vid,batch,calendar.getTime().toString());
	}
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		System.out.println(calendar.getTime());
	}
	@Override
	public void doPraise(long videoId,int min,int max) {
		//Set<String> majiaIdSet = jedisService.getSetFromShard(KEY_MAJIA_UID);
		//List<String> majiaList = new ArrayList<String>(majiaIdSet);
		//得到实际的赞总数
		long majiaTotal = jedisService.getSetCount(KEY_MAJIA_UID);//majiaIdSet.size();
//		int realTotal = getRandomTotal(majiaTotal)/maxHandleCount;
		
		
		
		int praiseCount = getPraiseCount(min,max);
		
		logger.debug("praise count min {},max {},randmon {}",min,max,praiseCount);
		
		if (praiseCount > majiaTotal) {
			praiseCount = (int)majiaTotal;
			logger.debug("praise count min {},max {},ma jia total less than randmon, praise count is {}",min,max,praiseCount);
		}
		
		int count = 0;
		List<String> praiseUids = jedisService.getSetRanomMembers(KEY_MAJIA_UID, praiseCount);
		for(String uid : praiseUids){
			//随机获取一个马甲用户
			String majiaid = uid;
			//增加赞记录
			try{
				logger.debug("ma jia total number is {},real total number is {}",majiaTotal,praiseCount);
				videoCheckDao.insertPraise(Long.parseLong(majiaid), videoId,System.currentTimeMillis());
				count++;
				logger.debug("insert success, video id is {},ma jia id is {}",videoId,majiaid);
			}catch(Exception ex){
				logger.error("insert praise error,video id is {},majia id is {},error msg is {}",videoId,majiaid,ex.getMessage());
			}
		}
		videoCheckDao.updateVideoPraiseCount(count, videoId);
	}
	
	/**
	 * 获取随机的赞总数
	 * @param max 上限
	 * @return
	 */
	private int getRandomTotal(int max){
		int rad = getRandomValue(max);
		if(rad < MIN_PRAISE_COUNT){
			rad = MIN_PRAISE_COUNT;
		}
		return rad;
	}
	/**
	 * 获取随机值
	 * @param max 上限
	 * @return
	 */
	private int getRandomValue(int max){
		random = new Random();
		int rad = random.nextInt(max);
		return rad;

	}
	
	public int getPraiseCount(int min,int max){
		if (min >= max) {
			return 1;
		}
		List<Integer> list = new ArrayList<Integer>();
		for(;min<=max;min++){
			list.add(min);
		}
		Random random = new Random();
		int index = random.nextInt(list.size());
		return list.get(index);
	}
}
