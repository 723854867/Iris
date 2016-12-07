package org.Iris.app.jilu.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	private static ClassPathXmlApplicationContext context;
	private static volatile boolean running = true;

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext("classpath:spring/spring-*.xml");
		context.start();
		logger.info("JiLu mqconsumer start!");
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                	context.stop();
                    logger.info("JiLu mqconsumer stopped!");
                } catch (Throwable t) {
                    logger.error(t.getMessage(), t);
                }
                synchronized (Main.class) {
                    running = false;
                    Main.class.notify();
                }
            }
        });
		synchronized (Main.class) {
            while (running) {
                try {
                    Main.class.wait();
                } catch (Throwable e) {}
            }
        }
	}
}
