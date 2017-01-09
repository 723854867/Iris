package com.busap.vcs.restadmin.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.busap.vcs.data.entity.SystemMess;
import com.busap.vcs.service.SystemMessService;
import com.busap.vcs.webcomn.util.SpringWebUtils;

public class SysmessPublishTimer implements Job {
	private static final Logger logger = LoggerFactory.getLogger(SysmessPublishTimer.class);
	private static String JOB_BASENAME = "sysmess_publish_";
	
	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		ApplicationContext appCtx = SpringWebUtils.getApplicationContext();
		SystemMessService systemMessService = (SystemMessService) appCtx.getBean("systemMessService");
		String jobName = ctx.getJobDetail().getName();
		String id = jobName.replace(JOB_BASENAME, "");
		logger.info("---------job execute--------id:"+id);
		SystemMess sysmess = systemMessService.find(Long.parseLong(id));
		systemMessService.sendMessage(sysmess);
	}

}
