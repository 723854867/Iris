package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.Spread;

/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = "spreadRepository")
public interface SpreadRepository extends BaseRepository<Spread, Long> {
	
	@Query(nativeQuery=true,value="select count(*) from spread where creator_id=?1")
	public int uidExist(Long uid);
}
