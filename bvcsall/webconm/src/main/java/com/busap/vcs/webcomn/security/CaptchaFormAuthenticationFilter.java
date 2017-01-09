package com.busap.vcs.webcomn.security;

import com.busap.vcs.service.security.CaptchaAuthenticationToken;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 基于密码和captcha的登陆认证过滤器
 * 1、对权限的判断基于登陆（有效）/非登陆（无效）
 * 2、
 */
public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter {

    Logger logger = LoggerFactory.getLogger(CaptchaFormAuthenticationFilter.class);


    private String enPasswordParam = "enPassword";
    private String captchaIdParam = "captchaId";
    private String captchaParam = "captcha";

    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        org.apache.shiro.authc.AuthenticationToken token = createToken(request, response);
        if (token == null) {
            String msg = "createToken method implementation returned null. A valid non-null CaptchaAuthenticationToken " +
                    "must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        }
        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }
    protected org.apache.shiro.authc.AuthenticationToken createToken(
            ServletRequest servletRequest, ServletResponse servletResponse) {
        String username = getUsername(servletRequest);
        String password = getPassword(servletRequest);
        String captchaId = getCaptchaId(servletRequest);
        String captcha = getCaptcha(servletRequest);
        boolean rememberMe = isRememberMe(servletRequest);
        String host = getHost(servletRequest);
        return new CaptchaAuthenticationToken(username, password,
                captchaId, captcha, rememberMe, host);
    }
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest,
                                     ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String ajaxAsync = request.getHeader("X-Requested-With");
        if ((ajaxAsync != null)
                && (ajaxAsync.equalsIgnoreCase("XMLHttpRequest"))) {
            response.addHeader("loginStatus", "accessDenied");
            try {
                response.sendError(403);
            } catch (IOException e) {
                //
                logger.error("IOException on send http response status code", e);
            }
            return false;
        }
        try {
            return super.onAccessDenied(request, response);
        } catch (Exception e) {
            //
            logger.error("IOException on supper", e);
            return false;
        }
    }
    @Override
    protected boolean onLoginSuccess(
            org.apache.shiro.authc.AuthenticationToken token, Subject subject,
            ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 拷贝所有shiro session中的内容到HTTPSession ??
        Session session = subject.getSession();
        HashMap localHashMap = new HashMap();
        Collection attributeKeys = session.getAttributeKeys();
        Iterator iter = attributeKeys.iterator();
        while (iter.hasNext()) {
            Object attributeKey = iter.next();
            localHashMap.put(attributeKey, session.getAttribute(attributeKey));
        }
        session.stop();
        session = subject.getSession();
        iter = localHashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            session.setAttribute(entry.getKey(), entry.getValue());
        }

        try {
            return super.onLoginSuccess(token, subject, servletRequest,
                    servletResponse);
        } catch (Exception e) {
            // just log
            logger.error("Exception on supper", e);
            throw e;
        }
    }
    @Override
    protected boolean onLoginFailure(org.apache.shiro.authc.AuthenticationToken token, AuthenticationException e,
                                     ServletRequest request, ServletResponse response) {
        String message = e.getMessage();
        request.setAttribute(getFailureKeyAttribute(), message);
        return super.onLoginFailure(token,e,request,response);
    }

    protected String getPassword(ServletRequest servletRequest) {
        HttpServletRequest localHttpServletRequest = (HttpServletRequest) servletRequest;
        String password = RSACryptUtils.decryptParameter(enPasswordParam,
                localHttpServletRequest);
        RSACryptUtils.removePrivateKey(localHttpServletRequest);
        return password;
    }

    protected String getCaptchaId(ServletRequest paramServletRequest) {
        String captchaId = WebUtils.getCleanParam(paramServletRequest,
                captchaIdParam);
        if (captchaId == null) {
            captchaId = ((HttpServletRequest) paramServletRequest).getSession()
                    .getId();
        }
        return captchaId;
    }

    protected String getCaptcha(ServletRequest paramServletRequest) {
        return WebUtils.getCleanParam(paramServletRequest, captchaParam);
    }

    public String getEnPasswordParam() {
        return enPasswordParam;
    }

    public void setEnPasswordParam(String enPasswordParam) {
        this.enPasswordParam = enPasswordParam;
    }

    public String getCaptchaIdParam() {
        return captchaIdParam;
    }

    public void setCaptchaIdParam(String captchaIdParam) {
        this.captchaIdParam = captchaIdParam;
    }

    public String getCaptchaParam() {
        return captchaParam;
    }

    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }
}
