package org.Iris.app.jilu.service.action;

import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.app.jilu.web.session.UnitSession;

/**
 * 单位操作
 * 
 * @author Ahab
 */
public abstract class UnitAction<SESSION extends UnitSession<?>> implements IAction<MerchantSession> {
	
	public void execute(SESSION session) {
		session.write(execute0(session));
	}
	
	protected abstract String execute0(SESSION session);
	
	@Override
	public String name() {
		return this.getClass().getSimpleName();
	}
}
