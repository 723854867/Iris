package com.busap.vcs.webcomn.log.servlet;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class SissyServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory
            .getLogger(SissyServlet.class);
    private transient SissyControl control = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (control == null) {
            control = new SissyControl();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        String withinPath = urlPathHelper.getPathWithinServletMapping(req);
        if (withinPath.equalsIgnoreCase("/run")) { // 命令,打印无序日志

            control.runJibberJabber(1, 1, 3000);
            resp.getWriter().append("Jibber-jabber has bean started");
            resp.getWriter().flush();
        } else if (withinPath.equalsIgnoreCase("/stop")) { // 命令,关闭无序的命令
            control.stopJibberJabber();
            resp.getWriter().append("Jibber-jabber has bean stoped");
            resp.getWriter().flush();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}

class SissyControl {
    private static final Logger logger = LoggerFactory
            .getLogger(SissyControl.class);

    private final ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(1);
    private ScheduledFuture<?> lstCancel = null;

    /**
     * 停止打印无序的日志
     */
    public void stopJibberJabber() {
        lstCancel.cancel(true);
    }

    /**
     * 开始定时打印无序的日志
     *
     * @param initialDelay
     * @param period
     * @param finalStopPeriod
     * @return
     */
    public void runJibberJabber(long initialDelay, long period,
                                long finalStopPeriod) {
        if (lstCancel != null) {
            lstCancel.cancel(true);
        }
        final Runnable jibberJabber = new Runnable() {

            public void randomLog() {
                int r = RandomUtils.nextInt(5);
                String msg = "The quick brown fox jumps over the lazy dog";
                Throwable th = null;
                if (RandomUtils.nextBoolean()) {
                    th = new Throwable() {
                        private static final long serialVersionUID = 8576038657175946287L;

                        @Override
                        public String getMessage() {
                            return RandomUtils.nextInt(0xFFFFF)
                                    + ":测试异常,错误码无意义";
                        }
                    };
                }
                switch (r) {
                    case 1:// debug
                        logger.debug(msg, th);
                        break;
                    case 2:// info
                        logger.info(msg, th);
                        break;
                    case 3:// warn
                        logger.warn(msg, th);
                        break;
                    case 4:// error
                        logger.error(msg, th);
                        break;
                    default:// off

                }
            }

            public void run() {
                randomLog();
            }
        };

        final ScheduledFuture<?> cancelHandle = scheduler.scheduleAtFixedRate(
                jibberJabber, initialDelay, period, SECONDS);
        scheduler.schedule(new Runnable() {
            public void run() {
                cancelHandle.cancel(true);
            }
        }, finalStopPeriod, SECONDS);

        this.lstCancel = cancelHandle;

    }
}