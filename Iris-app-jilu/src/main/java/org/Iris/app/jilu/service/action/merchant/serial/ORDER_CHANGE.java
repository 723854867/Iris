package org.Iris.app.jilu.service.action.merchant.serial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
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
		Merchant merchant = session.getMerchant();
		if(merchantId == merchant.getMemMerchant().getMerchantId())
			return Result.jsonError(JiLuCode.SELF_LIMIT);//不能转单给自己
		MemOrder superOrder = merchant.getMerchantOrderById(merchant.getMemMerchant().getMerchantId(), orderId);
		if(null == superOrder)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		if(superOrder.getStatus() > 4)//订单在完成之后不可进行操作
			return Result.jsonError(JiLuCode.ORDER_CONNOT_OPTION);
		MemMerchant targetMerchant = merchantService.getMerchantById(merchantId).getMemMerchant();
		if(targetMerchant==null)
			throw IllegalConstException.errorException(JiLuParams.MERCHANTID);
		//List<MemOrderGoods> changeOrderGoods = new ArrayList<MemOrderGoods>(Arrays.asList(SerializeUtil.JsonUtil.GSON.fromJson(goodsList, MemOrderGoods[].class)));
		List<MemOrderGoods> changeOrderGoods = new ArrayList<MemOrderGoods>();
		try {
			changeOrderGoods = new ArrayList<MemOrderGoods>(Arrays.asList(SerializeUtil.JsonUtil.GSON.fromJson(goodsList, MemOrderGoods[].class)));
			if(changeOrderGoods.size() == 0)
				throw IllegalConstException.errorException(JiLuParams.GOODSLIST);
		} catch (Exception e) {
			e.printStackTrace();
			throw IllegalConstException.errorException(JiLuParams.GOODSLIST);
		}
		return merchantService.orderChange(superOrder,targetMerchant,changeOrderGoods,merchant);
	}

	
}
