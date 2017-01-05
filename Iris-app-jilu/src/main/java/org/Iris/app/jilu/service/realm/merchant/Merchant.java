package org.Iris.app.jilu.service.realm.merchant;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.bean.enums.CustomerListType;
import org.Iris.app.jilu.common.bean.enums.JiLuLuaCommand;
import org.Iris.app.jilu.common.bean.form.AssumeRoleForm;
import org.Iris.app.jilu.common.bean.form.CustomerFrequencyPagerForm;
import org.Iris.app.jilu.common.bean.form.CustomerPagerForm;
import org.Iris.app.jilu.common.bean.form.Pager;
import org.Iris.app.jilu.common.bean.model.CustomerListPurchaseFrequencyModel;
import org.Iris.app.jilu.common.bean.model.OrderChangeModel;
import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.domain.MemOrderGoods;
import org.Iris.app.jilu.storage.domain.PubRelation;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.CnToSpell;
import org.Iris.util.common.IrisSecurity;
import org.Iris.util.common.SerializeUtil;
import org.Iris.util.common.uuid.AlternativeJdkIdGenerator;
import org.Iris.util.lang.DateUtils;
import org.Iris.util.reflect.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;

import redis.clients.jedis.Tuple;

/**
 * 这里注意，只有 login 本身需要 lock 其他任何操作都已经在 action 里面做好了 lock 操作不需要额外的 lock 
 * 
 * @author Ahab
 */
public class Merchant implements Beans {
	
	private static final Logger logger = LoggerFactory.getLogger(Merchant.class);

	private String lock;
	private MemMerchant memMerchant;
	
	public Merchant(MemMerchant memMerchant) {
		this.memMerchant = memMerchant;
		this.lock = MerchantKeyGenerator.merchantLockKey(memMerchant.getMerchantId());
	}
	
	/**
	 * 用户登陆
	 * 
	 * @param account
	 * @param create
	 * @return
	 */
	public boolean login(String account, boolean create) {
		String token = IrisSecurity.encodeToken(account);
		String lockId = AlternativeJdkIdGenerator.INSTANCE.generateId().toString();
		int time = DateUtils.currentTime();
		long flag = luaOperate.tokenReplace(lock, MerchantKeyGenerator.merchantDataKey(memMerchant.getMerchantId()), 
				MerchantKeyGenerator.tokenMerchantMapKey(), lockId, token, String.valueOf(time), 
				String.valueOf(memMerchant.getMerchantId()), String.valueOf(AppConfig.getRedisLockTimeout()));
		if (0 == flag)
			return false;
		
		try {
			memMerchant.setToken(token);
			// 如果是正常的登录，则还需要将数据同步到 DB 中,(创建用户的在登陆之前已经把数据插入 DB 因此不需要再次同步)
			if (!create) {
				memMerchant.setUpdated(time);
				memMerchant.setLastLoginTime(time);
				memMerchantMapper.update(memMerchant);
			}
			return true;
		} finally {
			unLock(lockId);
		}
	}
	
	/**
	 * 退出
	 * 
	 */
	public void logout()  {
		redisOperate.hdel(MerchantKeyGenerator.tokenMerchantMapKey(), memMerchant.getToken());
		redisOperate.hdel(MerchantKeyGenerator.merchantDataKey(memMerchant.getMerchantId()), "token");
	}
	
	/**
	 * 阿里云鉴权
	 * 
	 */
	public AssumeRoleForm assumeRole() { 
		String key = MerchantKeyGenerator.aliyunStsTokenDataKey(memMerchant.getMerchantId());
		AssumeRoleForm form = redisOperate.hgetAll(key, new AssumeRoleForm());
		if (null == form) {
			AssumeRoleResponse response = aliyunService.getStsToken(memMerchant.getMerchantId());
			form = new AssumeRoleForm(response);
			redisOperate.hmset(key, form);
			long expire = DateUtils.getTimeGap(form.getExpiration(), DateUtils.getUTCDate(), DateUtils.ISO8601_UTC, DateUtils.TIMEZONE_UTC);
			// 提前 5 分钟失效
			expire -= 60 * 5;
			redisOperate.expire(key, (int) (expire / 1000));
		}
		return form;
	}
	
	/**
	 * 修改个人信息
	 * 
	 * @return
	 */
	public void editInfo(String name, String address) { 
		memMerchant.setAddress(address);
		memMerchant.setName(name);
		memMerchant.setUpdated(DateUtils.currentTime());
		_updateMerchant();
	}
	
