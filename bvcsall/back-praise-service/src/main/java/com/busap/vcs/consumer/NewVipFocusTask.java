package com.busap.vcs.consumer;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.dao.VideoCheckDao;
import com.busap.vcs.srv.JedisService;
import com.busap.vcs.srv.impl.IntegralService;
import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 新增vip用户 自动关注
 * @author yangxinyu
 *
 */
@Service
public class NewVipFocusTask implements Job {

	protected static final Logger log = LoggerFactory.getLogger(NewVipFocusTask.class);

	@Autowired
	private JedisService jedisService;
	@Autowired
	private VideoCheckDao videoCheckDao;
	@Autowired
	private IntegralService integralService;

	public static final String KEY_ALL_MAJIA_UID = "ALL_MAJIA_UIDS_ASDPLSKDF_";

	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {

		Map<Long, Long> idTimeMap = new HashMap<>();
		Set<String> vipIds = jedisService.getSetFromShard(BicycleConstants.NEW_VIP_AUTO_FOCUS);
		idTimeMap = setVipData(vipIds, idTimeMap);
		/**
		 * 整理数据
 		 */
		long nowMillis = System.currentTimeMillis();
		for (Long uid : idTimeMap.keySet()) {
			long timeMillis = idTimeMap.get(uid);

			if (nowMillis - timeMillis > 5 * 60 * 1000 && nowMillis - timeMillis <= 15 * 60 * 1000) {
				// 5-15分钟内，完成第一批次涨粉，第一批50-70个
				// 约10 分钟 每分钟 5-7个
				log.info("NewVipFocusTask first batch: uid=" + uid);
				int num = new Random().nextInt(3) + 5;
				doFocus(uid, num);
			} else if (nowMillis - timeMillis > 100 * 60 * 1000 && nowMillis - timeMillis <= 120 * 60 * 1000) {
				// 100-120分钟内，完成第二批次涨粉，第二批30-40个
				// 约20 分钟 每分钟 1.5-2 个
				log.info("NewVipFocusTask second batch: uid=" + uid);
				float f = new Random().nextFloat() + 1.25f;
				BigDecimal bigDecimal = new BigDecimal(f).setScale(0, BigDecimal.ROUND_HALF_UP);
				doFocus(uid, bigDecimal.intValue());
			} else if (nowMillis - timeMillis > 180 * 60 * 1000 && nowMillis - timeMillis <= 200 * 60 * 1000) {
				// 180-200分钟内，完成第三批次涨粉，第三批20-30个
				// 约20 分钟 每分钟 1-1.5 个
				log.info("NewVipFocusTask third batch: uid=" + uid);
				float f = new Random().nextFloat() + 0.75f;
				BigDecimal bigDecimal = new BigDecimal(f).setScale(0, BigDecimal.ROUND_HALF_UP);
				doFocus(uid, bigDecimal.intValue());
			} else if (nowMillis - timeMillis > 400 * 60 * 1000 && nowMillis - timeMillis <= 500 * 60 * 1000) {
				// 400-500分钟内，完成第四批次涨粉，第一批50-70个
				// 约100 分钟 每分钟 0.5-0.7 个
				log.info("NewVipFocusTask forth batch: uid=" + uid);
				float f = new Random().nextFloat();
				if (f < 0.6) {
					doFocus(uid, 1);
				}
			} else if (nowMillis - timeMillis > 800 * 60 * 1000 && nowMillis - timeMillis <= 1000 * 60 * 1000) {
				// 800-1000分钟内，完成第五批次涨粉，第二批30-40个
				// 约200 分钟 每分钟 0.15-0.2 个
				log.info("NewVipFocusTask fifth batch: uid=" + uid);
				float f = new Random().nextFloat();
				if (f < 0.18) {
					doFocus(uid, 1);
				}
			} else if (nowMillis - timeMillis > 1500 * 60 * 1000 && nowMillis - timeMillis <= 1700 * 60 * 1000) {
				// 1500-1700分钟内，完成第六批次涨粉，第三批20-30个
				// 约200 分钟 每分钟 0.1-0.15 个
				log.info("NewVipFocusTask sixth batch: uid=" + uid);
				float f = new Random().nextFloat();
				if (f < 0.13) {
					doFocus(uid, 1);
				}
			} else if (nowMillis - timeMillis > 2200 * 60 * 1000 && nowMillis - timeMillis <= 2400 * 60 * 1000) {
				// 2200-2400分钟内，完成第七批次涨粉，第一批50-70个
				// 约200 分钟 每分钟 0.25-0.35 个
				log.info("NewVipFocusTask seventh batch: uid=" + uid);
				float f = new Random().nextFloat();
				if (f < 0.3) {
					doFocus(uid, 1);
				}
			} else if (nowMillis - timeMillis > 3200 * 60 * 1000 && nowMillis - timeMillis <= 3400 * 60 * 1000) {
				// 3200-3400分钟内，完成第八批次涨粉，第二批30-40个
				// 约200 分钟 每分钟 0.15-0.2 个
				log.info("NewVipFocusTask eighth batch: uid=" + uid);
				float f = new Random().nextFloat();
				if (f < 0.18) {
					doFocus(uid, 1);
				}
			} else if (nowMillis - timeMillis > 3800 * 60 * 1000 && nowMillis - timeMillis <= 4000 * 60 * 1000) {
				// 3800-4000分钟内，完成第九批次涨粉，第三批20-30个
				// 约200 分钟 每分钟 0.1-0.15 个
				log.info("NewVipFocusTask ninth batch: uid=" + uid);
				float f = new Random().nextFloat();
				if (f < 0.13) {
					doFocus(uid, 1);
				}
			} else if (nowMillis - timeMillis > 4000 * 60 * 1000) {
				String value = String.valueOf(uid) + "-" + String.valueOf(timeMillis);
				if (vipIds.contains(value)) {
					jedisService.deleteSetItemFromShard(BicycleConstants.NEW_VIP_AUTO_FOCUS,
							String.valueOf(uid) + "-" + String.valueOf(timeMillis));
				}

			}
		}

	}


