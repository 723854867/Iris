package com.busap.vcs.restadmin.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import com.busap.vcs.service.VideoService;
import com.busap.vcs.webcomn.util.SpringWebUtils;

public class CompleteDurationTask implements Job {

	private static final Logger logger = LoggerFactory.getLogger(CompleteDurationTask.class);
	
	private VideoService videoService;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("complete video duration");
		ApplicationContext appCtx = SpringWebUtils.getApplicationContext();
		videoService = (VideoService) appCtx.getBean("videoService");
		videoService.completeVideoDuration(50);
	}
}
