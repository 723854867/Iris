package com.busap.vcs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.PrivateMsg;
import com.busap.vcs.data.mapper.PrivateMsgDAO;
import com.busap.vcs.service.PrivateMsgService;

@Service("privateMsgService")
public class PrivateMsgServiceImpl implements PrivateMsgService {

	@Autowired
	private PrivateMsgDAO privateMsgDAO;
	
	@Override
	public Map<String, Object> searchUnreadMsg(String uid) {
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String,Object>> unread = privateMsgDAO.unreadCount(uid.toString());
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Integer totalCount = 0;
		if(unread != null && unread.size()>0){
			for(Map<String,Object> m:unread){
				m.put("reciever", uid);
				List<PrivateMsg> plist = privateMsgDAO.searchMsg(m);
				if(plist!=null && plist.size()>0){
					Map<String,Object> userMsg = new HashMap<String,Object>();
					totalCount = totalCount + plist.size();
					userMsg.put("count", plist.size());
					userMsg.put("senderId", m.get("sender"));
					userMsg.put("senderName", plist.get(0).getSenderName());
					userMsg.put("data", plist);
					
					resultList.add(userMsg);
				}
			}
		}
		result.put("result", resultList);
		result.put("total", totalCount);
		
		return result;
	}

}
