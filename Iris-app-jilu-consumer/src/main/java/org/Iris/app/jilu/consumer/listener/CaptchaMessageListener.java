package org.Iris.app.jilu.consumer.listener;

import javax.annotation.Resource;
import javax.jms.Message;

import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.common.model.jms.CaptchaMessage;
import org.Iris.app.jilu.common.model.jms.QueueName;
import org.Iris.util.network.email.SmtpEmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CaptchaMessageListener extends JiLuMessageListener<CaptchaMessage> {
	
	private static final Logger logger = LoggerFactory.getLogger(CaptchaMessageListener.class);
	
	@Resource
	private SmtpEmailSender emailSender;
	
	public CaptchaMessageListener() {
		super(QueueName.CAPTCHA);
	}

	@Override
	public void handleMessage(Message message) throws Throwable {
		CaptchaMessage cm = getObject(message);
		AccountType type = cm.getType();
		
		try {
			switch (type) {
			case MOBILE:
				
				break;
			case EMAIL:
				emailSender.sendTo("吉鹿验证码", cm.getAccount(), cm.getCaptcha());
				break;
			default:
				throw new RuntimeException("Unsupported captcha receiver device type - " + type.name());
			}
		} catch (Exception e) {
			logger.error("Captcha sender failure : {}!", cm, e);
		}
	}
}
