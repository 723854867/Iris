package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.IdentifyInfo;
import com.busap.vcs.data.entity.IdentifyInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IdentifyInfoMapper {
    int countByExample(IdentifyInfoExample example);

    int deleteByExample(IdentifyInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(IdentifyInfo record);

    int insertSelective(IdentifyInfo record);

    List<IdentifyInfo> selectByExample(IdentifyInfoExample example);

    IdentifyInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") IdentifyInfo record, @Param("example") IdentifyInfoExample example);

    int updateByExample(@Param("record") IdentifyInfo record, @Param("example") IdentifyInfoExample example);

    int updateByPrimaryKeySelective(IdentifyInfo record);

    int updateByPrimaryKey(IdentifyInfo record);
}