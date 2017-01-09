package com.busap.vcs.srv.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ApplicationCtxService implements ApplicationContextAware {
	private static ApplicationContext applicationContext = null;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	
	public static ApplicationContext getCtx(){
		if (applicationContext == null) {
			throw new IllegalStateException();
		}
		return applicationContext;
	}
}
