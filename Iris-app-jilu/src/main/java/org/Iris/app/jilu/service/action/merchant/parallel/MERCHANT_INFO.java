package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.common.bean.form.MerchantForm;
import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;

/**
 * 查询商户信息接口
 * @author 樊水东
 * 2016年12月13日
 */
public class MERCHANT_INFO extends ParallelMerchantAction {

	@Override
	protected String execute0(MerchantSession session) {
		return Result.jsonSuccess(new MerchantForm(session.getMerchant()));
	}
}