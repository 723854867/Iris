package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 同步维护客户端的个推clientId
 * @author 樊水东
 * 2017年2月7日
 */
public class SYC_CID extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		Merchant merchant = session.getMerchant();
		String cid = session.getKVParam(JiLuParams.CID);
		return merchant.sycMerchantToCID(cid);
	}

}
