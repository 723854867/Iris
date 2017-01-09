package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Gift;
import com.busap.vcs.data.model.GiftDisplay;

public interface GiftDao {
    int deleteByPrimaryKey(Long id);

    int insert(Gift record);

    Gift selectByPrimaryKey(Long id);

    List<Gift> selectAll(GiftDisplay giftDisplay);

    int updateByPrimaryKey(Gift record);
    
    Integer sendGift(Map<String,Object> params);
    
    public List<Map<String,Object>> reCount(Map<String,Object> params);
}