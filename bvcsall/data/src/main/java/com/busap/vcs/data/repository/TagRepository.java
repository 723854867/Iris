package com.busap.vcs.data.repository;

import java.util.List;

import com.busap.vcs.data.entity.Tag;

import javax.annotation.Resource;

/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = "tagRepository")
public interface TagRepository extends BaseRepository<Tag, Long> {
	
	public List findByStatus(String status);
}
