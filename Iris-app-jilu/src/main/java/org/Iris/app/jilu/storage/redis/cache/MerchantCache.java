package org.Iris.app.jilu.storage.redis.cache;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.bean.enums.JiLuLuaCommand;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemMerchantMapper;
import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.redis.operate.lua.LuaOperate;
import org.Iris.util.common.SerializeUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MerchantCache extends RedisCache {
	
	@Resource
	private LuaOperate luaOperate;
	@Resource
	private MemAccountMapper memAccountMapper;
	@Resource
	private MemMerchantMapper memMerchantMapper;

	public Merchant getMerchantByAccount(AccountType type, String account) { 
		String key = MerchantKeyGenerator.accountMerchantMapKey(type);
		String val = redisOperate.hget(key, account);
		if (null == val) {
			MemAccount memAccount = memAccountMapper.getByAccount(account);
			if (null == memAccount)
				return null;
			val = String.valueOf(memAccount.getMerchantId());
			// 添加 account - merchantId 映射和  merchant - account 映射
			luaOperate.evalLua(JiLuLuaCommand.ACCOUNT_REFRESH.name(), 2, 
					key, MerchantKeyGenerator.accountDataKey(memAccount.getMerchantId()), 
					val, SerializeUtil.JsonUtil.GSON.toJson(memAccount));
		}
		return getMerchantById(Long.valueOf(val));
	}
	
	public Merchant getMerchantById(long merchantId) { 
		MemMerchant memMerchant = getHashBean(new MemMerchant(merchantId));
		if (null == memMerchant) {
			memMerchant = memMerchantMapper.getByMerchantId(merchantId);
			if (null == memMerchant)
				return null;
			flushHashBean(memMerchant);
		}
		return new Merchant(memMerchant);
	}
	
	@Transactional
	public void insertMerchant(MemMerchant memMerchant, AccountType type, String account) { 
		memMerchantMapper.insert(memMerchant);
		MemAccount memAccount = BeanCreator.newMemAccount(type, account, memMerchant.getCreated(), memMerchant.getMerchantId());
		memAccountMapper.insert(memAccount);

		// 更新缓存
		flushHashBean(memMerchant);
		luaOperate.evalLua(JiLuLuaCommand.ACCOUNT_REFRESH.name(), 2, 
				MerchantKeyGenerator.accountMerchantMapKey(type), 
				MerchantKeyGenerator.accountDataKey(memAccount.getMerchantId()), 
				String.valueOf(memMerchant.getMerchantId()), 
				SerializeUtil.JsonUtil.GSON.toJson(memAccount));
//		aliyunService.createMerchantFolder(merchant);     看客户端 sts 接上之后是否可以自己直接创建商户的文件夹
	}
	
	public Merchant getMerchantByToken(String token) { 
		String key = MerchantKeyGenerator.tokenMerchantMapKey();
		String merchantId = redisOperate.hget(key, token);
		if (null == merchantId)
			return null;
		return getMerchantById(Long.valueOf(merchantId));
	}
}
