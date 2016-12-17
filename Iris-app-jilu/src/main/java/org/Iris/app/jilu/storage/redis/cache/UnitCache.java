package org.Iris.app.jilu.storage.redis.cache;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.service.realm.aliyun.AliyunService;
import org.Iris.app.jilu.service.realm.unit.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemCustomerMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemMerchantMapper;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.util.lang.DateUtils;
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
}
