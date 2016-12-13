if redis.call("exists", KEYS[1]) == 1
then
	return redis.call("del", KEYS[1])
else
	return 0
end