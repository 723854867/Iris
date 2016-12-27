package org.Iris.redis.operate.lua;

import org.Iris.redis.BaseTest;
import org.Iris.redis.bean.TokenProcessModel;
import org.Iris.util.common.IrisSecurity;
import org.Iris.util.common.SerializeUtil;
import org.Iris.util.common.uuid.AlternativeJdkIdGenerator;
import org.Iris.util.lang.DateUtils;

public class LuaOperateTest extends BaseTest {
	
	private LuaOperate luaOperate;
	private LuaCache luaCache;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		luaCache = new LuaCache();
		luaOperate = new LuaOperate();
		luaOperate.setLuaCache(luaCache);
		luaOperate.setRedisOperate(redisOperate);
	}

	public void testRecordMobileCode() {
//		long value = luaOperate.recordCaptcha("code", "codeCount", "1235", 20000, 3, 30000);
//		System.out.println(value);
		long val = luaOperate.evalLua(LuaCommand.TEST, 3, "string:cache:merchant:2:lock", "hash:db:merchant:2", "hash:cache:token:merchant",
				AlternativeJdkIdGenerator.INSTANCE.generateId().toString(), IrisSecurity.encodeToken("+8613105716369"), String.valueOf(DateUtils.currentTime()), "2", "300000");
		System.out.println(val);
	}
}
