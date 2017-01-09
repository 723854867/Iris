package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.LoadConfigUrl;
import com.busap.vcs.data.entity.Sign;
import com.busap.vcs.data.entity.SignUser;
import com.busap.vcs.data.vo.LoadConfigUrlVO;
import com.busap.vcs.data.vo.SignVO;

public interface SignUserService extends BaseService<SignUser,Long> {
	List<SignUser> findAllSignUser();
	List<SignUser> findSignUserByuid(String uid);
}
