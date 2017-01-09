package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.ActivityVideo;
import com.busap.vcs.data.model.ActivityVideoDisplay;
import com.busap.vcs.data.model.ExportActivityVideo;

import java.util.List;
import java.util.Map;

/**
 * Created by huoshanwei on 2015/9/23.
 */
public interface ActivityVideoDao {

    List<ActivityVideoDisplay> selectActivityVideos(Map<String,Object> params);

    /*Integer selectActivityVideoCount(Map<String,Object> params);*/

    List<ActivityVideo> selectActivityVideoByActivityId(Map<String,Object> params);

    int updateSort(Map<String,Object> params);

    ActivityVideo selectActivityVideoByOrderNum(Map<String,Object> params);

    ActivityVideo selectByPrimaryKey(Long id);

    int deleteInPrimaryKeys(String[] ids);

    int deleteByPrimaryKey(Long id);

    List<ExportActivityVideo> selectActivityVideosByTime(Map<String,Object> params);

}
