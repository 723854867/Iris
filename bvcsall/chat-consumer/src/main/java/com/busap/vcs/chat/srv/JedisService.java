package com.busap.vcs.chat.srv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;

@Service("jedisService")
public class JedisService {

	private static final String DEFAULT_INCR_VALUE = "1";
	private static final int DEFAULT_EXPIRE_TIME = 3600 * 24;
	private static final int AUTHLOGINCODE_EXPIRE_TIME = 60 * 5;

	//private static final int LOGIN_EXPIRE_TIME = 360000 * 24;  //默认登陆过期1天
	private static final double maxTime = 9999999999999d;

	private ShardedJedisPool shardedJedisPool;

	//@Value("${loginExpireTime}")    
	private int loginExpireTime = 24*3600;  //默认登陆过期1天

	public boolean setIfNotExists(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			Long result = jedis.setnx(key, DEFAULT_INCR_VALUE);
			jedis.expire(key, DEFAULT_EXPIRE_TIME);
			if (result == 0) {
				return false;
			}
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return true;
	}

	public long incr(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.incr(key);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	public boolean keyExists(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.exists(key);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	public String get(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.get(key);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	public void set(String key, String value) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.set(key, value);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 保存注册认证码或登陆随机码
	 * @param key
	 * @param value
	 */
	public void setAuthLoginCode(String key, String value) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.set(key, value);
			jedis.expire(key, AUTHLOGINCODE_EXPIRE_TIME);
		} finally { 
			shardedJedisPool.returnResource(jedis);
		}
	}

	public void expire(String key,int expireAt){
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.expire(key, expireAt);
		} finally { 
			shardedJedisPool.returnResource(jedis);
		}
	}

