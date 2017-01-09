package com.spnetty.netty;

/**
 * Created by
 * User: djyin
 * Date: 2/22/14
 * Time: 11:48 AM
 * NettyClientPool管理的客户端基本功能
 * 一般情况下，一个socket的client需要处理的常见问题有：
 * 1. 建立连接，测试连通情况，断线重连
 * 2. 处理消息
 */
public interface NettyClient {

    /**
     * 检查状态，是否激活可用
     *
     * @return the boolean
     */
    boolean isActive();

    /**
     * 准备好连接
     *
     * @return the boolean
     */
    boolean ready();

    /**
     * 关闭连接,可以再次调用ready重新建立连接
     */
    void close();


    /**
     * 销毁,已经销毁的实例,调用ready()会抛出illegelstateException
     */
    void destory();


    /**
     * 某些网络接口比较特殊,每次连接,连接后立即会接收数据,接收完成,服务器自动会中断连接
     * 这个方法就是等待服务器中断连接
     */
    void await();
}
