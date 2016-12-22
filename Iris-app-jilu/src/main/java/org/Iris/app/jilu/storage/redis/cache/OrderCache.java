package org.Iris.app.jilu.storage.redis.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.Merchant;
import org.Iris.app.jilu.storage.domain.MerchantOrder;
import org.Iris.app.jilu.storage.domain.MerchantOrderGoods;
import org.Iris.app.jilu.storage.mybatis.mapper.CfgGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MerchantOrderGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MerchantOrderMapper;
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
		List<MerchantOrderGoods> updateList = new ArrayList<MerchantOrderGoods>();
		for(MerchantOrderGoods ogs: list){
			MerchantOrderGoods mGood = getMerchantOrderGoodsById(orderId, ogs.getId());
			mGood.setCount(ogs.getCount());
			mGood.setUnitPrice(ogs.getUnitPrice());
			updateList.add(mGood);
		}
		merchantOrderGoodsMapper.batchUpdate(updateList);
		for(MerchantOrderGoods orderGoods :updateList){
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
			Long ids[] = SerializeUtil.JsonUtil.GSON.fromJson(updateGoodsList, Long[].class);
			List<Long> list = new ArrayList<Long>(Arrays.asList(ids));
			merchantOrderGoodsMapper.batchDelete(list);
			for(long id :list){
				redisOperate.del(MerchantKeyGenerator.merchantOrderGoodsDataKey(order.getOrderId(), id));
			}
		}
		flushHashBean(order);
	}
	/**
	 * 根据商户号和订单号获取订单基本信息
	 * @param orderId
	 * @return
	 */
	public MerchantOrder getMerchantOrderById(long merchatId,String orderId){
		MerchantOrder order = getHashBean(new MerchantOrder(merchatId,orderId));
		if(null !=order)
			return order;
		order = merchantOrderMapper.getOrderById(merchatId,orderId);
		if (null == order)
			return null;
		flushHashBean(order);
		return order;
	}
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	public List<MerchantOrderGoods> getOGListByOrderId(List<Long> ids){
		return merchantOrderGoodsMapper.getMerchantOrderGoodsByIdList(ids);
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
	 * 转单
	 * @param superOrder 父订单
	 * @param merchant 转单商户对象
	 * @param ogs 转单产品列表
	 */
	@Transactional
	public MerchantOrder orderChange(MerchantOrder superOrder,Merchant merchant,List<MerchantOrderGoods> ogs){
		MerchantOrder order = superOrder;
		order.setOrderId(System.currentTimeMillis()+""+new Random().nextInt(10));
		int time = DateUtils.currentTime();
		order.setCreated(time);
		order.setUpdated(time);
		order.setMerchantId(merchant.getMerchantId());
		order.setMerchantName(merchant.getName());
		order.setMerchantAddress(merchant.getAddress());
		order.setSuperOrderId(superOrder.getOrderId());
		order.setRootOrderId(superOrder.getRootOrderId());
		order.setStatus(2);
		merchantOrderMapper.insert(order);
		//处理产品列表
		for(MerchantOrderGoods goods : ogs){
			goods.setStatus(2);
			goods.setUpdated(DateUtils.currentTime());
		}
		merchantOrderGoodsMapper.batchUpdate(ogs);
		flushHashBean(order);
		batchFlushHashBean(ogs);
		return order;
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
		if(null == merchantOrderGoods)
			return null;
		flushHashBean(merchantOrderGoods);
		return merchantOrderGoods;
	}
	
	public CfgGoods getGoodsById(long goodsId){
		CfgGoods goods = getHashBean(new CfgGoods(goodsId));
		if(goods!=null)
			return goods;
		goods = cfgGoodsMapper.getGoodsById(goodsId);
		if(null == goods)
			return null;
		flushHashBean(goods);
		return goods;
	}
	/**
	 * 判断订单列表是否可以进行转单
	 * @param list
	 * @return
	 */
	public boolean isChangedMerchantOrderGoods(List<MerchantOrderGoods> list){
		for(MerchantOrderGoods orderGoods : list){
			if(orderGoods.getStatus() != 0)
				return false;
		}
		return true;
	}
	
}
