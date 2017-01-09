package com.busap.vcs.data.repository;


import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.busap.vcs.data.entity.ActivityVideo;
import com.busap.vcs.data.entity.Video;


/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = "videoRepository")
public interface VideoRepository extends BaseRepository<Video, Long> {
	
	@Modifying
//	@Query("update Video a set a.flowStat=?1,a.width=?3,a.height=?4,a.duration=?5,a.url=?6 where a.playKey=?2")
	@Query("update Video a set a.width=?2,a.height=?3,a.duration=?4,a.url=?5 where a.playKey=?1")
	public int updateVideo(String playKey,Integer w,Integer h,String duration,String url);
	
	@Modifying
	@Query("update Video a set a.duration=?2 where a.id=?1")
	public int updateVideoDuration(Long id,String duration);
	
	@Modifying
	@Query("update Video a set a.praiseCount=a.praiseCount+1 where a.id=?1")
	public int incPraiseCount(Long videoId);
	
	//通过playKey获取视频

	@Query("select v from Video v where (v.flowStat='published' or (v.flowStat!='delete' and v.flowStat!='check_fail')) and v.playKey=?1")
	public Video findByPlayKey(String playKey);
	
	@Modifying
	@Query("update Video a set a.favoriteCount=a.favoriteCount+1 where a.id=?1")
	public int incFavoriteCount(Long videoId);
	
	@Modifying
	@Query("update Video a set a.evaluationCount=a.evaluationCount+1 where a.id=?1")
	public int incEvaluationCount(Long videoId);
	
	@Modifying
	@Query("update Video a set a.praiseCount=a.praiseCount-1 where a.id=?1")
	public int decPraiseCount(Long videoId);
	
	@Modifying
	@Query("update Video a set a.favoriteCount=a.favoriteCount-1 where a.id=?1")
	public int decFavoriteCount(Long videoId);
	
	@Query("select COUNT(*) from Evaluation e where e.videoId=?1")
	public int decEvaluationCount(Long videoId);
	
	@Modifying
	@Query("update Video a set a.playCount=a.playCount+?1,a.playCountToday=a.playCountToday+?1 where a.id=?2")
	public int incPlayCount(int c,Long videoId);
	
	@Modifying
	@Query("update Ruser a set a.videoCount=a.videoCount+?1 where a.id=?2 and a.videoCount+?1>0")
	public int updateUserVideoCount(int c,Long uid); 
	
	@Modifying
	@Query(nativeQuery=true,value="update ruser set video_count=video_count+1 where id=?1")
	public int incUserVideoCount(Long uid);
	@Modifying
	@Query(nativeQuery=true,value="update ruser set video_count=video_count-?1 where id=?2")
	public int decUserVideoCount(int c,Long uid);

	@Modifying
	@Query("update Video a set a.flowStat=?1,a.auditorId=?2,a.auditDateTime=now() where a.id in ?3")
	public void updateVideoStatusByIds(String vs,Long auditorId,Collection ids);
	
	@Modifying
	@Query("update Video a set a.publishTime=?1 where a.id in ?2")
	public void updateVideoPublishTimeByIds(Date publishTime,Collection ids);
	
	@Modifying
	@Query("update Video a set a.playCount=?2 where a.id = ?1")
	public void updatePlayCount(Long videoId,Integer playCount);
	
	@Modifying
	@Query("update Video a set a.playRateToday=?2 , a.playRateState =1  where a.id = ?1")
	public void updatePlayRateToday(Long videoId,BigDecimal playRateToday);
	
	@Modifying
	@Query("update Video a set  a.playRateState =?2  where a.id = ?1")
	public void updatePlayRateState(Long videoId,Integer playRateState);
	
	@Modifying
	@Query("update Video a set a.isLogo=?2 where a.id = ?1")
	public void updateLogoState(Long videoId,String state);
	
	@Modifying
	@Query(value="update Video v set v.flowStat='delete' where v.flowStat!='delete' and v.id in ?1")
	public int updateDelVideos(Collection<Long> ids);

	@Query(name="findVideo")
	public List findVideo(Long id);
	
	@Modifying
	@Query("update Video a set a.failReason=?1,a.flowStat=?3,a.auditorId=?4,a.auditDateTime=now() where a.id in ?2")
	public void updateFailReason(String failReason,List<Long> id,String flowStat,Long auditorId);
	
	@Modifying
	@Query("update Video a set a.failReason=?1,a.flowStat=?3 where a.id=?2")
	public void updateFailReason(String failReason,Long id,String flowStat);
	
	
	@Query(nativeQuery=true,value="select c.id,c.name,c.create_at,c.width,c.height,c.play_key,c.url,c.video_pic from subject_video_relation a join subject b on a.subject_id=b.id join video c on a.video_id=c.id where c.flow_stat=?4 and b.name like ?1% order by c.id desc  limit ?2,?3")
	public List listpage(String subjectName,int start,int pagesize,String status);
	
