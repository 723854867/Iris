package com.spnetty.client;


import com.spnetty.util.CtxUtils;
import io.netty.channel.ChannelOption;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by djyin on 14-6-3.
 * 帮助类，用来从properties中读取client相关的配置
 */
public class PropertiesCtxLauncherUtils extends CtxUtils {

    public static final String NETTY_WORKERS = "netty.workers";
    public static final String NETTY_CONNECT_TIMEOUT = "netty.connect.timeout";
    public static final String NETTY_READY_TIMEOUT = "netty.ready.timeout";
    public static final String NETTY_TCP_NODELAY = "netty.tcp.nodelay";
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String HEARTBEAT = "heartbeat";


    public static Properties load(String filename) throws IOException {
        Properties properties = new Properties();
        properties.load(loadResource(filename));
        return properties;
    }

    /**
     * netty 的channel option
     * @param properties
     * @return
     */
    public static Map<ChannelOption<?>, Object> channelOptions(Properties properties) {
        Map<ChannelOption<?>, Object> channelOptions = new HashMap<ChannelOption<?>, Object>();
        String nodelay = properties.getProperty(NETTY_TCP_NODELAY);
        if (nodelay != null) {
            channelOptions.put(ChannelOption.TCP_NODELAY, true);
        }
        String connectTimeout = properties.getProperty(NETTY_CONNECT_TIMEOUT);
        if (connectTimeout != null && Integer.valueOf(connectTimeout) > 0) {
            channelOptions.put(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.valueOf(connectTimeout));
        }
        return channelOptions.isEmpty() ? null : channelOptions;
    }

    /**
     * netty的worker数量
     * @param properties
     * @return
     */
    public static Integer getWorkers(Properties properties) {
        String workers = properties.getProperty(NETTY_WORKERS);
        if (workers != null) {
            return Integer.valueOf(workers);
        }
        return null;
    }

    /**
     * 一个client获取连接的总消耗时间，单位ms
     * @param properties
     * @return
     */
    public static Integer getReadyTimeout(Properties properties) {
        String getReadyTimeout = properties.getProperty(NETTY_READY_TIMEOUT);
        if (getReadyTimeout != null && Integer.valueOf(getReadyTimeout) > 0) {
            return Integer.valueOf(getReadyTimeout);
        }
        return null;
    }

    /**
     * 服务器地址
     * @param properties
     * @return
     */
    public static String getHost(Properties properties) {
        String host = properties.getProperty(HOST);
        if (host != null) {
            return host;
        }
        return null;

    }

    /**
     * 端口
     * @param properties
     * @return
     */
    public static Integer getPort(Properties properties) {
        String port = properties.getProperty(PORT);
        if (port != null) {
            return  Integer.valueOf(port);
        }
        return null;
    }

    /**
     * 心跳间隔
     * @param properties
     * @return
     */
    public static Integer getHeartbeat(Properties properties) {
        String port = properties.getProperty(HEARTBEAT);
        if (port != null) {
            return  Integer.valueOf(port);
        }
        return null;
    }
}


