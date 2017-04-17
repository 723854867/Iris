package org.Iris.app.jilu.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.IrisDispatcher;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.exception.IllegalConstException;

/**
 * 后台
 * @author 樊水东
 * 2017年2月23日
 */
public class BackstageServlet extends IrisDispatcher<IrisSession, BackstageAction>{

	private static final long serialVersionUID = -954074316337614528L;

	public BackstageServlet(){
		super("org.Iris.app.jilu.service.action.backstage");
	}
	
	@Override
	protected IrisSession buildSession(HttpServletRequest req, HttpServletResponse resp) {
		return new IrisSession(req, resp);
	}

	@Override
	protected void receive(IrisSession session) {
		BackstageAction action = actions.get(session.getKVParam(JiLuParams.ACTION));
		if (null == action) 
			throw IllegalConstException.errorException(JiLuParams.ACTION);
		action.execute(session);
	}

}
