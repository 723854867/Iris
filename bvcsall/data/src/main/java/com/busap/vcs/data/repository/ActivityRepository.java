package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.Activity;

/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = "activityRepository")
public interface ActivityRepository extends BaseRepository<Activity, Long> {
	
	@Modifying
	@Query("update Activity a set a.order_num=?2 where a.id = ?1")
	public void updateOrderNum(Long activityId,Integer orderNum);
	
	@Query("select count(a.id) from Activity a  where a.order_num = ?1")
	public Long findByOrderNum(Integer orderNum);
	
	@Query("select count(a.id) from Activity a  where a.order_num = ?1 and a.id !=?2")
	public Long findByOrderNum(Integer orderNum,Long id);
	
	@Modifying
	@Query("update Activity a set a.status=?2 where a.id = ?1")
	public void updateActiveStatus(Long activityId,Integer status);
	
}
