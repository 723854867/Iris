package org.Iris.redis.operate.lua;

import org.Iris.redis.BaseTest;

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
		String val = luaOperate.evalLua(LuaCommand.TEST, 0, "string:cache:merchant:{0}:lock");
		System.out.println(val);
	}
}
