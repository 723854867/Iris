package org.Iris.app.jilu.service.realm.merchant;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.bean.enums.CustomerListType;
import org.Iris.app.jilu.common.bean.enums.IgtPushType;
import org.Iris.app.jilu.common.bean.enums.JbDetailType;
import org.Iris.app.jilu.common.bean.enums.JiLuLuaCommand;
import org.Iris.app.jilu.common.bean.enums.OrderStatus;
import org.Iris.app.jilu.common.bean.enums.PayOrderStatus;
import org.Iris.app.jilu.common.bean.form.OrderForm;
import org.Iris.app.jilu.common.bean.form.OrderGoodsForm;
import org.Iris.app.jilu.common.bean.model.OrderDetailedModel;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.realm.BackstageService;
import org.Iris.app.jilu.service.realm.courier.CourierService;
import org.Iris.app.jilu.service.realm.igt.IgtService;
import org.Iris.app.jilu.service.realm.igt.domain.PushOrderReceiveParam;
import org.Iris.app.jilu.service.realm.igt.domain.PushOrderStatusChangeParam;
import org.Iris.app.jilu.service.realm.igt.domain.PushOrderTransformParam;
import org.Iris.app.jilu.service.realm.igt.domain.TransmissionInfo;
import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemGoodsStore;
import org.Iris.app.jilu.storage.domain.MemJbDetail;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.domain.MemOrderGoods;
import org.Iris.app.jilu.storage.domain.MemOrderPacket;
import org.Iris.app.jilu.storage.domain.MemOrderStatus;
import org.Iris.app.jilu.storage.domain.MemPayInfo;
import org.Iris.app.jilu.storage.domain.StockGoodsStoreLog;
import org.Iris.app.jilu.storage.domain.UpdateStoreLog;
import org.Iris.app.jilu.storage.mybatis.mapper.CfgGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemGoodsStoreMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemJbDetailMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemMerchantMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderPacketMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderStatusMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemPayInfoMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.StockGoodsStoreLogMapper;
import org.Iris.app.jilu.storage.redis.JiLuLuaOperate;
import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.app.jilu.storage.redis.RedisCache;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.OrderNumberUtil;
import org.Iris.util.common.SerializeUtil;
import org.Iris.util.lang.DateUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.cj.api.io.PacketReceivedTimeHolder;

