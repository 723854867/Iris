package com.busap.vcs.webcomn.interceptor;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by djyin on 14-10-28.
 * 在返回给用户的内容(json)中, 添加用户信息
 */
public class UserinfoInterceptor extends HandlerInterceptorAdapter {
    public static final String HTTP_HEADER_ADDTIONS_USERINFO = "ADDTIONS_USERINFO";
    // 1. 分析用户请求, 检查自定义的HTTP头, 看是否需要附加用户信息
    // 2.

    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {

    }
}
