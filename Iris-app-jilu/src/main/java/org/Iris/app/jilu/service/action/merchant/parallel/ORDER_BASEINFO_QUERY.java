package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.domain.MemOrderPacket;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;

/**
 * 根据查询号码查询所有相关订单基本信息，查询号码可以为订单号或者快递单号
 * @author 樊水东
 * 2017年1月5日
 */
public class ORDER_BASEINFO_QUERY extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String queryId = session.getKVParam(JiLuParams.QUERYID);
		Merchant merchant = session.getMerchant();
		MemOrder order = merchant.getOrderByOrderId(queryId);
		if(order == null){
			MemOrderPacket packet = merchant.getMemOrderPacketByExpressCode(queryId);
			//检测快递单号
			if(packet == null)
				throw IllegalConstException.errorException(JiLuParams.QUERYID);
			order = merchant.getMerchantOrderById(packet.getMerchantId(), packet.getOrderId());
		}
		return merchant.queryAllOrderByOrderId(order.getRootOrderId());
	}

}
