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
}
