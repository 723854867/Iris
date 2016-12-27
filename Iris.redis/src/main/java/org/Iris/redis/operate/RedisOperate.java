package org.Iris.redis.operate;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.Iris.redis.RedisHashBean;
import org.Iris.redis.model.EXPX;
import org.Iris.redis.model.NXXX;
import org.Iris.util.reflect.BeanUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;

public class RedisOperate {

	protected JedisPool pool;
	
	public String select(int db) {
		return invoke(new RedisInvocation<String>() {
			@Override
			public String invok(Jedis jedis) {
				return jedis.select(db);
			}
		});
	}

	public long del(String... keys) {
		return invoke(new RedisInvocation<Long>() {
			@Override
			public Long invok(Jedis jedis) {
				return jedis.del(keys);
			}
		});
	}

	public long expire(String key, int seconds) {
		return invoke(new RedisInvocation<Long>() {
			@Override
			public Long invok(Jedis jedis) {
				return jedis.expire(key, seconds);
			}
		});
	}
	
	public long exist(String... keys) { 
		return invoke(new RedisInvocation<Long>() {
			@Override
			public Long invok(Jedis jedis) {
				return jedis.exists(keys);
			}
		});
	}
	
	public long expireAt(String key, long timestamp) {
		return invoke(new RedisInvocation<Long>() {
			@Override
			public Long invok(Jedis jedis) {
				return jedis.expireAt(key, timestamp);
			}
		});
	}

	public String get(String key) {
		return invoke(new RedisInvocation<String>() {
			@Override
			public String invok(Jedis jedis) {
				return jedis.get(key);
			}
		});
	}
	
	public String getSet(String key, String value) {
		return invoke(new RedisInvocation<String>() {
			@Override
			public String invok(Jedis jedis) {
				return jedis.getSet(key, value);
			}
		});
	}

	public String set(String key, String value) {
		return invoke(new RedisInvocation<String>() {
			@Override
			public String invok(Jedis jedis) {
				return jedis.set(key, value);
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
		return invoke(new RedisInvocation<String>() {
			@Override
			public String invok(Jedis jedis) {
				return jedis.set(key, value, nxxx.name(), expx.name(), time);
			}
		});
	}
	
	public String hget(String key, String field) {
		return invoke(new RedisInvocation<String>() {
			@Override
			public String invok(Jedis jedis) {
				return jedis.hget(key, field);
			}
		});
	}
	
	public long hdel(String key, String... fields) {
		return invoke(new RedisInvocation<Long>() {
			@Override
			public Long invok(Jedis jedis) {
				return jedis.hdel(key, fields);
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
	
	public long hset(String key, String field, String value) {
		return invoke(new RedisInvocation<Long>(){
			@Override
			public Long invok(Jedis jedis) {
				return jedis.hset(key, field, value);
			}
		});
	}

	public String hmset(String key, Map<String, String> map) {
		return invoke(new RedisInvocation<String>() {
			@Override
			public String invok(Jedis jedis) {
				return jedis.hmset(key, map);
			}
		});
	}

	public String hmset(String key, Object bean) {
		return invoke(new RedisInvocation<String>() {
			@Override
			public String invok(Jedis jedis) {
				return jedis.hmset(key, BeanUtils.beanToMap(bean));
			}
		});
	}
	
	public void hmset(List<? extends RedisHashBean> beans) {
		 invoke(new RedisInvocation<Void>() {
			@Override
			public Void invok(Jedis jedis) {
				Pipeline pipeline = jedis.pipelined();
				for(RedisHashBean object : beans)
					pipeline.hmset(object.redisKey(), BeanUtils.beanToMap(object));
				 pipeline.sync();
				 return null;
			}
		});
	}

	
	public Long sadd(String key,String... members){
		return invoke(new RedisInvocation<Long>() {
			@Override
			public Long invok(Jedis jedis) {
				return jedis.sadd(key, members);
			}
		});
	}
	
	public Set<String> sdiff(String key){
		return invoke(new RedisInvocation<Set<String>>() {
			@Override
			public Set<String> invok(Jedis jedis) {
				return jedis.sdiff(key);
			}
		});
	}
	
	public Long srem(String key,String member){
		return invoke(new RedisInvocation<Long>() {
			@Override
			public Long invok(Jedis jedis) {
				// TODO Auto-generated method stub
				return jedis.srem(key, member);
			}
		});
	}
	/**
	 * 获取整个有序集元素数量
	 * 
	 * @param key
	 * @return
	 */
	public long zcount(String key) {
		return invoke(new RedisInvocation<Long>() {
			@Override
			public Long invok(Jedis jedis) {
				return jedis.zcount(key, "-inf", "+inf");
			}
		});
	}
	
	public long zcount(String key, double min, double max) {
		return invoke(new RedisInvocation<Long>() {
			@Override
			public Long invok(Jedis jedis) {
				return jedis.zcount(key, min, max);
			}
		});
	}
	
	public long zadd(String key, Map<String, Double> scoreMembers) {  
		return invoke(new RedisInvocation<Long>() {
			@Override
			public Long invok(Jedis jedis) {
				return jedis.zadd(key, scoreMembers);
			}
		});
	}
	
	public long zadd(String key, double score, String member) {  
		return invoke(new RedisInvocation<Long>() {
			@Override
			public Long invok(Jedis jedis) {
				return jedis.zadd(key, score, member);
			}
		});
	}
	
	public Set<Tuple> zrangeWithScores(String key, long start, long end) {
		return invoke(new RedisInvocation<Set<Tuple>>() {
			@Override
			public Set<Tuple> invok(Jedis jedis) {
				return jedis.zrangeWithScores(key, start, end);
			}
		});
	}
	
	public long zunionstore(String destination, ZParams params, String... keys) { 
		return invoke(new RedisInvocation<Long>() {
			@Override
			public Long invok(Jedis jedis) {
				return jedis.zunionstore(destination, params, keys);
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
