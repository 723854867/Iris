package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.OperatePlan;

/**
 * Created by dmsong on 8/19/2015.
 */
@Resource(name = "operatePlanRepository")
public interface OperatePlanRepository extends BaseRepository<OperatePlan, Long> {
	
	
}
