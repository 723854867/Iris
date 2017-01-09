package com.busap.vcs.restadmin.quartz;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysmessQuartz {
	private static final Logger logger = LoggerFactory.getLogger(SysmessQuartz.class);
	private QuartzProducer quartzProducer;
	
	private static String JOB_BASENAME = "sysmess_publish_";
	private static String GROUP_BASENAME = null;
	
//	@Resource(name = "systemMessService")
//	private SystemMessService systemMessService;
//	/**
//	 * 定时任务初始化，应用启动初始化
//	 * @throws SchedulerException 
//	 */
//	public void initJob() throws SchedulerException{
//		logger.info("-----init system message start----");
//		List<SystemMess> sysmess = systemMessService.searchPlan();
//		if(sysmess != null && sysmess.size()>0){
//			for(SystemMess sm:sysmess){
//				addJob(sm.getId(),sm.getPublishTime());
//			}
//			logger.info("-----init system message end----message count:"+sysmess.size());
//		}
//		
//	}
	
	/**
	 * 添加定时任务
	 * @throws SchedulerException 
	 * @param id
	 * @param expire
	 */
	public void addJob(Long id,Date expire) throws SchedulerException{
		Scheduler sched = quartzProducer.getScheduler();
		sched.deleteJob(JOB_BASENAME+id, GROUP_BASENAME);
		JobDetail jobDetail = new JobDetail(JOB_BASENAME+id, GROUP_BASENAME,SysmessPublishTimer.class);// 任务名，任务组，任务执行类
		Trigger trigger = new SimpleTrigger(JOB_BASENAME+id, GROUP_BASENAME);
		trigger.setStartTime(expire);
		sched.scheduleJob(jobDetail, trigger);
		logger.info("------add system message plan publish job ok.--------messageid:"+id);
	}
	
	public QuartzProducer getQuartzProducer() {
		return quartzProducer;
	}

	public void setQuartzProducer(QuartzProducer quartzProducer) {
		this.quartzProducer = quartzProducer;
	}
	
}
