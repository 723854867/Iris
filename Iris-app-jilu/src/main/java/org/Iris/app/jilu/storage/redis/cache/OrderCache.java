package org.Iris.app.jilu.storage.redis.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.bean.model.OrderChangeModel;
import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.Merchant;
import org.Iris.app.jilu.storage.domain.MerchantOrder;
import org.Iris.app.jilu.storage.domain.MerchantOrderGoods;
import org.Iris.app.jilu.storage.mybatis.mapper.CfgGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MerchantOrderGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MerchantOrderMapper;
import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
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
	public void createOrder(MerchantOrder order,List<MerchantOrderGoods> list){
		merchantOrderMapper.insert(order);
		merchantOrderGoodsMapper.batchInsert(list);
		batchFlushHashBean(list);
		flushHashBean(order);
	}
	
	/**
	 * 更新订单
	 * @param order
	 * @param addGoodsList
	 * @param updateGoodsList
	 * @param deleteGoodsList
	 */
	@Transactional
	public void updateOrder(MerchantOrder order,List<MerchantOrderGoods> addGoodsList,List<MerchantOrderGoods> updateGoodsList,List<MerchantOrderGoods> deleteGoodsList){
		if(addGoodsList!=null){
			merchantOrderGoodsMapper.batchInsert(addGoodsList);
			batchFlushHashBean(addGoodsList);
		}
		if(updateGoodsList!=null){
			merchantOrderGoodsMapper.batchUpdate(updateGoodsList);
			batchFlushHashBean(updateGoodsList);
		}
		if(deleteGoodsList!=null){
			merchantOrderGoodsMapper.batchDelete(deleteGoodsList);
			for(MerchantOrderGoods goods :deleteGoodsList){
				redisOperate.del(MerchantKeyGenerator.merchantOrderGoodsDataKey(order.getOrderId(), goods.getGoodsId()));
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
	 * 获取MerchantOrderGoods列表 通过List<MerchantOrderGoods>
	 * @param ids
	 * @return
	 */
	public List<MerchantOrderGoods> getOGListByMerchantOrderGoodsList(List<MerchantOrderGoods> list){
		return merchantOrderGoodsMapper.getMerchantOrderGoodsByList(list);
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
	public MerchantOrder orderChange(MerchantOrder superOrder,Merchant merchant,Merchant superMerchant,List<MerchantOrderGoods> ogs){
		superOrder.setSuperOrderId(superOrder.getOrderId());
		superOrder.setOrderId(System.currentTimeMillis()+""+new Random().nextInt(10));
		int time = DateUtils.currentTime();
		superOrder.setCreated(time);
		superOrder.setUpdated(time);
		superOrder.setSuperMerchantId(superMerchant.getMerchantId());
		superOrder.setSuperMerchantName(superMerchant.getName());
		superOrder.setMerchantId(merchant.getMerchantId());
		superOrder.setMerchantName(merchant.getName());
		superOrder.setMerchantAddress(merchant.getAddress());
		superOrder.setStatus(2);
		merchantOrderMapper.insert(superOrder);
		//处理产品列表
		for(MerchantOrderGoods goods : ogs){
			goods.setStatus(2);
			goods.setUpdated(DateUtils.currentTime());
		}
		merchantOrderGoodsMapper.batchUpdate(ogs);
		batchFlushHashBean(ogs);
		for(MerchantOrderGoods goods : ogs){
			goods.setOrderId(superOrder.getOrderId());
			goods.setUpdated(DateUtils.currentTime());
			goods.setCreated(DateUtils.currentTime());
		}
		merchantOrderGoodsMapper.batchInsert(ogs);
		flushHashBean(superOrder);
		batchFlushHashBean(ogs);
		return superOrder;
	}
	
	/**
	 * 插入商品
	 * @param memGoods
	 */
	public void insertGoods(CfgGoods memGoods){
		cfgGoodsMapper.insert(memGoods);
		flushHashBean(memGoods);
	}
	
	public MerchantOrderGoods getMerchantOrderGoodsById(String orderId,long goodsId){
		MerchantOrderGoods merchantOrderGoods = getHashBean(new MerchantOrderGoods(orderId, goodsId));
		if(merchantOrderGoods != null)
			return merchantOrderGoods;
		merchantOrderGoods = merchantOrderGoodsMapper.getMerchantOrderGoodsByOrderId(orderId, goodsId);
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
	 * 判断订单列表是否可以进行转单或者修改
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
	/**
	 * 获取转单订单列表
	 * @param merchantId
	 * @return
	 */
	public List<MerchantOrder> getChangeOrderListByMerchantId(long merchantId){
		return merchantOrderMapper.getChangeMerchantOrderList(merchantId);
	}
	
	public List<OrderChangeModel> getOrderChangeListModelList(List<MerchantOrder> mList){
		List<OrderChangeModel> orderChangeModels = new ArrayList<>();
		for(MerchantOrder order : mList){
			List<MerchantOrderGoods> list = merchantOrderGoodsMapper.getChangeMerchantOrderGoodsByOrderId(order.getOrderId());
			orderChangeModels.add(new OrderChangeModel(order.getSuperMerchantName(), list));
		}
		return orderChangeModels;
	}
}
