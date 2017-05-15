package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.storage.domain.MemLabelBind;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
/**
 * 扫描标签二维码
 * @author 樊水东
 * 2017年5月15日
 */
public class LABEL_INFO extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		String labelId = session.getKVParam(JiLuParams.LABELID);
		MemLabelBind memLabelBind = memLabelBindMapper.findById(labelId);
		if(memLabelBind == null)
			throw IllegalConstException.errorException(JiLuParams.LABELID);
		return Result.jsonSuccess(memLabelBind);
	}


}
