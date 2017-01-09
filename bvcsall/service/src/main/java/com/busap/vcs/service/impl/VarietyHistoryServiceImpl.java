package com.busap.vcs.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.VarietyHistory;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.VarietyHistoryRepository;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.VarietyHistoryService;
import com.google.common.collect.Lists;
@Service("varietyHistoryService")
public class VarietyHistoryServiceImpl  extends BaseServiceImpl<VarietyHistory, Long> implements VarietyHistoryService {

	@Resource(name="varietyHistoryRepository")
	private VarietyHistoryRepository varietyHistoryRepository;
	
	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name="varietyHistoryRepository")
	@Override
	public void setBaseRepository(BaseRepository<VarietyHistory, Long> baseRepository) {
		super.setBaseRepository(varietyHistoryRepository);
	}


	@Override
	public List<VarietyHistory> findAllVariety() {
		List<VarietyHistory> result = this.findAll();
		if(result != null && result.size()>0){
			for(VarietyHistory history:result){
				String uids = history.getUids();
				String uidsArr[] = uids.split(",");
				List<Long> ids = new ArrayList<Long>();
				
				for(String u:uidsArr){
					ids.add(Long.parseLong(u));
				}
				Long idsArr[] =  new Long[ids.size()] ;
				List<Ruser> users = ruserService.findAll(ids.toArray(idsArr));
				history.setUsers(users);
			}
			result = Lists.reverse(result);
		}
		
		return result;
	}


	@Override
	public void addVarietyHistory(VarietyHistory variety) {
		this.save(variety);		
	}


	@Override
	public void updateVarietyHistory(VarietyHistory variety) {
		Long id = variety.getId();
		
		VarietyHistory vold = this.find(id);
		vold.setName(variety.getName());
		vold.setPicUrl(variety.getPicUrl());
		vold.setPlayUrl(variety.getPlayUrl());
		vold.setTag(variety.getTag());
		vold.setUids(variety.getUids());
		
		this.update(vold);		
	}


	@Override
	public void deleteVarietyHistory(Long id) {
		this.delete(id);		
	}
	
}
