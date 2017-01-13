package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 根据联系人名字或手机号查询联系人列表
 * @author 樊水东
 * 2017年1月13日
 */
public class MERCHANT_CUSTOMER_LIST extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		String value = session.getKVParam(JiLuParams.VALUE);
		Merchant merchant = session.getMerchant();
		return merchant.getMerchantCustomersByNameOrPhone(value, page, pageSize);
	}

}
