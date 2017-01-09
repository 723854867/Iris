package com.busap.vcs.restadmin.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.solr.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.data.entity.Activity;
import com.busap.vcs.data.entity.Tag;
import com.busap.vcs.data.entity.Task;
import com.busap.vcs.data.entity.OperationLog;
import com.busap.vcs.data.enums.TaskTypeSecondEnum;
import com.busap.vcs.service.ActivityService;
import com.busap.vcs.service.TaskService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.util.DateUtils;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.controller.CRUDForm;

/**
 * 任务
 * 
 * @author zx
 * 
 */
@Controller()
@RequestMapping("task")
public class TaskController extends CRUDController<Task, Long> {

	private static final Logger logger = LoggerFactory
			.getLogger(TaskController.class);

	@Resource(name = "taskService")
	private TaskService taskService;
	
	@Value("${files.localpath}")
	private String basePath;
	

	@Resource(name = "taskService")
	@Override
	public void setBaseService(BaseService<Task, Long> baseService) {
		this.baseService = baseService;
	}
	
	@Resource(name = "activityService")
	private ActivityService activityService;

	
	@RequestMapping("taskList")
	public String taskList(Integer page, Integer size, 
			@RequestParam(value = "groupType", required = false)  Integer groupType) throws Exception {
		

		if(page==null) {
			page=1;
        }
        if(size==null) {
        	size=20;
        }
        
        StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("FROM Task task where task.status=0 ");
        countJpql.append("SELECT COUNT(*) FROM Task task where task.status=0  ");
        
//        if(keyword!=null&& !keyword.equals("")) {
//			jpql.append(" AND video.name like :name ");
//			countJpql.append(" AND video.name like :name ");
//			
//			ParameterBean paramBean=new ParameterBean("name","%"+keyword+"%",null);
//			paramList.add(paramBean);
//			this.request.setAttribute("keyword", keyword);
//		}
        
        
		
		
		
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
		
		OrderByBean orderByObject1=new OrderByBean("typeOne",0,"task");
        orderByList.add(orderByObject1);
        
        OrderByBean orderByObject2=new OrderByBean("weight",0,"task");
        orderByList.add(orderByObject2);
        
        
        
        List taskList=taskService.getObjectByJpql(jpql, page, size, "task", paramList, null, orderByList);
        
        
       
        
        Long totalCount=taskService.getObjectCountByJpql(countJpql, paramList);
        
        Pageable pageable = new PageRequest(page == null ? 1 : page, size == null ? 20 : size,null);
        Page pageInfo = new PageImpl(taskList, pageable, totalCount);
        
        this.request.setAttribute("taskList", taskList);
        
		this.request.setAttribute("taskList", pageInfo.getContent());
		this.request.setAttribute("pageinfo", pageInfo);
		
		
		return "task/taskList";
	}
	
	@RequestMapping(value = "taskListJson")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> taskListJson(Integer page, Integer rows,
												@RequestParam(value = "keyword",required = false) String keyword) throws Exception{
		
		if(page==null) {
			page=1;
        }
        if(rows==null) {
        	rows=20;
        }
        
        StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("FROM Task task where task.status=0 ");
        countJpql.append("SELECT COUNT(*) FROM Task task where task.status=0  ");
        
//        if(keyword!=null&& !keyword.equals("")) {
//			jpql.append(" AND video.name like :name ");
//			countJpql.append(" AND video.name like :name ");
//			
//			ParameterBean paramBean=new ParameterBean("name","%"+keyword+"%",null);
//			paramList.add(paramBean);
//			this.request.setAttribute("keyword", keyword);
//		}
        
        
		
		
		
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
		
		OrderByBean orderByObject1=new OrderByBean("typeOne",0,"task");
        orderByList.add(orderByObject1);
        
        OrderByBean orderByObject2=new OrderByBean("weight",0,"task");
        orderByList.add(orderByObject2);
        
        
        
        List taskList=taskService.getObjectByJpql(jpql, page, rows, "task", paramList, null, orderByList);
        
        
       
        
        Long totalCount=taskService.getObjectCountByJpql(countJpql, paramList);
        
        
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("total",totalCount);
		map.put("rows",taskList);
		return map;
	}
	
	
	@RequestMapping("chooseTaskTemplate")
	public String chooseTaskTemplate(HttpServletRequest req) {
		
		return "task/chooseTaskTemplate";
	}
	
	
	@RequestMapping("showTaskAdd")
	public String showTaskAdd(HttpServletRequest req,
			@RequestParam(value = "taskId", required = false)  Long taskId,
			@RequestParam(value = "typeTwo", required = false)  String typeTwo
			) {
		
		Task task =new Task();
		
		if(taskId!=null){
			task = this.taskService.find(taskId);
		}else {
			task.setTypeTwo(typeTwo);
		}
		
		if(task.getTypeTwo()!=null&&(task.getTypeTwo().equals("41")||task.getTypeTwo().equals("1001"))) {
			List<Activity> activityList = this.activityService.findAll();
			this.request.setAttribute("activites", activityList);
		}
		
		
		req.setAttribute("task", task);
		
		return "task/showTaskAdd";
	}
	
