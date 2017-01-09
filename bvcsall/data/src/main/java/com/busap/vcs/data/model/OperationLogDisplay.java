package com.busap.vcs.data.model;

import com.busap.vcs.data.entity.OperationLog;

/**
 * Created by busap on 2015/10/12.
 */
public class OperationLogDisplay extends OperationLog {

    private String name;

    private Long roleId;

    private String permissionName;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}
