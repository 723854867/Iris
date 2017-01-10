package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.MemOrderStatus;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemOrderStatusSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemOrderStatusMapper {

	@InsertProvider(type = MemOrderStatusSQLBuilder.class , method="insert")
	void insert(MemOrderStatus status);
	
	@UpdateProvider(type = MemOrderStatusSQLBuilder.class , method="update")
	void update(MemOrderStatus status);
	
	@SelectProvider(type = MemOrderStatusSQLBuilder.class , method="getMemOrderStatusByOrderId")
	MemOrderStatus getMemOrderStatusByOrderId(String orderId);
}
