package com.busap.vcs.data.mapper;

import java.util.List;

import com.busap.vcs.data.entity.SignUser;

public interface SignUserDAO {
	List<SignUser> findAllSignUser();
	List<SignUser> findSignUserByuid(String uid);
}
