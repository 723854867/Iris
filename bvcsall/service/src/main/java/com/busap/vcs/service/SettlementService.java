package com.busap.vcs.service;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Settlement;
import com.busap.vcs.data.model.ExportSettlement;

/**
 * Created by busap on 2015/12/30.
 */
public interface SettlementService extends BaseService<Settlement, Long> {

    List<Settlement> queryAll(Map<String,Object> params);
    
    List<Settlement> queryAll(Long receiver,int page,int size);

    List<ExportSettlement> queryExportSettlementRecord(Map<String,Object> params);

	void doSettle(Settlement sm) throws Exception;

	List<Map<String,Object>> querySettlement(Map<String,Object> params);

	Long getTotalSettlement(Map<String, Object> params);

}
