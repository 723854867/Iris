package com.busap.vcs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.OperatePlan;
import com.busap.vcs.data.mapper.OperatePlanDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.OperatePlanRepository;
import com.busap.vcs.service.OperatePlanService;

@Service("operatePlanService")
public class OperatePlanServiceImpl extends BaseServiceImpl<OperatePlan, Long> implements OperatePlanService {
	
    @Resource(name = "operatePlanRepository")
    private OperatePlanRepository operatePlanRepository;
    
    @Autowired
    private OperatePlanDAO operatePlanDAO;

     
    @Resource(name = "operatePlanRepository")
    @Override
    public void setBaseRepository(BaseRepository<OperatePlan, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }

	@Override
	public Page<OperatePlan> searchOperatePlan(Integer pageNo, Integer pageSize) {
		Map<String,Object> params = new HashMap<String,Object>();
		Integer pageStart = (pageNo-1)*pageSize;
		params.put("pageStart", pageStart);
		params.put("pageSize", pageSize);
		
		List<OperatePlan> opList = operatePlanDAO.findAllPlan(params);
		Integer total = operatePlanDAO.findAllPlanCount(params);
		
		Page<OperatePlan> pinfo = new PageImpl<OperatePlan>(opList,new PageRequest(pageNo-1, pageSize, null),total);

		return pinfo;
	}

	@Override
	public Integer findPlanInTime(OperatePlan plan) {
		return operatePlanDAO.findPlanInTime(plan);
	}

	@Override
	public OperatePlan findCurrentPlan(String timeUnit) {
		List<OperatePlan> currentPlan = operatePlanDAO.findCurrentPlan(timeUnit);
		if(currentPlan != null && currentPlan.size()>0){
			return currentPlan.get(0);
		}
		return null;
	}

	@Override
	public List<OperatePlan> queryOperatePlanList(Map<String,Object> params){
		return operatePlanDAO.selectOperatePlanList(params);
	}
    
}
