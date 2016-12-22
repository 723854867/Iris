package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.storage.domain.MerchantCustomer;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.CnToSpell;
import org.Iris.util.lang.DateUtils;

public class CUSTOMER_EDIT extends SerialMerchantAction {

	@Override
	protected String execute0(MerchantSession session) {
		String name = session.getKVParam(JiLuParams.NAME);
		String address = session.getKVParam(JiLuParams.ADDRESS);
		String mobile = session.getKVParam(JiLuParams.MOBILE);
		String memo = session.getKVParam(JiLuParams.MEMO);
		String namePrefixLetter = CnToSpell.getFirstChar(name);
		
		MerchantCustomer customer = unitCache.getMerchantCustomerById(session.getUnit().uid(), session.getKVParam(JiLuParams.CUSTOMER_ID));
		if (null == customer)
			return Result.jsonError(JiLuCode.CUSTOMER_NOT_EXIST);
		
		boolean nameSort = !namePrefixLetter.equals(customer.getNamePrefixLetter());
		customer.setName(name);
		customer.setNamePrefixLetter(namePrefixLetter);
		customer.setAddress(address);
		customer.setMobile(mobile);
		customer.setMemo(memo);
		customer.setUpdated(DateUtils.currentTime());
		unitCache.updateCustomer(customer, nameSort);
		return Result.jsonSuccess();
	}
}
