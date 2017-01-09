package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import com.busap.vcs.data.entity.*;
import com.busap.vcs.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;


/**
 * 定制活动
 *
 * @author zx
 *
 */
@Controller()
@RequestMapping("customizeActivity")
public class CustomizeActivityController extends CRUDController<CustomizeActivity, Long> {

	private static final Logger logger = LoggerFactory
			.getLogger(CustomizeActivityController.class);
	
	@Resource(name = "customizeActivityService")
	private CustomizeActivityService customizeActivityService;

	@Resource(name = "activityService")
	private ActivityService activityService;
	
	@Resource(name = "templateService")
	private TemplateService templateService;


	@Value("${files.localpath}")
	private String basePath;

	@Value("${uploadfile_remote_url}")
	private String uploadfile_remote_url;

	@Resource(name = "customizeActivityService")
	@Override
	public void setBaseService(BaseService<CustomizeActivity, Long> baseService) {
		this.baseService = baseService;
	}

	//获取所有活动基本信息
    @RequestMapping("/findCurrentCustomizeActivity")
    @ResponseBody
    public RespBody findCurrentCustomizeActivity() throws Exception{ 
    	StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("FROM CustomizeActivity ca where 1=1  AND ca.status=1 ");
        countJpql.append("SELECT COUNT(*) FROM CustomizeActivity ca where 1=1   AND ca.status=1 ");
        
        jpql.append(" AND ca.timeStart <= :currentTime and  ca.timeEnd >= :currentTime ");
        countJpql.append(" AND ca.timeStart <= :currentTime and  ca.timeEnd >= :currentTime ");
        
        Date currentTime=new Date();
		
		ParameterBean paramBean = new ParameterBean("currentTime",currentTime , null);
		paramList.add(paramBean);
		
        
        
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
		
		
		
        
        
        
        
        List caList=customizeActivityService.getObjectByJpql(jpql, 1, 10, "ca", paramList, null, orderByList);
        
        CustomizeActivity ca=null;
        
        if(caList!=null&&caList.size()>0) {
        	ca=(CustomizeActivity) caList.get(0);
        	
        	if(ca.getActivityId()!=null&&!ca.getActivityId().equals("")) {
        		Activity a=activityService.find(Long.valueOf(ca.getActivityId()));
        		ca.setActivity(a);
            	
            }
        	
        	if(ca.getHeadId()!=null&&!ca.getHeadId().equals("")) {
        		Template head=templateService.find(Long.valueOf(ca.getHeadId()));
        		ca.setHead(head);
        	}
        	
        	if(ca.getMvId()!=null&&!ca.getMvId().equals("")) {
        		Template mv=templateService.find(Long.valueOf(ca.getMvId()));
        		ca.setMv(mv);
        	}
		}
        
        
    	return respBodyWriter.toSuccess(ca);
    } 
	

}
