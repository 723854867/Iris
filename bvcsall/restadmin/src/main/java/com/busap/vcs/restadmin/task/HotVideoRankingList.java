package com.busap.vcs.restadmin.task;


/**
 * Created by busap on 2015/11/12.
 */
public interface HotVideoRankingList {

    void queryDayHotVideosToRedis();

    void queryWeekHotVideosToRedis();

    void queryMonthHotVideosToRedis();

    void queryYearHotVideosToRedis();
}
