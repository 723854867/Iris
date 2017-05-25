package org.Iris.app.jilu.service.action.backstage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.storage.domain.SysRolePermission;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UPDATE_ROLE_PERMISSION extends BackstageAction{
	Logger logger = LoggerFactory.getLogger(UPDATE_ROLE_PERMISSION.class);
	@Override
	protected String execute0(IrisSession session) {
		String ids = session.getKVParam(JiLuParams.IDS);
        String roleId = session.getKVParam(JiLuParams.ROLEID);
        String[] permIds = ids.split(",");
        //原有权限
        List<SysRolePermission> sysRolePermissions = sysRolePermissionMapper.getList(Integer.valueOf(roleId));
        List<String> rpIds = new ArrayList<>();
        sysRolePermissions.forEach(rp -> rpIds.add(String.valueOf(rp.getPermissionId())));
        List<SysRolePermission> save = new ArrayList<>();
        //新增的权限
        for (String id : permIds) {
            if (!rpIds.contains(id)) {
                SysRolePermission rp = new SysRolePermission();
                rp.setRoleId(Integer.valueOf(roleId));
                rp.setPermissionId(Integer.valueOf(id));
                save.add(rp);
            }
        }
        //需要取消的权限
        List<Integer> delete = new ArrayList<>();
        for (SysRolePermission rp : sysRolePermissions) {
            if (!Arrays.asList(permIds).contains(String.valueOf(rp.getPermissionId()))) {
                delete.add(rp.getId());
            }
        }
        try {
        	if(!save.isEmpty())
        		sysRolePermissionMapper.batchSave(save);
        	if(!delete.isEmpty())
        		sysRolePermissionMapper.batchDelete(delete);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("系统权限修改错误:" + e.getMessage());
            return Result.jsonError(JiLuCode.PERMISSION_UPDATE_ERROR);
        }
        return Result.jsonSuccess();
	}

}
