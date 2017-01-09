package com.busap.vcs.restadmin.controller;

/**
 * 评论管理
 * @author dmsong
 * @createdate 2015-1-14
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.service.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.busap.vcs.data.entity.Activity;
import com.busap.vcs.data.entity.SystemMess;
import com.busap.vcs.data.entity.Tag;
import com.busap.vcs.restadmin.quartz.SysmessQuartz;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.controller.CRUDForm;

@Controller()
@RequestMapping("sysmess")
public class SystemMessController extends CRUDController<SystemMess, Long>{
	
	@Resource(name = "systemMessService")
	private SystemMessService systemMessService;
	
	@Resource(name = "activityService")
	private ActivityService activityService;

	@Resource(name = "uploadImageService")
	private UploadImageService uploadImageService;

	@Resource(name = "tagService")
	private TagService tagService;
	
	@Autowired
	private SysmessQuartz sysmessQuartz;
	
	@Resource(name = "systemMessService")
	@Override
	public void setBaseService(BaseService<SystemMess, Long> baseService) {
		this.baseService = baseService;		
	}
	
	@RequestMapping("listmess")
	public String list(){
		return "sysmess/sysmesslist";
	}
	
	@RequestMapping("newmess")
	public String newMess(){
		List<Activity> activityList = this.activityService.findAll();
		List<Tag> tagList = this.tagService.findAll();
		this.request.setAttribute("activites", activityList);
		this.request.setAttribute("tags", tagList);
		return "sysmess/create";
	}
	
	@RequestMapping("updatemess")
	public String updateMess(Long id){
		List<Activity> activityList = this.activityService.findAll();
		List<Tag> tagList = this.tagService.findAll();
		SystemMess mess = this.systemMessService.find(id);
		this.request.setAttribute("activites", activityList);
		this.request.setAttribute("tags", tagList);
		this.request.setAttribute("message", mess);
		return "sysmess/update";
	}
	
	@RequestMapping("searchListPage")
	@ResponseBody
	public Map searchListPage(Integer page, Integer rows, CRUDForm curdForm) {
		  // 从request中获取通用查询条件
    	if(page==0){
    		page=1;
    	}
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("pageStart", (page-1)*rows);
    	params.put("pageSize", rows);

    	for (Map.Entry<String, String> entry : curdForm.getFilters().entrySet()) {
        	if(StringUtils.isNotBlank(entry.getValue())){
        		if(entry.getKey().equals("stat")){
        			params.put("stat", entry.getValue());
        		} else if(entry.getKey().equals("title")){
					params.put("title", entry.getValue());
				} else if(entry.getKey().equals("content")){
					params.put("content", entry.getValue());
				} else if(entry.getKey().equals("publishTimeStart")){
					params.put("publishTimeStart", entry.getValue());
				} else if(entry.getKey().equals("publishTimeEnd")){
					params.put("publishTimeEnd", entry.getValue());
				}else if(entry.getKey().equals("startTime")){
					params.put("startTime", entry.getValue());
        		} else if(entry.getKey().equals("endTime")){
					params.put("endTime", entry.getValue());
        		} else if(entry.getKey().equals("destUser")){
					params.put("destUser", entry.getValue());
				}
        	}
    	}
    	Page pinfo = systemMessService.searchSysmess(page,rows,params);
    	        
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
        return jsonMap;
	}
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	@RequestMapping("remove")
	@ResponseBody
	public RespBody deleteSysmess(Long id){
		systemMessService.deleteSysmess(id);
		return this.respBodyWriter.toSuccess();
	}
		
	@RequestMapping("add")
	@ResponseBody
	public RespBody saveSysmess(SystemMess mess){
		
		mess.setCreatorId(U.getUid());
		mess.setCreateDate(new Date());
		System.out.println(JSONObject.fromObject(mess).toString());
		systemMessService.saveSysmess(mess);
		if("0".equals(mess.getIsplan())){
			systemMessService.sendMessage(mess);
		}else{
			addToPlan(mess);
		}
		
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("uploadfile")
	@ResponseBody
	public RespBody uploadFile(MultipartFile files){
		String result = "";
		try {
			InputStream input = files.getInputStream();
			BufferedReader in2=new BufferedReader(new InputStreamReader(input));
			   
			String y="";
			   
			while((y=in2.readLine())!=null){
				if(StringUtils.isNotBlank(y)){
					result += (y.trim() +";");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this.respBodyWriter.toSuccess(result);
	}

	@RequestMapping("uploadImage")
	@ResponseBody
	public RespBody uploadImage(@RequestParam("msgImage") MultipartFile file){
		String filePath = null;
		try {
			filePath = uploadImageService.upload(file.getSize(), file.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.respBodyWriter.toSuccess(filePath);
	}

	private void addToPlan(SystemMess mess) {
		try {
			sysmessQuartz.addJob(mess.getId(), mess.getPublishTime());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
