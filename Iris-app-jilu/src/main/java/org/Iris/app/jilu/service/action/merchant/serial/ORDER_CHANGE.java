package org.Iris.app.jilu.service.action.merchant.serial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.storage.domain.Merchant;
import org.Iris.app.jilu.storage.domain.MerchantOrder;
import org.Iris.app.jilu.storage.domain.MerchantOrderGoods;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.SerializeUtil;

public class ORDER_CHANGE extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		String goodsList = session.getKVParam(JiLuParams.GOODSLIST);
		long merchantId = session.getKVParam(JiLuParams.MERCHANTID);
		MerchantOrder superOrder = orderCache.getMerchantOrderById(session.getUnit().getUnit().getMerchantId(), orderId);
		if(null == superOrder)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		Merchant merchant = unitCache.getMerchantByMerchantId(merchantId).getUnit();
		if(null == merchant)
			throw IllegalConstException.errorException(JiLuParams.MERCHANTID);
		List<Long> ids = new ArrayList<Long>(Arrays.asList(SerializeUtil.JsonUtil.GSON.fromJson(goodsList, Long[].class)));
		if(null == ids || ids.size() == 0)
			throw IllegalConstException.errorException(JiLuParams.GOODSLIST);
		List<MerchantOrderGoods> ogs = orderCache.getOGListByOrderId(ids);
		if(null == ogs || ogs.size() == 0 || ogs.size() != ids.size())
			throw IllegalConstException.errorException(JiLuParams.GOODSLIST);
		if(!orderCache.isChangedMerchantOrderGoods(ogs))
			return Result.jsonError(JiLuCode.GOODSLIST_CAN_NOT_CHANGE);
		return Result.jsonSuccess(orderCache.orderChange(superOrder,merchant,ogs));
	}

	
}
