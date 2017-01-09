package com.busap.vcs.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.busap.vcs.base.Filter;
import com.busap.vcs.data.entity.Notification;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.NotificationRepository;
import com.busap.vcs.service.NotificationService;

@Service("notificationService")
public class NotificationServiceImpl extends BaseServiceImpl<Notification, Long> implements NotificationService {
	
	@Autowired
	NotificationJPushUtil util;
	
	@Resource(name = "notificationRepository")
	NotificationRepository notificationRepository;
	
	@Resource(name = "notificationRepository")
	@Override
	public void setBaseRepository(BaseRepository<Notification, Long> baseRepository) {
		super.setBaseRepository(baseRepository);
	}
	
	//重写save方法
	/**
	 * setCreatorId() 代表发送给的某个用户，而不是创建者，为空则发送给所有用户 <br>
	 * setType() 字符串（notification[通知] , message[消息内容]） 为空则全部<br>
	 * setContent() 是必须的  notification[长度为58个字符] , message[总长度为234个字符， 是消息内容+【发送给某人的id（3字符个算一个长度）】的和]，超出”极光“会自动截取
	 */
	public void save(Notification entity) {
		
		Map<String,Object> result=util.sendByAlias(entity.getType(),entity.getContent(),entity.getCreatorId()==null?null:entity.getCreatorId()+"");
		boolean isOk=(Boolean)(result.get("isok"));
		entity.setMsgid(Long.valueOf(result.get("msg_id")+""));
		if(isOk){
			entity.setSendno(Integer.valueOf(result.get("sendno")+""));
		}else{
			entity.setError(result.get("exception")+"");
		}
		//成功存1，失败存0
		entity.setResult(Integer.valueOf(isOk?"1":"0"));
		
		baseRepository.save(entity);
	}

	// 查找某用户收到的消息通知
	@Override
	public Page<Notification> findMyNotifications(Integer page, Integer size, String uid) {
		List<Filter> filters = new ArrayList<Filter>(1); 
    	filters.add(new Filter("creatorId", uid)); 
    	Pageable pr = new PageRequest(page, size);
    	Page<Notification> p = this.findAll(pr , filters);
    	return p;
	}
}
