package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;

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
		int type = session.getKVParam(JiLuParams.TYPE);//0表示安卓 1表示ios
		if(type !=0 && type !=1)
			throw IllegalConstException.errorException(JiLuParams.TYPE);
		return merchant.sycMerchantToCID(cid,type);
	}

}