@Service
public class MerchantService extends RedisCache implements Beans{
	
//	@Resource
//	private JiLuLuaOperate luaOperate;
//	@Resource
//	private MemAccountMapper memAccountMapper;
//	@Resource
//	private MemMerchantMapper memMerchantMapper;
//	@Resource
//	private MemOrderMapper memOrderMapper;
//	@Resource
//	private MemOrderGoodsMapper memOrderGoodsMapper;
//	@Resource
//	private MemOrderPacketMapper memOrderPacketMapper;
//	@Resource
//	private MemOrderStatusMapper memOrderStatusMapper;
//	@Resource
//	private CfgGoodsMapper cfgGoodsMapper;
//	@Resource
//	private MemGoodsStoreMapper memGoodsStoreMapper;
//	@Resource
//	private CourierService courierService;
//	@Resource
//	private IgtService igtService;
//	@Resource
//	private StockGoodsStoreLogMapper stockGoodsStoreLogMapper;
//	@Resource
//	private MemPayInfoMapper memPayInfoMapper;
//	@Resource
//	private BackstageService backstageService;
//	@Resource
//	private MemJbDetail memJbDetail;
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
//		String accessToken = null;
		switch (type) {
		case MOBILE:
		case EMAIL:
			if (!courierService.verifyCaptch(type, account, captch))
				return Result.jsonError(JiLuCode.CAPTCHA_ERROR);
			break;
		case WECHAT:
//			accessToken = redisOperate.get(CommonKeyGenerator.weiXinAccessTokenKey(account));
//			if(accessToken == null){
//				return Result.jsonError(JiLuCode.WEIXIN_ACCESSTOKEN_EXPAIRED);
//			}else{
//				if(!accessToken.equals(captch))
//					return Result.jsonError(JiLuCode.ACCESSTOKEN_ERROR);
//			}
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
	public String createOrder(MemCustomer customer,List<MemOrderGoods> list,String memo,Merchant merchant) {
		//判断创建订单是否要钱
		int orderCreatePrice = Integer.valueOf(backstageService.getConfigValue("orderCreatePrice"));
		if(orderCreatePrice != 0 && merchant.getMemMerchant().getMoney() < orderCreatePrice)
			return Result.jsonError(JiLuCode.BALANCE_IS_NOT_ENOUGH);
		List<MemGoodsStore> addStoreList = new ArrayList<MemGoodsStore>();
		List<MemGoodsStore> updateStoreList = new ArrayList<MemGoodsStore>();
		String orderId = OrderNumberUtil.getRandomOrderId(4);
		List<OrderGoodsForm> orderGoodsForms = new ArrayList<OrderGoodsForm>();
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
			//处理仓库
			MemGoodsStore store = merchant.getMemGoodsStore(merchant.getMemMerchant().getMerchantId(),ogs.getGoodsId());
			if(null == store)
				addStoreList.add(new MemGoodsStore(goods,merchant.getMemMerchant(), 0-ogs.getCount(),ogs.getCount(), Float.valueOf(ogs.getUnitPrice()), ""));
			else {
				store.setCount(store.getCount()-ogs.getCount());
				store.setUpdated(time);
				store.setWaitCount(store.getWaitCount()+ogs.getCount());
				updateStoreList.add(store);
			}
			
			orderGoodsForms.add(new OrderGoodsForm(ogs));
		}
		MemMerchant memMerchant = merchant.getMemMerchant();
		MemOrder order = BeanCreator.newMemOrder(orderId, memMerchant.getMerchantId(), memMerchant.getName(),memMerchant.getAddress(),
				customer.getCustomerId(), customer.getName(), customer.getMobile(), customer.getAddress(),memo,0);
		if(orderCreatePrice !=0){
			memMerchant.setMoney(memMerchant.getMoney()-orderCreatePrice);
			memMerchant.setUpdated(DateUtils.currentTime());
			MemJbDetail memJbDetail = new MemJbDetail(memMerchant.getMerchantId(),orderCreatePrice,DateUtils.currentTime(),JbDetailType.ORDER.type(),orderId);
			memMerchantMapper.update(memMerchant);
			memJbDetailMapper.insert(memJbDetail);
		}
		memOrderMapper.insert(order);
		memOrderGoodsMapper.batchInsert(list);
		if(addStoreList.size()>0)
			memGoodsStoreMapper.batchInsert(addStoreList);
		if(updateStoreList.size()>0)
			memGoodsStoreMapper.batchUpdate(updateStoreList);
		//保存订单状态信息
		MemOrderStatus status = new MemOrderStatus(orderId);
		memOrderStatusMapper.insert(status);
		
		if(orderCreatePrice !=0){
			redisOperate.hmset(memMerchant.redisKey(), memMerchant);
		}
		redisOperate.hmset(order.redisKey(), order);
		redisOperate.batchHmset(list);
		redisOperate.hmset(MerchantKeyGenerator.merchantOrderStatusDataKey(orderId), status);
		redisOperate.batchHmset(addStoreList);
		redisOperate.batchHmset(updateStoreList);
		
		OrderDetailedModel model = new OrderDetailedModel();
		model.setOrderInfo(new OrderForm(order));
		model.setNotFinishGoodsList(OrderGoodsForm.getList(list));
		return Result.jsonSuccess(model);
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
	public String updateOrder(MemOrder order,List<MemOrderGoods> addGoodsList,List<MemOrderGoods> updateGoodsList,List<MemOrderGoods> deleteGoodsList,int goodsCount,Merchant merchant){
		List<MemGoodsStore> addStoreList = new ArrayList<MemGoodsStore>();
		List<MemGoodsStore> updateStoreList = new ArrayList<MemGoodsStore>();
		int time = DateUtils.currentTime();
		if(addGoodsList!=null){
			//处理仓库
			for(MemOrderGoods ogs : addGoodsList){
				CfgGoods goods = merchant.getGoodsById(ogs.getGoodsId());
				MemGoodsStore store = merchant.getMemGoodsStore(merchant.getMemMerchant().getMerchantId(),ogs.getGoodsId());
				if(null == store)
					addStoreList.add(new MemGoodsStore(goods,merchant.getMemMerchant(), 0-ogs.getCount(), ogs.getCount(),Float.valueOf(ogs.getUnitPrice()), ""));
				else {
					store.setCount(store.getCount()-ogs.getCount());
					store.setUpdated(time);
					store.setWaitCount(store.getWaitCount()+ogs.getCount());
					updateStoreList.add(store);
				}
			}
			memOrderGoodsMapper.batchInsert(addGoodsList);
		}
		if (updateGoodsList != null) {
			//处理仓库
			for(MemOrderGoods ogs : updateGoodsList){
				MemOrderGoods mGood = merchant.getMerchantOrderGoodsById(ogs.getId());
				MemGoodsStore store = merchant.getMemGoodsStore(merchant.getMemMerchant().getMerchantId(),ogs.getGoodsId());
				store.setCount(store.getCount()+(mGood.getCount()-ogs.getCount()));
				store.setUpdated(time);
				store.setWaitCount(store.getWaitCount()-(mGood.getCount()-ogs.getCount()));
				updateStoreList.add(store);
			}
			memOrderGoodsMapper.batchUpdate(updateGoodsList);
		}
		if (deleteGoodsList != null) {
			//处理仓库
			for(MemOrderGoods ogs : deleteGoodsList){
				MemGoodsStore store = merchant.getMemGoodsStore(merchant.getMemMerchant().getMerchantId(),ogs.getGoodsId());
				store.setCount(store.getCount()+ogs.getCount());
				store.setUpdated(time);
				store.setWaitCount(store.getWaitCount()-ogs.getCount());
				updateStoreList.add(store);
			}
			memOrderGoodsMapper.batchDelete(deleteGoodsList);
		}
		if(addStoreList.size()>0)
			memGoodsStoreMapper.batchInsert(addStoreList);
		if(updateStoreList.size()>0)
			memGoodsStoreMapper.batchUpdate(updateStoreList);
		redisOperate.batchHmset(addStoreList);
		redisOperate.batchHmset(updateStoreList);
		if(addGoodsList!=null){
			redisOperate.batchHmset(addGoodsList);
		}
		if (updateGoodsList != null) {
			redisOperate.batchHmset(updateGoodsList);
		}
		if (deleteGoodsList != null) {
			redisOperate.batchDelete(deleteGoodsList);
		}
		
		OrderDetailedModel model = new OrderDetailedModel();
		model.setOrderInfo(new OrderForm(order));
		model.setNotFinishGoodsList(merchant.getNotFinishGoodsList(order.getOrderId()));
		return Result.jsonSuccess(model);
		
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
	public String orderChange(MemOrder order,MemMerchant memMerchant,List<MemOrderGoods> changeOrderGoods,Merchant merchant){
		order.setSuperOrderId(order.getOrderId());
		order.setOrderId(OrderNumberUtil.getRandomOrderId(4));
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
		List<MemOrderGoods> changeList = new ArrayList<MemOrderGoods>();//转单产品列表以及原产品剩余数量的产品列表 待添加
		List<MemOrderGoods> updateList = new ArrayList<MemOrderGoods>();//原产品转出去的产品列表 待更新
		for (MemOrderGoods ogs : changeOrderGoods) {
			MemOrderGoods mGood = merchant.getMerchantOrderGoodsById(ogs.getId());
			if (mGood == null)
				return Result.jsonError(JiLuCode.ORDER_GOODS_NOT_EXIST.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_NOT_EXIST.defaultValue(), ogs.getId()));
			if (mGood.getStatus() != 0)
				return Result.jsonError(JiLuCode.ORDER_GOODS_IS_LOCK.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_IS_LOCK.defaultValue(), ogs.getId()));
			long count  = mGood.getCount() - ogs.getCount();
			if(count > 0){//原产品数量大于转出去的产品数量
				mGood.setCount(count);
				mGood.setUpdated(time);
				changeList.add(mGood);
			}
			if(count < 0)
				return Result.jsonError(JiLuCode.CHANGE_SUM_BIGGER);
		}
		for(MemOrderGoods ogs : changeOrderGoods){
			MemOrderGoods mGood = merchant.getMerchantOrderGoodsById(ogs.getId());
			mGood.setCount(ogs.getCount());
			mGood.setUpdated(time);
			mGood.setStatus(1);
			updateList.add(mGood);
			MemOrderGoods memOrderGoods = new MemOrderGoods();
			memOrderGoods.setOrderId(order.getOrderId());
			memOrderGoods.setGoodsId(mGood.getGoodsId());
			memOrderGoods.setGoodsName(mGood.getGoodsName());
			memOrderGoods.setCount(ogs.getCount());
			memOrderGoods.setUnitPrice(mGood.getUnitPrice());
			memOrderGoods.setStatus(2);
			memOrderGoods.setUpdated(time);
			memOrderGoods.setCreated(time);
			changeList.add(memOrderGoods);
		}
		memOrderMapper.insert(order);
		memOrderGoodsMapper.batchUpdate(updateList);
		memOrderGoodsMapper.batchInsert(changeList);
		
		/*更新父订单状态表mem_order_status start*/
		MemOrderStatus status = merchant.getMemOrderStatusByOrderId(order.getSuperOrderId());
		status.setTransformCount(status.getTransformCount()+1);
		memOrderStatusMapper.update(status);
		MemOrder superOrder = merchant.getMerchantOrderById(order.getSuperMerchantId(), order.getSuperOrderId());
		if(superOrder.getStatus() != 1){
			superOrder.setStatus(1);
			memOrderMapper.update(superOrder);
			redisOperate.hmset(superOrder.redisKey(), superOrder);
			//推送订单状态改变消息
//			JiLuPushUtil.OrderStatusChangePush(merchant.getMemCid(superOrder.getMerchantId()), superOrder.getOrderId(), "", 1);
//			JiLuPushUtil.OrderStatusChangePush(merchant.getMemCid(superOrder.getSuperMerchantId()), 
//					superOrder.getSuperOrderId(),superOrder.getOrderId(),1);
			igtService.pushToSingle(merchant.getMemCid(superOrder.getMerchantId()), 
					new TransmissionInfo(new PushOrderStatusChangeParam(superOrder.getOrderId(), "", 1), IgtPushType.ORDER_STATUS_CHANGE));
			igtService.pushToSingle(merchant.getMemCid(superOrder.getMerchantId()), 
					new TransmissionInfo(new PushOrderStatusChangeParam(superOrder.getSuperOrderId(), superOrder.getOrderId(), 1), IgtPushType.ORDER_STATUS_CHANGE));
		}
		redisOperate.hmset(status.redisKey(),status);
		/*更新父订单状态表mem_order_status end*/
		
		redisOperate.hmset(order.redisKey(), order);
		redisOperate.batchHmset(updateList);
		redisOperate.batchHmset(changeList);
		
		//推送转单信息  参数：转单方名字，转单订单号，转单时间
//		JiLuPushUtil.OrderTransformPush(merchant.getMemCid(order.getMerchantId()),
//				merchant.getMemMerchant().getName(), order.getOrderId(), time);
		
		igtService.pushToSingle(merchant.getMemCid(order.getMerchantId()), 
				new TransmissionInfo(new PushOrderTransformParam(merchant.getMemMerchant().getName(), order.getOrderId(), time),IgtPushType.ORDER_TRANSFORM));
		
		//获取转单后订单信息返回给客户端
		OrderDetailedModel model = new OrderDetailedModel();
		model.setOrderInfo(new OrderForm(superOrder));
		model.setNotFinishGoodsList(merchant.getNotFinishGoodsList(superOrder.getOrderId()));
		List<MemOrder> transferList = new ArrayList<>();
		transferList.add(order);
		model.setTransferOrderList(merchant.getTransferOrderListModelList(transferList));
		return Result.jsonSuccess(model);
	}
	
