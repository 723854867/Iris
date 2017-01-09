package com.busap.vcs.webcomn.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * The type Cookie utils.
 */
public class CookieUtils {

    private static String cookiePath = "/";

    private static String cookieDomain = "";

    public static void setCookieDomain(String cookieDomain) {
        CookieUtils.cookieDomain = cookieDomain;
    }

    public static void setCookiePath(String cookiePath) {
        CookieUtils.cookiePath = cookiePath;
    }
    /**
     * Add cookie.
     *
     * @param request the request
     * @param response the response
     * @param name the name
     * @param value the value
     * @param maxAge the max age
     * @param path the path
     * @param domain the domain
     * @param secure the secure
     */
    public static void addCookie(HttpServletRequest request,
                                 HttpServletResponse response, String name, String value,
                                 Integer maxAge, String path, String domain, Boolean secure) {
        Assert.notNull(request);
        Assert.notNull(response);
        Assert.hasText(name);
        try {
            name = URLEncoder.encode(name, "UTF-8");
            value = URLEncoder.encode(value, "UTF-8");
            Cookie cookie = new Cookie(name, value);
            if (maxAge != null) {
                cookie.setMaxAge(maxAge.intValue());
            }
            if (StringUtils.isNotEmpty(path)) {
                cookie.setPath(path);
            }
            if (StringUtils.isNotEmpty(domain)) {
                cookie.setDomain(domain);
            }
            if (secure != null) {
                cookie.setSecure(secure.booleanValue());
            }
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            // ignored
        }
    }

    /**
     * Add cookie.
     *
     * @param request the request
     * @param response the response
     * @param name the name
     * @param value the value
     * @param maxAge the max age
     */
    public static void addCookie(HttpServletRequest request,
                                 HttpServletResponse response, String name, String value,
                                 Integer maxAge) {
        addCookie(request, response, name, value, maxAge,
                cookiePath, cookieDomain, null);
    }

    /**
     * Add cookie.
     *
     * @param request the request
     * @param response the response
     * @param name the name
     * @param value the value
     */
    public static void addCookie(HttpServletRequest request,
                                 HttpServletResponse response, String name, String value) {
        addCookie(request, response, name, value, null,
                cookiePath, cookieDomain, null);
    }

    /**
     * Gets cookie.
     *
     * @param request the request
     * @param name the name
     * @return the cookie
     */
    public static String getCookie(HttpServletRequest request, String name) {
        Assert.notNull(request);
        Assert.hasText(name);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            try {
                name = URLEncoder.encode(name, "UTF-8");
                for (Cookie cookie : cookies) {
                    if (name.equals(cookie.getName())) {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Remove cookie.
     *
     * @param request the request
     * @param response the response
     * @param name the name
     * @param path the path
     * @param domain the domain
     */
    public static void removeCookie(HttpServletRequest request,
                                    HttpServletResponse response, String name, String path,
                                    String domain) {
        Assert.notNull(request);
        Assert.notNull(response);
        Assert.hasText(name);
        try {
            name = URLEncoder.encode(name, "UTF-8");
            Cookie cookie = new Cookie(name, null);
            cookie.setMaxAge(0);
            if (StringUtils.isNotEmpty(path)) {
                cookie.setPath(path);
            }
            if (StringUtils.isNotEmpty(domain)) {
                cookie.setDomain(domain);
            }
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove cookie.
     *
     * @param request the request
     * @param response the response
     * @param name the name
     */
    public static void removeCookie(HttpServletRequest request,
                                    HttpServletResponse response, String name) {
        removeCookie(request, response, name, cookiePath,
                cookieDomain);
    }
}
