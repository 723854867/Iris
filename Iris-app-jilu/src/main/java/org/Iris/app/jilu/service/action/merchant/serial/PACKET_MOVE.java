package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 手动调整包裹
 * @author 樊水东
 * 2017年3月20日
 */
public class PACKET_MOVE extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String fromPacketId = session.getKVParam(JiLuParams.FROMPACKETID);
		String toPacketId = session.getKVParam(JiLuParams.TOPACKETID);
		long id = session.getKVParam(JiLuParams.ID);
		long count = session.getKVParam(JiLuParams.COUNT);
		return merchantService.packetMove(fromPacketId,toPacketId,id,count,session.getMerchant());
	}

}
