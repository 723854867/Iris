package com.busap.vcs.chat.srv;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.base.WsMessage;
import com.busap.vcs.data.entity.PrivateMsg;
import com.busap.vcs.data.mapper.PrivateMsgDAO;

@Service("p2pService")
public class P2pService {
	private Logger logger = LoggerFactory.getLogger(P2pService.class);
	@Autowired
	private PrivateMsgDAO privateMsgDAO;

	public void saveMsg(WsMessage message){
		logger.info("save private message:{}",message);
		PrivateMsg pmsg = new PrivateMsg();
		
		InsertThread ins = new InsertThread(pmsg);
		new Thread(ins).start();
	}
	
	public Integer getUnreadCount(String uid){
		List<Map<String,Object>> result = privateMsgDAO.unreadCount(uid);
		Integer count = 0;
		if(result!=null && result.size()>0){
			for(Map<String,Object> m:result){
				count += m.get("count")==null?0:(Integer)m.get("count");
			}
		}
		return count; 
	}

	public void readMsg(WsMessage message) {
		String id = message.getMessageId();
		if(StringUtils.isNotBlank(id)){
			ReadThread rt = new ReadThread(id);
			new Thread(rt).start();
		} else {
			logger.info("the message don't has an id,{}",message);
		}
	}
	
	class InsertThread implements Runnable{
		private PrivateMsg privateMsg;
		
		public InsertThread(PrivateMsg pmsg){
			this.privateMsg = pmsg;
		}
		@Override
		public void run() {
			Integer i = privateMsgDAO.insertMsg(privateMsg);
			logger.info("do insert private message,{} record insert successful.",i);
		}
	}
	
	class ReadThread implements Runnable{
		private String id;
		
		public ReadThread(String id){
			this.id = id;
		}
		@Override
		public void run() {
			Integer i = privateMsgDAO.readMsg(id);
			logger.info("do read private message,{} record insert successful.",i);
		}
	}

}
