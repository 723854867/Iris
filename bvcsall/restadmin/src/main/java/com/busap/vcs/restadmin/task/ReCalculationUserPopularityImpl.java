package com.busap.vcs.restadmin.task;

import com.busap.vcs.service.RuserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by busap on 2015/11/12.
 */
@Service("reCalculationUserPopularity")
public class ReCalculationUserPopularityImpl implements ReCalculationUserPopularity {

    @Resource
    private RuserService ruserService;

    @Override
    public void reCalculationDayUserPopularity(){
        ruserService.reCalculateUserPopularity("day");
    }

    @Override
    public void reCalculationWeekUserPopularity(){
        ruserService.reCalculateUserPopularity("week");
    }

    @Override
    public void reCalculationMonthUserPopularity(){
        ruserService.reCalculateUserPopularity("month");
    }

    @Override
    public void reCalculationYearUserPopularity(){
        ruserService.reCalculateUserPopularity("year");
    }

}
