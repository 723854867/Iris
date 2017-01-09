package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.data.entity.Activity;
import com.busap.vcs.data.entity.LiveActivity;
import com.busap.vcs.data.entity.Prize;
import com.busap.vcs.data.model.PrizeDetailDisplay;
import com.busap.vcs.service.ActivityService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.LiveActivityService;
import com.busap.vcs.service.PrizeDetailService;
import com.busap.vcs.service.PrizeService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
 
@Controller
@RequestMapping("/activity")
public class ActivityController extends CRUDController<Activity, Long> {

    private Logger logger = LoggerFactory.getLogger(ActivityController.class);
    
    @Resource
    private PrizeService prizeService;

    @Resource
    private PrizeDetailService prizeDetailService;
    
    @Resource(name = "liveActivityService")
	private LiveActivityService liveActivityService;

    @Resource(name="activityService")
    ActivityService activityService; 

    @Resource(name="activityService")
    @Override
    public void setBaseService(BaseService<Activity, Long> baseService) {
        this.baseService = baseService;
    }

    //获取所有活动基本信息
    @RequestMapping("/findAll")
    @ResponseBody
    public RespBody findAll(){   
    	return respBodyWriter.toSuccess(activityService.getAllActivity()); 
    }  
    
    //获取所有活动基本信息(时间倒序)
    @RequestMapping("/findAllByGroupType")
    @ResponseBody
    public RespBody findAllByGroupType(int groupType){   
    	//获得分类,ios做单独处理
    	String platform = this.request.getParameter("osVersion");
    	if (platform == null || "".equals(platform)){
    		platform = this.request.getHeader("osVersion");
    	}
    	if (platform != null && platform.contains("ios")){
    		platform = "ios";
    	}
    	List a=activityService.findAllByGroupType(groupType,platform);
    	//groupType==2的不再有返回个数限制
//    	if(groupType==2&&a!=null&&a.size()>4) {
//    		a=a.subList(0, 4);
//    	}
    	return respBodyWriter.toSuccess(a); 
    }  
    
    @RequestMapping("/getActivityBanner")
    @ResponseBody
    public RespBody getActivityBanner(Long id) {
    	Map<String,Object> map = activityService.getBanner(id);
    	List<Prize> list = prizeService.queryAvaliblePrizes(id);
    	if (list != null && list.size()>0){ //如果该活动有中奖名单，返回中奖名单地址
    		map.put("prizeResultUrl", "/activity/getPrizeResult?activityId="+id);
    	} else {
    		map.put("prizeResultUrl", "");
    	}
        return respBodyWriter.toSuccess(map);
    }
    
    /**
     * 跳转到h5中奖名单页面
     * @return
     */
    @RequestMapping("/getPrizeResult")
	public String getPrizeResult(Long activityId) {
    	List<Prize> list = prizeService.queryAvaliblePrizes(activityId);
    	Prize prize = null;
    	if (list != null && list.size()>0) { //查询中奖榜单信息，返回各奖项等级
    		prize = list.get(0);
    		List<Integer> levelList = prizeDetailService.queryPrizeLevel(prize.getId());
    		this.request.setAttribute("prize", prize);
    		this.request.setAttribute("levelList", levelList);
    		List result = new ArrayList();
    		for (int i=0;i<levelList.size();i++) {
    			List<PrizeDetailDisplay> detailList = prizeDetailService.queryPrizeDetailsByPrizeId(prize.getId(), levelList.get(i), 1, 500);
    			result.add(detailList);
    		}
    		this.request.setAttribute("result", result);
    	}
    	return "html5/default/prizeResult";
	}
    
