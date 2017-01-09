package com.busap.vcs.data.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Forward;
import org.apache.ibatis.annotations.Param;

public interface ForwardDAO {

	/**
	 * 判断用户是否关注过某视频
	 * @param params
	 * @return
	 */
	public Integer isForward(Map<String,Object> params);
	
	/**
	 * 根据用户id查询转发的视频id
	 * @param uid
	 * @return
	 */
	public List<Long> getForwardVideoIds(Long uid);
	
	/**
	 * 根据用户id查询转发信息
	 * @param uid
	 * @return
	 */
	public List<Forward> getForwardList(Long uid);
	
	/**
	 * 根据用户id集合查询转发信息
	 * @param ids
	 * @return
	 */
	public List<Forward> getForwardListByIds(Collection ids);
	
	/**
	 * 查询用户是否转发过
	 * @param uid
	 * @return
	 */
	public int hasForwarded(Long uid);
	
	/**
	 * 取消转发某视频
	 * @param params
	 * @return
	 */
	public int cancelForward(Map<String,Object> params);

	/**
	 * 根据ID删除转发
	 * @param idList forwardID
	 */
	public void deleteForward(List<Long> idList);
	/**
	 * 查询视频的转发次数
	 * @param vid
	 * @return
	 */
	public Integer findVideoForwardCount(Long vid);
}
