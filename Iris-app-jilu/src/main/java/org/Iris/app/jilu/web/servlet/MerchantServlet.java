package org.Iris.app.jilu.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.service.action.MerchantAction;
import org.Iris.app.jilu.service.realm.unit.merchant.Merchant;
import org.Iris.app.jilu.web.IrisServlet;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.auth.MerchantAuthenticator;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.Iris.core.util.SpringContextUtil;

/**
 * 和商户相关的 Servlet
 * 
 * @author Ahab
 */
public class MerchantServlet extends IrisServlet<MerchantSession, MerchantAction> {
	
	private static final long serialVersionUID = -3144141122029756489L;
	
	public MerchantServlet() {
		super("org.Iris.app.jilu.service.action.merchant");
	}
	
	@Override
	protected MerchantSession buildSession(HttpServletRequest request, HttpServletResponse response) {
		return new MerchantSession(request, response);
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		this.authenticator = SpringContextUtil.getBean("merchantAuthenticator", MerchantAuthenticator.class);
	}
	
	@Override
	protected void receive(MerchantSession session) {
		MerchantAction action = actions.get(session.getKVParam(JiLuParams.ACTION));
		if (null == action) 
			throw IllegalConstException.errorException(JiLuParams.ACTION);
		
		if (action.serial()) {
			Merchant merchant = session.getUnit();
			String lockId = merchant.tryLock();
			if (null == lockId) {
				session.write(Result.jsonError(ICode.Code.REQUEST_FREQUENTLY));
				return;
			}
			
			try {
				action.execute(session);
			} finally {
				merchant.unLock(lockId);
			}
		} else 
			action.execute(session);
	}
}
