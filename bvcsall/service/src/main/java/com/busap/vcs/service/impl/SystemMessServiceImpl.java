package com.busap.vcs.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.busap.vcs.base.Action;
import com.busap.vcs.base.Message;
import com.busap.vcs.base.Module;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.SystemMess;
import com.busap.vcs.data.mapper.SysmessDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.SystemMessRepository;
import com.busap.vcs.data.vo.SysmessVO;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.SystemMessService;
import com.busap.vcs.service.kafka.producer.KafkaProducer;
import com.busap.vcs.service.mipush.MiPushUtil;

@Service("systemMessService")
public class SystemMessServiceImpl extends BaseServiceImpl<SystemMess, Long> implements SystemMessService {
 
	@Resource(name = "systemMessRepository")
	private SystemMessRepository systemMessRepository;
	
	@Autowired
    private KafkaProducer kafkaProducer;
	
	@Autowired
	private SysmessDAO sysmessDAO;

	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name = "systemMessRepository")
	@Override
	public void setBaseRepository(BaseRepository<SystemMess, Long> baseRepository) {
		super.setBaseRepository(baseRepository);
	}

	@Transactional
	@Override
	public void saveSysmess(SystemMess sm) {
		this.save(sm);
	}

	@Transactional
	@Override
	public void deleteSysmess(Long id) {
		this.delete(id);
		
	}

	@Transactional
	@Override
	public void updateSysmess(SystemMess sm) {
		this.update(sm);
		
	}


	@Override
	public Page searchSysmess(Integer pageNo, Integer pageSize,	Map<String, Object> params) {
		List<SysmessVO> sysmessList = sysmessDAO.searchSysmess(params);
		Integer total = sysmessDAO.searchSysmessCount(params);
		Page<SysmessVO> pinfo = new PageImpl<SysmessVO>(sysmessList,new PageRequest(pageNo, pageSize, null),total);
		return pinfo;
	}
	
	@Transactional
	@Override
	public void sendMessage(SystemMess sm) {
		Message msg = new Message();
		msg.setModule(Module.BNOTICE);
		msg.setAction(Action.INSERT);
		Map<String, Object> map = new HashMap<String, Object>();
		if("all".equals(sm.getDestUser())){
			map.put("scope", "all");
		}else{
			map.put("scope", "notall");
			map.put("uids", sm.getDestUser());
		}
		if(!"app".equals(sm.getOperation())){
			if("h5".equals(sm.getOperation())){
				map.put("targetUrl", sm.getTargetUrl());
			}else {
				map.put("targetid", sm.getTargetid().toString());
			}
		}
		map.put("op", sm.getOperation());
		map.put("title", sm.getTitle());
		map.put("content", sm.getContent());
		msg.setDataMap(map);
    	kafkaProducer.send("push-msg-topic", msg);
    	
    	sm.setStat("1");
    	sm.setPublishTime(new Date());
    	String targetUser = sm.getDestUser();
    	if(StringUtils.isNotBlank(targetUser)){
    		String newDest = "";
    		String temp[] = targetUser.split(";");
			for(String alias:temp){
				String deviceUUID = jedisService.getValueFromMap(BicycleConstants.USER_INFO+alias, "deviceUuid");
				if(StringUtils.isNotBlank(deviceUUID)){
					newDest = newDest + alias + deviceUUID+";";
				}
			}
			if(StringUtils.isNotBlank(newDest)){
				sm.setDestUser(newDest.substring(0,newDest.length()-1));
			}
    	}
    	//小米推送
		try {
			MiPushUtil.sendMessage(sm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sm.setDestUser(targetUser);
    	this.update(sm);
		System.out.println("==========================================================++++++");
	}

	@Override
	public List<SystemMess> searchPlan() {
		return sysmessDAO.searchPlan();
	}

	@Override
	public List<SysmessVO> searchAvailableSysmessByUid(Long uid,
			Date timestamp, int count) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("uid", uid);
		paramMap.put("timestamp", timestamp);
		paramMap.put("count", count);
		return sysmessDAO.searchAvailableSysmessByUid(paramMap);
	}   
	
}
