package org.Iris.app.jilu.service.realm.merchant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemMerchant;
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
		MemCustomer customer = _getCustomer(customerId);
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
		for (MemCustomer customer : list)
			form.add(type == CustomerListType.PURCHASE_FREQUENCY ? new CustomerFrequencyPagerForm(customer) : new CustomerPagerForm(customer));
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
	
	private MemCustomer _getCustomer(long customerId) { 
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
