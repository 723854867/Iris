if (redis.call("get", KEYS[1]))
then
	return -1
else
	local codeCount = redis.call("get", KEYS[2])
	if (codeCount and tonumber(codeCount) >= tonumber(ARGV[3]))
	then
		return -2
	else
		redis.call("set", KEYS[1], ARGV[1], "PX", ARGV[2])
		redis.call("incr", KEYS[2])
		redis.call("pexpire", KEYS[2], ARGV[4])
		return 0
	end
end