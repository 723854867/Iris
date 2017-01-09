package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.busap.vcs.base.Filter;
import com.busap.vcs.data.entity.ActivityVideo;
import com.busap.vcs.data.entity.LabelVideo;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.mapper.AttentionDAO;
import com.busap.vcs.data.repository.LabelVideoRepository;
import com.busap.vcs.service.AttentionService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.VideoService;
import com.busap.vcs.service.impl.SolrWoPaiVideoTagService;

/**
 * h5页面有关用户信息的controller
 *
 */
@Controller
@RequestMapping("/video")
public class VideoH5Controller extends BaseH5Controller {

	private Logger logger = LoggerFactory.getLogger(VideoH5Controller.class);

	@Autowired
	protected HttpServletRequest request;

	@Resource(name = "ruserService")
	private RuserService ruserService;

	@Resource(name = "videoService")
	private VideoService videoService;
	
	@Resource(name="attentionService")
	private AttentionService attentionService;
	
	@Autowired
	private SolrWoPaiVideoTagService solrWoPaiVideoTagService;

	@Autowired
	AttentionDAO attention;
	
	@Autowired
    private LabelVideoRepository labelVideoRepository;
	
	/**
	 * 视频详情
	 * @param videoId
	 * @return
	 */
	@RequestMapping("/videoDetail")
	public String videoDetail(Long videoId,String uid,Integer isNext,Integer nextType) {
//		return "redirect:http://www.wopaitv.com";
		try {
			String activityId = request.getParameter("activityId");
			String tag = request.getParameter("tag");
			String userId = request.getParameter("userId");
			
			if (isNext == null) {
				isNext = 0;
			}
			if (nextType == null){
				nextType = -1;
			}
			
			if (isNext == 1){  //点击下一条视频进入视频详情页的操作
				if (nextType == 1 || nextType == 2) { //首页进入,活动进入
					List<Video> list = videoService.findActVideos(null, Long.parseLong(activityId), uid, 200, 1);
					Long nextVideoId = findNextVideoId(list, videoId);
					if (videoId.intValue() == nextVideoId.intValue()) {
						this.request.setAttribute("hasNext", "0");
					}
					videoId = nextVideoId;
				} else if (nextType == 3) {  //话题进入
					List<Long> videoIdList = solrWoPaiVideoTagService.search(tag, 0,200);
					List<Video> list = videoService.findVideosByIds(videoIdList, uid);
					Long nextVideoId = findNextVideoId(list, videoId);
					if (videoId.intValue() == nextVideoId.intValue()) {
						this.request.setAttribute("hasNext", "0");
					}
					videoId = nextVideoId;
				} else if (nextType == 4) {  //排行榜进入
					List<Video> list = videoService.findDayHotVideosRank(StringUtils.isNotBlank(uid)?Long.parseLong(uid):null,0,50);
					Long nextVideoId = findNextVideoId(list, videoId);
					if (videoId.intValue() == nextVideoId.intValue()) {
						this.request.setAttribute("hasNext", "0");
					}
					videoId = nextVideoId;
				} else if (nextType == 5) {  //关注页进入
					List<Long> ids = attentionService.selectAllAttentionId(Long.parseLong(uid));
					ids.add(Long.parseLong(uid));
					List<Video> list = videoService.findAttentionNewVideos(Long.parseLong(uid),ids, null, 200);
					Long nextVideoId = findNextVideoId(list, videoId);
					if (videoId.intValue() == nextVideoId.intValue()) {
						this.request.setAttribute("hasNext", "0");
					}
					videoId = nextVideoId;
				} else if (nextType == 6) {  //个人中心进入
					List<Video> list = videoService.findUserVideos(Long.parseLong(userId), null, 10, StringUtils.isNotBlank(uid)?Long.parseLong(uid):null);
					Long nextVideoId = findNextVideoId(list, videoId);
					if (videoId.intValue() == nextVideoId.intValue()) {
						this.request.setAttribute("hasNext", "0");
					}
					videoId = nextVideoId;
				} else {
					List<ActivityVideo> actVideolist = videoService.findActivityIdByVideoId(videoId);
					if (actVideolist != null && actVideolist.size() >0 ){
						List<Video> list = videoService.findActVideos(null, Long.parseLong(String.valueOf(actVideolist.get(0).getActivityid())), uid, 200, 1);
						Long nextVideoId = findNextVideoId(list, videoId);
						if (videoId.intValue() == nextVideoId.intValue()) {
							this.request.setAttribute("hasNext", "0");
						}
						videoId = nextVideoId;
					} else {
						List<Video> list = videoService.findVideosWithouActivity(videoId, 1);
						if (list != null && list.size() >0 ){
							videoId = list.get(0).getId();
						}
					}
				}
			} 
			logger.info("H5,videoDetail,videoId={},uid={}",videoId,uid);
			//视频信息
			Video video = findOne(videoId,uid);
			//视频赞用户信息
			List<Map<String,Object>> praiseUserList = praiseUserList(videoId, 0l, 20, uid);
			
			this.request.setAttribute("video",video ); 
			this.request.setAttribute("praiseUserList",praiseUserList ); 
			
			this.request.setAttribute("activityId",activityId ); 
			this.request.setAttribute("tag",tag ); 
			this.request.setAttribute("userId",userId ); 
			this.request.setAttribute("nextType",nextType ); 
		} catch (Exception e) {
			e.printStackTrace();
			return "html5/default/errorpage";
		}
		
    	return "html5/default/video_show";
	}
	