	public long delete(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.del(key);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 从List中取出所有数据
	 */
	public List<String> getValuesFromList(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.lrange(key, 0, -1);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 从List中取出所有数据指定范围的item
	 */
	public List<String> getValuesFromList(String key,long start,long end) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.lrange(key,start,end);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}


	/**
	 * 将新增数据放到List的left头,并保留listSize个元素
	 * 
	 */
	public void setValueToListHead(int listSize,String key,String ... value) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.lpush(key,value);
			jedis.ltrim(key,0,listSize-1); //保留0到listSize的元素，删除其它元素
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public void setValueToList(String key,String ... value){
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.lpush(key,value);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 将新增数据放到List的left头,并返回指定lindex的Item,并保留listSize个元素
	 * 
	 */
	public String setValueToListHeadAndGetLindexItem(int listSize,String key,String value,int lindex) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.lpush(key,value);
			jedis.ltrim(key,0,listSize-1); //保留0到listSize的元素，删除其它元素
			return jedis.lindex(key,lindex);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 获取指定key的SortedSet集合大小
	 */
	public long getSortedSetSizeFromShard(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.zcard(key);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 获取指定key的大于start的SortedSet集合 in shard,倒序
	 */
	public Set<String> getSortedSetFromShardByDesc(String key,long start) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.zrevrangeByScore(key,maxTime,start);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 获取指定key的SortedSet集合 in shard,倒序
	 */
	public Set<String> getSortedSetFromShardByDesc(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.zrevrange(key, 0, -1);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 获取指定key的SortedSet集合 in shard,倒序
	 */
	public Set<String> getSortedSetFromShardByDesc(String key,long start,long end) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.zrevrange(key,start,end);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 获取指定key的SortedSet集合 in shard,正序
	 */
	public Set<String> getSortedSetFromShardByScore(String key,double start,double end) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.zrangeByScore(key,start,end);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 获取指定key的Set集合
	 */
	public Set<String> getSetFromShard(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.smembers(key);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 获取指定key的Set集合
	 */
	public Set<byte[]> getSetFromShard(byte[] key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.smembers(key);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 将新增数据放到指定key的SortedSet集合
	 * 
	 */
	public void setValueToSortedSetInShard(String key,double score,String value) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.zadd(key, score, value);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public void zrem(String key,String ... members){
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.zrem(key, members);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 将新增数据放到指定key的SortedSet集合,删除超过sortCount的值,并返回returnIndex指定的item
	 * 
	 */
	public Set<String> setValueToSortedSetAndDelReturnInShard(String key,double score,String value, int sortCount,int returnIndex) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.zadd(key, score, value);
			long sortedSetSize = getSortedSetSizeFromShard(key);
			Set<String> returnValue = null;
			if(sortedSetSize>sortCount){
				jedis.zremrangeByRank(key,0,sortedSetSize-sortCount-1); //删除sortCount以后的所有set值
				returnValue = jedis.zrevrange(key,returnIndex,returnIndex);
			}
			else if(sortedSetSize>returnIndex){ //返回第returnIndex个值
				returnValue = jedis.zrevrange(key,returnIndex,returnIndex);
			}
			return returnValue;
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 删除sortedSet指定key的指定元素
	 * 
	 */
	public long deleteSortedSetItemFromShard(String key,String ... value) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.zrem(key,value);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 将新增数据放到指定key的Set集合
	 * 
	 */
	public void setValueToSetInShard(String key,String ... value) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.sadd(key,value);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 将新增数据放到指定key的Set集合
	 * 
	 */
	public void setValueToSetInShard(byte[] key,byte[] value) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.sadd(key,value);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 删除set指定key的指定元素
	 * 
	 */
	public long deleteSetItemFromShard(String key,String ... value) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.srem(key,value);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 删除指定key的Set
	 * 
	 */
	public long deleteSetFromShard(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.del(key);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 判断value是否指定set的成员in shard
	 * 
	 */
	public boolean isSetMemberInShard(String key,String member) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.sismember(key, member);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 判断value是否指定sortedset的成员in shard
	 * 
	 */
	public Double isSortedSetMemberInShard(String key,String member) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.zscore(key, member);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 将值放入Map
	 * 
	 * @param key
	 * @param detailKey
	 * @param value
	 */
	public void setValueToMap(String key, String detailKey, String value) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.hset(key, detailKey, value);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 将map存入缓存
	 * @param key
	 * @param map
	 */
	public void setValueToMap(String key,Map<String,String> map) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.hmset(key, map);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 将map存入缓存
	 * @param key
	 * @param map
	 */
	public void setLoginValueToMap(String key,Map<String,String> map) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.hmset(key,map);
			jedis.expire(key,loginExpireTime);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 从map取值
	 * 
	 * @param key
	 * @param detailKey
	 * return value
	 */
	public String getValueFromMap(String key, String detailKey) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.hget(key, detailKey);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 从map取值自增
	 * 
	 * @param key
	 * @param detailKey
	 * return value
	 */
	public long getIncrValueFromMap(String key, String detailKey) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.hincrBy(key, detailKey,1);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 取得map所有值
	 * 
	 * @param key
	 * return map对象
	 */
	public Map<String,String> getMapByKey(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.hgetAll(key);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 删除map中指定detailKey
	 * @param key
	 * return long
	 */
	public long deleteValueFromMap(String key,String ...detailKey) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.hdel(key, detailKey);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 从map取值并进行加减计算
	 * @param key
	 * @param detailKey
	 * @param step 步长，加或减
	 * return value
	 */
	public long getIncrValueFromMap(String key, String detailKey,long step) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.hincrBy(key, detailKey,step);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	/**
	 * 排序的zset，元素分值累加
	 * @param key
	 * @param member
	 * @param score
	 * @return
	 */
	public Double zincrByScore(String key,String member,Double score){
		ShardedJedis jedis = shardedJedisPool.getResource();
		try{
			return jedis.zincrby(key, score, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return 0d;
	}

	/**
	 * 返回hash中元素的个数
	 * 
	 * @param key
	 *            key值
	 * @return 元素个数
	 */
	public long getLength(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.hlen(key);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 返回hash中的元素
	 * 
	 * @param key
	 *            key值
	 * @return 元素集
	 */
	public Map<String, String> getSnapShots(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.hgetAll(key);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * 返回hash中的具体字段
	 * 
	 * @param key
	 *            key值
	 * @param detailKey
	 *            hash的key值
	 * @return 元素集
	 */
	public String getSnapShots(String key, String detailKey) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.hget(key, detailKey);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * PipeLine批量get操作获取map数据
	 * @param keys
	 * @param fields
	 * @return
	 */
	public List<Response<List<String>>> pipeLineFromMap(String[] keys,String[] fields) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		ShardedJedisPipeline pipeLine = jedis.pipelined();

		List<Response<List<String>>> responseList = new ArrayList<Response<List<String>>>();
		try {
			for (String key : keys) {
				Response<List<String>> r= pipeLine.hmget(key, fields);
				responseList.add(r);
			}
			pipeLine.sync();
			return responseList;
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	/**
	 * PipeLine批量zadd操作
	 * @param keys
	 * @return
	 */
	/*
	public List<Response<Long>> pipeLineSortedSetZAdd(List<SortedSetItemObj> sortedSetObjList) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		ShardedJedisPipeline pipeLine = jedis.pipelined();
		List<Response<Long>> responseList = new ArrayList<Response<Long>>();
		try {
			for (SortedSetItemObj sortedSetObj : sortedSetObjList) {
				Response<Long> result = pipeLine.zadd(sortedSetObj.getKey(), sortedSetObj.getScoreMemberMap());
				responseList.add(result);
			}
			pipeLine.sync();
			return responseList;
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}*/

	public List<Response<Set<String>>> pipleGetFriendBlack(String key,List<String> uIdList){
		ShardedJedis jedis = shardedJedisPool.getResource();
		ShardedJedisPipeline pipeLine = jedis.pipelined();

		List<Response<Set<String>>> responseList = new ArrayList<Response<Set<String>>>();
		try {
			for (String uid : uIdList) {
				Response<Set<String>> response = pipeLine.smembers(key+uid);

				responseList.add(response);
			}
			pipeLine.sync();
			return responseList;
		} finally {
			shardedJedisPool.returnResource(jedis);
		}


	}

	/**
	 * 转换所有离线消息到新的key
	 * @param key
	 * @return
	 */
	/*
	public void convertOfflineValues(String key,int listSize,JsonBinder jsonBinder) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {	
			OfflineMessageRedisComparator offlineMessageRedisComparator = new OfflineMessageRedisComparator();

			//1.从旧的key查询离线消息
			Collection<Jedis> jedisColl = jedis.getAllShards();
			Map<String,Response<List<String>>> responseMap = new HashMap<String,Response<List<String>>>();
			for(Jedis jedisInfo:jedisColl){
				Set<String> set = jedisInfo.keys(key);
				Pipeline pipeLine = jedisInfo.pipelined();
				for (String str : set) {
					Response<List<String>> response = pipeLine.hvals(str);
					responseMap.put(str,response);
				}
				pipeLine.sync();
			}	

			//2.从将查询到离线消息存入新key
			ShardedJedisPipeline pipeLineNew = jedis.pipelined();
			for(String keyStr :responseMap.keySet()) {
				Response<List<String>> response = responseMap.get(keyStr);
				List<String> offlineList = null;
				try {
					offlineList = response.get();
				}
				catch(Exception ex){
					pipeLineNew.del(keyStr);
					continue;
				}
				if(offlineList==null){
					continue;
				}
				List<OfflineMessageRedis> objList = new ArrayList<OfflineMessageRedis>();
				for(String jsonStr:offlineList){
					OfflineMessageRedis  offlineMessageRedis= jsonBinder.fromJson(jsonStr,OfflineMessageRedis.class);
					objList.add(offlineMessageRedis);  //加入推送消息列表
				}
				Collections.sort(objList,offlineMessageRedisComparator);
				String newKey = keyStr.replaceAll(BicycleConstants.OFFLINE_KEY, BicycleConstants.OFFLINE_LIST_KEY);

				for(OfflineMessageRedis obj:objList){
					pipeLineNew.lpush(newKey,jsonBinder.toJson(obj));
				}
				pipeLineNew.ltrim(newKey,0,listSize-1); //保留0到listSize的元素，删除其它元素

			}
			pipeLineNew.sync();
		}
		catch(Exception ex){
			ex.printStackTrace();	
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	*/

	/**
	 * 模糊查询某个key的数量
	 * @param key
	 * @return
	 */
	public int getKeyCount(String key) {
		int userCount = 0;
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {	
			Collection<Jedis> jedisColl = jedis.getAllShards();
			for(Jedis jedisInfo:jedisColl){
				Set<String> set = jedisInfo.keys(key);
				if(set!=null){
					userCount+=set.size();
				}
			}
			return userCount;
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public long zCount(String key,long max,long min){
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.zcount(key, min, max);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	/**
	 * 验证某key是否存在
	 * 
	 * @param key
	 * @return
	 */
	public boolean ifKeyExists(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.exists(key);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	public boolean isKeyExistsInMap(String key,String field){
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {	
			return jedis.hexists(key, field);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public void pipleLineDelListValue(String key,List<String> idList){
		ShardedJedis jedis = shardedJedisPool.getResource();
		ShardedJedisPipeline pipeLine = jedis.pipelined();
		try {
			for (String id : idList) {
				pipeLine.lrem(key, 0, id);
			}
			pipeLine.sync();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public List<String> getSetRanomMembers(String key,int count){
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.srandmember(key, count);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public String getSetRanomMembers(String key){
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.srandmember(key);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public ShardedJedisPool getShardedJedisPool() {
		return shardedJedisPool;
	}

	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}

	public Set<Tuple> zrevrangeWithScores(String key, long min, long max) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.zrevrangeWithScores(key, min, max);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public long zremrangeByScore(String key,Double start,Double end) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.zremrangeByScore(key, start, end);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public long zremrangeByRank(String key,Long start,Long end) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.zremrangeByRank(key, start, end);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.zrangeByScoreWithScores(key, min, max);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public Long zrank(String key, String member) {
		// TODO Auto-generated method stub
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.zrank(key, member);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 集合中元素个数
	 * 
	 */
	public Long scard(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.scard(key);
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
}
