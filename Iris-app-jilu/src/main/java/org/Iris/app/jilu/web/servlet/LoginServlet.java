package org.Iris.app.jilu.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.common.JiLuParams;
import org.Iris.app.jilu.model.AccountType;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.app.jilu.web.IrisServlet;
import org.Iris.app.jilu.web.IrisSession;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.redis.operate.lua.LuaOperate;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginServlet extends IrisServlet<IrisSession> {

	private static final long serialVersionUID = 4517810899411632888L;
	
	@Autowired
	private LuaOperate luaOperate;

	@Override
	protected IrisSession buildSession(HttpServletRequest req, HttpServletResponse resp) {
		return new IrisSession(req, resp);
	}

	@Override
	protected void receive(IrisSession session) {
		AccountType type = AccountType.match(session.getKVParamOptional(JiLuParams.TYPE));
		String account = session.getKVParam(JiLuParams.ACCOUNT);

		switch (type) {
		case MOBILE:
		case EMAIL:
			if (!luaOperate.delIfEquals(RedisKeyGenerator.getCaptchaKey(type, account), session.getKVParam(JiLuParams.CAPTCHA))) {
				session.write(Result.jsonError(JiLuCode.CAPTCHA_ERROR));
				return;
			}
			
			
			break;
		case WECHAT:
			break;
		default:
			throw IllegalConstException.errorException(JiLuParams.TYPE);
		}
	}
}
