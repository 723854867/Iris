package org.Iris.app.jilu.service.realm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.service.realm.unit.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.domain.Order;
import org.Iris.app.jilu.storage.domain.OrderGoods;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemMerchantMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.OrderGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.OrderMapper;
import org.Iris.app.jilu.storage.redis.cache.OrderCache;
import org.Iris.app.jilu.storage.redis.cache.UnitCache;
import org.Iris.util.lang.DateUtils;
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
	private UnitCache unitCache;
	
	@Resource
	private OrderCache orderCache;

	/**
	 * 创建商户
	 * 
	 * @param merchant
	 * @param am
	 */
	@Transactional
	public Merchant createMerchant(MemMerchant merchant, AccountModel am) { 
		// 先插db
		memMerchantMapper.insert(merchant);
		MemAccount account = BeanCreator.newMemAccount(am, merchant.getCreated(), merchant.getMerchantId());
		memAccountMapper.insert(account);
		
		// 再更新缓存
		unitCache.flushHashBean(merchant);
		unitCache.flushHashBean(account);
		return new Merchant(merchant);
	}
	
	/**
	 * 更新商户
	 * @param merchant
	 * @return
	 */
	@Transactional
	public void updateMerchant(MemMerchant merchant) {
		merchant.setUpdated(DateUtils.currentTime());
		memMerchantMapper.update(merchant);
		unitCache.flushHashBean(merchant);
	}
	
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
			map.put(orderGoods.getOrderId()+"_"+number++, orderGoods);
		orderMapper.insert(order);
		orderGoodsMapper.batchInsert(map);
		orderCache.flushHashBean(order);
		
	}
}
