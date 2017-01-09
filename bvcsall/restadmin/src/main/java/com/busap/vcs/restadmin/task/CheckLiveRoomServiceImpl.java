package com.busap.vcs.restadmin.task;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by busap on 2016/5/6.
 */
@Service("checkLiveRoomService")
public class CheckLiveRoomServiceImpl implements CheckLiveRoomService {

    @Resource
    private JedisService jedisService;

    @Resource
    private RoomService roomService;

    private Logger logger = LoggerFactory.getLogger(CheckLiveRoomServiceImpl.class);

    @Override
    public void refreshNewestLiveRoom(){
        logger.info("更新最新直播房间ID到redis");
        Long maxRoomId = roomService.queryMaxRoomId();
        //System.out.println(maxRoomId);
        jedisService.set(BicycleConstants.NEWEST_LIVE_ROOM, String.valueOf(maxRoomId));
    }

}
