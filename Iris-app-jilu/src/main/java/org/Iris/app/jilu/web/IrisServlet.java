package org.Iris.app.jilu.web;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.service.JiLu;
import org.Iris.app.jilu.service.action.IAction;
import org.Iris.app.jilu.web.auth.Authenticator;
import org.Iris.app.jilu.web.handler.ErrorHandler;
import org.Iris.app.jilu.web.handler.ErrorHandler.DefaultErrorHandler;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.util.reflect.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class IrisServlet<SESSION extends IrisSession, ACTION extends IAction<SESSION>> extends HttpServlet {
	
	private static final Logger logger = LoggerFactory.getLogger(IrisServlet.class);

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
	
	protected Authenticator<SESSION> authenticator;
	protected String actionPackage;
	
	protected Map<String, ACTION> actions = new HashMap<String, ACTION>();
	
	protected IrisServlet(String actionPackage) {
		this.actionPackage = actionPackage;
	}
	
	@Override
	@SuppressWarnings({ "resource", "unchecked" })
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
		
		List<Class<?>> classes = ClassUtil.scanPackage(actionPackage, false);
		for (Class<?> clazz : classes) {
			int modifiers = clazz.getModifiers();
			if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers) || !Modifier.isPublic(modifiers))
				continue;
			try {
				actions.put(clazz.getSimpleName().toLowerCase(), (ACTION) clazz.newInstance());
			} catch (Exception e) {
				logger.error("Action load failure, system will closed!", e);
				System.exit(1);
			} 
		}
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
			if (null != authenticator && !authenticator.auth(session))
				return;
			
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
