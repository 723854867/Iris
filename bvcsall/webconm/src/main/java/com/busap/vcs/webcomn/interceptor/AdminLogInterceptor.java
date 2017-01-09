package com.busap.vcs.webcomn.interceptor;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.busap.vcs.data.entity.OperationLog;
import com.busap.vcs.service.OperationLogService;
import com.busap.vcs.webcomn.U;

/**
 * 一个简单的管理日志拦截器
 * 应该将所有的url和对应的描述信息在初始化的时候保存，
 * 然后针对需要记录日志的url及请求参数和操作人保存到数据库或文件系统中
 */
public class AdminLogInterceptor extends HandlerInterceptorAdapter {

    AdminLogDataProvider adminLogDataProvider;
    
    private static final Logger logger = LoggerFactory
            .getLogger(AdminLogInterceptor.class);

    private static final String REDIRECT = "redirect:";
    
    @Autowired
    private OperationLogService operationLogService;
    
    @Value("${log_meduleType}")
    protected String log_meduleType;

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        // 解析request
        // 处理post请求,且非文件上传?
        // 尝试分析出id
        // 保存到request对象中
        AdminLog log = new AdminLog();
        
        String url=request.getRequestURI();
        String referer=request.getHeader("Referer");
        if (org.apache.commons.lang3.StringUtils.isBlank(referer) || !referer.contains("restadmin")){
        	return false;
        }
        String objectid=U.nvl(request.getParameter("id"));
        String method=request.getMethod();
        
        List<String> strList = new ArrayList<String>();
        Enumeration pnames=request.getParameterNames();
        while (pnames.hasMoreElements()) {
			Object object = (Object) pnames.nextElement();
			String value=request.getParameter(object.toString());
			strList.add(object+"="+value);
		}
        String params="{"+StringUtils.join(strList,",")+"}";
        log.setDescription(params);
        log.setReqUrl(url);
        log.setReferer(referer);
        log.setObjectid(objectid);
        log.setReqType(method);
        String endUrl=FilenameUtils.getName(url);
        
        /**
         * get方法或者请求list不记入日志
         * 其它的请求方法都记入日志，可能会记入一些没必要的日志 ，后期再过滤完善
         */
        if(StringUtils.isBlank(method)||(method.equalsIgnoreCase("get")&&StringUtils.isBlank(objectid))
        		||endUrl.toLowerCase().indexOf("list")!=-1){
        	return true;
        }
//        logger.info("url:{}===referer:{}===objectid:{}=======method:{}====params:{}",url,referer,objectid,method,params);
        request.setAttribute("AdminLogInterceptor.log", log);
        return true;
    }

    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView mv) {
        // 尝试分析 respbody
        Object log = request.getAttribute("AdminLogInterceptor.log");
        if (log != null) {
            // 记录业务日志到存储中
        	OperationLog otlog=new OperationLog();
        	try {
				BeanUtils.copyProperties(otlog, log);
				otlog.setUid(U.getUid());
				otlog.setUname(U.getUname());
				otlog.setModuleType(log_meduleType);
                otlog.setIp(getRemoteIp());
				operationLogService.save(otlog);
			} catch (Exception e) {
				logger.error("应该发出告警,等待开发人员查找问题并处理.....save log error.....",e);
			}
        }
    }

    /**
     * 获取远程访问IP
     *
     * @return
     */
    protected String getRemoteIp() {
        // 获取请求
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String remoteIp = request.getHeader("x-forwarded-for");
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("X-Real-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("Proxy-Client-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getRemoteAddr();
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getRemoteHost();
        }
        return remoteIp;
    }

}
