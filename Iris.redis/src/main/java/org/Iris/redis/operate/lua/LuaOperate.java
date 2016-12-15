package org.Iris.redis.operate.lua;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.Iris.redis.RedisConsts;
import org.Iris.redis.RedisHashLuaSerializableBean;
import org.Iris.redis.operate.RedisOperate;
import org.Iris.redis.operate.RedisOperate.RedisInvocation;
import org.Iris.util.reflect.BeanUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisNoScriptException;

public class LuaOperate {
	
	private LuaCache luaCache;
	private RedisOperate redisOperate;
	
	public <T> T evalLua(LuaCommand command, int keyNum, String ...params) {
		return evalLua(command.name(), keyNum, params);
	}
	
	/**
	 * 如果 key 值存在并且值等于 value 则删除 value 然后返回 true，否则什么也不做返回 false
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean delIfEquals(String key, String value) {
		long flag = evalLua(LuaCommand.DEL_IF_EQUALS, 1, key, value);
		return flag == 1;
	}
	/**
	 * 如果key 存在 则删除 然后返回true 否则什么也不做返回false
	 * @param key
	 * @return
	 */
	public boolean delIfExist(String key){
		long flag = evalLua(LuaCommand.DEL_IF_EXIST, 1, key);
		return flag == 1;
	}
	
	public boolean hmsetIfExist(RedisHashLuaSerializableBean bean) {
		String result = evalLua(LuaCommand.HMSET_IF_EXIST.name(), 1, bean.serializeToLuaParams());
		return null != result && result.equals(RedisConsts.OK);
	}
	
	public boolean hsetIfExist(String key, String field, String value) {
		Long result = evalLua(LuaCommand.HSET_IF_EXIST.name(), 1, key, field, value);
		return null != result;
	}
	
	/**
	 * 记录验证码
	 * 
	 * @param codeKey 验证码 key
	 * @param countKey 验证码获取次数 key
	 * @param code 验证码
	 * @param codeLifeTime 验证码有效时长
	 * @param countMaxinum 验证码获取最大次数
	 * @param countLiftTime 验证码次数生命周期(超过该时间没有获取验证码，则验证码次数 key 会被删除，也就是说验证码次数会被清零)
	 * @return 0 - 表示成功；-1 - 表示获取验证码获取太频繁，-2 - 表示验证码获取次数上限
	 */
	public long recordCaptcha(String codeKey, String countKey, String code, long codeLifeTime, long countMaxinum, long countLiftTime) {
		return evalLua(LuaCommand.RECORD_CAPTCHA, 2, codeKey, countKey, code, String.valueOf(codeLifeTime), String.valueOf(countMaxinum), String.valueOf(countLiftTime));
	}
	
	/**
	 * 如果 hash key 存在则获取该 hash key 对应的值并且删除该 hash key
	 * 
	 * @param key
	 * @param bean
	 * @return
	 */
	public <T> T hdelAndGet(String key, T bean) { 
		List<String> list = evalLua(LuaCommand.HDEL_AND_GET, 1, key);
		if (list.isEmpty())
			return null;
	    Map<String, String> hash = new HashMap<String, String>(list.size()/2, 1);
	    Iterator<String> iterator = list.iterator();
	    while (iterator.hasNext())
	    	hash.put(iterator.next(), iterator.next());
	    return BeanUtils.mapToBean(hash, bean);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T evalLua(String cacheName, int keyNum, String ...params) {
		LuaScript script = this.luaCache.get(cacheName);
		if (null == script)
			throw new JedisNoScriptException("Script " + cacheName + " not exist!");
		
		return redisOperate.invoke(new RedisInvocation<T>() {
			@Override
			public T invok(Jedis jedis) {
				if (script.isStored()) 
					try {
						return (T)jedis.evalsha(script.getSha1Key(), keyNum, params);
					} catch (JedisNoScriptException e) {}
				
				T object = (T) jedis.eval(script.getContent(), keyNum, params);
				script.setStored(true);
				return object;
			}
		});
	}
	
	public void setLuaCache(LuaCache luaCache) {
		this.luaCache = luaCache;
	}
	
	public void setRedisOperate(RedisOperate redisOperate) {
		this.redisOperate = redisOperate;
	}
}
