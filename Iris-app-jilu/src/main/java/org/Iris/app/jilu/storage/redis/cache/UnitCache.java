package org.Iris.app.jilu.storage.redis.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.bean.enums.CustomerListType;
import org.Iris.app.jilu.common.bean.enums.JiLuLuaCommand;
import org.Iris.app.jilu.common.bean.form.CustomerFrequencyPagerForm;
import org.Iris.app.jilu.common.bean.form.CustomerPagerForm;
import org.Iris.app.jilu.common.bean.form.Pager;
import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.common.bean.model.CustomerListPurchaseFrequencyModel;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.realm.aliyun.AliyunService;
import org.Iris.app.jilu.service.realm.unit.merchant.MerchantOperator;
import org.Iris.app.jilu.storage.domain.Merchant;
import org.Iris.app.jilu.storage.domain.MerchantAccount;
import org.Iris.app.jilu.storage.domain.MerchantCustomer;
import org.Iris.app.jilu.storage.mybatis.mapper.CfgGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MerchantAccountMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MerchantCustomerMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MerchantMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MerchantOrderMapper;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.redis.operate.lua.LuaOperate;
import org.Iris.util.lang.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Tuple;

/**
 * 负责所有商户户相关的数据存取：比如 mem_merchant 等
 * 
 * @author Ahab
 */
@Service
public class UnitCache extends RedisCache {
	
	@Resource
	private LuaOperate luaOperate;
	@Resource
	private AliyunService aliyunService;
	@Resource
	private MerchantMapper merchantMapper;
	@Resource
	private CfgGoodsMapper cfgGoodsMapper;
	@Resource
	private MerchantOrderMapper merchantOrderMapper;
	@Resource
	private MerchantAccountMapper merchantAccountMapper;
	@Resource
	private MerchantCustomerMapper merchantCustomerMapper;

	
	/**
	 * 新建商户时需要插入商户数据，商户账号数据，并且在阿里云新建 oss 存储文件夹，之后该用户
	 * 拥有该 app 的资源文件夹的所有读权限，但是只能对自己所在文件夹进行写操作。
	 * 
	 * @param merchant
	 * @param am
	 * @return
	 */
	@Transactional
	public MerchantOperator insertMerchant(Merchant merchant, AccountModel am) {
		merchantMapper.insert(merchant);
		MerchantAccount account = BeanCreator.newMemAccount(am, merchant.getCreated(), merchant.getMerchantId());
		merchantAccountMapper.insert(account);

		// 更新缓存
		flushHashBean(merchant);
		flushHashBean(account);
		
		// 创建用户 oss 资源文件夹
		aliyunService.createMerchantFolder(merchant);
		return new MerchantOperator(merchant);
	}
	
	/**
	 * 更新商户信息
	 * 
	 * @param merchant
	 */
	public void updateMerchant(Merchant merchant) {
		merchant.setUpdated(DateUtils.currentTime());
		merchantMapper.update(merchant);
		flushHashBean(merchant);
	}

	/**
	 * 通过商户ID获取商户
	 * 
	 * @param id
	 * @return
	 */
	public MerchantOperator getMerchantByMerchantId(long merchantId) {
		Merchant merchant = getHashBean(new Merchant(merchantId));
		if (null != merchant)
			return new MerchantOperator(merchant);
		merchant = merchantMapper.getByMerchantId(merchantId);
		if (null == merchant)
			return null;
		flushHashBean(merchant);
		return new MerchantOperator(merchant);
	}

	/**
	 * 通过商户账号获取商户
	 * 
	 * @param type
	 * @param account
	 * @return
	 */
	public MerchantOperator getMerchantByAccount(AccountType type, String account) {
		String key = CommonKeyGenerator.accountMerchantIdMapKey(type);
		String value = redisOperate.hget(key, account);
		if (null != value) {
			MerchantAccount merchantAccount = merchantAccountMapper.getByAccount(account);
			if (null == merchantAccount)
				return null;
			redisOperate.hset(key, account, String.valueOf(merchantAccount.getMerchantId()));
		}
		return getMerchantByMerchantId(Long.valueOf(value));
	}

	/**
	 * 根据 Token 获取商户数据
	 * 
	 * @param token
	 * @return
	 */
	public MerchantOperator getMerchantByToken(String token) {
		String val = redisOperate.get(CommonKeyGenerator.getMerchantTokenKey(token));
		if (null == val)
			return null;
		return getMerchantByMerchantId(Long.valueOf(val));
	}
	