	/**
	 * 拒绝转单操作/取消转单
	 * 
	 * @param orderId
	 */
	@Transactional
	public void refuseOrder(MemOrder order,Merchant merchant) {
		int time = DateUtils.currentTime();
		// 删除转单订单即子订单
		memOrderMapper.delete(order.getMerchantId(),order.getOrderId());
		// 删除转单产品列表
		List<MemOrderGoods> list = memOrderGoodsMapper.getChangeMerchantOrderGoodsByOrderId(order.getOrderId());
		// 更新父订单产品状态
		List<MemOrderGoods> updateList = new ArrayList<MemOrderGoods>();
		List<MemOrderGoods> deleteList = new ArrayList<MemOrderGoods>();
		for (MemOrderGoods merchantOrderGoods : list) {
			//MemOrderGoods mOrderGoods = merchant.getMerchantOrderGoodsById(merchantOrderGoods.getId());
			//获取父订单单个产品的未转单部分
			MemOrderGoods superNotChange = memOrderGoodsMapper.getSuperNotChangeOrderGoods(order.getSuperOrderId(),merchantOrderGoods.getGoodsId());
			MemOrderGoods superChange = memOrderGoodsMapper.getSuperChangeOrderGoods(order.getSuperOrderId(),merchantOrderGoods.getGoodsId(), merchantOrderGoods.getCount());
			if(superNotChange == null){
				//产品的数量已经全部转出去
				superChange.setStatus(0);
				superChange.setUpdated(time);
				updateList.add(superChange);
			}else{
				superNotChange.setCount(superNotChange.getCount()+merchantOrderGoods.getCount());
				superNotChange.setUpdated(time);
				updateList.add(superNotChange);
				deleteList.add(superChange);
			}
		}
		deleteList.addAll(list);
		memOrderGoodsMapper.batchDelete(deleteList);
		memOrderGoodsMapper.batchUpdate(updateList);
		
		/*更新父订单状态 和 订单状态表 开始*/
		MemOrderStatus status = merchant.getMemOrderStatusByOrderId(order.getSuperOrderId());
		status.setTransformCount(status.getTransformCount()-1);
		setOrderStatus(merchant, order.getSuperOrderId(), order.getSuperMerchantId(), status);
		/*更新父订单状态 和 订单状态表 结束*/
		
		// 更新缓存
		redisOperate.del(order.redisKey());
		redisOperate.batchDelete(deleteList);
//		for (MemOrderGoods goods : deleteList) {
//			redisOperate.del(MerchantKeyGenerator.merchantOrderGoodsDataKey(goods.getId()));
//		}
		redisOperate.batchHmset(updateList);
	}
	
