local token = redis.call("hget", KEYS[1], "token")
redis.call("hmset", KEYS[1], "token", ARGV[1], "lastLoginTime", ARGV[3], "updated", ARGV[3])
redis.call("hset", KEYS[2], ARGV[1], ARGV[2])
if (token) 
then
	redis.call("hdel", KEYS[2], token)
end