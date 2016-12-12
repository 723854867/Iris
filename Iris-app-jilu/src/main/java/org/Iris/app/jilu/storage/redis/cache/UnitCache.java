package org.Iris.app.jilu.storage.redis.cache;

import javax.annotation.Resource;

import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemMerchantMapper;
import org.springframework.stereotype.Service;

/**
 * 负责所有用户相关的数据存取：比如 mem_merchant,mem_customer等
 * 
 * @author Ahab
 */
@Service
public class UnitCache extends RedisCache {

	@Resource
	private MemAccountMapper memAccountMapper;
	@Resource
	private MemMerchantMapper memMerchantMapper;

	/**
	 * 通过商户ID获取商户
	 * 
	 * @param id
	 * @return
	 */
	public MemMerchant getMerchantByMerchantId(long merchantId) { 
		MemMerchant merchant = getHashBean(new MemMerchant(merchantId));
		if (null != merchant)
			return merchant;
		merchant = memMerchantMapper.getByMerchantId(merchantId);
		if (null == merchant)
			return null;
		flushHashBean(merchant);
		return merchant;
	}

	/**
	 * 通过商户账号获取商户
	 * 
	 * @param type
	 * @param account
	 * @return
	 */
	public MemMerchant getMerchantByAccount(String account) {
		MemAccount memAccount = getHashBean(new MemAccount(account));
		if (null == memAccount) {
			memAccount = memAccountMapper.getByAccount(account);
			if (null == memAccount)
				return null;
			flushHashBean(memAccount);
		}
	
		return getMerchantByMerchantId(memAccount.getMerchantId());
	}
}
