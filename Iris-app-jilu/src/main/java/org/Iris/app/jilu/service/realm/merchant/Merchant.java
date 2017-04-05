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
import org.Iris.app.jilu.common.JiLuPushUtil;
import org.Iris.app.jilu.common.bean.enums.CustomerListType;
import org.Iris.app.jilu.common.bean.enums.GoodsListType;
import org.Iris.app.jilu.common.bean.enums.JiLuLuaCommand;
import org.Iris.app.jilu.common.bean.enums.MerchantStatusMod;
import org.Iris.app.jilu.common.bean.enums.OrderByType;
import org.Iris.app.jilu.common.bean.form.AllOrderGoodsSumForm;
import org.Iris.app.jilu.common.bean.form.AssumeRoleForm;
import org.Iris.app.jilu.common.bean.form.CfgGoodsForm;
import org.Iris.app.jilu.common.bean.form.CfgGoodsListForm;
import org.Iris.app.jilu.common.bean.form.CustomerForm;
import org.Iris.app.jilu.common.bean.form.CustomerFrequencyPagerForm;
import org.Iris.app.jilu.common.bean.form.CustomerPagerForm;
import org.Iris.app.jilu.common.bean.form.GoodsPagerForm;
import org.Iris.app.jilu.common.bean.form.GoodsStoreAndStockForm;
import org.Iris.app.jilu.common.bean.form.GoodsStoreForm;
import org.Iris.app.jilu.common.bean.form.GoodsStoreSearchForm;
import org.Iris.app.jilu.common.bean.form.MerchantForm;
import org.Iris.app.jilu.common.bean.form.OrderForm;
import org.Iris.app.jilu.common.bean.form.OrderGoodsForm;
import org.Iris.app.jilu.common.bean.form.OrderGoodsStoreInfoForm;
import org.Iris.app.jilu.common.bean.form.OrderPacketForm;
import org.Iris.app.jilu.common.bean.form.Pager;
import org.Iris.app.jilu.common.bean.model.CustomerListPurchaseFrequencyModel;
import org.Iris.app.jilu.common.bean.model.OrderChangeModel;
import org.Iris.app.jilu.common.bean.model.OrderDetailedModel;
import org.Iris.app.jilu.common.bean.model.TransferOrderModel;
import org.Iris.app.jilu.service.realm.wyyx.result.WyyxCreateAccountResultForm;
import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.MemAccid;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.domain.MemCid;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemGoodsStore;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.domain.MemOrderGoods;
import org.Iris.app.jilu.storage.domain.MemOrderPacket;
import org.Iris.app.jilu.storage.domain.MemOrderStatus;
import org.Iris.app.jilu.storage.domain.MemWaitStore;
import org.Iris.app.jilu.storage.domain.StockGoodsStoreLog;
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
			form.setExpireTime(expire/1000);
		}
		long expire = DateUtils.getTimeGap(form.getExpiration(), DateUtils.getUTCDate(), DateUtils.ISO8601_UTC,
				DateUtils.TIMEZONE_UTC);
		form.setExpireTime(expire/1000);
		return form;
	}

	/**
	 * 修改个人信息
	 * 
	 * @return
	 */
	public String editInfo(String name, String address) {
		memMerchant.setAddress(address);
		memMerchant.setName(name);
		memMerchant.setUpdated(DateUtils.currentTime());
		memMerchant.setStatusMod(MerchantStatusMod.QUALIFIED.mod());
		_updateMerchant();
		MerchantForm merchantForm = new MerchantForm(this);
		// 加网易云信账号信息
		MemAccid memAccid = getMemAccid();
		merchantForm.setAccid(memAccid.getAccid());
		merchantForm.setAccidToken(memAccid.getToken());
		return Result.jsonSuccess(merchantForm);
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
	 * 查找自己的所有订单基本信息
	 * 
	 * @return
	 */
	public String getMyOrderList(int page, int pageSize) {
		long count = memOrderMapper.getOrderCountByMerchantId(getMemMerchant().getMerchantId());
		if (count == 0)
			return Result.jsonSuccess(Pager.EMPTY);
		List<MemOrder> list = memOrderMapper.getOrderListByMerchantId(getMemMerchant().getMerchantId(),
				(page - 1) * pageSize, pageSize);
		List<OrderForm> orderForms = new ArrayList<OrderForm>();
		for (MemOrder order : list)
			orderForms.add(new OrderForm(order));
		return Result.jsonSuccess(new Pager<OrderForm>(count, orderForms));
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
		redisOperate.hmset(memGoods.redisKey(), memGoods);
	}

	/**
	 * 删除商品
	 * 
	 * @param goodsId
	 * @return
	 */
	public String removeGoods(long goodsId) {
		CfgGoods goods = getGoodsById(goodsId);
		if (goods == null)
			throw IllegalConstException.errorException(JiLuParams.GOODS_ID);
		if (!goods.getSource().equals(String.valueOf(getMemMerchant().getMerchantId())))
			return Result.jsonError(JiLuCode.GOODS_DELETE_LIMIT.constId(),
					MessageFormat.format(JiLuCode.GOODS_DELETE_LIMIT.defaultValue(), goodsId));
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
					order.getSuperMerchantId(), order.getCreated(), list));
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
		MemOrder order = getOrderByOrderId(orderId);
		OrderForm orderInfo = new OrderForm(order);
		List<OrderGoodsForm> notFinishGoodsList = getNotFinishGoodsList(orderId);
		List<MemOrder> mList = memOrderMapper.getTransferMerchantOrderListByOrderId(orderId);
		List<TransferOrderModel> transferOrderModel = getTransferOrderListModelList(mList);
		List<OrderPacketForm> packetList = getPacketList(orderId);
		// 获取子订单信息
		List<MemOrder> childOrders = memOrderMapper.getChildOrderByOrderId(orderId);
		List<OrderForm> childOrderList = new ArrayList<OrderForm>();
		for (MemOrder memOrder : childOrders)
			childOrderList.add(new OrderForm(memOrder));

		return Result.jsonSuccess(
				new OrderDetailedModel(orderInfo, notFinishGoodsList, transferOrderModel, packetList, childOrderList));
	}

	/**
	 * 获取订单包裹
	 * @param orderId
	 * @return
	 */
	protected List<OrderPacketForm> getPacketList(String orderId) {
		List<MemOrderPacket> packets = memOrderPacketMapper.getMemOrderPacketByOrderId(orderId);
		List<OrderPacketForm> packetList = new ArrayList<OrderPacketForm>();
		for (MemOrderPacket memOrderPacket : packets) {
			List<MemOrderGoods> goodsList = memOrderGoodsMapper
					.getPacketMerchantOrderGoodsByPacketId(memOrderPacket.getPacketId());
			List<OrderGoodsForm> packetOrderGoodsList = new ArrayList<OrderGoodsForm>();
			for (MemOrderGoods memOrderGoods : goodsList)
				packetOrderGoodsList.add(new OrderGoodsForm(memOrderGoods));
			packetList.add(new OrderPacketForm(memOrderPacket, packetOrderGoodsList));
		}
		return packetList;
	}
	/**
	 * 获取订单未分配订单产品信息
	 * @param orderId
	 * @return
	 */
	public List<OrderGoodsForm> getNotFinishGoodsList(String orderId) {
		// 获取未完成产品
		List<MemOrderGoods> notFinishOrderGoods = memOrderGoodsMapper.getNotFinishMerchantOrderGoodsByOrderId(orderId);
		List<OrderGoodsForm> notFinishGoodsList = new ArrayList<OrderGoodsForm>();
		for (MemOrderGoods memOrderGoods : notFinishOrderGoods)
			notFinishGoodsList.add(new OrderGoodsForm(memOrderGoods));
		return notFinishGoodsList;
	}
	
	/**
	 * 获取正在转单中的订单信息
	 * @param mList
	 * @return
	 */
	public List<TransferOrderModel> getTransferOrderListModelList(List<MemOrder> mList) {
		List<TransferOrderModel> orderChangeModels = new ArrayList<>();
		for (MemOrder order : mList) {
			List<MemOrderGoods> list = memOrderGoodsMapper.getChangeMerchantOrderGoodsByOrderId(order.getOrderId());
			List<OrderGoodsForm> orderGoodsForms = new ArrayList<OrderGoodsForm>();
			for (MemOrderGoods memOrderGoods : list)
				orderGoodsForms.add(new OrderGoodsForm(memOrderGoods));
			orderChangeModels.add(new TransferOrderModel(order.getOrderId(), order.getMerchantName(),
					order.getMerchantId(), order.getCreated(), orderGoodsForms));
		}
		return orderChangeModels;
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
	public String getGoodsStoreList(int page, int pageSize,int type) {
		long count = memGoodsStoreMapper.getCountBymerchantId(getMemMerchant().getMerchantId());
		if (count == 0)
			return Result.jsonSuccess(Pager.EMPTY);
		String sort = "";//表示以type递增或递减
		String orderByColumn = "";
		switch (type) {
		case 0:
			sort = OrderByType.ASC.name();
			orderByColumn = "name_prefix_letter";
			break;
		case 1:
			sort = OrderByType.DESC.name();
			orderByColumn = "last_stock_time";
			break;
		case 2:
			sort = OrderByType.DESC.name();
			orderByColumn = "sell_count";
			break;
		default:
			throw IllegalConstException.errorException(JiLuParams.TYPE);
		}
		List<MemGoodsStore> list = memGoodsStoreMapper.getMemGoodsStoreList((page - 1) * pageSize, pageSize,
				getMemMerchant().getMerchantId(),orderByColumn,sort);
		return Result.jsonSuccess(new Pager<GoodsStoreForm>(count, GoodsStoreForm.getGoodsStoreFormList(list)));
	}

	/**
	 * 通过商品名字或者商户查找商品列表
	 * @param page
	 * @param pageSize
	 * @param name
	 * @param type
	 * @return
	 */
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
			break;
		case MERCHANT:
			count = cfgGoodsMapper.getCountByMerchantId(getMemMerchant().getMerchantId());
			if (count == 0)
				return Result.jsonSuccess(Pager.EMPTY);
			list = cfgGoodsMapper.getGoodsListByMerchantId((page - 1) * pageSize, pageSize,
					getMemMerchant().getMerchantId());
			break;
		default:
			throw IllegalConstException.errorException(JiLuParams.TYPE);
		}
		return Result.jsonSuccess(new Pager<CfgGoodsForm>(count, CfgGoodsForm.getCfgGoodsFormList(list)));
	}
	/**
	 * 通过商品条形码查找商品列表
	 * @param code
	 * @return
	 */
	public String getGoodsListByCode(String code) {
		String[] codes = code.split(";");
		List<CfgGoodsListForm> cfgGoodsListForms = new ArrayList<CfgGoodsListForm>();
		for(String goodsCode:codes){
			List<CfgGoods> list = cfgGoodsMapper.getGoodsListByCode(0, 1000, goodsCode);
			cfgGoodsListForms.add(new CfgGoodsListForm(goodsCode, CfgGoodsForm.getCfgGoodsFormList(list)));
		}
		return Result.jsonSuccess(cfgGoodsListForms);
	}
	
	/**
	 * 通过商品id查找商品列表
	 * @param code
	 * @return
	 */
	public String getGoodsListByGoodsId(String ids) {
		String[] goodsIds = ids.split(";");
		List<CfgGoodsListForm> cfgGoodsListForms = new ArrayList<CfgGoodsListForm>();
		for(String goodsId:goodsIds){
			CfgGoods cfgGoods = getGoodsById(Long.valueOf(goodsId));
			if(null == cfgGoods)
				return Result.jsonError(JiLuCode.GOODS_NOT_EXIST.constId(),MessageFormat.format(JiLuCode.GOODS_NOT_EXIST.defaultValue(), goodsId));
			List<CfgGoods> list = cfgGoodsMapper.getGoodsListByCode(0, 1000, cfgGoods.getGoodsCode());
			cfgGoodsListForms.add(new CfgGoodsListForm(cfgGoods.getGoodsCode(), CfgGoodsForm.getCfgGoodsFormList(list)));
		}
		return Result.jsonSuccess(cfgGoodsListForms);
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
	 * 
	 * @param goodsId
	 * @return
	 */
	public String getGoodsInfo(long goodsId) {
		CfgGoods goods = getGoodsById(goodsId);
		if (goods == null)
			throw IllegalConstException.errorException(JiLuParams.GOODS_ID);
		return Result.jsonSuccess(goods);
	}

	/**
	 * 获取商户信息
	 * 
	 * @return
	 */
	public String getMerchantInfo() {
		MerchantForm merchantForm = new MerchantForm(this);
		List<MemAccount> list = memAccountMapper.getByMerchantId(getMemMerchant().getMerchantId());
		for (MemAccount memAccount : list) {
			switch (memAccount.getType()) {
			case 0:
				merchantForm.setPhone(memAccount.getAccount());
			case 1:
				merchantForm.setEmail(memAccount.getAccount());
			case 2:
				merchantForm.setOpenId(memAccount.getAccount());
			default:
			}
		}
		return Result.jsonSuccess(merchantForm);
	}

	/**
	 * 添加产品仓储
	 * 
	 * @param goodsId
	 * @param count
	 * @return
	 */
	public String addGoodsStore(long goodsId, long count, float price, String memo) {
		CfgGoods goods = getGoodsById(goodsId);
		if (goods == null)
			throw IllegalConstException.errorException(JiLuParams.GOODS_ID);
		MemGoodsStore store = new MemGoodsStore(goods,memMerchant,count,(long)0, price, memo);
		memGoodsStoreMapper.insert(store);
		redisOperate.hmset(store.redisKey(), store);
		return Result.jsonSuccess(new GoodsStoreForm(store));
	}

	/**
	 * 同步商户对应的个推cid
	 * 
	 * @param cid
	 * @return
	 */
	public String sycMerchantToCID(String cid, int type) {
		long merchantId = getMemMerchant().getMerchantId();
		MemCid memCid = new MemCid(merchantId, cid, type);
		MemCid memCid_ = getMemCid(memMerchant.getMerchantId());
		if (memCid_ == null) {
			memCidMapper.insert(memCid);
		} else {
			memCidMapper.update(memCid);
		}
		redisOperate.hmset(MerchantKeyGenerator.merchantCIDDataKey(merchantId), memCid);
		return Result.jsonSuccess(memCid);
	}

	/**
	 * 获取商户对应个推clientId
	 * 
	 * @return
	 */
	public MemCid getMemCid(long merchantId) {
		MemCid memCid = redisOperate.hgetAll(MerchantKeyGenerator.merchantCIDDataKey(merchantId), new MemCid());
		if (memCid == null)
			memCid = memCidMapper.getMemCid(merchantId);
		return memCid;
	}

	/**
	 * 获取网易云信账号
	 * 
	 * @return
	 * @throws Exception
	 */
	public MemAccid getMemAccid() {
		MemAccid memAccid = redisOperate
				.hgetAll(MerchantKeyGenerator.merchantACCIDDataKey(getMemMerchant().getMerchantId()), new MemAccid());
		if (memAccid == null)
			memAccid = memAccidMapper.getMemAccid(getMemMerchant().getMerchantId());
		if (memAccid == null) {
			// 通过网易云信接口获取
			WyyxCreateAccountResultForm result = wyyxService
					.createWyyxIdAndToken(getMemMerchant().getMerchantId() + "_");
			if (result.getCode() != 200) {
				if (result.getDesc().equals("already register"))
					result = wyyxService.refreshWyyxIdAndToken(getMemMerchant().getMerchantId() + "_");
				else
					throw IllegalConstException.errorException(JiLuCode.WYYX_ACCOUNT_CREATE_FAIL);
			}
			memAccid = new MemAccid(getMemMerchant().getMerchantId(), result);
			memAccidMapper.insert(memAccid);
			redisOperate.hmset(MerchantKeyGenerator.merchantACCIDDataKey(getMemMerchant().getMerchantId()), memAccid);
		}
		return memAccid;
	}


	/**
	 * 搜索商品仓储
	 * @param type
	 * @param value
	 * @return
	 */
	public String searchGoodsStore(int type, String value,int page,int pageSize) {
		long count = 0;
		List<CfgGoods> list = new ArrayList<CfgGoods>();
		List<GoodsStoreSearchForm> forms = new ArrayList<GoodsStoreSearchForm>();
		switch (type) {
		case 0:
			count = cfgGoodsMapper.getCountByGoodsName(value);
			list = cfgGoodsMapper.getGoodsListByGoodsName((page - 1) * pageSize, pageSize, value);
			break;
		case 1:
			count = cfgGoodsMapper.getCountByCode(value);
			list = cfgGoodsMapper.getGoodsListByCode((page - 1) * pageSize, pageSize, value);
		}
		if (count == 0)
			return Result.jsonSuccess(Pager.EMPTY);
		for(CfgGoods cfgGoods : list){
			MemGoodsStore goodsStore = getMemGoodsStore(memMerchant.getMerchantId(),cfgGoods.getGoodsId());
			if(goodsStore == null){
				forms.add(new GoodsStoreSearchForm(cfgGoods));
			}else{
				forms.add(new GoodsStoreSearchForm(goodsStore));
			}
		}
		
		return Result.jsonSuccess(new Pager<GoodsStoreSearchForm>(count, forms));
	}
	
	/**
	 * 获取商品的仓储数据（包括最近进货记录,商户该产品的待出库数据）
	 * @param goodsId
	 * @return
	 */
	public String getGoodsStoreInfo(long goodsId) {
		MemGoodsStore goodsStore = getMemGoodsStore(memMerchant.getMerchantId(),goodsId);
		if(null== goodsStore)
			throw IllegalConstException.errorException(JiLuParams.GOODS_ID);
		List<StockGoodsStoreLog> logs = stockGoodsStoreLogMapper.getLogList(memMerchant.getMerchantId(),goodsId,0,5);
		//计算平均成本
		long count = 0;
		float averagePrice=0;
		if(logs!=null && logs.size()>0 && goodsStore.getCount()>0){
			for(StockGoodsStoreLog log : logs){
				if(goodsStore.getCount() < count)
					break;
				averagePrice+=log.getPrice()*log.getCount();
				count +=log.getCount();
			}
			averagePrice = averagePrice/count;
		}else{
			averagePrice = goodsStore.getPrice();
		}
		return Result.jsonSuccess(new GoodsStoreAndStockForm(goodsStore,logs,averagePrice));
	}
	
	/**
	 * 订单未处理产品以及库存查询
	 * @param orderId
	 * @return
	 */
	public String orderGoodsStoreInfo(String orderId) {
		MemOrder memOrder = getMerchantOrderById(memMerchant.getMerchantId(), orderId);
		if(memOrder == null)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		List<MemOrderGoods> list = memOrderGoodsMapper.getNotFinishMerchantOrderGoodsByOrderId(orderId);
		List<OrderGoodsStoreInfoForm> orderGoodsStoreInfoForms = new ArrayList<OrderGoodsStoreInfoForm>();
		for(MemOrderGoods memOrderGoods : list){
			long goodsId = memOrderGoods.getGoodsId();
			MemGoodsStore store = getMemGoodsStore(memMerchant.getMerchantId(),goodsId);
			if(null == store){
				orderGoodsStoreInfoForms.add(new OrderGoodsStoreInfoForm(goodsId, memOrderGoods.getGoodsName(), memOrderGoods.getCount(), 0, 0,0));
			}else{
				orderGoodsStoreInfoForms.add(new OrderGoodsStoreInfoForm(goodsId, memOrderGoods.getGoodsName(), memOrderGoods.getCount(), 1, store.getCount(), store.getPrice()));
			}
		}
		return Result.jsonSuccess(orderGoodsStoreInfoForms);
	}
	/**
	 * 订单备注信息修改 推送消息
	 * @param memo
	 * @return
	 */
	public String orderMemoEdit(String orderId,String memo) {
		MemOrder memOrder = getMerchantOrderById(memMerchant.getMerchantId(), orderId);
		if(null == memOrder)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		if(!memOrder.getRootOrderId().equals(orderId))
			return Result.jsonError(JiLuCode.ORDER_MEMO_CANNOT_EDIT);
		//推送消息给与该订单相关的商户
		List<MemOrder> list = memOrderMapper.getAllOrderByRootOrderId(memOrder.getRootOrderId());
		List<String> cids = new ArrayList<String>();
		for(MemOrder order : list){
			order.setMemo(memo);
			order.setUpdated(DateUtils.currentTime());
			if(order.getMerchantId() == memMerchant.getMerchantId() || order.getStatus() > 4)
				continue;
			MemCid memCid = getMemCid(order.getMerchantId());
			if(memCid !=null)
				cids.add(memCid.getClientId());
		}
		memOrderMapper.batchUpdate(list);
		redisOperate.batchHmset(list);
		//推送
		JiLuPushUtil.orderMemoEditPush(cids, memMerchant.getMerchantId(), memMerchant.getName(), memo);
		return Result.jsonSuccess();
	}
	/**
	 * 根据时间段查询所有订单待采购清单总和
	 * @param start
	 * @param end
	 * @return
	 */
	public String allOrderGoodsSum(int start, int end) {
		List<AllOrderGoodsSumForm> list = memOrderGoodsMapper.getNotFinishMerchantOrderGoodsByMerchantId(memMerchant.getMerchantId(),start,end);
		return Result.jsonSuccess(list);
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

	/**
	 * 获取客户
	 * @param customerId
	 * @return
	 */
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
	 * 获取订单产品
	 * @param id
	 * @return
	 */
	public MemOrderGoods getMerchantOrderGoodsById(long id) {
		String key = MerchantKeyGenerator.merchantOrderGoodsDataKey(id);
		MemOrderGoods merchantOrderGoods = redisOperate.hgetAll(key, new MemOrderGoods());
		if (merchantOrderGoods != null)
			return merchantOrderGoods;
		merchantOrderGoods = memOrderGoodsMapper.getMerchantOrderGoodsById(id);
		if (null != merchantOrderGoods)
			redisOperate.hmset(merchantOrderGoods.redisKey(), merchantOrderGoods);
		return merchantOrderGoods;
	}

	/**
	 * 获取商品
	 * @param goodsId
	 * @return
	 */
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
	 * 获取商户库存
	 * @param merchantId
	 * @param goodsId
	 * @return
	 */
	public MemGoodsStore getMemGoodsStore(long merchantId,long goodsId) {
		MemGoodsStore store = redisOperate.hgetAll(MerchantKeyGenerator.merchantGoodsStoreDataKey(merchantId, goodsId),
				new MemGoodsStore());
		if(store == null){
			store = memGoodsStoreMapper.getMemGoodsStoreById(merchantId, goodsId);
			if(store != null)
				redisOperate.hmset(store.redisKey(), store);
		}
		return store;
	}
	
	/**
	 * 获取商户订单对应的产品待出库信息
	 * @param merchantId
	 * @param goodsId
	 * @param orderId
	 * @return
	 */
	public MemWaitStore getMemWaitStore(long merchantId,long goodsId,String orderId){
		MemWaitStore store = redisOperate.hgetAll(MerchantKeyGenerator.merchantWaitStoreDataKey(orderId, merchantId, goodsId),new MemWaitStore());
		if(store == null){
			store = memWaitStoreMapper.find(orderId, merchantId, goodsId);
			if(store != null)
				redisOperate.hmset(store.redisKey(), store);
		}
		return store;
	}


}
