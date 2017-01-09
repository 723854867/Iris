package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.InviteInfo;
import com.busap.vcs.data.vo.InviteInfoVO;

public interface InviteInfoService extends BaseService<InviteInfo,Long>{

	/**
	 * load All InviteInfo
	 */
	List<InviteInfoVO> findAllInviteInfo();
	
	List<InviteInfoVO> findInviteInfoByPlatformMark(String platformMark);
	List<InviteInfoVO> findAvaliblelInviteInfo();
}
