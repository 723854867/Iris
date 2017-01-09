package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Activity;
import com.busap.vcs.data.entity.Video;


public interface SubscribeDAO {

	public int isSubscribed(Map<String, Object> params);

	public int insert(Map<String, Object> params);

	public int delete(Map<String, Object> params);

	public List<Activity> getMyActivityList(long uid);

	public List<Video> getMyActivityVideoList(Map<String, Object> params);
}
