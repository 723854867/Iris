--[[
该脚本是用于判断订单的状态
订单状态:0-新建;1-完成;2-转单中;3-打包中;4-运输中;
--]]
goodsCount = redis.call("hget",KEYS[1],KEYS[2])
transformSuccessCount = redis.call("Hget",KEYS[1],KEYS[3])
if(goodsCount and transformSuccessCount and (tonumber(goodsCount) == tonumber(transformSuccessCount)))
then
	return 1
end

transformCount = redis.call("hget",KEYS[1],KEYS[4])
packetCount = redis.call("hget",KEYS[1],KEYS[5])
transportCount = redis.call("hget",KEYS[1],KEYS[6])
finishedCount = redis.call("hget",KEYS[1],KEYS[7])
if(transformCount and tonumber(transformCount) > 0)
then
	return 2
else if(packetCount and tonumber(packetCount) > 0)
	return 3
else if(transportCount and tonumber(transportCount) > 0)
	return 4
else if(finishedCount and tonumber(finishedCount) > 0)
	return 1
else
	return 0
end
	
