package com.busap.vcs.data.repository;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.busap.vcs.data.entity.Ruser;

@Resource(name = "ruserRepository")
public interface RuserRepository extends BaseRepository<Ruser, Long> {
	Ruser findByUsername(String username);
	
	Ruser findByBandPhone(String bandPhone);

	Ruser findByWechatUnionid(String wechatUnionid);
	
	List findByType(String type);

	@Query("SELECT user.id FROM Ruser user WHERE  user.name like %?1% ")
	List<Long> findByNameLike(String username);

	@Query("SELECT user.id FROM Ruser user WHERE  user.phone like %?1% or user.bandPhone  like %?1%  ")
	List<Long> findByPhoneLike(String phone);

	@Query("select a.id from Ruser a where a.vipStat=?1")
	List<Long> findIdsByVipStat(int vipstate);
	
	@Query("select count(a.name) from Ruser a where a.name=?1 and id!=?2")
	public int countByName(String name,Long uid);
	
	@Query("select count(a.name) from Ruser a where a.name=?1")
	public int countByName(String name);
	
	@Query("select count(a.thirdUserame) from Ruser a where a.thirdUserame=?1 and a.thirdFrom=?2")
	public int countByThirdName(String thirdUsername,String thirdFrom);
	
	@Query("select count(a.username) from Ruser a where a.username=?1")
	public int countByUsername(String username);
	
	@Query("select count(a.wechatUnionid) from Ruser a where a.wechatUnionid=?1 and a.thirdFrom=?2")
	public int countByWechatUnionid(String unionid,String thirdFrom);
	
	@Query(value="SELECT user FROM Ruser user WHERE (user.stat = 0 OR user.stat = 1) AND user.id in ?1 ")
	public List<Ruser> findRUsersByIds(Collection<Long> ids);
	
	@Query(value="SELECT user FROM Ruser user WHERE user.vipStat > 0 AND  user.stat < 2 order by vipWeight desc")
	public List<Ruser> findVipUser();
	
//	@Query(value="SELECT user FROM Ruser user WHERE  user.vipStat > 0 AND  user.stat < 2 AND user.id IN ?2 order by vipWeight desc limit ?1")
	@Query(nativeQuery=true,value="select r.* from ruser r where r.vstat>0 and r.stat<2 and r.id not in ?2 order by vip_weight desc limit ?1")
	public List<Ruser> findRecommondVipUser(int count,Collection ids);

	
	@Query(nativeQuery=true,value="select r.* from ruser r where r.vstat>0 and r.stat<2 order by vip_weight desc limit ?1")
	public List<Ruser> findRecommondVipUser(int count);
	
	@Modifying
	@Transactional
	@Query("update Ruser set videoCount = ?1 where id=?2")
	public void updateUserVideoCount(int count,Long uid);
	
	@Modifying
	@Query("update Ruser set loginDate = now() where username=?1")
	public void updateLoginDate(String username);
	
	@Modifying
	@Query("update Ruser set wechatUnionid = ?2 where username=?1")
	public void insertWechatUnionid(String username,String unionid);
	
	@Query("select count(*) from Ruser a where a.type='normal'")
	public long countAll();
	
	@Query("select a.id from Ruser a")
	List<Long> findAllIds();
	

	@Modifying
	@Query(nativeQuery=true,value="update ruser r set r.allow_evaluation=?1 where r.id=?2")
	public void allowEvaluation(int isAllow,Long uid);
	
	@Query(nativeQuery=true,value="select name from ruser_channel")
	public List<String> selectAllRegPlatform();
	
	@Modifying
	@Query("update Ruser set berryCount = berryCount+1 where id=?1")
	public void updateBerryCount(Long uid);
	
	@Modifying
	@Query("update Ruser set diamondCount = diamondCount+?2 where id=?1")
	public void updateDiamond(Long uid,Integer count);
	
	@Modifying
	@Query("update Ruser set diamondCount = diamondCount-?2 where id=?1 and diamondCount >= ?2")
	public int reduceDiamond(Long uid,Integer count);
	
	@Query("select count(r.id) from Ruser r where r.id=?1 and r.ifa is not null")
	public Long findCountByUserIdIfaNotNull(Long userId);

}
