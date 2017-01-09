package com.busap.vcs.chat.service;
/**
 * 连接超时检查
 */
import io.netty.channel.Channel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.busap.vcs.chat.util.ChatUtil;
import com.busap.vcs.chat.util.MessUtil;
import com.busap.vcs.chat.util.PpsConfig;
import com.busap.vcs.constants.BicycleConstants;

@Service
public class CheckTimeoutService {
	private ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
	private static final Logger logger = LoggerFactory.getLogger(CheckTimeoutService.class);
	
	@Value("${checkall_time}")
	private Integer checkAllTime;
	
	@Autowired
	private JedisService jedisService;
	private static String localhost = PpsConfig.getString("websocket.host");
	private static String port = PpsConfig.getString("websokcet.port");
	
	public void run() {
		final String localUri = localhost + ":" +port;
		logger.info("Scheduled task run start............{}",localUri);
		// 推送房间人数
		service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try{
					Map<String,List<Channel>> roomChannel= ChatUtil.roomChannels;
					logger.info("check active rooms {},and send room user count. local:{}",roomChannel==null?0:roomChannel.size(),localUri);
					if(roomChannel!=null && !roomChannel.isEmpty()){//存在活跃的房间
						Set<String> roomIds = ChatUtil.roomChannels.keySet();
						logger.info("active rooms[{}]",roomIds.size());
						for(String roomId:roomIds){
							Double scoreValue = jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, roomId);
							if(scoreValue!=null && scoreValue>=0 && !ChatUtil.roomChannels.get(roomId).isEmpty()){//只更新在线房间
								logger.info("send room[{}] user count",roomId);
								MessUtil.sendRoomUserCount(roomId,null);
							}else{
								try{
									ChatUtil.roomChannels.remove(roomId);
									logger.info("remove offline room {},host:{}",roomId,localUri);
									jedisService.deleteSetItemFromShard(BicycleConstants.ROOM_SERVERURLS+roomId, localUri);
								}catch(Exception ex){
									ex.printStackTrace();
									logger.error("remove offline room {},host:{}",roomId,localUri);
								}
							}
						}
					}
				} catch(Exception ex){
					ex.printStackTrace();
					logger.error("send user count in room error.", ex);
				}
			}
		}, 15,30,TimeUnit.SECONDS);
		
		// 推送网警提示
		service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try{
					Map<String,List<Channel>> roomChannel= ChatUtil.roomChannels;
			
					if(roomChannel!=null && !roomChannel.isEmpty()){//存在活跃的房间
						Set<String> roomIds = ChatUtil.roomChannels.keySet();
						String notice = jedisService.get("LOOP_LIVE_NOTICE");
						if(StringUtils.isNotBlank(notice)){
							for(String roomId:roomIds){
								List<Channel> list = ChatUtil.roomChannels.get(roomId);
								if(list !=null && !list.isEmpty()){
									MessUtil.sendLoopNotice(roomId,notice,"提示");
								}else{
									ChatUtil.roomChannels.remove(roomId);
								}
							}
						}
					}
				}catch(Exception ex){
					logger.error("loop live notice error.", ex);
				}
			}
		}, 5,5,TimeUnit.MINUTES);
		
		//心跳检查，保持与客户端的链接
		service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try{
					Set<String> onlineChannels = ChatUtil.uidChannel.keySet();
					logger.info("在线时间检查：{}",onlineChannels==null?0:onlineChannels.size());
					if(onlineChannels != null && onlineChannels.size()>0){
						logger.info("当前在线人数：{}",onlineChannels.size());
						long now = System.currentTimeMillis();
						for(Iterator<String> it=onlineChannels.iterator();it.hasNext();){
							String uid = it.next();
							if(StringUtils.isNotBlank(uid)){
								Double hbTime = jedisService.zscore(BicycleConstants.USER_LAST_HARTBEAT, uid);//ChatUtil.userOnline.get(chn);
								if((now-hbTime.longValue())>300000){
									logger.info("用户连接超时,即将关闭连接.");
									ChatUtil.removeUser(uid);
								}
							}
						}
					}	
				}catch(Exception ex){
					logger.error("check user online error.", ex);
				}
			}
			
		}, 1,15,TimeUnit.SECONDS);
	}

	public JedisService getJedisService() {
		return jedisService;
	}

	public void setJedisService(JedisService jedisService) {
		this.jedisService = jedisService;
	}
}
