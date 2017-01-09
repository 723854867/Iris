package com.busap.vcs.webcomn.log.servlet;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UrlPathHelper;

import com.busap.vcs.webcomn.log.config.LogbackWebConfigurer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <B> caution : 目前只能适配 slf4j + logback + jcl-over-slf4j 的环境 </B> <br/>
 * PS: 使用的spring-web包里面的帮助类,所以,也需要spring环境. <br/>
 * /reset <BR/>
 * 重置所有的logger,配置输入的方式是配置文件.输入HTTP参数有: <BR/>
 * 1. newconf (可选) 新配置文件的URI,如果不输人则使用之前在web.xml中配置的文件路径 <BR/>
 * <p/>
 * /set <BR/>
 * 重置某个logger的属性,主要能修改level和additive两个属性,输入的HTTP参数有 <BR/>
 * 1. logger (必填) 修改的logger的名称,输入root为根logger <BR/>
 * 2. level (必填) 新日志的输出等级 <BR/>
 * 3. additive (可选) 指示是否遵循缺省的继承机制 <BR/>
 * /show <BR/>
 * 输出logger的属性,输入测HTTP参数有 <BR/>
 * 1. logger (可选) logger的名称,如果不输入则打印出全部logger的信息. <BR/>
 *
 * @author ydj
 */
