package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.UserEmail;

import java.util.List;

/**
 * Created by busap on 2015/10/10.
 */
public interface UserEmailDao {

    List<UserEmail> select(UserEmail userEmail);

    int updateUserEmailInEmails(String[] emails);

}
