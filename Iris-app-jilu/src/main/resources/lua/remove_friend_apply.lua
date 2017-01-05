if (redis.call("zrem", KEYS[1], ARGV[1]) == 1)
then
	local data = redis.call("hget", KEYS[2], ARGV[1])
	redis.call("hdel", KEYS[2], ARGV[1])
	return data
else
	return nil
end