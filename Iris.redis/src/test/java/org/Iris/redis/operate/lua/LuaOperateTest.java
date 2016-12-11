package org.Iris.redis.operate.lua;

import org.Iris.redis.BaseTest;
import org.Iris.redis.bean.User;

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
		long value = luaOperate.recordCaptcha("code", "codeCount", "1235", 20000, 3, 30000);
		System.out.println(value);
	}
	
	public void testHdelAndGet() {
 		User user = luaOperate.hdelAndGet("user", new User());
		System.err.println(user.getAge() + " " + user.getName());
	}
}
