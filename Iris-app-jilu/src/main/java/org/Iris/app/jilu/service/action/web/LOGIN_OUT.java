package org.Iris.app.jilu.service.action.web;

import javax.servlet.http.HttpSession;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;

public class LOGIN_OUT extends BackstageAction {

	@Override
	protected String execute0(IrisSession session) {

		HttpSession session1 = session.getRequest().getSession(true); 
        //删除以前的
        session1.removeAttribute("merchant");
        
        return Result.jsonSuccess();
	}

}
