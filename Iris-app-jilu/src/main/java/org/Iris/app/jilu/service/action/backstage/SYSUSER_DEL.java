package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.storage.redis.BgkeyGenerator;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;

public class SYSUSER_DEL extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		int adminId = session.getKVParam(JiLuParams.ADMINID);
		sysUserMapper.deleteUser(adminId);
		sysUserRoleMapper.userRoleDel(adminId);
		redisOperate.hdel(BgkeyGenerator.sysUserDataKey(),String.valueOf(adminId));
		return Result.jsonSuccess();
	}

}
