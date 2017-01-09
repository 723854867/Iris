package com.busap.vcs.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.busap.vcs.data.entity.*;
import com.busap.vcs.data.model.ExportWopaiNormalUser;
import com.busap.vcs.data.model.ExportWopaiUser;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.busap.vcs.base.Filter;
import com.busap.vcs.base.OrderByObject;
import com.busap.vcs.data.enums.VideoStatus;
import com.busap.vcs.data.repository.ActivityVideoRepository;

/**
 * Created by
 * User: djyin
 * Date: 12/5/13
 * Time: 11:52 AM
 */
public interface VideoService extends BaseService<Video, Long> {
	
	public Video getVideo(Long id, String uid);
	
	public Video getVideo(String playKey);
	public Page<Video> getVideos(String ...playKeys);
	
//	public List<String> getVideoPic(Long videoId);
	
	public List<Video> getAllVideo();
	
	public List<Video> getVideos(List<Long> ids); 
	
	public void incCount(Integer c,Long id,String deviceid,Long uid);
	

	public void incCount(Integer c,Long id);
	
	public void updateUserVideoCount(int c,Long uid);
	
	public int updateVideo(String playKey,Integer w,Integer h,String duration,String url);
	
	public Page<Video> findBySubjectId(Integer page, Integer size, String subjectId, String uid);
	
	public Page<Video> findByActivityId(Integer page, Integer size, long actId, String uid);
	public List<Video> findActVideos(Long timestamp,Long actId,String uid,Integer count,Integer page);
	public List<Video> findActVideosAfterAssignedTime(Long timestamp,Long actId,Integer count);
	public List<Video> findVideosByIds(List<Long> ids,String uid);
	
	public List<Video> findVideosByIds(List<Long> ids);
	/**
	 * 根据活动ID查询视频
	 * @param page 当前页码
	 * @param size 每页记录数
	 * @param actId 活动ID
	 * @param direction 排序
	 * @param sortField 排序字段
	 * @param beginTime 视频创建时间 开始
	 * @return
	 */
	public Page<Video> findByActivityId(Integer page, Integer size, Long actId,Direction direction,String sortField,Date beginTime);
	
	public Page<Video> findRecommendVideos(Integer page, Integer size, String uid);
	
	public Page<Video> findMyVideos(Integer page, Integer size, String creator);
	
	public List<Long> findMyVideoIdList(Long uid);
	
	public Page<Video> findNewVideos(Integer page, Integer size);
	
	public List<Video> findNewVideos(Integer start, Integer length, List<Filter> filters, Sort sort,String uid);
	
	public List<Video> findVideosWithouActivity(Long maxId, Integer length);
		
	public Page<Video> findHotVideos(Integer page, Integer size,String uid);
	public List<Video> findHotVideos(Date timestamp, Integer count,String uid);

//    public Page<VideoFullText> findKeywordVides(Integer page, Integer size, String keywords);
    public Page<Video> findKeywordVides(Integer page, Integer size, String keywords,String uid);
    public List<Video> findVideoByTagName(String tag,int dir,int pageSize,long lastId,String uid);
	
	public Page<Video> findOtherVideos(Integer page, Integer size, String uid, String otherUid);
 
	public Page<Video> findCheckedVideos(Integer page, Integer size,String status);
	
	public Page<Video> findCheckedVideos(Pageable pr,List<Filter> filters);
	
	public Page<Video> findHotVideos(Pageable pageable, List<RootInfo> rootList,List<JoinInfo> joinList,List<FetchInfo> fetchList,List<Filter> filters,List<OrderByObject> orderByObjList);

	public void saveVideo(String[] subject,String[] tag,String title,Long uploader,String description,String playId,String dataFrom,long loginId);
	
	public Video saveVideo2activity(String[] activity,Set<String> tag,String title,Long uploader,String description,String playId,String dataFrom,
			String introductionMark,Long templateId,long loginId,String videoPic,String videoListPic,
			Date planTime,String pub,String isLogo,String playRateToday,ShowVideo showVideo);//update by dmsong 20150722 增加我拍秀
	
	public void deleteAll(String ids);
	
	public void deleteVideos(List<Long> ids,Long uid);
	
	public void updateVideoStatusByIds(Long auditorId,Collection ids);
	
	public void updateVideoStatusByIds(Long auditorId,String status,Collection ids);
	
	public void updatePlayCount(Long videoId,Integer playCount);
	
	public void updatePlayRateToday(Long videoId,BigDecimal playRateToday);
	
	public void updatePlayRateState(Long videoId,Integer playRateState);
	
