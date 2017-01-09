package com.busap.vcs.restadmin.quartz;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.busap.vcs.service.PraiseService;
import com.busap.vcs.webcomn.util.SpringWebUtils;

public class AutoPraiseTask implements Job {

	private static final Logger logger = LoggerFactory.getLogger(AutoPraiseTask.class);
	
	
	private PraiseService praiseService;


	public void execute(JobExecutionContext context) throws JobExecutionException {
		Date d = new Date();
		int hours = d.getHours();
		if (hours >=6 && hours< 24) { //只在每天的6点到24点之间进行
			logger.info("======AutoPraiseTask execute start======");
			ApplicationContext appCtx = SpringWebUtils.getApplicationContext();
			praiseService = (PraiseService) appCtx.getBean("praiseService");
			praiseService.autoPraise();
		}
	}
	public static void main(String[] args) {
		System.out.println(new Date(1453123500000l));
	}
	
}
