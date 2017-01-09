package com.busap.vcs.service;

import com.busap.vcs.data.entity.SchoolRegister;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SchoolRegisterService extends BaseService<SchoolRegister, Long> {
	
	public SchoolRegister getRegisterInfo(String phone);
	public SchoolRegister getRegisterInfoByUid(Long uid);

	public Integer getRegisterByNameTotal(String name);

	public List<SchoolRegister> getRegisterByName(String name, int page, int size);
	
	public List<SchoolRegister> getRegisterByInviteCode(String inviteCode, int page, int size,Date startTime,Date endTime);
	
	public Integer getRegisterCodeByInviteCode(String inviteCode,Date startTime,Date endTime);

	public List<SchoolRegister> getRegisterByPage(int page, int size);
	
	public boolean inviteCodeExist(String code);
	
	public List<Map<String,Object>> getInviteInfo(int type);
}
