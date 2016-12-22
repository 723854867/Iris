package org.Iris.app.jilu.common.bean.enums;

public enum JiLuLuaCommand {

	CUSTOMER_LIST_REFRESH,
	
	// 当添加新客户时会尝试将该新客户加入客户排序列表(如果商户的客户排序列表已经加载的情况下)
	CUSTOMER_LIST_ADD;
}
