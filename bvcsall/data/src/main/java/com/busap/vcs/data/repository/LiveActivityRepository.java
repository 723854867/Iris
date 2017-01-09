package com.busap.vcs.data.repository;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.LiveActivity;

@Resource(name = "liveActivityRepository")
public interface LiveActivityRepository extends BaseRepository<LiveActivity, Long> {
	
	@Query(nativeQuery=true,value="select * from live_activity l left join ruser_live_activity r on r.live_activity_id=l.id where r.creator_id=?1 order by r.create_at desc")
	public List<LiveActivity> findMyLiveActivity (Long uid);
	
	@Query(nativeQuery=true,value="select * from live_activity l left join ruser_live_activity r on r.live_activity_id=l.id where r.creator_id=?1 and l.start_time<?2 and l.end_time>?2 and l.status=1 order by r.create_at desc")
	public List<LiveActivity> findMyAvalibleLiveActivity (Long uid,Date current);
	
	/**
	 * 查询未下线的直播活动
	 * @return
	 */
	@Query(nativeQuery=true,value="select * from live_activity where status=1")
	public List<LiveActivity> getLiveActivityList();
}
