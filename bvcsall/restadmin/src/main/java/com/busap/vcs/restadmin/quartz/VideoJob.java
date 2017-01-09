package com.busap.vcs.restadmin.quartz;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.repository.RuserRepository;
import com.busap.vcs.data.repository.VideoRepository;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.webcomn.util.SpringWebUtils;

public class VideoJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(Quartz.class);

	private VideoRepository videoRepository;

	private RuserRepository ruserRepository;
	
	private RuserService ruserService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ApplicationContext appCtx = SpringWebUtils.getApplicationContext();
		videoRepository = (VideoRepository) appCtx.getBean("videoRepository");
		ruserRepository = (RuserRepository) appCtx.getBean("ruserRepository");
		ruserService = (RuserService)appCtx.getBean("ruserService");
		if (StringUtils.isNotBlank(context.getJobDetail().getName())) {
			List<Video> list = videoRepository.findPlanPublished();
			videoRepository.autoPublish(Long.parseLong(context.getJobDetail().getName()));
			logger.info("计划发布视频ID：", context.getJobDetail().getName());
			for (Video video : list) {
				int count = videoRepository.countAdminVideoForRuser(video.getCreatorId());// 统计该用户发的视频数
				ruserRepository.updateUserVideoCount(count,video.getCreatorId());
				//计算用户排行人气值
				ruserService.executeDayUserPopularity(video.getCreatorId());
			}
		}
	}
}