package org.Iris.app.jilu.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.web.IrisServlet;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.exception.IllegalConstException;

public class CommonServlet extends IrisServlet<IrisSession, CommonAction> {

	private static final long serialVersionUID = -419034761363132842L;
	
	public CommonServlet() {
		super("org.Iris.app.jilu.service.action.common");
	}
	
	@Override
	protected IrisSession buildSession(HttpServletRequest req, HttpServletResponse resp) {
		return new IrisSession(req, resp);
	}

	@Override
	protected void receive(IrisSession session) {
		CommonAction action = actions.get(session.getKVParam(JiLuParams.ACTION));
		if (null == action) 
			throw IllegalConstException.errorException(JiLuParams.ACTION);
		action.execute(session);
	}
}
