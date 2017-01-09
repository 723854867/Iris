package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.Praise;

/**
 * Created by
 * User: djyin
 * Date: 12/5/13
 * Time: 11:52 AM
 */
public interface PraiseService extends BaseService<Praise, Long> { 
	
	public Praise getPraise(Long videoId,Long uid); 
	
	public void savePraise(Praise f);
	
	public void deletePraise(Long videoId,Long creator);
	/**
	 * 后台批量添加赞，批量试用马甲账号点赞
	 * @param videoId 视频id
	 * @param creatorIds 马甲id列表
	 * @param adminId 后台管理员id
	 */
	public void savePraise(Long videoId,String creatorIds[],Long adminId);
	/**
	 * 查询一个视频马甲账号的赞
	 * @param videoId
	 * @return
	 */
	public List<Praise> searchByMajia(Long videoId);

	public void deletePraiseByIds(List<Long> ids);
	
	public void autoPraise();

}