	/**
	 * 接收转单 全部或部分接收
	 * 
	 * @param changeOrderList
	 * @param orderId
	 * @param superOrderId
	 * @param merchantId
	 */
	@Transactional
	public String receiveOrder(List<MemOrderGoods> receiveGoodsList, String orderId, String superOrderId,
			long merchantId,Merchant merchant) {
		//判断创建订单是否要钱
		int orderCreatePrice = Integer.valueOf(backstageService.getConfigValue("orderCreatePrice"));
		int orderTransformPrice = Integer.valueOf(backstageService.getConfigValue("orderTransformPrice"));
		if(orderCreatePrice != 0 && orderTransformPrice !=0 && merchant.getMemMerchant().getMoney() < orderTransformPrice)
			return Result.jsonError(JiLuCode.BALANCE_IS_NOT_ENOUGH);
		
		List<MemGoodsStore> addStoreList = new ArrayList<MemGoodsStore>();
		List<MemGoodsStore> updateStoreList = new ArrayList<MemGoodsStore>();
		int time= DateUtils.currentTime();
		//扣除创建转单金额
		MemMerchant memMerchant = merchant.getMemMerchant();
		if(orderCreatePrice !=0 && orderTransformPrice !=0){
			memMerchant.setMoney(memMerchant.getMoney()-orderTransformPrice);
			memMerchant.setUpdated(DateUtils.currentTime());
			MemJbDetail memJbDetail = new MemJbDetail(memMerchant.getMerchantId(),orderTransformPrice,DateUtils.currentTime(),JbDetailType.ORDER.type(),orderId);
			memMerchantMapper.update(memMerchant);
			memJbDetailMapper.insert(memJbDetail);
		}
		
		// 更新子订单状态
		MemOrder order = redisOperate.hgetAll(MerchantKeyGenerator.merchantOrderDataKey(merchantId, orderId), new MemOrder());
		order.setStatus(0);
		order.setUpdated(DateUtils.currentTime());
		memOrderMapper.update(order);
		//保存新建子订单状态信息
		memOrderStatusMapper.insert(new MemOrderStatus(orderId));
		// 查找本次转单申请的所有产品
		List<MemOrderGoods> list = memOrderGoodsMapper.getChangeMerchantOrderGoodsByOrderId(orderId);
		List<Long> recevieGoodsIds = new ArrayList<Long>();
		for (MemOrderGoods goods : receiveGoodsList)
			recevieGoodsIds.add(goods.getId());

		Iterator<MemOrderGoods> it = list.iterator();
		while (it.hasNext()) {
			MemOrderGoods goods = it.next();
			if (recevieGoodsIds.contains(goods.getId()))
				it.remove();
		}
		//memOrderGoodsMapper.batchUpdate(receiveGoodsList);
		List<MemOrderGoods> updateList = new ArrayList<MemOrderGoods>();
		List<MemOrderGoods> deleteList = new ArrayList<MemOrderGoods>();
		// 更新父订单产品处理
		for (MemOrderGoods merchantOrderGoods : receiveGoodsList) {
			MemOrderGoods superChange = memOrderGoodsMapper.getSuperChangeOrderGoods(order.getSuperOrderId(),merchantOrderGoods.getGoodsId(), merchantOrderGoods.getCount());
			superChange.setStatus(7);
			superChange.setUpdated(time);
			superChange.setChangeOrderId(orderId);
			updateList.add(superChange);
		}
		//处理接收转单商户和转单商户的仓库数据
		for (MemOrderGoods ogs : receiveGoodsList) {
			//接收转单商户仓库数据处理
			CfgGoods goods = merchant.getGoodsById(ogs.getChangeId());
			MemGoodsStore store = merchant.getMemGoodsStore(merchantId,ogs.getChangeId());
			if(null == store)
				addStoreList.add(new MemGoodsStore(goods, merchant.getMemMerchant(),0-ogs.getCount(), ogs.getCount(),Float.valueOf(ogs.getUnitPrice()), ""));
			else {
				store.setCount(store.getCount()-ogs.getCount());
				store.setUpdated(time);
				store.setWaitCount(store.getWaitCount()+ogs.getCount());
				updateStoreList.add(store);
			}
			//转单商户仓库数据处理
			MemGoodsStore store2 = merchant.getMemGoodsStore(order.getSuperMerchantId(),ogs.getGoodsId());
			store2.setCount(store2.getCount()+ogs.getCount());
			store2.setUpdated(time);
			store2.setWaitCount(store2.getWaitCount()-ogs.getCount());
			store2.setSellCount(store2.getSellCount()+ogs.getCount());
			updateStoreList.add(store2);
			
			ogs.setGoodsId(ogs.getChangeId());
		}
		
		if (null != list && list.size() > 0) {
			//转单中的产品有未被接收的 需要回收
			for (MemOrderGoods merchantOrderGoods : list) {
				//获取父订单单个产品的未转单部分
				MemOrderGoods superNotChange = memOrderGoodsMapper.getSuperNotChangeOrderGoods(order.getSuperOrderId(),merchantOrderGoods.getGoodsId());
				MemOrderGoods superChange = memOrderGoodsMapper.getSuperChangeOrderGoods(order.getSuperOrderId(),merchantOrderGoods.getGoodsId(), merchantOrderGoods.getCount());
				if(superNotChange == null){
					//产品的数量已经全部转出去
					superChange.setStatus(0);
					superChange.setUpdated(time);
					updateList.add(superChange);
				}else{
					superNotChange.setCount(superNotChange.getCount()+merchantOrderGoods.getCount());
					superNotChange.setUpdated(time);
					updateList.add(superNotChange);
					deleteList.add(superChange);
				}
			}
		}
		deleteList.addAll(list);
		updateList.addAll(receiveGoodsList);
		memOrderGoodsMapper.batchUpdate(updateList);
		if(deleteList.size()>0)
			memOrderGoodsMapper.batchDelete(deleteList);
		if(addStoreList.size()>0)
			memGoodsStoreMapper.batchInsert(addStoreList);
		if(updateStoreList.size()>0)
			memGoodsStoreMapper.batchUpdate(updateStoreList);

		/*更新父订单状态 和 订单状态表 开始*/
		MemOrderStatus status = merchant.getMemOrderStatusByOrderId(superOrderId);
		status.setTransformCount(status.getTransformCount()-1);
		setOrderStatus(merchant, superOrderId, order.getSuperMerchantId(), status);
		/*更新父订单状态 和 订单状态表 结束*/
		
		// 更新缓存
		redisOperate.hmset(order.redisKey(), order);
		redisOperate.batchDelete(deleteList);
//		for (MemOrderGoods goods : deleteList) {
//			redisOperate.del(MerchantKeyGenerator.merchantOrderGoodsDataKey(goods.getId()));
//		}
		redisOperate.batchHmset(updateList);
		redisOperate.batchHmset(addStoreList);
		redisOperate.batchHmset(updateStoreList);
		redisOperate.hmset(memMerchant.redisKey(), memMerchant);
		
		//获取接收转单后订单信息返回给客户端
		MemOrder superOrder = merchant.getMerchantOrderById(order.getSuperMerchantId(), superOrderId);
		OrderDetailedModel model = new OrderDetailedModel();
		model.setOrderInfo(new OrderForm(superOrder));
		model.setNotFinishGoodsList(merchant.getNotFinishGoodsList(superOrderId));
		
		//推送转单接收信息  参数：转单单号，转单父订单号
		//JiLuPushUtil.OrderReceivePush(merchant.getMemCid(order.getSuperMerchantId()), orderId, superOrderId);
		igtService.pushToSingle(merchant.getMemCid(order.getSuperMerchantId()), new TransmissionInfo(new PushOrderReceiveParam(orderId, superOrderId), IgtPushType.ORDER_RECEIVE));
		return Result.jsonSuccess(model);
	}
	
