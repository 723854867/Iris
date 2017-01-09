package com.busap.vcs.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.busap.vcs.base.Filter;
import com.busap.vcs.data.entity.Sms;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.SmsRepository;
import com.busap.vcs.service.SmsService;

@Service("smsService")
public class SmsServiceImpl extends BaseServiceImpl<Sms, Long> implements SmsService { 
	@Resource(name = "smsRepository")
	private SmsRepository smsRepository; 

	@Resource(name = "smsRepository")
	@Override
	public void setBaseRepository(BaseRepository<Sms, Long> baseRepository) {
		super.setBaseRepository(baseRepository);
	}

	@Override
	public List<Sms> findMySmss(Long uid,boolean communal) {
		
		if(uid==null){
			return null;
		}
		Sort sort=new Sort(Sort.Direction.ASC,"createDate");
		List<Filter> filters=new ArrayList<Filter>();
		Filter f=Filter.eq("creatorId", uid);
		filters.add(f);
		
		List<Sms> smss=this.smsRepository.findAll(filters, sort);
		
		if(communal){
			filters.clear();
			filters.add(Filter.isNull("creatorId"));
			smss.addAll(this.smsRepository.findAll(filters, sort));
		}
		
		return smss;
	}  
	
}
