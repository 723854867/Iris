package com.busap.vcs.data.repository;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Update;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.Praise;

/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = "praiseRepository")
public interface PraiseRepository extends BaseRepository<Praise, Long> { 
	 
	public Praise findByCreatorIdAndVideoId(Long creatorId,Long videoId); 

	public void deleteByCreatorIdAndVideoId(Long creatorId,Long videoId); 

	@Query("select p from Praise p  where p.videoId=?1 and p.adminId>0 ")
	public List<Praise> findByMajia(Long videoId);
}
