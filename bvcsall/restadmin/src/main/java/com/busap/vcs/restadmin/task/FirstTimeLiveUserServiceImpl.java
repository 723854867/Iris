package com.busap.vcs.restadmin.task;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RoomService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by busap on 2016/7/12.
 */
@Service("firstTimeLiveUserService")
public class FirstTimeLiveUserServiceImpl implements FirstTimeLiveUserService {

    @Resource
    private RoomService roomService;

    @Resource
    private JedisService jedisService;

    public void queryPeriodFirstTimeLiveUser(){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("hours", StringUtils.isBlank(jedisService.get(BicycleConstants.FIRST_LIVE_TIMES))?24:jedisService.get(BicycleConstants.FIRST_LIVE_TIMES));
        List<String> userIdList = roomService.queryPeriodFirstTimeLiveUser(params);
        if(userIdList != null && userIdList.size()>0) {
            jedisService.setObject(BicycleConstants.FIRST_LIVE_USER_INFO, userIdList);
        }
    }

}
