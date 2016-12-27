package org.Iris.app.jilu.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.service.action.MerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.service.realm.merchant.MerchantService;
import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.app.jilu.web.IrisDispatcher;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.Iris.redis.operate.lock.DistributeLock;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 和商户相关的 Servlet
 * 
 * @author Ahab
 */
public class MerchantServlet extends IrisDispatcher<MerchantSession, MerchantAction> {
	
	private static final long serialVersionUID = -3144141122029756489L;
	
	@Autowired
	private DistributeLock distributeLock;
	@Autowired
	private MerchantService merchantService;
	
	public MerchantServlet() {
		super("org.Iris.app.jilu.service.action.merchant");
	}
	
	@Override
	protected MerchantSession buildSession(HttpServletRequest request, HttpServletResponse response) {
		return new MerchantSession(request, response);
	}
	
	@Override
	protected void receive(MerchantSession session) {
		MerchantAction action = actions.get(session.getKVParam(JiLuParams.ACTION));
		if (null == action) 
			throw IllegalConstException.errorException(JiLuParams.ACTION);
		
		String token = session.getHeader(JiLuParams.TOKEN);
		String val = merchantService.getMerchantIdByToken(token);
		Merchant merchant = null;
		if (null != val) {
			long merchantId = Long.valueOf(val);
			if (action.serial()) {
				// 在执行 action 期间 token 不可能失效
				String lock = MerchantKeyGenerator.merchantLockKey(merchantId);
				String lockId = distributeLock.tryLock(lock);
				if (null == lockId) {
					session.write(Result.jsonError(ICode.Code.REQUEST_FREQUENTLY));
					return;
				}
				try {
					merchant = merchantService.getMerchantById(merchantId);
					if (null == merchant || !merchant.getMemMerchant().getToken().equals(token)) {
						session.write(Result.jsonError(ICode.Code.TOKEN_INVALID));
						return;
					}
					
					session.bind(merchant);
					action.execute(session);
				} finally {
					distributeLock.unLock(lock, lockId);
				}
			} else {
				// 有可能在执行 action 的时候 token 失效了
				merchant = merchantService.getMerchantById(merchantId);
				if (null == merchant || !merchant.getMemMerchant().getToken().equals(token)) {
					session.write(Result.jsonError(ICode.Code.TOKEN_INVALID));
					return;
				}
				
				session.bind(merchant);
				action.execute(session);
			}
		} else
			session.write(Result.jsonError(ICode.Code.TOKEN_INVALID));
	}
}
