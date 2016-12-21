package org.Iris.app.jilu.web.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.service.action.UnitAction;
import org.Iris.app.jilu.service.action.merchant.parallel.ALIYUN_ASSUME_ROLE;
import org.Iris.app.jilu.service.action.merchant.parallel.CUSTOMER_ADD;
import org.Iris.app.jilu.service.action.merchant.parallel.GOODS_ADD;
import org.Iris.app.jilu.service.action.merchant.parallel.MERCHANT_QUERY;
import org.Iris.app.jilu.service.action.merchant.parallel.ORDER_ADD;
import org.Iris.app.jilu.service.action.merchant.serial.CUSTOMER_LIST;
import org.Iris.app.jilu.service.action.merchant.serial.LOGOUT;
import org.Iris.app.jilu.service.action.merchant.serial.MERCHANT_EDIT;
import org.Iris.app.jilu.service.action.merchant.serial.ORDER_EDIT;
import org.Iris.app.jilu.service.action.merchant.serial.ORDER_LOCK;
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
public class MerchantServlet extends IrisServlet<MerchantSession> {
	
	private static final long serialVersionUID = -3144141122029756489L;
	
	protected Map<String, UnitAction<?>> actions = new HashMap<String, UnitAction<?>>();
	
	@Override
	protected MerchantSession buildSession(HttpServletRequest request, HttpServletResponse response) {
		return new MerchantSession(request, response);
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		this.authenticator = SpringContextUtil.getBean("merchantAuthenticator", MerchantAuthenticator.class);
		
		_addAction(LOGOUT.INSTANCE);
		_addAction(MERCHANT_EDIT.INSTANCE);
		_addAction(ORDER_EDIT.INSTANCE);
		_addAction(ORDER_LOCK.INSTANCE);
		_addAction(CUSTOMER_LIST.INSTANCE);
		_addAction(CUSTOMER_ADD.INSTANCE);
		_addAction(MERCHANT_QUERY.INSTANCE);
		_addAction(ALIYUN_ASSUME_ROLE.INSTANCE);
		_addAction(ORDER_ADD.INSTANCE);
		_addAction(GOODS_ADD.INSTANCE);
	}
	
	@Override
	protected void receive(MerchantSession session) {
		UnitAction<?> action = actions.get(session.getKVParam(JiLuParams.ACTION));
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
	
	private void _addAction(UnitAction<?> action) { 
		this.actions.put(action.name(), action);
	}
}
