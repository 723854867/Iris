package org.Iris.app.jilu.service.action.merchant;

import org.Iris.app.jilu.service.action.UnitAction;
import org.Iris.app.jilu.web.session.MerchantSession;

public abstract class SerialMerchantAction extends UnitAction<MerchantSession> {

	@Override
	public boolean serial() {
		return true;
	}
}
