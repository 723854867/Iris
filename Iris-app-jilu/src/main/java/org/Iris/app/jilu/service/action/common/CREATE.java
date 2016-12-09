package org.Iris.app.jilu.service.action.common;

import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

public class CREATE extends CommonAction {
	
	public static final CREATE INSTANCE						 = new CREATE();
	
	private CREATE() {}

	@Override
	protected String execute0(IrisSession session) {
		AccountType type = AccountType.match(session.getKVParamOptional(JiLuParams.TYPE));
		String account = type == AccountType.MOBILE ? session.getKVParam(JiLuParams.MOBILE) : session.getKVParam(JiLuParams.EMAIL);
		String avatar = session.getKVParam(JiLuParams.AVATAR);
		String address = session.getKVParam(JiLuParams.ADDRESS);
		return null;
	}
}
