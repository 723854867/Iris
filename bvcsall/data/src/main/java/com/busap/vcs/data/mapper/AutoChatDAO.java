package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.AutoChat;

public interface AutoChatDAO {

	List<AutoChat> searchList(Map<String,Object> params);
	
	Integer searchCount(Map<String,Object> params);
}
