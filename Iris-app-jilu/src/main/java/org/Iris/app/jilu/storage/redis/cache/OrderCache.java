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
import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
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
		merchantOrderGoodsMapper.batchInsert(list);
		for(MerchantOrderGoods orderGoods :list){
			flushHashBean(orderGoods);
		}
	}
	
	private void batchUpdateOrderGoodsByList(String orderId, List<MerchantOrderGoods> list) {
		for(MerchantOrderGoods ogs: list){
			int count = ogs.getCount();
			String unitPrice = ogs.getUnitPrice();
			ogs = getMerchantOrderGoodsById(orderId, ogs.getId());
			ogs.setCount(count);
			ogs.setUnitPrice(unitPrice);
		}
		merchantOrderGoodsMapper.batchUpdate(list);
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
		if(addGoodsList!=null && !"".equals(addGoodsList)){
			MerchantOrderGoods addOrderGoods[] = SerializeUtil.JsonUtil.GSON.fromJson(addGoodsList, MerchantOrderGoods[].class);
			List<MerchantOrderGoods> list = new ArrayList<MerchantOrderGoods>(Arrays.asList(addOrderGoods));
			batchInsertOrderGoodsByList(order.getOrderId(), list);
		}
		if(updateGoodsList!=null && !"".equals(updateGoodsList)){
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
				redisOperate.del(MerchantKeyGenerator.merchantOrderGoodsDataKey(order.getOrderId(), goodsId));
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
		return null;
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
	
	public MerchantOrderGoods getMerchantOrderGoodsById(String orderId,long id){
		MerchantOrderGoods merchantOrderGoods = getHashBean(new MerchantOrderGoods(orderId, id));
		if(merchantOrderGoods != null)
			return merchantOrderGoods;
		merchantOrderGoods = merchantOrderGoodsMapper.getMerchantOrderGoodsById(id);
		return merchantOrderGoods;
	}
	
	public CfgGoods getGoodsById(long goodsId){
		CfgGoods goods = getHashBean(new CfgGoods(goodsId));
		if(goods!=null)
			return goods;
		goods = cfgGoodsMapper.getGoodsById(goodsId);
		return goods;
	}
	
}
