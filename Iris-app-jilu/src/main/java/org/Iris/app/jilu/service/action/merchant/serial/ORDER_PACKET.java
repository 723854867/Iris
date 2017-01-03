package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 分包
 * @author 樊水东
 * 2017年1月3日
 */
public class ORDER_PACKET extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		/**
		 * packetGoodsList的结构 "1:2;3:4;..."
		 */
		String packetGoodsList = session.getKVParam(JiLuParams.PACKETGOODSLIST);
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		Merchant merchant = session.getMerchant();
		return merchant.orderPacket(orderId, packetGoodsList);
	}
	
}
