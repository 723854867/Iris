package org.Iris.app.jilu.consumer.listener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Message;

import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.common.model.jms.CaptchaMessage;
import org.Iris.app.jilu.common.model.jms.QueueName;
import org.Iris.util.network.email.SmtpEmailSender;
import org.Iris.util.network.http.HttpProxy;
import org.Iris.util.network.http.handler.AsyncRespHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CaptchaMessageListener extends JiLuMessageListener<CaptchaMessage> {
	
	private static final Logger logger = LoggerFactory.getLogger(CaptchaMessageListener.class);
	
	private String PARAM_TPL_VALUE		= "tpl_value";
	private String PARAM_MOBILE			= "mobile";
	
	@Value("${yunPian.apiKey}")
	private String yunPianKey;
	@Value("${yunpian.captcha.tplId}")
	private int captchaTplId;
	@Value("${yunpian.captcha.tplSendUrl}")
	private static String tplSendUrl;
	@Resource
	private HttpProxy httpProxy;
	@Resource
	private SmtpEmailSender emailSender;
	
	private NameValuePair API_KEY;
	private NameValuePair TPL_ID;
	private CaptchaSendHandler handler;
	
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
				_sendCaptcha(cm.getAccount(), cm.getCaptcha());
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
	
	private void _sendCaptcha(String mobile, String captcha) {
		String tplValue = null;
		try {
			tplValue = URLEncoder.encode("#code#", "UTF-8") + "=" + URLEncoder.encode(captcha, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("captcha url encode failure!", e);
			return;
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>(3);
		params.add(API_KEY);
		params.add(TPL_ID);
		params.add(new BasicNameValuePair(PARAM_TPL_VALUE, tplValue));
		params.add(new BasicNameValuePair(PARAM_MOBILE, mobile));
		HttpPost method = new HttpPost(tplSendUrl);
		httpProxy.asyncRequest(method, handler);
	}
	
	private class CaptchaSendHandler implements AsyncRespHandler {
		@Override
		public void completed(HttpResponse response) {
			StatusLine statusLine = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			try {
				if (null == entity)
					return;
				if (statusLine.getStatusCode() >= 300) 
					logger.error("captcha send failure!{}, {} --- {}", statusLine.getStatusCode(), statusLine.getReasonPhrase(), EntityUtils.toString(entity));
				else
					logger.info("captcha send success --- {}", EntityUtils.toString(entity));
			} catch (ParseException | IOException e) {
				e.printStackTrace();
			} finally {
				try {
					EntityUtils.consume(entity);
				} catch (IOException e) {
					logger.warn("Http entity release failure!", e);
				}
			}
		}

		@Override
		public void failed(Exception ex) {
			logger.error("captcha send exception!", ex);
		}

		@Override
		public void cancelled() {
			logger.warn("captcha send cancelled!");
		}
	}
}
