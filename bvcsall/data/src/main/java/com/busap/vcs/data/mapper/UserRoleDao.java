package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.UserRole;

import java.util.List;

/**
 * Created by huoshanwei on 2015/10/20.
 */
public interface UserRoleDao {

    List<UserRole> select(UserRole userRole);

}
