package com.busap.vcs.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.SchoolRegister;
import com.busap.vcs.data.mapper.SchoolRegisterDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.SchoolRegisterRepository;
import com.busap.vcs.service.SchoolRegisterService;

@Service("schoolRegisterService")
public class SchoolRegisterServiceImpl extends BaseServiceImpl<SchoolRegister, Long> implements
SchoolRegisterService {
	
	private Logger logger = LoggerFactory.getLogger(SchoolRegisterServiceImpl.class);
	
	@Resource(name="schoolRegisterRepository")
	private SchoolRegisterRepository schoolRegisterRepository;
	
	@Autowired
	private SchoolRegisterDAO schoolRegisterDAO;
	
	@Resource(name="schoolRegisterRepository")
	@Override
	public void setBaseRepository(BaseRepository<SchoolRegister, Long> baseRepository) {
		super.setBaseRepository(schoolRegisterRepository);
	}

	@Override
	public SchoolRegister getRegisterInfo(String phone) {
		return schoolRegisterRepository.getRegisterInfo(phone);
	}

	@Override
	public Integer getRegisterByNameTotal(String name) {
		return schoolRegisterRepository.getRegisterByNameTotal(name);
	}

	@Override
	public List<SchoolRegister> getRegisterByName(String name, int page, int size) {
		return schoolRegisterRepository.getRegisterByName(name, (page - 1) * size, size);
	}

	@Override
	public List<SchoolRegister> getRegisterByPage(int page, int size) {
		return schoolRegisterRepository.getRegisterByPage((page - 1) * size, size);
	}

	@Override
	public SchoolRegister getRegisterInfoByUid(Long uid) {
		return schoolRegisterRepository.getRegisterInfoByUid(uid);
	}

	@Override
	public boolean inviteCodeExist(String code) {
		int count = schoolRegisterRepository.getInviteCodeCount(code);
		if (count>0){
			return true;
		}
		return false;
	}

	@Override
	public List<SchoolRegister> getRegisterByInviteCode(String inviteCode,
			int page, int size,Date startTime,Date endTime) {
		return schoolRegisterRepository.getRegisterByInviteCode(inviteCode, (page - 1) * size, size,startTime,endTime);
	}
	
	@Override
	public Integer getRegisterCodeByInviteCode(String inviteCode,Date startTime,Date endTime) {
		return schoolRegisterRepository.getRegisterCountByInviteCode(inviteCode,startTime,endTime);
	}

	@Override
	public List<Map<String, Object>> getInviteInfo(int type) {
		if (type == 1){
			return schoolRegisterDAO.getInviteInfo1();
		} else {
			return schoolRegisterDAO.getInviteInfo2();
		}
	}

}
