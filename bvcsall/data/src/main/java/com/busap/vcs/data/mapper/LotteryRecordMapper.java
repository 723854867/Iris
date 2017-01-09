package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.LotteryRecord;
import com.busap.vcs.data.entity.LotteryRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LotteryRecordMapper {
    int countByExample(LotteryRecordExample example);

    int deleteByExample(LotteryRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(LotteryRecord record);

    int insertSelective(LotteryRecord record);

    List<LotteryRecord> selectByExample(LotteryRecordExample example);

    LotteryRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") LotteryRecord record, @Param("example") LotteryRecordExample example);

    int updateByExample(@Param("record") LotteryRecord record, @Param("example") LotteryRecordExample example);

    int updateByPrimaryKeySelective(LotteryRecord record);

    int updateByPrimaryKey(LotteryRecord record);
}