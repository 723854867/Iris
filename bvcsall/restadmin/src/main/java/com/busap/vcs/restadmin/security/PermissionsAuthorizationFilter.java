package com.busap.vcs.restadmin.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.webcomn.U;

public class PermissionsAuthorizationFilter extends AuthorizationFilter {

	private static final Logger logger = LoggerFactory.getLogger(PermissionsAuthorizationFilter.class);
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		logger.info("===========invoke PermissionsAuthorizationFilter=================");
		boolean isPermitted = true;
		HttpServletRequest req=(HttpServletRequest)request;
		Subject subject = getSubject(request, response);
		String uri=req.getRequestURI();
		
        String pid = request.getParameter("pid");//获取权限的id
        if(StringUtils.isBlank(pid)){
        	pid=U.getAttribute("pid")==null?"":U.getAttribute("pid").toString();
        }
        if(StringUtils.isBlank(pid)){
        	logger.error("===========pid is null===============",req.getRequestURI());
        	isPermitted=false;
        }else if (!subject.isPermitted(pid)) {
            isPermitted = false;
        }
        if(!isPermitted){
        	logger.info("#########################{}:no autoration access permission###################",req.getRequestURI());
        }else{
        	if(StringUtils.isNotBlank(request.getParameter("pid"))){
        		U.setAttribute("pid", request.getParameter("pid"));
        	}
        }
        return isPermitted;
	}

}
