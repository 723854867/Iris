package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.Settlement;
import com.busap.vcs.data.model.ExportSettlement;

import java.util.List;
import java.util.Map;

public interface SettlementDao {

    int insert(Settlement settlement);

    List<Settlement> selectAll(Map<String,Object> params);

    List<ExportSettlement> selectExportSettlementRecord(Map<String,Object> params);

	List<Settlement> select(Map<String, Object> params);
	
	List<Map<String,Object>> querySettlement(Map<String,Object> params);

	Long getTotalSettlement(Map<String,Object> params);
}