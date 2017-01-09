package com.busap.vcs.data.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.ConsumeRecord;

@Resource(name = "consumeRecordRepository")
public interface ConsumeRecordRepository extends BaseRepository<ConsumeRecord, Long>{
	
	//@Query(nativeQuery=true,value="select reciever as uid,sum(points) as total from consume_record where create_at >?1 group by reciever order by total desc")
	@Query(nativeQuery=true,value="select cr.reciever as uid,sum(cr.points) as total from consume_record cr,ruser r  where cr.reciever=r.id and r.stat!=2 and r.recommend_bit=0 and  cr.create_at >?1 group by cr.reciever order by total desc")
	public List<Object[]> getTopPointUserInfo(Date date);

	@Query(nativeQuery=true,value="select cr.reciever as uid,sum(cr.points) as total from consume_record cr force index(consume_create_at),ruser r  where cr.reciever=r.id and r.stat!=2 and r.recommend_bit=0 and  cr.create_at >?1 group by cr.reciever order by total desc limit ?2 , ?3 ")
	public List<Object[]> findAnchorRankingList(Date date,Integer start,Integer count);

	@Query(nativeQuery=true,value="select cr.sender as uid,sum(cr.diamond_count) as total from consume_record cr force index(consume_create_at),ruser r  where cr.sender=r.id and r.stat!=2 and r.recommend_bit=0 and  cr.create_at >?1 group by cr.sender order by total desc limit ?2, ?3 ")
	public List<Object[]> findRichRankingList(Date date,Integer start,Integer count);
	
	@Query(nativeQuery=true,value="select a.creator_id as uid,a.total_point_count as total from anchor a,ruser r  where a.creator_id=r.id and  r.stat!=2 and r.recommend_bit=0 and a.create_at >?1  order by total desc limit ?2 ,?3 ")
	public List<Object[]> findAnchorAllRankingList(Date date,Integer start,Integer count);
	
	
	
		
}
