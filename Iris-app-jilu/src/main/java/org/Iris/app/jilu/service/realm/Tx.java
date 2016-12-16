package org.Iris.app.jilu.service.realm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.Iris.app.jilu.storage.domain.Order;
import org.Iris.app.jilu.storage.domain.OrderGoods;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemMerchantMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.OrderGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.OrderMapper;
import org.Iris.app.jilu.storage.redis.cache.OrderCache;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component(value = "tx")
public class Tx {
	
	@Resource
	private MemAccountMapper memAccountMapper;
	@Resource
	private MemMerchantMapper memMerchantMapper;
	@Resource
	private OrderMapper orderMapper;
	@Resource
	private OrderGoodsMapper orderGoodsMapper;
	@Resource
	private OrderCache orderCache;

	/**
	 * 确认订单
	 * @param orderId
	 */
	@Transactional
	public void OrderLock(String orderId){
		Order order = orderCache.getByOrderId(orderId);
		List<OrderGoods> oList = orderCache.getOGListByOrderId(orderId);
		order.setStatus(0);
		Map<String, OrderGoods> map = new HashMap<String,OrderGoods>();
		int number = 1;
		for(OrderGoods orderGoods:oList)
			map.put(orderGoods.getOrderId() + "_" + number++, orderGoods);
		orderMapper.insert(order);
		orderGoodsMapper.batchInsert(map);
		orderCache.flushHashBean(order);
	}
}