	@RequestMapping(value = {"/taskAdd"}, method = {RequestMethod.POST, RequestMethod.PUT})
	public String taskAdd(Task entity,
			@RequestParam(value = "taskId", required = false)  Long taskId
			) {
//		Task entity=new Task();
//		entity.setTitle(title);
//		entity.setCover(cover);
		
		Task task =entity;
				
		if(entity!=null&&entity.getId()!=null){
			task = this.taskService.find(entity.getId());
			
			task.setDescription(entity.getDescription());
			task.setIntegral(entity.getIntegral());
			task.setModifyDate(new Date());
			task.setNum(entity.getNum());
			task.setPreviousTaskId(entity.getPreviousTaskId());
			task.setTaskValue(entity.getTaskValue());
			task.setWeight(entity.getWeight());
			task.setTitle(entity.getTitle());
			task.setIcon(entity.getIcon());
			
			if(entity.getDeadLineStr()!=null&&!entity.getDeadLineStr().equals("")) {
				task.setDeadLine(DateUtils.parseDate("yyyy-MM-dd HH:mm:ss", entity.getDeadLineStr()));
			}
			
			
			
		}
		
		task.setStatus(0);
        baseService.save(task);
		
        
        return "redirect:taskList";
	}
	
	
	@RequestMapping("updateTaskStatus")
	@ResponseBody
	public String updateTaskStatus(
			@RequestParam(value = "taskId", required = false)  Long taskId,
			@RequestParam(value = "status", required = false)  Integer status,
			 CRUDForm curdForm) {
		
		if(taskId!=null) {
			Task task = this.taskService.find(taskId);
			if(task!=null) {
				task.setStatus(status);
				taskService.save(task);
			}
			
			
		}
		
		
		
		
		return "ok";
	}
	
	@RequestMapping("updateTaskWeight")
	@ResponseBody
	public String updateTaskWeight(
			@RequestParam(value = "taskId", required = false)  Long taskId,
			@RequestParam(value = "weight", required = false)  Long weight,
			 CRUDForm curdForm) {
		
		if(taskId!=null) {
			Task task = this.taskService.find(taskId);
			if(task!=null) {
				task.setWeight(weight);
				taskService.save(task);
			}
			
			
		}
		
		
		
		
		return "ok";
	}
	
	
	@RequestMapping(value = {"/taskIconUpload"}, method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
	public RespBody taskIconUpload(@RequestParam MultipartFile hlsfiles) {

		String fileName = hlsfiles.getOriginalFilename();
		String sufix = fileName.substring(fileName.lastIndexOf("."));
    	String relPath = File.separator+"taskIcon"+File.separator+DateFormatUtils.format(new Date(), "yyyy-MM-dd")+File.separator;
        String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + sufix;

		//上传
//    	String relPath = File.separator+"activitycover"+File.separator+DateFormatUtils.format(new Date(), "yyyy-MM-dd")+File.separator;
//        String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_")+hlsfiles.getOriginalFilename();
        String relUrl = "";
        try {
        	FileUtils.copyInputStreamToFile(hlsfiles.getInputStream(), new File(basePath+relPath, originalFilename));
            relUrl = relPath+originalFilename;
        } catch (IOException e) {
        	logger.error("文件[" + originalFilename + "]上传失败",e);
        }
        return respBodyWriter.toSuccess(relUrl);
	}
}
