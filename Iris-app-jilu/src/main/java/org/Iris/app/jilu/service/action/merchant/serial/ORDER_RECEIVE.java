package org.Iris.app.jilu.service.action.merchant.serial;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.storage.domain.MerchantOrder;
import org.Iris.app.jilu.storage.domain.MerchantOrderGoods;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.SerializeUtil;
import org.Iris.util.lang.DateUtils;

/**
 * 接收转单，分为部分接收和全部接收
 * @author 樊水东
 * 2016年12月26日
 */
public class ORDER_RECEIVE extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		String goodsList = session.getKVParam(JiLuParams.GOODSLIST);
		long merchantId = session.getUnit().getUnit().getMerchantId();
		MerchantOrder order = orderCache.getHashBean(new MerchantOrder(merchantId, orderId));
		if(null == order)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		String superOrderId = order.getSuperOrderId();
		if(null == superOrderId)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		List<MerchantOrderGoods> receiveGoodsList = new ArrayList<MerchantOrderGoods>(
				Arrays.asList(SerializeUtil.JsonUtil.GSON.fromJson(goodsList, MerchantOrderGoods[].class)));
		if (null == receiveGoodsList || receiveGoodsList.size() == 0)
			throw IllegalConstException.errorException(JiLuParams.GOODSLIST);
		List<MerchantOrderGoods> receiveGoodsList_ = new ArrayList<MerchantOrderGoods>();
		for (MerchantOrderGoods ogs : receiveGoodsList) {
			MerchantOrderGoods mGood = orderCache.getMerchantOrderGoodsById(orderId, ogs.getGoodsId());
			if (mGood == null)
				return Result.jsonError(JiLuCode.ORDER_GOODS_NOT_EXIST.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_NOT_EXIST.defaultValue(), ogs.getGoodsId()));
			if (mGood.getStatus() != 2)
				return Result.jsonError(JiLuCode.ORDER_GOODS_NOT_CHANGING.constId(), MessageFormat.format(JiLuCode.ORDER_GOODS_NOT_CHANGING.defaultValue(), ogs.getGoodsId()));
			mGood.setUpdated(DateUtils.currentTime());
			mGood.setStatus(0);
			receiveGoodsList_.add(mGood);
		}
		orderCache.receiveOrder(receiveGoodsList_, orderId, superOrderId, merchantId);
		return Result.jsonSuccess();
	}

}
