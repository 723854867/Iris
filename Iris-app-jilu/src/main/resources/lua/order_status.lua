--[[
该脚本是用于判断订单的状态
订单状态:0-新建;1-完成;2-转单中;3-打包中;4-运输中;
--]]
local goodsCount = redis.call("hget",KEYS[1],"goodsCount")
local transformSuccessCount = redis.call("hget",KEYS[1],"transformSuccessCount")
if(goodsCount and transformSuccessCount and (tonumber(goodsCount) == tonumber(transformSuccessCount)))
then
	return 5
end

local transformCount = redis.call("hget",KEYS[1],"transformCount")
local packetCount = redis.call("hget",KEYS[1],"packetCount")
local transportCount = redis.call("hget",KEYS[1],"transportCount")
local finishedCount = redis.call("hget",KEYS[1],"finishedCount")
if(transformCount and tonumber(transformCount) > 0)
then
	return 1
elseif(packetCount and tonumber(packetCount) > 0)
then
	return 3
elseif(transportCount and tonumber(transportCount) > 0)
then
	return 4
elseif(finishedCount and tonumber(finishedCount) > 0)
then
	return 5
else
	return 0
end
	
