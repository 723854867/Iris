package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.LoadConfigUrl;
import com.busap.vcs.data.entity.Sign;
import com.busap.vcs.data.vo.LoadConfigUrlVO;
import com.busap.vcs.data.vo.SignVO;

public interface SignService extends BaseService<Sign,Long> {
	/**
	 * load All LoadConfigUrl
	 */
	List<SignVO> findUserAllSgin(String uid,int pageStart,int pageSize);
	
	/**
	 * 获取用户所有的积分信息分页
	 */
	List<SignVO> findAllSgin(String uid,int pageStart,int pageSize);
	/**
	 * 统计每天的积分获取信息
	 */
	List<SignVO> findEveryDaySumSign(int pageStart,int pageSize,String startTime,String endTime,Integer startCount,Integer endCount);
	/**
	 * 统计每天的积分获取信息的总天数
	 */
	Integer findEveryDaySumSignCount(String startTime,String endTime,Integer startCount,Integer endCount);
	/**
	 * 获取今天的信息记录
	 */
	Integer findSginToday();
	/**
	 * 获取昨天的积分信息
	 */
	List<SignVO> findYesterdaySign(String uid);
	/**
	 * 获取今天的积分信息
	 */
	List<SignVO> findTodaySign(String uid);
	/**
	 * 获取超过30天的用户数
	 */
	Integer findAllBeyondMaxUser();
	/**
	 * 获取连续领取1天到30天的用户数
	 */
	Integer findAllSignUser();
	/**
	 *查找用户是否点赞视频获取过积分
	 */
	List<SignVO> findPraiseShareSign(String videoId,String fromUid,String uid);
}
