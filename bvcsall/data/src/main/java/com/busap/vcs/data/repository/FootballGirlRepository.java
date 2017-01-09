package com.busap.vcs.data.repository;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.FootballGirl;


@Resource(name = "footballGirlRepository")
public interface FootballGirlRepository extends BaseRepository<FootballGirl, Long> {
	@Query(nativeQuery=true,value="select count(*) from football_girl where phone=?1")
	public int getCountByPhone(String phone);
	
	@Query(nativeQuery=true,value="select count(*) from football_girl where creator_id=?1")
	public int getCountByUid(Long uid);
	
	@Query(nativeQuery=true,value="select creator_id from football_girl")
	public List<Long> findUids();

}
