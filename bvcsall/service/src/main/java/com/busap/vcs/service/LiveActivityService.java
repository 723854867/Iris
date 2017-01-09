package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.LiveActivity;
import com.busap.vcs.data.model.LiveActivityDisplay;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * Created by
 * User: dmsong
 * Date: 6/8/15
 * Time: 11:52 AM
 */
public interface LiveActivityService extends BaseService<LiveActivity, Long> {

	public List<LiveActivity> findMyLiveActivity(Long uid, int findAll);

    public Page queryLiveActivity(Map<String, Object> params);

    public Integer queryLiveActivityCount(Map<String,Object> params);

    public int insert(LiveActivity liveActivity);

    public List<LiveActivityDisplay> queryLiveActivities(Map<String,Object> params);

    public void save(String[] gifts, Long id, String title, String description, Integer status, Integer type,
                     String cover, int showCountOfTop, String startTime, String endTime, Long uid, Integer orderNum);

    public void deleteById(Long id);
    
    public List<LiveActivity> getLiveActivityList();

    LiveActivity queryLiveActivityById(Long id);
}
