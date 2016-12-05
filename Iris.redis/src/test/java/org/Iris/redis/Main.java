package org.Iris.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

public class Main {

	public static void main(String[] args) {
		HostAndPort hostAndPort = new HostAndPort("121.42.155.96", 6000);
		Jedis jedis = new Jedis(hostAndPort.getHost(), hostAndPort.getPort(), 2000,
		        2000, false, null, null, null);
		jedis.connect();
		System.out.println(jedis.auth("zxl870613"));
	}

}
