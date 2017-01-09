package com.busap.vcs.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.busap.vcs.base.Filter;
import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.data.entity.Activity;
import com.busap.vcs.data.entity.ActivityVideo;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.mapper.ActivityDAO;
import com.busap.vcs.data.model.ActivityDisplay;
import com.busap.vcs.data.repository.ActivityRepository;
import com.busap.vcs.data.repository.ActivityVideoRepository;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.service.ActivityService;
import com.busap.vcs.service.IntegralService;
import com.busap.vcs.service.JedisService;

@Service("activityService")
public class ActivityServiceImpl extends BaseServiceImpl<Activity, Long> implements ActivityService {
	
    @Resource(name = "activityRepository")
    private ActivityRepository activityRepository;
     
    @Resource(name = "activityVideoRepository")
    private ActivityVideoRepository activityVideoRepository;
    
    @Autowired
    ActivityDAO activityDAO;
    
    @Autowired
    JedisService jedisService;

	@Autowired
	IntegralService integralService;
    
    @Resource(name = "activityRepository")
    @Override
    public void setBaseRepository(BaseRepository<Activity, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }  
    
    public List<Activity> getAllActivity(){ 
		Sort sort=new Sort(Direction.ASC,"order_num");
		List<Filter> ls = new ArrayList<Filter>(1);
		ls.add(new Filter("status", "1"));
		return activityRepository.findAll(ls,sort);
	}
    
    @Override
	public List<Activity> findAllByGroupType(int groupType,String platform) {
    	Sort sort = new Sort(Direction.DESC,"createDate");
		List<Filter> ls = new ArrayList<Filter>(1);
		
		if (groupType == 3) {  //3为查询全部活动，不区分groupType
			if (platform !=null && platform.equals("ios")) { //ios获得首页推荐，单独处理
				ls.add(new Filter("source","ne", "android"));
				sort = new Sort(Direction.ASC,"order_num").and(new Sort(Direction.DESC,"createDate"));
			}
		} else {
			if(groupType == 0||groupType == 2) {
				ls.add(new Filter("status", "1"));
				if (platform !=null && platform.equals("ios") && groupType == 0 ) { //ios获得首页推荐，单独处理
					ls.add(new Filter("source","ne", "android"));
				}
				sort=new Sort(Direction.ASC,"order_num");
			}
			if(groupType == 1) {
				List<Integer> activityList=new ArrayList<Integer>();
				activityList.add(1);
				activityList.add(2);
				ls.add(new Filter("groupType", "in", activityList));
				return activityRepository.findAll(ls,sort);
			}
			ls.add(new Filter("groupType", "eq", groupType));
		}
		
		return activityRepository.findAll(ls,sort);
	}
    
    public List<Long> getActVideoIds(Long actId){ 
    	List<Filter> ls = new ArrayList<Filter>();
		ls.add(new Filter("activityid", ""+actId));
		List<ActivityVideo> l= activityVideoRepository.findAll(ls,null); 
		List<Long> ret = new ArrayList<Long>(); 
		if(l==null||l.size()<=0)
			return ret;
		for(ActivityVideo one:l){ 
			if(one!=null){
				ret.add(one.getVideoid());
			}
		}
		return ret; 
    }
    
    public void saveActVideoRelation(List<Long> actIds,Long videoId){
    	if(actIds==null||actIds.size()<=0)
    		return ;
    	for(Long one:actIds){
    		ActivityVideo av = new ActivityVideo();
    		av.setActivityid(one);
    		av.setVideoid(videoId);
    		activityVideoRepository.save(av);
    	}
    }
    
