package com.busap.vcs.restadmin.task;

import com.busap.vcs.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by busap on 2015/11/12.
 */
@Service("hotVideoRankingList")
public class HotVideoRankingListImpl implements HotVideoRankingList {

    private Logger logger = LoggerFactory.getLogger(HotVideoRankingListImpl.class);

    @Resource
    private VideoService videoService;

    @Override
    public void queryDayHotVideosToRedis(){
        videoService.queryHotVideosToRedis("day");
    }

    @Override
    public void queryWeekHotVideosToRedis(){
        videoService.queryHotVideosToRedis("week");
    }

    @Override
    public void queryMonthHotVideosToRedis(){
        videoService.queryHotVideosToRedis("month");
    }

    @Override
    public void queryYearHotVideosToRedis(){
        videoService.queryHotVideosToRedis("year");
    }

}
