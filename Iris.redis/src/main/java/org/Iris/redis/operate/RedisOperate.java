package org.Iris.redis.operate;

import java.util.Map;

import org.Iris.redis.model.EXPX;
import org.Iris.redis.model.NXXX;
import org.Iris.util.reflect.BeanUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisOperate {

	protected JedisPool pool;
	
	public String get(String key) {
		return invoke(new RedisInvocation<String>() {
			@Override
			public String invok(Jedis jedis) {
				return jedis.get(key);
			}
		});
	}
	
	/**
	 * 有条件的设置
	 * 
	 * @param key
	 * @param value
	 * @param nxxx
	 * @param expx
	 * @param time
	 * @return
	 */
	public String setnxpx(String key, String value, NXXX nxxx, EXPX expx, long time) {
		return invoke(new RedisInvocation<String>(){
			@Override
			public String invok(Jedis jedis) {
				return jedis.set(key, value, nxxx.name(), expx.name(), time);
			}
		});
	}
	
	public Map<String, String> hgetAll(String key) {
		return invoke(new RedisInvocation<Map<String, String>>() {
			@Override
			public Map<String, String> invok(Jedis jedis) {
				return jedis.hgetAll(key);
			}
		});
	}
	
	public <T> T hgetAll(String key, T bean) {
		Map<String, String> map = invoke(new RedisInvocation<Map<String, String>>() {
			@Override
			public Map<String, String> invok(Jedis jedis) {
				return jedis.hgetAll(key);
			}
		});
		if (map.isEmpty())
			return null;
		return BeanUtils.mapToBean(map, bean);
	}
	
	public String hmset(String key, Map<String, String> map) {
		return invoke(new RedisInvocation<String>() {
			@Override
			public String invok(Jedis jedis) {
				return jedis.hmset(key, map);
			}
		});
	}
	
	public <T> T invoke(RedisInvocation<T> invoke) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return invoke.invok(jedis);
		} finally {
			jedis.close();
		}
	}
	
	public void setPool(JedisPool pool) {
		this.pool = pool;
	}
	
	public interface RedisInvocation<T> {
		T invok(Jedis jedis);
	}
}
