package com.busap.vcs.data.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.MissingPlaybackInfo;
import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.model.ExportWopaiNormalUser;
import com.busap.vcs.data.model.ExportWopaiUser;
import com.busap.vcs.data.vo.VideoVO;

public interface VideoDAO {
	/**
	 * 用户视频列表查询
	 * @param params
	 * @return
	 */
	public List<Video> searchUserVideo(Map<String,Object> params); 
	
	public List<Video> getFavoriteList(Map<String,Object> params);
	/**
	 * 用户视频列表总记录数
	 * @param params
	 * @return
	 */
	public Integer searchUserVideoCount(Map<String,Object> params);
	
	public List<Map<String,Object>> getVideoPraiseUserList(Map<String,Object> params);
	
	
	
	/**
	 * 全部最新视频分页查询
	 * @param params
	 * @return
	 */
	public List<VideoVO> searchNewVideo(Map<String,Object> params); 
	/**
	 * 全部最新视频数量
	 * @param params
	 * @return
	 */
	public Integer searchNewVideoCount(Map<String,Object> params);
	
	//按发布时间倒排序，查找最新视频
	public List<Video> findNewVideos(Map<String,Object> params);
	

	//按权重和时间倒排序，查找活动视频
	public List<Video> findActVideos(Map<String,Object> params);
	//获得相关视频
	public List<Video> findRelatedVideos(Map<String,Object> params);
	//按发布时间倒排序，查找活动视频
	public List<Video> findActVideosByTimestamp(Map<String,Object> params);
	
	
	public List<Video> findActVideosAfterAssignedTime(Map<String,Object> params);
	
	//按发布时间倒排序，查找用户视频
	public List<Video> findUserVideos(Map<String,Object> params);
	
	//按发布时间倒排序，查找用户视频及用户转发视频
	public List<Video> findUserVideosAndForward(Map<String,Object> params);
	
	public List<Video> findAttentionNewVideos(Map<String,Object> params);
	
	//按发布时间倒排序，查找关注用户的视频及关注用户转发的视频
	public List<Video> findAttentionNewVideosAndForward(Map<String,Object> params);
	
	//按发布时间倒排序，查找关注当前用户及关注用户转发的视频，直播预告，图片，回放
	public List<Video> getAttentionInfoList(Map<String,Object> params);
	
	//按创建时间倒排序，查找关注当前用户及关注用户直播
	public List<Room> getAttentionLiveInfoList(Map<String,Object> params);
	
	//按发布时间倒排序，查找他人关注用户的视频
	public List<Video> findOtherAttentionNewVideos(Map<String,Object> params);
	
	public boolean isCheckedOk(Long vid);
	
	public String getVideoStat(Long vid);
	
	//获得视频的各种指数（平均播放次数、平均点赞次数、平均评论次数、平均已发布时间）
	public Map<String,Object> getAllIndiceOfVideo();
	
	//根据指数排序，查询视频
	public List<Video> findVideoByHotPoint(Map<String,Object> params);
	
	//查询我拍秀关联视频
	public List<Video> findRefVideos(Map<String,Object> params);
	//执行每日视频热度计算存储过程
	public void execDayHotVideoProc(Map<String, Object> params);
	//当日视频热度排行榜，前50
	public List<Video> findDayHotTop50(Integer count);
	//查询用户热度排名最高的视频
	public List<Video> findUserVideoByDayHotValue(Long uid);
	
	/**
	 * 查询视频时长为空的视频playKey
	 * @param count
	 * @return
	 */
	public List<Video> findNoneDurationVideos(Integer count);
	/**
	 * 查询视频当日的播放次数
	 * @param vid
	 * @return
	 */
	public Integer dayVideoPlayCount(Long vid);
	//视频当日评论数
	public Integer dayVideoEvaCount(Long vid);
	//视频当日赞数
	public Integer dayVideoPraiseCount(Long vid);
	//后台最热视频列表
	public List<Video> searchHotVideo(Map<String, Object> params);
	//后台最热视频条数
	public Integer searchHotVideoCount(Map<String, Object> params);
	//查询最大播放率，最热视频
	public BigDecimal findMaxPlayRate();
	//查询比当前播放率排序高一位的视频
	public List<Video> findVideoUperPlayRate(Map<String, Object> params);
	//查询比当前播放率低一位的视频
	public List<Video> findVideoLowerPlayRate(Map<String, Object> params);
	//批量移除最热视频，play_rate_tody
	public void removeHotVideos(Map<String, Object> params);

	List<Video> selectVideosByCreatorId(Long creatorId);

	List<Video> selectHotVideoRankingList(Map<String,Object> params);

	List<Video> selectUserHotVideoRank(Map<String,Object> params);

	Video selectByPrimaryKey(Long id);

	/*List<ExportWopaiNormalUser> selectWopaiUserByVideo(Map<String,Object> params);*/

	Video selectVideoByWeight(Map<String,Object> params);

	int updateSort(Map<String,Object> params);

	/**
	 * 直播回放总记录数
	 * @return
	 */
	Integer getLiveBackCount();
	/**
	 * 直播回放列表-权重倒序
	 * @param params
	 * @return
	 */
	List<Video> findLiveBackList(Map<String,Object> params);
	/**
	 * 直播回放列表-时间倒序
	 * @param params
	 * @return
	 */
	List<Video> findNewLiveBackList(Map<String, Object> params);

	/**
	 * 获取最热视频-v3.0
	 * @param params
	 * @return
	 */
	List<Map<String,Object>>searchHotVideoV3(Map<String,Object> params);

	/**
	 * 根据条件查询视频信息
	 * @param params
	 * @return
	 */
	List<Video> select(Map<String,Object> params);

	Video selectVideoByLiveNoticeId(Long liveNoticeId);

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

	List<MissingPlaybackInfo> selectMPByStreamId(String streamId);

}
