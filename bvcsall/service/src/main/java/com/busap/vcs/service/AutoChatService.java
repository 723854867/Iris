package com.busap.vcs.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.busap.vcs.data.entity.AutoChat;
import com.busap.vcs.data.entity.AutoChatType;

public interface AutoChatService extends BaseService<AutoChat, Long> {

	void addAutoChat(AutoChat chat);
	
	void updateStatus(Long id,Integer status);
	
	Page<AutoChat> searchList(Integer pageNo, Integer pageSize,Map<String,Object> params);
	
	void addAutoChatType(String name);
	
	void updateAutoChatTypeStatus(Long id,Integer status);
	
	List<AutoChatType> findTypes();
}
