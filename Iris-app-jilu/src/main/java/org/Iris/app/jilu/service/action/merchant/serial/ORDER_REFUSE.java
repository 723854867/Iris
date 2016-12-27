package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;

/**
 * 拒绝转单
 * @author 樊水东
 * 2016年12月26日
 */
public class ORDER_REFUSE extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		long merchantId = session.getUnit().getUnit().getMerchantId();
		MerchantOrder order = orderCache.getHashBean(new MerchantOrder(merchantId, orderId));
		if(null == order)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		String superOrderId = order.getSuperOrderId();
		if(null == superOrderId)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		orderCache.refuseOrder(orderId, superOrderId, merchantId);
		return Result.jsonSuccess();
	}

}