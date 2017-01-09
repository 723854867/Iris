package com.busap.vcs.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.LiveCheckLog;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.LiveCheckLogRepository;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LiveCheckLogService;

/**
 * Created by busap on 2015/12/23.
 */
@Service("liveCheckLogService")
public class LiveCheckLogImpl  extends BaseServiceImpl<LiveCheckLog, Long> implements LiveCheckLogService {
	
	@Resource(name="liveCheckLogRepository")
	private LiveCheckLogRepository liveCheckLogRepository;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name="liveCheckLogRepository")
	@Override
	public void setBaseRepository(BaseRepository<LiveCheckLog, Long> baseRepository) {
		super.setBaseRepository(liveCheckLogRepository);
	}

	@Override
	public void check(Long operatorId,String operate, String reason, Long roomId, Long userId,String type) {
		LiveCheckLog lcLog = new LiveCheckLog();
		lcLog.setCreatorId(operatorId);
		lcLog.setOperate(operate);
		lcLog.setReason(reason);
		lcLog.setUserId(userId);
		lcLog.setRoomId(roomId);
		lcLog.setType(type);
		save(lcLog);
		
		//将下线或者封号的房间id存到redis中，该房间不能保存回放
		jedisService.setValueToSetInShard(BicycleConstants.CHECK_FAIL_ROOM_ID, String.valueOf(roomId));
	}

}
