package com.busap.vcs.data.repository;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.Favorite;
import com.busap.vcs.data.entity.Video;

/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = "favoriteRepository")
public interface FavoriteRepository extends BaseRepository<Favorite, Long> {
	
	public List<Favorite> findByCreatorId(Long creator);
	
	public Favorite findByCreatorIdAndVideoId(Long creatorId,Long videoId);

	public void deleteByCreatorIdAndVideoId(Long creatorId,Long videoId); 
}
