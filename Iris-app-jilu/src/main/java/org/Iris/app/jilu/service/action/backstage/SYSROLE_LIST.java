package org.Iris.app.jilu.service.action.backstage;

import java.util.List;

import org.Iris.app.jilu.common.bean.form.Pager;
import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.storage.domain.SysRole;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;

public class SYSROLE_LIST extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		int count = sysRoleMapper.getRolesCount();
		List<SysRole> list = sysRoleMapper.getRoles((page-1)*pageSize, pageSize);
		return Result.jsonSuccess(new Pager<SysRole>(count, list));
	}

}
