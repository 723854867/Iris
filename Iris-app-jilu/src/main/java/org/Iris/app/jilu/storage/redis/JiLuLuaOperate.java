package org.Iris.app.jilu.storage.redis;

import org.Iris.app.jilu.common.bean.enums.JiLuLuaCommand;
import org.Iris.redis.operate.lua.LuaOperate;

public class JiLuLuaOperate extends LuaOperate {
 
	/**
	 * 替换 token，适用在 login(create也会触发 login)
	 * 
	 * @param merchantLockKey
	 * @param merchantDataKey
	 * @param tokenMerchantMapKey
	 * @param lockId
	 * @param token
	 * @param time
	 * @param merchantId
	 * @return
	 */
	public long tokenReplace(String merchantLockKey, String merchantDataKey, String tokenMerchantMapKey, 
			String lockId, String token, String time, String merchantId, String lockTimeout) { 
		return evalLua(JiLuLuaCommand.TOKEN_REPLACE.name(), 3, merchantLockKey, merchantDataKey, tokenMerchantMapKey, lockId, token, time, merchantId, lockTimeout);
	}
}
