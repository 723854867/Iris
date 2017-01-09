package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.busap.vcs.data.entity.LabelVideo;

@Resource(name = "labelVideoRepository")
public interface LabelVideoRepository extends BaseRepository<LabelVideo, Long> {
	
	@Modifying
	@Transactional
	@Query("delete from LabelVideo a where a.videoId =?1")
	public void deleteLabelVideo(Long vid); 
}
