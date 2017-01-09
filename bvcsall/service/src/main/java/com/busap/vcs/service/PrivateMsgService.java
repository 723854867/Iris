package com.busap.vcs.service;

import java.util.Map;

public interface PrivateMsgService {

	/**
	 * 查询未读私信列表
	 * @param uid
	 * @return
	 */
	Map<String,Object> searchUnreadMsg(String uid);
}
