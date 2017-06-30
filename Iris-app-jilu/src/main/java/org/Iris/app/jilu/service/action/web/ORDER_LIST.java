package org.Iris.app.jilu.service.action.web;

import org.Iris.app.jilu.service.action.MerchantWebAction;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantWebSession;
/**
 * 订单列表
 * @author caiww
 *
 */
public class ORDER_LIST extends MerchantWebAction{

	@Override
	protected String execute0(MerchantWebSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		MemMerchant merchant = session.getMemMerchant();
		return merchantWebService.getOrderList(page, pageSize, 0, merchant);
	}
}
