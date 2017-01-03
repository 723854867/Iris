package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 添加快递号
 * @author 樊水东
 * 2017年1月3日
 */
public class EXPRESSCODE_ADD extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String packetId = session.getKVParam(JiLuParams.PACKETID);
		String expressCode = session.getKVParam(JiLuParams.EXPRESSCODE);
		Merchant merchant = session.getMerchant();
		return merchant.addExpress(packetId, expressCode);
	}

	
}
