package org.Iris.app.jilu.service.action.merchant.serial;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.domain.MemOrderGoods;
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
		MemOrder superOrder = orderCache.getMerchantOrderById(session.getUnit().getUnit().getMerchantId(), orderId);
		if(null == superOrder)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		MemMerchant merchant = unitCache.getMerchantByMerchantId(merchantId).getUnit();
		if(null == merchant)
			throw IllegalConstException.errorException(JiLuParams.MERCHANTID);
		List<MemOrderGoods> changeOrderGoods = new ArrayList<MemOrderGoods>(Arrays.asList(SerializeUtil.JsonUtil.GSON.fromJson(goodsList, MemOrderGoods[].class)));
		if(null == changeOrderGoods || changeOrderGoods.size() == 0)
			throw IllegalConstException.errorException(JiLuParams.GOODSLIST);
		
		List<MemOrderGoods> list = new ArrayList<MemOrderGoods>();
		for (MemOrderGoods ogs : changeOrderGoods) {
			MemOrderGoods mGood = orderCache.getMerchantOrderGoodsById(orderId, ogs.getGoodsId());
			if (mGood == null)
				return Result.jsonError(JiLuCode.ORDER_GOODS_NOT_EXIST.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_NOT_EXIST.defaultValue(), ogs.getGoodsId()));
			if (mGood.getStatus() != 0)
				return Result.jsonError(JiLuCode.ORDER_GOODS_IS_LOCK.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_IS_LOCK.defaultValue(), ogs.getGoodsId()));
			list.add(mGood);
		}
		
		return Result.jsonSuccess(orderCache.orderChange(superOrder,merchant,session.getUnit().getUnit(),list));
	}

	
}