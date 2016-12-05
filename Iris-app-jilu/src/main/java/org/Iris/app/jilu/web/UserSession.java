package org.Iris.app.jilu.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.service.realm.user.User;
import org.Iris.core.exception.IrisRuntimeException;

public class UserSession extends IrisSession {
	
	private User user;

	public UserSession(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		if (null != user)
			throw new IrisRuntimeException("User Session already bind to another user!");
		this.user = user;
	}
}