	@Query(nativeQuery=true,value="select count(*) as cnt from subject_video_relation a join subject b on a.subject_id=b.id join video c on a.video_id=c.id where c.flow_stat=?2 and b.name like ?1%")
	public int listpagecount(String subjectName,String status);
	
	@Query("select o from Video o ")
	public List fiubdtest(Pageable page);
	/**
	 * 根据专题id查找
	 * @param start
	 * @param end
	 * @param subjectId
	 * @param status
	 * @param sortField
	 * @param direction
	 * @return
	 */
	@Query("select v from Video v where v.flowStat=?2")
	public List<Video> videoListPage(Long subjectId,String status,Pageable p);
	
	@Query("select count(v.id) from Video v  where v.flowStat=?2")
	public long videoListPageCount(Long subjectId,String status);
	
	@Query("select o from Video o where o.flowStat='planPublish' and o.planPublishTime<=now()")
	public List<Video> findPlanPublished();
	
	//轮询Video表 5分钟一次  查看上传超过5分钟的视频，并发短信告知审核。
//	@Query("select count(*) from Video o where o.flowStat = 'uncheck' and TIMESTAMPDIFF(MINUTE,o.publishTime,now()) >= 5")
//	public int Polling();
	
	//查询有5分钟还未审核的视频需要告知的手机号
	@Query("select s.phoneNo from Sms s")
	public List<String> findPhones();
	
	@Modifying
	@Transactional
	@Query("update Video a set a.flowStat='published',a.publishTime=a.planPublishTime where a.id=?1")
	public void autoPublish(Long id);
	
	@Modifying
	@Query("delete from Video a where a.creatorId=?1 and a.id in(?2)")
	public void deleteVideos(Long creatorId,List<Long> ids);
	
	@Modifying
	@Query(nativeQuery=true,value="delete from praise where video_id in ?1")
	public void deletePraises(Collection ids);
	
	@Modifying
	@Query(nativeQuery=true,value="delete from favorite where video_id in ?1")
	public void deleteFavarites(Collection ids);
	
	
	@Query(value="select o from Video o where (o.flowStat=?1 and o.dataFrom=?2) or o.flowStat=?3 order by o.id desc")
	public Page allvalidlistpage(String flowStat,String dataFrom,String flowStat2,Pageable pageable);
	
	@Query(value="select o from Video o where ((o.flowStat=?1 and o.dataFrom=?2) or o.flowStat=?3) and o.name like ?4% order by o.id desc")
	public Page allvalidlistByNameLikepage(String flowStat,String dataFrom,String flowStat2,String name,Pageable pageable);
	
	@Query(value="select o from Video o where ((o.flowStat=?1 and o.dataFrom=?2) or o.flowStat=?3) and o.tag like %?4% order by o.id desc")
	public Page allvalidlistByTagLikepage(String flowStat,String dataFrom,String flowStat2,String tag,Pageable pageable);
	
	@Query(value="select o from Video o , ActivityVideo a " +
			"where o.id = a.videoid and ((o.flowStat=?1 and o.dataFrom=?2) or o.flowStat=?3) and a.activityid = ?4 order by a.orderNum desc")
	public Page allvalidlistByActivityLikepage(String flowStat,String dataFrom,String flowStat2,Long activity,Pageable pageable);
	
	@Query(value="select a from ActivityVideo a where a.activityid=?1")
	public List<ActivityVideo> allvalidlistByActivityTemp(Long activity);
	
	@Query(nativeQuery=true,value="select a.* from video a ,activity_video b where a.id=b.videoid and ((a.flow_stat!='delete' and a.flow_stat!='check_fail') or a.flow_stat='published') and b.activityid=?1 ORDER BY a.introduction_mark DESC,a.id DESC limit ?2,?3")
	public List<Video> getActivityVideo(Long actid,Long start,Integer length);
	
	@Query(nativeQuery=true,value="select count(a.id) from video a ,activity_video b where a.id=b.videoid and a.flow_stat!='delete'  and a.flow_stat!='check_fail' and b.activityid=?1")
	public int countActivityVideo(Long actid);
	

	//(a.flow_stat!='delete' and a.flow_stat!='check_fail' and a.data_from='myvideo_restwww')
	@Query(nativeQuery=true,value="select a.* from video a where (a.tag like %?1% or a.description like %?1%) and  (a.flow_stat='published' or a.flow_stat='check_ok') ORDER BY a.create_at DESC limit ?2,?3")
	public List<Video> searchVideo(String keyword,Long start,Integer length);
	
