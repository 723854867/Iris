package com.busap.vcs.restadmin.quartz;
/**
 * @author dmsong
 * @description 视频当日热度排行
 */
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.busap.vcs.service.VideoService;
import com.busap.vcs.webcomn.util.SpringWebUtils;

public class DayHotVideoRankTask implements Job {

	private static final Logger logger = LoggerFactory.getLogger(DayHotVideoRankTask.class);
	
	private VideoService videoService;


	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("======day hot videos rank schedule job execute start======");
		ApplicationContext appCtx = SpringWebUtils.getApplicationContext();
		videoService = (VideoService) appCtx.getBean("videoService");
		videoService.dayHotVideosToRedis();
	}
}
