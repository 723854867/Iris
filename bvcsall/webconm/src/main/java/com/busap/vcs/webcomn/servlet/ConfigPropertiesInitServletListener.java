package com.busap.vcs.webcomn.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

/**
 * 配合spring的context:property-placeholder, 在增加环境变量.<br/>
 * # TOMCAT中部署的*.war, 配置文件(config.properties)和程序(war)分离的逻辑如下:
 # 在Web容器中, 根据context的名字, 动态使用不同配置文件, 并保证配置文件的优先顺序.<br/>
 # 1. ./config.${ServletContextPath}.properties, 比如 tomcat/config.restwww.properties  <br/>
 # 2. ../config.${ServletContextPath}.properties 比如 tomcat/bin/config.restwww.properties<br/>
 # 3.默认值, 会读取war文件中得 /WEB-INF/config*.properties <br/>

 */
public class ConfigPropertiesInitServletListener implements ServletContextListener {

    // 在程序第一时间初始化

    public static final String FILE = "/WEB-INF/config*.properties";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("*******************************************************************************************");
        System.out.println("[ConfigPropertiesInitServletListener] working path : " + new File(".").getAbsolutePath());

        String wctxPath = sce.getServletContext().getContextPath();
        String wctxPropertiesName;
        if (wctxPath.equals("")) {
            wctxPropertiesName = "config.properties";
        } else {
            wctxPropertiesName = "config." + wctxPath.substring(1) + ".properties";
        }
        // 检查本地目录或者上级目录
        String p = "." + File.separator + wctxPropertiesName;
        File f = new File(p);
        if (f.exists() && f.canRead()) {
            System.setProperty("config.properties", "file:" + p);
            System.out.println("[ConfigPropertiesInitServletListener] Spring CTX config.properties has bean change to : " + p);
            System.out.println("*******************************************************************************************");
            return;
        }
        p = ".." + File.separator + wctxPropertiesName;
        f = new File(p);
        if (f.exists() && f.canRead()) {
            System.setProperty("config.properties", "file:" + p);
            System.out.println("[ConfigPropertiesInitServletListener] Spring CTX config.properties has bean change to : " + p);
            System.out.println("*******************************************************************************************");
            return;
        }

        System.out.println("*******************************************************************************************");
        System.out.println("[ConfigPropertiesInitServletListener] Spring CTX config.properties has bean change to default : " + FILE);
        System.out.println("*******************************************************************************************");
        System.setProperty("config.properties", FILE);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
