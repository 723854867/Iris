package org.Iris.app.jilu.common.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.I0Itec.zkclient.ZkClient;
import org.Iris.app.jilu.common.model.Config;
import org.Iris.app.jilu.common.model.Env;
import org.Iris.util.common.IpUtil;
import org.Iris.util.common.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZkUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ZkUtil.class);

	@SuppressWarnings("unchecked")
	public static Map<String, String> loadConfiguration(ZkClient zkClient, Env env) {
		byte[] buffer = zkClient.readData(Config.ZK_CONFIGURATION_PATH, true);
		if (null == buffer) {
			logger.warn("Zookeeper /configuration/jilu has no data to read!");
			return new HashMap<String, String>();
		}
		Map<String, String> map = SerializeUtil.JsonUtil.GSON.fromJson(new String(buffer), Map.class);
		for (Entry<String, String> entry : map.entrySet()) {
			StringBuilder builder = new StringBuilder();
			builder.append(entry.getValue()).append("-").append(env.name().toLowerCase());
			if (env == Env.LOCAL) {
				String macAddr = IpUtil.getMacAddress();
				if (null != macAddr)
					builder.append("-").append(macAddr);
			}
			map.put(entry.getKey(), builder.toString());
		}
		logger.info("JiLu zookeeper configuration load success, load {} queue names -- {}", map.size(), map);
		return map;
	}
}
class Captcha implements Serializable{
	private static final long serialVersionUID = 1L;
	private String captcha;
	public Captcha(){
		
	}
	public Captcha(String captcha){
		this.captcha = captcha;
	}
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	
}