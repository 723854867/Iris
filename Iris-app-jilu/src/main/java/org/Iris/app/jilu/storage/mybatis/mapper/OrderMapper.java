package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.Order;

public interface OrderMapper {

	Order getOrderById(String orderId);
	
	void insert(Order order);
	
	void update(Order order);
}
