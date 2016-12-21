local time = redis.call("get", KEYS[1])
if (ARGV[1] == "3" or (not time)) 
then
	redis.call("set", KEYS[1], ARGV[2])
end
return time
