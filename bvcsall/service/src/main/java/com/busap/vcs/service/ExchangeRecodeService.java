package com.busap.vcs.service;

import com.busap.vcs.data.model.ExportUserExchangeRecord;
import com.busap.vcs.data.model.UserExchangeRecord;

import java.util.List;
import java.util.Map;

/**
 * Created by busap on 2016/4/13.
 */

public interface ExchangeRecodeService {

    List<UserExchangeRecord> queryUserExchangeRecord(Map<String,Object> params);

    Map<String,Object> queryGoldCoinAndGoldCount(Map<String,Object> params);

    List<ExportUserExchangeRecord> queryExportUserExchangeRecord(Map<String,Object> params);

    Integer queryPersonCountByUserId(Map<String,Object> params);

}
