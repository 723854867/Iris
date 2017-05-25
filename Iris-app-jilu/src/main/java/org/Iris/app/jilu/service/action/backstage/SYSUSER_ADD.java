package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.storage.domain.SysUser;
import org.Iris.app.jilu.storage.domain.SysUserRole;
import org.Iris.app.jilu.storage.redis.BgkeyGenerator;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.IrisSecurity;

public class SYSUSER_ADD extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		String loginNo = session.getKVParam(JiLuParams.LOGINNO);
		String userName = session.getKVParam(JiLuParams.USERNAME);
		String password = session.getKVParam(JiLuParams.PASSWORD);
		String roleId = session.getKVParam(JiLuParams.ROLEID);
		SysUser user = sysUserMapper.getUserByLoginNo(loginNo);
		if (user != null)
			return Result.jsonError(JiLuCode.LOGINNO_ALREADY_REGISTER);
		SysUser sysUser = new SysUser(loginNo, userName, IrisSecurity.toMd5(password));
		sysUserMapper.userAdd(sysUser);
		redisOperate.hsetByJson(BgkeyGenerator.sysUserDataKey(), loginNo, sysUser);
		SysUserRole sysUserRole = new SysUserRole(sysUser.getAdminId(), Integer.valueOf(roleId));
		sysUserRoleMapper.userRoleAdd(sysUserRole);
		return Result.jsonSuccess(sysUser);
	}

}
