local tokenProcessModel = cjson.decode(ARGV[1])
local merchantDataKey = tokenProcessModel["merchantDataKey"]
local lockKey = tokenProcessModel["merchantLockKey"]
if ((not merchantDataKey) or (not lockKey))
then
	return -2
end
if (not tokenProcessModel["initial"])
then
	local token = tokenProcessModel["token"]
	local tokenMerchantMapKey = tokenProcessModel["tokenMerchantMapKey"];
	if ((not token) or (not tokenMerchantMapKey))
	then 
		return -2
	else
		local val = redis.call("hget", tokenMerchantMapKey, token)
		if (not val)
		then 
			return -1
		else
			local merchantId = tonumber(val)
			merchantDataKey = string.gsub(merchantDataKey, "{0}", merchantId)
			lockKey = string.gsub(lockKey, "{0}", merchantId)
		end
	end
end
if (not tokenProcessModel["lock"])
then
	return redis.call("hgetall", merchantDataKey)
else
	local isLock = redis.call("set", lockKey, tokenProcessModel["lockId"], "NX", "PX", tokenProcessModel["lockTimeout"])
	if (isLock and (isLock["ok"] == "OK"))
	then
		if (tokenProcessModel["replace"])
		then
			local oldToken = redis.call("hget", merchantDataKey, "token")
			local token = tokenProcessModel["token"]
			local time = tokenProcessModel["time"]
			if (token)
			then
				redis.call("hmset", merchantDataKey, "token", token, "lastLoginTime", time, "updated", time)
				redis.call("hset", KEYS[2], ARGV[1], ARGV[2])
			else
				redis.call("hdel", merchantDataKey, "token")
				redis.call("hmset", merchantDataKey, "lastLoginTime", ARGV[3], "updated", ARGV[3])
			end
		end
	else
		return 0
	end
end
