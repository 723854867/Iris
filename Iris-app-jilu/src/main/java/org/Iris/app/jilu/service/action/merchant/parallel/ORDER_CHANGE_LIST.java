package org.Iris.app.jilu.service.action.merchant.parallel;

import java.util.List;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;

/**
 * 获取转单申请列表
 * @author 樊水东
 * 2016年12月23日
 */
public class ORDER_CHANGE_LIST extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		MemMerchant merchant = session.getUnit().getUnit();
		List<MemOrder> mList = orderCache.getChangeOrderListByMerchantId(merchant.getMerchantId());
		if(mList == null || mList.size() == 0)
			return Result.jsonSuccess();
		return Result.jsonSuccess(orderCache.getOrderChangeListModelList(mList));
	}
	
}