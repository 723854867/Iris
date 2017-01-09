package com.busap.vcs.web.interceptor;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.busap.vcs.data.entity.RequestLog;
import com.busap.vcs.service.RequestLogService;

public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory
            .getLogger(RequestInterceptor.class);
    
    @Value("${files.localpath}")
	private String basePath;
    
    @Resource(name = "requestLogService")
	private RequestLogService requestLogService;

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        
        try {
        	String url=request.getRequestURI();
        	if (!url.contains("/download/") && !url.contains("404.html")) {
        		String referer=request.getHeader("Referer");
        		String uid= request.getHeader("uid");
        		String method=request.getMethod();
        		String reqSignature = request.getHeader("reqSignature");
        		String reqTimestamp = request.getHeader("reqTimestamp");
        		
        		List<String> headerList = new ArrayList<String>();
        		Enumeration hnames = request.getHeaderNames();
        		while (hnames.hasMoreElements()) {
        			Object object = (Object) hnames.nextElement();
        			String value=request.getHeader(object.toString());
        			headerList.add(object+"="+value);
        		}
        		String headers="{"+StringUtils.join(headerList,",")+"}";
        		
        		List<String> strList = new ArrayList<String>();
        		Enumeration pnames = request.getParameterNames();
        		while (pnames.hasMoreElements()) {
        			Object object = (Object) pnames.nextElement();
        			String value=request.getParameter(object.toString());
        			strList.add(object+"="+value);
        		}
        		String params="{"+StringUtils.join(strList,",")+"}";
        		String localIp = getRemoteIp();
        		if (url.contains("/getLiveAndRecordList")) {  //记录日志文件
        			StringBuffer sb = new StringBuffer();
        			sb.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            		sb.append("|");
            		sb.append(url);
            		sb.append("|");
            		sb.append(method);
            		sb.append("|");
            		sb.append(uid);
            		sb.append("|");
            		sb.append(params);
            		sb.append("|");
            		sb.append(localIp);
            		sb.append("|");
            		sb.append(headers);
            		logger.info(sb.toString());
        		} else {  //记录日志表
        			RequestLog rl = new RequestLog();
            		rl.setUid(uid);
            		rl.setMethod(method);
            		rl.setUrl(url);
            		rl.setReferer(referer);
            		rl.setParams(params);
            		rl.setLocalIp(localIp);
            		rl.setReqSignature(reqSignature == null?"":reqSignature);
            		rl.setReqTimestamp(reqTimestamp == null?"":reqTimestamp);
            		rl.setHeaders(headers);
            		requestLogService.save(rl);
        		}
        	} 
		} catch (Exception e) {
			e.printStackTrace();
		}
        return true;
    }

    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView mv) {
    }

    /**
     * 获取远程访问IP
     *
     * @return
     */
    protected String getRemoteIp() {
        // 获取请求
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
		try {
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
				ip = request.getHeader("Proxy-Client-IP");
			}
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
				ip = request.getRemoteAddr();
			}
			if (StringUtils.isNotBlank(ip)) {
				String[] ss = ip.split(",");
				if (ss.length > 1) {
					ip = ss[0];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
    }
    
}
