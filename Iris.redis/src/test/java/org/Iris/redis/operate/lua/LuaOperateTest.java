package org.Iris.redis.operate.lua;

import java.util.HashMap;
import java.util.Map;

import org.Iris.redis.BaseTest;
import org.Iris.redis.bean.User;
import org.Iris.util.common.SerializeUtil;
import org.Iris.util.reflect.BeanUtils;

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
		long list = luaOperate.evalLua(LuaCommand.TEST, 0);
		System.out.println(list);
	}
}
