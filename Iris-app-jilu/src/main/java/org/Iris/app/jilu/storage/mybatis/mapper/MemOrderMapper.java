package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemOrderSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemOrderMapper {

	@SelectProvider(type = MemOrderSQLBuilder.class,method="getOrderById")
	MemOrder getOrderById(String orderId);
	@InsertProvider(type = MemOrderSQLBuilder.class,method="insert")
	void insert(MemOrder order);
	@UpdateProvider(type = MemOrderSQLBuilder.class,method="update")
	void update(MemOrder order);
	
}
