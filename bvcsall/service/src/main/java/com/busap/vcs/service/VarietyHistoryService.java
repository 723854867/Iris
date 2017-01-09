package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.VarietyHistory;

public interface VarietyHistoryService  extends BaseService<VarietyHistory, Long> {
	
	List<VarietyHistory> findAllVariety();
	
	void addVarietyHistory(VarietyHistory variety);
	
	void updateVarietyHistory(VarietyHistory variety);
	
	void deleteVarietyHistory(Long id);
}