	//获得视频详情
	private Video findOne(Long id,String uid){ 
    	Video video = videoService.getVideo(id,uid);
    	if(video!=null&&uid!=null&&!uid.trim().equals("")){
    		Map<String, Object> params = new HashMap<String, Object>();
    		params.put("uid", Long.parseLong(uid));
    		params.put("otherUid", video.getCreatorId());
    		video.setAttentionAuthor(attention.isAttention(params));
    	}
    	
    	//新老版本获取标签
    	if(video != null){
    		List<Filter> filters = new ArrayList<Filter>();
			filters.add(Filter.eq("videoId", video.getId()));
			Order order = new Order(Direction.ASC,"id");
			Sort sort = new Sort(order);
			List<LabelVideo> lvs = labelVideoRepository.findAll(filters, sort);
			List<String> tagList = null;
			if (CollectionUtils.isNotEmpty(lvs)) {
				tagList = new ArrayList<String>(lvs.size());
				for(LabelVideo lv : lvs){
					tagList.add(lv.getLabelName());
				}
			}
    		
			video.setTags(tagList);
    	}
    	parseUserInfo(video);
    	return video;
    }  
	
	//获得视频赞用户列表
	public List<Map<String,Object>> praiseUserList(Long vid, Long minPraiseId, Integer count,String uid) {
    	List<Map<String,Object>> ls= videoService.getVideoPraiseUserList(vid, minPraiseId, count,(uid==null||uid.trim().equals(""))?null:Long.parseLong(uid));
    	parseUserInfo(ls);
        return ls;
    }
	
	@RequestMapping("/tagVideos")
	public String tagVideos(String tag) { 
		request.setAttribute("tag", tag);
		return  "html5/default/video_show_tag";
	}
	
	private Long findNextVideoId(List<Video> list,Long currentId){
		for (int i=0;i<list.size();i++) { //获得下一条视频id
			Video video = list.get(i);
			if (video.getId().intValue() == currentId){
				if (i<list.size()-1) {
					return list.get(i+1).getId();
				} 
			}
		}
		return currentId;
	}
	
	@RequestMapping("/inviteFriendVideo") 
    public String inviteFriendVideo(String inviteId) {
        return "html5/inviteActivity";
    }
}
