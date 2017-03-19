package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
/**
 * 根据时间段查询所有订单待采购清单总和
 * @author Administrator
 *
 */
public class ALL_ORDER_GOODS_SUM extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		int start = session.getKVParam(JiLuParams.START);
		int end = session.getKVParam(JiLuParams.END);
		return session.getMerchant().allOrderGoodsSum(start,end);
	}

}
