package org.Iris.app.jilu.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.I0Itec.zkclient.ZkClient;
import org.Iris.app.jilu.common.model.Env;
import org.Iris.app.jilu.common.model.JiLuCommonConsts;
import org.Iris.util.common.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZkUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ZkUtil.class);

	@SuppressWarnings("unchecked")
	public static Map<String, String> loadConfiguration(ZkClient zkClient, Env env) { 
		byte[] buffer = zkClient.readData(JiLuCommonConsts.ZK_CONFIGURATION_PATH, true);
		if (null == buffer) {
			logger.warn("Zookeeper /configuration/jilu has no data to read!");
			return new HashMap<String, String>();
		}
		Map<String, String> map = SerializeUtil.JsonUtil.GSON.fromJson(new String(buffer), Map.class);
		for (Entry<String, String> entry : map.entrySet()) 
			map.put(entry.getKey(), entry.getValue() + "_" + env.name().toLowerCase());
		logger.info("JiLu zookeeper configuration load success, load {} queue names -- {}", map.size(), map);
		return map;
	}
}
