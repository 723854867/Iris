package com.busap.vcs.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.busap.vcs.data.model.ActivityDisplay;
import org.springframework.data.domain.Page;

import com.busap.vcs.data.entity.Activity;
import com.busap.vcs.data.entity.ActivityVideo;
import com.busap.vcs.data.entity.Ruser;

/**
 * Created by
 * User: djyin
 * Date: 12/5/13
 * Time: 11:52 AM
 */
public interface ActivityService extends BaseService<Activity, Long> {

	public List<Activity> getAllActivity();
	public List<Activity> findAllByGroupType(int groupType,String platform);
	
	public List<Long> getActVideoIds(Long actId); 

	public void saveActVideoRelation(List<Long> actIds,Long videoId);
	
	public void saveActVideoRelation(List<Long> actIds,Long videoId,Set<String> tags,Ruser u);
	
	/**
	 * 根据分类类型(发现:0 | 活动:1)获得列表
	 * @author shouchen.shan@10020.cn
	 * @param groupId
	 * @return
	 */
	public List<Activity> getAllActivityByGroupType(String groupType);
	/**
	 * 根据活动ID列表获得所有的活动与视频对应关系列表
	 * @author shouchen.shan@10020.cn
	 * @param activityIds 活动ID列表
	 * @return
	 */
	public List<ActivityVideo> getAllActivityVideoByActivityIds(List<Long> activityIds);
	/**
	 * 根据活动列表获得所有的活动与视频对应关系列表
	 * @author shouchen.shan@10020.cn
	 * @param activities 活动列表
	 * @return
	 */
	public List<ActivityVideo> getAllActivityVideoByActivities(List<Activity> activities);
	/**
	 * 根据活动列表,获得视频ID列表
	 * @author shouchen.shan@10020.cn
	 * @param activities 活动列表
	 * @return
	 */
	public List<Long> getAllVideoIdsByActivities(List<Activity> activities);
	/**
	 * 根据活动ID列表，获得视频ID列表
	 * @author shouchen.shan@10020.cn
	 * @param activityIds 活动ID列表
	 * @return
	 */
	public List<Long> getAllVideoIdsByActivityIds(List<Long> activityIds);
	
	/**
	 * 查找视频所属的活动
	 * @param vid
	 * @return
	 */
	public List<Activity> findActivityidByVideoid(Long vid);
	
	/**
	 * 保存视频排序数字
	 * @param id
	 * @param orderNum
	 * @return
	 */
	public int saveOrderNum(Long id,Integer orderNum);
	
	public void updateOrderNum(Long activityId,Integer orderNum);
	
	public Long findByOrderNum(Integer orderNum);
	
	public Long findByOrderNum(Integer orderNum,Long id);
	
	public void updateActiveStatus(Long activityId,Integer status);
	
	public Map<String,Object> getBanner(Long id);
	
	public Page searchActivitys(Map<String,Object> params);
	// 获取活动下视频数、参与用户数、点赞总数、评论总数
	public Map<String,Object> activityDatas(Long id);

	List<ActivityDisplay> queryActivities(Map<String,Object> params);

	/*Integer queryActivityCount(Map<String,Object> params);*/

	int insert(Activity activity);

	int batchOnline(List<Activity> activityList);

	List<Activity> selectBatchOnline(String[] ids);

	int batchOffline(List<Activity> activityList);

	int deleteActivity(Long id);

	Activity selectByPrimaryKey(Long id);

	int updateSort(Map<String,Object> params);

	Activity selectActivityByOrderNum(Map<String,Object> params);

	boolean activitySort(Long activityId,Integer type) throws Throwable;

	Long queryActivityVideoCount(Map<String,Object> params);

	List<Activity> queryActivityList(Map<String,Object> params);
	
	List<Map<String,Object>> getLiveAndNormalActivitiesList(Map<String,Object> params);

	
}
