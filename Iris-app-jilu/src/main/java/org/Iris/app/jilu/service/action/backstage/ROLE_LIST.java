package org.Iris.app.jilu.service.action.backstage;

import java.util.List;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.storage.domain.SysRole;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;
/**
 * 角色列表
 * @author 樊水东
 * 2017年5月25日
 */
public class ROLE_LIST extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		List<SysRole> list = sysRoleMapper.getAllRoles();
		return Result.jsonSuccess(list);
	}

}