	/**
	 * 分包
	 * @param orderId
	 * @param goodsList 1:2;3:4
	 * @return
	 */
//	@Transactional
//	public String orderPacket(String orderId,String packetGoodsList,Merchant merchant){
//		String[] packetGoods = packetGoodsList.split(";");
//		StringBuilder builder = new StringBuilder();
//		List<MemOrderPacket> packetList = new ArrayList<MemOrderPacket>();
//		List<MemOrderGoods> orderGoodsList = new ArrayList<MemOrderGoods>();
//		for(String str : packetGoods){
//			String packetId = "p_"+System.currentTimeMillis() + "" + new Random().nextInt(10);
//			MemOrderPacket packet = BeanCreator.newMemOrderPacket(packetId, orderId,merchant.getMemMerchant().getMerchantId());
//			packetList.add(packet);
//			builder.append(packetId+";");
//			String[] goods = str.split(":");
//			for(String goodsId : goods){
//				MemOrderGoods mGoods = merchant.getMerchantOrderGoodsById(Long.valueOf(goodsId));
//				if (mGoods == null)
//					return Result.jsonError(JiLuCode.GOODS_NOT_EXIST.constId(), MessageFormat.format(JiLuCode.GOODS_NOT_EXIST.defaultValue(), goodsId));
//				if(mGoods.getStatus()!=0)
//					return Result.jsonError(JiLuCode.ORDER_GOODS_IS_LOCK.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_IS_LOCK.defaultValue(), goodsId));
//				mGoods.setStatus(3);
//				int time = DateUtils.currentTime();
//				mGoods.setUpdated(time);
//				mGoods.setPacketId(packetId);
//				orderGoodsList.add(mGoods);
//			}
//		}
//		memOrderPacketMapper.batchInsert(packetList);
//		memOrderGoodsMapper.batchUpdate(orderGoodsList);
//		
//		/*更新父订单状态 和 订单状态表 开始*/
//		MemOrderStatus status = merchant.getMemOrderStatusByOrderId(orderId);
//		status.setPacketCount(status.getPacketCount()+packetGoods.length);
//		setOrderStatus(merchant, orderId, merchant.getMemMerchant().getMerchantId(), status);
//		/*更新父订单状态 和 订单状态表 结束*/
//		
//		redisOperate.batchHmset(packetList);
//		redisOperate.batchHmset(orderGoodsList);
//		
//		return Result.jsonSuccess(builder.toString().substring(0, builder.length()-1));
//	}
	
