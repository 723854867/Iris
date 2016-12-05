package org.Iris.app.jilu.service.action;

import org.Iris.app.jilu.web.UserSession;

public abstract class UserAction implements IAction<UserSession> {
	
	public void execute(UserSession session) {
		session.write(execute0(session));
	}
	
	protected abstract String execute0(UserSession session);
}
