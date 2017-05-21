package org.Iris.app.jilu.service.action.web;

import java.util.List;

import org.Iris.app.jilu.service.action.MerchantWebAction;
import org.Iris.app.jilu.storage.domain.MemGoodsStore;
import org.Iris.app.jilu.web.session.MerchantWebSession;
import org.Iris.core.service.bean.Result;
/**
 * 待进货清单
 * @author fansd
 *
 */
public class DJH_LIST extends MerchantWebAction{

	@Override
	protected String execute0(MerchantWebSession session) {
		List<MemGoodsStore> list = memGoodsStoreMapper.getDjhMemGoodsStoreListByMerchantId(session.getMemMerchant().getMerchantId());
		return Result.jsonSuccess(list);
	}
}