	/**
	 * 插入客户
	 * 
	 * @param customer
	 */
	public void insertCustomer(MerchantCustomer customer) {
		merchantCustomerMapper.insert(customer);
		flushHashBean(customer);
		String member = String.valueOf(customer.getCustomerId());
		if (0 == redisOperate.exist(CommonKeyGenerator.getCustomerListLoadTimeKey(customer.getMerchantId())))
			return;
		
		// 客户列表已经加载到内存中需要同步, 初始购物金额，购物频率和最近购物时间都是 0，只有名字有 score
		redisOperate.zadd(CustomerListType.PURCHASE_SUM.redisCustomerListKey(customer.getMerchantId()), 0, member);
		redisOperate.zadd(CustomerListType.PURCHASE_RECENT.redisCustomerListKey(customer.getMerchantId()), 0, member);
		redisOperate.zadd(CustomerListType.PURCHASE_FREQUENCY.redisCustomerListKey(customer.getMerchantId()), 0, member);			
		redisOperate.zadd(CustomerListType.NAME.redisCustomerListKey(customer.getMerchantId()), Double.valueOf((int) customer.getNamePrefixLetter().charAt(0)), member);
	}
	
	public MerchantCustomer getMemCustomerById(long customerId){
		MerchantCustomer customer = getHashBean(new MerchantCustomer(customerId));
		if(customer != null)
			return customer;
		return merchantCustomerMapper.getMemCustomerById(customerId);
	}
	/**
	 * 加载商户客户列表，返回的是排序的 set
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Pager<CustomerPagerForm> getCustomerList(CustomerListType type, long merchantId, int page, int pageSize) { 
		String key = type.redisCustomerListKey(merchantId);
		long count = _customerCount(merchantId, type);
		if (0 == count)
			return Pager.EMPTY;
		
		long total = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
		if (total < page || page < 1)
			return Pager.EMPTY;
		int start = (page - 1) * pageSize;
		int end = start + pageSize - 1;
		Set<Tuple> set = redisOperate.zrangeWithScores(key, start, end);
		List<MerchantCustomer> list = merchantCustomerMapper.getCustomersByIds(set);
		List<CustomerPagerForm> form = new ArrayList<CustomerPagerForm>();
		for (MerchantCustomer customer : list)
			form.add(type == CustomerListType.PURCHASE_FREQUENCY ? new CustomerFrequencyPagerForm(customer) : new CustomerPagerForm(customer));
		return new Pager<CustomerPagerForm>(total, form);
	}
	
	/**
	 * 会导致加载客户列表
	 */
	private long _customerCount(long merchantId, CustomerListType type) {
		String key = CommonKeyGenerator.getCustomerListLoadTimeKey(merchantId);
		String zeroTime = String.valueOf(DateUtils.zeroTime());
		String time = luaOperate.evalLua(JiLuLuaCommand.CUSTOMER_LIST_REFRESH.name(), 1, key, String.valueOf(type.mark()), zeroTime);
		if (null == time) 
			return _loadCustomerList(merchantId);
		if (null != time && type == CustomerListType.PURCHASE_FREQUENCY && !zeroTime.equals(time))
			_refreshCustomerListFrequency(merchantId);
		return redisOperate.zcount(type.redisCustomerListKey(merchantId));
	}
	
	private int _loadCustomerList(long merchantId) {
		List<MerchantCustomer> list = merchantCustomerMapper.getMerchantCustomers(merchantId);
		if (list.isEmpty())
			return 0;
		Map<String, Double> map = new HashMap<String, Double>(list.size());
		_loadCustomerList(merchantId, list, map, CustomerListType.NAME);
		_loadCustomerList(merchantId, list, map, CustomerListType.PURCHASE_SUM);
		_loadCustomerList(merchantId, list, map, CustomerListType.PURCHASE_RECENT);
		
		// 购物频率还要去订单表实时获取(每天只会获取一次)
		_loadCustomerList(merchantId, list, map, CustomerListType.PURCHASE_FREQUENCY);
		_refreshCustomerListFrequency(merchantId);
		return list.size();
	}
	
	private void _loadCustomerList(long merchantId, List<MerchantCustomer> list, Map<String, Double> map, CustomerListType type) { 
		for (MerchantCustomer model : list)
			map.put(String.valueOf(model.getCustomerId()), model.getScore(type));
		redisOperate.zadd(type.redisCustomerListKey(merchantId), map);
	}
	
	private void _refreshCustomerListFrequency(long merchantId) { 
		int end = DateUtils.currentTime();
		int start = end - DateUtils.MONTH_SECONDS;
		List<CustomerListPurchaseFrequencyModel> list = merchantOrderMapper.getMerchantOrderCountGroupByCustomerBetweenTime(merchantId, start, end);
		if (list.isEmpty())
			return;
		Map<String, Double> map = new HashMap<String, Double>();
		for (CustomerListPurchaseFrequencyModel model : list)
			map.put(String.valueOf(model.getCustomerId()), Double.valueOf(model.getCount()));
		redisOperate.zadd(CustomerListType.PURCHASE_FREQUENCY.redisCustomerListKey(merchantId), map);
	}
}
