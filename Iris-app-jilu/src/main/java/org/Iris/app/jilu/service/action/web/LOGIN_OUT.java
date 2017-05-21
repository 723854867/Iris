package org.Iris.app.jilu.service.action.web;

import javax.servlet.http.HttpSession;

import org.Iris.app.jilu.service.action.MerchantWebAction;
import org.Iris.app.jilu.web.session.MerchantWebSession;
import org.Iris.core.service.bean.Result;

public class LOGIN_OUT extends MerchantWebAction {

	@Override
	protected String execute0(MerchantWebSession session) {

		HttpSession session1 = session.getRequest().getSession(true); 
        //删除以前的
        session1.removeAttribute("merchant");
        
        return Result.jsonSuccess();
	}

}
