package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.ExchangeRecode;
import com.busap.vcs.data.entity.ExchangeRecodeExample;
import java.util.List;
import java.util.Map;

import com.busap.vcs.data.model.ExportUserExchangeRecord;
import com.busap.vcs.data.model.UserExchangeRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("exchangeRecodeMapper")
public interface ExchangeRecodeMapper {
    int countByExample(ExchangeRecodeExample example);

    int deleteByExample(ExchangeRecodeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ExchangeRecode record);

    int insertSelective(ExchangeRecode record);

    List<ExchangeRecode> selectByExample(ExchangeRecodeExample example);

    ExchangeRecode selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ExchangeRecode record, @Param("example") ExchangeRecodeExample example);

    int updateByExample(@Param("record") ExchangeRecode record, @Param("example") ExchangeRecodeExample example);

    int updateByPrimaryKeySelective(ExchangeRecode record);

    int updateByPrimaryKey(ExchangeRecode record);

    List<UserExchangeRecord> selectUserExchangeRecord(Map<String,Object> params);

    Map<String,Object> selectGoldCoinAndGoldCount(Map<String,Object> params);

    List<ExportUserExchangeRecord> selectExportUserExchangeRecord(Map<String,Object> params);

    Integer selectPersonCountByUserId(Map<String,Object> params);

}