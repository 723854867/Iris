package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.AdvImage;

/**
 * Created by linghai.kong on 05/07/2015.
 */
@Resource(name = "advImageRepository")
public interface AdvImageRepository extends BaseRepository<AdvImage, Long> {
	
}
