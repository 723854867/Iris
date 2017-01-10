package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 邮包运输完成 送达客户
 * @author 樊水东
 * 2017年1月10日
 */
public class PACKET_FINISH extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String packetId = session.getKVParam(JiLuParams.PACKETID);
		Merchant merchant = session.getMerchant();
		return null;
	}

	
}
