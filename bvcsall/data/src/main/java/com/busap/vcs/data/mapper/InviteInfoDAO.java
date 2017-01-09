package com.busap.vcs.data.mapper;

import java.util.List;


import com.busap.vcs.data.vo.InviteInfoVO;

public interface InviteInfoDAO {

	/**
	 * 查找所有的邀请信息
	 * @param params
	 * @return
	 */
	public List<InviteInfoVO> findAllInviteInfo();
	
	/**
	 * 通过平台标示查找邀请信息
	 * @param platformMark
	 * @return
	 */
	public List<InviteInfoVO> findInviteInfoByPlatformMark(String platformMark);
	
	/**
	 * 查找所有可用邀请信息
	 * @return
	 */
	public List<InviteInfoVO> findAvaliblelInviteInfo();
	
}
