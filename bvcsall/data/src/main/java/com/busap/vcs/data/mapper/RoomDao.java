package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.model.LiveDayDetailDisplay;
import com.busap.vcs.data.model.LiveDetailDisplay;
import com.busap.vcs.data.model.OrganizationAnchorDisplay;

/**
 * Created by busap on 2016/5/16.
 */
public interface RoomDao {

    List<LiveDetailDisplay> selectLiveDetailRecord(Map<String,Object> params);

    Map<String,String> selectLiveDataByLiveActivityId(Long liveActivityId);
    
    Map<String,String> selectLiveDataByLiveActivityIdAndUserId(Map<String,Object> params);
    
    Long findSenderCount(Map<String,Object> params);
    
    Long findSumDPByLiveActivityId(Map<String,Object> params);

    Long selectDailyDataLiveCount(Map<String,Object> params);

    Long selectDailyDataNewRegLiveCount(Map<String,Object> params);

    Long selectDailyDataNewLiveCount(Map<String,Object> params);

    List<LiveDayDetailDisplay> selectDailyDataLiveDetailCount(Map<String,Object> params);

    Long selectDailyDataLiveTotalCount(Map<String,Object> params);

    Long selectDistinctLiveNumByLiveActivityId(Long liveActivityId);

    OrganizationAnchorDisplay selectUserLiveDurationInfo(Map<String,Object> params);

    List<String> selectPeriodFirstTimeLiveUser(Map<String,Object> params);

    Room selectRoomByPersistentId(Map<String, Object> params);

    Integer endLive(Map<String,Object> params);

    List<Room> selectRoomListByUserId(Map<String,Object> params);
}
