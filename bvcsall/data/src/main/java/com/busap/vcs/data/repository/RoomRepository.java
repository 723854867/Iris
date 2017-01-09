package com.busap.vcs.data.repository;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.Room;

@Resource(name = "roomRepository")
public interface RoomRepository extends BaseRepository<Room, Long> {
	public List<Room> findByCreatorIdAndStatus(Long creatorId,int status);
	
	@Query(nativeQuery=true,value="select r.* from room r where r.status=?1 order by r.max_access_number DESC limit ?2,?3")
	public List<Room> getRoomByStatus(int status,int start,int length);
	
	@Query(nativeQuery=true,value="select  now()  from   dual")
	public Date getDbTime();

	@Query(nativeQuery = true,value = "select * from room where status = 1 and creator_id = ?1 order by id desc limit 1")
	Room selectLivingRoomByUserId(Long userId);

	@Query(nativeQuery=true,value="select count(*) from room where status=?1")
	int getRoomCountByStatus(int status);
	
	@Query(nativeQuery=true,value="select count(*) from room where creator_id=?1")
	public Integer getLiveTimes(Long userId);

/*	@Query(nativeQuery=true,value="select count(distinct creator_id) from room where create_at > ?1 and create_at < ?2")
	int queryDailyLiveNum(String start, String end);*/

	@Query(nativeQuery=true,value="select max(id) as roomId from room where status = 1")
	Long queryMaxRoomId();

/*	@Query(nativeQuery=true,value="SELECT COUNT(a.id) FROM anchor a LEFT JOIN ruser r ON r.id = a.creator_id WHERE a.create_at > ?1 and a.create_at < ?2 AND r.create_at > ?1 and r.create_at < ?2")
	Long queryNewRegUserLiveCount(String start, String end);

	@Query(nativeQuery=true,value="SELECT COUNT(a.id) FROM anchor a WHERE a.create_at > ?1 and a.create_at < ?2")
	Long queryNewLiveCount(String start, String end);*/
}
