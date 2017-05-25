package org.Iris.app.jilu.service.action.backstage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.Iris.app.jilu.common.TreeHelper;
import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.storage.domain.SysPermission;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;

public class PERMISSION_TREE extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		String roleId = session.getKVParam(JiLuParams.ROLEID);
		List<SysPermission> allPermission = sysPermissionMapper.getAllPers();
		List<SysPermission> rolePermission = sysPermissionMapper.getByRoleId(Integer.valueOf(roleId));
		//去重
		List<SysPermission> result = new ArrayList<SysPermission>();  
        Set<Integer> menuIds = new HashSet<Integer>();  
        for (int i = 0; i < rolePermission.size(); i++) {  
        	SysPermission m = rolePermission.get(i);  
            if (m != null && menuIds.add(m.getPermissionId())) {  
                result.add(m);  
            }  
        }  
		TreeHelper treeHelper = new TreeHelper(allPermission, result);
		treeHelper.togetherTwoList();
	    return Result.jsonSuccess(treeHelper.getTempNodeList());
	}

}
