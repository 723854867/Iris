package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 标签绑定
 * @author 樊水东
 * 2017年4月27日
 */
public class BINDING_LABEL extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String labelId = session.getKVParam(JiLuParams.LABELID);
		int type = session.getKVParam(JiLuParams.TYPE);
		String bindId = session.getKVParam(JiLuParams.BINDID);
		String latitude = session.getKVParam(JiLuParams.LATITUDE);
		String longitude = session.getKVParam(JiLuParams.LONGITUDE);
		String address = session.getKVParam(JiLuParams.ADDRESS);
		String bindShow = session.getKVParam(JiLuParams.BINDSHOW);
		String memo = session.getKVParamOptional(JiLuParams.MEMO);
		return session.getMerchant().bindingLabel(labelId,type,bindId,latitude,longitude,address,bindShow,memo);
	}

}
