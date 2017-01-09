package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.IntegralConsume;
import com.busap.vcs.data.entity.IntegralConsumeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IntegralConsumeMapper {
    int countByExample(IntegralConsumeExample example);

    int deleteByExample(IntegralConsumeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(IntegralConsume record);

    int insertSelective(IntegralConsume record);

    List<IntegralConsume> selectByExample(IntegralConsumeExample example);

    IntegralConsume selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") IntegralConsume record, @Param("example") IntegralConsumeExample example);

    int updateByExample(@Param("record") IntegralConsume record, @Param("example") IntegralConsumeExample example);

    int updateByPrimaryKeySelective(IntegralConsume record);

    int updateByPrimaryKey(IntegralConsume record);
}