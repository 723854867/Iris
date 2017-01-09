package com.busap.vcs.webcomn.interceptor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExecuteTimeInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory
            .getLogger(ExecuteTimeInterceptor.class);
    private static final String START_TIME_ATTRIBUTE_NAME = ExecuteTimeInterceptor.class
            .getName() + ".START_TIME";
    public static final String EXECUTE_TIME_ATTRIBUTE_NAME = ExecuteTimeInterceptor.class
            .getName() + ".EXECUTE_TIME";

    private static final String REDIRECT = "redirect:";

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE_NAME);
        if (startTime == null) {
            startTime = Long.valueOf(System.currentTimeMillis());
            request.setAttribute(START_TIME_ATTRIBUTE_NAME, startTime);
        }
        return true;
    }

    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        Long executeTime = (Long) request
                .getAttribute(EXECUTE_TIME_ATTRIBUTE_NAME);
        if (executeTime == null) {
            Long startTime = (Long) request
                    .getAttribute(START_TIME_ATTRIBUTE_NAME);
            Long currentTime = Long.valueOf(System.currentTimeMillis());
            executeTime = currentTime - startTime;
            request.setAttribute(START_TIME_ATTRIBUTE_NAME, startTime);
        }
        if (modelAndView != null) {
            String viewName = modelAndView.getViewName();
            if (!StringUtils.startsWith(viewName, REDIRECT)) {
                modelAndView
                        .addObject(EXECUTE_TIME_ATTRIBUTE_NAME, executeTime);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("[" + handler + "] executeTime: " + executeTime + "ms");
        }
    }
}
