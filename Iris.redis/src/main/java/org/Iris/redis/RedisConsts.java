package org.Iris.redis;

import org.Iris.core.consts.IrisBoolConst;
import org.Iris.core.consts.IrisIntConst;
import org.Iris.core.consts.IrisLongConst;
import org.Iris.core.consts.IrisStrConst;

public interface RedisConsts {

	final IrisIntConst REDIS_PORT										= new IrisIntConst("redis.port", 6379);
	final IrisStrConst REDIS_HOST										= new IrisStrConst("redis.host", null);
	final IrisStrConst REDIS_PASSWORDD									= new IrisStrConst("redis.password", null);
	final IrisIntConst REDIS_MIN_IDLE									= new IrisIntConst("redis.min.idle", 4);
	final IrisIntConst REDIS_MAX_IDLE									= new IrisIntConst("redis.max.idle", 12);
	final IrisBoolConst REDIS_LIFO										= new IrisBoolConst("redis.lifo", false);
	final IrisIntConst REDIS_MAX_TOTAL									= new IrisIntConst("redis.max.total", 12);
	final IrisBoolConst REDIS_TEST_ON_BORROW							= new IrisBoolConst("redis.test.on.borrow", true);
	final IrisLongConst REDIS_MAX_WAIT_MILLIS							= new IrisLongConst("redis.max.wait.millis", 3000);
	final IrisBoolConst REDIS_TEST_WHILE_IDLE							= new IrisBoolConst("redis.test.while.idle", false);
	final IrisIntConst REDIS_CONN_TIMEOUT_MILLIS						= new IrisIntConst("redis.conn.timeout.millis", 2000);
	final IrisBoolConst REIDS_BLOCK_WHEN_EXHAUSTED						= new IrisBoolConst("redis.block.when.exhausted", true);
	final IrisIntConst REDIS_NUM_TEST_PER_EVICTION_RUN					= new IrisIntConst("redis.num.test.per.eviction.run", -1);
	final IrisLongConst REDIS_MIN_EVICTABLE_IDLE_MILLIS					= new IrisLongConst("redis.min.evitable.idle.millis", -1);
	final IrisLongConst REDIS_TIME_BETWEEN_EVICTION_RUNS_MILLIS			= new IrisLongConst("redis.time.between.eviction.runs.millis", 30000);
	final IrisLongConst REDIS_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS		= new IrisLongConst("redis.soft.min.evictable.idle.time.millis", 60000);
	
	final String OK														= "OK";
}
