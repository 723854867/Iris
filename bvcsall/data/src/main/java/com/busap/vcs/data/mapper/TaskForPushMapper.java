package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.TaskForPush;
import com.busap.vcs.data.entity.TaskForPushExample;
import java.util.List;

import com.busap.vcs.operate.MyBatis.OperateMyBatis;
import org.apache.ibatis.annotations.Param;

public interface TaskForPushMapper {
    int countByExample(TaskForPushExample example);

    int deleteByExample(TaskForPushExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TaskForPush record);

    int insertSelective(TaskForPush record);

    List<TaskForPush> selectByExample(TaskForPushExample example);

    TaskForPush selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TaskForPush record, @Param("example") TaskForPushExample example);

    int updateByExample(@Param("record") TaskForPush record, @Param("example") TaskForPushExample example);

    int updateByPrimaryKeySelective(TaskForPush record);

    int updateByPrimaryKey(TaskForPush record);
}