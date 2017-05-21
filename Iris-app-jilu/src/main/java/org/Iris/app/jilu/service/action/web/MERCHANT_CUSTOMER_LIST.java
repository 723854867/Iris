package org.Iris.app.jilu.service.action.web;

import org.Iris.app.jilu.service.action.MerchantWebAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantWebSession;

/**
 * 商户客户列表
 * @author 樊水东
 * 2017年1月13日
 */
public class MERCHANT_CUSTOMER_LIST extends MerchantWebAction{

	@Override
	protected String execute0(MerchantWebSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		String name = session.getKVParamOptional(JiLuParams.NAME);
		String phone = session.getKVParamOptional(JiLuParams.PHONES);
		return merchantWebService.getMerchantCustomersByNameOrPhone(page, pageSize,name,phone,session.getMemMerchant());
	}

}