	/**
	 * 添加新客户
	 * 
	 */
	public MemCustomer addCustomer(String name, String mobile, String address, String memo, String IDNumber) { 
		MemCustomer customer = BeanCreator.newMemCustomer(memMerchant.getMerchantId(), name, mobile, address, memo, IDNumber);
		_insertCustomer(customer);
		String member = String.valueOf(customer.getCustomerId());
		// 尝试将客户添加到商户排序列表中(如果商户排序列表还没有加载，则不会添加)
		long merchantId = customer.getMerchantId();
		luaOperate.evalLua(JiLuLuaCommand.CUSTOMER_LIST_ADD.name(), 5, 
				MerchantKeyGenerator.customerListLoadTimeKey(merchantId),
				CustomerListType.PURCHASE_SUM.redisCustomerListKey(merchantId),
				CustomerListType.PURCHASE_RECENT.redisCustomerListKey(merchantId),
				CustomerListType.PURCHASE_FREQUENCY.redisCustomerListKey(merchantId),
				CustomerListType.NAME.redisCustomerListKey(merchantId),
				member, String.valueOf((int) customer.getNamePrefixLetter().charAt(0)));
		return customer;
	}
	
	/**
	 * 修改客户
	 * 
	 */
	public String editCustomer(long customerId, String name, String mobile, String address, String memo) { 
		MemCustomer customer = getCustomer(customerId);
		if (null == customer)
			return Result.jsonError(JiLuCode.CUSTOMER_NOT_EXIST);
		
		String namePrefixLetter = CnToSpell.getFirstChar(name);
		boolean nameSort = !namePrefixLetter.equals(customer.getNamePrefixLetter());
		customer.setName(name);
		customer.setNamePrefixLetter(namePrefixLetter);
		customer.setAddress(address);
		customer.setMobile(mobile);
		customer.setMemo(memo);
		_updateCustomer(customer, nameSort);
		return Result.jsonSuccess();
	}
	
	/**
	 * 获取客户列表
	 * 
	 * @param type
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String customerList(CustomerListType type, int page, int pageSize) { 
		String key = type.redisCustomerListKey(memMerchant.getMerchantId());
		long count = _customerCount(memMerchant.getMerchantId(), type);
		if (0 == count)
			return Result.jsonSuccess(Pager.EMPTY);
		
		long total = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
		if (total < page || page < 1)
			return Result.jsonSuccess(Pager.EMPTY);
		int start = (page - 1) * pageSize;
		int end = start + pageSize - 1;
		Set<Tuple> set = redisOperate.zrangeWithScores(key, start, end);
		List<MemCustomer> list = memCustomerMapper.getCustomersByIds(set);
		List<CustomerPagerForm> form = new ArrayList<CustomerPagerForm>();
		
		for (Tuple tuple : set) {
			Iterator<MemCustomer> iterator = list.iterator();
			long customerId = Long.valueOf(tuple.getElement());
			while (iterator.hasNext()) {
				MemCustomer customer = iterator.next();
				if (customer.getCustomerId() == customerId) {
					iterator.remove();
					form.add(type == CustomerListType.PURCHASE_FREQUENCY ? new CustomerFrequencyPagerForm(customer) : new CustomerPagerForm(customer));
				}
			}
		}
		return Result.jsonSuccess(new Pager<CustomerPagerForm>(total, form));
	}
	
	private long _customerCount(long merchantId, CustomerListType type) {
		String key = MerchantKeyGenerator.customerListLoadTimeKey(merchantId);
		String zeroTime = String.valueOf(DateUtils.zeroTime());
		String time = luaOperate.evalLua(JiLuLuaCommand.CUSTOMER_LIST_REFRESH_TIME.name(), 1, key, String.valueOf(type.mark()), zeroTime);
		if (null == time) 
			return _loadCustomerList(merchantId);
		if (null != time && type == CustomerListType.PURCHASE_FREQUENCY && !zeroTime.equals(time))
			_refreshCustomerListFrequency(merchantId);
		return redisOperate.zcount(type.redisCustomerListKey(merchantId));
	}
	
	private int _loadCustomerList(long merchantId) {
		List<MemCustomer> list = memCustomerMapper.getMerchantCustomers(merchantId);
		if (list.isEmpty())
			return 0;
		Map<String, Double> map = new HashMap<String, Double>(list.size());
		redisOperate.del(CustomerListType.NAME.redisCustomerListKey(merchantId),
				CustomerListType.PURCHASE_SUM.redisCustomerListKey(merchantId),
				CustomerListType.PURCHASE_RECENT.redisCustomerListKey(merchantId),
				CustomerListType.PURCHASE_FREQUENCY.redisCustomerListKey(merchantId));
		_loadCustomerList(merchantId, list, map, CustomerListType.NAME);
		_loadCustomerList(merchantId, list, map, CustomerListType.PURCHASE_SUM);
		_loadCustomerList(merchantId, list, map, CustomerListType.PURCHASE_RECENT);
		
		// 购物频率还要去订单表实时获取(每天只会获取一次)
		_loadCustomerList(merchantId, list, map, CustomerListType.PURCHASE_FREQUENCY);
		_refreshCustomerListFrequency(merchantId);
		return list.size();
	}
	
	private void _loadCustomerList(long merchantId, List<MemCustomer> list, Map<String, Double> map, CustomerListType type) { 
		for (MemCustomer model : list)
			map.put(String.valueOf(model.getCustomerId()), model.getScore(type));
		redisOperate.zadd(type.redisCustomerListKey(merchantId), map);
	}
	
	private void _refreshCustomerListFrequency(long merchantId) { 
		int end = DateUtils.currentTime();
		int start = end - DateUtils.MONTH_SECONDS;
		List<CustomerListPurchaseFrequencyModel> list = memOrderMapper.getMerchantOrderCountGroupByCustomerBetweenTime(merchantId, start, end);
		if (list.isEmpty())
			return;
		Map<String, Double> map = new HashMap<String, Double>();
		for (CustomerListPurchaseFrequencyModel model : list)
			map.put(String.valueOf(model.getCustomerId()), Double.valueOf(model.getCount()));
		redisOperate.zadd(CustomerListType.PURCHASE_FREQUENCY.redisCustomerListKey(merchantId), map);
	}
	
	// ************************************************************
	
	/**
	 * 更新 mem_merchant
	 * 
	 */
	private void _updateMerchant() { 
		memMerchant.setUpdated(DateUtils.currentTime());
		memMerchantMapper.update(memMerchant);
		redisOperate.hmset(memMerchant.redisKey(), BeanUtils.beanToMap(memMerchant));
	}
	
