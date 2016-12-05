package org.Iris.app.jilu.web.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.common.JiLuParams;
import org.Iris.app.jilu.service.action.UserAction;
import org.Iris.app.jilu.service.realm.user.User;
import org.Iris.app.jilu.web.IrisServlet;
import org.Iris.app.jilu.web.UserSession;
import org.Iris.app.jilu.web.auth.UserAuthenticator;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.Iris.core.util.SpringContextUtil;

/**
 * 和用户相关的 Servlet，所有涉及到用户 增、删、改的操作都需要上用户锁
 * 
 * @author Ahab
 */
public class UserServlet extends IrisServlet<UserSession> {
	
	private static final long serialVersionUID = -3144141122029756489L;
	
	protected Map<String, UserAction> serialActions = new HashMap<String, UserAction>();
	protected Map<String, UserAction> parallelActions = new HashMap<String, UserAction>();
	
	@Override
	protected UserSession buildSession(HttpServletRequest request, HttpServletResponse response) {
		return new UserSession(request, response);
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		this.authenticator = SpringContextUtil.getBean("userAuthenticator", UserAuthenticator.class);
	}
	
	@Override
	protected void receive(UserSession session) {
		boolean serial = session.getKVParamOptional(JiLuParams.SERIAL);
		String action = session.getKVParam(JiLuParams.ACTION);
		UserAction userAction = serial ? serialActions.get(action) : parallelActions.get(action);
		if (null == userAction) 
			throw IllegalConstException.errorException(JiLuParams.ACTION);
		
		if (serial) {
			User user = session.getUser();
			String lockId = user.tryLock();
			if (null == lockId) {
				session.write(Result.jsonError(ICode.Code.REQUEST_FREQUENTLY));
				return;
			}
			
			try {
				userAction.execute(session);
			} finally {
				user.unLock(lockId);
			}
		} else 
			userAction.execute(session);
	}
}
