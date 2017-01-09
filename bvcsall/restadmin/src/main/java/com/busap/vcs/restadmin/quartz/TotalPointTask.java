package com.busap.vcs.restadmin.quartz;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.service.ConsumeRecordService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.PraiseService;
import com.busap.vcs.webcomn.util.SpringWebUtils;

public class TotalPointTask implements Job {

	private static final Logger logger = LoggerFactory.getLogger(TotalPointTask.class);
	
	
	private ConsumeRecordService consumeRecordService;
	private JedisService jedisService;


	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("======TotalPointTask execute start======");
		ApplicationContext appCtx = SpringWebUtils.getApplicationContext();
		consumeRecordService = (ConsumeRecordService) appCtx.getBean("consumeRecordService");
		jedisService = (JedisService) appCtx.getBean("jedisService");
		List<Object[]> list = consumeRecordService.getTopPointUserInfo();
		if (list != null && list.size() >0) {
			try {
				jedisService.delete(BicycleConstants.TOP_POINT_USER_ID);
				for (Object[] array:list){
					if (array[0] != null && array[1] !=null) {
						jedisService.setValueToSortedSetInShard(BicycleConstants.TOP_POINT_USER_ID, Double.parseDouble(String.valueOf(array[1])), String.valueOf(array[0]));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		System.out.println(new Date(1453123500000l));
	}
	
}
