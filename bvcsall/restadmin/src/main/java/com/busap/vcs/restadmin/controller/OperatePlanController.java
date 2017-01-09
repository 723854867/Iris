package com.busap.vcs.restadmin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.PagingContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.entity.OperatePlan;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.OperatePlanService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;

@Controller()
@RequestMapping("operatePlan")
public class OperatePlanController extends CRUDController<OperatePlan, Long> {

	@Resource(name = "operatePlanService")
	private OperatePlanService operatePlanService;
	
	@Resource(name = "operatePlanService")
	@Override
	public void setBaseService(BaseService<OperatePlan, Long> baseService) {
		this.baseService = baseService;
	}
	
	@RequestMapping("planlist")
	public String operatePlanList(){
		return "operatePlan/list";
	}
	
/*	@RequestMapping("searchOperatePlan")
	@ResponseBody
	public Map searchOperatePlan(Integer page,Integer rows, CRUDForm curdForm){
		if(page==null || page<1){
			page = 1;
		}
		if(rows==null || rows<1){
			rows = 20;
		}
		
		Page pinfo = operatePlanService.searchOperatePlan(page,rows);
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
        return jsonMap;
	}*/

	@RequestMapping("queryOperatePlanList")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> queryOperatePlanList(@ModelAttribute("queryPage") JQueryPage queryPage){
		Map<String,Object> params = new HashMap<String, Object>();
		List<OperatePlan> list = operatePlanService.queryOperatePlanList(params);
		com.busap.vcs.util.page.Page page = PagingContextHolder.getPage();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("total",page.getTotalResult());
		map.put("rows",list);
		return map;
	}
	
	@RequestMapping("add")
	@ResponseBody
	public RespBody add(OperatePlan plan) {
		Integer count = operatePlanService.findPlanInTime(plan);
		if(count>0){
			return this.respBodyWriter.toError("与已有计划起止时间有冲突，请重新设置！", -1);
		}
		if(plan.getId()!=null){
			OperatePlan old = this.operatePlanService.find(plan.getId());
			old.setEndDate(plan.getEndDate());
			old.setStartDate(plan.getStartDate());
			old.setTargetNum(plan.getTargetNum());
			old.setPlanType(plan.getPlanType());
			old.setTimeUnit(plan.getTimeUnit());
			
			this.operatePlanService.save(old);
		}else{
			plan.setCreatorId(U.getUid());
			operatePlanService.save(plan);
		}
		
		return this.respBodyWriter.toSuccess();
	}

}
