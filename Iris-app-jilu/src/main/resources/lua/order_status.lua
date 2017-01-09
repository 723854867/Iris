--[[
该脚本是用于判断订单的状态
订单状态:0-新建;1-完成;2-转单中;3-打包中;4-运输中;
--]]
goodsCount = redis.call("hget",KEYS[1],KEYS[2])
changeSuccess = redis.call("Hget",KEYS[1],KEYS[3])
if(goodsCount and changeSuccess and (goodsCount == changeSuccess))
then
	return 1
end

change = redis.call("hget",KEYS[1],KEYS[4])
packet = redis.call("hget",KEYS[1],KEYS[5])
transport = redis.call("hget",KEYS[1],KEYS[6])
finish = redis.call("hget",KEYS[1],KEYS[7])
if(change and change > 0)
then
	return 2
else if(packet and packet > 0)
	return 3
else if(transport and transport > 0)
	return 4
else if(finish and finish > 0)
	return 1
else
	return 0
end
	
