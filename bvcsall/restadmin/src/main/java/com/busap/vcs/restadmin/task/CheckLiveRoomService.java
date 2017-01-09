package com.busap.vcs.restadmin.task;

/**
 * Created by busap on 2016/5/6.
 */
public interface CheckLiveRoomService {

    /**
    * 定时刷新，将最新直播房间ID写入redis
    */
    void refreshNewestLiveRoom();

}
