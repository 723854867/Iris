package com.busap.vcs.data.repository;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.ActivityVideo;

/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = "activityVideoRepository")
public interface ActivityVideoRepository extends BaseRepository<ActivityVideo, Long> {
	
	public List findActivityidByVideoid(Long videoid);
	
	@Modifying
	@Query("delete from ActivityVideo a where a.videoid=?1")
	public void deleteByVideoid(Long videoid);
	
	@Modifying
	@Query("update ActivityVideo a set a.orderNum=?1 where a.id=?2")
	public int saveOrderNum(int orderNum,Long Id);
	
	@Modifying
	@Query("delete from ActivityVideo a where a.videoid=?1 and a.activityid=?2")
	public void deleteByVideoidAndActivityId(Long videoid,Long activityid);
	
	@Query("select count(a.id) from ActivityVideo a where a.videoid=?1 and a.activityid=?2")
	public Long findCountByVideoidAndActivityId(Long videoid,Long activityid);

	@Query("select count(a.id) from ActivityVideo a where a.creatorId=?1 and a.activityid=?2")
	public Long findCountByCreatorIdAndActivityId(Long userid,Long activityid);
}
