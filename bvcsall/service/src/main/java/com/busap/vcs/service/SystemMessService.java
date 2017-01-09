package com.busap.vcs.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.busap.vcs.data.entity.SystemMess;
import com.busap.vcs.data.vo.SysmessVO;

public interface SystemMessService extends BaseService<SystemMess, Long>{

	/**
	 * 保存系统消息
	 * @param sm
	 */
	public void saveSysmess(SystemMess sm);
	
	/**
	 * 删除系统消息
	 * @param id
	 */
	public void deleteSysmess(Long id); 
	
	/**
	 * 修改系统消息 
	 * @param sm
	 */
	public void updateSysmess(SystemMess sm);
	
	/**
	 * 查询系统消息，dmsong add 20150505
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public Page searchSysmess(Integer pageNo,Integer pageSize,Map<String,Object> params);
	
	/**
	 * 向消息中间件发送消息
	 * @param sm
	 */
	public void sendMessage(SystemMess sm);
	
	/**
	 * 获取计划发布的系统消息
	 * @return
	 */
	public List<SystemMess> searchPlan();
	
	public List<SysmessVO> searchAvailableSysmessByUid(Long uid,Date timestamp,int count);
}
