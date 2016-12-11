local data = redis.call("hgetall", KEYS[1])
redis.call("del", KEYS[1])
return data
