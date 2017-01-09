package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.MusicType;

/**
 * Created by dmsong on 02/03/2015.
 */
@Resource(name = "musicTypeRepository")
public interface MusicTypeRepository extends BaseRepository<MusicType, Long> {
	
	
}
