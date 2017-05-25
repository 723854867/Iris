package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 用户反馈
 * @author sam
 * 2017年3月3日
 */
public class MERCHANT_FEEDBACK extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String content = session.getKVParam(JiLuParams.CONTENT);
		String contact = session.getKVParam(JiLuParams.CONTACT);
		return session.getMerchant().addFeedBack(content, contact);
	}
}
