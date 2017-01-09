package com.busap.vcs.consumer;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.dao.VideoCheckDao;
import com.busap.vcs.srv.JedisService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class MajiaCommentTask implements Job {

	private static final Logger logger = LoggerFactory.getLogger(MajiaCommentTask.class);

	public static final String KEY_ALL_MAJIA_UID = "ALL_MAJIA_UIDS_ASDPLSKDF_";

	@Autowired
	private VideoCheckDao videoCheckDao;

	@Autowired
	private JedisService jedisService;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		Set<String> videoIds = jedisService.getSetFromShard(BicycleConstants.NEW_VIDEO_AUTO_COMMENT);

		long nowMillis = System.currentTimeMillis();

		for (String keyVideoId : videoIds) {
			if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 5) {
				// 0-5点不执行自动评论
				continue;
			}
			Map<String, String> videoMap = jedisService.getMapByKey(keyVideoId);
			if (checkMap(videoMap)) {
				long userId = Long.parseLong(videoMap.get("uid"));
				long pubDate = Long.parseLong(videoMap.get("pubDate"));
				long vid = Long.parseLong(videoMap.get("vid"));
				if (!checkVideoPraiseCount(vid)) {
					continue;
				}
				boolean isVIP = isVIP(userId);
				if (nowMillis - pubDate > 3 * 60 * 1000 && nowMillis - pubDate <= 15 * 60 * 1000) {
					// 审核通过后，3-15分钟内，进行第一批次评论，随机选取2-5个马甲进行评论
					// 约12 分钟 每分钟0.16-0.42个
					float f = new Random().nextFloat();
					if (f < 0.0822) {
						logger.info("MajiaCommentTask first batch: uid=" + userId);
						doComment(vid);
					}
				} else if (nowMillis - pubDate > 25 * 60 * 1000 && nowMillis - pubDate <= 40 * 60 * 1000) {
					// 视频审核通过后，25-40分钟内，进行第二批次评论，随机选取3-8个马甲进行评论
					// 约15 分钟 每分钟0.2-0.67个
					float f = new Random().nextFloat();
					if (f < 0.12325) {
						logger.info("MajiaCommentTask second batch: uid=" + userId);
						doComment(vid);
					}
					if (!isVIP && videoCheckDao.hasComment(vid)) {
						// if not vip user and has auto_comment, remove value from redis when finish second job
						logger.info("MajiaCommentTask remove from cache: uid=" + userId + " is not VIP!");
						jedisService.delete(keyVideoId);
						jedisService.deleteSetItemFromShard(BicycleConstants.NEW_VIDEO_AUTO_COMMENT, keyVideoId);
					}
				} else if (nowMillis - pubDate > 80 * 60 * 1000 && nowMillis - pubDate <= 100 * 60 * 1000 && isVIP) {
					// 视频审核通过后，80-100分钟内，进行第三批次评论，随机选取5-12个马甲进行评论
					// 约20 分钟 每分钟0.25-0.6个
					float f = new Random().nextFloat();
					if (f < 0.1204) {
						logger.info("MajiaCommentTask third batch: uid=" + userId);
						doComment(vid);
					}
				} else if (nowMillis - pubDate > 150 * 60 * 1000 && nowMillis - pubDate <= 180 * 60 * 1000 && isVIP) {
					// 视频审核通过后，150-180分钟内，进行第四批次评论，随机选取8-15个马甲进行评论
					// 约20 分钟 每分钟0.4-0.75个
					float f = new Random().nextFloat();
					if (f < 0.1629) {
						logger.info("MajiaCommentTask forth batch: uid=" + userId);
						doComment(vid);
					}
				} else if (nowMillis - pubDate > 180 * 60 * 1000) {
					logger.info("Job Finish: remove value from redis====KeyVideoId=" + keyVideoId);
					jedisService.delete(keyVideoId);
					jedisService.deleteSetItemFromShard(BicycleConstants.NEW_VIDEO_AUTO_COMMENT, keyVideoId);
				}
			}
		}
	}

	/**
	 * 检查这个视频的评论数是否小于点赞数
	 * @param videoId 视频id
	 * @return true:评论 < 点赞; false:评论 > 点赞;
	 */
	public boolean checkVideoPraiseCount(long videoId) {
		return videoCheckDao.checkPraiseCommentCount(videoId);
	}

	public void doComment(long videoId) {
		boolean bln = videoCheckDao.allowEvaluation(videoId);
		if (bln) {
			String content = videoCheckDao.getRandomComment();

			List<String> majiaUids = jedisService.getSetRanomMembers(KEY_ALL_MAJIA_UID, 1);
			if (majiaUids.size() == 1 && StringUtils.isNotBlank(content)) {
				long majiaId = Long.parseLong(majiaUids.get(0));
				videoCheckDao.saveEvaluation(majiaId, content, videoId);
				videoCheckDao.incEvaluationCount(videoId);
			}
		}
	}

	private boolean isVIP(long uid) {
		boolean isBlue = jedisService.isSetMemberInShard(BicycleConstants.VIP_OF_BLUE, String.valueOf(uid));
		boolean isYellow = jedisService.isSetMemberInShard(BicycleConstants.VIP_OF_YELLOW, String.valueOf(uid));
		boolean isGreen = jedisService.isSetMemberInShard(BicycleConstants.VIP_OF_GREEN, String.valueOf(uid));
		return isBlue || isYellow || isGreen;

	}

	private boolean checkMap(Map<String, String> map) {
		return StringUtils.isNotBlank(map.get("vid"))
				&& StringUtils.isNotBlank(map.get("uid"))
				&& StringUtils.isNotBlank(map.get("pubDate"))
				&& StringUtils.isNumeric(map.get("vid"))
				&& StringUtils.isNumeric(map.get("uid"))
				&& StringUtils.isNumeric(map.get("pubDate"));
	}

}