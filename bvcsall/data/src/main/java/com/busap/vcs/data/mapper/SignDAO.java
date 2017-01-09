package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.vo.SignVO;

public interface SignDAO {
	/**
	 * 获取用户所有的积分信息分页
	 */
	public List<SignVO> findUserAllSgin(Map<String,Object> params);
	/**
	 * 统计每天的积分获取信息
	 */
	public List<SignVO> findEveryDaySumSign(Map<String,Object> params);
	/**
	 * 统计每天的积分获取信息的总天数
	 */
	public int findEveryDaySumSignCount(Map<String,Object> params);
	/**
	 * 获取今天的信息记录
	 */
	public Integer findSginToday();
	/**
	 * 获取昨天的积分信息
	 */
	public List<SignVO> findYesterdaySign(String uid);
	/**
	 * 获取今天的积分信息
	 */
	public List<SignVO> findTodaySign(String uid);
	/**
	 * 获取超过30天的用户数
	 */
	public int findAllBeyondMaxUser();
	/**
	 * 获取连续领取1天到30天的用户数
	 */
	public int findAllSignUser();
	/**
	 *查找用户是否点赞视频获取过积分
	 */
	public List<SignVO> findPraiseShareSign(Map<String,Object> params);
}
