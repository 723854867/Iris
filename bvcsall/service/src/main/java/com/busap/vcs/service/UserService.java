package com.busap.vcs.service;

import com.busap.vcs.data.entity.User;
import com.busap.vcs.data.model.UserDisplay;

import java.util.List;
import java.util.Map;


public abstract interface UserService extends BaseService<User, Long> {

    /**
     * Username exists.
     *
     * @param username the username
     * @return the boolean
     */
    public abstract boolean usernameExists(String username);

    /**
     * Find by username.
     *
     * @param username the username
     * @return the user
     */
    public abstract User findByUsername(String username);


    /**
     * Gets current.
     *
     * @return the current
     */
    public abstract User getCurrent();

    /**
     * Gets current username.
     *
     * @return the current username
     */
    public abstract String getCurrentUsername();


    List<UserDisplay> queryUsers(Map<String,Object> params);

/*    Integer queryUserCount(Map<String,Object> params);*/

    List<User> queryUserInfo(Map<String,Object> params);

    int updateUser(User user);

}
