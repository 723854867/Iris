package org.Iris.app.jilu.service.action.merchant.parallel;

import java.util.List;

import org.Iris.app.jilu.common.bean.form.Pager;
import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.storage.domain.CmsBanner;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;
/**
 * 获取公告
 * @author 樊水东
 * 2017年5月8日
 */
public class BANNER_GD_LIST extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		long count = session.getKVParam(JiLuParams.COUNT);
		List<CmsBanner> list = cmsBannerMapper.getGdBannerList(count);
		return Result.jsonSuccess(new Pager<CmsBanner>(list.size(), list));
	}
}
