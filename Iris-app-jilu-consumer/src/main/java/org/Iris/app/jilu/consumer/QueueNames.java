package org.Iris.app.jilu.consumer;

import java.util.Map;

import org.I0Itec.zkclient.ZkClient;
import org.Iris.app.jilu.common.model.Env;
import org.Iris.app.jilu.common.util.ZkUtil;

public class QueueNames {
	
	private Env env;
	private ZkClient zkClient;
	private Map<String, String> queueNames;
	
	public void init() { 
		this.queueNames = ZkUtil.loadConfiguration(zkClient, env);
	}
	
	public String getDestination(String queueName) {
		return queueNames.get(queueName);
	}
	
	public void setEnv(String env) {
		this.env = Env.match(env);
	}
	
	public void setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
	}
}
