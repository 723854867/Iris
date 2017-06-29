package org.Iris.app.jilu.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.service.action.MerchantWebAction;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.web.IrisDispatcher;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantWebSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;

/**
 * 商家web端
 * @author 樊水东
 * 2017年2月23日
 */
public class MerchantWebServlet extends IrisDispatcher<MerchantWebSession, MerchantWebAction>{

	private static final long serialVersionUID = -954074316337614528L;

	public MerchantWebServlet(){
		super("org.Iris.app.jilu.service.action.web");
	}
	
	@Override
	protected MerchantWebSession buildSession(HttpServletRequest req, HttpServletResponse resp) {
		return new MerchantWebSession(req, resp);
	}

	@Override
	protected void receive(MerchantWebSession session) {
		MerchantWebAction action = actions.get(session.getKVParam(JiLuParams.ACTION));
		if (null == action) 
			throw IllegalConstException.errorException(JiLuParams.ACTION);
		MemMerchant memMerchant = (MemMerchant)session.getRequest().getSession().getAttribute("merchant");
		if(memMerchant ==null){
			session.write(Result.jsonError(ICode.Code.TOKEN_INVALID));
			return;
		}
		session.bind(memMerchant);
		action.execute(session);
	}

}
