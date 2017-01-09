package com.busap.vcs.service;

import org.springframework.data.domain.Page;

import com.busap.vcs.data.entity.OperatePlan;

import java.util.List;
import java.util.Map;


/**
 * Created by
 * User: dmsong
 * Date: 19/8/15
 * Time: 11:52 AM
 */
public interface OperatePlanService extends BaseService<OperatePlan, Long> {

	//分页查询列表
	public Page searchOperatePlan(Integer pageNo, Integer pageSize);
 
	//查询已有运营计划与当前计划时间是否冲突
	public Integer findPlanInTime(OperatePlan plan);
	
	//查询当前运营计划
	public OperatePlan findCurrentPlan(String timeUnit);

	List<OperatePlan> queryOperatePlanList(Map<String,Object> params);
}