	private void _insertCustomer(MemCustomer customer) { 
		memCustomerMapper.insert(customer);
		String key = MerchantKeyGenerator.customerDataKey(customer.getMerchantId());
		redisOperate.hset(key, String.valueOf(customer.getCustomerId()), customer.toString());
	}
	
	public MemCustomer getCustomer(long customerId) { 
		String key = MerchantKeyGenerator.customerDataKey(memMerchant.getMerchantId());
		String val = redisOperate.hget(key, String.valueOf(customerId));
		if (null != val) 
			return SerializeUtil.JsonUtil.GSON.fromJson(val, MemCustomer.class);
		MemCustomer customer = memCustomerMapper.getMerchantCustomerById(memMerchant.getMerchantId(), customerId);
		if (null != customer) 
			redisOperate.hset(key, String.valueOf(customer.getCustomerId()), SerializeUtil.JsonUtil.GSON.toJson(customer));
		return customer;
	}
	
	private void _updateCustomer(MemCustomer customer, boolean nameSort) { 
		customer.setUpdated(DateUtils.currentTime());
		memCustomerMapper.update(customer);
		redisOperate.hset(MerchantKeyGenerator.customerDataKey(memMerchant.getMerchantId()), String.valueOf(customer.getCustomerId()), customer.toString());
		if (nameSort && _isCustomerListLoaded(customer.getMerchantId()))
			redisOperate.zadd(CustomerListType.NAME.redisCustomerListKey(customer.getMerchantId()), Double.valueOf((int) customer.getNamePrefixLetter().charAt(0)), String.valueOf(customer.getCustomerId()));
	}
	
	/**
	 * 好友列表
	 * 
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Pager<PubRelation> friendList(int page, int pageSize) {
		return null;
	}
	
	/**
	 * 创建订单
	 * 
	 * @throws Exception
	 */
	@Transactional
	public void createOrder(MemOrder order,List<MemOrderGoods> list) {
		memOrderMapper.insert(order);
		memOrderGoodsMapper.batchInsert(list);
		redisOperate.hmset(order.redisKey(), order);
		redisOperate.batchHmset(list);
	}
	
