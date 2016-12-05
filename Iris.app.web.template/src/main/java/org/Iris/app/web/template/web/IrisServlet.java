package org.Iris.app.web.template.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class IrisServlet extends HttpServlet {

	private static final long serialVersionUID = -7815032356377001691L;

	@Override
	public void init() throws ServletException {
		super.init();
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
		factory.autowireBean(this);
	}
}
