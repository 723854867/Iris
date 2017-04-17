package org.Iris.app.jilu.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// 获得在下面代码中要用的request,response,session对象
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		HttpSession session = servletRequest.getSession();
		//获得请求URI
		String path = servletRequest.getRequestURI();
		//从session中获取后台登陆用户
		String account = (String)session.getAttribute("account");
		//过滤不需要验证登陆的页面
		for(String string :Constants.noFilterPage){
			if(path.indexOf(string)>-1){
				chain.doFilter(request, response);
				return;
			}
		}
		if(account == null || "".equals(account)){
			//跳转到登陆页面
			servletResponse.sendRedirect("/jilu/login.html");
		}else
			chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
