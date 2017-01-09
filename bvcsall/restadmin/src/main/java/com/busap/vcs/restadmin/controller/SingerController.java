package com.busap.vcs.restadmin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.SingerService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.controller.CRUDController;

@Controller
@RequestMapping("singer")
public class SingerController extends CRUDController{
	
	@Resource(name="singerService")
	private SingerService singerService;
	
	private String  defaultStart="2016-08-01";
	


	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param request
	 * @return
	 */
	@RequestMapping("stuList")
	@ResponseBody
	@EnablePaging
	public Map<String, Object> stuList(@ModelAttribute("queryPage") JQueryPage queryPage,
			@RequestParam(required = false)String startTime,
			@RequestParam(required = false)String endTime,
		   HttpServletRequest request){
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime==null?defaultStart:startTime);
		params.put("endTime", endTime);
		params.put("userType", 1);
		
		 Map<String, Object> resultMap = new HashMap<String, Object>();
	    List<Map> list=singerService.querySinger(params);
	       
	        Page page = PagingContextHolder.getPage();
	        resultMap.put("total", page.getTotalResult());
	        resultMap.put("rows", list);
	        return resultMap;
		
	}
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param request
	 * @return
	 */
	@RequestMapping("anchorList")
	@ResponseBody
	@EnablePaging
	public Map<String, Object> anchorList(@ModelAttribute("queryPage") JQueryPage queryPage,
			@RequestParam(required = false)String startTime,
			@RequestParam(required = false)String endTime,
		   HttpServletRequest request){
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime==null?defaultStart:startTime);
		params.put("endTime", endTime);
		params.put("userType", 2);
		
		 Map<String, Object> resultMap = new HashMap<String, Object>();
	    List<Map> list=singerService.querySinger(params);
	       
	       Page page = PagingContextHolder.getPage();
	        resultMap.put("total",page.getTotalResult());
	        resultMap.put("rows", list);
	        return resultMap;
		
	}
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param request
	 * @return
	 */
	@RequestMapping("richList")
	@ResponseBody
	@EnablePaging
	public Map<String, Object> richList(@ModelAttribute("queryPage") JQueryPage queryPage,
			@RequestParam(required = false)String startTime,
			@RequestParam(required = false)String endTime,
		   HttpServletRequest request){
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime==null?defaultStart:startTime);
		params.put("endTime", endTime);
		params.put("userType", 2);
		
		 Map<String, Object> resultMap = new HashMap<String, Object>();
	    List<Map> list=singerService.searchRicher(params);
	       
	       Page page = PagingContextHolder.getPage();
	        resultMap.put("total", page.getTotalResult());
	        resultMap.put("rows", list);
	        return resultMap;
		
	}
	@RequestMapping("stupage")
	public String stupage(HttpServletRequest request){
		return "singer/stu";
	}

	@RequestMapping("anchorpage")
	public String anchorpage(HttpServletRequest request){
		return "singer/anchor";
	}
	@RequestMapping("richpage")
	public String richpage(HttpServletRequest request){
		return "singer/rich";
	}
	@Override
	public void setBaseService(BaseService baseService) {
		// TODO Auto-generated method stub
		
	}

}
