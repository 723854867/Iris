package com.busap.vcs.srv.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.base.Action;
import com.busap.vcs.base.Message;
import com.busap.vcs.base.Module;
import com.busap.vcs.bean.VideoTaskItem;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.consumer.Consumer;
import com.busap.vcs.dao.VideoCheckDao;
import com.busap.vcs.srv.IUserFocusService;
import com.busap.vcs.srv.JedisService;


/**
 * 发布视频自动关注
 */
@Service
public class UserFocusService implements  IUserFocusService{
	// 普通用户自动关注上限
	private static final int MAX_FOCUS_USERS_NUMBER = 40;
	// VIP用户自动关注上限
	private static final int MAX_FOCUS_VIP_USERS_NUMBER = 70;

	private static final Logger logger = LoggerFactory.getLogger(UserFocusService.class);
	@Autowired
	VideoCheckDao videoCheckDao;
	@Autowired
	JedisService jedisService;
	@Autowired
	IntegralService integralService;
	public static final String KEY_ALL_MAJIA_UID = "ALL_MAJIA_UIDS_ASDPLSKDF_";
	private int[] delay = new int[]{5*60*1000, 60*60*1000, 120*60*1000};
	/**
	 * 用户发布视频后，分时段对用户进行涨粉
	 * 1、审核通过后，60分钟内完成第一批次自动关注，普通用户涨粉10-30，V用户涨粉20-40；
	 * 2、审核通过后，120分钟后完成第二批次自动关注，普通用户涨粉5-10，V用户涨粉10-30。
	 */
	@Override
	public boolean doFocus(VideoTaskItem vti ) {
		long now = new Date().getTime();
		if (vti.getFcount() >= 2) {
			return false;
		}
		int attentionNumber = 0;
		if ((now - vti.getPubDate()) < delay[1]
				&& (now - vti.getPubDate()) >= delay[0]
				&& vti.getFcount()==0) {
				// 第一批关注
			attentionNumber = this.payAttention(vti.getUid(), vti.getFcount());
		} else if ((now - vti.getPubDate()) > delay[2]
				&& vti.getFcount()==1){
				// 第二批关注
			attentionNumber = this.payAttention(vti.getUid(), vti.getFcount());
		}
		if (attentionNumber > 0) {
			logger.debug("doFocus()------uid= " + vti.getUid() + ", vid=" + vti.getVid()
					+ ", vti.getFcount=" + vti.getFcount() + ", attentionNumber =" + attentionNumber);
			vti.setFun(vti.getFun() + attentionNumber);
		}
		return true;
	}

	/**
	 * 验证今天关注数量是否已达上限
	 * @param uid 被关注人
	 * @return true:已达上限  false:未达上限
	 */
	public boolean checkTodayOverSize(long uid) {
		return (checkIsVIP(uid) && videoCheckDao.todayAttentionCount(uid) >= MAX_FOCUS_VIP_USERS_NUMBER)
				|| (!checkIsVIP(uid) && videoCheckDao.todayAttentionCount(uid) >= MAX_FOCUS_USERS_NUMBER);
	}

	private int payAttention(long uid, int fcount) {
		int count = 0;
		boolean isOversize = checkTodayOverSize(uid);
		if (isOversize) {
			return count;
		}
		int attentionCount = this.getRandom(uid, fcount);
		List<String> majiaUids = jedisService.getSetRanomMembers(KEY_ALL_MAJIA_UID, attentionCount);
		// 本次关注数量
		for (String mid : majiaUids) {
			if (!videoCheckDao.isAttentUser(uid, Long.valueOf(mid))) {
				// 马甲用户还没关注过视频发布者
//				logger.info("payAttention --> insertAttention(uid=" + uid + ", mid=" + mid + ")");
				int result = videoCheckDao.insertAttention(uid, Long.valueOf(mid));
				if (result > 0) {
					// 添加关注成功，放到redis里
					// 马甲增加关注数
					videoCheckDao.updateUserAttentionNumber( Long.valueOf(mid), 1);
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
		return count;
	}

	private int getRandom(long uid, int fcount) {
		if (fcount > 0) {
			// 第二批关注
			if (checkIsVIP(uid)) {
				return new Random().nextInt(20) + 10;
			} else {
				return new Random().nextInt(5) + 5;
			}
		} else {
			// 第一批关注
			if (checkIsVIP(uid)) {
				return new Random().nextInt(20) + 20;
			} else {
				return new Random().nextInt(20) + 10;
			}
		}
	}
	private boolean checkIsVIP(long uid) {
		boolean isBlue = jedisService.isSetMemberInShard(BicycleConstants.VIP_OF_BLUE, String.valueOf(uid));
		boolean isYellow = jedisService.isSetMemberInShard(BicycleConstants.VIP_OF_YELLOW, String.valueOf(uid));
		return isBlue || isYellow;
	}

	public void initMajia(){
		List<String> list = videoCheckDao.findMajiaIds();
		String[] majiaids = list.toArray(new String[list.size()]);
		jedisService.setValueToSetInShard(KEY_ALL_MAJIA_UID, majiaids);
	}
}
