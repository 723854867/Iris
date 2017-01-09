package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.Activity;
import com.busap.vcs.data.entity.Subscribe;
import com.busap.vcs.data.entity.Video;

public interface SubscribeService extends BaseService<Subscribe, Long> {
	
	/**
	 * 判断用户是否订阅某活动，1：是，0：否
	 * @param parseLong
	 * @param activityId
	 * @return
	 */
	public Integer isSubscribed(long uid, Long activityId);

	/**
	 * 订阅
	 * @param uid
	 * @param activityId
	 * @return
	 */
	public Integer subscribe(long uid, Long activityId,String dateFrom);

	/**
	 * 取消订阅
	 * @param uid
	 * @param activityId
	 * @return
	 */
	public Integer cancelSubscribe(long uid, Long activityId);

	/**
	 * 获得订阅活动列表
	 * @param uid
	 * @return
	 */
	public List<Activity> getMyActivityList(long uid);

	/**
	 * 获得订阅活动的视频列表
	 * @param uid
	 * @param timestamp
	 * @param count
	 * @return
	 */
	public List<Video> getMyActivityVideoList(long uid, Long timestamp,Integer count);
 
}
