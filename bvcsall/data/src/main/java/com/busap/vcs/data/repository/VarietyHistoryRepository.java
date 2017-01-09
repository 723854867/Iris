package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.VarietyHistory;

/**
 * 网综往期
 * Created by dmsong
 */
@Resource(name = "varietyHistoryRepository")
public interface VarietyHistoryRepository extends BaseRepository<VarietyHistory, Long> {
	
	
}
