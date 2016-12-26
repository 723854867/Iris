package org.Iris.app.jilu.service.realm.merchant;

import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.bean.enums.JiLuLuaCommand;
import org.Iris.app.jilu.common.bean.form.AssumeRoleForm;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.util.common.IrisSecurity;
import org.Iris.util.lang.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;

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
		String lockId = tryLock();
		if (null == lockId) 
			return false;
		
		try {
			int time = DateUtils.currentTime();
			String token = IrisSecurity.encodeToken(account);
			memMerchant.setToken(token);
			// 如果是正常的登录，则还需要将数据同步到 DB 中,(创建用户的在登陆之前已经把数据插入 DB 因此不需要再次同步)
			if (!create) {
				memMerchant.setUpdated(time);
				memMerchant.setLastLoginTime(time);
				memMerchantMapper.update(memMerchant);
			}
			
			// 把新的 token 写入 redis 并且删除老 token, 同时更新 lastLoginTime 和 updated 的时间
			luaOperate.evalLua(JiLuLuaCommand.LOGIN.name(), 2, 
					MerchantKeyGenerator.merchantDataKey(memMerchant.getMerchantId()),
					MerchantKeyGenerator.tokenMerchantMapKey(),
					token, String.valueOf(memMerchant.getMerchantId()), String.valueOf(time));
			return true;
		} finally {
			unLock(lockId);
		}
	}
	
	/**
	 * 阿里云鉴权
	 * 
	 */
	public AssumeRoleForm assumeRole() { 
		String key = MerchantKeyGenerator.aliyunStsTokenDataKey(memMerchant.getMerchantId());
		AssumeRoleForm form = redisOperate.hgetAll(key, new AssumeRoleForm());
		if (null == form) {
			AssumeRoleResponse response = aliyunService.getStsToken(merchant.getUnit().getMerchantId());
			form = new AssumeRoleForm(response);
			redisOperate.hmset(key, form);
			long expire = DateUtils.getTimeGap(form.getExpiration(), DateUtils.getUTCDate(), DateUtils.ISO8601_UTC, DateUtils.TIMEZONE_UTC);
			// 提前 5 分钟失效
			expire -= 60 * 5;
			redisOperate.expire(key, (int) (expire / 1000));
		}
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