	/**
	 * 分包
	 * 处理逻辑：删除原被分包产品，根据分包规则重新生成分包产品数据
	 * @param orderId
	 * @param packets 里面的一个对象对应一个包
	 * @return
	 */
	@Transactional
	public String orderPacket(String orderId,List<List<MemOrderGoods>> packets,Merchant merchant){
		int time = DateUtils.currentTime();
		List<Long> ids = new ArrayList<Long>();
		List<MemOrderGoods> addList = new ArrayList<MemOrderGoods>();
		List<MemOrderGoods> delList = new ArrayList<MemOrderGoods>();
		List<MemOrderPacket> packetList = new ArrayList<MemOrderPacket>();
		List<MemGoodsStore> updateStoreList = new ArrayList<MemGoodsStore>();
		StringBuilder builder = new StringBuilder();
		for(List<MemOrderGoods> packet : packets){
			//packet 代表一个分包中的所有产品
			String packetId = "p_"+OrderNumberUtil.getRandomOrderId(2);
			MemOrderPacket memOrderPacket = BeanCreator.newMemOrderPacket(packetId, orderId,merchant.getMemMerchant().getMerchantId());
			packetList.add(memOrderPacket);
			builder.append(packetId+";");
			for(MemOrderGoods mog : packet){
				if(!ids.contains(mog.getId())){
					delList.add(mog);
					ids.add(mog.getId());
				}
				MemOrderGoods memOrderGoods = merchant.getMerchantOrderGoodsById(mog.getId());
				if (memOrderGoods == null)
					return Result.jsonError(JiLuCode.GOODS_NOT_EXIST.constId(), MessageFormat.format(JiLuCode.GOODS_NOT_EXIST.defaultValue(), mog.getId()));
				if(memOrderGoods.getStatus()!=0)
					return Result.jsonError(JiLuCode.ORDER_GOODS_IS_LOCK.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_IS_LOCK.defaultValue(), mog.getId()));
				memOrderGoods.setPacketId(packetId);
				memOrderGoods.setUpdated(time);
				memOrderGoods.setCount(mog.getCount());
				memOrderGoods.setStatus(3);
				addList.add(memOrderGoods);
				//处理仓库数据
				MemGoodsStore store = merchant.getMemGoodsStore(merchant.getMemMerchant().getMerchantId(),memOrderGoods.getGoodsId());
				store.setUpdated(time);
				store.setWaitCount(store.getWaitCount()-mog.getCount());
				store.setSellCount(store.getSellCount()+mog.getCount());
				updateStoreList.add(store);
			}
		}
		memOrderPacketMapper.batchInsert(packetList);
		memOrderGoodsMapper.batchInsert(addList);
		memOrderGoodsMapper.batchDelete(delList);
		memGoodsStoreMapper.batchUpdate(updateStoreList);
		/*更新父订单状态 和 订单状态表 开始*/
		MemOrderStatus status = merchant.getMemOrderStatusByOrderId(orderId);
		status.setPacketCount(status.getPacketCount()+packets.size());
		setOrderStatus(merchant, orderId, merchant.getMemMerchant().getMerchantId(), status);
		/*更新父订单状态 和 订单状态表 结束*/
		
		redisOperate.batchHmset(packetList);
		redisOperate.batchHmset(addList);
		redisOperate.batchDelete(delList);
		redisOperate.batchHmset(updateStoreList);
		
