package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 添加邮费
 * @author 樊水东
 * 2017年1月3日
 */
public class POSTAGE_ADD extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String packetId = session.getKVParam(JiLuParams.PACKETID);
		String postage = session.getKVParam(JiLuParams.POSTAGE);
		String express = session.getKVParam(JiLuParams.EXPRESS);
		String memo = session.getKVParam(JiLuParams.MEMO);
		Merchant merchant = session.getMerchant();
		return merchant.addPostage(packetId, postage, express, memo);
	}

	
}
