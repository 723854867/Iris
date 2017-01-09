package com.busap.vcs.service;

import com.busap.vcs.data.entity.Forward;

import java.util.List;

public interface ForwardService extends BaseService<Forward, Long> {
	//查询用户是否转发过某视频
	public boolean isForward(Long videoId,Long uid);
	
	//查询用户是否转发过
	public boolean hasForwarded(Long uid);
	
	//查询用户转发视频个数
	public int getForwardNumber(Long uid);
	
	//取消转发
	public void cancelForward(Long videoId,Long uid);

	/**
	 * 删除转发信息
	 * @param ids forwardId
	 */
	public void deleteForwardByIds(List<Long> ids);
}