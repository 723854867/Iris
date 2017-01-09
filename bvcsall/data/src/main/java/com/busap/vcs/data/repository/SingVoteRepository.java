package com.busap.vcs.data.repository;


import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.SingVote;


@Resource(name = "singVoteRepository")
public interface SingVoteRepository extends BaseRepository<SingVote, Long> {

	@Query(nativeQuery=true,value="select sum(popularity) from sing_vote where dest_id=?1")
	public Long getPopularity(Long destId);
	
	@Query(nativeQuery=true,value="select sum(popularity) from sing_vote where creator_id=?1")
	public Long getContributePopularity(Long creatorId);
	

}
