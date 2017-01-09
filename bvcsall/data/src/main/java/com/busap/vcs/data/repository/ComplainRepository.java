package com.busap.vcs.data.repository;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.busap.vcs.data.entity.Complain;

/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = "complainRepository")
public interface ComplainRepository extends BaseRepository<Complain, Long> {
	
	/**
	 * 批量删除
	 * @param ids
	 */
	@Modifying
	@Transactional
	@Query("delete from Complain a where a.id in(?1)")
	public void deleteAllComplains(List<Long> ids); 
	
	/**
	 * 批量取消投诉
	 * @param ids
	 */
	@Modifying
	@Transactional
	@Query("update Complain a set a.stat=1 where a.id in(?1)")
	public void cancleAllComplains(List<Long> ids); 
}