	public void updateLogoState(Long videoId,String state);
	
	public void publishVideos(Long auditorId,Collection ids);
	
	public List<Map<String,String>> updateVideoStatusByIds(Long auditorId,VideoStatus vs,Collection ids) throws SolrServerException, IOException;
	
	public List findVideo(Long id);
	 
	
	public List findActivityIdByVideoId(Long videoId);
	
	public void updateVideoAndCheckOk(Long id,String[] subject,String[] tag,String title,Long uploader,String description);
	
	public void updateVideoAndCheckOk2activity(Long id,String[] activity,String[] tag,String title,Long uploader,String description);
	/**
	 * 批量设置审核不通过，dmsong modify 20150122
	 * 原方法定义：public void updateFailReason(String failReason,Long id);
	 * @param failReason
	 * @param id
	 */
	public void updateFailReason(Long auditorId,String failReason,List<Long> id);
	
	public void updateFailReason(String failReason,Long id);
	/**
	 * 视频恢复
	 * @param ids
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void recoverVideos(Long auditorId,Collection ids) throws SolrServerException, IOException;

	/**
	 * 编辑视频
	 * @param id
	 * @param subjects
	 * @param tags
	 * @param title
	 * @param uploader
	 * @param description
	 */
	public void updateVideo(Long id,String[] subjects,String[] tags,String title,Long uploader,String description);
	
	public void updateVideo2activity(Long id, String[] activitys, Set<String> tags,String title, Long uploader, 
			String description,String introductionMark,Long templateId,String isLogo,String playRateToday,ShowVideo showVideo);
	
	public Video findVideoByPlayKey(String playKey,Long uid);
	
	public Page<Map> listpage(int page,int size,String queryname,String status) ;
	
	public Page listpage(int page,int size,List<Filter> filters,Sort sort);
	
	public String findImgByVideo(Long videoId); 
	
	public List test(Pageable page);
	/**
	 * 依据渠道、专题查询视频列表
	 * @param pageNo
	 * @param size
	 * @param channelId
	 * @param subjectId
	 * @param sortField
	 * @param status
	 * @return
	 */
	public Page<Video> listPage(int pageNo,int size,Long subjectId,String sortField,Direction direction,String status);
	
	public Page allvalidlistpage(String flowStat,String dataFrom,String flowStat2,Pageable pageable);
	
	public Page allvalidlistByNameLikepage(String flowStat,String dataFrom,String flowStat2,String name,Pageable pageable);
	
	public Page allvalidlistByTagLikepage(String flowStat,String dataFrom,String flowStat2,String tag,Pageable pageable);
	
	public Page allvalidlistByActivityLikepage(String flowStat, String dataFrom,String flowStat2, String activity, Pageable pageable);
	
	public Page searchAdminVideo(String startTime,String endTime, String description, String tag,String flow_stat, String isLogo,String activities,String username,Pageable pr);
	
	public int recommendVideo(Long videoId);
	
	public boolean praised(Long creatorId,Long vId);
	public boolean favorited(Long creatorId,Long vId);
	/**
	 * 更新视频标签
	 * @param vid
	 * @param tag
	 * @return
	 */
	public Video updateVideoTag(Long vid,String tag);
	
	public Page<Video> findVideos(int pageNo,int size,List<Filter> filter);
	
	
	public List<Video> findNewVideos(Long maxId, Integer length,String uid);
	public List<Video> findNewVideos(Date timestamp,Integer count,String uid);
	
	public Page<Video> findAttentionNewVideos(Collection ids, Integer page, Integer size);
	public List<Video> findAttentionNewVideos(Long uid,List<Long> ids,Date timestamp,Integer count);
	
	public List<Video> getAttentionInfoList(Long uid,List<Long> ids,Date timestamp,Integer count,Integer type);
	
	public List<Map<String,String>> getAttentionLiveInfoList(List<Long> ids,Integer count,Integer start);
	
	public List<Video> findOtherAttentionNewVideos(Long uid,List<Long> ids,Date timestamp,Integer count);
	
