package com.busap.vcs.data.repository;


import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.Bstar;


@Resource(name = "bstarRepository")
public interface BstarRepository extends BaseRepository<Bstar, Long> {
	
	@Query(nativeQuery=true,value="select count(*) from bstar where creator_id=?1")
	public int getCountByUid(Long uid);

}
