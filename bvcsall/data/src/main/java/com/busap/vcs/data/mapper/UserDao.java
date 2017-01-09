package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.User;
import com.busap.vcs.data.model.UserDisplay;

import java.util.List;
import java.util.Map;

/**
 * Created by huoshanwei on 2015/10/12.
 */
public interface UserDao {

    List<UserDisplay> select(Map<String,Object> params);

    /*Integer selectCount(Map<String,Object> params);*/

    List<User> selectUserInfo(Map<String,Object> params);

    int update(User user);

}
