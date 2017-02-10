package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.JsonAppender;

/**
 * 商户取消转单
 * @author 樊水东
 * 2016年12月26日
 */
public class ORDER_CANCEL extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		long merchantId = session.getKVParam(JiLuParams.MERCHANTID);//被转单商户编号
		Merchant merchant = session.getMerchant();
		MemOrder order = merchant.getMerchantOrderById(merchantId, orderId);
		if(null == order)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		String superOrderId = order.getSuperOrderId();
		if(null == superOrderId)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		merchantService.refuseOrder(order,merchant);
		//推送转单取消信息  参数：转单单号，转单父订单号
		String msg = JsonAppender.newAppender().append("orderId", orderId).append("superOrderId", superOrderId).toString();
		igtService.pushToSingle(merchant.getMemCid(merchantId).getClientId(), "", msg);
		
		return Result.jsonSuccess();
	}

}
