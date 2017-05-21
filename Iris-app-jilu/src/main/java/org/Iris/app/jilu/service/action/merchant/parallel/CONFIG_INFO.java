package org.Iris.app.jilu.service.action.merchant.parallel;

import java.util.List;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.storage.domain.BgConfig;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;
/**
 * 获取配置信息
 * @author 樊水东
 * 2017年5月21日
 */
public class CONFIG_INFO extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		List<BgConfig> list = bgConfigMapper.list();
		return Result.jsonSuccess(list);
	}

}
