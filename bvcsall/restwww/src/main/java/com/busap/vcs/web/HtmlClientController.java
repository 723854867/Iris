package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.base.Filter;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Activity;
import com.busap.vcs.data.entity.Banner;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.enums.VideoStatus;
import com.busap.vcs.data.repository.VideoRepository;
import com.busap.vcs.service.ActivityService;
import com.busap.vcs.service.AttentionService;
import com.busap.vcs.service.BannerService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.VideoService;
import com.busap.vcs.service.impl.NotificationJPushUtil;
//import com.busap.vcs.data.mapper.AttentionDAO;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;

@Controller
@RequestMapping("/html5")
public class HtmlClientController {

	private Logger logger = LoggerFactory.getLogger(HtmlClientController.class);

	@Autowired
	NotificationJPushUtil util;

	@Autowired
	protected HttpServletRequest request;

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@Resource(name = "bannerService")
	private BannerService bannerService;
	
	@Resource(name="activityService")
	private ActivityService activityService; 

	@Resource(name="videoService") 
	VideoService videoService; 
	
	@Resource(name="videoRepository") 
	private VideoRepository videoRepository;
	
	@Resource(name="attentionService")
	private AttentionService attentionService;

	@RequestMapping("/index") 
    public String loadIndex(Integer startId,String uid) {
		String uidheader = (String)this.request.getHeader("uid");
		if(uidheader!=null&&!uidheader.isEmpty()){
		uid=	uidheader;
		}
    	Integer count=5;
    	long activityId=0;
    	if(startId==null){
    		startId=0;
    	}
    	List<Video> videolist = videoService.findHotIndiceVideos(startId,count,uid,activityId);
     	for(Video v:videolist){ 
    		if(v!=null&&v.getCreatorId()!=null){
    			v.setUser(ruserService.find(v.getCreatorId()));
    			int count1 = videoRepository.decEvaluationCount(v.getId());
    			v.setEvaluationCount(count1);
//    			v.setDescription(v.getDescription().replace("'", "\\'"));
        		if(v.getName()!=null){
    	    		v.setName(v.getName().replace("'", "\\'"));
    	    		}
     		}
     	}
     	
     	List<Banner> banners = bannerService.findAllBanner(0);
     	
     	List<Activity> activitys =  activityService.findAllByGroupType(0,"");
     	
     	
     	this.request.setAttribute("activitys",activitys); 
     	this.request.setAttribute("banners",banners); 
    	this.request.setAttribute("videolist",videolist); 
        return "h5/index1";
    }
	
	@Resource(name = "ruserService")
	RuserService ruserService;
	
    @RequestMapping("/allactivity") 
    public String loadAllActivity() {
    	List<Activity> allactivity= activityService.findAllByGroupType(1,"");
    	this.request.setAttribute("allactivity", allactivity);
        return "h5/allactivity";
    }

    @RequestMapping("/videodetail") 
    public String loadVideoDetail(String playKey,String uid,String videoId) {
       	Video v=null;
    	if(playKey!=null&&playKey.length()>0){
    		
    		v = videoService.getVideo(playKey);
    	}
    	if(videoId!=null&&videoId.length()>0){
    		v = videoService.find(Long.parseLong(videoId));
    		if(v!=null&&v.getCreatorId()!=null){
    			v.setUser(ruserService.find(v.getCreatorId()));
    		}
    	}
    	if(uid!=null){
    		this.request.setAttribute("shareuid",uid ); 
    	}
    	this.request.setAttribute("video",v ); 
    	this.request.setAttribute("topHot",videoService.findTopHotVideos(6)); 
    	return "h5/detail";
    }
    
