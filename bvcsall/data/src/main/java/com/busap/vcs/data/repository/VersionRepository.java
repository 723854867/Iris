package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.Version;

/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = "versionRepository")
public interface VersionRepository extends BaseRepository<Version, Long> { 
	  
	public Version findByType(String type);
}
