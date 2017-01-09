package com.busap.vcs.service;


import com.busap.vcs.data.entity.UserEmail;

import java.util.List;

/**
 * Created by busap on 2015/10/10.
 */

public interface UserEmailService {

    List<UserEmail> queryUserEmails(UserEmail userEmail);

    int updateUserEmailInEmails(String[] emails);

}
