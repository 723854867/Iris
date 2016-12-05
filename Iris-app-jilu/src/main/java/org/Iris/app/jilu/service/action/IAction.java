package org.Iris.app.jilu.service.action;

import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.web.IrisSession;

public interface IAction<SESSION extends IrisSession> extends Beans {

	void execute(SESSION session);
}
