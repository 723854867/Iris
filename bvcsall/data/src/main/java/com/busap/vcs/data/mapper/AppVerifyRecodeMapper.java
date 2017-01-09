package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.AppVerifyRecode;
import com.busap.vcs.data.entity.AppVerifyRecodeExample;
import com.busap.vcs.data.entity.AppVerifyRecodeWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppVerifyRecodeMapper {
    int countByExample(AppVerifyRecodeExample example);

    int deleteByExample(AppVerifyRecodeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AppVerifyRecodeWithBLOBs record);

    int insertSelective(AppVerifyRecodeWithBLOBs record);

    List<AppVerifyRecodeWithBLOBs> selectByExampleWithBLOBs(AppVerifyRecodeExample example);

    List<AppVerifyRecode> selectByExample(AppVerifyRecodeExample example);

    AppVerifyRecodeWithBLOBs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AppVerifyRecodeWithBLOBs record, @Param("example") AppVerifyRecodeExample example);

    int updateByExampleWithBLOBs(@Param("record") AppVerifyRecodeWithBLOBs record, @Param("example") AppVerifyRecodeExample example);

    int updateByExample(@Param("record") AppVerifyRecode record, @Param("example") AppVerifyRecodeExample example);

    int updateByPrimaryKeySelective(AppVerifyRecodeWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(AppVerifyRecodeWithBLOBs record);

    int updateByPrimaryKey(AppVerifyRecode record);
}