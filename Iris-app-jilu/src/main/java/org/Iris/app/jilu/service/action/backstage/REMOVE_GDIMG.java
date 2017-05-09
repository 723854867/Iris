package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.storage.domain.CmsBanner;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;

public class REMOVE_GDIMG extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		long id = session.getKVParam(JiLuParams.ID);
		CmsBanner cmsBanner = cmsBannerMapper.getBannerById(id);
		if(cmsBanner==null)
			throw IllegalConstException.errorException(JiLuParams.ID);
		cmsBanner.setGdUrl(null);
		cmsBanner.setGdType(0);
		cmsBannerMapper.update(cmsBanner);
		return Result.jsonSuccess();
	}

}
