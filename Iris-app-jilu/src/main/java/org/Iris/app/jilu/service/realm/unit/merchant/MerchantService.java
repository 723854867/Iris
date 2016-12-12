package org.Iris.app.jilu.service.realm.unit.merchant;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.redis.cache.UnitCache;
import org.springframework.stereotype.Service;

@Service
public class MerchantService {
	
	@Resource
	private UnitCache unitCache;
	
	public Merchant getMerhantByAccount(String account) { 
		MemMerchant merchant = unitCache.getMerchantByAccount(account);
		return null == merchant ? null : new Merchant(merchant);
	}
	
	/**
	 * 创建商户，其实就是晚上商户信息
	 * 
	 * @param type
	 * @param account
	 * @param avatar
	 * @param address
	 */
	public void create(AccountType type, String account, String avatar, String address) { 
		
	}

	public void setUnitCache(UnitCache unitCache) {
		this.unitCache = unitCache;
	}
}
