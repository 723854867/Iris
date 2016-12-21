package org.Iris.app.jilu.storage.redis.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.Iris.app.jilu.storage.domain.MemGoods;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.domain.MemOrderGoods;
import org.Iris.app.jilu.storage.mybatis.mapper.MemGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderMapper;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.util.common.SerializeUtil;
import org.Iris.util.lang.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderCache extends RedisCache {

	@Resource
	private MemOrderMapper orderMapper;
	@Resource
	private MemOrderGoodsMapper orderGoodsMapper;
	@Resource
	private MemGoodsMapper memGoodsMapper;

	/**
	 * 创建订单
	 * @throws Exception 
	 */
	@Transactional
	public void createOrder(MemOrder order,MemOrderGoods[] orderGoods){
		orderMapper.insert(order);
		List<MemOrderGoods> list = new ArrayList<MemOrderGoods>(Arrays.asList(orderGoods));
		batchInsertOrderGoodsByList(order.getOrderId(), list);
		flushHashBean(order);
	}

	private void batchInsertOrderGoodsByList(String orderId, List<MemOrderGoods> list) {
		for(MemOrderGoods ogs: list){
			MemGoods goods = getGoodsById(ogs.getGoodsId());
			ogs.setOrderId(orderId);
			ogs.setGoodsName(goods.getZhName());
			ogs.setStatus(0);
			int time = DateUtils.currentTime();
			ogs.setCreated(time);
			ogs.setUpdated(time);
		}
		Map<String, List<MemOrderGoods>> map = new HashMap<String, List<MemOrderGoods>>();
		map.put("list", list);
		orderGoodsMapper.batchInsert(map);
		for(MemOrderGoods orderGoods :list){
			redisOperate.sadd(RedisKeyGenerator.getMemOrderGoodsSetKey(orderId),String.valueOf(orderGoods.getGoodsId()));
			flushHashBean(orderGoods);
		}
	}
	
	private void batchUpdateOrderGoodsByList(String orderId, List<MemOrderGoods> list) {
		for(MemOrderGoods ogs: list){
			ogs = getHashBean(new MemOrderGoods(orderId, ogs.getGoodsId()));
			ogs.setCount(ogs.getCount());
			ogs.setUnitPrice(ogs.getUnitPrice());
			
		}
		Map<String, List<MemOrderGoods>> map = new HashMap<String, List<MemOrderGoods>>();
		map.put("list", list);
		orderGoodsMapper.batchUpdate(map);
		for(MemOrderGoods orderGoods :list){
			flushHashBean(orderGoods);
		}
	}
	/**
	 * 更新订单
	 * @param order
	 * @param addGoodsList
	 * @param updateGoodsList
	 * @param deleteGoodsList
	 */
	@Transactional
	public void updateOrder(MemOrder order,String addGoodsList,String updateGoodsList,String deleteGoodsList){
		orderMapper.update(order);
		if(addGoodsList!=null){
			MemOrderGoods addOrderGoods[] = SerializeUtil.JsonUtil.GSON.fromJson(addGoodsList, MemOrderGoods[].class);
			List<MemOrderGoods> list = new ArrayList<MemOrderGoods>(Arrays.asList(addOrderGoods));
			batchInsertOrderGoodsByList(order.getOrderId(), list);
		}
		if(updateGoodsList!=null){
			MemOrderGoods updateOrderGoods[] = SerializeUtil.JsonUtil.GSON.fromJson(updateGoodsList, MemOrderGoods[].class);
			List<MemOrderGoods> list = new ArrayList<MemOrderGoods>(Arrays.asList(updateOrderGoods));
			batchUpdateOrderGoodsByList(order.getOrderId(), list);
		}
		if(deleteGoodsList!=null){
			Long goodsIds[] = SerializeUtil.JsonUtil.GSON.fromJson(updateGoodsList, Long[].class);
			List<Long> list = new ArrayList<Long>(Arrays.asList(goodsIds));
			Map<String, List<Long>> map = new HashMap<String, List<Long>>();
			map.put("list", list);
			orderGoodsMapper.batchDelete(map);
			for(long goodsId :list){
				redisOperate.del(RedisKeyGenerator.getMemOrderGoodsDataKey(order.getOrderId(), goodsId));
				redisOperate.srem(RedisKeyGenerator.getMemOrderGoodsSetKey(order.getOrderId()),goodsId+"");
			}
		}
		flushHashBean(order);
	}
	/**
	 * 根据订单号获取订单基本信息
	 * @param orderId
	 * @return
	 */
	public MemOrder getByOrderId(String orderId){
		MemOrder order = getHashBean(new MemOrder(orderId));
		if(null !=order)
			return order;
		order = orderMapper.getOrderById(orderId);
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
	public List<MemOrderGoods> getOGListByOrderId(String orderId){
		Set<String> goodsIds = redisOperate.sdiff(RedisKeyGenerator.getMemOrderGoodsSetKey(orderId));
		List<MemOrderGoods> oList = new ArrayList<>();
		for(String goodsId:goodsIds){
			MemOrderGoods oGoods = getHashBean(new MemOrderGoods(orderId,Integer.valueOf(goodsId)));
			oList.add(oGoods);
		}
		return oList;
	}
	/**
	 * 订单确认
	 * @param orderId
	 */
	public void orderLock(MemOrder order){
		orderMapper.update(order);
		flushHashBean(order);
	}
	
	/**
	 * 插入商品
	 * @param memGoods
	 */
	public void insertGoods(MemGoods memGoods){
		memGoodsMapper.insert(memGoods);
		flushHashBean(memGoods);
	}
	
	public MemGoods getGoodsById(long goodsId){
		MemGoods goods = getHashBean(new MemGoods(goodsId));
		if(goods!=null)
			return goods;
		goods = memGoodsMapper.getGoodsById(goodsId);
		if(goods!=null)
			return goods;
		return null;
	}
	
}
