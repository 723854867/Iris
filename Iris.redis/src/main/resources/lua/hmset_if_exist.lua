if redis.call("exists", KEYS[1]) == 1
then
	return redis.call("hmset", KEYS[1], unpack(ARGV))
else
	return nil
end