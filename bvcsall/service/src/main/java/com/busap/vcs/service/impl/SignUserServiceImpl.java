package com.busap.vcs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.SignUser;
import com.busap.vcs.data.mapper.SignUserDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.SignUserRepository;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.SignUserService;
@Service("signUserService")
public class SignUserServiceImpl extends BaseServiceImpl<SignUser,Long> implements SignUserService  {
    @Resource(name = "signUserRepository")
    private SignUserRepository signRepository;
    
    @Resource(name = "signUserRepository")
    @Override
    public void setBaseRepository(BaseRepository<SignUser, Long> signRepository) {
        super.setBaseRepository(signRepository);
    }
    
	@Autowired
    JedisService jedisService;
	
	@Autowired
	SignUserDAO signUserDao;
	
	@Override
	public List<SignUser> findAllSignUser(){
		return signUserDao.findAllSignUser();
	}
	@Override
	public List<SignUser> findSignUserByuid(String uid){
		return signUserDao.findSignUserByuid(uid);
	}

}
