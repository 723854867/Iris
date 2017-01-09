package com.busap.vcs.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.model.LiveDayDetailDisplay;
import com.busap.vcs.data.model.LiveDetailDisplay;
import com.busap.vcs.data.model.OrganizationAnchorDisplay;

public interface RoomService extends BaseService<Room, Long> {

	/**
	 * 按权重大小倒序
	 * @param page
	 * @param size
	 * @param isLive
	 * @param userId
	 * @return
	 */
	List<Map<String,String>> getRoomList(Integer page, Integer size, Integer isLive, String userId);
	/**
	 * 按创建时间倒序
	 * @param page
	 * @param size
	 * @param isLive
	 * @param userId
	 * @return
	 */
	
	public Map<String,List> getSolrRoomList(Integer page, Integer size, String title);
	
	List<Map<String,String>> getNewRoomList(Long liveActivityId,Integer page, Integer size);
	
	Map<String,String> createRoom(String title,String pic,Long uid,Long liveActivityId,Integer liveType,boolean canChangeCDN,String localOutIp,String appVersion,String platform,String channel,String longitude,String latitude,String area,boolean canLinkMic);

	List<Map<String, String>> getRoomUser(String roomId, int page, int size,String uid);

	void destroyRoom(String roomId);
	
	int isLive(String roomId);
	
	public List<Room> getRoomListByUid(Long uid,int status);
	
	public Integer getLiveTimes(Long userId);

	Map<String, String> getSingleUser(Long userId,String uid,String roomId);
	
	public Date getDbTime();

	Room queryLivingRoomByUserId(Long userId);

	/**
	 * 获得直播直播房间数量
	 * @param isLive 1正在直播 0直播结束
	 * @return
	 */
	int getLivingRoomSize(Integer isLive);
	
	public void savePlayback(Long roomId);
	/**
	 * 直播回放记录数
	 * @return
	 */
	Integer getAllPlaybackCount();
	/**
	 * 直播回放列表
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
	List<Map<String,String>> findPlaybackList(Long userId,Integer start,Integer end);
	
	/**
	 * 直播回放列表-时间倒序
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
	List<Map<String,String>> findNewPlaybackList(Long userId,Integer start,Integer end);
	/**
	 * 发送弹幕消息
	 * @param uid
	 * @param roomId
	 * @param message
	 */
	Integer sendBarrage(Long uid, Long roomId, String message);
	/**
	 * 发送弹幕消息
	 * @param uid
	 * @param roomId
	 * @param message
	 */
	Integer sendBarrageNew(Long uid, Long roomId, String message);
	/**
	 * 根据直播活动查询
	 * @param activityId
	 * @param page
	 * @param size
	 * @return
	 */
	List<Map<String, String>> getRoomList(Long activityId, Integer page,Integer size);

	/*Integer queryDailyLiveNum (String date);*/

	Long queryMaxRoomId();

	List<LiveDetailDisplay> queryLiveDetailRecord(Map<String,Object> params);

/*	Long queryNewRegUserLiveCount(String date);

	Long queryNewLiveCount(String date);*/

	void offlineRoom(Long roomId,Long uid,String message);

	Map<String,String> queryLiveDataByLiveActivityId(Long liveActivityId);
	
	Map<String, String> selectLiveDataByLiveActivityIdAndUserId(Map params);
	
	Long findSenderCount(Map<String,Object> params);
	
	Long findSumDPByLiveActivityId(Map<String,Object> params);

	Long queryDailyDataLiveCount(Map<String,Object> params);

	Long queryDailyDataNewRegLiveCount(Map<String,Object> params);

	Long queryDailyDataNewLiveCount(Map<String,Object> params);

	List<LiveDayDetailDisplay> queryDailyDataLiveDetailCount(Map<String,Object> params);

	Long queryDailyDataLiveTotalCount(Map<String,Object> params);

	Long queryDistinctLiveNumByLiveActivityId(Long liveActivityId);

	OrganizationAnchorDisplay queryUserLiveDurationInfo(Map<String,Object> params);

	List<String> queryPeriodFirstTimeLiveUser(Map<String, Object> params);

	Room queryRoomByPersistentId(Map<String, Object> params);

	List<Room> queryRoomListByUserId(Map<String,Object> params);

}
