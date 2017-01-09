package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleDao {

    List<Role> select(Map<String,Object> params);

/*    Integer selectCount(Map<String,Object> params);*/

    int update(Role role);

}
