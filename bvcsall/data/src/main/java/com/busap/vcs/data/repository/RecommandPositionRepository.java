package com.busap.vcs.data.repository;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.RecommandPosition;

@Resource(name = "recommandPositionRepository")
public interface RecommandPositionRepository extends BaseRepository<RecommandPosition, Long> {

	@Query("select count(*) from RecommandPosition where status=1 and page<?1")
	public int getPrePagePositionCount(Integer page);
	
	@Query("select rp from RecommandPosition rp where rp.status=1 and (NOW() BETWEEN rp.startTime and rp.endTime) and page=?1")
	public List<RecommandPosition> findByPage(Integer page);
}
