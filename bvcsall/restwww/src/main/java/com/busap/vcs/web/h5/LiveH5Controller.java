package com.busap.vcs.web.h5;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.LiveNotice;
import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.service.AnchorService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LiveNoticeService;
import com.busap.vcs.service.RoomService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.VideoService;

/**
 * h5页面关注的controller
 *
 */
@Controller
@RequestMapping("/page/live")
public class LiveH5Controller extends BaseH5Controller {

	private Logger logger = LoggerFactory.getLogger(LiveH5Controller.class);

	@Autowired
	protected HttpServletRequest request;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name = "roomService")
	private RoomService roomService;
	
	@Resource(name = "liveNoticeService")
	private LiveNoticeService liveNoticeService;
	
	@Resource(name="videoService") 
    VideoService videoService; 
	
	@Resource(name = "anchorService")
	private AnchorService anchorService;

	/**
	 * 直播分享
	 * @param videoId
	 * @return
	 */
	@RequestMapping("/shareLive")
	public String shareLive(String roomId) {
		try {
			logger.info("H5,shareLive");
			logger.info("roomId={}",roomId);
			
			Map<String,String> room = jedisService.getMapByKey(BicycleConstants.ROOM_+ roomId);
			if (room != null && room.size() > 0 && room.get("id") != null) {
				//计算直播时长
				Long duration = System.currentTimeMillis() - Long.parseLong(room.get("createDate")); 
				room.put("duration", String.valueOf(duration < 0?0:duration));
				//查询用户信息
				Ruser ruser = ruserService.find(Long.parseLong(room.get("creatorId")));
				room.put("anchorName", ruser.getName());
				room.put("anchorPic", ruser.getPic());
				room.put("anchorVstat", String.valueOf(ruser.getVipStat()));
				room.put("anchorSignature", ruser.getSignature());
			} else {
				room = new HashMap<String,String>();
				Room roomEntity = roomService.find(Long.parseLong(roomId));
				if (roomEntity != null) {
					Ruser ruser = ruserService.find(roomEntity.getCreatorId());
					room.put("roomPic", roomEntity.getRoomPic());
					room.put("duration",String.valueOf(roomEntity.getDuration()));
					room.put("maxAccessNumber",String.valueOf(roomEntity.getMaxAccessNumber()));
					room.put("praiseNumber", String.valueOf(roomEntity.getPraiseNumber()));
					room.put("mjPraiseNumber", String.valueOf(roomEntity.getMjPraiseNumber()));
					room.put("anchorName", ruser.getName());
					room.put("anchorPic", ruser.getPic());
					room.put("anchorVstat", String.valueOf(ruser.getVipStat()));
					room.put("anchorSignature", ruser.getSignature());
					room.put("creatorId", String.valueOf(roomEntity.getCreatorId()));
					room.put("id", roomId);
				}
				
			}
			
			//查询回放信息
			Video video = videoService.getPlaybackByRoomId(Long.parseLong(roomId));
			this.request.setAttribute("video", video);
			this.request.setAttribute("room", room);
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		
    	return "html5/live/live";
	}
	
	@RequestMapping("/identify")
	public String identify() {
		String uid = this.request.getParameter("uid");
		if (uid == null || "".equals(uid)){
			uid = this.request.getHeader("uid");
		}
		String accessToken = this.request.getParameter("access_token");
		if (accessToken == null || "".equals(accessToken)) {
			accessToken = this.request.getHeader("access_token");
		}
		Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(uid));
		//用户的证件信息照片地址不返回
		if (anchor != null) {
			anchor.setPicOne("");
			anchor.setPicTwo("");
			anchor.setPicThree("");
		}
		try {
			this.request.setAttribute("uid", uid);
			this.request.setAttribute("accessToken", accessToken);
			this.request.setAttribute("anchor", anchor);
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		
    	return "html5/app_within/liveIdentify/index";
	}
	
	//直播预告分享
	@RequestMapping("/shareLiveNotice")
	public String shareLiveNotice(String noticeId) {
		try {
			logger.info("H5,shareLiveNotice");
			logger.info("noticeId={}",noticeId);
			
			LiveNotice notice = liveNoticeService.find(Long.parseLong(noticeId));
			if (notice != null) {
				Ruser ruser = ruserService.find(notice.getCreatorId());
				this.request.setAttribute("ruser", ruser);
			}
			
			this.request.setAttribute("notice", notice);
			
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		
    	return "html5/app_within/live_forenotice/index";
	}
}