public class LogbackSetupServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 打印一个logger的配置
     *
     * @param lg
     * @param writer
     * @throws IOException
     */
    private void outputLg(ch.qos.logback.classic.Logger lg, Writer writer)
            throws IOException {
        // String brief = "Logger Name = " + lg.getName() + ",TRACE="
        // + lg.isTraceEnabled() + ",DEBUG = "
        // + lg.isDebugEnabled() + ",INFO = "
        // + lg.isInfoEnabled() + ",WARN = "
        // + lg.isWarnEnabled() + ",ERROR = "
        // + lg.isErrorEnabled();
        writer.append("Logger '");
        writer.append(lg.getName());
        writer.append("'\n");
        writer.append("    OPTIONS: ");
        writer.append("level = ");
        writer.append(String.valueOf(lg.getLevel()));
        writer.append(",additive = ");
        writer.append(String.valueOf(lg.isAdditive()));
        writer.append("\n");
        writer.flush();
    }

    /**
     * 强制boolean转换,防止cast异常
     *
     * @param param
     * @return
     */
    private boolean fcastBoolean(String param) {
        try {
            return Boolean.valueOf(param);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        String withinPath = urlPathHelper.getPathWithinServletMapping(req);
        if (withinPath.equalsIgnoreCase("/reset")) { // 重置命令,通过配置文件重置所有的logger
            String newconf = req.getParameter("newconf");
            if (!StringUtils.isEmpty(newconf)) {
                newconf = LogbackWebConfigurer.initLogging(
                        this.getServletContext(), newconf);
            } else {
                newconf = LogbackWebConfigurer.initLogging(this
                        .getServletContext());
            }
            resp.getWriter().append("reset logback using configure file from ");
            resp.getWriter().append(newconf);
            resp.getWriter().append("\n");
            resp.getWriter().flush();
        } else if (withinPath.equalsIgnoreCase("/set")) { // 设置某个logger的级别
            String loggername = req.getParameter("logger");
            String level = req.getParameter("level");
            if (StringUtils.isEmpty(loggername)) {
                resp.getWriter().append(
                        "miss parameters logger (which logger to be change).");
            } else if (StringUtils.isEmpty(level)) {
                resp.getWriter().append(
                        "miss parameters level (which log level to be set). ");
            } else {
                if (loggername.equalsIgnoreCase("root")) {
                    loggername = org.slf4j.Logger.ROOT_LOGGER_NAME;
                }
                LoggerContext context = (LoggerContext) LoggerFactory
                        .getILoggerFactory();
                ch.qos.logback.classic.Logger lg = context
                        .getLogger(loggername);
                // print change before.
                outputLg(lg, resp.getWriter());

                String additive = req.getParameter("additive");
                if (!StringUtils.isEmpty(additive)) {
                    lg.setAdditive(fcastBoolean(additive));
                    resp.getWriter().append("Logger '");
                    resp.getWriter().append(lg.getName());
                    resp.getWriter().append("' additive has bean changed to ");
                    resp.getWriter().append(String.valueOf(lg.isAdditive()));
                    resp.getWriter().append("\n");
                    resp.getWriter().flush();
                }
                if (level.equalsIgnoreCase("trace")) {
                    lg.setLevel(Level.TRACE);
                    resp.getWriter().append("Logger '");
                    resp.getWriter().append(lg.getName());
                    resp.getWriter().append("' level has bean changed to ");
                    resp.getWriter().append("TRACE");
                    resp.getWriter().flush();
                } else if (level.equalsIgnoreCase("debug")) {
                    lg.setLevel(Level.DEBUG);
                    resp.getWriter().append("Logger '");
                    resp.getWriter().append(lg.getName());
                    resp.getWriter().append("' level has bean changed to ");
                    resp.getWriter().append("DEBUG");
                    resp.getWriter().flush();
                } else if (level.equalsIgnoreCase("warn")) {
                    lg.setLevel(Level.WARN);
                    resp.getWriter().append("Logger '");
                    resp.getWriter().append(lg.getName());
                    resp.getWriter().append("' level has bean changed to ");
                    resp.getWriter().append("WARN");
                    resp.getWriter().flush();
                } else if (level.equalsIgnoreCase("info")) {
                    lg.setLevel(Level.INFO);
                    resp.getWriter().append("Logger '");
                    resp.getWriter().append(lg.getName());
                    resp.getWriter().append("' level has bean changed to ");
                    resp.getWriter().append("INFO");
                    resp.getWriter().flush();
                } else if (level.equalsIgnoreCase("error")) {
                    lg.setLevel(Level.ERROR);
                    resp.getWriter().append("Logger '");
                    resp.getWriter().append(lg.getName());
                    resp.getWriter().append("' level has bean changed to ");
                    resp.getWriter().append("ERROR");
                    resp.getWriter().flush();
                } else {
                    resp.getWriter()
                            .append("incorrect parameters level (TRACE DEBUG INFO WARN ERROR). ");
                    resp.getWriter().append("input is ");
                    resp.getWriter().append(level);
                    resp.getWriter().flush();
                }

            }

        } else if (withinPath.equalsIgnoreCase("/show")) { // 查看logger的配置
            String loggername = req.getParameter("logger");
            if (!StringUtils.isEmpty(loggername)) {
                LoggerContext context = (LoggerContext) LoggerFactory
                        .getILoggerFactory();
                if (loggername.equalsIgnoreCase("root")) {
                    loggername = org.slf4j.Logger.ROOT_LOGGER_NAME;
                }
                ch.qos.logback.classic.Logger lg = context
                        .getLogger(loggername);
                outputLg(lg, resp.getWriter());
            } else {
                LoggerContext context = (LoggerContext) LoggerFactory
                        .getILoggerFactory();
                List<ch.qos.logback.classic.Logger> loggers = context
                        .getLoggerList();
                for (Iterator<ch.qos.logback.classic.Logger> iterator = loggers
                        .iterator(); iterator.hasNext(); ) {
                    ch.qos.logback.classic.Logger lg = (ch.qos.logback.classic.Logger) iterator
                            .next();
                    outputLg(lg, resp.getWriter());
                }
            }
        } else if (withinPath.equalsIgnoreCase("/properties")) { // 查看property的配置
            LoggerContext context = (LoggerContext) LoggerFactory
                    .getILoggerFactory();
            Writer writer = resp.getWriter();
            writer.append("logger properties is \n");
            Map<String, String> mps = context.getCopyOfPropertyMap();
            Set<Entry<String, String>> keyset = mps.entrySet();
            for (Iterator<Entry<String, String>> iterator = keyset.iterator(); iterator
                    .hasNext(); ) {
                Entry<String, String> entry = (Entry<String, String>) iterator
                        .next();
                String key = entry.getKey();
                String value = entry.getValue();

                writer.append("    key = ");
                writer.append(key);
                writer.append(", value = ");
                writer.append(value);
                writer.append("\n");
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
