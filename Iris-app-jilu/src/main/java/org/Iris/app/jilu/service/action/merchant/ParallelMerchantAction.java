package org.Iris.app.jilu.service.action.merchant;

import org.Iris.app.jilu.service.action.UnitAction;
import org.Iris.app.jilu.web.session.MerchantSession;

public abstract class ParallelMerchantAction extends UnitAction<MerchantSession> {

	@Override
	public boolean serial() {
		return false;
	}
}
