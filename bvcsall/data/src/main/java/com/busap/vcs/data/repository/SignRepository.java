package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.LoadConfigUrl;
import com.busap.vcs.data.entity.Sign;

/**
 * Created by lj on 7/20/2015.
 */
@Resource(name = "signRepository")
public interface SignRepository extends BaseRepository<Sign,Long> {

}
