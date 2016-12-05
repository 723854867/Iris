package org.Iris.app.jilu.web.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.common.JiLuParams;
import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.service.action.common.CAPTCHA_GET;
import org.Iris.app.jilu.web.IrisServlet;
import org.Iris.app.jilu.web.IrisSession;
import org.Iris.core.exception.IllegalConstException;

public class CommonServlet extends IrisServlet<IrisSession> {

	private static final long serialVersionUID = -419034761363132842L;
	
	protected Map<String, CommonAction> actions = new HashMap<String, CommonAction>();
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		// 初始化普通模块的 action
		actions.put("mobileCodeGet", 				new CAPTCHA_GET());
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
