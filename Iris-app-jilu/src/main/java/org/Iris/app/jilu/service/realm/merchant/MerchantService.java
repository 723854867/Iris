package org.Iris.app.jilu.service.realm.merchant;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.JiLuPushUtil;
import org.Iris.app.jilu.common.bean.enums.JiLuLuaCommand;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.realm.courier.CourierService;
import org.Iris.app.jilu.service.realm.igt.IgtService;
import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemGoodsStore;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.domain.MemOrderGoods;
import org.Iris.app.jilu.storage.domain.MemOrderPacket;
import org.Iris.app.jilu.storage.domain.MemOrderStatus;
import org.Iris.app.jilu.storage.mybatis.mapper.CfgGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemGoodsStoreMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemMerchantMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderPacketMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderStatusMapper;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.storage.redis.JiLuLuaOperate;
import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.app.jilu.storage.redis.RedisCache;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.JsonAppender;
import org.Iris.util.common.SerializeUtil;
import org.Iris.util.lang.DateUtils;
import org.springframework.dao.DuplicateKeyException;
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
	@Resource
	private CfgGoodsMapper cfgGoodsMapper;
	@Resource
	private MemGoodsStoreMapper memGoodsStoreMapper;
	@Resource
	private CourierService courierService;
	@Resource
	private IgtService igtService;
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
	public Merchant createMerchant(String account, AccountType type) {
		MemMerchant memMerchant = BeanCreator.newMemMerchant();
		memMerchantMapper.insert(memMerchant);
		MemAccount memAccount = BeanCreator.newMemAccount(type, account, memMerchant.getCreated(), memMerchant.getMerchantId());
		memAccountMapper.insert(memAccount);

		// 更新缓存
		flushHashBean(memMerchant);
		luaOperate.evalLua(JiLuLuaCommand.ACCOUNT_REFRESH.name(), 2, 
				MerchantKeyGenerator.accountMerchantMapKey(type), 
				MerchantKeyGenerator.accountDataKey(memAccount.getMerchantId()), 
				account, 
				String.valueOf(memMerchant.getMerchantId()),
				SerializeUtil.JsonUtil.GSON.toJson(memAccount));
//		aliyunService.createMerchantFolder(merchant);     看客户端 sts 接上之后是否可以自己直接创建商户的文件夹
		return new Merchant(memMerchant);
	}
	
	/**
	 * 手机或邮箱绑定
	 * @param account
	 */
	public String bindingPhoneOrMobile(String account,AccountType type,String captch,long merchantId){
		String accessToken = null;
		switch (type) {
		case MOBILE:
		case EMAIL:
			if (!courierService.verifyCaptch(type, account, captch))
				return Result.jsonError(JiLuCode.CAPTCHA_ERROR);
			break;
		case WECHAT:
			accessToken = redisOperate.get(CommonKeyGenerator.weiXinAccessTokenKey(account));
			if(accessToken == null){
				return Result.jsonError(JiLuCode.WEIXIN_ACCESSTOKEN_EXPAIRED);
			}else{
				if(!accessToken.equals(captch))
					return Result.jsonError(JiLuCode.ACCESSTOKEN_ERROR);
			}
			break;
		default:
			throw IllegalConstException.errorException(JiLuParams.TYPE);
		}
		Merchant merchant = getMerchantByAccount(type, account);
		if(merchant != null)
			return Result.jsonError(JiLuCode.ACCOUNT_ALREADY_BINDED.constId(),MessageFormat.format(JiLuCode.ACCOUNT_ALREADY_BINDED.defaultValue(), account));
		MemAccount memAccount = BeanCreator.newMemAccount(type, account, DateUtils.currentTime(), merchantId);
		try {
			if(memAccountMapper.getByMerchantIdAndType(merchantId, type.mark()) == null)
				memAccountMapper.insert(memAccount);
			else{
				memAccountMapper.update(memAccount);
				redisOperate.hdel(MerchantKeyGenerator.accountMerchantMapKey(type),account);
				redisOperate.hdel(MerchantKeyGenerator.accountDataKey(memAccount.getMerchantId()),account);
			}
		} catch (DuplicateKeyException e) {
			return Result.jsonError(JiLuCode.ACCOUNT_ALREADY_BINDED.constId(),MessageFormat.format(JiLuCode.ACCOUNT_ALREADY_BINDED.defaultValue(), account));
		}
		// 更新缓存
		luaOperate.evalLua(JiLuLuaCommand.ACCOUNT_REFRESH.name(), 2, 
				MerchantKeyGenerator.accountMerchantMapKey(type), 
				MerchantKeyGenerator.accountDataKey(memAccount.getMerchantId()), 
				account, 
				String.valueOf(merchantId),
				SerializeUtil.JsonUtil.GSON.toJson(memAccount));
		return Result.jsonSuccess();
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
			sum-=deleteGoodsList.size();
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
	 * @param order
	 *            转单商户订单
	 * @param merchant
	 *            转单商户对象
	 * @param ogs
	 *            转单产品列表
	 */
	@Transactional
	public MemOrder orderChange(MemOrder order,MemMerchant memMerchant,List<MemOrderGoods> ogs,Merchant merchant){
		order.setSuperOrderId(order.getOrderId());
		order.setOrderId(System.currentTimeMillis() + "" + new Random().nextInt(10));
		int time = DateUtils.currentTime();
		order.setCreated(time);
		order.setUpdated(time);
		order.setSuperMerchantId(merchant.getMemMerchant().getMerchantId());
		order.setSuperMerchantName(merchant.getMemMerchant().getName());
		order.setMerchantId(memMerchant.getMerchantId());
		order.setMerchantName(memMerchant.getName());
		order.setMerchantAddress(memMerchant.getAddress());
		order.setStatus(2);
		//处理产品列表
		for(MemOrderGoods goods : ogs){
			goods.setStatus(1);
			goods.setUpdated(DateUtils.currentTime());
		}
		List<MemOrderGoods> addGoods = new ArrayList<MemOrderGoods>();
		for(MemOrderGoods goods : ogs){
			MemOrderGoods memOrderGoods = new MemOrderGoods();
			memOrderGoods.setOrderId(order.getOrderId());
			memOrderGoods.setGoodsId(goods.getGoodsId());
			memOrderGoods.setGoodsName(goods.getGoodsName());
			memOrderGoods.setCount(goods.getCount());
			memOrderGoods.setUnitPrice(goods.getUnitPrice());
			memOrderGoods.setStatus(2);
			time = DateUtils.currentTime();
			memOrderGoods.setUpdated(time);
			memOrderGoods.setCreated(time);
			addGoods.add(memOrderGoods);
		}
		memOrderMapper.insert(order);
		memOrderGoodsMapper.batchUpdate(ogs);
		memOrderGoodsMapper.batchInsert(addGoods);
		
		/*更新父订单状态表mem_order_status start*/
		MemOrderStatus status = merchant.getMemOrderStatusByOrderId(order.getSuperOrderId());
		status.setTransformCount(status.getTransformCount()+ogs.size());
		memOrderStatusMapper.update(status);
		MemOrder superOrder = merchant.getMerchantOrderById(order.getSuperMerchantId(), order.getSuperOrderId());
		if(superOrder.getStatus() != 1){
			superOrder.setStatus(1);
			memOrderMapper.update(superOrder);
			redisOperate.hmset(superOrder.redisKey(), superOrder);
			//推送订单状态改变消息
			JiLuPushUtil.OrderStatusChangePush(merchant.getMemCid(superOrder.getMerchantId()), superOrder.getOrderId(), "", 1);
			JiLuPushUtil.OrderStatusChangePush(merchant.getMemCid(superOrder.getSuperMerchantId()), 
					superOrder.getSuperOrderId(),superOrder.getOrderId(),1);
		}
		redisOperate.hmset(status.redisKey(),status);
		/*更新父订单状态表mem_order_status end*/
		
		redisOperate.hmset(order.redisKey(), order);
		redisOperate.batchHmset(ogs);
		redisOperate.batchHmset(addGoods);
		
		//推送转单信息  参数：转单方名字，转单订单号，转单时间
		JiLuPushUtil.OrderTransformPush(merchant.getMemCid(order.getMerchantId()),
				merchant.getMemMerchant().getName(), order.getOrderId(), time);
		return order;
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
		setOrderStatus(merchant, order.getSuperOrderId(), order.getSuperMerchantId(), status);
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
		//保存新建子订单状态信息
		memOrderStatusMapper.insert(new MemOrderStatus(orderId,receiveGoodsList.size()));
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
		status.setTransformCount(status.getTransformCount()-receiveGoodsList.size());
		setOrderStatus(merchant, superOrderId, order.getSuperMerchantId(), status);
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
		
		//推送转单接收信息  参数：转单单号，转单父订单号
		JiLuPushUtil.OrderReceivePush(merchant.getMemCid(order.getSuperMerchantId()), orderId, superOrderId);
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
		StringBuilder builder = new StringBuilder();
		List<MemOrderPacket> packetList = new ArrayList<MemOrderPacket>();
		List<MemOrderGoods> orderGoodsList = new ArrayList<MemOrderGoods>();
		for(String str : packetGoods){
			String packetId = "p_"+System.currentTimeMillis() + "" + new Random().nextInt(10);
			MemOrderPacket packet = BeanCreator.newMemOrderPacket(packetId, orderId,merchant.getMemMerchant().getMerchantId());
			packetList.add(packet);
			builder.append(packetId+";");
			String[] goods = str.split(":");
			for(String goodsId : goods){
				MemOrderGoods mGoods = merchant.getMerchantOrderGoodsById(orderId, Long.valueOf(goodsId));
				if (mGoods == null)
					return Result.jsonError(JiLuCode.GOODS_NOT_EXIST.constId(), MessageFormat.format(JiLuCode.GOODS_NOT_EXIST.defaultValue(), goodsId));
				if(mGoods.getStatus()!=0)
					return Result.jsonError(JiLuCode.ORDER_GOODS_IS_LOCK.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_IS_LOCK.defaultValue(), goodsId));
				mGoods.setStatus(3);
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
		setOrderStatus(merchant, orderId, merchant.getMemMerchant().getMerchantId(), status);
		/*更新父订单状态 和 订单状态表 结束*/
		
		redisOperate.batchHmset(packetList);
		redisOperate.batchHmset(orderGoodsList);
		return Result.jsonSuccess(builder.toString().substring(0, builder.length()-1));
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
		packet.setStatus(4);
		packet.setUpdated(DateUtils.currentTime());
		memOrderPacketMapper.update(packet);
		redisOperate.hmset(packet.redisKey(), packet);
		//设置产品状态运输中
		List<MemOrderGoods> list = memOrderGoodsMapper.getPacketMerchantOrderGoodsByPacketId(packetId);
		for(MemOrderGoods goods : list)
			goods.setStatus(4);
		memOrderGoodsMapper.batchUpdate(list);
		redisOperate.batchHmset(list);
		/*更新订单状态 和 订单状态表 开始*/
		MemOrderStatus status = merchant.getMemOrderStatusByOrderId(packet.getOrderId());
		status.setPacketCount(status.getPacketCount()-1);
		status.setTransportCount(status.getTransportCount()+1);
		setOrderStatus(merchant, packet.getOrderId(), packet.getMerchantId(), status);
		/*更新订单状态 和 订单状态表 结束*/
		return Result.jsonSuccess(packet);
	}
	
	/**
	 * 邮包运输完成
	 * @param packetId
	 * @param merchant
	 * @return
	 */
	@Transactional
	public String packetFinished(String packetId,Merchant merchant){
		MemOrderPacket packet = merchant.getMemOrderPacket(packetId);
		if(packet == null)
			return Result.jsonError(JiLuCode.PACKET_NOT_EXIST.constId(), MessageFormat.format(JiLuCode.PACKET_NOT_EXIST.defaultValue(), packetId));
		packet.setStatus(5);
		packet.setUpdated(DateUtils.currentTime());
		memOrderPacketMapper.update(packet);
		redisOperate.hmset(packet.redisKey(), packet);
		//设置产品状态运输中
		List<MemOrderGoods> list = memOrderGoodsMapper.getPacketMerchantOrderGoodsByPacketId(packetId);
		for(MemOrderGoods goods : list)
			goods.setStatus(5);
		memOrderGoodsMapper.batchUpdate(list);
		redisOperate.batchHmset(list);
		/*更新订单状态 和 订单状态表 开始*/
		MemOrderStatus status = merchant.getMemOrderStatusByOrderId(packet.getOrderId());
		status.setTransportCount(status.getTransportCount()-1);
		status.setFinishedCount(status.getFinishedCount()+1);
		setOrderStatus(merchant, packet.getOrderId(), packet.getMerchantId(), status);
		/*更新订单状态 和 订单状态表 结束*/
		return Result.jsonSuccess(packet);
	}
	
	protected void setOrderStatus(Merchant merchant,String orderId,long merchantId,MemOrderStatus status){
		MemOrder order = merchant.getMerchantOrderById(merchantId, orderId);
		memOrderStatusMapper.update(status);
		redisOperate.hmset(status.redisKey(), status);
		long orderStatus = luaOperate.getOrderStatus(status);
		if(order.getStatus() != orderStatus){
			order.setStatus((int)orderStatus);
			memOrderMapper.update(order);
			redisOperate.hmset(order.redisKey(), order);
			//推送订单状态改变信息
			JiLuPushUtil.OrderStatusChangePush(merchant.getMemCid(merchantId), orderId, "", order.getStatus());
			JiLuPushUtil.OrderStatusChangePush(merchant.getMemCid(order.getSuperMerchantId()), 
					order.getSuperOrderId(), orderId, order.getStatus());
		}
	}
	
	/**
	 * 插入商品
	 * 
	 * @param memGoods
	 */
	@Transactional
	public String insertGoods(CfgGoods memGoods,Merchant merchant) {
		cfgGoodsMapper.insert(memGoods);
		redisOperate.hmset(memGoods.redisKey(), memGoods);
		return Result.jsonSuccess(memGoods);
	}
	
}
