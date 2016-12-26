local res = redis.call("set", "sss", "hssessss", "NX", "PX", "1000000")
if (res and res["ok"] == "OK")
then
	return 1
else
	return 0
end
