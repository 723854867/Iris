package org.Iris.app.jilu.common.bean.enums;

public enum JiLuLuaCommand {
	
	TOKEN_REPLACE,
	
	CUSTOMER_LIST_REFRESH_TIME,
	
	// 当添加新客户时会尝试将该新客户加入客户排序列表(如果商户的客户排序列表已经加载的情况下)
	CUSTOMER_LIST_ADD,
	// 当完成一笔交易的时候 需要更新商户的客户排序列表
	FINISH_ORDER,
	
	ACCOUNT_REFRESH,
	
	ORDER_STATUS,
	
	REMOVE_FRIEND_APPLY;
}
