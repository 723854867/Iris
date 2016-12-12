package org.Iris.util.network.email;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class SmtpEmailSenderTest {

	public static void main(String[] args) throws AddressException, MessagingException, UnsupportedEncodingException {
		SmtpEmailSender sender = new SmtpEmailSender();
		sender.setAuth(true);
		sender.setHost("smtp.163.com");
		sender.setUsername("zxn0887472@163.com");
		sender.setPassword("zxl870613");
		sender.setSenderName("吉鹿");
		sender.setSenderEmail("zxn0887472@163.com");
		sender.init();
		sender.sendTo("发大财了", "723854867@qq.com", "我们要发大财啦");
	}
}
