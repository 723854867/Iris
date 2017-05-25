package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;

public class SYSROLE_DEL extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		String roleId = session.getKVParam(JiLuParams.ROLEID);
		sysRoleMapper.delete(Long.valueOf(roleId));
		return Result.jsonSuccess();
	}

}