    /**
     * 根据奖项id，奖项等级获得中奖明细
     * @param prizeId
     * @param prizeLevel
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/getPrizeDetail")
    @ResponseBody
    public RespBody getPrizeDetail(Long prizeId,Integer prizeLevel,int page,int size) {
    	List<PrizeDetailDisplay> prizeList = prizeDetailService.queryPrizeDetailsByPrizeId(prizeId, prizeLevel, page, size);
        return respBodyWriter.toSuccess(prizeList);
    }
    
    @RequestMapping("/getActivitysByName")
    @ResponseBody
    public RespBody getActivitysByName(
    		@RequestParam(value = "page", required = false)  Integer page,
    		@RequestParam(value = "size", required = false)  Integer size,
    		@RequestParam(value = "name", required = false)  String name,
    		@RequestParam(value = "cover", required = false)  String cover,
    		@RequestParam(value = "status", required = false)  Integer status
    		) throws Exception {
    	
//    	name="ߘߘߘߘߘߘߘߘ";
    	
    	if(name==null||name.equals("")) {
    		return null;
    	}
    	
    	if(page==null) {
			page=1;
        }
        if(size==null) {
        	size=10;
        }
        
    	StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("FROM Activity activity   WHERE    1=1  ");
        countJpql.append("SELECT COUNT(*)  FROM  Activity activity    WHERE      1=1    ");
        
        if(name!=null&& !name.equals("")) {
//			jpql.append(" AND convert_utf8_unicode_ci(activity.title) like :name ");
//			countJpql.append(" AND convert_utf8_unicode_ci(activity.title) like :name ");
			
			jpql.append(" AND activity.title like :name ");
			countJpql.append(" AND activity.title like :name ");
			
//			jpql.append(" AND CONVERT(title USING utf8) COLLATE utf8_unicode_ci like '%"+name+"%' ");
//			countJpql.append(" AND CONVERT(title USING utf8) COLLATE utf8_unicode_ci like '%"+name+"%' ");
			
			 
			
			ParameterBean paramBean=new ParameterBean("name","%"+name+"%",null);
			paramList.add(paramBean);
//			this.request.setAttribute("name", name);
		}
        
        if(status!=null) {
			jpql.append(" AND activity.status = :status ");
			countJpql.append(" AND activity.status = :status ");
			
			ParameterBean paramBean=new ParameterBean("status",status,null);
			paramList.add(paramBean);
			this.request.setAttribute("status", status);
		}else {
			jpql.append(" AND activity.status =1 ");
			countJpql.append(" AND activity.status =1 ");
		}
        
        List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("createDate",1,"activity");
        orderByList.add(orderByObject);
        
        if(cover!=null) {
			jpql=new StringBuffer(cover);
			countJpql =new StringBuffer(cover);
			paramList=new ArrayList<ParameterBean>();
			ParameterBean paramBean=new ParameterBean("cover",cover,null);
			orderByObject.setSortProperty("");;
			
			this.request.setAttribute("cover", cover);
		}
        
		
        
        
        List activityList=activityService.getObjectByJpql(jpql, page, size, "activity", paramList, null, orderByList);
        
        

        return respBodyWriter.toSuccess(activityList);
    }

	/**
	 * 获取全部活动列表按权重排序（分页版）
	 * @param page 页码
	 * @param rows 行数
	 * @return
	 */
	@RequestMapping("/getActivitiesList")
	@ResponseBody
	public RespBody getActivitiesList(Integer page,Integer rows){
		//获得分类,ios做单独处理
		String platform = this.request.getParameter("osVersion");
		if (platform == null || "".equals(platform)) {
			platform = this.request.getHeader("osVersion");
		}
		if (platform != null && platform.contains("ios")) {
			platform = "ios";
		} else {
			platform = "android";
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("source", platform);
		params.put("pageStart", (page - 1) * rows);
		params.put("pageSize", rows);
		List<Activity> list = activityService.queryActivityList(params);
		return respBodyWriter.toSuccess(list);
	}
	
	/**
	 * 获取全部活动列表按权重排序（分页版,包括直播活动和普通活动）
	 * @param page 页码
	 * @param rows 行数
	 * @return
	 */
	@RequestMapping("/getLiveAndNormalActivitiesList")
	@ResponseBody
	public RespBody getLiveAndNormalActivitiesList(Integer page,Integer rows){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		//获得分类,ios做单独处理
		String platform = this.request.getParameter("osVersion");
		if (platform == null || "".equals(platform)) {
			platform = this.request.getHeader("osVersion");
		}
		if (platform != null && platform.contains("ios")) {
			platform = "ios";
		} else {
			platform = "android";
		}
		//获得普通活动
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("source", platform);
		params.put("pageStart", (page - 1) * rows);
		params.put("pageSize", rows);
		List<Map<String,Object>> list = activityService.getLiveAndNormalActivitiesList(params);
		if (list != null && list.size() > 0) {
			for (Map<String,Object> map :list) {
				if ("1".equals(String.valueOf(map.get("type")))){  //普通活动
					Map<String,Object> resultMap = new HashMap<String,Object>();
					resultMap.put("isLiveActivity", 0);
					resultMap.put("activityInfo", activityService.find((Long)map.get("id")));
					result.add(resultMap);
				} else {  //直播活动
					Map<String,Object> resultMap = new HashMap<String,Object>();
					resultMap.put("isLiveActivity", 1);
					resultMap.put("activityInfo", liveActivityService.find((Long)map.get("id")));
					result.add(resultMap);
				}
			}
		}
		return respBodyWriter.toSuccess(result);
	}

	
}

