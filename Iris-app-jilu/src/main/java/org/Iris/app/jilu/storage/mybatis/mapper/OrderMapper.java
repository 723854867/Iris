package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.Order;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.OrderSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;

public interface OrderMapper {

	@SelectProvider(type = OrderSQLBuilder.class,method="getByOrderId")
	Order getByOrderId(String orderId);
	@InsertProvider(type = OrderSQLBuilder.class,method="insert")
	void insert(Order order);
	
}
