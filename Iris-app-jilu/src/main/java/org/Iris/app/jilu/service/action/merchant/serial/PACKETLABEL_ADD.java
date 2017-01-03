package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 添加邮包标签
 * @author 樊水东
 * 2017年1月3日
 */
public class PACKETLABEL_ADD extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String packetId = session.getKVParam(JiLuParams.PACKETID);
		String label = session.getKVParam(JiLuParams.LABEL);
		String longitude = session.getKVParam(JiLuParams.LONGITUDE);
		String latitude = session.getKVParam(JiLuParams.LATITUDE);
		Merchant merchant = session.getMerchant();
		return merchant.addPacketLabel(packetId, label, latitude, longitude);
	}

}