	/**
	 * 创建订单
	 * 
	 * @throws Exception
	 */
	@Transactional
	public String createOrder(MemCustomer customer,List<MemOrderGoods> list) {
		String orderId = System.currentTimeMillis()+""+new Random().nextInt(10);
		for(MemOrderGoods ogs: list){
			CfgGoods goods = getGoodsById(ogs.getGoodsId());
			if(goods == null)
				return Result.jsonError(JiLuCode.GOODS_NOT_EXIST.constId(), MessageFormat.format(JiLuCode.GOODS_NOT_EXIST.defaultValue(), ogs.getGoodsId()));
			ogs.setOrderId(orderId);
			ogs.setGoodsName(goods.getZhName());
			ogs.setStatus(0);
			int time = DateUtils.currentTime();
			ogs.setCreated(time);
			ogs.setUpdated(time);
		}
		MemOrder order = BeanCreator.newMemOrder(orderId, memMerchant.getMerchantId(), memMerchant.getName(), memMerchant.getAddress(),
				customer.getCustomerId(), customer.getName(), customer.getMobile(), customer.getAddress(),0);
		memOrderMapper.insert(order);
		memOrderGoodsMapper.batchInsert(list);
		redisOperate.hmset(order.redisKey(), order);
		redisOperate.batchHmset(list);
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
	public void updateOrder(MemOrder order,List<MemOrderGoods> addGoodsList,List<MemOrderGoods> updateGoodsList,List<MemOrderGoods> deleteGoodsList){
		if(addGoodsList!=null){
			memOrderGoodsMapper.batchInsert(addGoodsList);
			redisOperate.batchHmset(addGoodsList);
		}
		if (updateGoodsList != null) {
			memOrderGoodsMapper.batchUpdate(updateGoodsList);
			redisOperate.batchHmset(updateGoodsList);
		}
		if (deleteGoodsList != null) {
			memOrderGoodsMapper.batchDelete(deleteGoodsList);
			redisOperate.batchDelete(deleteGoodsList);
		}
	}

	/**
	 * 根据商户号和订单号获取订单基本信息
	 * 
	 * @param orderId
	 * @return
	 */
	public MemOrder getMerchantOrderById(long merchantId,String orderId){
		String key = MerchantKeyGenerator.merchantOrderDataKey(merchantId, orderId);
		MemOrder order = redisOperate.hgetAll(key, new MemOrder());
		if(null !=order)
			return order;
		order = memOrderMapper.getOrderById(merchantId, orderId);
		if (null != order)
			redisOperate.hmset(key, order);
		return order;
	}

	/**
	 * 获取MerchantOrderGoods列表 通过List<MerchantOrderGoods>
	 * 
	 * @param ids
	 * @return
	 */
	public List<MemOrderGoods> getOGListByMerchantOrderGoodsList(List<MemOrderGoods> list){
		return memOrderGoodsMapper.getMerchantOrderGoodsByList(list);
	}

	/**
	 * 订单确认
	 * 
	 * @param orderId
	 */
	public void orderLock(MemOrder order){
		memOrderMapper.update(order);
		redisOperate.hmset(order.redisKey(),order);
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
	public MemOrder orderChange(MemOrder superOrder,MemMerchant memMerchant,List<MemOrderGoods> ogs){
		superOrder.setSuperOrderId(superOrder.getOrderId());
		superOrder.setOrderId(System.currentTimeMillis() + "" + new Random().nextInt(10));
		int time = DateUtils.currentTime();
		superOrder.setCreated(time);
		superOrder.setUpdated(time);
		superOrder.setSuperMerchantId(getMemMerchant().getMerchantId());
		superOrder.setSuperMerchantName(getMemMerchant().getName());
		superOrder.setMerchantId(memMerchant.getMerchantId());
		superOrder.setMerchantName(memMerchant.getName());
		superOrder.setMerchantAddress(memMerchant.getAddress());
		superOrder.setStatus(2);
		memOrderMapper.insert(superOrder);
		//处理产品列表
		for(MemOrderGoods goods : ogs){
			goods.setStatus(2);
			goods.setUpdated(DateUtils.currentTime());
		}
		memOrderGoodsMapper.batchUpdate(ogs);
		redisOperate.batchHmset(ogs);
		for(MemOrderGoods goods : ogs){
			goods.setOrderId(superOrder.getOrderId());
			goods.setUpdated(DateUtils.currentTime());
			goods.setCreated(DateUtils.currentTime());
		}
		memOrderGoodsMapper.batchInsert(ogs);
		redisOperate.hmset(superOrder.redisKey(), superOrder);
		redisOperate.batchHmset(ogs);
		return superOrder;
	}

	/**
	 * 插入商品
	 * 
	 * @param memGoods
	 */
	public void insertGoods(CfgGoods memGoods) {
		cfgGoodsMapper.insert(memGoods);
		redisOperate.hmset(memGoods.redisKey(), memGoods);
	}
	
	public MemOrderGoods getMerchantOrderGoodsById(String orderId,long goodsId){
		String key = MerchantKeyGenerator.merchantOrderGoodsDataKey(orderId, goodsId);
		MemOrderGoods merchantOrderGoods = redisOperate.hgetAll(key, new MemOrderGoods());
		if(merchantOrderGoods != null)
			return merchantOrderGoods;
		merchantOrderGoods = memOrderGoodsMapper.getMerchantOrderGoodsByOrderId(orderId, goodsId);
		if (null != merchantOrderGoods)
			redisOperate.hmset(merchantOrderGoods.redisKey(), merchantOrderGoods);
		return merchantOrderGoods;
	}

	public CfgGoods getGoodsById(long goodsId) {
		String key = CommonKeyGenerator.getMemGoodsKey(goodsId);
		CfgGoods goods = redisOperate.hgetAll(key, new CfgGoods(goodsId));
		if (goods != null)
			return goods;
		goods = cfgGoodsMapper.getGoodsById(goodsId);
		if (null != goods)
			redisOperate.hmset(key, goods);
		return goods;
	}

	/**
	 * 判断订单列表是否可以进行转单或者修改
	 * 
	 * @param list
	 * @return
	 */
	public boolean isChangedMerchantOrderGoods(List<MemOrderGoods> list){
		for(MemOrderGoods orderGoods : list){
			if(orderGoods.getStatus() != 0)
				return false;
		}
		return true;
	}

	/**
	 * 获取转单订单列表
	 * 
	 * @param merchantId
	 * @return
	 */
	public List<MemOrder> getChangeOrderListByMerchantId(long merchantId){
		return memOrderMapper.getChangeMerchantOrderList(merchantId);
	}
	
	public List<OrderChangeModel> getOrderChangeListModelList(List<MemOrder> mList){
		List<OrderChangeModel> orderChangeModels = new ArrayList<>();
		for(MemOrder order : mList){
			List<MemOrderGoods> list = memOrderGoodsMapper.getChangeMerchantOrderGoodsByOrderId(order.getOrderId());
			orderChangeModels.add(new OrderChangeModel(order.getSuperMerchantName(), list));
		}
		return orderChangeModels;
	}

	/**
	 * 拒绝转单操作
	 * 
	 * @param orderId
	 */
	@Transactional
	public void refuseOrder(String orderId, String superOrderId, long merchantId) {
		// 删除转单订单即子订单
		memOrderMapper.delete(merchantId, orderId);
		// 删除转单产品列表
		List<MemOrderGoods> list = memOrderGoodsMapper.getChangeMerchantOrderGoodsByOrderId(orderId);
		memOrderGoodsMapper.batchDelete(list);
		// 更新父订单产品状态
		List<MemOrderGoods> superGoodsList = new ArrayList<MemOrderGoods>();
		for (MemOrderGoods merchantOrderGoods : list) {
			MemOrderGoods mOrderGoods = redisOperate.hgetAll(MerchantKeyGenerator.merchantOrderGoodsDataKey(superOrderId, merchantOrderGoods.getGoodsId()), new MemOrderGoods());
			mOrderGoods.setStatus(0);
			mOrderGoods.setUpdated(DateUtils.currentTime());
			superGoodsList.add(mOrderGoods);
		}
		memOrderGoodsMapper.batchUpdate(superGoodsList);
		// 更新缓存
		redisOperate.del(MerchantKeyGenerator.merchantOrderDataKey(merchantId, orderId));
		for (MemOrderGoods goods : list) {
			redisOperate.del(MerchantKeyGenerator.merchantOrderGoodsDataKey(orderId, goods.getGoodsId()));
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
			long merchantId) {
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
			mOrderGoods.setStatus(3);
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
	 * 判断商户的客户排序列表是否已经加载
	 * 
	 * @param merchantId
	 * @return
	 */
	private boolean _isCustomerListLoaded(long merchantId) {
		return 1 == redisOperate.exist(MerchantKeyGenerator.customerListLoadTimeKey(merchantId));
	}
	
	public MemMerchant getMemMerchant() {
		return memMerchant;
	}
	
	public String tryLock() { 
		return distributeLock.tryLock(lock);
	}
	
	public void unLock(String lockId) {
		if (!distributeLock.unLock(lock, lockId))
			logger.warn("Merchant lock {} release failure for lockId {}!", lock, lockId);
	}
}
