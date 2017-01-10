package org.Iris.app.jilu.service.realm.merchant;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.bean.enums.JiLuLuaCommand;
import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.domain.MemOrderGoods;
import org.Iris.app.jilu.storage.domain.MemOrderPacket;
import org.Iris.app.jilu.storage.domain.MemOrderStatus;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemMerchantMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderPacketMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderStatusMapper;
import org.Iris.app.jilu.storage.redis.JiLuLuaOperate;
import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.app.jilu.storage.redis.RedisCache;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.SerializeUtil;
import org.Iris.util.lang.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MerchantService extends RedisCache {
	
	@Resource
	private JiLuLuaOperate luaOperate;
	@Resource
	private MemAccountMapper memAccountMapper;
	@Resource
	private MemMerchantMapper memMerchantMapper;
	@Resource
	private MemOrderMapper memOrderMapper;
	@Resource
	private MemOrderGoodsMapper memOrderGoodsMapper;
	@Resource
	private MemOrderPacketMapper memOrderPacketMapper;
	@Resource
	private MemOrderStatusMapper memOrderStatusMapper;
	
	/**
	 * 通过账号获取商户
	 * 
	 * @param type
	 * @param account
	 * @return
	 */
	public Merchant getMerchantByAccount(AccountType type, String account) { 
		String key = MerchantKeyGenerator.accountMerchantMapKey(type);
		String val = redisOperate.hget(key, account);
		if (null == val) {
			MemAccount memAccount = memAccountMapper.getByAccount(account);
			if (null == memAccount)
				return null;
			val = String.valueOf(memAccount.getMerchantId());
			// 添加 account - merchantId 映射和  merchant - account 映射
			luaOperate.evalLua(JiLuLuaCommand.ACCOUNT_REFRESH.name(), 2, 
					key, MerchantKeyGenerator.accountDataKey(memAccount.getMerchantId()), 
					account, val, SerializeUtil.JsonUtil.GSON.toJson(memAccount));
		}
		return getMerchantById(Long.valueOf(val));
	}
	
	/**
	 * 通过商户 ID 获取商户
	 * 
	 * @param merchantId
	 * @return
	 */
	public Merchant getMerchantById(long merchantId) { 
		MemMerchant memMerchant = getHashBean(new MemMerchant(merchantId));
		if (null == memMerchant) {
			memMerchant = memMerchantMapper.getByMerchantId(merchantId);
			if (null == memMerchant)
				return null;
			flushHashBean(memMerchant);
		}
		return new Merchant(memMerchant);
	}
	
	/**
	 * 创建商户
	 * 
	 * @param token
	 * @return
	 */
	@Transactional
	public Merchant createMerchant(AccountModel am, String name, String address) {
		AccountType type = AccountType.match(am.getType());
		MemMerchant memMerchant = BeanCreator.newMemMerchant(name, address);
		memMerchantMapper.insert(memMerchant);
		MemAccount memAccount = BeanCreator.newMemAccount(type, am.getAccount(), memMerchant.getCreated(), memMerchant.getMerchantId());
		memAccountMapper.insert(memAccount);

		// 更新缓存
		flushHashBean(memMerchant);
		luaOperate.evalLua(JiLuLuaCommand.ACCOUNT_REFRESH.name(), 2, 
				MerchantKeyGenerator.accountMerchantMapKey(type), 
				MerchantKeyGenerator.accountDataKey(memAccount.getMerchantId()), 
				String.valueOf(memMerchant.getMerchantId()), 
				SerializeUtil.JsonUtil.GSON.toJson(memAccount));
//		aliyunService.createMerchantFolder(merchant);     看客户端 sts 接上之后是否可以自己直接创建商户的文件夹
		return new Merchant(memMerchant);
	}
	
	/**
	 * 通过 token 获取商户 ID
	 * 
	 * @param token
	 * @return
	 */
	public String getMerchantIdByToken(String token) {
		return redisOperate.hget(MerchantKeyGenerator.tokenMerchantMapKey(), token);
	}
	
	/**
	 * 创建订单
	 * 
	 * @throws Exception
	 */
	@Transactional
	public String createOrder(MemCustomer customer,List<MemOrderGoods> list,Merchant merchant) {
		String orderId = System.currentTimeMillis()+""+new Random().nextInt(10);
		for(MemOrderGoods ogs: list){
			CfgGoods goods = merchant.getGoodsById(ogs.getGoodsId());
			if(goods == null)
				return Result.jsonError(JiLuCode.GOODS_NOT_EXIST.constId(), MessageFormat.format(JiLuCode.GOODS_NOT_EXIST.defaultValue(), ogs.getGoodsId()));
			ogs.setOrderId(orderId);
			ogs.setGoodsName(goods.getZhName());
			ogs.setStatus(0);
			int time = DateUtils.currentTime();
			ogs.setCreated(time);
			ogs.setUpdated(time);
		}
		MemMerchant memMerchant = merchant.getMemMerchant();
		MemOrder order = BeanCreator.newMemOrder(orderId, memMerchant.getMerchantId(), memMerchant.getName(),memMerchant.getAddress(),
				customer.getCustomerId(), customer.getName(), customer.getMobile(), customer.getAddress(),0);
		
		memOrderMapper.insert(order);
		memOrderGoodsMapper.batchInsert(list);
		//保存订单状态信息
		MemOrderStatus status = new MemOrderStatus(orderId,list.size());
		memOrderStatusMapper.insert(status);
		
		redisOperate.hmset(order.redisKey(), order);
		redisOperate.batchHmset(list);
		redisOperate.hmset(MerchantKeyGenerator.merchantOrderStatusDataKey(orderId), new MemOrderStatus(orderId,list.size()));
		
		return Result.jsonSuccess(order);
	}
	
	/**
	 * 更新订单
	 * 
	 * @param order
	 * @param addGoodsList
	 * @param updateGoodsList
	 * @param deleteGoodsList
	 */
	@Transactional
	public void updateOrder(MemOrder order,List<MemOrderGoods> addGoodsList,List<MemOrderGoods> updateGoodsList,List<MemOrderGoods> deleteGoodsList,Merchant merchant){
		int sum = 0;//记录产品数量的改变
		if(addGoodsList!=null){
			memOrderGoodsMapper.batchInsert(addGoodsList);
			sum+=addGoodsList.size();
		}
		if (updateGoodsList != null) {
			memOrderGoodsMapper.batchUpdate(updateGoodsList);
		}
		if (deleteGoodsList != null) {
			memOrderGoodsMapper.batchDelete(deleteGoodsList);
			sum-=addGoodsList.size();
		}
		//更新订单状态表 mem_order_status
		MemOrderStatus status = merchant.getMemOrderStatusByOrderId(order.getOrderId());
		status.setGoodsCount(status.getGoodsCount()+sum);
		memOrderStatusMapper.update(status);
		redisOperate.hmset(status.redisKey(), status);
		
		if(addGoodsList!=null){
			redisOperate.batchHmset(addGoodsList);
		}
		if (updateGoodsList != null) {
			redisOperate.batchHmset(updateGoodsList);
		}
		if (deleteGoodsList != null) {
			redisOperate.batchDelete(deleteGoodsList);
		}
	}
	
	
	/**
	 * 转单
	 * 
	 * @param superOrder
	 *            父订单
	 * @param merchant
	 *            转单商户对象
	 * @param ogs
	 *            转单产品列表
	 */
	@Transactional
	public MemOrder orderChange(MemOrder superOrder,MemMerchant memMerchant,List<MemOrderGoods> ogs,Merchant merchant){
		superOrder.setSuperOrderId(superOrder.getOrderId());
		superOrder.setOrderId(System.currentTimeMillis() + "" + new Random().nextInt(10));
		int time = DateUtils.currentTime();
		superOrder.setCreated(time);
		superOrder.setUpdated(time);
		superOrder.setSuperMerchantId(merchant.getMemMerchant().getMerchantId());
		superOrder.setSuperMerchantName(merchant.getMemMerchant().getName());
		superOrder.setMerchantId(memMerchant.getMerchantId());
		superOrder.setMerchantName(memMerchant.getName());
		superOrder.setMerchantAddress(memMerchant.getAddress());
		superOrder.setStatus(2);
		//处理产品列表
		for(MemOrderGoods goods : ogs){
			goods.setStatus(1);
			goods.setUpdated(DateUtils.currentTime());
		}
		List<MemOrderGoods> addGoods = ogs;
		for(MemOrderGoods goods : addGoods){
			goods.setOrderId(superOrder.getOrderId());
			goods.setStatus(2);
			time = DateUtils.currentTime();
			goods.setUpdated(time);
			goods.setCreated(time);
		}
		memOrderMapper.insert(superOrder);
		memOrderGoodsMapper.batchUpdate(ogs);
		memOrderGoodsMapper.batchInsert(addGoods);
		
		//更新父订单状态表mem_order_status
		MemOrderStatus status = merchant.getMemOrderStatusByOrderId(superOrder.getSuperOrderId());
		status.setTransformCount(status.getTransformCount()+ogs.size());
		memOrderStatusMapper.update(status);
		MemOrder order = merchant.getMerchantOrderById(memMerchant.getMerchantId(), superOrder.getSuperOrderId());
		if(order.getStatus() != 1){
			order.setStatus(1);
			memOrderMapper.update(order);
			redisOperate.hmset(order.redisKey(), order);
		}
		redisOperate.hmset(status.redisKey(),status);
		
		redisOperate.hmset(superOrder.redisKey(), superOrder);
		redisOperate.batchHmset(ogs);
		redisOperate.batchHmset(addGoods);
		return superOrder;
	}
	
	/**
	 * 拒绝转单操作/取消转单
	 * 
	 * @param orderId
	 */
	@Transactional
	public void refuseOrder(MemOrder order,Merchant merchant) {
		// 删除转单订单即子订单
		memOrderMapper.delete(order.getMerchantId(),order.getOrderId());
		// 删除转单产品列表
		List<MemOrderGoods> list = memOrderGoodsMapper.getChangeMerchantOrderGoodsByOrderId(order.getOrderId());
		memOrderGoodsMapper.batchDelete(list);
		// 更新父订单产品状态
		List<MemOrderGoods> superGoodsList = new ArrayList<MemOrderGoods>();
		for (MemOrderGoods merchantOrderGoods : list) {
			MemOrderGoods mOrderGoods = redisOperate.hgetAll(MerchantKeyGenerator.merchantOrderGoodsDataKey(order.getSuperOrderId(), merchantOrderGoods.getGoodsId()), new MemOrderGoods());
			mOrderGoods.setStatus(0);
			mOrderGoods.setUpdated(DateUtils.currentTime());
			superGoodsList.add(mOrderGoods);
		}
		memOrderGoodsMapper.batchUpdate(superGoodsList);
		
		/*更新父订单状态 和 订单状态表 开始*/
		MemOrderStatus status = merchant.getMemOrderStatusByOrderId(order.getSuperOrderId());
		status.setTransformCount(status.getTransformCount()-list.size());
		memOrderStatusMapper.update(status);
		redisOperate.hmset(status.redisKey(), status);
		
		int orderStatus = luaOperate.getOrderStatus(status);
		MemOrder superOrder = merchant.getMerchantOrderById(order.getSuperMerchantId(), order.getSuperOrderId());
		superOrder.setStatus(orderStatus);
		memOrderMapper.update(superOrder);
		redisOperate.hmset(superOrder.redisKey(), superOrder);
		/*更新父订单状态 和 订单状态表 结束*/
		
		// 更新缓存
		redisOperate.del(order.redisKey());
		for (MemOrderGoods goods : list) {
			redisOperate.del(MerchantKeyGenerator.merchantOrderGoodsDataKey(order.getOrderId(), goods.getGoodsId()));
		}
		redisOperate.batchHmset(superGoodsList);
	}
	
	/**
	 * 接收转单
	 * 
	 * @param changeOrderList
	 * @param orderId
	 * @param superOrderId
	 * @param merchantId
	 */
	@Transactional
	public void receiveOrder(List<MemOrderGoods> receiveGoodsList, String orderId, String superOrderId,
			long merchantId,Merchant merchant) {
		// 更新子订单状态
		MemOrder order = redisOperate.hgetAll(MerchantKeyGenerator.merchantOrderDataKey(merchantId, orderId), new MemOrder());
		order.setStatus(0);
		order.setUpdated(DateUtils.currentTime());
		memOrderMapper.update(order);
		// 查找本次转单申请的所有产品
		List<MemOrderGoods> list = memOrderGoodsMapper.getChangeMerchantOrderGoodsByOrderId(orderId);
		List<Long> recevieGoodsIds = new ArrayList<Long>();
		for (MemOrderGoods goods : receiveGoodsList)
			recevieGoodsIds.add(goods.getGoodsId());

		Iterator<MemOrderGoods> it = list.iterator();
		while (it.hasNext()) {
			MemOrderGoods goods = it.next();
			if (recevieGoodsIds.contains(goods.getGoodsId()))
				it.remove();
		}
		if (null != list && list.size() > 0)
			memOrderGoodsMapper.batchDelete(list);
		memOrderGoodsMapper.batchUpdate(receiveGoodsList);
		// 更新父订单产品状态
		List<MemOrderGoods> superRecevieGoodsList = new ArrayList<MemOrderGoods>();
		for (MemOrderGoods merchantOrderGoods : receiveGoodsList) {
			MemOrderGoods mOrderGoods = redisOperate.hgetAll(MerchantKeyGenerator.merchantOrderGoodsDataKey(superOrderId, merchantOrderGoods.getGoodsId()), new MemOrderGoods());
			mOrderGoods.setStatus(7);
			mOrderGoods.setUpdated(DateUtils.currentTime());
			superRecevieGoodsList.add(mOrderGoods);
		}
		List<MemOrderGoods> superRefuseGoodsList = new ArrayList<MemOrderGoods>();
		if (null != list && list.size() > 0) {
			for (MemOrderGoods merchantOrderGoods : list) {
				MemOrderGoods mOrderGoods = redisOperate.hgetAll(MerchantKeyGenerator.merchantOrderGoodsDataKey(superOrderId, merchantOrderGoods.getGoodsId()), new MemOrderGoods());
				mOrderGoods.setStatus(0);
				mOrderGoods.setUpdated(DateUtils.currentTime());
				superRefuseGoodsList.add(mOrderGoods);
			}
			memOrderGoodsMapper.batchUpdate(superRefuseGoodsList);
		}
		memOrderGoodsMapper.batchUpdate(superRecevieGoodsList);

		/*更新父订单状态 和 订单状态表 开始*/
		MemOrderStatus status = merchant.getMemOrderStatusByOrderId(superOrderId);
		status.setTransformSuccessCount(status.getTransformSuccessCount()+receiveGoodsList.size());
		status.setTransformCount(status.getTransformCount()-list.size());
		memOrderStatusMapper.update(status);
		redisOperate.hmset(status.redisKey(), status);
		
		int orderStatus = luaOperate.getOrderStatus(status);
		MemOrder superOrder = merchant.getMerchantOrderById(order.getSuperMerchantId(), superOrderId);
		superOrder.setStatus(orderStatus);
		memOrderMapper.update(superOrder);
		redisOperate.hmset(superOrder.redisKey(), superOrder);
		/*更新父订单状态 和 订单状态表 结束*/
		
		// 更新缓存
		redisOperate.hmset(order.redisKey(), order);
		if (null != list && list.size() > 0) {
			for (MemOrderGoods goods : list) {
				redisOperate.del(MerchantKeyGenerator.merchantOrderGoodsDataKey(orderId, goods.getGoodsId()));
			}
			redisOperate.batchHmset(superRefuseGoodsList);
		}
		redisOperate.batchHmset(receiveGoodsList);
		redisOperate.batchHmset(superRecevieGoodsList);
	}
	
	/**
	 * 分包
	 * @param orderId
	 * @param goodsList 1:2;3:4
	 * @return
	 */
	@Transactional
	public String orderPacket(String orderId,String packetGoodsList,Merchant merchant){
		String[] packetGoods = packetGoodsList.split(";");
		List<MemOrderPacket> packetList = new ArrayList<MemOrderPacket>();
		List<MemOrderGoods> orderGoodsList = new ArrayList<MemOrderGoods>();
		for(String str : packetGoods){
			String[] goods = str.split(":");
			for(String goodsId : goods){
				MemOrderGoods mGoods = merchant.getMerchantOrderGoodsById(orderId, Long.valueOf(goodsId));
				if (mGoods == null)
					return Result.jsonError(JiLuCode.GOODS_NOT_EXIST.constId(), MessageFormat.format(JiLuCode.GOODS_NOT_EXIST.defaultValue(), goodsId));
				if(mGoods.getStatus()!=0)
					return Result.jsonError(JiLuCode.ORDER_GOODS_IS_LOCK.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_IS_LOCK.defaultValue(), goodsId));
				String packetId = "p_"+System.currentTimeMillis() + "" + new Random().nextInt(10);
				MemOrderPacket packet = BeanCreator.newMemOrderPacket(packetId, orderId,merchant.getMemMerchant().getMerchantId());
				packetList.add(packet);
				mGoods.setStatus(4);
				int time = DateUtils.currentTime();
				mGoods.setUpdated(time);
				mGoods.setPacketId(packetId);
				orderGoodsList.add(mGoods);
			}
		}
		memOrderPacketMapper.batchInsert(packetList);
		memOrderGoodsMapper.batchUpdate(orderGoodsList);
		
		/*更新父订单状态 和 订单状态表 开始*/
		MemOrderStatus status = merchant.getMemOrderStatusByOrderId(orderId);
		status.setPacketCount(status.getPacketCount()+packetGoods.length);
		memOrderStatusMapper.update(status);
		redisOperate.hmset(status.redisKey(), status);
		
		int orderStatus = luaOperate.getOrderStatus(status);
		MemOrder superOrder = merchant.getMerchantOrderById(merchant.getMemMerchant().getMerchantId(), orderId);
		superOrder.setStatus(orderStatus);
		memOrderMapper.update(superOrder);
		redisOperate.hmset(superOrder.redisKey(), superOrder);
		/*更新父订单状态 和 订单状态表 结束*/
		
		redisOperate.batchHmset(packetList);
		redisOperate.batchHmset(orderGoodsList);
		return Result.jsonSuccess();
	}
	
	/**
	 * 添加快递单
	 * @param packetId
	 * @param expressCode 快递号
	 * @return
	 */
	@Transactional
	public String addExpress(String packetId,String expressCode,Merchant merchant){
		MemOrderPacket packet = merchant.getMemOrderPacket(packetId);
		if(packet == null)
			return Result.jsonError(JiLuCode.PACKET_NOT_EXIST.constId(), MessageFormat.format(JiLuCode.PACKET_NOT_EXIST.defaultValue(), packetId));
		packet.setExpressCode(expressCode);
		packet.setUpdated(DateUtils.currentTime());
		memOrderPacketMapper.update(packet);
		
		
		
		redisOperate.hmset(packet.redisKey(), packet);
		return Result.jsonSuccess(packet);
	}
}
