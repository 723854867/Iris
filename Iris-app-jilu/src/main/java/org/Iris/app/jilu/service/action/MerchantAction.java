package org.Iris.app.jilu.service.action;

import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 单位操作
 * 
 * @author Ahab
 */
public abstract class MerchantAction implements IAction<MerchantSession> {
	
	public void execute(MerchantSession session) {
		session.write(execute0(session));
	}
	
	protected abstract String execute0(MerchantSession session);
	
	public abstract boolean serial();
}
