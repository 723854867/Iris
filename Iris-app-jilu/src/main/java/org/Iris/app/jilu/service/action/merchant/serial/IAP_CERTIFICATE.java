package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 接收iOS端发过来的购买凭证 
 * @author 樊水东
 * 2017年4月10日
 */
public class IAP_CERTIFICATE extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String receipt = session.getKVParam(JiLuParams.RECEIPT);
		return session.getMerchant().iapCertificate(receipt);
	}

}
