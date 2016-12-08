package org.Iris.util.network.email;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.Iris.util.network.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmtpEmailSender {
	
	private static final Logger logger = LoggerFactory.getLogger(SmtpEmailSender.class);
	
	private int port = 25;
	private String host;
	private boolean auth;
	private String username;
	private String password;
	private String senderName;
	private String senderEmail;
	
	private String from;
	private Properties mailProps;
	private Authenticator authenticator;
	
	public void init() throws UnsupportedEncodingException {
		mailProps = new Properties();
		mailProps.put("mail.transport.protocol", Protocol.SMTP.mark());
		mailProps.put("mail.smtp.host", this.host);
		mailProps.put("mail.smtp.port", this.port);
		mailProps.put("mail.smtp.auth", String.valueOf(auth));
		if (auth) {
			authenticator = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				};
			};
		}
		this.from = String.format("%1$s <%2$s>", MimeUtility.encodeText(this.senderName, "UTF-8", "B"), this.senderEmail);
		logger.info("SMTP email sender init success!");
	}
	
	/**
	 * 发送简单文本邮件
	 * 
	 * @param subject 邮件标题
	 * @param receivers 收件人邮箱，多个邮箱用逗号隔开
	 * @param text 文本数据
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void sendTo(String subject, String receivers, String text) throws AddressException, MessagingException {
		Session session = auth ? Session.getDefaultInstance(mailProps, authenticator) : Session.getDefaultInstance(mailProps);
		session.setDebug(false);
		_sendTo(_createTextMessage(session, subject, receivers, text), session);
	}
	
	private Message _createTextMessage(Session session, String subject, String receivers, String text) throws AddressException, MessagingException { 
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(this.from));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receivers));
		message.setSubject(subject);
		message.setText(text);
		return message;
	}
	
	private void _sendTo(Message message, Session session) throws MessagingException {
		Transport ts = null;
		try {
			ts = session.getTransport();
			ts.connect();
			ts.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
		} finally {
			if (null != ts)
				ts.close();
		}
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setAuth(boolean auth) {
		this.auth = auth;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
}
