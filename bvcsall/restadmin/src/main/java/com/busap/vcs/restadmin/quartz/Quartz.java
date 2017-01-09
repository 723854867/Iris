package com.busap.vcs.restadmin.quartz;

import java.text.ParseException;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

import com.busap.vcs.data.entity.Video;

public class Quartz {
	
	/*@Autowired
	private  SchedulerFactoryBean schedulerFactoryBean;*/
	private QuartzProducer quartzProducer;
	private static String JOB_GROUP_NAME = "video_Plan_Publish";
	private static String TRIGGER_GROUP_NAME = null;

	/** 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名 */
	public void addJob(Video video) throws SchedulerException, ParseException {
		TRIGGER_GROUP_NAME = video.getId().toString();
		addJob(video,new VideoJob());
	}

	/**
	 * 添加一个定时任务
	 * 
	 * @param jobName 任务名
	 * @param jobGroupName 任务组名
	 * @param triggerName  触发器名
	 * @param triggerGroupName 触发器组名
	 * @param job  任务
	 * @param cronExpression  时间设置，参考quartz说明文档
	 */
	private void addJob(Video video,Job job) throws SchedulerException, ParseException {
		Scheduler sched = quartzProducer.getScheduler();
		JobDetail jobDetail = new JobDetail(TRIGGER_GROUP_NAME, JOB_GROUP_NAME,job.getClass());// 任务名，任务组，任务执行类
		Trigger trigger = new SimpleTrigger(TRIGGER_GROUP_NAME,JOB_GROUP_NAME);
		trigger.setStartTime(video.getPlanPublishTime());
		sched.scheduleJob(jobDetail, trigger);
	}

	public QuartzProducer getQuartzProducer() {
		return quartzProducer;
	}

	public void setQuartzProducer(QuartzProducer quartzProducer) {
		this.quartzProducer = quartzProducer;
	}
	
	
}