	public List<Video> findTopHotVideos(Integer count);
	/**
	 * 用户视频列表查询，dmsong add 20150121
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public Page searchVideoList(Integer pageNo,Integer pageSize,Map<String,Object> params);
	/**
	 * 根据用户id查询视频列表 ，dmsong add 20150126
	 * @param uid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page findByCreator(Long uid,Integer pageNo,Integer pageSize);
	
	public List<Video> findIntroductionVideosByActivityId(Long activityId);
	
	public long updateIntroductionVideosByActivityId(Long activityId);
	
	//保存手机端上传的视频
	public boolean saveVideoFromPhone(Video video);
	public boolean saveVideoFromPhoneV2(Video video,Set<String> tags);
	
	public int countVideoForRuser(Long id);
	 
	public List<Map<String,Object>> getVideoPraiseUserList(Long vid ,Long pid,Integer count,Long uid);
  
	
	public List<Video> findUserVideos(Long userid ,Date timestamp,Integer count,Long uid);
	
	/**
	 * 用户视频列表查询，dmsong add 20150121
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public Page searchNewVideoList(Integer pageNo,Integer pageSize,Map<String,Object> params);
	
	//根据热度指数（播放指数+点赞指数+评论指数+时间指数）排序，查询视频
	public List<Video> findHotIndiceVideos(Integer startIndex, Integer count,String uid,Long activityId);
	
	//查询当前用户最新的一个视频或多个
	public List<Video> getNewVideoByUid(Long uid,int limitnum);

	public List<Video> findShowVideo(Long videoId,String refVideoId, String uid,Long timestamp,Integer count);

	//视频热度日排行定时计算存入redis前50条
	public void dayHotVideosToRedis();
	//获取视频热度日排行
	public List<Video> findDayHotVideosRank(Long uid,Integer start,Integer count);

	public ActivityVideoRepository getActivityVideoRepository();
	
	/**
	 * 根据视频id更新视频的热度指数
	 * @param id
	 */
	public void updateHotPointByVideoId(Long id);
	
	/**
	 * 补全视频的时长
	 */
	public void completeVideoDuration(int count);
	//计算当日视频热度
	public void excuteVideoDayHotValue(Long vid);

	/**
	 * 获取Video信息列表
	 * @param params
	 * @return Video List
	 */
	List<Video> queryAdministratorVideos(Map<String,Object> params);

	/**
	 * 获取Video信息列表COUNT
	 * @param params
	 * @return Video List COUNT
	 */
	Integer queryAdministratorVideoCount(Map<String,Object> params);
	/**
	 * 	获取视频详情
	 * @param id
	 * @return
	 */
	Video findVideoDetail(Long id);

	/**
	 * 分页查询最热视频
	 * @param page
	 * @param rows
	 * @param params
	 * @return
	 */
	public Page searchHotVideoList(Integer page, Integer rows,Map<String, Object> params);
	//查询当前最大播放率
	public BigDecimal findMaxPlayRate();
	//最热视频排序上移
	public void moveHotVideoUp(Long id);
	//最热视频排序上移
	public void moveHotVideoDown(Long id);
	//批量移除最热视频
	public void removeHotVideos(List<Long> idList);

	List<Video> queryVideosByCreatorId(Long creatorId);

	public List<Video> findRelatedVideos(Long videoId,Integer page, Integer size, String uid);

	void queryHotVideosToRedis(String type);

	List<Video> queryHotVideoRankingList(String type, Long uid, Integer start, Integer count);
	
	public int findUserVideoCount(Long userId);

	Video queryVideoById(Long id);

/*	List<ExportWopaiNormalUser> queryWopaiUserByVideo(Map<String,Object> params);*/

	Video queryVideoByWeight(Map<String,Object> params);

	int updateSort(Map<String,Object> params);

	Map<String,Object> upSort(Long videoId,Video video) throws Throwable;

	Map<String,Object> downSort(Long videoId,Video video) throws Throwable;

	public List<Map<String,Object>> findHotVideosV3(Integer start, Integer end);

	/**
	 * 根据条件查询视频信息
	 * @param params
	 * @return
	 */
	List<Video> select(Map<String,Object> params);
	
	public Video getPlaybackByRoomId(Long roomId);
	
	public Video getDeletePlaybackByRoomId(Long roomId);
	
	public Video getVideoByLiveNoticeId(Long liveNoticeId);

	Video queryVideoByLiveNoticeId(Long liveNoticeId);

	/**
	 * 根据条件查询遗漏的回放信息
	 * @param persistentId
	 * @return
	 */
	MissingPlaybackInfo selectMissPlaybackByPersistentId(String persistentId);

	/**
	 * 更新遗漏的回放信息
	 * @param missingPlaybackInfo
	 * @return
	 */
	int updateMissPlaybackByPersistentId(MissingPlaybackInfo missingPlaybackInfo);

	/**
	 * 添加遗漏的回放信息
	 * @param missingPlaybackInfo
	 * @return
	 */
	int insertMissPlayback(MissingPlaybackInfo missingPlaybackInfo);

	List<MissingPlaybackInfo> queryMPByStreamId(String streamId);

}
