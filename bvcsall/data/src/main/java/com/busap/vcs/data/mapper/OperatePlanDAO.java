package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.OperatePlan;

public interface OperatePlanDAO {
	//查询所有运营计划
	public List<OperatePlan> findAllPlan(Map<String,Object> params);
	//查询总条数
	public Integer findAllPlanCount(Map<String,Object> params);
	//查询当前计划是否与已存在的计划有时间交集
	public Integer findPlanInTime(OperatePlan plan);
	//查询当前的运营计划
	public List<OperatePlan> findCurrentPlan(String timeUnit);

	List<OperatePlan> selectOperatePlanList(Map<String,Object> params);
}
