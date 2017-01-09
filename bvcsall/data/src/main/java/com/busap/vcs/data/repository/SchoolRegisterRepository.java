package com.busap.vcs.data.repository;


import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.SchoolRegister;

import java.util.Date;
import java.util.List;


@Resource(name = "schoolRegisterRepository")
public interface SchoolRegisterRepository extends BaseRepository<SchoolRegister, Long> {
	@Query(nativeQuery=true,value="select s.* from school_register s where s.phone=?1")
	public SchoolRegister getRegisterInfo(String phone);
	
	@Query(nativeQuery=true,value="select s.* from school_register s where s.creator_id=?1")
	public SchoolRegister getRegisterInfoByUid(Long uid);

	@Query(nativeQuery=true,value="select s.* from school_register s where s.name like ?1% limit ?2,?3")
	public List<SchoolRegister> getRegisterByName(String name, int start, int size);
	
	@Query(nativeQuery=true,value="select s.* from school_register s,ruser r where s.creator_id=r.id and s.invite_code=?1 and r.create_at >=?4 and r.create_at<=?5 limit ?2,?3")
	public List<SchoolRegister> getRegisterByInviteCode(String inviteCode, int start, int size,Date startTime,Date endTime);
	
	@Query(nativeQuery=true,value="select count(*) from school_register s,ruser r where s.creator_id=r.id and s.invite_code=?1 and r.create_at >=?2 and r.create_at<=?3")
	public Integer getRegisterCountByInviteCode(String inviteCode,Date startTime,Date endTime);

	@Query(nativeQuery=true,value="select count(id) from school_register s where s.name like ?1% ")
	public int getRegisterByNameTotal(String name);

	@Query(nativeQuery=true,value="select s.* from school_register s limit ?1,?2")
	public List<SchoolRegister> getRegisterByPage(int page, int size);
	
	@Query(nativeQuery=true,value="select count(*) from school_code where code=?1")
	public int getInviteCodeCount(String inviteCode);

}
