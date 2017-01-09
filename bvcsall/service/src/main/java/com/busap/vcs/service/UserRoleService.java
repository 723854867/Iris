package com.busap.vcs.service;

import com.busap.vcs.data.entity.UserRole;

import java.util.List;

/**
 * Created by huoshanwei on 2015/10/20.
 */
public interface UserRoleService {

    List<UserRole> queryUserRoles(UserRole userRole);

}
