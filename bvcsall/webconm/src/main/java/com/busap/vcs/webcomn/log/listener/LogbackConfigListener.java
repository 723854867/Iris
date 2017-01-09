package com.busap.vcs.webcomn.log.listener;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.busap.vcs.webcomn.log.config.LogbackWebConfigurer;

/**
 * logback配置的帮助类. 使用方法:
 * 1.在WEB-INF/web.xml中注册此listener
 * 2.在WEB-INF/web.xml中增加context
 * -param,logbackConfigLocation,值设置成logback.xml所在的uri
 *
 * @author ydj
 */
public class LogbackConfigListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        LogbackWebConfigurer.initLogging(event.getServletContext());
    }

    public void contextDestroyed(ServletContextEvent event) {
        LogbackWebConfigurer.shutdownLogging(event.getServletContext());
    }

}
