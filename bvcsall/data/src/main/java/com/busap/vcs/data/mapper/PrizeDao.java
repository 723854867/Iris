package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Prize;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.model.ActivityVideoUser;

/**
 * Created by huoshanwei on 2015/10/23.
 */
public interface PrizeDao {

    List<Prize> select(Prize prize);
    
    List<Prize> selectAvalible(Long activityId);

    int insert(Prize prize);

    int update(Prize prize);

    Prize selectByPrimaryKey(Long id);

    int deleteByPrimaryKey(Long id);

    List<ActivityVideoUser> selectActivityVideoUsers(Map<String,Object> params);

}
