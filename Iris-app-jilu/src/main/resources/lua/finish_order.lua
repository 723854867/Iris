if (0 == redis.call("exists", KEYS[1]))
then
	return
else
	redis.call("zincreby", KEYS[2], tonumber(ARGV[3]), ARGV[1])
	redis.call("zincreby", KEYS[3], tonumber(ARGV[2]), ARGV[1])
	redis.call("zincreby", KEYS[4], 1, ARGV[1])
end