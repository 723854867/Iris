package com.busap.vcs.data.mapper;


import com.busap.vcs.data.entity.LiveActivity;
import com.busap.vcs.data.model.LiveActivityDisplay;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public interface LiveActivityDAO {

    public List<Map<String,Object>> searchLiveActivity(Map<String,Object> params);

    public Integer searchLiveActivityCount(Map<String,Object> params);

    public int insert(LiveActivity liveActivity);

    public List<LiveActivityDisplay> selectActivities(Map<String,Object> params);

    LiveActivity selectLiveActivityById(Long id);

}
