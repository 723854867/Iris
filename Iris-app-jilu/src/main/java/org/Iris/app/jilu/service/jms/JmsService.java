package org.Iris.app.jilu.service.jms;

import org.I0Itec.zkclient.ZkClient;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.common.model.Env;
import org.Iris.app.jilu.common.model.jms.CaptchaMessage;
import org.Iris.app.jilu.common.model.jms.QueueName;
import org.Iris.app.jilu.common.util.ZkUtil;
import org.Iris.core.util.SpringActiveMQOperator;

public class JmsService extends SpringActiveMQOperator {
	
	private ZkClient zkClient;
	
	public void init(Env env) { 
		this.queueNames = ZkUtil.loadConfiguration(zkClient, env);
	}

	/**
	 * 发送验证码
	 * 
	 * @param type
	 * @param account
	 * @param code
	 */
	public void sendCaptchaMessage(AccountType type, String account, String captcha) {
		sendMessage(getQueueName(QueueName.CAPTCHA), generateObjectCreator(new CaptchaMessage(type, account, captcha)));
	}
	
	public void setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
	}
}
