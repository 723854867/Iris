package com.busap.vcs.service.impl;

import com.busap.vcs.data.entity.UserRole;
import com.busap.vcs.data.mapper.UserRoleDao;
import com.busap.vcs.service.UserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by busap on 2015/10/20.
 */
@Service("userRoleService")
public class UserRoleServiceImpl implements UserRoleService {

    @Resource
    private UserRoleDao userRoleDao;

    @Override
    public List<UserRole> queryUserRoles(UserRole userRole){
        return userRoleDao.select(userRole);
    }

}
