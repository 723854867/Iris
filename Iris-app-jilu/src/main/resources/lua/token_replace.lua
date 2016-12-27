local isLock = redis.call("set", KEYS[1], ARGV[1], "NX", "PX", ARGV[5])
if (isLock and (isLock["ok"] == "OK"))
then
	local oldToken = redis.call("hget", KEYS[2], "token")
	redis.call("hmset", KEYS[2], "token", ARGV[2], "lastLoginTime", ARGV[3], "updated", ARGV[3])
	redis.call("hset", KEYS[3], ARGV[2], ARGV[4])
	if (oldToken)
	then
		redis.call("hdel", KEYS[3], oldToken)
	end
	return 1
else
	return 0
end