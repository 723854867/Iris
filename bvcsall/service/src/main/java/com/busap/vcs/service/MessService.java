package com.busap.vcs.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.busap.vcs.data.entity.SystemMess;
import com.busap.vcs.data.vo.MessVO;
import com.busap.vcs.data.vo.SysmessVO;

public interface MessService extends BaseService<SystemMess, Long>{
	public List<MessVO>  findMess(Integer page, Integer size, String creator,Integer type);
}
