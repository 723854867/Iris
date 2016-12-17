package org.Iris.app.jilu.service.action;

import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.web.session.IrisSession;

public interface IAction<SESSION extends IrisSession> extends Beans {

	void execute(SESSION session);
	
	String name();
}