	@Query(nativeQuery=true,value="SELECT v.* FROM video v,label_video lv WHERE lv.label_name = ?1 and v.id = lv.video_id and (v.flow_stat = 'published' OR v.flow_stat = 'check_ok') and lv.video_id > ?2 ORDER BY lv.video_id ASC limit ?3")
	public List<Video> searchPrevPageVideoByTag(String tag,long firstId,int pageSize);
	
	@Query(nativeQuery=true,value="SELECT v.* FROM video v,label_video lv WHERE lv.label_name = ?1 and v.id = lv.video_id and (v.flow_stat = 'published' OR v.flow_stat = 'check_ok') ORDER BY lv.video_id DESC limit ?2")
	public List<Video> searchFirstPageVideoByTag(String tag,int pageSize);
	
	@Query(nativeQuery=true,value="SELECT v.* FROM video v,label_video lv WHERE lv.label_name = ?1 and v.id = lv.video_id and (v.flow_stat = 'published' OR v.flow_stat = 'check_ok') and lv.video_id < ?2 ORDER BY lv.video_id DESC limit ?3")
	public List<Video> searchNextPageVideoByTag(String tag,long lastId,int pageSize);
	
	@Query(value="SELECT v FROM Video v WHERE (v.flowStat = 'published' OR v.flowStat = 'check_ok') AND v.id in ?1 ORDER BY v.id DESC")
	public List<Video> findVideosByIds(Collection<Long> ids);
	
	//and a.flow_stat!='delete' and a.flow_stat!='check_fail'
	@Query(nativeQuery=true,value="select count(a.id) from video a where (a.tag like %?1% or a.description like %?1%) and (a.flow_stat='published' or a.flow_stat='check_ok')   ")
	public int countSearchVideo(String keyword);
	
	
	@Modifying
	@Query("update Video a set a.isRecommend=1 where a.id=?1")
	public int recommendVideo(Long videoId);
	
	@Modifying
	@Query("update Video a set a.hotPoint=(a.praiseCount*?2+a.evaluationCount*?3+a.playCount*?4) where a.id=?1")
	public int updateHotPointByVideoId(Long videoId,int praiseCountWeight,int evaluationCountWeight,int playCountWeight);
	
	//查询某人的视频 
	@Query(value="select v from Video v where (v.flowStat='published' or (v.flowStat!='delete' and v.flowStat!='check_fail')) and v.creatorId=?1 ORDER BY v.createDate DESC")
	public Page<Video> findUserVideos(Long uid,Pageable pageable); 
	
	/**
	 * 查询其他人的视频列表(仅包含发布或者审核通过的视频)
	 * @param uid
	 * @param pageable
	 * @author wangyongfei
	 * @return
	 */
	@Query(value="select v from Video v where (v.flowStat='published' or v.flowStat='check_ok') and v.creatorId=?1 ORDER BY v.createDate DESC")
	public Page<Video> findOtherUserVideos(Long uid,Pageable pageable); 
	
	/**
	 * 查询自己的视频列表(包含发布的及审核和未审核的视频)
	 * @param uid
	 * @param pageable
	 * @author wangyongfei
	 * @return
	 */
	@Query(value="select v from Video v where (v.flowStat='published' or v.flowStat='uncheck' or v.flowStat='check_ok') and v.creatorId=?1 ORDER BY v.createDate DESC")
	public Page<Video> findMyVideos(Long uid,Pageable pageable); 
	
	//查询最新视频历史
	@Query(nativeQuery=true,value="select v.* from video v where (v.flow_stat='published' or (v.flow_stat!='delete' and v.flow_stat!='check_fail')) and v.id<?1  ORDER BY v.id DESC limit 0,?2")
	public List<Video> findNewVideosHistory(Long mId, Integer length);
	//查询最新视频最新
	@Query(nativeQuery=true,value="select v.* from video v where (v.flow_stat='published' or (v.flow_stat!='delete' and v.flow_stat!='check_fail')) ORDER BY v.id DESC limit 0,?1")
	public List<Video> findNewVideosFirst(Integer length);
	
	
	
	//查询没有分类的视频历史
	@Query(nativeQuery=true,value="select v.* from video v where (v.flow_stat='published' or v.flow_stat ='check_ok') and v.id<?1  ORDER BY v.id DESC limit 0,?2")
	public List<Video> findVideosWithouActivityHistory(Long mId, Integer length);
	//查询么有分类的视频最新
	@Query(nativeQuery=true,value="select v.* from video v where (v.flow_stat='published' or v.flow_stat ='check_ok') ORDER BY v.id DESC limit 0,?1")
	public List<Video> findVideosWithouActivityFirst(Integer length);
	
	
	
	//查询关注人最新视频最新
	@Query(value="select v from Video v where (v.flowStat='published' or (v.flowStat!='delete' and v.flowStat!='check_fail')) and v.creatorId in ?1 ")
	public Page<Video> findAttentionNewVideos(Collection ids,Pageable pageable);
	
