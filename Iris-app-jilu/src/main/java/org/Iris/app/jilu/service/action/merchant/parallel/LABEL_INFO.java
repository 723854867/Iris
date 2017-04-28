package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.storage.domain.MemLabelBind;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;

public class LABEL_INFO extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String labelId = session.getKVParam(JiLuParams.LABELID);
		MemLabelBind memLabelBind = memLabelBindMapper.findById(labelId);
		if(memLabelBind == null)
			throw IllegalConstException.errorException(JiLuParams.LABELID);
		return Result.jsonSuccess(memLabelBind);
	}

}
