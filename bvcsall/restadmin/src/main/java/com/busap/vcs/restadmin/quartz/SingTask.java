package com.busap.vcs.restadmin.quartz;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.busap.vcs.service.SingVoteService;
import com.busap.vcs.webcomn.util.SpringWebUtils;

public class SingTask implements Job {

	private static final Logger logger = LoggerFactory.getLogger(SingTask.class);
	
	
	private SingVoteService singVoteService;


	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("======SingTask execute start======");
		ApplicationContext appCtx = SpringWebUtils.getApplicationContext();
		singVoteService = (SingVoteService) appCtx.getBean("singVoteService");
		singVoteService.createRank(1);
	}
	
}
