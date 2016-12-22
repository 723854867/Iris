if (0 == redis.call("exists", KEYS[1]))
then
	return
else
	redis.call("zadd", KEYS[2], 0, ARGV[1])
	redis.call("zadd", KEYS[3], 0, ARGV[1])
	redis.call("zadd", KEYS[4], 0, ARGV[1])
	redis.call("zadd", KEYS[5], tonumber(ARGV[2]), ARGV[1])
end