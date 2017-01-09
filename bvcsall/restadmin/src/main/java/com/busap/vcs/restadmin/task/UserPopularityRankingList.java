package com.busap.vcs.restadmin.task;

/**
 * Created by busap on 2015/11/12.
 */
public interface UserPopularityRankingList {

    void queryDayUserPopularityToRedis();

    void queryWeekUserPopularityToRedis();

    void queryMonthUserPopularityToRedis();

    void queryYearUserPopularityToRedis();

}
