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
package com.busap.vcs.websocket.init;


import static io.netty.handler.codec.http.HttpHeaders.Names.HOST;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import org.apache.log4j.Logger;

import com.busap.vcs.websocket.bean.Base;
import com.busap.vcs.websocket.bean.PubParameter;
import com.busap.vcs.websocket.mess.MessageAdapter;
import com.busap.vcs.websocket.mess.MessageConst;
import com.busap.vcs.websocket.util.HttpUtil;

/**
 * Handles handshakes and messages
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
	private static Logger logger = Logger.getLogger(WebSocketServerHandler.class);

	private WebSocketServerHandshaker handshaker;

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) {
		logger.info("receivemessage from ip " + ctx.channel().remoteAddress());
		// 处理接受消息

		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {
			
			long starttime=System.currentTimeMillis();
			
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
			
			long	endtime=System.currentTimeMillis();
			//加入执行完毕时长
			logger.debug("receivemessage from ip " + ctx.channel().remoteAddress()+" messagetime "+(endtime-starttime));
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	private void handleHttpRequest(ChannelHandlerContext ctx,FullHttpRequest req) {
		Base base = new Base(req);
		
		String optType = HttpUtil.getOptName(req.getUri());
		logger.info("optType-----------------:" + optType);
		if (optType.equals(PubParameter.CHECK_PATH)) {

			WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
					getWebSocketLocation(req), null, false, 5 * 1024 * 1024);
			handshaker = wsFactory.newHandshaker(req);
			if (handshaker == null) {
				WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
			} else {
				handshaker.handshake(ctx.channel(), req);
			}
			//审核人员建立socket连接
			MessageAdapter.addChannel(base, ctx.channel());
			return ;
		} else if (optType.equals(PubParameter.VIDEO_PATH)) {
			WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
					getQueryWebSocketLocation(req), null, false, 5 * 1024 * 1024);
			handshaker = wsFactory.newHandshaker(req);
			if (handshaker == null) {
				WebSocketServerHandshakerFactory
						.sendUnsupportedVersionResponse(ctx.channel());
			} else {
				handshaker.handshake(ctx.channel(), req);
			}
//			TODO:视频上传建立链接	
			return;
		} 

	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx,WebSocketFrame frame) {

		if (frame instanceof CloseWebSocketFrame) {
			
			Channel channel = ctx.channel();
//			TODO: 链接断开时处理
			return;
		}
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(
					new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(String.format(
					"%s frame types not supported", frame.getClass().getName()));
		}
		String message = ((TextWebSocketFrame) frame).text();
		try {
			Channel chn = ctx.channel();

			if(MessageConst.CHECK_MESS.equals(message)){
				MessageAdapter.sendToChecker(message);
			}else if(MessageConst.LIVE_COMPLAINTS_MESS.equals(message)){
				MessageAdapter.sendToChecker(message);
			} /*else if(MessageConst.CUTIMAGE_IRREGULARITY_MESS.equals(message)){
				MessageAdapter.sendToChecker(message);
			} */
			else if(MessageConst.NEW_LIVE.equals(message)){
				MessageAdapter.sendToChecker(message);
			} else {
				MessageAdapter.sendMessage(message, chn);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		} 

	}
	
	

	private static void sendHttpResponse(ChannelHandlerContext ctx,
			FullHttpRequest req, FullHttpResponse res) {
		// Generate an error page if response getStatus code is not OK (200).
		if (res.getStatus().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),
					CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
			HttpHeaders.setContentLength(res, res.content().readableBytes());
		}

		// Send the response and close the connection if necessary.
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	private static String getWebSocketLocation(FullHttpRequest req) {
		return "ws://" + req.headers().get(HOST) + PubParameter.CHECK_PATH;
	}

	private static String getQueryWebSocketLocation(HttpRequest req) {
		return "ws://" + req.headers().get(HOST) + "/"
				+ PubParameter.VIDEO_PATH;
	}
}
