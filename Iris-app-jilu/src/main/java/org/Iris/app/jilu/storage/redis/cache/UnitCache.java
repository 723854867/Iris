package org.Iris.app.jilu.storage.redis.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.bean.enums.CustomerListType;
import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.common.bean.model.CustomerListModel;
import org.Iris.app.jilu.service.realm.aliyun.AliyunService;
import org.Iris.app.jilu.service.realm.unit.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemCustomerMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemMerchantMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderMapper;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.util.lang.DateUtils;
import org.apache.ibatis.annotations.Case;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	}
	
	/**
	 * 加载商户客户列表，返回的是排序的 set
	 * 
	 */
	public List<MemCustomer> getCustomerList(CustomerListType type, long merchantId, int page, int pageSize) { 
		String key = type.redisCustomerListKey(merchantId);
		long count = redisOperate.zcount(key);
		switch (type) {
		case PURCHASE_SUM:
		case PURCHASE_FREQUENCY:
		case NAME:
			
			break;
		default:					// 最近三十天的购物频率排序
			if (0 == count) 		// 则需要重新加在
				count = _loadCustomerListOrderByPurchaseFrequency(merchantId);
			break;
		}
		return null;
	}
	
	private List<MemCustomer> getFixedCustomerList(CustomerListType type, int page, int pageSize) {
		return null;
	}
	
	/**
	 * 用户登录时清理缓存
	 * 
	 * @param merchantId
	 */
	public void clearCache(long merchantId) { 
		redisOperate.del(
				RedisKeyGenerator.getCustomerListNameKey(merchantId),
				RedisKeyGenerator.getCustomerListPurchaseSumKey(merchantId),
				RedisKeyGenerator.getCustomerListPurchaseRecentKey(merchantId),
				RedisKeyGenerator.getCustomerListPurchaseFrequencyKey(merchantId));
	}
	
	/**
	 * 加载客户列表：注意这里不会加载 PURCHASE_FREQUENCY 因为该排序需要每天更新
	 * 
	 */
	public void loadCustomerList(long merchantId) {
		List<MemCustomer> list = memCustomerMapper.getMerchantCustomers(merchantId);
		if (list.isEmpty())
			return;
		Map<String, Double> map = new HashMap<String, Double>(list.size());
		_loadCustomerList(merchantId, list, map, CustomerListType.NAME);
		_loadCustomerList(merchantId, list, map, CustomerListType.PURCHASE_SUM);
		_loadCustomerList(merchantId, list, map, CustomerListType.PURCHASE_RECENT);
	}
	
	private int _loadCustomerListOrderByPurchaseFrequency(long merchantId) { 
		int end = DateUtils.currentTime();
		int start = end - DateUtils.MONTH_SECONDS;
		List<CustomerListModel> list = memOrderMapper.getMerchantOrderCountGroupByCustomerBetweenTime(merchantId, start, end);
		if (list.isEmpty()) 
			return 0;
		Map<String, Double> map = new HashMap<String, Double>(list.size());
		_loadCustomerList(merchantId, list, map, CustomerListType.PURCHASE_FREQUENCY);
//		redisOperate.expire(CustomerListType.PURCHASE_FREQUENCY.redisCustomerListKey(merchantId), seconds);
		return list.size();
	}
	
	private <T extends CustomerListModel> void _loadCustomerList(long merchantId, List<T> list, Map<String, Double> map, CustomerListType type) { 
		for (T model : list)
			map.put(String.valueOf(model.getCustomerId()), model.getScore(type));
		redisOperate.zadd(type.redisCustomerListKey(merchantId), map);
	}
}
