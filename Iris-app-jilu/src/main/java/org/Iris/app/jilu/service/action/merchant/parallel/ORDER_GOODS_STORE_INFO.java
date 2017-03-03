package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 订单未处理产品以及对应库存信息查询
 * @author 樊水东
 * 2017年3月3日
 */
public class ORDER_GOODS_STORE_INFO extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		return session.getMerchant().orderGoodsStoreInfo(orderId);
	}

}
