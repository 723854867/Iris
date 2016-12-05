package org.Iris.redis;

import org.Iris.redis.operate.RedisOperate;

import junit.framework.TestCase;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class BaseTest extends TestCase {

	protected RedisOperate redisOperate;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "121.42.155.96", 6000, 3000, "zxl870613", 8);
		redisOperate = new RedisOperate();
		redisOperate.setPool(pool);
	}
}
