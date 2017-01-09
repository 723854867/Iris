/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.busap.vcs.chat.init;


import static io.netty.handler.codec.http.HttpHeaders.Names.HOST;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.chat.bean.Base;
import com.busap.vcs.chat.bean.PubParameter;
import com.busap.vcs.chat.mess.MessageAdapter;
import com.busap.vcs.chat.util.AdminUtil;
import com.busap.vcs.chat.util.ChatUtil;
import com.busap.vcs.chat.util.ConsumeUtil;
import com.busap.vcs.chat.util.HttpUtil;

/**
 * Handles handshakes and messages
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
	private static Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);

	private WebSocketServerHandshaker handshaker;

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) {
		logger.debug("receivemessage from ip {},message {}", ctx.channel().remoteAddress(),msg);
		// 处理接受消息

		if (msg instanceof FullHttpRequest) {
			logger.debug("do connect handler.");
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {
			logger.debug("do message handler.message {}");
			long starttime=System.currentTimeMillis();
			
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
			
			long	endtime=System.currentTimeMillis();
			//加入执行完毕时长
			logger.debug("receivemessage from ip " + ctx.channel().remoteAddress()+" messagetime "+(endtime-starttime));
		}else{
			logger.info("不识别的消息类型：{}",msg.getClass().getName());
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	private void handleHttpRequest(ChannelHandlerContext ctx,FullHttpRequest req) {
		Base base = new Base(req);
		
		String optType = HttpUtil.getOptName(req.getUri());
		logger.info("request connect,uri:{},optType:{}",req.getUri(),optType);
		if (optType.equals(PubParameter.CHAT_PATH)) {

			WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
					getWebSocketLocation(req), null, false, 5 * 1024 * 1024);
			handshaker = wsFactory.newHandshaker(req);
			if (handshaker == null) {
				WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
			} else {
				handshaker.handshake(ctx.channel(), req);
			}
			//用户建立socket连接
			if(!ChatUtil.addUser(base, ctx.channel())){
				ChatUtil.removeUser(ctx.channel());
			}
			return ;
		} else if (optType.equals(PubParameter.ADMIN_PATH)) {
			WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
					getQueryWebSocketLocation(req), null, false, 5 * 1024 * 1024);
			handshaker = wsFactory.newHandshaker(req);
			if (handshaker == null) {
				WebSocketServerHandshakerFactory
						.sendUnsupportedVersionResponse(ctx.channel());
			} else {
				handshaker.handshake(ctx.channel(), req);
			}
			if(!AdminUtil.addAdmin(ctx.channel(), base)){
				AdminUtil.removeAdmin(ctx.channel());
			}
			return;
		}else if (optType.equals(PubParameter.CONSUME_PATH)){
			WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
					getConsumeWebSocketLocation(req), null, false, 5 * 1024 * 1024);
			handshaker = wsFactory.newHandshaker(req);
			if (handshaker == null) {
				WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
			} else {
				handshaker.handshake(ctx.channel(), req);
			}
			ConsumeUtil.addChannel(ctx.channel(), base.getParams().get("client"));
			logger.info("consume client connected.");
			
			return;
		} else {
			logger.info("不合法的Url."+optType);
			ctx.channel().close();
		}

	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx,WebSocketFrame frame) {
		Channel chn = ctx.channel();
		if (frame instanceof CloseWebSocketFrame) {
			if(AdminUtil.adminChannel.containsKey(chn)){
				AdminUtil.removeAdmin(chn);
			} else if(ConsumeUtil.consumeChannels.contains(chn)){
				ConsumeUtil.removeChannel(chn);
			} else {
				ChatUtil.removeUser(chn);
			}
			return;
		}
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(
					new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		if(frame instanceof BinaryWebSocketFrame){
			BinaryWebSocketFrame message = (BinaryWebSocketFrame) frame;
			ByteBuf buf = message.content();
			logger.debug("recieve binary message.size:[]",buf.readableBytes());
			if(ConsumeUtil.consumeChannels.contains(chn)){
				ConsumeUtil.consumeBinary(buf);
			} else {
				MessageAdapter.recieveBinaryMessage(buf.array());
			}
			return;
		}
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(String.format(
					"%s frame types not supported", frame.getClass().getName()));
		}
		String message = ((TextWebSocketFrame) frame).text();
		PubParameter.recieveLog.info("recieve message:"+message);
		try {
			if(AdminUtil.adminChannel.containsKey(chn)){
				MessageAdapter.parseAdminMessage(message, chn);
			}  else if(ConsumeUtil.consumeChannels.contains(chn)){
				ConsumeUtil.consume(message);
			} else {
				MessageAdapter.parseMessage(message, chn);
			}
		} catch (Exception e2) {
			logger.error("read message exception.", e2);
		} 

	}
	

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("Caught an exception.", cause);
		ctx.close();
	}

	private static String getWebSocketLocation(FullHttpRequest req) {
		return "ws://" + req.headers().get(HOST) + PubParameter.CHAT_PATH;
	}

	private static String getQueryWebSocketLocation(HttpRequest req) {
		return "ws://" + req.headers().get(HOST) + "/"
				+ PubParameter.ADMIN_PATH;
	}
	private static String getConsumeWebSocketLocation(HttpRequest req) {
		return "ws://" + req.headers().get(HOST) + "/"
				+ PubParameter.CONSUME_PATH;
	}
}
