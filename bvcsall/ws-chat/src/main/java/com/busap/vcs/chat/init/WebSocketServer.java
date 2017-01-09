package com.busap.vcs.chat.init;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.xbean.spring.context.ResourceXmlApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import com.busap.vcs.chat.service.CheckTimeoutService;
import com.busap.vcs.chat.util.PpsConfig;

/**
 * A HTTP server which serves Web Socket requests at:
 * 
 * http://localhost:8080/websocket
 * 
 * Open your browser at http://localhost:8080/, then the demo page will be
 * loaded and a Web Socket connection will be made automatically.
 * 
 * This server illustrates support for the different web socket specification
 * versions and will work with:
 * 
 * <ul>
 * <li>Safari 5+ (draft-ietf-hybi-thewebsocketprotocol-00)
 * <li>Chrome 6-13 (draft-ietf-hybi-thewebsocketprotocol-00)
 * <li>Chrome 14+ (draft-ietf-hybi-thewebsocketprotocol-10)
 * <li>Chrome 16+ (RFC 6455 aka draft-ietf-hybi-thewebsocketprotocol-17)
 * <li>Firefox 7+ (draft-ietf-hybi-thewebsocketprotocol-10)
 * </ul>
 */
public class WebSocketServer {
	static Logger logger = Logger.getLogger(WebSocketServer.class);
	private final int port;

	private static final String SPRING_CONFIG="/spring.xml";
	
	static{
		String xml;
		try {
			xml = IOUtils.toString(WebSocketServer.class.getResourceAsStream(SPRING_CONFIG), "UTF-8");
			Resource byteResource = new ByteArrayResource(xml.getBytes());
			final ApplicationContext ctx = new ResourceXmlApplicationContext(byteResource);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public WebSocketServer(int port) {
		this.port = port;
	}

	public void run() {
		// Configure the server.
//		NioServerSocketChannelFactory nioServerSocketChannelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
//		ServerBootstrap bootstrap = new ServerBootstrap(nioServerSocketChannelFactory);
//		// Set up the event pipeline factory.
//		bootstrap.setPipelineFactory(new WebSocketServerPipelineFactory());
//
//		// Bind and start to accept incoming connections.
//		bootstrap.bind(new InetSocketAddress(port));
		
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new WebSocketServerInitializer(null))
             .option(ChannelOption.SO_BACKLOG, 65536)
             .option(ChannelOption.SO_SNDBUF, 32768)
             .option(ChannelOption.TCP_NODELAY, true)
             .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
             .childOption(ChannelOption.TCP_NODELAY, true)
             .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            
            
            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            bossGroup.shutdownGracefully();
           workerGroup.shutdownGracefully();
        }
        
		System.out.println("Web socket server started at port " + port + '.');
		System.out
				.println("Open your browser and navigate to http://localhost:"
						+ port + '/');
	}
	
	public static void main(String[] args) {
//		CheckTimeoutService checkService = new CheckTimeoutService();
//	    checkService.run();
//	    logger.info("run check service ok");
		
		WebSocketServer wsServer = new WebSocketServer(PpsConfig.getint("websokcet.port"));
		wsServer.run();
	}
}
