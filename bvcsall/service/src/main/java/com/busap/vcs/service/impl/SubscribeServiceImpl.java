package com.busap.vcs.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Activity;
import com.busap.vcs.data.entity.Subscribe;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.mapper.SubscribeDAO;
import com.busap.vcs.service.SubscribeService;
import com.busap.vcs.service.VideoService;

@Service("subscribeService")
public class SubscribeServiceImpl extends BaseServiceImpl<Subscribe, Long> implements SubscribeService {
	
    
    @Autowired
    private SubscribeDAO subscribeDao;
    
    @Resource(name="videoService")
    private VideoService videoService;

	@Override
	public Integer isSubscribed(long uid, Long activityId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("uid", uid);
		params.put("activityId", activityId);
		return subscribeDao.isSubscribed(params);
	}

	@Override
	public Integer subscribe(long uid, Long activityId,String dateFrom) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("uid", uid);
		params.put("activityId", activityId);
		params.put("createAt", new Date());
		params.put("dateFrom", dateFrom);
		return subscribeDao.insert(params);
	}

	@Override
	public Integer cancelSubscribe(long uid, Long activityId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("uid", uid);
		params.put("activityId", activityId);
		subscribeDao.delete(params);
		return subscribeDao.isSubscribed(params);
	}

	@Override
	public List<Activity> getMyActivityList(long uid) {
		return subscribeDao.getMyActivityList(uid);
	}

	@Override
	public List<Video> getMyActivityVideoList(long uid, Long timestamp, Integer count) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("uid", uid);
		params.put("timestamp", (timestamp==null||timestamp.longValue()==0)?null:new Date(timestamp));
		params.put("count", count);
		List<Video> list = subscribeDao.getMyActivityVideoList(params);
		for(Video video:list){ 
    		video.setPraise(videoService.praised(uid, video.getId()));
    		video.setFavorite(videoService.favorited(uid, video.getId()));
    	}
		return list;
	}

}
