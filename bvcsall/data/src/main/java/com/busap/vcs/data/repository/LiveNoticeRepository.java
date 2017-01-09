package com.busap.vcs.data.repository;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.busap.vcs.data.entity.LiveNotice;

/**
 * Created by 
 */
@Resource(name = "liveComplainRepository")
public interface LiveNoticeRepository extends BaseRepository<LiveNotice, Long> {
	
	@Transactional
	@Query("select ln from LiveNotice ln where ln.creatorId = ?1 and status=1 order by ln.createDate desc limit 1 ")
	public List<LiveNotice> findLiveNoticeByCreatorId(Long creatorId);
}
