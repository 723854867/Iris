package org.Iris.app.jilu.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.service.JiLu;
import org.Iris.app.jilu.web.auth.Authenticator;
import org.Iris.app.jilu.web.handler.ErrorHandler;
import org.Iris.app.jilu.web.handler.ErrorHandler.DefaultErrorHandler;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class IrisServlet<SESSION extends IrisSession> extends HttpServlet {

	private static final long serialVersionUID = -7815032356377001691L;
	
	/**
	 * 如果要允许其他方法那么需要修改该字段，并且重写该方法；
	 * 如果要屏蔽某个方法也需要修改该字段(去掉该方法)，并且删除该方法；
	 */
	private static final String METHOD_ALLOWS					= "TRACE, OPTIONS, GET, POST";
	
	private static final String ERROR_HANDLER_NAME				= "errorHandler";
	
	@Autowired
	protected JiLu jilu;
	@Autowired
	private ErrorHandler errorHandler;
	
	protected Authenticator authenticator;
	
	@Override
	public void init() throws ServletException {
		super.init();
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		
		// if user not inject the "errorHandler" then inject the DefaultErrorHandler
		if (!context.containsBean(ERROR_HANDLER_NAME)) {
			ConfigurableApplicationContext cat = (ConfigurableApplicationContext) context;
	        DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) cat.getBeanFactory();
	        BeanDefinitionBuilder dataSourceBuider = BeanDefinitionBuilder.genericBeanDefinition(DefaultErrorHandler.class);  
	        dbf.registerBeanDefinition(ERROR_HANDLER_NAME, dataSourceBuider.getBeanDefinition());  
		}

		AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
		factory.autowireBean(this);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this._receive(req, resp);
	}
	
	private void _receive(HttpServletRequest request, HttpServletResponse response) {
		SESSION session = buildSession(request, response);
		try {
			if (null != authenticator && !authenticator.auth(session)) {
				session.write(Result.jsonError(ICode.Code.AUTH_FAIL));
				return;
			}
			
			receive(session);
		} catch (IllegalConstException e) {
			if (e.isNil())
				errorHandler.onParamNull(session, e);
			else 
				errorHandler.onParamError(session, e);
		} catch (Exception e) {
			errorHandler.onServerError(session, e);
		}
	}
	
	protected abstract SESSION buildSession(HttpServletRequest req, HttpServletResponse resp);
	
	protected abstract void receive(SESSION session);
	
	@Override
	protected final void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader(HttpHeaders.ALLOW, METHOD_ALLOWS);
	}
}
