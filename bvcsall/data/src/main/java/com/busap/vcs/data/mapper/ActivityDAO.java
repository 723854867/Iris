package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.Activity;
import com.busap.vcs.data.model.ActivityDisplay;

import java.util.List;
import java.util.Map;

public interface ActivityDAO {

	 
	public Map<String,Object> getBanner(Long id);
	
	public List<Map<String,Object>> searchActivitys(Map<String,Object> params);
	
	public Integer searchActivitysCount(Map<String,Object> params);
	//活动下视频数
	public Integer activityVideos(Long id);
	//活动参与用户数
	public Integer activityUsers(Long id);
	//活动视频点赞总数
	public Integer activityPraise(Long id);
	//活动视频评论总数
	public Integer activityEvaluations(Long id);

	List<ActivityDisplay> selectActivities(Map<String,Object> params);

/*	Integer selectActivityCount(Map<String,Object> params);*/

	int insert(Activity activity);

	int batchOnline(List<Activity> activityList);

	List<Activity> selectBatchOnline(String[] ids);

	int batchOffline(List<Activity> activityList);

	int deleteActivity(Long id);

	Activity selectByPrimaryKey(Long id);

	int updateSort(Map<String,Object> params);

	Activity selectActivityByOrderNum(Map<String,Object> params);

	Long selectActivityVideoCount(Map<String,Object> params);

	List<Activity> selectActivityList(Map<String,Object> params);
	
	List<Map<String,Object>> getLiveAndNormalActivitiesList(Map<String,Object> params);
	
}
