package com.busap.vcs.service;

import com.busap.vcs.data.entity.Diamond;
import com.busap.vcs.data.model.DiamondDisplay;

import java.util.List;

public interface DiamondService {
    int deleteByPrimaryKey(Long id);

    int insert(Diamond record);

    Diamond selectByPrimaryKey(Long id);

    List<Diamond> selectAll(DiamondDisplay diamondDisplay);

    int updateByPrimaryKey(Diamond record);
}