package org.Iris.app.jilu.service.action;

import org.Iris.app.jilu.web.session.MerchantWebSession;

/**
 * 单位操作
 * 
 * @author Ahab
 */
public abstract class MerchantWebAction implements IAction<MerchantWebSession> {
	
	public void execute(MerchantWebSession session) {
		session.write(execute0(session));
	}
	
	protected abstract String execute0(MerchantWebSession session);
}
