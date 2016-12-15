package org.Iris.app.jilu.service.realm;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.service.realm.unit.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemMerchantMapper;
import org.Iris.app.jilu.storage.redis.cache.UnitCache;
import org.Iris.util.lang.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component(value = "tx")
public class Tx {
	
	@Resource
	private MemAccountMapper memAccountMapper;
	@Resource
	private MemMerchantMapper memMerchantMapper;
	
	@Resource
	private UnitCache unitCache;

	/**
	 * 创建商户
	 * 
	 * @param merchant
	 * @param am
	 */
	@Transactional
	public Merchant createMerchant(MemMerchant merchant, AccountModel am) { 
		// 先插db
		memMerchantMapper.insert(merchant);
		MemAccount account = BeanCreator.newMemAccount(am, merchant.getCreated(), merchant.getMerchantId());
		memAccountMapper.insert(account);
		
		// 再更新缓存
		unitCache.flushHashBean(merchant);
		unitCache.flushHashBean(account);
		return new Merchant(merchant);
	}
	
	/**
	 * 更新商户
	 * @param merchant
	 * @return
	 */
	@Transactional
	public void updateMerchant(MemMerchant merchant) {
		merchant.setUpdated(DateUtils.currentTime());
		memMerchantMapper.update(merchant);
		unitCache.flushHashBean(merchant);
	}
}
