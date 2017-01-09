package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.RuserLiveActivity;

public interface RuserLiveActivityService extends BaseService<RuserLiveActivity, Long> {
 
	/**
	 * 判断用户是否参与了此活动
	 * @param uid
	 * @param liveActivityId
	 * @return
	 */
	public Integer isJoin(Long uid,Long liveActivityId);
	
	
	/**
	 * 查询用户参与的直播活动id
	 * @param uid
	 * @return
	 */
	public List<Long> getLiveActivityIdByUid(Long uid);

	/**
	 * 查询报名此活动的人数
	 * @param liveActivityId 直播活动ID
	 * @return
	 */
	Integer getCountByLiveActivityId(Long liveActivityId);
	
}
