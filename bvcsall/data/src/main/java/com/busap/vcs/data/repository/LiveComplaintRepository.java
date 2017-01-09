package com.busap.vcs.data.repository;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.busap.vcs.data.entity.LiveComplaint;

/**
 * Created by 
 */
@Resource(name = "liveComplainRepository")
public interface LiveComplaintRepository extends BaseRepository<LiveComplaint, Long> {
	
	/**
	 * 批量删除
	 * @param ids
	 */
	@Modifying
	@Transactional
	@Query("delete from LiveComplaint a where a.id in(?1)")
	public void deleteAllComplaints(List<Long> ids); 
	
	/**
	 * 批量取消投诉
	 * @param ids
	 */
	@Modifying
	@Transactional
	@Query("update LiveComplaint a set a.stat=1 where a.id in(?1)")
	public void cancleAllComplaints(List<Long> ids); 
	
	@Modifying
	@Transactional
	@Query("update LiveComplaint a set a.stat=1 where a.stat=0 and a.liveId= ?1 ")
	public void dealComplaints(Long liveId); 
	
	@Transactional
	@Query("select count(*) from LiveComplaint a where a.stat=0 ")
	public Long unDealComplaintsCount();
}
