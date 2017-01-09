package com.busap.vcs.service;

import java.util.List;
import java.util.Map;
import com.busap.vcs.data.entity.*;
import com.busap.vcs.data.model.ActivityVideoDisplay;
import com.busap.vcs.data.model.ExportActivityVideo;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by
 * User: djyin
 * Date: 12/5/13
 * Time: 11:52 AM
 */
public interface ActivityVideoService extends BaseService<ActivityVideo, Long> {
	
	public void deleteByVideoidAndActivityId(Long videoid,Long activityid);
	
	public Long findCountByVideoidAndActivityId(Long videoid,Long activityid);

	public Long findCountByCreatorIdAndActivityId(Long userId,Long activityid);

	List<ActivityVideoDisplay> queryActivityVideos(Map<String,Object> params);

	/*Integer queryActivityVideoCount(Map<String,Object> params);*/

	List<ActivityVideo> selectActivityVideoByActivityId(Map<String,Object> params);

	int updateSort(Map<String,Object> params);

	ActivityVideo selectActivityVideoByOrderNum(Map<String,Object> params);

	ActivityVideo selectByPrimaryKey(Long id);

	Map<String,Object> upSort(Long activityVideoId,Long activityId,ActivityVideo activityVideo) throws Throwable;

	Map<String,Object> downSort(Long activityVideoId,Long activityId,ActivityVideo activityVideo) throws Throwable;

	int deleteInPrimaryKeys(String[] ids) throws Throwable;

	int deleteByPrimaryKey(Long id);

	List<ExportActivityVideo> queryActivityVideosByTime(Map<String,Object> params);

}
