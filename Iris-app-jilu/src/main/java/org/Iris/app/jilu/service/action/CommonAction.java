package org.Iris.app.jilu.service.action;

import org.Iris.app.jilu.web.session.IrisSession;

public abstract class CommonAction implements IAction<IrisSession> {

	public final void execute(IrisSession session) {
		session.write(execute0(session));
	}
	
	protected abstract String execute0(IrisSession session); 
	
	@Override
	public String name() {
		return this.getClass().getSimpleName().toLowerCase();
	}
}
