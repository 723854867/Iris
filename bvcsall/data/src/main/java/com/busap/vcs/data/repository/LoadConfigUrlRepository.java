package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.LoadConfigUrl;

/**
 * Created by lj on 7/2/2015.
 */
@Resource(name = "loadConfigUrlRepository")
public interface LoadConfigUrlRepository extends BaseRepository<LoadConfigUrl,Long> {

}
