package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.Map;

import org.Iris.app.jilu.storage.domain.Order;
import org.Iris.app.jilu.storage.domain.OrderGoods;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.OrderGoodsSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;

public interface OrderGoodsMapper {

	OrderGoods getOrderById(String orderId);
	
	void insert(Order order);
	@InsertProvider(type = OrderGoodsSQLBuilder.class, method = "batchInsert")
	void batchInsert(Map<String,OrderGoods> map);
	
	void update(Order order);
}
