package org.Iris.redis.operate.lua;

public enum LuaCommand {
	
	TEST_PARAM,

	DEL_IF_EQUALS,
	DEL_IF_EXIST,
	HMSET_IF_EXIST,
	HSET_IF_EXIST,
	RECORD_CAPTCHA,
	DEL_AND_GET_HASH;
}
