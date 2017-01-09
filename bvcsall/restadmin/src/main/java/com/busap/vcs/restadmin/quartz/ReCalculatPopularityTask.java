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

import com.busap.vcs.service.RuserService;
import com.busap.vcs.webcomn.util.SpringWebUtils;

public class ReCalculatPopularityTask implements Job {

	private static final Logger logger = LoggerFactory.getLogger(ReCalculatPopularityTask.class);
	
	private RuserService ruserService;


	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("======recalculate user popularity schedule job execute start======");
		ApplicationContext appCtx = SpringWebUtils.getApplicationContext();
		ruserService = (RuserService) appCtx.getBean("ruserService");
		ruserService.calculatePopularity();
	}
}
