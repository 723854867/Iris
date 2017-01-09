package com.busap.vcs.service.impl;

import com.busap.vcs.data.mapper.ExchangeRecodeMapper;
import com.busap.vcs.data.model.ExportUserExchangeRecord;
import com.busap.vcs.data.model.UserExchangeRecord;
import com.busap.vcs.service.ExchangeRecodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by busap on 2016/4/13.
 */
@Service("exchangeRecodeService")
public class ExchangeRecodeServiceImpl extends BaseServiceImpl<UserExchangeRecord, Long> implements ExchangeRecodeService {

    @Resource
    private ExchangeRecodeMapper exchangeRecodeMapper;

    @Override
    public List<UserExchangeRecord> queryUserExchangeRecord(Map<String,Object> params){
        return exchangeRecodeMapper.selectUserExchangeRecord(params);
    }

    @Override
    public Map<String, Object> queryGoldCoinAndGoldCount(Map<String, Object> params) {
        return exchangeRecodeMapper.selectGoldCoinAndGoldCount(params);
    }

    @Override
    public List<ExportUserExchangeRecord> queryExportUserExchangeRecord(Map<String, Object> params) {
        return exchangeRecodeMapper.selectExportUserExchangeRecord(params);
    }

    @Override
    public Integer queryPersonCountByUserId(Map<String,Object> params){
        return exchangeRecodeMapper.selectPersonCountByUserId(params);
    }
}
