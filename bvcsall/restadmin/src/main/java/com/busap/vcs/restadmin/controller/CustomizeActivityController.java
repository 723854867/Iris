package com.busap.vcs.restadmin.controller;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.busap.vcs.data.entity.*;
import com.busap.vcs.data.model.ActivityDisplay;
import com.busap.vcs.data.model.ActivityVideoDisplay;
import com.busap.vcs.data.model.EvaluationDisplay;
import com.busap.vcs.data.model.ExportActivityVideo;
import com.busap.vcs.restadmin.utils.CommonUtils;
import com.busap.vcs.restadmin.utils.EnableFunction;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.*;
import com.busap.vcs.service.utils.ExcelUtils;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.PagingContextHolder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.busap.vcs.base.Filter;
import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.OrderByObject;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.data.enums.VideoStatus;
import com.busap.vcs.service.impl.SolrWoPaiTagService;
import com.busap.vcs.util.DateUtils;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.controller.CRUDForm;

import org.springframework.web.servlet.ModelAndView;


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

	@Value("${uploadpic_url_prefix}")
	private String uploadpic_url_prefix;

	@Value("${video_play_url_prefix}")
	private String video_play_url_prefix;

	@Value("${inner.actives}")
	private String innerActives;

	@Value("${solr.zookeeper.host}")
	private String solrZKHost;

	@Value("${solr.default.collection}")
	private String solrDefaultCllection;


	@EnableFunction("定制活动管理,查看定制活动管理信息")
	@RequestMapping("customizeActivityList")
	public String customizeActivityList(){
		return "customizeActivity/customizeActivityList";
	}
	
	@RequestMapping("queryCustomizeActivityList")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> queryCustomizeActivityList(
												@RequestParam(value = "page", required = false)  Integer page,
												@RequestParam(value = "rows", required = false)  Integer rows,
												@RequestParam(value = "title", required = false)  String title,
												@RequestParam(value = "status", required = false) Integer status,
												@RequestParam(value = "timeStart", required = false) String timeStart,
												@RequestParam(value = "timeEnd", required = false) String timeEnd
												) throws Exception{
		if(page==null) {
			page=1;
        }
        if(rows==null) {
        	rows=20;
        }
        
        StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("FROM CustomizeActivity ca where 1=1 ");
        countJpql.append("SELECT COUNT(*) FROM CustomizeActivity ca where 1=1  ");
        
        if(status!=null) {
       	 	jpql.append(" AND ca.status=:status ");
            countJpql.append(" AND  ca.status=:status  ");
            ParameterBean paramBean=new ParameterBean("status",status,null);
			paramList.add(paramBean);
       }else {
//    	    jpql.append(" AND  lc.status=0 ");
//            countJpql.append(" AND  lc.status=0  ");
       }
        
        if(title!=null&&!title.equals("")) {
        	 jpql.append(" AND  ca.title= :title ");
             countJpql.append(" AND  ca.title= :title ");
             ParameterBean paramBean=new ParameterBean("title",title,null);
 			 paramList.add(paramBean);
        }
        
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
		
//		OrderByBean orderByObject1=new OrderByBean("typeOne",0,"lc");
//        orderByList.add(orderByObject1);
        
        
        
        
        List caList=customizeActivityService.getObjectByJpql(jpql, page, rows, "ca", paramList, null, orderByList);
        
        
       
        
        Long totalCount=customizeActivityService.getObjectCountByJpql(countJpql, paramList);
        
        
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("total",totalCount);
		map.put("rows",caList);
		return map;
	}


	@RequestMapping("showCustomizeActivityAdd")
	public String showCustomizeActivityAdd(HttpServletRequest req, @RequestParam(value = "caId", required = false)  Long caId) {
		if(caId!=null){
			CustomizeActivity ca = customizeActivityService.find(caId);
			if(ca.getHeadId()!=null&&!ca.getHeadId().equals("")) {
				ca.setHead(templateService.find(Long.valueOf(ca.getHeadId()))); 
			}
			if(ca.getMvId()!=null&&!ca.getMvId().equals("")) {
				ca.setMv(templateService.find(Long.valueOf(ca.getMvId()))); 
			}
			if(ca.getActivityId()!=null&&!ca.getActivityId().equals("")) {
				ca.setActivity(activityService.find(Long.valueOf(ca.getActivityId())));
			}
			
			
			req.setAttribute("activity", ca);
		}
		req.setAttribute("uploadfile_remote_url", uploadfile_remote_url);
		return "customizeActivity/showCustomizeActivityAdd";
	}


	@EnableFunction("活动管理,添加/编辑活动")
	@RequestMapping(value = {"/customizeActivityAdd"}, method = {RequestMethod.POST, RequestMethod.PUT})
	public String customizeActivityAdd(CustomizeActivity entity
			) {

		CustomizeActivity ca =entity;

		if(entity!=null&&entity.getId()!=null){
			ca = this.customizeActivityService.find(entity.getId());
			
			
			ca.setTitle(entity.getTitle());
			
			ca.setType(entity.getType());
			
			ca.setUrl(entity.getUrl());
			
			ca.setActivityId(entity.getActivityId());
			
			ca.setActivityDes(entity.getActivityDes());
			
			ca.setShareDes(entity.getShareDes());
			
			ca.setHeadId(entity.getHeadId());
			
			ca.setMvId(entity.getMvId());
			
			ca.setStatus(entity.getStatus());
			
			if(entity.getTimeStartStr()!=null&&!entity.getTimeStartStr().equals("")){
				ca.setTimeStart(DateUtils.parseDate("yyyy-MM-dd", entity.getTimeStartStr()));
			}
			
			if(entity.getTimeEndStr()!=null&&!entity.getTimeEndStr().equals("")){
				ca.setTimeEnd(DateUtils.parseDate("yyyy-MM-dd HH:mm:ss", entity.getTimeEndStr()+" 23:59:59"));
			}
			
			
			if(entity.getActPicIos()!=null&&!entity.getActPicIos().equals("")){
				ca.setActPicIos(entity.getActPicIos());
			}
			if(entity.getActIconIos()!=null&&!entity.getActIconIos().equals("")){
				ca.setActIconIos(entity.getActIconIos());
			}
			if(entity.getLoadPicIos()!=null&&!entity.getLoadPicIos().equals("")){
				ca.setLoadPicIos(entity.getLoadPicIos());
			}
			if(entity.getLoadFailPicIos()!=null&&!entity.getLoadFailPicIos().equals("")){
				ca.setLoadFailPicIos(entity.getLoadFailPicIos());
			}
			if(entity.getBagIos()!=null&&!entity.getBagIos().equals("")){
				ca.setBagIos(entity.getBagIos());
			}
			
			if(entity.getButtonIconIos()!=null&&!entity.getButtonIconIos().equals("")){
				ca.setButtonIconIos(entity.getButtonIconIos());
			}
			
			
			if(entity.getActPicAndroid()!=null&&!entity.getActPicAndroid().equals("")){
				ca.setActPicAndroid(entity.getActPicAndroid());
			}
			if(entity.getActIconAndroid()!=null&&!entity.getActIconAndroid().equals("")){
				ca.setActIconAndroid(entity.getActIconAndroid());
			}
			if(entity.getLoadPicAndroid()!=null&&!entity.getLoadPicAndroid().equals("")){
				ca.setLoadPicAndroid(entity.getLoadPicAndroid());
			}
			if(entity.getLoadFailPicAndroid()!=null&&!entity.getLoadFailPicAndroid().equals("")){
				ca.setLoadFailPicAndroid(entity.getLoadFailPicAndroid());
			}
			if(entity.getBagAndroid()!=null&&!entity.getBagAndroid().equals("")){
				ca.setBagAndroid(entity.getBagAndroid());
			}
			
			if(entity.getButtonIconAndroid()!=null&&!entity.getButtonIconAndroid().equals("")){
				ca.setButtonIconAndroid(entity.getButtonIconAndroid());
			}
			
			
		}else {
			if(entity.getTimeStartStr()!=null&&!entity.getTimeStartStr().equals("")){
				ca.setTimeStart(DateUtils.parseDate("yyyy-MM-dd", entity.getTimeStartStr()));
			}
			
			if(entity.getTimeEndStr()!=null&&!entity.getTimeEndStr().equals("")){
				ca.setTimeEnd(DateUtils.parseDate("yyyy-MM-dd HH:mm:ss", entity.getTimeEndStr()+" 23:59:59"));
			}
		}
		
		
        baseService.save(ca);


        return "redirect:customizeActivityList";
	}

	//@EnableFunction(permissionId+",加入活动")
	@RequestMapping("chooseActivity")
	@ResponseBody
	public RespBody chooseActivity(Long videoId,Long activityId){
//		if(videoId!=null&&activityId!=null){
//        	ActivityVideo av=new ActivityVideo();
//			av.setActivityid(activityId);
//			av.setVideoid(videoId);
//			Long c=activityVideoService.findCountByVideoidAndActivityId( videoId, activityId);
//			if(c==0){
//				activityVideoService.save(av);
//			}
//        }
		return this.respBodyWriter.toSuccess();
	}





	@RequestMapping("updateOrderNum")
	@ResponseBody
	public String updateOrderNum(Long activeId,Integer orderNum){
		long count=activityService.findByOrderNum(orderNum);
		if(count>0){
			return "exist";
		}
		activityService.updateOrderNum( activeId, orderNum);
		return "ok";
	}


	@EnableFunction("活动管理,更新活动状态信息【上线下线】")
	@RequestMapping("updateActiveStatus")
	@ResponseBody
	public String updateActiveStatus(Long activeId,Integer status){
		
		CustomizeActivity ca=baseService.find(activeId);
		ca.setStatus(status);
		baseService.save(ca);
		return "ok";
	}
	
	@RequestMapping(value = {"/fileUpload"}, method = {RequestMethod.POST, RequestMethod.PUT})
	@ResponseBody
	public RespBody activityUploadFile(@RequestParam MultipartFile file,String fileType) {

		String fileName = file.getOriginalFilename();
		String sufix = fileName.substring(fileName.lastIndexOf("."));
		String relPath = File.separator + "costomizeActivity" +File.separator + fileType + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
		String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + sufix;

		//上传
		String relUrl = "";
		try {
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(basePath+relPath, originalFilename));
			relUrl = relPath + originalFilename;
		} catch (IOException e) {
			logger.error("文件[" + originalFilename + "]上传失败",e);
		}
		return respBodyWriter.toSuccess(relUrl);
	}


}
