package com.busap.vcs.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.busap.vcs.srv.impl.ApplicationCtxService;
import com.busap.vcs.srv.impl.VideoService;

public class PraiseJob implements Job {
	private final Logger logger = LoggerFactory.getLogger(PraiseJob.class);
	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		JobDataMap jobDataMap = jec.getJobDetail().getJobDataMap();
		long vid = jobDataMap.getLong("vid");
		String batch = jobDataMap.getString("info");
		logger.debug("start do job,video id is {},batch is {}",vid,batch);
		
		ApplicationContext ctx = ApplicationCtxService.getCtx();
		VideoService videoService = ctx.getBean(VideoService.class);
		videoService.doPraise(vid,10,25);
		logger.debug("do job end,video id is {},batch is {}",vid,batch);
	}
}
