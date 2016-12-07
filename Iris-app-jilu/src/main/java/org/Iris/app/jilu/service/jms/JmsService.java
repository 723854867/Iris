package org.Iris.app.jilu.service.jms;

import java.io.IOException;

import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.common.model.jms.CaptchaMessage;
import org.Iris.app.jilu.common.model.jms.QueueName;
import org.Iris.core.util.SpringActiveMQOperator;

public class JmsService extends SpringActiveMQOperator {

	public JmsService(String queueNamesConfigurationLocation) throws IOException {
		super(queueNamesConfigurationLocation);
	}

	/**
	 * 发送验证码
	 * 
	 * @param type
	 * @param account
	 * @param code
	 */
	public void sendCaptchaMessage(AccountType type, String account, String captcha) {
		sendMessage(QueueName.CAPTCHA, generateObjectCreator(new CaptchaMessage(type, account, captcha)));
	}
}
