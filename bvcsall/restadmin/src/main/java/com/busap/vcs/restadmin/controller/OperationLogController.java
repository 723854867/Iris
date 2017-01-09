package com.busap.vcs.restadmin.controller;

import javax.annotation.Resource;

import com.busap.vcs.restadmin.utils.EnableFunction;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.PagingContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.busap.vcs.data.entity.OperationLog;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.OperationLogService;
import com.busap.vcs.webcomn.controller.CRUDController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 暂时结合easyui写的增删查改的例子
 * @author meizhiwen
 *
 */
@Controller()
@RequestMapping("operationLog")
public class OperationLogController extends CRUDController<OperationLog, Long>{

	private static final Logger logger = LoggerFactory.getLogger(OperationLogController.class);
	
	@Resource(name="operationLogService")
	private OperationLogService operationLogService;
	
	@Resource(name="operationLogService")
	@Override
	public void setBaseService(BaseService<OperationLog, Long> baseService) {
		this.baseService=baseService;
	}

	@EnableFunction("操作日志,查看操作日志")
	@RequestMapping("operationLoglist")
	public ModelAndView list(@RequestParam(value = "searchUsername",required = false) String searchUsername){
		ModelAndView mav = new ModelAndView();
		mav.addObject("searchUsername",searchUsername);
		mav.setViewName("operationLog/list");
		return mav;
	}

	@RequestMapping("queryOperationLogs")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> queryOperationLogs(@ModelAttribute("queryPage") JQueryPage queryPage,
												 @RequestParam(value = "uname",required = false) String uname,
												 @RequestParam(value = "roleId",required = false) Integer roleId,
												 @RequestParam(value = "permissionId",required = false) Long permissionId,
												 @RequestParam(value = "startTime",required = false) String startTime,
												 @RequestParam(value = "endTime",required = false) String endTime){
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("moduleType","myvideo_restadmin");
		params.put("uname",uname);
		params.put("roleId",roleId);
		params.put("permissionId",permissionId);
		params.put("startTime",startTime);
		params.put("endTime",endTime);
		List<OperationLog> list = operationLogService.queryOperationLogs(params);
		com.busap.vcs.util.page.Page page = PagingContextHolder.getPage();
		map.put("total",page.getTotalResult());
		map.put("rows",list);
		return map;
	}
	
}
