package com.spnetty.server.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.spnetty.util.CtxUtils;

/**
 * Created by
 * User: djyin
 * Date: 2/26/14
 * Time: 12:07 AM
 * 帮助类：用来从命令行中分析出配置文件，启动SpringContext环境
 */
public class SimpleSpringCtxLaunchUtils extends CtxUtils {

    public static final String DEFAULT_FILE_NAME_CONFIG = "server.properties";
    public static final String DEFAULT_FILE_NAME_LOCKBACK = "logback.xml";
    public static final String DEFAULT_FILE_NAME_SPRING_CTX_XML_FILE = "applicationContext.xml";

    public static AbstractApplicationContext ctx;
    /**
     * 启动，并载入springcontext
     *
     * @param args the args
     * @return the abstract application context
     */
    public static AbstractApplicationContext Launch(String[] args) {

        System.out.println("********************************************");
        System.out.println("*     spring powered tcp server start      *");
        System.out.println("********************************************");

        if (System.getProperty("com.sun.management.jmxremote") == null) {
            System.out.println("JMX remote is disabled");
        } else {
            String portString = System.getProperty("com.sun.management.jmxremote.port");
            if (portString != null) {
                System.out.println("JMX running on port "
                        + Integer.parseInt(portString));
            }
        }

        // 模拟PID
        int pid = pid();

        // 处理logback日志
        // 读取配置文件
        String ctxconfig = System.getProperty("ctxconfig");
        String logback = System.getProperty("logback");

        // create the Options
        try {
            // parse the command line arguments
            String lc = null;  // logback config
            if (logback != null) { // 指定了一个配置目录，在目录中读取配置文件
                lc = logback;
                File lcFile = new File(lc);
                if (!lcFile.canRead()) {
                    throw new IllegalArgumentException("illegal argument -logback: file " + lc + " is not exist");
                } else {
                    System.out.println("extra logback configuration used:");
                    System.out.println(lc);
                }
            }
            // 尝试在本地目录下寻找 logback.xml
            if (lc == null) {
                File lcFile = new File(DEFAULT_FILE_NAME_LOCKBACK);
                if (lcFile.canRead()) {
                    lc = DEFAULT_FILE_NAME_LOCKBACK;
                    System.out.println("pwd logback configuration used:");
                    System.out.println(lc);
                }else{
                    System.out.println("classpath logback configuration used");
                }
            }

            String c = null;   // spring context perperties
            if (ctxconfig != null) { // 文件中读取配置文件
                c = ctxconfig;
                File cFile = new File(c);
                if (!cFile.canRead()) {
                    throw new IllegalArgumentException("illegal argument -Dctxconfig=" + c + ", file is not exist");
                } else {
                    System.out.println("extra spring properties configuration used:");
                    System.out.println(c);
                    String nCtxFileName = pid + "." + DEFAULT_FILE_NAME_SPRING_CTX_XML_FILE;
                    System.out.println("extra spring context configuration used:");
                    System.out.println(nCtxFileName);

                }
            }
            // 尝试在本地目录下寻找 server.properties
            if (c == null) {
                File cFile = new File(DEFAULT_FILE_NAME_CONFIG);
                if (cFile.canRead()) {
                    c = DEFAULT_FILE_NAME_CONFIG;
                    System.out.println("pwd spring properties configuration used:");
                    System.out.println(lc);
                    String nCtxFileName = pid + "." + DEFAULT_FILE_NAME_SPRING_CTX_XML_FILE;
                    System.out.println("pwd spring context configuration used:");
                    System.out.println(nCtxFileName);
                }else{
                    System.out.println("classpath spring context configuration used");
                }
            }

            // 初始化logback
            if (StringUtils.isNotBlank(lc)) {
                DefaultLogbackCofigurer.initLogging(lc);
            }


            // 采用临时文件的方式，将命令行指定的 xxx.properties 配置文件和 classpath:/applicationContext.xml合并起来
            if (StringUtils.isNotBlank(c)) {
                String nCtxFileName = pid + "." + DEFAULT_FILE_NAME_SPRING_CTX_XML_FILE;
                ResourceLoader loader = new DefaultResourceLoader();
                Resource rs = loader.getResource("classpath:/" + DEFAULT_FILE_NAME_SPRING_CTX_XML_FILE);
                String xml = IOUtils.toString(rs.getInputStream(), "UTF-8");
                xml = StringUtils.replace(xml, "classpath:/" + DEFAULT_FILE_NAME_CONFIG, c);
                FileUtils.write(new File(nCtxFileName), xml, "UTF-8", false);
                ctx = new FileSystemXmlApplicationContext(nCtxFileName);
            } else {
                // 最默认使用JAR中带的配置文件

                ctx = new ClassPathXmlApplicationContext(DEFAULT_FILE_NAME_SPRING_CTX_XML_FILE);
            }

            ctx.registerShutdownHook();
            return ctx;

        } catch (IOException e) {
            System.err.println("Unexpected IOException: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }


}
