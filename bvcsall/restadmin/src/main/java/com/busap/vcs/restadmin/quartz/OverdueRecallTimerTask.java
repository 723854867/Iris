package com.busap.vcs.restadmin.quartz;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.busap.vcs.data.repository.VideoRepository;
import com.busap.vcs.restadmin.task.QueryVideoConvertResult;
import com.busap.vcs.restadmin.utils.Contant;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.webcomn.util.Client;
import com.busap.vcs.webcomn.util.SpringWebUtils;

public class OverdueRecallTimerTask implements Job {

	private static final Logger logger = LoggerFactory.getLogger(QueryVideoConvertResult.class);
	
	private VideoRepository videoRepository;

	private JedisService jedisService;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("OverdueRecallTimerTask excute once");
		ApplicationContext appCtx = SpringWebUtils.getApplicationContext();
		videoRepository = (VideoRepository) appCtx.getBean("videoRepository");
		jedisService = (JedisService) appCtx.getBean("jedisService");
		long now = System.currentTimeMillis() - 1000 * 300;
		long num = jedisService.zCount(Contant.VIDEO_CHECK, now, 0);
/*		System.out.println("===================================");
		System.out.println("++++++++++++++"+num+"++++++++++++++");
		System.out.println("===================================");*/
		if (num > 0) {
			logger.info("------有超过5分钟还未审核的视频,及时短信通知审核人员------");
			List<String> phoneNos = videoRepository.findPhones();
			if (phoneNos.size() > 0){
				Mass(phoneNos);
			}
			now = now - 1500000;//清除超过半小时仍未被清除的元素
			jedisService.deleteSortedSetItemByScore(Contant.VIDEO_CHECK, 0, now);
		}
	}
//	private static final String sn = "SDK-BBX-010-18410";
//	private static final String pwd = "ea2da@66";
//	private static Client client;
//	static {
//		try {
//			client = new Client(sn, pwd);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}

	/**
	 * 【群发短信告知审核专员有超过5分钟还未审核的视频】
	 * 
	 * @param strPhone
	 */
	private void Mass(List<String> strPhones) {
		String content = "有超过5分钟还未审核的视频，请及时审核!" + new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date())+"【巴士在线】";
		try {
			content = URLEncoder.encode(content, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (strPhones.size() > 0){
			// send message for Phone
			String phones = strPhones.toString().replaceAll(" ", "").replace("[", "").replace("]", "");
			Client client = Client.getInstance();
			String backData = client.mdsmssend(phones, content);
			logger.info("------给手机号为：【" + phones + "】发送了5分钟未审核提醒，回执：" + backData + "------");
		}
	}
}