	/**
	 * 马甲关注动作
	 * @param uid 用户id
	 * @param num 关注数量
	 */
	public boolean doFocus (Long uid, int num) {
		// 取样扩大10倍 防止部分马甲已关注该用户
		log.info("NewVipFocusTask : doFocus() ==== uid:" + uid + " & num:" + num);
		List<String> majiaUids = jedisService.getSetRanomMembers(KEY_ALL_MAJIA_UID, num * 10);
		// 控制此次关注数量
		int count = 0;
		for (String mid : majiaUids) {
			if (count >= num) {
				continue;
			}
			// 判断马甲用户 是否关注过视频发布者
			if (!videoCheckDao.isAttentUser(uid, Long.valueOf(mid)) && count < num) {
				// 添加关注
				int result = videoCheckDao.insertAttention(uid, Long.valueOf(mid));
				if (result > 0) {
					// 马甲增加关注数
//					videoCheckDao.updateUserAttentionNumber( Long.valueOf(mid), 1);
					// 添加关注成功，放到redis里
					jedisService.setValueToSetInShard(BicycleConstants.ATTENTION_ID_OF + mid, String.valueOf(uid));
					// 完成粉丝任务
					integralService.getIntegralFromFanNumber(uid);
					count++;
				}
			}
		}
		if (count > 0) {
			// 用户增加粉丝
			videoCheckDao.updateUserFunsNumber(uid, count);
		}
		return true;
	}

	private Map<Long, Long> setVipData(Set<String> source, Map<Long, Long> result) {
		for (String string : source) {
			String[] idTime = string.split("-");
			if (StringUtils.isNumeric(idTime[0]) && StringUtils.isNumeric(idTime[1])) {
				result.put(Long.parseLong(idTime[0]), Long.parseLong(idTime[1]));
			}
		}
		return result;
	}

}
