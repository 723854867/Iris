package com.busap.vcs.restadmin.quartz;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.busap.vcs.service.AttentionService;
import com.busap.vcs.webcomn.util.SpringWebUtils;

public class AutoAttentionTask implements Job {

	private static final Logger logger = LoggerFactory.getLogger(AutoAttentionTask.class);
	
	
	private AttentionService attentionService;


	public void execute(JobExecutionContext context) throws JobExecutionException {
		Date d = new Date();
		int hours = d.getHours();
		if (hours >=6 && hours< 24) { //只在每天的6点到24点之间进行
			logger.info("======AutoAttentionTask execute start======");
			ApplicationContext appCtx = SpringWebUtils.getApplicationContext();
			attentionService = (AttentionService) appCtx.getBean("attentionService");
			attentionService.autoAttention();;
		}
	}
	public static void main(String[] args) {
		System.out.println(new Date(1453948800000l));
	}
	
}
