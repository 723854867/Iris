package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.Banner;

/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = "bannerRepository")
public interface BannerRepository extends BaseRepository<Banner, Long> {
	
	
}
