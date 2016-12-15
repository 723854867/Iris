package org.Iris.app.jilu.storage.redis.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.Iris.app.jilu.storage.domain.Order;
import org.Iris.app.jilu.storage.domain.OrderGoods;
import org.Iris.app.jilu.storage.mybatis.mapper.OrderGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.OrderMapper;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.springframework.stereotype.Service;

@Service
public class OrderCache extends RedisCache {

	@Resource
	private OrderMapper orderMapper;
	@Resource
	private OrderGoodsMapper orderGoodsMapper;

	/**
	 * 创建订单时 缓存订单信息
	 * @throws Exception 
	 */
	public void createOrder(Order order,OrderGoods[] orderGoods){
		try {
			flushHashBean(order);
			for(OrderGoods ogs: orderGoods){
				redisOperate.sadd(RedisKeyGenerator.getOrderGoodsSetKey(order.getOrderId()),String.valueOf(ogs.getGoodsId()));
				ogs.setOrderId(order.getOrderId());
				flushHashBean(ogs);
			}
		} catch (Exception e) {
			//创建订单过程中如果出现异常回滚缓存
			redisOperate.del(order.redisKey());
			redisOperate.del(RedisKeyGenerator.getOrderGoodsSetKey(order.getOrderId()));
			for(OrderGoods ogs: orderGoods){
				redisOperate.del(ogs.redisKey());
			}
			throw e;
		}
		
	}
	
	public Order updateOrder(Order order,OrderGoods[] orderGoods){
		Order oldOrder = null;
		List<OrderGoods> oldList = null;
		try {
			oldOrder = getHashBean(new Order(order.getOrderId()));
			oldList = getOGListByOrderId(order.getOrderId());
			//先清除原先缓存
			redisOperate.del(RedisKeyGenerator.getOrderGoodsSetKey(order.getOrderId()));
			for(OrderGoods ogs: oldList){
				redisOperate.del(ogs.redisKey());
			}
			//更新
			flushHashBean(order);
			for(OrderGoods ogs: orderGoods){
				redisOperate.sadd(RedisKeyGenerator.getOrderGoodsSetKey(order.getOrderId()),String.valueOf(ogs.getGoodsId()));
				ogs.setOrderId(order.getOrderId());
				flushHashBean(ogs);
			}
			
		} catch (Exception e) {
			//更新订单过程中如果出现异常回滚缓存
			redisOperate.del(RedisKeyGenerator.getOrderGoodsSetKey(order.getOrderId()));
			for(OrderGoods ogs: orderGoods){
				redisOperate.del(ogs.redisKey());
			}
			
			flushHashBean(oldOrder);
			for(OrderGoods ogs: oldList){
				redisOperate.sadd(RedisKeyGenerator.getOrderGoodsSetKey(order.getOrderId()),String.valueOf(ogs.getGoodsId()));
				ogs.setOrderId(order.getOrderId());
				flushHashBean(ogs);
			}
			throw e;
		}
		
		return order;
	}
	/**
	 * 根据订单号获取订单基本信息
	 * @param orderId
	 * @return
	 */
	public Order getByOrderId(String orderId){
		Order order = getHashBean(new Order(orderId));
		if(null !=order)
			return order;
		order = orderMapper.getByOrderId(orderId);
		if (null == order)
			return null;
		flushHashBean(order);
		return order;
	}
	/**
	 * 通过订单号获取该订单下的产品列表
	 * @param orderId
	 * @return
	 */
	public List<OrderGoods> getOGListByOrderId(String orderId){
		Set<String> goodsIds = redisOperate.sdiff(RedisKeyGenerator.getOrderGoodsSetKey(orderId));
		List<OrderGoods> oList = new ArrayList<>();
		for(String goodsId:goodsIds){
			OrderGoods oGoods = getHashBean(new OrderGoods(orderId,Integer.valueOf(goodsId)));
			oList.add(oGoods);
		}
		return oList;
	}
	
}
