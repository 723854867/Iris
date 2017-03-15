package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 订单备注修改
 * @author 樊水东
 * 2017年3月15日
 */
public class ORDER_MEMO_EDIT extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String memo = session.getKVParam(JiLuParams.MEMO);
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		Merchant merchant = session.getMerchant();
		return merchant.orderMemoEdit(orderId,memo);
	}

}
