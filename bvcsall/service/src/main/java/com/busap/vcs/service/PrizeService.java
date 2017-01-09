package com.busap.vcs.service;

import com.busap.vcs.data.entity.Prize;
import com.busap.vcs.data.model.ActivityVideoUser;

import java.util.List;
import java.util.Map;

/**
 * Created by
 * User: huoshanwei
 * Date: 15/10/23
 * Time: 15:52
 */
public interface PrizeService extends BaseService<Prize, Long> {

    List<Prize> queryPrizes(Prize prize);
    
    List<Prize> queryAvaliblePrizes(Long activityId);

    int insertPrize(Prize prize);

    int updatePrize(Prize prize);

    Prize queryPrize(Long id);

    int deletePrizeById(Long id);

    List<ActivityVideoUser> queryActivityVideoUsers(Map<String,Object> params);

}
