package com.busap.vcs.data.repository;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.RuserLiveActivity;

@Resource(name = "ruserLiveActivityRepository")
public interface RuserLiveActivityRepository extends BaseRepository<RuserLiveActivity, Long> {
	
	/**
	 * 判断用户是否参与某个直播活动
	 * @param uid
	 * @param liveActivityId
	 * @return
	 */
	@Query(nativeQuery=true,value="select count(*) from ruser_live_activity where creator_id=?1 and live_activity_id=?2")
	public Integer isJoin(Long uid, Long liveActivityId);
	
	/**
	 * 查询用户参与的直播活动id
	 * @param uid
	 * @return
	 */
	@Query(nativeQuery=true,value="select live_activity_id from ruser_live_activity where creator_id=?1")
	public List<Long> getLiveActivityIdByUid(Long uid);

	/**
	 * 查询报名此活动的人数
	 * @param liveActivityId 直播活动ID
	 * @return
	 */
	@Query(nativeQuery=true,value="select count(*) as signCount from ruser_live_activity where live_activity_id = ?1")
	Integer getCountByLiveActivityId(Long liveActivityId);
	
	
}
