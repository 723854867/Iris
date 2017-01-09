package com.spnetty.server;

import com.spnetty.server.util.SimpleSpringCtxLaunchUtils;
import org.springframework.context.support.AbstractApplicationContext;

public class SpringCtxServerLauncher {


    public static final String SPRING_CTX_SERVER_NAME = "springCtxServer";

    public static void main(String[] args) throws Exception {

        // 启动,处理命令行参数

        AbstractApplicationContext ctx = SimpleSpringCtxLaunchUtils.Launch(args);
        // 启动socket服务
        TcpSocketServer server = ctx.getBean(SPRING_CTX_SERVER_NAME, TcpSocketServer.class);
        server.run();

    }

}
