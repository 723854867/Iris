package org.Iris.app.jilu.consumer.listener;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

@Component
public class HelloWorldMessageListener extends JiLuMessageListener {

	public HelloWorldMessageListener() {
		super("hello-world");
	}

	@Override
	public void handleMessage(Message message) throws Throwable {
		TextMessage tm = (TextMessage) message;
		System.out.println(tm.getText());
	}
}
