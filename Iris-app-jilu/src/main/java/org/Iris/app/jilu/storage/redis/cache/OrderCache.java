package org.Iris.app.jilu.storage.redis.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.MerchantOrder;
import org.Iris.app.jilu.storage.domain.MerchantOrderGoods;
import org.Iris.app.jilu.storage.mybatis.mapper.CfgGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MerchantOrderGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MerchantOrderMapper;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.util.common.SerializeUtil;
import org.Iris.util.lang.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderCache extends RedisCache {

	@Resource
	private CfgGoodsMapper cfgGoodsMapper;
	@Resource
	private MerchantOrderMapper merchantOrderMapper;
	@Resource
	private MerchantOrderGoodsMapper merchantOrderGoodsMapper;

	/**
	 * 创建订单
	 * @throws Exception 
	 */
	@Transactional
	public void createOrder(MerchantOrder order,MerchantOrderGoods[] orderGoods){
		merchantOrderMapper.insert(order);
		List<MerchantOrderGoods> list = new ArrayList<MerchantOrderGoods>(Arrays.asList(orderGoods));
		batchInsertOrderGoodsByList(order.getOrderId(), list);
		flushHashBean(order);
	}

	private void batchInsertOrderGoodsByList(String orderId, List<MerchantOrderGoods> list) {
		for(MerchantOrderGoods ogs: list){
			CfgGoods goods = getGoodsById(ogs.getGoodsId());
			ogs.setOrderId(orderId);
			ogs.setGoodsName(goods.getZhName());
			ogs.setStatus(0);
			int time = DateUtils.currentTime();
			ogs.setCreated(time);
			ogs.setUpdated(time);
		}
		Map<String, List<MerchantOrderGoods>> map = new HashMap<String, List<MerchantOrderGoods>>();
		map.put("list", list);
		merchantOrderGoodsMapper.batchInsert(map);
		for(MerchantOrderGoods orderGoods :list){
			redisOperate.sadd(CommonKeyGenerator.getMemOrderGoodsSetKey(orderId),String.valueOf(orderGoods.getGoodsId()));
			flushHashBean(orderGoods);
		}
	}
	
	private void batchUpdateOrderGoodsByList(String orderId, List<MerchantOrderGoods> list) {
		for(MerchantOrderGoods ogs: list){
			ogs = getHashBean(new MerchantOrderGoods(orderId, ogs.getGoodsId()));
			ogs.setCount(ogs.getCount());
			ogs.setUnitPrice(ogs.getUnitPrice());
			
		}
		Map<String, List<MerchantOrderGoods>> map = new HashMap<String, List<MerchantOrderGoods>>();
		map.put("list", list);
		merchantOrderGoodsMapper.batchUpdate(map);
		for(MerchantOrderGoods orderGoods :list){
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
	public void updateOrder(MerchantOrder order,String addGoodsList,String updateGoodsList,String deleteGoodsList){
		merchantOrderMapper.update(order);
		if(addGoodsList!=null){
			MerchantOrderGoods addOrderGoods[] = SerializeUtil.JsonUtil.GSON.fromJson(addGoodsList, MerchantOrderGoods[].class);
			List<MerchantOrderGoods> list = new ArrayList<MerchantOrderGoods>(Arrays.asList(addOrderGoods));
			batchInsertOrderGoodsByList(order.getOrderId(), list);
		}
		if(updateGoodsList!=null){
			MerchantOrderGoods updateOrderGoods[] = SerializeUtil.JsonUtil.GSON.fromJson(updateGoodsList, MerchantOrderGoods[].class);
			List<MerchantOrderGoods> list = new ArrayList<MerchantOrderGoods>(Arrays.asList(updateOrderGoods));
			batchUpdateOrderGoodsByList(order.getOrderId(), list);
		}
		if(deleteGoodsList!=null && !"".equals(deleteGoodsList)){
			Long goodsIds[] = SerializeUtil.JsonUtil.GSON.fromJson(updateGoodsList, Long[].class);
			List<Long> list = new ArrayList<Long>(Arrays.asList(goodsIds));
			Map<String, List<Long>> map = new HashMap<String, List<Long>>();
			map.put("list", list);
			merchantOrderGoodsMapper.batchDelete(map);
			for(long goodsId :list){
				redisOperate.del(CommonKeyGenerator.getMemOrderGoodsDataKey(order.getOrderId(), goodsId));
				redisOperate.srem(CommonKeyGenerator.getMemOrderGoodsSetKey(order.getOrderId()),goodsId+"");
			}
		}
		flushHashBean(order);
	}
	/**
	 * 根据订单号获取订单基本信息
	 * @param orderId
	 * @return
	 */
	public MerchantOrder getByOrderId(String orderId){
		MerchantOrder order = getHashBean(new MerchantOrder(orderId));
		if(null !=order)
			return order;
		order = merchantOrderMapper.getOrderById(orderId);
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
	public List<MerchantOrderGoods> getOGListByOrderId(String orderId){
		Set<String> goodsIds = redisOperate.sdiff(CommonKeyGenerator.getMemOrderGoodsSetKey(orderId));
		List<MerchantOrderGoods> oList = new ArrayList<>();
		for(String goodsId:goodsIds){
			MerchantOrderGoods oGoods = getHashBean(new MerchantOrderGoods(orderId,Integer.valueOf(goodsId)));
			oList.add(oGoods);
		}
		return oList;
	}
	/**
	 * 订单确认
	 * @param orderId
	 */
	public void orderLock(MerchantOrder order){
		merchantOrderMapper.update(order);
		flushHashBean(order);
	}
	
	/**
	 * 插入商品
	 * @param memGoods
	 */
	public void insertGoods(CfgGoods memGoods){
		cfgGoodsMapper.insert(memGoods);
		flushHashBean(memGoods);
	}
	
	public CfgGoods getGoodsById(long goodsId){
		CfgGoods goods = getHashBean(new CfgGoods(goodsId));
		if(goods!=null)
			return goods;
		goods = cfgGoodsMapper.getGoodsById(goodsId);
		if(goods!=null)
			return goods;
		return null;
	}
	
}
