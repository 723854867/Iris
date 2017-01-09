package com.busap.vcs.service.security;

import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.Cacheable;

import com.busap.vcs.data.entity.Permission;
import com.busap.vcs.data.entity.Role;
import com.busap.vcs.data.entity.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by
 * User: djyin
 * Date: 12/19/13
 * Time: 4:05 AM
 * 提供查询用户权限列表的服务
 */
public interface SubjectPermissionProvider {

    /**
     * Find permissions.
     *
     * @return the list
     */
    List<String> findPermissions();

    /**
     * Gets string permission.
     *
     * @return the string permission
     */
    List<String> findPermissions(Long userId);

    /**
     * Gets string permission.
     *
     * @return the string permission
     */
    List<String> findPermissions(Subject subject);

    /**
     * Gets string permission.
     *
     * @param patten
     *         the patten
     * @return the string permission
     */
    List<String> findPermissions(Subject subject, String patten);


    /**
     * Find permissions.
     *
     * @param patten the patten
     * @return the list
     */
    List<String> findPermissions(String patten);

    /**
     * Find permissions by start with.
     *
     * @param subject the subject
     * @param startWith the start with
     * @return the list
     */
    List<String> findPermissionsByStartWith(Subject subject, String startWith);

    /**
     * Find permissions by start with.
     *
     * @param startWith the start with
     * @return the list
     */
    List<String> findPermissionsByStartWith(String startWith);
    
    
    public List<String> findPermissionsId(Long id);

}
