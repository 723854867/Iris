package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.PrivateMsg;

public interface PrivateMsgDAO {

	/**
	 * 写入消息
	 * @param message
	 * @return
	 */
	public Integer insertMsg(PrivateMsg message);
	
	/**
	 * 更新消息已读状态
	 * @param id
	 * @return
	 */
	public Integer readMsg(String id);
	
	/**
	 * 拉取消息列表
	 * @param params
	 * @return
	 */
	public List<PrivateMsg> searchMsg(Map<String,Object> params);
	//查询用户未读私信数量
	public List<Map<String,Object>> unreadCount(String uid);
}
