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
public class BANNER_LIST extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		long total = cmsBannerMapper.getPublishBannerCount();
		if(total == 0)
			return Result.jsonSuccess(Pager.EMPTY);
		List<CmsBanner> list = cmsBannerMapper.getPublishBannerList((page-1)*pageSize, pageSize);
		return Result.jsonSuccess(new Pager<CmsBanner>(total, list));
	}
}
