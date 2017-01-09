package com.busap.vcs.service.impl;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.Activity;
import com.busap.vcs.data.mapper.ActivityVideoDao;
import com.busap.vcs.data.model.ActivityVideoDisplay;
import com.busap.vcs.data.model.ExportActivityVideo;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.ActivityVideo;
import com.busap.vcs.data.repository.ActivityRepository;
import com.busap.vcs.data.repository.ActivityVideoRepository;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.service.ActivityVideoService;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("activityVideoService")
public class ActivityVideoServiceImpl extends BaseServiceImpl<ActivityVideo, Long> implements ActivityVideoService {
	
    @Resource(name = "activityRepository")
    private ActivityRepository activityRepository;
     
    @Resource(name = "activityVideoRepository")
    private ActivityVideoRepository activityVideoRepository;

    @Resource
    private ActivityVideoDao activityVideoDao;

    @Resource(name = "activityRepository")
    @Override
    public void setBaseRepository(BaseRepository<ActivityVideo, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }  
    

    public void deleteByVideoidAndActivityId(Long videoid,Long activityid){
    	activityVideoRepository.deleteByVideoidAndActivityId(videoid, activityid);
    }

    public Long findCountByVideoidAndActivityId(Long videoid,Long activityid){
    	return activityVideoRepository.findCountByVideoidAndActivityId(videoid, activityid);
    }

    public Long findCountByCreatorIdAndActivityId(Long userId,Long activityid){
        return activityVideoRepository.findCountByCreatorIdAndActivityId(userId, activityid);
    }

    @Override
    public List<ActivityVideoDisplay> queryActivityVideos(Map<String,Object> params){
        return activityVideoDao.selectActivityVideos(params);
    }

/*    @Override
    public Integer queryActivityVideoCount(Map<String,Object> params){
        return activityVideoDao.selectActivityVideoCount(params);
    }*/

    @Override
    public List<ActivityVideo> selectActivityVideoByActivityId(Map<String,Object> params){
        return activityVideoDao.selectActivityVideoByActivityId(params);
    }

    @Override
    public int updateSort(Map<String,Object> params){
        return activityVideoDao.updateSort(params);
    }

    @Override
    public ActivityVideo selectActivityVideoByOrderNum(Map<String,Object> params){
        return activityVideoDao.selectActivityVideoByOrderNum(params);
    }

    @Override
    public ActivityVideo selectByPrimaryKey(Long id){
        return activityVideoDao.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public Map<String,Object> upSort(Long activityVideoId,Long activityId,ActivityVideo activityVideo) throws Throwable{
        Map<String,Object> map = new HashMap<String, Object>();
        Map<String,Object> ascParams = new HashMap<String, Object>();
        ascParams.put("ASC","ASC");
        ascParams.put("orderNum",activityVideo.getOrderNum());
        ascParams.put("activityId", activityId);
        ActivityVideo ascActivityVideo = activityVideoDao.selectActivityVideoByOrderNum(ascParams);//上移活动信息
        if (ascActivityVideo == null){
            map.put("resultCode","warningAsc");
            map.put("resultMessage","不能再上移了哦！");
            return map;
        }
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("id",activityVideoId);
        params.put("orderNum",ascActivityVideo.getOrderNum());
        int res = activityVideoDao.updateSort(params);
        Map<String,Object> param = new HashMap<String, Object>();
        param.put("id",ascActivityVideo.getId());
        param.put("orderNum",activityVideo.getOrderNum());
        int ret = activityVideoDao.updateSort(param);
        if(res > 0 && ret > 0){
            map.put("resultCode","ok");
            map.put("resultMessage","排序成功！");
        }else{
            map.put("resultCode","error");
            map.put("resultMessage", "排序失败！");
        }
        return map;
    }

    @Override
    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public Map<String,Object> downSort(Long activityVideoId,Long activityId,ActivityVideo activityVideo) throws Throwable{
        Map<String,Object> map = new HashMap<String, Object>();
        Map<String,Object> descParams = new HashMap<String, Object>();
        descParams.put("DESC","DESC");
        descParams.put("orderNum",activityVideo.getOrderNum());
        descParams.put("activityId",activityId);
        ActivityVideo descActivityVideo = activityVideoDao.selectActivityVideoByOrderNum(descParams);//下移活动信息
        if(descActivityVideo == null){
            map.put("resultCode","warningDesc");
            map.put("resultMessage","不能再下移了哦！");
            return map;
        }
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("id",activityVideoId);
        params.put("orderNum",descActivityVideo.getOrderNum());
        int res = activityVideoDao.updateSort(params);
        Map<String,Object> param = new HashMap<String, Object>();
        param.put("id",descActivityVideo.getId());
        param.put("orderNum",activityVideo.getOrderNum());
        int ret = activityVideoDao.updateSort(param);
        if(res > 0 && ret > 0){
            map.put("resultCode","ok");
            map.put("resultMessage","排序成功！");
        }else{
            map.put("resultCode","error");
            map.put("resultMessage","排序失败！");
        }
        return map;
    }

    @Override
    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public int deleteInPrimaryKeys(String[] ids) throws Throwable{
        return activityVideoDao.deleteInPrimaryKeys(ids);
    }

    @Override
    public int deleteByPrimaryKey(Long id){
        return activityVideoDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<ExportActivityVideo> queryActivityVideosByTime(Map<String,Object> params){
        return activityVideoDao.selectActivityVideosByTime(params);
    }

}
