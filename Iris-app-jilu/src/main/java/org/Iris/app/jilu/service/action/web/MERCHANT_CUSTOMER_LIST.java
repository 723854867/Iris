package org.Iris.app.jilu.service.action.web;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;

/**
 * 商户客户列表
 * @author 樊水东
 * 2017年1月13日
 */
public class MERCHANT_CUSTOMER_LIST extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		String name = session.getKVParamOptional(JiLuParams.NAME);
		String phone = session.getKVParamOptional(JiLuParams.PHONES);
		MemMerchant memMerchant = (MemMerchant)session.getRequest().getSession().getAttribute("merchant");
		if(memMerchant ==null)
			return Result.jsonError(ICode.Code.TOKEN_INVALID);
		return merchantWebService.getMerchantCustomersByNameOrPhone(page, pageSize,name,phone,memMerchant);
	}

}
