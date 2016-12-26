package org.Iris.app.jilu.service.realm.merchant;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.redis.cache.MerchantCache;

@Resource
public class MerchantService {
	
	@Resource
	private MerchantCache merchantCache;

	/**
	 * 通过账号获取商户
	 * 
	 * @param type
	 * @param account
	 * @return
	 */
	public Merchant getMerchantByAccount(AccountType type, String account) { 
		return merchantCache.getMerchantByAccount(type, account);
	}
	
	/**
	 * 通过 
	 * 
	 * @param token
	 * @return
	 */
	public Merchant createMerchant(AccountModel am, String name, String address) {
		MemMerchant memMerchant = BeanCreator.newMemMerchant(name, address);
		merchantCache.insertMerchant(memMerchant, AccountType.match(am.getType()), am.getAccount());
		return new Merchant(memMerchant);
	}
	
	/**
	 * 通过 token 获取商户
	 * 
	 * @param token
	 * @return
	 */
	public Merchant getMerchantByToken(String token) { 
		return merchantCache.getMerchantByToken(token);
	}
}
