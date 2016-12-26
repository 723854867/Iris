redis.call("hset", KEYS[1], ARGV[1], ARGV[2])
redis.call("hset", KEYS[2], ARGV[1], ARGV[3])