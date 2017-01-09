package com.busap.vcs.restadmin.task;

import com.busap.vcs.service.RuserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by busap on 2015/11/12.
 */
@Service("userPopularityRankingList")
public class UserPopularityRankingListImpl implements UserPopularityRankingList {

    @Resource
    private RuserService ruserService;

    @Override
    public void queryDayUserPopularityToRedis(){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("count",50);
        params.put("type","day");
        ruserService.queryUserPopularityToRedis(params);
    }

    @Override
    public void queryWeekUserPopularityToRedis(){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("count",50);
        params.put("type","week");
        ruserService.queryUserPopularityToRedis(params);
    }

    @Override
    public void queryMonthUserPopularityToRedis(){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("count",50);
        params.put("type","month");
        ruserService.queryUserPopularityToRedis(params);
    }

    @Override
    public void queryYearUserPopularityToRedis(){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("count",50);
        params.put("type","year");
        ruserService.queryUserPopularityToRedis(params);
    }

}