    public void saveActVideoRelation(List<Long> actIds,Long videoId,Set<String> tags,Ruser u ){
    	
    	
    	Set<Long> activityIdSet=new HashSet<Long>();
		
		List<ActivityVideo> svr=new ArrayList<ActivityVideo>();
		if(actIds!=null&&actIds.size()>0){
			for (Long s : actIds) {
				ActivityVideo sv=new ActivityVideo();
				sv.setVideoid(videoId);
				sv.setActivityid(s);
//				sv.setCreatorId(uploader);
				svr.add(sv);
				activityIdSet.add(s);
			}
		}
		
		
		//获取发现页推荐二级列表
				StringBuffer jpql = new StringBuffer();
		        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
		        
//		        jpql.append("FROM Activity  a    WHERE    a.groupType = 0 and a.status=1  ");
		        
		        jpql.append("FROM Activity  a          ");
		        
//		        jpql.append(" and a.vipActivity is null and a.personalActivity is null  ");
				
				List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
		        OrderByBean orderByObject=new OrderByBean("id",1,"a");
		        orderByList.add(orderByObject);
		        
		        List allActivityList=new ArrayList();
				try {
					allActivityList = this.getObjectByJpql(jpql, 0, 10000, "a", paramList, null, orderByList);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				 List<Activity> indexActivityList=new ArrayList<Activity>();
				 List<Activity> vipActivityList=new ArrayList<Activity>();
				 List<Activity> ruserActivityList=new ArrayList<Activity>();
				
				for (Object object : allActivityList) {
					Activity actTemp=(Activity) object;
					if(  (actTemp.getVips()==null||actTemp.getVips().equals(""))&&(actTemp.getRusers()==null||actTemp.getRusers().equals(""))  ) {
						if(actTemp.getStatus()==1) {
							indexActivityList.add(actTemp);
						}
					}else {
						if(actTemp.getVips()!=null&&!actTemp.getVips().equals("")) {
							vipActivityList.add(actTemp);
						}
						
						if(actTemp.getRusers()!=null&&!actTemp.getRusers().equals("")) {
							ruserActivityList.add(actTemp);
						}
					}
				}
				
				
		if(tags!=null&&tags.size()>0) {
			
			
	        
	        
	        //(原逻辑，已废弃)如果tag与发现页列表项的名称相同，则把该视频自动加入到该推荐项中（如果勾选活动时，已经勾选了该项，则不重复添加）
			//           如果tag与发现页列表项的标签列表相同，则把该视频自动加入到该推荐项中（如果勾选活动时，已经勾选了该项，则不重复添加）
			if(indexActivityList!=null&&indexActivityList.size()>0) {
				for (Object object : indexActivityList) {
					Activity actTemp=(Activity) object;
					if(actTemp.getTags()!=null&&!actTemp.getTags().equals("")) {
						Set<String> result = new HashSet<String>();
				        

				        
				        
						Set<String> activityTagSet =new HashSet<String>();
						String tempTags=actTemp.getTags();
						tempTags=tempTags.replace("#", "");
						String[] activityTagArr=tempTags.split(";");
						activityTagSet.addAll(Arrays.asList(activityTagArr));
						
						result.clear();
				        result.addAll(activityTagSet);
				        result.retainAll(tags);
				        
//						boolean containFlg= activityTagSet.retainAll(tags);
						
						if(result!=null&&result.size()>0) {
							if(activityIdSet.contains(actTemp.getId())) {
								
							}else {
								ActivityVideo sv=new ActivityVideo();
								sv.setVideoid(videoId);
								sv.setActivityid(actTemp.getId());
//								sv.setCreatorId(uploader);
								svr.add(sv);
								
								activityIdSet.add(actTemp.getId());
								
							}
						}
						
					}
					
				}
			}
		}
		
		//增加vip栏目和个人明星栏目，所以原黄V逻辑废弃
		//如果上传者是黄V用户，则把视频自动归类到明星栏目
		
//		if(u.getId()!=null) {
////					uploader=16l;
//			if(u!=null&&u.getVipStat()==2) {
//				String vipActivityId=jedisService.get("YELLOW_VIP_ACTIVITY_ID");
//				if(vipActivityId!=null&&!vipActivityId.equals("")) {
//					ActivityVideo sv=new ActivityVideo();
//					sv.setVideoid(videoId);
//					sv.setActivityid(Long.valueOf(vipActivityId));
//					sv.setCreatorId(u.getId());
//					svr.add(sv);
//					
//					activityIdSet.add(Long.valueOf(vipActivityId));
//				}
//				
//			}
//		}
		
		
		//增加vip栏目和个人明星栏目
		if(u!=null) {
		//	uploader=16l;
			if(u!=null&&u.getVipStat()!=null) {//如果用户存在且是vip用户，则走vip专场加入逻辑
				
				if(vipActivityList!=null&&vipActivityList.size()>0) {//vip活动列表不为空
					for (Activity vipActivity : vipActivityList) {//遍历vip活动列表
						if(activityIdSet.contains(vipActivity.getId())) {//如果活动集合中已经存在该活动，则不再走加入逻辑
							break;
						}
						if(vipActivity.getVips()!=null&&!vipActivity.getVips().equals("")) {//活动vip属性不为空
							String[] vipArr=vipActivity.getVips().split(";");//按照;拆分属性，可能对应多种vip
							if(vipArr!=null&&vipArr.length>0) {//vip列表不空，遍历vip数组
								for(int i=0;i<vipArr.length;i++) {
									if(vipArr[i]!=null&&!vipArr[i].equals("")) {
										Integer vipActId=Integer.valueOf(vipArr[i]);
										if(vipActId.intValue()==u.getVipStat()) {
											ActivityVideo sv=new ActivityVideo();
											sv.setVideoid(videoId);
											sv.setActivityid(vipActivity.getId());
											sv.setCreatorId(u.getId());
											svr.add(sv);
											
											activityIdSet.add(vipActivity.getId());
											
											break;
										}
									}
								}
								
							}
						}
						
					}					
				}
				
				//个人明星栏目
				
				if(u!=null) {//如果用户存在，则走个人明星专场加入逻辑					
					if(ruserActivityList!=null&&ruserActivityList.size()>0) {//个人明星活动列表不为空
						for (Activity ruserActivity : ruserActivityList) {//遍历个人明星活动列表
							if(activityIdSet.contains(ruserActivity.getId())) {//如果活动集合中已经存在该活动，则不再走加入逻辑
								break;
							}
							if(ruserActivity.getRusers()!=null&&!ruserActivity.getRusers().equals("")) {//活动rusers属性不为空
								String[] ruserArr=ruserActivity.getRusers().split(";");//按照;拆分属性，可能对应多种ruser
								if(ruserArr!=null&&ruserArr.length>0) {//rusers列表不空，遍历rusers数组
									for(int i=0;i<ruserArr.length;i++) {
										if(ruserArr[i]!=null&&!ruserArr[i].equals("")) {
											Long ruserId=Long.valueOf(ruserArr[i]);
											if(ruserId.longValue()==u.getId().longValue()) {//当前用户id与活动用户属性用户相等
												ActivityVideo sv=new ActivityVideo();
												sv.setVideoid(videoId);
												sv.setActivityid(ruserActivity.getId());
												sv.setCreatorId(u.getId());
												svr.add(sv);
												
												activityIdSet.add(ruserActivity.getId());
												
												break;
											}
										}
									}
									
								}
							}
							
						}
						
					}
				
				}
				
			}
		}
		
		this.activityVideoRepository.save(svr);

    }

	@Override
	public List<Activity> getAllActivityByGroupType(String groupType) {
		List<Filter> filters=new ArrayList<Filter>();
		Filter filter=new Filter("groupType",Filter.Operator.eq,groupType);
		filters.add(filter);
		Sort sort=new Sort(Direction.DESC,"groupType");
		return activityRepository.findAll(filters, sort);
	}

	@Override
	public List<ActivityVideo> getAllActivityVideoByActivityIds(
			List<Long> activityIds) {
		List<Filter> filters=new ArrayList<Filter>();
		if(CollectionUtils.isNotEmpty(activityIds) && activityIds.size()>0){
			filters.add(Filter.in("activityid", activityIds));
			List<ActivityVideo> activityVideos=activityVideoRepository.findAll(filters, null);
			return activityVideos;
		}
		return null;
	}

	@Override
	public List<ActivityVideo> getAllActivityVideoByActivities(
			List<Activity> activities) {
		if(CollectionUtils.isEmpty(activities)){
			return null;
		}
		List<Long> activityIds=new ArrayList<Long>();
		for(Activity a:activities){
			activityIds.add(a.getId());
		}
		return getAllActivityVideoByActivityIds(activityIds);
	}

	@Override
	public List<Long> getAllVideoIdsByActivities(List<Activity> activities) {
		List<ActivityVideo> activityVideos=getAllActivityVideoByActivities(activities);
		if(CollectionUtils.isEmpty(activityVideos)){
			return null;
		}
		List<Long> videoIds=new ArrayList<Long>();
		for(ActivityVideo av:activityVideos){
			videoIds.add(av.getVideoid());
		}
		return videoIds;
	}

	@Override
	public List<Long> getAllVideoIdsByActivityIds(List<Long> activityIds) {
		List<ActivityVideo> activityVideos=getAllActivityVideoByActivityIds(activityIds);
		if(CollectionUtils.isEmpty(activityVideos)){
			return null;
		}
		List<Long> videoIds=new ArrayList<Long>();
		for(ActivityVideo av:activityVideos){
			videoIds.add(av.getVideoid());
		}
		return videoIds;
	}
	
    public List<Activity> findActivityidByVideoid(Long vid){
    	List<Activity> result=new ArrayList<Activity>();
    	List<ActivityVideo> list = activityVideoRepository.findActivityidByVideoid(vid);
    	for (ActivityVideo activityVideo : list) {
    		Activity act = activityRepository.findOne(activityVideo.getActivityid());
    		if(act!=null){
    			result.add(act);
    		}
		}
    	return result;
    }

	@Override
	public int saveOrderNum(Long id, Integer orderNum) {
		return activityVideoRepository.saveOrderNum(orderNum, id);
	}
	
	@Transactional
	public void updateOrderNum(Long activityId,Integer orderNum) {
		this.activityRepository.updateOrderNum(activityId, orderNum);
	}
	
	public Long findByOrderNum(Integer orderNum){
		return activityRepository.findByOrderNum(orderNum);
	}
	
	public Long findByOrderNum(Integer orderNum,Long id) {
		return activityRepository.findByOrderNum(orderNum,id);
	}
	
	public void updateActiveStatus(Long activityId,Integer status){
		this.activityRepository.updateActiveStatus(activityId, status);
	}
	
	public Map<String,Object> getBanner(Long actid){
		return activityDAO.getBanner(actid); 
	}

	@Override
	public Page searchActivitys(Map<String, Object> params) {
		
		Integer totalCount = activityDAO.searchActivitysCount(params);
		Integer pageNo = (Integer)params.get("pageNo");
		Integer pageSize = (Integer)params.get("pageSize");
		Integer totalPage = (totalCount/pageSize) + (totalCount%pageSize>0?1:0);
		if(pageNo>totalPage&&totalPage!=0){
			pageNo = totalPage;
			params.put("pageNo", pageNo);
			params.put("pageStart", (pageNo-1)*pageSize);
		}
		List<Map<String,Object>> acts = activityDAO.searchActivitys(params);
		
		return new PageImpl(acts,new PageRequest(pageNo-1, pageSize, null),totalCount);
	}

	@Override
	public Map<String, Object> activityDatas(Long id) {
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> majiaParams = new HashMap<String,Object>();
		majiaParams.put("type","majia");
		majiaParams.put("activityId",id);
		Map<String,Object> userParams = new HashMap<String,Object>();
		userParams.put("type","normal");
		userParams.put("activityId",id);
		data.put("majiaVideoCount",activityDAO.selectActivityVideoCount(majiaParams));
		data.put("userVideoCount", activityDAO.selectActivityVideoCount(userParams));
		data.put("userCount", activityDAO.activityUsers(id));
		data.put("praiseCount", activityDAO.activityPraise(id));
		data.put("evaluationCount", activityDAO.activityEvaluations(id));
		return data;
	}

	@Override
	public List<ActivityDisplay> queryActivities(Map<String,Object> params){
		return activityDAO.selectActivities(params);
	}

/*
	@Override
	public Integer queryActivityCount(Map<String,Object> params){
		return activityDAO.selectActivityCount(params);
	}
*/

	@Override
	public int insert(Activity activity){
		return activityDAO.insert(activity);
	}

	@Override
	public int batchOnline(List<Activity> activityList){
		return activityDAO.batchOnline(activityList);
	}

	@Override
	public List<Activity> selectBatchOnline(String[] ids){
		return activityDAO.selectBatchOnline(ids);
	}

	@Override
	public int batchOffline(List<Activity> activityList){
		return activityDAO.batchOffline(activityList);
	}

	@Override
	public int deleteActivity(Long id){
		return activityDAO.deleteActivity(id);
	}

	@Override
	public Activity selectByPrimaryKey(Long id){
		return activityDAO.selectByPrimaryKey(id);
	}

	@Override
	public int updateSort(Map<String,Object> params){
		return activityDAO.updateSort(params);
	}

	@Override
	public Activity selectActivityByOrderNum(Map<String,Object> params){
		return activityDAO.selectActivityByOrderNum(params);
	}

	@Override
	@Transactional(readOnly = true,rollbackFor = Exception.class)
	public boolean activitySort(Long activityId,Integer type) throws Throwable{
		Activity activity = activityDAO.selectByPrimaryKey(activityId);//当前传入ID活动信息
		int res = 0,ret = 0;
		if(type == 1){
			Map<String,Object> maxParams = new HashMap<String, Object>();
			maxParams.put("MAX","MAX");
			maxParams.put("orderNum",activity.getOrder_num());
			Activity maxActivity = activityDAO.selectActivityByOrderNum(maxParams);//置顶活动信息
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("id",activityId);
			params.put("orderNum",maxActivity.getOrder_num()+1);
			int result = activityDAO.updateSort(params);
			if(result>0){
				return true;
			}
		}else if(type == 2){
			Map<String,Object> ascParams = new HashMap<String, Object>();
			ascParams.put("ASC","ASC");
			ascParams.put("orderNum",activity.getOrder_num());
			Activity ascActivity = activityDAO.selectActivityByOrderNum(ascParams);//上移活动信息
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("id",activityId);
			params.put("orderNum",ascActivity.getOrder_num());
			res = activityDAO.updateSort(params);
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("id",ascActivity.getId());
			param.put("orderNum",activity.getOrder_num());
			ret = activityDAO.updateSort(param);
		}else{
			Map<String,Object> descParams = new HashMap<String, Object>();
			descParams.put("DESC","DESC");
			descParams.put("orderNum",activity.getOrder_num());
			Activity descActivity = activityDAO.selectActivityByOrderNum(descParams);//下移活动信息
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("id",activityId);
			params.put("orderNum",descActivity.getOrder_num());
			res = activityDAO.updateSort(params);
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("id",descActivity.getId());
			param.put("orderNum",activity.getOrder_num());
			ret = activityDAO.updateSort(param);
		}
		if(res > 0 && ret > 0){
			return true;
		}else{
			Throwable throwable = new Throwable();
			throwable.printStackTrace();
			throw throwable;
		}
	}

	@Override
	public Long queryActivityVideoCount(Map<String,Object> params){
		return activityDAO.selectActivityVideoCount(params);
	}

	@Override
	public List<Activity> queryActivityList(Map<String,Object> params){
		return activityDAO.selectActivityList(params);
	}

	@Override
	public List<Map<String, Object>> getLiveAndNormalActivitiesList(
			Map<String, Object> params) {
		return activityDAO.getLiveAndNormalActivitiesList(params);
	}

}
