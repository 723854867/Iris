package org.Iris.redis;

public interface RedisHashLuaSerializableBean extends RedisHashBean {
	
	String[] serializeToLuaParams();
}
