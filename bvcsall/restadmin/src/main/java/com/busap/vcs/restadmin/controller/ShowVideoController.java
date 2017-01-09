package com.busap.vcs.restadmin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.entity.Activity;
import com.busap.vcs.data.entity.ActivityVideo;
import com.busap.vcs.data.entity.ShowVideo;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.enums.UserType;
import com.busap.vcs.service.ActivityService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.ShowVideoService;
import com.busap.vcs.service.VideoService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;

@Controller()
@RequestMapping("showvideo")
public class ShowVideoController extends CRUDController<ShowVideo, Long> {


	@Value("${video_play_url_prefix}")
	private String video_play_url_prefix;
	
	@Value("${uploadpic_url_prefix}")
	private String uploadpic_url_prefix;
	
	@Resource(name = "showVideoService")
	private ShowVideoService showVideoService;

	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name = "activityService")
	private ActivityService activityService;
	
	@Resource(name = "videoService")
	private VideoService videoService;
	
	@Override
	public void setBaseService(BaseService<ShowVideo, Long> baseService) {
		this.baseService = baseService;
	}
	
	@RequestMapping("listShows")
	public String listShows(){
		this.request.setAttribute("video_play_url_prefix", video_play_url_prefix);
		this.request.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix); 
		List<Activity> activityList = this.activityService.findAll();
		List rusers=ruserService.findByType(UserType.马甲.getName());
		this.request.setAttribute("uploaduserlist", rusers);
		this.request.setAttribute("activites", activityList);
		return "show/list";
	}
	
	@RequestMapping("searchListPage")
	@ResponseBody
	public Map searchList(Integer page,Integer rows){
		if(page == null || page<1){
			page = 1;
		}
		Page pinfo = showVideoService.findShowVideos(page, rows);

		Map<String, Object> jsonMap = new HashMap<String, Object>();
	    jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的
	    jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list

	    return jsonMap;
	}
	
	@RequestMapping("modifyShow")
	public String updateShows(Long id){
		ShowVideo show = showVideoService.find(id);
				
		this.request.setAttribute("showVideo", show);
		
		String img="";
		
		Video video = this.videoService.find(show.getVideoId());
		List svList=U.parseListMapToList(this.videoService.findActivityIdByVideoId(show.getVideoId()),ActivityVideo.class,"activityid");
		
		if(video!=null){
			img=this.videoService.findImgByVideo(show.getVideoId());
			if(StringUtils.isBlank(img)){
				img=this.video_play_url_prefix+ video.getPlayKey() +".jpg";
			}else{
				img=this.uploadpic_url_prefix+img;
			}
			video.setImg(img);
		}
		
		
		List activityList = this.activityService.findAll();
		this.request.setAttribute("activityList", activityList);
		
		List rusers=ruserService.findByType(UserType.马甲.getName());
		this.request.setAttribute("uploaduserlist", rusers);

		this.request.setAttribute("svList", svList);
		this.request.setAttribute("video", video);
		this.request.setAttribute("video_play_url_prefix", video_play_url_prefix);
		this.request.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);

		return "show/update";
	}
	@RequestMapping("updateShowVideo")
	@ResponseBody
	public RespBody updateShow(ShowVideo showVideo){
		if(showVideo.getId()!=null){
			ShowVideo temp = this.showVideoService.find(showVideo.getId());
			temp.setDescription(showVideo.getDescription());
			temp.setTitle(showVideo.getTitle());
			temp.setRefVideoId(showVideo.getRefVideoId());
			temp.setPic(showVideo.getPic());
			this.showVideoService.update(temp);
		}
		return this.respBodyWriter.toSuccess();
	}

}
