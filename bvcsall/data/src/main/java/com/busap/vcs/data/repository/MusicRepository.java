package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.Music;

/**
 * Created by dmsong on 02/03/2015.
 */
@Resource(name = "musicRepository")
public interface MusicRepository extends BaseRepository<Music, Long> {
	
	
}