	//根据热度指数查询视频
	@Query(nativeQuery=true,value="select v.* from video v where (v.flow_stat='published' or v.flow_stat='check_ok') ORDER BY v.publish_time DESC limit ?1,?2")
	public List<Video> findHotIndiceVideos(Integer startIndex,Integer count);
	
	//根据热度指数查询活动视频
	@Query(nativeQuery=true,value="select v.* from video v,activity_video a where a.activityid=?3 and v.id=a.videoid and (v.flow_stat='published' or v.flow_stat='check_ok') ORDER BY a.order_num DESC,v.hot_point DESC,v.publish_time DESC limit ?1,?2")
	public List<Video> findHotIndiceActivityVideos(Integer startIndex,Integer count,Long activityId);
	
	//查询最热视频 
	//(v.flowStat!='delete' and v.flowStat!='check_fail' and v.dataFrom='myvideo_restwww')
	/**
	 * 查询最热视频(仅包含发布或者审核通过的视频)
	 * @param pageable
	 * @return
	 */
	@Query(value="select v from Video v where (v.flowStat='published' or  v.flowStat='check_ok') ")
	public Page<Video> findHotVideos(Pageable pageable);
	
	@Query(nativeQuery=true,value="select v.* from video v where (v.flow_stat='published' or  v.flow_stat='check_ok')  and v.publish_time<?1 ORDER BY v.play_rate_today DESC limit 0,?2")
	public List<Video> findHotVideos(Date timestamp,Integer count);
	
	//查询单个视频 
	@Query(value="select v from Video v where (v.flowStat='published' or (v.flowStat!='delete')) and v.id=?1")
	public Video getOneVideo(Long id);
	
	//查询最热视频前几条  
	@Query(nativeQuery=true,value="select v.* from video v where (v.flow_stat='published' or (v.flow_stat!='delete' and v.flow_stat!='check_fail')) ORDER BY v.play_rate_today DESC limit 0,?1")
	public List<Video> findTopHotVideos(Integer length);
	
	//统计管理员马甲每个人发了多少个视频
	@Query(nativeQuery=true,value="SELECT COUNT(*) FROM video where flow_stat = 'published' AND creator_id = ?1")
	public int countAdminVideoForRuser(Long id);
	
	//统计用户每个人发了多少个视频
	@Query(nativeQuery=true,value="SELECT COUNT(*) FROM video where flow_stat = 'check_ok' AND creator_id = ?1")
	public int countUserVideoForRuser(Long id);
	
	@Query("select v from Activity a  left join a.video v  where  a.id=?1 ")
	public List<Video> findIntroductionVideosByActivityId(Long activityId);
	
	//查询用户所有的视频id
	@Query(nativeQuery=true,value="select id from video where (flow_stat = 'published' or flow_stat = 'check_ok') and creator_id=?1")
	public List<Long> getVideoIdListByUid(Long uid);
	//查询当前用户最新的一个视频
	@Query(nativeQuery=true,value="select v.* from video v where (flow_stat = 'published' or flow_stat = 'check_ok') and creator_id=?1 ORDER BY v.id DESC limit 0,?2")
	public List<Video> getNewVideoByUid(Long uid,int limitnum);
//	@Modifying
//	@Query("update Video v set v.introductionMark='0' from  v.activity a   where   a.id=?1 and v.introductionMark='1'   ")
//	public long updateIntroductionVideosByActivityId(Long activityId);
	/**
	 * 查询一组视频的创建者id
	 * @param vids
	 * @return
	 */
	@Query("select v.creatorId from Video v where v.id in ?1")
	public List findCreatorIds(List<Long> vids);
	
	//统计用户发布视频数量
	@Query(nativeQuery=true,value="SELECT COUNT(*) FROM video where (flow_stat = 'check_ok' or flow_stat = 'published' or flow_stat = 'uncheck')  AND creator_id = ?1")
	public int findUserVideoCount(Long id);
	
	@Query(nativeQuery=true,value="SELECT v.* FROM video v WHERE v.live_notice_id=?1 and type=2 and (v.flow_stat = 'published' OR v.flow_stat = 'check_ok')")
	public List<Video> getPlaybackByRoomId(Long roomId);
	
	@Query(nativeQuery=true,value="SELECT v.* FROM video v WHERE v.live_notice_id=?1 and type=2 and v.flow_stat = 'delete'")
	public List<Video> getDeletePlaybackByRoomId(Long roomId);
	
	@Query(nativeQuery=true,value="SELECT v.* FROM video v WHERE v.live_notice_id=?1 and type=4 and (v.flow_stat = 'published' OR v.flow_stat = 'check_ok')")
	public List<Video> getVideoByLiveNoticeId(Long liveNoticeId);
	
}
