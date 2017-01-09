package org.Iris.app.jilu.service.action.merchant.parallel;

import java.util.List;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;

/**
 * 待转单列表，指商户查看自己正在转出中的订单
 * @author 樊水东
 * 2016年12月23日
 */
public class TRANSFER_ORDER_LIST extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		Merchant merchant = session.getMerchant();
		List<MemOrder> mList = merchant.getTransferOrderListByMerchantId(merchant.getMemMerchant().getMerchantId());
		if(mList == null || mList.size() == 0)
			return Result.jsonSuccess();
		return Result.jsonSuccess(merchant.getTransferOrderListModelList(mList));
	}
	
}
