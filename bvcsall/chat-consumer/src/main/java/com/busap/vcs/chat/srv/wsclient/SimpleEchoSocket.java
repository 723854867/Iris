package com.busap.vcs.chat.srv.wsclient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic Echo Client Socket
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public class SimpleEchoSocket {
	
	private Logger logger = LoggerFactory.getLogger(SimpleEchoSocket.class);
	
    private final CountDownLatch closeLatch;
    
    private boolean isConnected = false;
    
    private Session session;

    public SimpleEchoSocket() {
        this.closeLatch = new CountDownLatch(1);
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
    	logger.info("await timeout{}",duration);
        return this.closeLatch.await(duration,unit);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        logger.info("Connection closed,statusCode:{},reason:{}",statusCode,reason);
        isConnected = false;
        this.session = null;
        this.closeLatch.countDown(); // trigger latch
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        logger.info("Got connect: {}",session);
        isConnected = true;
        this.session = session;
//        try{
//            Future<Void> fut;
//            fut = session.getRemote().sendStringByFuture("Hello");
//            fut.get(2,TimeUnit.SECONDS); // wait for send to complete.
//
//            fut = session.getRemote().sendStringByFuture("Thanks for the conversation.");
//            fut.get(2,TimeUnit.SECONDS); // wait for send to complete.
//
//            session.close(StatusCode.NORMAL,"I'm done");
//        } catch (Throwable t){
//            t.printStackTrace();
//        }
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        logger.info("Got msg: {}",msg);
    }
    
    public void sendBytes(byte[] bytes){
		try {
			session.getRemote().sendBytes(ByteBuffer.wrap(bytes));
		} catch (IOException e) {
			logger.error("send byte message error", e);
			e.printStackTrace();
		}
	}
    
    public void sendMessage(String textMessage){
		session.getRemote().sendStringByFuture(textMessage);
	}
    
    public boolean isConnected(){
    	if(this.session==null){
    		return false;
    	}
    	if(!this.session.isOpen()){
    		logger.info("session is closed {},connect status:{}",this.session,this.isConnected);
    		return false;
    	}
    	return this.isConnected;
    }
}