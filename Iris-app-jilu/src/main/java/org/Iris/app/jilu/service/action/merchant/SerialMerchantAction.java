package org.Iris.app.jilu.service.action.merchant;

import org.Iris.app.jilu.service.action.MerchantAction;

public abstract class SerialMerchantAction extends MerchantAction {

	@Override
	public boolean serial() {
		return true;
	}
}
