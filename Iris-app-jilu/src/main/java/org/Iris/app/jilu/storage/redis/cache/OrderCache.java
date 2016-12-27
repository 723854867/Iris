package org.Iris.app.jilu.storage.redis.cache;

import javax.annotation.Resource;

import org.Iris.app.jilu.storage.mybatis.mapper.CfgGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderCache extends RedisCache {

	@Resource
	private CfgGoodsMapper cfgGoodsMapper;
	@Resource
	private MemOrderMapper memOrderMapper;
	@Resource
	private MemOrderGoodsMapper memOrderGoodsMapper;

//	/**
//	 * 创建订单
//	 * 
//	 * @throws Exception
//	 */
//	@Transactional
//	public void createOrder(MemOrder order,List<MemOrderGoods> list) {
//		merchantOrderMapper.insert(order);
//		merchantOrderGoodsMapper.batchInsert(list);
//		batchFlushHashBean(list);
//		flushHashBean(order);
//	}
//
//	/**
//	 * 更新订单
//	 * 
//	 * @param order
//	 * @param addGoodsList
//	 * @param updateGoodsList
//	 * @param deleteGoodsList
//	 */
//	@Transactional
//	public void updateOrder(MemOrder order,List<MemOrderGoods> addGoodsList,List<MemOrderGoods> updateGoodsList,List<MemOrderGoods> deleteGoodsList){
//		if(addGoodsList!=null){
//			merchantOrderGoodsMapper.batchInsert(addGoodsList);
//			batchFlushHashBean(addGoodsList);
//		}
//		if (updateGoodsList != null) {
//			merchantOrderGoodsMapper.batchUpdate(updateGoodsList);
//			batchFlushHashBean(updateGoodsList);
//		}
//		if (deleteGoodsList != null) {
//			merchantOrderGoodsMapper.batchDelete(deleteGoodsList);
//			for(MemOrderGoods goods :deleteGoodsList){
//				redisOperate.del(MerchantKeyGenerator.merchantOrderGoodsDataKey(order.getOrderId(), goods.getGoodsId()));
//			}
//		}
//		flushHashBean(order);
//	}
//
//	/**
//	 * 根据商户号和订单号获取订单基本信息
//	 * 
//	 * @param orderId
//	 * @return
//	 */
//	public MemOrder getMerchantOrderById(long merchatId,String orderId){
//		MemOrder order = getHashBean(new MemOrder(merchatId,orderId));
//		if(null !=order)
//			return order;
//		order = merchantOrderMapper.getOrderById(merchatId, orderId);
//		if (null == order)
//			return null;
//		flushHashBean(order);
//		return order;
//	}
//
//	/**
//	 * 获取MerchantOrderGoods列表 通过List<MerchantOrderGoods>
//	 * 
//	 * @param ids
//	 * @return
//	 */
//	public List<MemOrderGoods> getOGListByMerchantOrderGoodsList(List<MemOrderGoods> list){
//		return merchantOrderGoodsMapper.getMerchantOrderGoodsByList(list);
//	}
//
//	/**
//	 * 订单确认
//	 * 
//	 * @param orderId
//	 */
//	public void orderLock(MemOrder order){
//		merchantOrderMapper.update(order);
//		flushHashBean(order);
//	}
//
//	/**
//	 * 转单
//	 * 
//	 * @param superOrder
//	 *            父订单
//	 * @param merchant
//	 *            转单商户对象
//	 * @param ogs
//	 *            转单产品列表
//	 */
//	@Transactional
//	public MemOrder orderChange(MemOrder superOrder,MemMerchant merchant,MemMerchant superMerchant,List<MemOrderGoods> ogs){
//		superOrder.setSuperOrderId(superOrder.getOrderId());
//		superOrder.setOrderId(System.currentTimeMillis() + "" + new Random().nextInt(10));
//		int time = DateUtils.currentTime();
//		superOrder.setCreated(time);
//		superOrder.setUpdated(time);
//		superOrder.setSuperMerchantId(superMerchant.getMerchantId());
//		superOrder.setSuperMerchantName(superMerchant.getName());
//		superOrder.setMerchantId(merchant.getMerchantId());
//		superOrder.setMerchantName(merchant.getName());
//		superOrder.setMerchantAddress(merchant.getAddress());
//		superOrder.setStatus(2);
//		merchantOrderMapper.insert(superOrder);
//		//处理产品列表
//		for(MemOrderGoods goods : ogs){
//			goods.setStatus(2);
//			goods.setUpdated(DateUtils.currentTime());
//		}
//		merchantOrderGoodsMapper.batchUpdate(ogs);
//		batchFlushHashBean(ogs);
//		for(MemOrderGoods goods : ogs){
//			goods.setOrderId(superOrder.getOrderId());
//			goods.setUpdated(DateUtils.currentTime());
//			goods.setCreated(DateUtils.currentTime());
//		}
//		merchantOrderGoodsMapper.batchInsert(ogs);
//		flushHashBean(superOrder);
//		batchFlushHashBean(ogs);
//		return superOrder;
//	}
//
//	/**
//	 * 插入商品
//	 * 
//	 * @param memGoods
//	 */
//	public void insertGoods(CfgGoods memGoods) {
//		cfgGoodsMapper.insert(memGoods);
//		flushHashBean(memGoods);
//	}
//	public MemOrderGoods getMerchantOrderGoodsById(String orderId,long goodsId){
//		MemOrderGoods merchantOrderGoods = getHashBean(new MemOrderGoods(orderId, goodsId));
//		if(merchantOrderGoods != null)
//			return merchantOrderGoods;
//		merchantOrderGoods = merchantOrderGoodsMapper.getMerchantOrderGoodsByOrderId(orderId, goodsId);
//		if (null == merchantOrderGoods)
//			return null;
//		flushHashBean(merchantOrderGoods);
//		return merchantOrderGoods;
//	}
//
//	public CfgGoods getGoodsById(long goodsId) {
//		CfgGoods goods = getHashBean(new CfgGoods(goodsId));
//		if (goods != null)
//			return goods;
//		goods = cfgGoodsMapper.getGoodsById(goodsId);
//		if (null == goods)
//			return null;
//		flushHashBean(goods);
//		return goods;
//	}
//
//	/**
//	 * 判断订单列表是否可以进行转单或者修改
//	 * 
//	 * @param list
//	 * @return
//	 */
//	public boolean isChangedMerchantOrderGoods(List<MemOrderGoods> list){
//		for(MemOrderGoods orderGoods : list){
//			if(orderGoods.getStatus() != 0)
//				return false;
//		}
//		return true;
//	}
//
//	/**
//	 * 获取转单订单列表
//	 * 
//	 * @param merchantId
//	 * @return
//	 */
//	public List<MemOrder> getChangeOrderListByMerchantId(long merchantId){
//		return merchantOrderMapper.getChangeMerchantOrderList(merchantId);
//	}
//	
//	public List<OrderChangeModel> getOrderChangeListModelList(List<MemOrder> mList){
//		List<OrderChangeModel> orderChangeModels = new ArrayList<>();
//		for(MemOrder order : mList){
//			List<MemOrderGoods> list = merchantOrderGoodsMapper.getChangeMerchantOrderGoodsByOrderId(order.getOrderId());
//			orderChangeModels.add(new OrderChangeModel(order.getSuperMerchantName(), list));
//		}
//		return orderChangeModels;
//	}
//
//	/**
//	 * 拒绝转单操作
//	 * 
//	 * @param orderId
//	 */
//	@Transactional
//	public void refuseOrder(String orderId, String superOrderId, long merchantId) {
//		// 删除转单订单即子订单
//		merchantOrderMapper.delete(merchantId, orderId);
//		// 删除转单产品列表
//		List<MerchantOrderGoods> list = merchantOrderGoodsMapper.getChangeMerchantOrderGoodsByOrderId(orderId);
//		merchantOrderGoodsMapper.batchDelete(list);
//		// 更新父订单产品状态
//		List<MerchantOrderGoods> superGoodsList = new ArrayList<MerchantOrderGoods>();
//		for (MerchantOrderGoods merchantOrderGoods : list) {
//			MerchantOrderGoods mOrderGoods = getHashBean(
//					new MerchantOrderGoods(superOrderId, merchantOrderGoods.getGoodsId()));
//			mOrderGoods.setStatus(0);
//			mOrderGoods.setUpdated(DateUtils.currentTime());
//			superGoodsList.add(mOrderGoods);
//		}
//		merchantOrderGoodsMapper.batchUpdate(superGoodsList);
//		// 更新缓存
//		redisOperate.del(MerchantKeyGenerator.merchantOrderDataKey(merchantId, orderId));
//		for (MerchantOrderGoods goods : list) {
//			redisOperate.del(MerchantKeyGenerator.merchantOrderGoodsDataKey(orderId, goods.getGoodsId()));
//		}
//		batchFlushHashBean(superGoodsList);
//	}
//
//	/**
//	 * 接收转单
//	 * 
//	 * @param changeOrderList
//	 * @param orderId
//	 * @param superOrderId
//	 * @param merchantId
//	 */
//	@Transactional
//	public void receiveOrder(List<MerchantOrderGoods> receiveGoodsList, String orderId, String superOrderId,
//			long merchantId) {
//		// 更新子订单状态
//		MerchantOrder order = getHashBean(new MerchantOrder(merchantId, orderId));
//		order.setStatus(0);
//		order.setUpdated(DateUtils.currentTime());
//		merchantOrderMapper.update(order);
//		// 查找本次转单申请的所有产品
//		List<MerchantOrderGoods> list = merchantOrderGoodsMapper.getChangeMerchantOrderGoodsByOrderId(orderId);
//		List<Long> recevieGoodsIds = new ArrayList<Long>();
//		for (MerchantOrderGoods goods : receiveGoodsList)
//			recevieGoodsIds.add(goods.getGoodsId());
//
//		Iterator<MerchantOrderGoods> it = list.iterator();
//		while (it.hasNext()) {
//			MerchantOrderGoods goods = it.next();
//			if (recevieGoodsIds.contains(goods.getGoodsId()))
//				it.remove();
//		}
//		if (null != list && list.size() > 0)
//			merchantOrderGoodsMapper.batchDelete(list);
//		merchantOrderGoodsMapper.batchUpdate(receiveGoodsList);
//		// 更新父订单产品状态
//		List<MerchantOrderGoods> superRecevieGoodsList = new ArrayList<MerchantOrderGoods>();
//		for (MerchantOrderGoods merchantOrderGoods : receiveGoodsList) {
//			MerchantOrderGoods mOrderGoods = getHashBean(
//					new MerchantOrderGoods(superOrderId, merchantOrderGoods.getGoodsId()));
//			mOrderGoods.setStatus(3);
//			mOrderGoods.setUpdated(DateUtils.currentTime());
//			superRecevieGoodsList.add(mOrderGoods);
//		}
//		List<MerchantOrderGoods> superRefuseGoodsList = new ArrayList<MerchantOrderGoods>();
//		if (null != list && list.size() > 0) {
//			for (MerchantOrderGoods merchantOrderGoods : list) {
//				MerchantOrderGoods mOrderGoods = getHashBean(
//						new MerchantOrderGoods(superOrderId, merchantOrderGoods.getGoodsId()));
//				mOrderGoods.setStatus(0);
//				mOrderGoods.setUpdated(DateUtils.currentTime());
//				superRefuseGoodsList.add(mOrderGoods);
//			}
//			merchantOrderGoodsMapper.batchUpdate(superRefuseGoodsList);
//		}
//		merchantOrderGoodsMapper.batchUpdate(superRecevieGoodsList);
//
//		// 更新缓存
//		flushHashBean(order);
//		if (null != list && list.size() > 0) {
//			for (MerchantOrderGoods goods : list) {
//				redisOperate.del(MerchantKeyGenerator.merchantOrderGoodsDataKey(orderId, goods.getGoodsId()));
//			}
//			batchFlushHashBean(superRefuseGoodsList);
//		}
//		batchFlushHashBean(receiveGoodsList);
//		batchFlushHashBean(superRecevieGoodsList);
//	}
}
