package org.Iris.app.jilu.consumer.listener;

import javax.jms.Message;

import org.Iris.app.jilu.common.model.jms.CaptchaMessage;
import org.Iris.app.jilu.common.model.jms.QueueName;
import org.springframework.stereotype.Component;

@Component
public class CaptchaMessageListener extends JiLuMessageListener<CaptchaMessage> {
	
	public CaptchaMessageListener() {
		super(QueueName.CAPTCHA);
	}

	@Override
	public void handleMessage(Message message) throws Throwable {
		CaptchaMessage cm = getObject(message);
		System.out.println(cm.getCaptcha());
	}
}
