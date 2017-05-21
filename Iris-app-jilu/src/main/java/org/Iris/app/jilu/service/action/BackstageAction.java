package org.Iris.app.jilu.service.action;

import org.Iris.app.jilu.web.session.IrisSession;

public abstract class BackstageAction implements IAction<IrisSession>{

	@Override
	public void execute(IrisSession session) {
		session.write(execute0(session));
	}
	
	protected abstract String execute0(IrisSession session);
}
