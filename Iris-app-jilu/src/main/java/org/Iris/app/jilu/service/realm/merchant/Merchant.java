package org.Iris.app.jilu.service.realm.merchant;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.bean.enums.CustomerListType;
import org.Iris.app.jilu.common.bean.enums.GoodsListType;
import org.Iris.app.jilu.common.bean.enums.JiLuLuaCommand;
import org.Iris.app.jilu.common.bean.enums.MerchantStatusMod;
import org.Iris.app.jilu.common.bean.form.AssumeRoleForm;
import org.Iris.app.jilu.common.bean.form.CustomerForm;
import org.Iris.app.jilu.common.bean.form.CustomerFrequencyPagerForm;
import org.Iris.app.jilu.common.bean.form.CustomerPagerForm;
import org.Iris.app.jilu.common.bean.form.GoodsPagerForm;
import org.Iris.app.jilu.common.bean.form.MerchantForm;
import org.Iris.app.jilu.common.bean.form.OrderForm;
import org.Iris.app.jilu.common.bean.form.OrderGoodsForm;
import org.Iris.app.jilu.common.bean.form.OrderPacketForm;
import org.Iris.app.jilu.common.bean.form.Pager;
import org.Iris.app.jilu.common.bean.model.CustomerListPurchaseFrequencyModel;
import org.Iris.app.jilu.common.bean.model.OrderChangeModel;
import org.Iris.app.jilu.common.bean.model.OrderDetailedModel;
import org.Iris.app.jilu.common.bean.model.TransferOrderModel;
import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemGoodsStore;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.domain.MemOrderGoods;
import org.Iris.app.jilu.storage.domain.MemOrderPacket;
import org.Iris.app.jilu.storage.domain.MemOrderStatus;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.CnToSpell;
import org.Iris.util.common.IrisSecurity;
import org.Iris.util.common.SerializeUtil;
import org.Iris.util.common.uuid.AlternativeJdkIdGenerator;
import org.Iris.util.lang.DateUtils;
import org.Iris.util.reflect.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public void logout() {
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
			long expire = DateUtils.getTimeGap(form.getExpiration(), DateUtils.getUTCDate(), DateUtils.ISO8601_UTC,
					DateUtils.TIMEZONE_UTC);
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
		memMerchant.setStatusMod(MerchantStatusMod.QUALIFIED.mod());
		_updateMerchant();
	}

	/**
	 * 添加新客户
	 * 
	 */
	public MemCustomer addCustomer(String name, String mobile, String address, String memo, String IDNumber) {
		MemCustomer customer = BeanCreator.newMemCustomer(memMerchant.getMerchantId(), name, mobile, address, memo,
				IDNumber);
		_insertCustomer(customer);
		String member = String.valueOf(customer.getCustomerId());
		// 尝试将客户添加到商户排序列表中(如果商户排序列表还没有加载，则不会添加)
		long merchantId = customer.getMerchantId();
		luaOperate.evalLua(JiLuLuaCommand.CUSTOMER_LIST_ADD.name(), 5,
				MerchantKeyGenerator.customerListLoadTimeKey(merchantId),
				CustomerListType.PURCHASE_SUM.redisCustomerListKey(merchantId),
				CustomerListType.PURCHASE_RECENT.redisCustomerListKey(merchantId),
				CustomerListType.PURCHASE_FREQUENCY.redisCustomerListKey(merchantId),
				CustomerListType.NAME.redisCustomerListKey(merchantId), member,
				String.valueOf((int) customer.getNamePrefixLetter().charAt(0)));
		return customer;
	}

	/**
	 * 修改客户
	 * 
	 */
	public String editCustomer(long customerId, String name, String mobile, String address, String memo, int mod) {
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
		customer.setStatusMod(mod);
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
					form.add(type == CustomerListType.PURCHASE_FREQUENCY ? new CustomerFrequencyPagerForm(customer)
							: new CustomerPagerForm(customer));
				}
			}
		}
		return Result.jsonSuccess(new Pager<CustomerPagerForm>(total, form));
	}

	private long _customerCount(long merchantId, CustomerListType type) {
		String key = MerchantKeyGenerator.customerListLoadTimeKey(merchantId);
		String zeroTime = String.valueOf(DateUtils.zeroTime());
		String time = luaOperate.evalLua(JiLuLuaCommand.CUSTOMER_LIST_REFRESH_TIME.name(), 1, key,
				String.valueOf(type.mark()), zeroTime);
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

	private void _loadCustomerList(long merchantId, List<MemCustomer> list, Map<String, Double> map,
			CustomerListType type) {
		for (MemCustomer model : list)
			map.put(String.valueOf(model.getCustomerId()), model.getScore(type));
		redisOperate.zadd(type.redisCustomerListKey(merchantId), map);
	}

	private void _refreshCustomerListFrequency(long merchantId) {
		int end = DateUtils.currentTime();
		int start = end - DateUtils.MONTH_SECONDS;
		List<CustomerListPurchaseFrequencyModel> list = memOrderMapper
				.getMerchantOrderCountGroupByCustomerBetweenTime(merchantId, start, end);
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
			redisOperate.hset(key, String.valueOf(customer.getCustomerId()),
					SerializeUtil.JsonUtil.GSON.toJson(customer));
		return customer;
	}

	private void _updateCustomer(MemCustomer customer, boolean nameSort) {
		customer.setUpdated(DateUtils.currentTime());
		memCustomerMapper.update(customer);
		redisOperate.hset(MerchantKeyGenerator.customerDataKey(memMerchant.getMerchantId()),
				String.valueOf(customer.getCustomerId()), customer.toString());
		if (nameSort && _isCustomerListLoaded(customer.getMerchantId()))
			redisOperate.zadd(CustomerListType.NAME.redisCustomerListKey(customer.getMerchantId()),
					Double.valueOf((int) customer.getNamePrefixLetter().charAt(0)),
					String.valueOf(customer.getCustomerId()));
	}

	/**
	 * 根据商户号和订单号获取订单基本信息
	 * 
	 * @param orderId
	 * @return
	 */
	public MemOrder getMerchantOrderById(long merchantId, String orderId) {
		String key = MerchantKeyGenerator.merchantOrderDataKey(merchantId, orderId);
		MemOrder order = redisOperate.hgetAll(key, new MemOrder());
		if (null != order)
			return order;
		order = memOrderMapper.getOrderById(merchantId, orderId);
		if (null != order)
			redisOperate.hmset(key, order);
		return order;
	}

	/**
	 * 查询订单号是否存在
	 * 
	 * @param orderId
	 * @return
	 */
	public MemOrder getOrderByOrderId(String orderId) {
		MemOrder order = memOrderMapper.getOrderByOrderId(orderId);
		return order;
	}

	/**
	 * 查询快递单号是否存在
	 * 
	 * @param orderId
	 * @return
	 */
	public MemOrderPacket getMemOrderPacketByExpressCode(String expressCode) {
		MemOrderPacket packet = memOrderPacketMapper.getMemOrderPacketByExpressCode(expressCode);
		return packet;
	}

	/**
	 * 通过根订单号查询所有关联的子订单和父订单信息
	 * 
	 * @param orderId
	 * @return
	 */
	public String queryAllOrderByOrderId(String rootOrderId) {
		List<MemOrder> list = memOrderMapper.getAllOrderByRootOrderId(rootOrderId);
		List<OrderForm> orderForms = new ArrayList<OrderForm>();
		for (MemOrder order : list)
			orderForms.add(new OrderForm(order));
		return Result.jsonSuccess(orderForms);
	}

	/**
	 * 获取MerchantOrderGoods列表 通过List<MerchantOrderGoods>
	 * 
	 * @param ids
	 * @return
	 */
	public List<MemOrderGoods> getOGListByMerchantOrderGoodsList(List<MemOrderGoods> list) {
		return memOrderGoodsMapper.getMerchantOrderGoodsByList(list);
	}

	/**
	 * 订单确认
	 * 
	 * @param orderId
	 */
	public void orderLock(MemOrder order) {
		memOrderMapper.update(order);
		redisOperate.hmset(order.redisKey(), order);
	}

	/**
	 * 插入商品
	 * 
	 * @param memGoods
	 */
	public void insertGoods(CfgGoods memGoods) {
		cfgGoodsMapper.insert(memGoods);
		// 目前在商户添加商品之后自动生成一条该商户商品的产品库信息
		MemGoodsStore store = new MemGoodsStore(getMemMerchant().getMerchantId(), memGoods.getGoodsId(),
				memGoods.getZhName(), 100);
		memGoodsStoreMapper.insert(store);
		redisOperate.hmset(memGoods.redisKey(), memGoods);
		redisOperate.hmset(store.redisKey(), store);
	}
	/**
	 * 删除商品
	 * @param goodsId
	 * @return
	 */
	public String removeGoods(long goodsId){
		CfgGoods goods = getGoodsById(goodsId);
		if(goods == null)
			throw IllegalConstException.errorException(JiLuParams.GOODS_ID);
		if(!goods.getSource().equals(String.valueOf(getMemMerchant().getMerchantId())))
			return Result.jsonError(JiLuCode.GOODS_DELETE_LIMIT.constId(), MessageFormat.format(JiLuCode.GOODS_DELETE_LIMIT.defaultValue(), goodsId));
		cfgGoodsMapper.delete(goods);
		redisOperate.del(goods.redisKey());
		return Result.jsonSuccess();
	}

	/**
	 * 更新商品
	 * 
	 * @param memGoods
	 */
	public String updateGoods(long goodsId, String zhName, String usName, String goodsFormat, String classification,
			String zhBrand, String usBrand, String unit, float weight, String alias, String barcode, String sku,
			float unitPrice) {
		CfgGoods memGoods = getGoodsById(goodsId);
		if (memGoods == null)
			throw IllegalConstException.errorException(JiLuParams.GOODS_ID);
		if (zhName != null)
			memGoods.setZhName(zhName);
		if (usName != null)
			memGoods.setUsName(usName);
		if (goodsFormat != null)
			memGoods.setGoodsFormat(goodsFormat);
		if (classification != null)
			memGoods.setClassification(classification);
		if (zhBrand != null)
			memGoods.setZhBrand(zhBrand);
		if (usBrand != null)
			memGoods.setUsBrand(usBrand);
		if (unit != null)
			memGoods.setUnit(unit);
		if (weight != 0)
			memGoods.setWeight(weight);
		if (alias != null)
			memGoods.setAlias(alias);
		if (barcode != null)
			memGoods.setBarcode(barcode);
		if (sku != null)
			memGoods.setSku(sku);
		if (unitPrice != 0)
			memGoods.setUnitPrice(unitPrice);
		int time = DateUtils.currentTime();
		memGoods.setUpdated(time);
		cfgGoodsMapper.update(memGoods);
		redisOperate.hmset(memGoods.redisKey(), memGoods);
		return Result.jsonSuccess(new GoodsPagerForm(memGoods));
	}

	public MemOrderGoods getMerchantOrderGoodsById(String orderId, long goodsId) {
		String key = MerchantKeyGenerator.merchantOrderGoodsDataKey(orderId, goodsId);
		MemOrderGoods merchantOrderGoods = redisOperate.hgetAll(key, new MemOrderGoods());
		if (merchantOrderGoods != null)
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
	public boolean isChangedMerchantOrderGoods(List<MemOrderGoods> list) {
		for (MemOrderGoods orderGoods : list) {
			if (orderGoods.getStatus() != 0)
				return false;
		}
		return true;
	}

	/**
	 * 获取转单申请列表
	 * 
	 * @param merchantId
	 * @return
	 */
	public List<MemOrder> getChangeOrderListByMerchantId(long merchantId) {
		return memOrderMapper.getChangeMerchantOrderList(merchantId);
	}

	/**
	 * 获取待转订单列表
	 * 
	 * @param merchantId
	 * @return
	 */
	public List<MemOrder> getTransferOrderListByMerchantId(long superMerchantId) {
		return memOrderMapper.getTransferMerchantOrderList(superMerchantId);
	}

	public List<OrderChangeModel> getOrderChangeListModelList(List<MemOrder> mList) {
		List<OrderChangeModel> orderChangeModels = new ArrayList<>();
		for (MemOrder order : mList) {
			List<MemOrderGoods> list = memOrderGoodsMapper.getChangeMerchantOrderGoodsByOrderId(order.getOrderId());
			orderChangeModels.add(new OrderChangeModel(order.getOrderId(), order.getSuperMerchantName(),
					order.getSuperMerchantId(), order.getStatus(), list));
		}
		return orderChangeModels;
	}

	public List<TransferOrderModel> getTransferOrderListModelList(List<MemOrder> mList) {
		List<TransferOrderModel> orderChangeModels = new ArrayList<>();
		for (MemOrder order : mList) {
			List<MemOrderGoods> list = memOrderGoodsMapper.getChangeMerchantOrderGoodsByOrderId(order.getOrderId());
			orderChangeModels.add(new TransferOrderModel(order.getOrderId(), order.getMerchantName(),
					order.getMerchantId(), order.getStatus(), list));
		}
		return orderChangeModels;
	}

	/**
	 * 添加邮费
	 * 
	 * @param packetId
	 *            邮单号
	 * @param postage
	 *            邮费
	 * @param express
	 *            快递公司
	 * @param memo
	 *            备注
	 */
	public String addPostage(String packetId, String postage, String express, String memo) {
		MemOrderPacket packet = getMemOrderPacket(packetId);
		if (packet == null)
			return Result.jsonError(JiLuCode.PACKET_NOT_EXIST.constId(),
					MessageFormat.format(JiLuCode.PACKET_NOT_EXIST.defaultValue(), packetId));
		packet.setPostage(postage);
		packet.setExpress(express);
		packet.setMemo(memo);
		packet.setUpdated(DateUtils.currentTime());
		memOrderPacketMapper.update(packet);
		redisOperate.hmset(packet.redisKey(), packet);
		return Result.jsonSuccess(packet);
	}

	/**
	 * 添加快递单
	 * 
	 * @param packetId
	 * @param expressCode
	 *            快递号
	 * @return
	 */
	public String addExpress(String packetId, String expressCode) {
		MemOrderPacket packet = getMemOrderPacket(packetId);
		if (packet == null)
			return Result.jsonError(JiLuCode.PACKET_NOT_EXIST.constId(),
					MessageFormat.format(JiLuCode.PACKET_NOT_EXIST.defaultValue(), packetId));
		packet.setExpressCode(expressCode);
		packet.setUpdated(DateUtils.currentTime());
		memOrderPacketMapper.update(packet);
		redisOperate.hmset(packet.redisKey(), packet);
		return Result.jsonSuccess(packet);
	}

	/**
	 * 添加邮包标签
	 * 
	 * @param packetId
	 * @param label
	 *            标签
	 * @param latitude
	 *            纬度
	 * @param longitude
	 *            经度
	 * @return
	 */
	public String addPacketLabel(String packetId, String label, String latitude, String longitude) {
		MemOrderPacket packet = getMemOrderPacket(packetId);
		if (packet == null)
			return Result.jsonError(JiLuCode.PACKET_NOT_EXIST.constId(),
					MessageFormat.format(JiLuCode.PACKET_NOT_EXIST.defaultValue(), packetId));
		packet.setLabel(label);
		packet.setLatitude(latitude);
		packet.setLongitude(longitude);
		packet.setUpdated(DateUtils.currentTime());
		memOrderPacketMapper.update(packet);
		redisOperate.hmset(packet.redisKey(), packet);
		return Result.jsonSuccess(packet);
	}

	public MemOrderPacket getMemOrderPacket(String packetId) {
		MemOrderPacket packet = redisOperate.hgetAll(
				MerchantKeyGenerator.merchantOrderPacketDataKey(getMemMerchant().getMerchantId(), packetId),
				new MemOrderPacket());
		if (null == packet)
			packet = memOrderPacketMapper.getMemOrderPacketById(packetId, getMemMerchant().getMerchantId());
		return packet;
	}

	/**
	 * 根据订单号查询本订单详细信息 ，以及所有子订单基本信息
	 * 
	 * @param orderId
	 * @return
	 */
	public String getOrderDetailedInfoByOrderId(String orderId) {
		// 获取本订单基本信息
		OrderForm orderInfo = new OrderForm(getOrderByOrderId(orderId));
		// 获取未完成产品
		List<MemOrderGoods> notFinishOrderGoods = memOrderGoodsMapper.getNotFinishMerchantOrderGoodsByOrderId(orderId);
		List<OrderGoodsForm> notFinishGoodsList = new ArrayList<OrderGoodsForm>();
		for (MemOrderGoods memOrderGoods : notFinishOrderGoods)
			notFinishGoodsList.add(new OrderGoodsForm(memOrderGoods));
		// 获取正在转单中的产品
		List<MemOrderGoods> changeingOrderGoods = memOrderGoodsMapper.getChangeMerchantOrderGoodsByOrderId(orderId);
		List<OrderGoodsForm> changeGoodsList = new ArrayList<OrderGoodsForm>();
		for (MemOrderGoods memOrderGoods : changeingOrderGoods)
			changeGoodsList.add(new OrderGoodsForm(memOrderGoods));
		// 获取已打包的信息
		List<MemOrderPacket> packets = memOrderPacketMapper.getMemOrderPacketByOrderId(orderId);
		List<OrderPacketForm> packetList = new ArrayList<OrderPacketForm>();
		for (MemOrderPacket memOrderPacket : packets)
			packetList.add(new OrderPacketForm(memOrderPacket));
		// 获取子订单信息
		List<MemOrder> childOrders = memOrderMapper.getChildOrderByOrderId(orderId);
		List<OrderForm> childOrderList = new ArrayList<OrderForm>();
		for (MemOrder order : childOrders)
			childOrderList.add(new OrderForm(order));

		return Result.jsonSuccess(
				new OrderDetailedModel(orderInfo, notFinishGoodsList, changeGoodsList, packetList, childOrderList));
	}

	/**
	 * 获取订单状态对象
	 * 
	 * @param orderId
	 * @return
	 */
	public MemOrderStatus getMemOrderStatusByOrderId(String orderId) {
		MemOrderStatus status = redisOperate.hgetAll(MerchantKeyGenerator.merchantOrderStatusDataKey(orderId),
				new MemOrderStatus());
		if (status == null)
			status = memOrderStatusMapper.getMemOrderStatusByOrderId(orderId);
		return status;
	}

	/**
	 * 获取商户产品库列表
	 * 
	 * @return
	 */
	public String getGoodsStoreList(int page, int pageSize) {
		long count = memGoodsStoreMapper.getCountBymerchantId(getMemMerchant().getMerchantId());
		if (count == 0)
			return Result.jsonSuccess(Pager.EMPTY);
		List<MemGoodsStore> list = memGoodsStoreMapper.getMemGoodsStoreList((page - 1) * pageSize, pageSize,
				getMemMerchant().getMerchantId());
		return Result.jsonSuccess(new Pager<MemGoodsStore>(count, list));
	}

	public String getGoodsList(int page, int pageSize, String name, GoodsListType type) {
		long count;
		List<CfgGoods> list;
		switch (type) {
		case GOODSNAME:
			if (name == null)
				throw IllegalConstException.errorException(JiLuParams.NAME);
			count = cfgGoodsMapper.getCountByGoodsName(name);
			if (count == 0)
				return Result.jsonSuccess(Pager.EMPTY);
			list = cfgGoodsMapper.getGoodsListByGoodsName((page - 1) * pageSize, pageSize, name);
			return Result.jsonSuccess(new Pager<CfgGoods>(count, list));
		case MERCHANT:
			count = cfgGoodsMapper.getCountByMerchantId(getMemMerchant().getMerchantId());
			if (count == 0)
				return Result.jsonSuccess(Pager.EMPTY);
			list = cfgGoodsMapper.getGoodsListByMerchantId((page - 1) * pageSize, pageSize,
					getMemMerchant().getMerchantId());
			return Result.jsonSuccess(new Pager<CfgGoods>(count, list));
		default:
			throw IllegalConstException.errorException(JiLuParams.TYPE);
		}
	}

	/**
	 * 删除联系人
	 * 
	 * @param customerId
	 * @return
	 */
	public String removeCustomer(long customerId) {
		MemCustomer customer = getCustomer(customerId);
		if (customer == null)
			throw IllegalConstException.errorException(JiLuParams.CUSTOMER_ID);
		memCustomerMapper.delete(customer);
		redisOperate.del(MerchantKeyGenerator.customerDataKey(customerId));
		return Result.jsonSuccess();
	}

	/**
	 * 根据名字或手机号查询联系人列表
	 * 
	 * @param value
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String getMerchantCustomersByNameOrPhone(String value, int page, int pageSize) {
		long count = memCustomerMapper.getCountByNameOrPhone(getMemMerchant().getMerchantId(), value);
		if (count == 0)
			return Result.jsonSuccess(Pager.EMPTY);
		List<MemCustomer> list = memCustomerMapper.getMerchantCustomersByNameOrPhone(getMemMerchant().getMerchantId(),
				value, (page - 1) * pageSize, pageSize);
		List<CustomerForm> forms = new ArrayList<CustomerForm>();
		for (MemCustomer customer : list)
			forms.add(new CustomerForm(customer));
		return Result.jsonSuccess(new Pager<CustomerForm>(count, forms));
	}
	
	/**
	 * 查看商品信息
	 * @param goodsId
	 * @return
	 */
	public String getGoodsInfo(long goodsId){
		CfgGoods goods = getGoodsById(goodsId);
		if(goods == null)
			throw IllegalConstException.errorException(JiLuParams.GOODS_ID);
		return Result.jsonSuccess(goods);
	}
	
	/**
	 * 获取商户信息
	 * @return
	 */
	public String getMerchantInfo(){
		MerchantForm merchantForm = new MerchantForm(this);
		List<MemAccount> list = memAccountMapper.getByMerchantId(getMemMerchant().getMerchantId());
		if(list.size() > 1){
			merchantForm.setPhoneStatus(1);
			merchantForm.setEmailStatus(1);
		}else{
			if(list.get(0).getType() == 0)
				merchantForm.setPhoneStatus(1);
			if(list.get(0).getType() == 1)
				merchantForm.setEmailStatus(1);
		}
		return Result.jsonSuccess(merchantForm);
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
