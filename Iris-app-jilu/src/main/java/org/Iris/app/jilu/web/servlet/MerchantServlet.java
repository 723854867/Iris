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
	
	protected Map<String, UnitAction<?>> serialActions = new HashMap<String, UnitAction<?>>();
	protected Map<String, UnitAction<?>> parallelActions = new HashMap<String, UnitAction<?>>();
	
	@Override
	protected MerchantSession buildSession(HttpServletRequest request, HttpServletResponse response) {
		return new MerchantSession(request, response);
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		this.authenticator = SpringContextUtil.getBean("merchantAuthenticator", MerchantAuthenticator.class);
		
		_addSerialAction(LOGOUT.INSTANCE);
		_addSerialAction(MERCHANT_EDIT.INSTANCE);
		_addSerialAction(ORDER_EDIT.INSTANCE);
		_addSerialAction(ORDER_LOCK.INSTANCE);
		
		_addParallelAction(CUSTOMER_ADD.INSTANCE);
		_addParallelAction(MERCHANT_QUERY.INSTANCE);
		_addParallelAction(ALIYUN_ASSUME_ROLE.INSTANCE);
		_addParallelAction(ORDER_ADD.INSTANCE);
		_addParallelAction(GOODS_ADD.INSTANCE);
	}
	
	@Override
	protected void receive(MerchantSession session) {
		boolean serial = session.getKVParamOptional(JiLuParams.SERIAL);
		String action = session.getKVParam(JiLuParams.ACTION);
		UnitAction<?> unitAction = serial ? serialActions.get(action) : parallelActions.get(action);
		if (null == unitAction) 
			throw IllegalConstException.errorException(JiLuParams.ACTION);
		
		if (serial) {
			Merchant merchant = session.getUnit();
			String lockId = merchant.tryLock();
			if (null == lockId) {
				session.write(Result.jsonError(ICode.Code.REQUEST_FREQUENTLY));
				return;
			}
			
			try {
				unitAction.execute(session);
			} finally {
				merchant.unLock(lockId);
			}
		} else 
			unitAction.execute(session);
	}
	
	private void _addSerialAction(UnitAction<?> action) { 
		this.serialActions.put(action.name().toLowerCase(), action);
	}
	
	private void _addParallelAction(UnitAction<?> action) { 
		this.parallelActions.put(action.name().toLowerCase(), action);
	}
}
