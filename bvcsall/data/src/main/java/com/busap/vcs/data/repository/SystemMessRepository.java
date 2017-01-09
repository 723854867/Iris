package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.SystemMess;

/**
 * Created by dmsong on 5/5/2015.
 */
@Resource(name = "systemMessRepository")
public interface SystemMessRepository extends BaseRepository<SystemMess, Long> {
		
}