    @RequestMapping("/activity") 
    public String loadActivity(String activityid,String uid) {
		String uidheader = (String)this.request.getHeader("uid");
		if(uidheader!=null&&!uidheader.isEmpty()){
		uid=	uidheader;
		}
    	Activity activity = activityService.find(Long.parseLong(activityid));
    	this.request.setAttribute("activity", activity);
    	List<Video> hotVides = videoService.findHotIndiceVideos(0, 5,uid,Long.parseLong(activityid));
    	
     	for(Video v:hotVides){ 
    		if(v!=null&&v.getCreatorId()!=null){
    			v.setUser(ruserService.find(v.getCreatorId()));
    			int count = videoRepository.decEvaluationCount(v.getId());
    			v.setEvaluationCount(count);
//    			v.setDescription(v.getDescription().replace("'", "\\'"));
        		if(v.getName()!=null){
    	    		v.setName(v.getName().replace("'", "\\'"));
    	    		}
     		}
     	}
     	this.request.setAttribute("hotVides", hotVides);
    	List<Video> newVides = videoService.findActVideos(0l,Long.parseLong(activityid),uid,5,1);
      	for(Video v:newVides){ 
    		if(v!=null&&v.getCreatorId()!=null){
    			v.setUser(ruserService.find(v.getCreatorId()));
    			int count = videoRepository.decEvaluationCount(v.getId());
    			v.setEvaluationCount(count);
    			if(uid!=null&&!uid.trim().equals("")){
    	    		v.setPraise(videoService.praised(Long.parseLong(uid), v.getId()));
    	    		v.setFavorite(videoService.favorited(Long.parseLong(uid), v.getId()));
    	    		v.setAttentionAuthor(attentionService.isAttention(Long.parseLong(uid), v.getCreatorId()));
//    	    		v.setDescription(v.getDescription().replace("'", "\\'"));
    	    		if(v.getName()!=null){
    	    		v.setName(v.getName().replace("'", "\\'"));
    	    		}
        		}
     		}
     	}
    	this.request.setAttribute("newVides", newVides);
    	this.request.setAttribute("activityid",activityid );
//    	Map<String,Object> Banners= activityService.getBanner(Long.parseLong(activityid));
//    	this.request.setAttribute("bannerPic", Banners.get("bannerPic"));
        return "h5/find";
    }
    
    //按发布时间倒排序获取活动视频
    @RequestMapping("/findNewActVideos")
    @ResponseBody
    public RespBody findNewVideos(Integer page,Integer size,Integer actId,String uid){
		String uidheader = (String)this.request.getHeader("uid");
		if(uidheader!=null&&!uidheader.isEmpty()){
		uid=	uidheader;
		}
    	List<Video> newVides = videoService.findByActivityId(page, size, actId,uid).getContent();
      	for(Video v:newVides){ 
    		if(v!=null&&v.getCreatorId()!=null){
    			v.setUser(ruserService.find(v.getCreatorId()));
    			int count = videoRepository.decEvaluationCount(v.getId());
    			v.setEvaluationCount(count);
    			if(uid!=null&&!uid.trim().equals("")){
    	    		v.setPraise(videoService.praised(Long.parseLong(uid), v.getId()));
    	    		v.setFavorite(videoService.favorited(Long.parseLong(uid), v.getId()));
    	    		v.setAttentionAuthor(attentionService.isAttention(Long.parseLong(uid), v.getCreatorId()));
//    	    		v.setDescription(v.getDescription().replace("'", "\\'"));
    	    		if(v.getName()!=null){
        	    		v.setName(v.getName().replace("'", "\\'"));
        	    		}
        		}
     		}
     	}
    	return respBodyWriter.toSuccess(newVides); 
    }
    //根据热度指数（播放指数+点赞指数+评论指数+时间指数）排序，查询视频
    @RequestMapping("/findHotIndiceVideos")
    @ResponseBody
    public RespBody findHotIndiceVideos(Integer startIndex, Integer count,Long activityId,String uid){  
		String uidheader = (String)this.request.getHeader("uid");
		if(uidheader!=null&&!uidheader.isEmpty()){
		uid=	uidheader;
		}
    	List<Video> hotVides = videoService.findHotIndiceVideos(startIndex, count,uid,activityId);
    	
     	for(Video v:hotVides){ 
    		if(v!=null&&v.getCreatorId()!=null){
    			v.setUser(ruserService.find(v.getCreatorId()));
    			int count1 = videoRepository.decEvaluationCount(v.getId());
//    			v.setDescription(v.getDescription().replace("'", "\\'"));
        		if(v.getName()!=null){
    	    		v.setName(v.getName().replace("'", "\\'"));
    	    		}
    			v.setEvaluationCount(count1);
     		}
     	}
    	return respBodyWriter.toSuccess(hotVides); 
    }
 
    
    
    
}
