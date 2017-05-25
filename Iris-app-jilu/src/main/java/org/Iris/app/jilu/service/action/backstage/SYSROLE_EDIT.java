package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;

public class SYSROLE_EDIT extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		String roleId = session.getKVParam(JiLuParams.ROLEID);
		String name = session.getKVParam(JiLuParams.NAME);
		sysRoleMapper.roleUpdate(Integer.valueOf(roleId), name);
		return Result.jsonSuccess();
	}

}
