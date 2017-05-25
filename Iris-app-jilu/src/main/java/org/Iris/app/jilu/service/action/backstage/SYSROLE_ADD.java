package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.storage.domain.SysRole;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;

public class SYSROLE_ADD extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		String name = session.getKVParam(JiLuParams.NAME);
		SysRole sysRole = new SysRole(name);
		sysRoleMapper.insert(sysRole);
		return Result.jsonSuccess();
	}

}
