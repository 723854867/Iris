package org.Iris.app.jilu.web.listener;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

public class IrisContextLoaderListener extends ContextLoaderListener {
	
	private static final String LOGBACK_CONFIGURATION_KEY							= "logback.configurationFile";
	private static final String LOGBACK_CONFIGURATION_LOCATION						= "logbackConfigLocation";
	
	public IrisContextLoaderListener() {}

	public IrisContextLoaderListener(WebApplicationContext context) {
		super(context);
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		String logbackConfiguration = event.getServletContext().getInitParameter(LOGBACK_CONFIGURATION_LOCATION);
		if (null != logbackConfiguration)
			System.setProperty(LOGBACK_CONFIGURATION_KEY, logbackConfiguration);
		super.contextInitialized(event);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
	}
}