		//获取分包后订单信息返回给客户端
		MemOrder order = merchant.getMerchantOrderById(merchant.getMemMerchant().getMerchantId(), orderId);
		OrderDetailedModel model = new OrderDetailedModel();
		model.setOrderInfo(new OrderForm(order));
		model.setNotFinishGoodsList(merchant.getNotFinishGoodsList(orderId));
		model.setPacketList(merchant.getPacketList(orderId));
		return Result.jsonSuccess(model);
	}
	
	
	/**
	 * 移包
	 * @param fromPacketId
	 * @param toPacketId
	 * @param id
	 * @param count
	 * @return
	 */
	@Transactional
	public String packetMove(String fromPacketId, String toPacketId, long id, long count,Merchant merchant) {
		int time = DateUtils.currentTime();
		MemOrderPacket fromPacket = merchant.getMemOrderPacket(fromPacketId);
		MemOrderPacket toPacket = merchant.getMemOrderPacket(toPacketId);
		if(null == fromPacket)
			throw IllegalConstException.errorException(JiLuParams.FROMPACKETID);
		if(null == toPacket)
			throw IllegalConstException.errorException(JiLuParams.TOPACKETID);
		if(fromPacket.getStatus()!=OrderStatus.PACKET.status() || toPacket.getStatus()!=OrderStatus.PACKET.status() || !fromPacket.getOrderId().equals(toPacket.getOrderId()))
			return Result.jsonError(JiLuCode.PACKET_CANNOT_MOVE);
		long fromPacketCount = memOrderGoodsMapper.getPacketMerchantOrderGoodsCountByPacketId(fromPacketId);
		MemOrderGoods fromMemOrderGoods = merchant.getMerchantOrderGoodsById(id);
		if(null == fromMemOrderGoods) 
			throw IllegalConstException.errorException(JiLuParams.ID);
		if(fromMemOrderGoods.getCount() < count)
			throw IllegalConstException.errorException(JiLuParams.COUNT);
		MemOrderGoods toMemOrderGoods = memOrderGoodsMapper.getMerchantOrderGoodsByPacketId(toPacketId, fromMemOrderGoods.getGoodsId());
		//处理转入的包裹
		if(toMemOrderGoods == null){//移入的包中没有该产品
			toMemOrderGoods = new MemOrderGoods(fromMemOrderGoods);
			toMemOrderGoods.setPacketId(toPacketId);
			memOrderGoodsMapper.insert(toMemOrderGoods);
		}else{
			toMemOrderGoods.setCount(toMemOrderGoods.getCount()+count);
			toMemOrderGoods.setUpdated(time);
			memOrderGoodsMapper.update(toMemOrderGoods);
		}
		//处理转出的包裹
		if(fromPacketCount == 1 && fromMemOrderGoods.getCount() == count){
			//转出的包裹产品全转没了 此时要删除包裹
			memOrderPacketMapper.delete(fromPacket);
			memOrderGoodsMapper.delete(fromMemOrderGoods.getId());
			/*更新父订单状态 和 订单状态表 开始*/
			MemOrderStatus status = merchant.getMemOrderStatusByOrderId(fromPacket.getOrderId());
			status.setPacketCount(status.getPacketCount()-1);
			setOrderStatus(merchant, fromPacket.getOrderId(), merchant.getMemMerchant().getMerchantId(), status);
			/*更新父订单状态 和 订单状态表 结束*/
			redisOperate.del(fromPacket.redisKey());
			redisOperate.del(fromMemOrderGoods.redisKey());
			
		}else if(fromPacketCount >1 && fromMemOrderGoods.getCount() == count){
			//转出的包裹还有别的产品且转出的产品数量全转完
			memOrderGoodsMapper.delete(fromMemOrderGoods.getId());
			redisOperate.del(fromMemOrderGoods.redisKey());
		}else{
			//转出的产品没有全转完
			fromMemOrderGoods.setCount(fromMemOrderGoods.getCount()-count);
			fromMemOrderGoods.setUpdated(time);
			memOrderGoodsMapper.update(fromMemOrderGoods);
			redisOperate.hmset(fromMemOrderGoods.redisKey(), fromMemOrderGoods);
		}
		redisOperate.hmset(toMemOrderGoods.redisKey(), toMemOrderGoods);
		
		//获取移包后订单信息返回给客户端
		MemOrder order = merchant.getMerchantOrderById(merchant.getMemMerchant().getMerchantId(), fromPacket.getOrderId());
		OrderDetailedModel model = new OrderDetailedModel();
		model.setOrderInfo(new OrderForm(order));
		model.setPacketList(merchant.getPacketList(order.getOrderId()));
		return Result.jsonSuccess(model);
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
		packet.setStatus(OrderStatus.TRANSPORT.status());
		packet.setUpdated(DateUtils.currentTime());
		memOrderPacketMapper.update(packet);
		redisOperate.hmset(packet.redisKey(), packet);
		//设置产品状态运输中
		double money=0;//定义这次邮单的消费金额
		List<MemOrderGoods> list = memOrderGoodsMapper.getPacketMerchantOrderGoodsByPacketId(packetId);
		for(MemOrderGoods goods : list){
			goods.setStatus(OrderStatus.TRANSPORT.status());
			money+=goods.getCount()*Double.valueOf(goods.getUnitPrice());
		}
		memOrderGoodsMapper.batchUpdate(list);
		redisOperate.batchHmset(list);
		/*更新订单状态 和 订单状态表 开始*/
		MemOrderStatus status = merchant.getMemOrderStatusByOrderId(packet.getOrderId());
		status.setPacketCount(status.getPacketCount()-1);
		status.setTransportCount(status.getTransportCount()+1);
		setOrderStatus(merchant, packet.getOrderId(), packet.getMerchantId(), status);
		/*更新订单状态 和 订单状态表 结束*/
		
		//更新商户的客户排序列表
		MemOrder order = merchant.getMerchantOrderById(packet.getMerchantId(), packet.getOrderId());
		long customerId = order.getCustomerId();
		MemCustomer customer = merchant.getCustomer(customerId);
		int time = DateUtils.currentTime();
		customer.setLastPurchaseTime(time);//更新最后消费时间
		customer.setPurchaseSum(String.valueOf(Double.valueOf(customer.getPurchaseSum())+money));//更新消费总额
		customer.setUpdated(time);
		memCustomerMapper.update(customer);
		redisOperate.hmset(MerchantKeyGenerator.customerDataKey(packet.getMerchantId()), customer);
		luaOperate.evalLua(JiLuLuaCommand.FINISH_ORDER.name(), 4,
				MerchantKeyGenerator.customerListLoadTimeKey(packet.getMerchantId()),
				CustomerListType.PURCHASE_SUM.redisCustomerListKey(packet.getMerchantId()),
				CustomerListType.PURCHASE_RECENT.redisCustomerListKey(packet.getMerchantId()),
				CustomerListType.PURCHASE_FREQUENCY.redisCustomerListKey(packet.getMerchantId()),
				String.valueOf(customerId),String.valueOf(time),String.valueOf(money));
		
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
		if(packet.getStatus() != OrderStatus.TRANSPORT.status())
			throw IllegalConstException.errorException(JiLuParams.PACKETID);
		packet.setStatus(OrderStatus.FINISH.status());
		packet.setUpdated(DateUtils.currentTime());
		//设置产品状态运输中
		List<MemOrderGoods> list = memOrderGoodsMapper.getPacketMerchantOrderGoodsByPacketId(packetId);
		for(MemOrderGoods goods : list)
			goods.setStatus(OrderStatus.FINISH.status());
		memOrderGoodsMapper.batchUpdate(list);
		memOrderPacketMapper.update(packet);
		redisOperate.hmset(packet.redisKey(), packet);
		redisOperate.batchHmset(list);
		/*更新订单状态 和 订单状态表 开始*/
		MemOrderStatus status = merchant.getMemOrderStatusByOrderId(packet.getOrderId());
		status.setTransportCount(status.getTransportCount()-1);
		status.setFinishedCount(status.getFinishedCount()+1);
		setOrderStatus(merchant, packet.getOrderId(), packet.getMerchantId(), status);
		/*更新订单状态 和 订单状态表 结束*/
		return Result.jsonSuccess();
	}
	
	/**
	 * 删除订单（订单作废）
	 * @param packetId
	 * @param merchant
	 * @return
	 */
	@Transactional
	public String deleteOrder(String orderId,Merchant merchant){
		MemOrder order = merchant.getMerchantOrderById(merchant.getMemMerchant().getMerchantId(), orderId);
		if(null == order)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		if(order.getStatus()!=0 && order.getStatus()!=3)
			return Result.jsonError(JiLuCode.ORDER_IS_LOCK);
		order.setStatus(9);
		List<MemOrderGoods> list = memOrderGoodsMapper.getDelMerchantOrderGoodsByOrderId(orderId);
		List<MemGoodsStore> updateGoodsStore = new ArrayList<>();
		Map<Long, MemGoodsStore> map = new HashMap<Long, MemGoodsStore>();
		for(MemOrderGoods mog : list){
			MemGoodsStore goodsStore = new MemGoodsStore();
			if(map.containsKey(mog.getGoodsId())){
				goodsStore = map.get(mog.getGoodsId());
			}else{
				goodsStore = merchant.getMemGoodsStore(merchant.getMemMerchant().getMerchantId(), mog.getGoodsId());
			}
			switch (mog.getStatus()) {
			case 0:
				goodsStore.setWaitCount(goodsStore.getWaitCount()-mog.getCount());
			case 3:
				goodsStore.setCount(goodsStore.getCount()+mog.getCount());
				//updateGoodsStore.add(goodsStore);
				//mog.setStatus(9);
				map.put(mog.getGoodsId(), goodsStore);
				break;
			default:
				return Result.jsonError(JiLuCode.ORDER_IS_LOCK);
			}
		}
		updateGoodsStore.addAll(map.values());
		memOrderMapper.update(order);
		memGoodsStoreMapper.batchUpdate(updateGoodsStore);
		//memOrderGoodsMapper.batchUpdate(list);
		redisOperate.batchHmset(updateGoodsStore);
		//redisOperate.batchHmset(list);
		redisOperate.hmset(order.redisKey(), order);
		return Result.jsonSuccess();
	}
	
	@Transactional
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
//			JiLuPushUtil.OrderStatusChangePush(merchant.getMemCid(merchantId), orderId, "", order.getStatus());
//			JiLuPushUtil.OrderStatusChangePush(merchant.getMemCid(order.getSuperMerchantId()), 
//					order.getSuperOrderId(), orderId, order.getStatus());
			igtService.pushToSingle(merchant.getMemCid(merchantId), 
					new TransmissionInfo(new PushOrderStatusChangeParam(orderId, "", order.getStatus()), IgtPushType.ORDER_STATUS_CHANGE));
			igtService.pushToSingle(merchant.getMemCid(merchantId), 
					new TransmissionInfo(new PushOrderStatusChangeParam(order.getSuperOrderId(), orderId, order.getStatus()), IgtPushType.ORDER_STATUS_CHANGE));
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
	
	/**
	 * 进货
	 * @param goodsId
	 * @param count
	 * @param memo
	 * @param merchant
	 * @return
	 */
	@Transactional
	public String stockGoodsStore(long goodsId, long count, float price , String memo,int stockTime, Merchant merchant) {
		MemGoodsStore store = merchant.getMemGoodsStore(merchant.getMemMerchant().getMerchantId(),goodsId);
		if(store==null)
			throw IllegalConstException.errorException(JiLuParams.GOODS_ID);
		store.setCount(store.getCount()+count);
		int time = DateUtils.currentTime();
		store.setLastStockTime(time);
		store.setUpdated(time);
		memGoodsStoreMapper.update(store);
		stockGoodsStoreLogMapper.insert(new StockGoodsStoreLog(goodsId,store.getGoodsName(),
				merchant.getMemMerchant().getMerchantId(),merchant.getMemMerchant().getName(),memo, price,count, time,stockTime));
		redisOperate.hmset(store.redisKey(), store);
		return Result.jsonSuccess();
	}
	
	/**
	 * 修改商品仓储
	 * @param memo
	 * @param count
	 * @param price
	 * @return
	 */
	@Transactional
	public String updateGoodsStore(long goodsId,String memo, long count, float price,String operation,Merchant merchant) {
		MemGoodsStore store = merchant.getMemGoodsStore(merchant.getMemMerchant().getMerchantId(),goodsId);
		if(store == null)
			throw IllegalConstException.errorException(JiLuParams.GOODS_ID);
		UpdateStoreLog log = new UpdateStoreLog(store, price, count, memo, operation);
		store.setMemo(memo);
		store.setCount(count);
		store.setPrice(price);
		store.setUpdated(DateUtils.currentTime());
		memGoodsStoreMapper.update(store);
		Beans.updateStoreLogMapper.insert(log);
		redisOperate.hmset(store.redisKey(), store);
		return Result.jsonSuccess();
	}

	/**
	 * 售后
	 * @param orderId
	 * @param shInfo
	 * @param shTime
	 * @param merchant
	 * @return
	 */
	public String orderSh(String orderId, String shInfo, int shTime, Merchant merchant) {
		MemOrder order = merchant.getMerchantOrderById(merchant.getMemMerchant().getMerchantId(), orderId);
		if(order==null)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		if(order.getStatus()!=OrderStatus.FINISH.status())
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		order.setShInfo(shInfo);
		order.setShTime(shTime);
		order.setUpdated(DateUtils.currentTime());
		order.setStatus(OrderStatus.CUSTOMER_SERVICE.status());
		memOrderMapper.update(order);
		redisOperate.hmset(order.redisKey(), order);
		return Result.jsonSuccess();
	}
	/**
	 * 完成售后
	 * @param orderId
	 * @param shMemo
	 * @param merchant
	 * @return
	 */
	public String finishOrderSh(String orderId,String shMemo,Merchant merchant){
		MemOrder order = merchant.getMerchantOrderById(merchant.getMemMerchant().getMerchantId(), orderId);
		if(order==null)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		if(order.getStatus()!=OrderStatus.CUSTOMER_SERVICE.status())
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		order.setShMemo(shMemo);
		order.setUpdated(DateUtils.currentTime());
		order.setStatus(OrderStatus.FINISH.status());
		memOrderMapper.update(order);
		redisOperate.hmset(order.redisKey(), order);
		return Result.jsonSuccess();
	}

	/**
	 * 支付宝支付成功异步处理
	 * @param out_trade_no
	 * @param total_amount
	 */
	@Transactional
	public void alipayAsyncHandle(String outTradeNo) {
		MemPayInfo memPayInfo = getMemPayInfo(outTradeNo);
		if(memPayInfo != null){
			int time = DateUtils.currentTime();
			memPayInfo.setStatus(PayOrderStatus.PAY_SUCCESS.status());
			memPayInfo.setCzTime(time);
			memPayInfo.setUpdated(time);
			
			MemMerchant memMerchant = getMerchantById(memPayInfo.getMerchantId()).getMemMerchant();
			memMerchant.setMoney(memMerchant.getMoney() + memPayInfo.getTotalJb());
			memMerchant.setUpdated(time);
			
        	//充值记录明细
    		MemJbDetail memJbDetail = new MemJbDetail(memMerchant.getMerchantId(), memPayInfo.getTotalJb(), time, JbDetailType.CZ.type(), null);
    		memJbDetailMapper.insert(memJbDetail);
			
			memPayInfoMapper.update(memPayInfo);
			memMerchantMapper.update(memMerchant);
			redisOperate.hset(MerchantKeyGenerator.merchantPayDataKey(), outTradeNo, SerializeUtil.JsonUtil.GSON.toJson(memPayInfo));
			redisOperate.hmset(memMerchant.redisKey(), memMerchant);
			
		}
	}

	public MemPayInfo getMemPayInfo(String outTradeNo){
		MemPayInfo payInfo = redisOperate.hgetBean(MerchantKeyGenerator.merchantPayDataKey(), outTradeNo, MemPayInfo.class);
		if(payInfo == null){
			payInfo = memPayInfoMapper.findByOutRradeNo(outTradeNo);
			if(payInfo!=null)
				redisOperate.hset(MerchantKeyGenerator.merchantPayDataKey(), outTradeNo, SerializeUtil.JsonUtil.GSON.toJson(payInfo));
		}
		return payInfo;
	}
}
