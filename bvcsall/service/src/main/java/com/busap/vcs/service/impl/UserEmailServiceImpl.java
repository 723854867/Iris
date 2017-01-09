package com.busap.vcs.service.impl;

import com.busap.vcs.data.entity.UserEmail;
import com.busap.vcs.data.mapper.UserEmailDao;
import com.busap.vcs.service.UserEmailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("userEmailService")
public class UserEmailServiceImpl implements UserEmailService {
	
    @Resource
    private UserEmailDao userEmailDao;
     
	@Override
	public List<UserEmail> queryUserEmails(UserEmail userEmail){
		return userEmailDao.select(userEmail);
	}

	@Override
	public int updateUserEmailInEmails(String[] emails){
		return userEmailDao.updateUserEmailInEmails(emails);
	}



}
