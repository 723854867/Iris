package org.Iris.app.jilu.storage.redis.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.bean.enums.CustomerListType;
import org.Iris.app.jilu.common.bean.form.CustomerFrequencyPagerForm;
import org.Iris.app.jilu.common.bean.form.CustomerPagerForm;
import org.Iris.app.jilu.common.bean.form.Pager;
import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.common.bean.model.CustomerListModel;
import org.Iris.app.jilu.service.realm.aliyun.AliyunService;
import org.Iris.app.jilu.service.realm.unit.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemCustomerMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemMerchantMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderMapper;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.util.lang.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;

/**
 * 负责所有商户户相关的数据存取：比如 mem_merchant 等
 * 
 * @author Ahab
 */
@Service
public class UnitCache extends RedisCache {
	
	@Resource
	private AliyunService aliyunService;
	@Resource
	private MemOrderMapper memOrderMapper;
	@Resource
	private MemAccountMapper memAccountMapper;
	@Resource
	private MemMerchantMapper memMerchantMapper;
	@Resource
	private MemCustomerMapper memCustomerMapper;
	@Resource
	private MemGoodsMapper memGoodsMapper;
	
	/**
	 * 新建商户时需要插入商户数据，商户账号数据，并且在阿里云新建 oss 存储文件夹，之后该用户
	 * 拥有该 app 的资源文件夹的所有读权限，但是只能对自己所在文件夹进行写操作。
	 * 
	 * @param merchant
	 * @param am
	 * @return
	 */
	@Transactional
	public Merchant insertMerchant(MemMerchant merchant, AccountModel am) {
		memMerchantMapper.insert(merchant);
		MemAccount account = BeanCreator.newMemAccount(am, merchant.getCreated(), merchant.getMerchantId());
		memAccountMapper.insert(account);

		// 更新缓存
		flushHashBean(merchant);
		flushHashBean(account);
		
		// 创建用户 oss 资源文件夹
		aliyunService.createMerchantFolder(merchant);
		return new Merchant(merchant);
	}
	
	/**
	 * 更新商户信息
	 * 
	 * @param merchant
	 */
	public void updateMerchant(MemMerchant merchant) {
		merchant.setUpdated(DateUtils.currentTime());
		memMerchantMapper.update(merchant);
		flushHashBean(merchant);
	}

	/**
	 * 通过商户ID获取商户
	 * 
	 * @param id
	 * @return
	 */
	public Merchant getMerchantByMerchantId(long merchantId) {
		MemMerchant merchant = getHashBean(new MemMerchant(merchantId));
		if (null != merchant)
			return new Merchant(merchant);
		merchant = memMerchantMapper.getByMerchantId(merchantId);
		if (null == merchant)
			return null;
		flushHashBean(merchant);
		return new Merchant(merchant);
	}

	/**
	 * 通过商户账号获取商户
	 * 
	 * @param type
	 * @param account
	 * @return
	 */
	public Merchant getMerchantByAccount(String account) {
		MemAccount memAccount = getHashBean(new MemAccount(account));
		if (null == memAccount) {
			memAccount = memAccountMapper.getByAccount(account);
			if (null == memAccount)
				return null;
			flushHashBean(memAccount);
		}

		return getMerchantByMerchantId(memAccount.getMerchantId());
	}

	/**
	 * 根据 Token 获取商户数据
	 * 
	 * @param token
	 * @return
	 */
	public Merchant getMerchantByToken(String token) {
		String val = redisOperate.get(RedisKeyGenerator.getMerchantTokenKey(token));
		if (null == val)
			return null;
		return getMerchantByMerchantId(Long.valueOf(val));
	}
	
	/**
	 * 插入客户
	 * 
	 * @param customer
	 */
	public void insertCustomer(MemCustomer customer) {
		memCustomerMapper.insert(customer);
		flushHashBean(customer);
		String member = String.valueOf(customer.getCustomerId());
		redisOperate.zadd(CustomerListType.NAME.redisCustomerListKey(customer.getMerchantId()), customer.getScore(CustomerListType.NAME), member);
		redisOperate.zadd(CustomerListType.PURCHASE_SUM.redisCustomerListKey(customer.getMerchantId()), customer.getScore(CustomerListType.PURCHASE_SUM), member);
		redisOperate.zadd(CustomerListType.PURCHASE_RECENT.redisCustomerListKey(customer.getMerchantId()), customer.getScore(CustomerListType.PURCHASE_RECENT), member);
	}
	
