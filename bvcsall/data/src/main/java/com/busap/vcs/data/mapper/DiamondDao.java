package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.Diamond;
import com.busap.vcs.data.model.DiamondDisplay;

import java.util.List;

public interface DiamondDao {
    int deleteByPrimaryKey(Long id);

    int insert(Diamond record);

    Diamond selectByPrimaryKey(Long id);

    List<Diamond> selectAll(DiamondDisplay diamondDisplay);

    int updateByPrimaryKey(Diamond record);
}