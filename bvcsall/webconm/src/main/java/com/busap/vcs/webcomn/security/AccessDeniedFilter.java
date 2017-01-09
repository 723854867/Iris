package com.busap.vcs.webcomn.security;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 一个简单的过滤器,通过配置在web.xml来阻挡某路径的访问.
 * 一般用来处理*.properties,/WEB-INFO/*.xml之类
 */
public class AccessDeniedFilter implements Filter {
    private static final String ACCESSDENIED = "Access denied!";

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletResponse localHttpServletResponse = (HttpServletResponse) servletResponse;
        try {
            localHttpServletResponse.sendError(403, ACCESSDENIED);
        } catch (IOException e) {
            // ignored
        }
    }
}