	public MemCustomer getMemCustomerById(long customerId){
		MemCustomer customer = getHashBean(new MemCustomer(customerId));
		if(customer!=null)
			return customer;
		customer = memCustomerMapper.getMemCustomerById(customerId);
		if(customer!=null)
			return customer;
		return null;
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
		List<MemCustomer> list = memCustomerMapper.getCustomersByIds(set);
		List<CustomerPagerForm> form = new ArrayList<CustomerPagerForm>();
		for (MemCustomer customer : list)
			form.add(type == CustomerListType.PURCHASE_FREQUENCY ? new CustomerFrequencyPagerForm(customer) : new CustomerPagerForm(customer));
		return new Pager<CustomerPagerForm>(total, form);
	}
	
	/**
	 * 加载客户列表
	 */
	private long _customerCount(long merchantId, CustomerListType type) {
		String key = type.customerListLoadTimeKey(merchantId);
		String today = String.valueOf(DateUtils.zeroTime());
		String time = redisOperate.getSet(key, today);
		List<? extends CustomerListModel> list = null;								// 先获取该商户的所有客户
		switch (type) {
		case PURCHASE_FREQUENCY:													// 最近三十天的购物频率排序
			long count = 0;
			if (null == time || !time.equals(today)) {								// 该缓存需要每天重新计算刷新，因此如果刷新时间不是当天或者不存在则要重新计算
				// 重新计算时先看是否已经缓存了其他排序列表，如果已经刷新了其他排序列表则直接用其他排序列表的id，只不过 score 全部变成了0
				if (1 == redisOperate.exist(CustomerListType.NAME.redisCustomerListKey(merchantId))) {
					ZParams params = new ZParams();
					params.weightsByDouble(0);
					count = redisOperate.zunionstore(type.redisCustomerListKey(merchantId), params, CustomerListType.NAME.redisCustomerListKey(merchantId));
				// 如果其他列表还没刷新，则从数据库读取数据刷新
				} else {
					list = memCustomerMapper.getMerchantCustomers(merchantId);
					if (list.isEmpty())
						return 0;
					Map<String, Double> map = new HashMap<String, Double>(list.size());
					_loadCustomerList(merchantId, list, map, CustomerListType.PURCHASE_FREQUENCY);
					count = list.size();
				}
				
				// 同步 score
				int end = DateUtils.currentTime();
				int start = end - DateUtils.MONTH_SECONDS;
				list = memOrderMapper.getMerchantOrderCountGroupByCustomerBetweenTime(merchantId, start, end);
				if (list.isEmpty()) 
					return count;
				redisOperate.expireAt(CustomerListType.PURCHASE_FREQUENCY.redisCustomerListKey(merchantId), DateUtils.nextZeroTime() - 1);
			}
			
			
			int end = DateUtils.currentTime();
			int start = end - DateUtils.MONTH_SECONDS;
			list = memOrderMapper.getMerchantOrderCountGroupByCustomerBetweenTime(merchantId, start, end);
			redisOperate.expireAt(CustomerListType.PURCHASE_FREQUENCY.redisCustomerListKey(merchantId), DateUtils.nextZeroTime() - 1);
			return list.size();
		default:	
			if (null != time)				// 已经更新过排序列表了，直接返回总的客户数
				return redisOperate.zcount(type.redisCustomerListKey(merchantId));
			
			// 没有更新过，则需要从数据库中更新
			list = memCustomerMapper.getMerchantCustomers(merchantId);
			if (list.isEmpty())
				return 0;
			Map<String, Double> map = new HashMap<String, Double>(list.size());
			_loadCustomerList(merchantId, list, map, CustomerListType.NAME);
			_loadCustomerList(merchantId, list, map, CustomerListType.PURCHASE_SUM);
			_loadCustomerList(merchantId, list, map, CustomerListType.PURCHASE_RECENT);
			return list.size();
		}
	}
	
	private <T extends CustomerListModel> void _loadCustomerList(long merchantId, List<T> list, Map<String, Double> map, CustomerListType type) { 
		for (T model : list)
			map.put(String.valueOf(model.getCustomerId()), model.getScore(type));
		redisOperate.zadd(type.redisCustomerListKey(merchantId), map);
	}
}
