package com.busap.vcs.web;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.busap.vcs.base.Filter;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Label;
import com.busap.vcs.data.entity.LabelVideo;
import com.busap.vcs.data.entity.LiveNotice;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.mapper.AttentionDAO;
import com.busap.vcs.data.mapper.RuserDAO;
import com.busap.vcs.data.repository.LabelRepository;
import com.busap.vcs.data.repository.LabelVideoRepository;
import com.busap.vcs.data.vo.LoadConfigUrlVO;
import com.busap.vcs.service.ActivityService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.ForwardService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LabelService;
import com.busap.vcs.service.LiveNoticeService;
import com.busap.vcs.service.LoadConfigUrlService;
import com.busap.vcs.service.RecommandPositionService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.SensitiveWordService;
import com.busap.vcs.service.SubscribeService;
import com.busap.vcs.service.VideoService;
import com.busap.vcs.service.impl.SolrRoomService;
import com.busap.vcs.service.impl.SolrVideoService;
import com.busap.vcs.service.impl.SolrWoPaiLiveTagService;
import com.busap.vcs.service.impl.SolrWoPaiTagService;
import com.busap.vcs.service.impl.SolrWoPaiVideoTagService;
import com.busap.vcs.service.kafka.producer.KafkaProducer;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.util.PictureScaleUtil;
 
@Controller
@RequestMapping("/video")
public class VideoController extends CRUDController<Video, Long> {

    private Logger logger = LoggerFactory.getLogger(VideoController.class); 

    @Resource(name="videoService") 
    VideoService videoService; 
     
    
    @Autowired
    KafkaProducer kafkaProducer;
    
    @Resource(name="activityService") 
    ActivityService activityService;
    
    @Resource(name="sensitiveWordService") 
    SensitiveWordService sensitiveWordService;
    
    @Resource
    private LiveNoticeService liveNoticeService;

	@Value("${files.localpath}")
	private String basePath;
	
	 @Resource(name="subscribeService") 
	 SubscribeService subscribeService;
	 
	 @Resource(name = "solrVideoService")
	SolrVideoService solrVideoService;
	 
	 @Resource(name="forwardService")
	 ForwardService forwardService;
	 
	 @Autowired
	private RuserDAO ruserDAO;
	 
	 @Autowired
	 private SolrWoPaiVideoTagService solrWoPaiVideoTagService;
	 
	 @Resource(name="recommandPositionService")
	 private RecommandPositionService recommandPositionService;
	 
	 @Resource(name = "solrRoomService")
	private SolrRoomService solrRoomService;
	 
	 @Resource(name = "solrWoPaiLiveTagService")
	private SolrWoPaiLiveTagService solrWoPaiLiveTagService;
	 
	@Resource(name="labelRepository")
	private LabelRepository labelRepository;
	
	@Autowired
	private SolrWoPaiTagService solrWoPaiTagService;
	
    @Resource(name="videoService")
    @Override
    public void setBaseService(BaseService<Video, Long> baseService) {
        this.baseService = baseService;
    } 
     
    @Autowired
	AttentionDAO attention;
    
    @Autowired
    private LabelVideoRepository labelVideoRepository;
    
    
	@Autowired
    JedisService jedisService;
	
    @Resource(name = "ruserService")
	private RuserService ruserService;
    
    @Resource(name="labelService")
    private LabelService labelService;
    
    @Resource(name="loadConfigUrlService")
    private LoadConfigUrlService loadConfigUrlService;
    
    //在专题页通过分页和专题号获取视频列表信息
    @RequestMapping("/findBySubjectId")
    @ResponseBody
    public RespBody findBySubjectId(Integer page, Integer size, String subjectId){
    	String uid = (String)this.request.getHeader("uid");
    	return respBodyWriter.toSuccess(videoService.findBySubjectId(page, size, subjectId,uid)); 
    }
    
    //在活动页通过分页和活动号获取视频列表信息
    @RequestMapping("/findByActivityId")
    @ResponseBody
    public RespBody findByActivityId(Integer page, Integer size, Long actId){
    	String uid = (String)this.request.getHeader("uid");
    	Integer isSubscribe = 0;
    	if (uid !=null && !"".equals(uid))
    		isSubscribe = subscribeService.isSubscribed(Long.parseLong(uid), actId);
    	return respBodyWriter.toSuccess(RespBody.MESSAGE_OK,videoService.findByActivityId(page, size, actId,uid),String.valueOf(isSubscribe)); 
    }
    
    //按发布时间倒排序获取活动视频
    @RequestMapping("/findActVideos")
    @ResponseBody
    public RespBody findActVideos(Long timestamp,Long actId,Integer count,Integer page){
    	String uid = (String)this.request.getHeader("uid");
    	Integer isSubscribe = 0;
    	if (uid !=null && !"".equals(uid))
    		isSubscribe = subscribeService.isSubscribed(Long.parseLong(uid), actId);
    	return respBodyWriter.toSuccess(RespBody.MESSAGE_OK,videoService.findActVideos(timestamp, actId, uid, count,page),String.valueOf(isSubscribe)); 
    }
    
    //获取推荐视频
    @RequestMapping("/findRecommendVideos")
    @ResponseBody
    public RespBody findRecommendVideos(Integer page, Integer size){
    	String uid = (String)this.request.getHeader("uid");
    	return respBodyWriter.toSuccess(videoService.findRecommendVideos(page, size,uid)); 
    }
    
    //我的视频列表信息
    @RequestMapping("/findMyVideos")
    @ResponseBody
    public RespBody findMyVideos(Integer page, Integer size){
    	String uid = (String)this.request.getHeader("uid");
    	return respBodyWriter.toSuccess(videoService.findMyVideos(page, size, uid)); 
    } 
    
    //最新视频
    @RequestMapping("/findNewVideos")
    @ResponseBody
    public RespBody findNewVideos(int flag,Long startId, Integer count){
    	String uid = (String)this.request.getHeader("uid"); 
    	return respBodyWriter.toSuccess(videoService.findNewVideos(flag==0?null:startId, count, uid)); 
    } 
    
    //按发布时间获取视频，按发布时间倒排序
    @RequestMapping("/findVideosByPublishTime")
    @ResponseBody
    public RespBody findVideosByPublishTime(Long timestamp,Integer count){
    	Date s = new Date();
    	String uid = (String)this.request.getHeader("uid"); 
    	Date d = null;
    	if(timestamp!=null&&timestamp.longValue()!=0){
    		d=new Date(timestamp);
    	}
    	List<Video> ls = videoService.findNewVideos(d, count, (uid==null||"".equals(uid))?"0":uid); 
    	return respBodyWriter.toSuccess(ls); 
    } 
    
    
    //最热视频
    @RequestMapping("/findHotVideos")
    @ResponseBody
    public RespBody findHotVideos(Integer page, Integer size){  
    	String uid = (String)this.request.getHeader("uid");
    	return respBodyWriter.toSuccess(videoService.findHotVideos(page, size,uid)); 
    }
    
    //相关视频
    @RequestMapping("/findRelatedVideos")
    @ResponseBody
    public RespBody findRelatedVideos(Long videoId,Integer page, Integer size){  
    	String uid = (String)this.request.getHeader("uid");
    	logger.info("findRelatedVideos,videoId={},page={},size={}",videoId,page,size);
    	return respBodyWriter.toSuccess(videoService.findRelatedVideos(videoId,page, size,uid)); 
    }

    //根据热度指数（播放指数+点赞指数+评论指数）排序，查询视频
    @RequestMapping("/findHotIndiceVideos")
    @ResponseBody
    public RespBody findHotIndiceVideos(Integer startIndex, Integer count,Long activityId){  
    	String uid = (String)this.request.getHeader("uid");
    	return respBodyWriter.toSuccess(videoService.findHotIndiceVideos(startIndex, count,uid,activityId)); 
    }
    
    //我的他人视频列表信息
    @RequestMapping("/findOtherVideos")
    @ResponseBody
    public RespBody findOtherVideos(Integer page, Integer size,String otherUid){ 
    	String uid = (String)this.request.getHeader("uid");
    	if(otherUid==null||otherUid.trim().equals(""))
    		return respBodyWriter.toError(ResponseCode.CODE_312.toString(),ResponseCode.CODE_312.toCode()); 
    	else 
    		return respBodyWriter.toSuccess(videoService.findOtherVideos(page, size, uid, otherUid)); 
    }
    
    //获取用户视频
    @RequestMapping("/findUserVideos")
    @ResponseBody
    public RespBody findUserVideos(Long timestamp, Integer count,Long userid){ 
    	String uid = (String)this.request.getHeader("uid"); 
    	
    	List<Video> videos=videoService.findUserVideos(userid, 
    			(timestamp==null||timestamp.longValue()==0)?null:new Date(timestamp), 
    			count, StringUtils.hasText(uid)?Long.parseLong(uid):null);
    	
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("videos", videos); 
    	Ruser user = ruserService.find(userid); 
    	
    	if(StringUtils.hasText(uid)&&userid!=null&&userid.toString().equals(uid)){
    		user.setVideoCount(user.getAllVideoCount());
    	}
    	map.put("user", user);  
    	
    	if(uid != null && !"".equals(uid.trim())){
			int isAttention = 0;
    		Map<String, Object> params = new HashMap<String, Object>();
    		params.put("uid", Long.parseLong(uid));
    		params.put("otherUid", userid); 
    		//是否关注
			if (attention.isAttention(params) == 1) {
				params.put("uid", userid);
				params.put("otherUid", Long.parseLong(uid));
				isAttention = 1 + attention.isAttention(params);
			}
        	map.put("attentionAuthor", isAttention);
    	} else {
    		map.put("attentionAuthor", 0);
    	} 
    	if (uid.equals(String.valueOf(userid))) { //用户查询自己的视频列表，返回用户发布和转发的视频数
    		map.put("videoCount",videoService.findUserVideoCount(userid));
    		map.put("forwardCount",forwardService.getForwardNumber(userid));
    	} else {
    		map.put("videoCount",0);
    		map.put("forwardCount",0);
    	}
		return respBodyWriter.toSuccess(map);  
    }
    
    //获取单个视频的基本信息
    @RequestMapping("/findOne")
    @ResponseBody
    public RespBody findOne(Long id,@RequestParam(required=false) String v){ 
    	String uid = (String)this.request.getHeader("uid");
    	Video video = videoService.getVideo(id,uid);
    	if(video==null) {
    		return respBodyWriter.toError(ResponseCode.CODE_332.toString(), ResponseCode.CODE_332.toCode());
    	}
    	if(video!=null&&uid!=null&&!uid.trim().equals("")){
			int isAttention = 0;
			Map<String, Object> params = new HashMap<String, Object>();
    		params.put("uid", Long.parseLong(uid));
    		params.put("otherUid", video.getCreatorId());
			if (attention.isAttention(params) == 1) {
				params.put("uid", video.getCreatorId());
				params.put("otherUid", Long.parseLong(uid));
				isAttention = 1 + attention.isAttention(params);
			}
			video.setAttentionAuthor(isAttention);
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
    		
    		
    		//新版本取视频
    		if (org.apache.commons.lang3.StringUtils.isNotBlank(v) && CollectionUtils.isNotEmpty(tagList)) {
    			video.setTags(tagList);
    		}else if (org.apache.commons.lang3.StringUtils.isBlank(v) && CollectionUtils.isNotEmpty(tagList)) {//老版本取视频
    			String joinTags = org.apache.commons.lang3.StringUtils.join(tagList, " ");
    			video.setTag(joinTags);
    		}
    	}else {
    		try {
//				solrRoomService.addRoom(video.getId(), room.getTitle(), ruser.getId().toString(), ruser.getPic(), playbackUrl, room.getRoomPic());
				solrRoomService.deleteDocByid(id);
    		} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	return respBodyWriter.toSuccess(video);
    }  
    
    //获得我拍秀视频
    @RequestMapping("/findShowVideo")
    @ResponseBody
    public RespBody findShowVideo(Long videoId,String refVideoId,Long timestamp,int count){ 
    	String uid = (String)this.request.getHeader("uid");
    	List<Video> list = videoService.findShowVideo(videoId,refVideoId,uid,timestamp,count);
    	for(Video video:list){
    		video.setPlayRateToday(new BigDecimal(0));
    	}
    	return respBodyWriter.toSuccess(list);   
    } 
    
    @RequestMapping("/incCount")
    @ResponseBody
    public RespBody incCount(Long id,Integer c){
    	String uid = (String)this.request.getHeader("uid");
    	String deviceid = (String)this.request.getHeader("deviceid");
    	videoService.incCount(c, id,deviceid,(uid==null||uid.trim().equals(""))?null:Long.parseLong(uid));
    	return respBodyWriter.toSuccess(videoService.find(id).getPlayCount());   
    } 
     
    @RequestMapping("/deleteVideos")
    @ResponseBody
    public RespBody deleteVideos(Long [] ids){ 
    	String uid = (String)this.request.getHeader("uid"); 
    	if(uid==null||uid.trim().equals("")){
    		return respBodyWriter.toError(ResponseCode.CODE_453.toString(), ResponseCode.CODE_453.toCode());
    	} 
    	videoService.deleteVideos(Arrays.asList(ids),Long.parseLong(uid));
    	return respBodyWriter.toSuccess();   
    } 
    
    @RequestMapping("/multiDeleteVideos")
    @ResponseBody
    public RespBody multiDeleteVideos(String ids){ 
    	String uid = (String)this.request.getHeader("uid"); 
    	logger.info("ids={},multiDeleteVideos",ids);
    	if(uid==null||uid.trim().equals("")){
    		return respBodyWriter.toError(ResponseCode.CODE_453.toString(), ResponseCode.CODE_453.toCode());
    	} 
    	List<Long> idsList = new ArrayList<Long>();
    	if (ids !=null && !"".equals(ids)){
    		String[] idsArray = ids.split(",");
    		for(int i=0;i<idsArray.length;i++){
    			idsList.add(Long.parseLong(idsArray[i]));
    		}
    		videoService.deleteVideos(idsList,Long.parseLong(uid));
    	}
    	return respBodyWriter.toSuccess();   
    } 
    
    @RequestMapping("/saveVideo")
    @ResponseBody
    public RespBody saveVideo(@RequestParam MultipartFile file,Video f){
    	String videoPic = this.uploadVideoPic(file);
    	if("failed".equals(videoPic))
    		return respBodyWriter.toError(ResponseCode.CODE_331.toString(), ResponseCode.CODE_331.toCode());
    	String uid = (String)this.request.getHeader("uid");
    	f.setCreatorId(Long.parseLong(uid));
    	f.setDataFrom(DataFrom.麦视rest接口.getName()); 
    	f.setVideoPic(videoPic);
    	f.setPublishTime(new Date());
    	sensitiveWordService.checkAndReplaceSensitiveWord(f); 
    	if(videoService.saveVideoFromPhone(f))
    		return respBodyWriter.toSuccess(f); 
    	else
    		return respBodyWriter.toError(ResponseCode.CODE_500.toString(), ResponseCode.CODE_500.toCode());
    }
    
    //视频保存第二版，需要裁剪图片
    @RequestMapping("/saveVideoAndPic")
    @ResponseBody
    public RespBody saveVideoAndPic(@RequestParam MultipartFile file,Video f,int x,int y,int width,int height){
    	String videoPic = this.uploadVideoPic(file);
    	if("failed".equals(videoPic))
    		return respBodyWriter.toError(ResponseCode.CODE_331.toString(), ResponseCode.CODE_331.toCode());
    	else{//裁剪视频列表页图片
	    	String videoListPic=videoPic.substring(0,videoPic.indexOf("."))+"_vl"+videoPic.substring(videoPic.indexOf("."));
	    	PictureScaleUtil.region(x, y, width, height, basePath+videoPic, basePath+videoListPic);
	    	f.setVideoListPic(videoListPic);
    	}
    	String uid = (String)this.request.getHeader("uid");
    	f.setCreatorId(Long.parseLong(uid));
    	f.setDataFrom(DataFrom.麦视rest接口.getName());
    	f.setVideoPic(videoPic);
    	f.setPublishTime(new Date());
    	sensitiveWordService.checkAndReplaceSensitiveWord(f);
    	if(videoService.saveVideoFromPhone(f))
    		return respBodyWriter.toSuccess(f); 
    	else
    		return respBodyWriter.toError(ResponseCode.CODE_500.toString(), ResponseCode.CODE_500.toCode());
    }
    
    /**
     * 保存视频数据，主要是因为标签在正文中
     * @param file
     * @param f
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    @RequestMapping("saveVideoAndPicV2")
    @ResponseBody
    public RespBody saveVideoAndPicV2(@RequestParam MultipartFile file,Video f,int x,int y,int width,int height){
    	//如果客户端重复提交保存请求，不进行操作,直接返回已经保存过的视频对象
    	String playKey = f.getPlayKey();
    	if (playKey != null && !"".equals(playKey)){
    		Video video = videoService.getVideo(playKey);
    		if (video !=null)
    			return respBodyWriter.toSuccess(video); 
    	}
    	
    	
    	String videoPic = this.uploadVideoPic(file);
    	if("failed".equals(videoPic))
    		return respBodyWriter.toError(ResponseCode.CODE_331.toString(), ResponseCode.CODE_331.toCode());
    	else{//图片保存成功则裁剪视频列表页图片
	    	String videoListPic=videoPic.substring(0,videoPic.indexOf("."))+"_vl"+videoPic.substring(videoPic.indexOf("."));
	    	PictureScaleUtil.region(x, y, width, height, basePath+videoPic, basePath+videoListPic);
	    	f.setVideoListPic(videoListPic);
    	}
    	String uid = (String)this.request.getHeader("uid");
    	f.setCreatorId(Long.parseLong(uid));
    	f.setDataFrom(DataFrom.麦视rest接口.getName());
    	f.setVideoPic(videoPic);
    	f.setPublishTime(new Date());
    	sensitiveWordService.checkAndReplaceSensitiveWord(f);
    	String content = f.getDescription();
    	Set<String> tags = new HashSet<String>();
    	if (org.apache.commons.lang3.StringUtils.isNotBlank(content)) {
    		Pattern pattern = Pattern.compile("(#([^#]+)#)");
    		Matcher matcher = pattern.matcher(content);
    		while (matcher.find()) {
    			String tag = matcher.group(2);
    			tags.add(tag);
    		}
		}
    	
    	//1.5.4版本之前，视频信息放在尾部，现为了支持mp4边下边播和拖拽，放在头部。所以客户端加传app版本号参数。如何有版本号，认为是以mp4播放。如果没有，认为是老版本视频，仍以m3u8播放。
    	if(f.getVersionNum()!=null&&!f.getVersionNum().equals("")) {
    		f.setMp4Flg(1);
    	}
    	
    	try {
    		if(videoService.saveVideoFromPhoneV2(f,tags)){
        		if (CollectionUtils.isNotEmpty(tags)) {
        			List<String> lTags = new ArrayList<String>(tags.size());
        			lTags.addAll(tags);
    				f.setTags(lTags);
    			}
        		return respBodyWriter.toSuccess(f); 
        	}else{
        		return respBodyWriter.toError(ResponseCode.CODE_500.toString(), ResponseCode.CODE_500.toCode());
        	}
		} catch (Exception e) {
			return respBodyWriter.toError(ResponseCode.CODE_500.toString(), ResponseCode.CODE_500.toCode());
		}
    }
    
    
     
    public String uploadVideoPic(MultipartFile file){ 
    	String relPath = File.separator+"videoPic"+File.separator+DateFormatUtils.format(new Date(), "yyyy-MM-dd")+File.separator;
        String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_")
        +file.getOriginalFilename();  
        try { 
        	FileUtils.copyInputStreamToFile(file.getInputStream(), new File(basePath+relPath, originalFilename));
            return relPath+originalFilename; 
        } catch (IOException e) {
        	logger.error("文件[" + originalFilename + "]上传失败",e);
        	return "failed";
        }     
    }

    // 根据关键词查找视频
//    @RequestMapping("/findKeywordVideo")
//    @ResponseBody
//    public RespBody findByKeyword(String keywords, Integer page, Integer size) {
//        PageImpl<VideoFullText> p = (PageImpl<VideoFullText>) videoService.findKeywordVides(page, size, keywords);
//        List<Video> vl = new ArrayList<Video>();
//        List<VideoFullText> vtl = p.getContent();
//        for (int i = 0; i < vtl.size(); i++) {
//            vl.add(VideoFullText.toVideo(vtl.get(i)));
//        }
//        PageImpl<Video> np = new PageImpl<Video>(vl, new PageRequest(p.getNumber(), p.getSize()), p.getTotalElements());
//        return respBodyWriter.toSuccess(np);
//    }
    
    @RequestMapping("/findKeywordVideo")
    @ResponseBody
    public RespBody findByKeyword(String keywords, Integer page, Integer size) {
    	String uid = (String)this.request.getHeader("uid");
        return respBodyWriter.toSuccess(videoService.findKeywordVides(page, size, keywords, uid));
    }
    
    @RequestMapping("/findVideosByDescriptionSolr")
    @ResponseBody
    public RespBody findVideosByDescriptionSolr(String description,Integer page,Integer size) throws SolrServerException  {
    	String uid = (String)this.request.getHeader("uid");

    	if(description==null||description.equals("")||page==null) {
			return respBodyWriter.toSuccess();
		}
		
		if (description != null && !"".equals(description)) {
			
			List videoIdList=solrVideoService.search(description, page, size);
			if(videoIdList==null||videoIdList.size()==0) {
				return respBodyWriter.toSuccess();
			}
			List videoList=videoService.findVideosByIds(videoIdList,uid);
			List orderVideoList=new ArrayList();
			for(int i=0;i<videoIdList.size();i++) {
				for(int j=0;j<videoList.size();j++) {
					if(((Long)videoIdList.get(i)).longValue()==((Video)videoList.get(j)).getId().longValue()) {
						orderVideoList.add(videoList.get(j));
						break;
					}
				}
				
			}
			return respBodyWriter.toSuccess(orderVideoList);
		} else {
			return respBodyWriter.toSuccess();
		}
		
    }
    
    /**
     * @param tag
     * @param dir 拉取方向0下拉（取最新的）,1上来（取历史的）
     * @param lastid
     * @param row
     * @return
     */
    @RequestMapping("/findKeywordVideoV2")
    @ResponseBody
    public RespBody findByKeywordV2(String tag,@RequestParam(required=false,defaultValue="1") int dir,@RequestParam(required=false,defaultValue="0") long lastid,@RequestParam(required=false,defaultValue="30") int row) {
    	String uid = (String)this.request.getHeader("uid");
    	Map<String, List<Video>> contentMap = new HashMap<String, List<Video>>();
    	try {
			List<Long> videoIdList = solrWoPaiVideoTagService.search(tag, lastid,row);
			if (CollectionUtils.isNotEmpty(videoIdList)) {
				contentMap.put("content",videoService.findVideosByIds(videoIdList, uid));
			}else {
				contentMap.put("content",new ArrayList<Video>(0));
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
			logger.error("video controller findByKeywordV2 error,error msg is {}", e.getMessage());
			contentMap.put("content",new ArrayList<Video>(0));
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//contentMap.put("content", videoService.findVideoByTagName(tag,dir ,row, lastid, uid));
    	return respBodyWriter.toSuccess(contentMap);
    }
    
    
    @RequestMapping("/thirdH5Video") 
    public String thirdVideo(String playKey,String videoId) {
    	Video v=null;
    	if(playKey!=null&&playKey.length()>0){
    		
    		v = videoService.getVideo(playKey);
    	}
    	if(videoId!=null&&videoId.length()>0){
    		v = videoService.find(Long.parseLong(videoId));
    	}
    	if (v != null) {
    		v.setDescription(v.getDescription().replace("'", "\\'"));
    	}
    	this.request.setAttribute("video",v ); 
    	this.request.setAttribute("topHot",videoService.findTopHotVideos(6)); 
        return "thirdplay";
    }
    
    @RequestMapping("/thirdVideo") 
    public String thirdH5Video(String playKey,String uid,String videoId,String activityId,String shareuid,Integer shareType) {
    	if (shareType != null && shareType.intValue() == 1){ //shareType=1,跳转到个人页面
    		logger.info("H5,share,uid={}",uid);
    		return "redirect:/page/user/userDetail?userId="+uid;
    	}
    	if (shareType != null && shareType.intValue() == 2){ //shareType=1,跳转到活动详情页面
    		logger.info("H5,share,activityId={}",activityId);
    		return "redirect:/page/activity/activityIndex?activityId="+activityId;
    	}
    	if (videoId != null && !"".equals(videoId)) {
        	return "redirect:/page/video/videoDetail?videoId="+ videoId; 
    	}
    	if (playKey != null && !"".equals(playKey)) {
    		Video video = videoService.getVideo(playKey);
        	return "redirect:/page/video/videoDetail?videoId="+ (video == null ? 0:video.getId()); 
    	}
    	return "redirect:http://www.wopaitv.com";
    	
    }
    
    
    //根据热度指数（播放指数+点赞指数+评论指数+时间指数）排序，查询视频
    @RequestMapping("/findToHead")
    public String findToHead(Integer startId){  
    	String uid = (String)this.request.getHeader("uid");
    	Integer count=3;
    	long activityId=0;
    	List<Video> videolist = videoService.findHotIndiceVideos(startId,count,uid,activityId);
    	this.request.setAttribute("videolist",videolist); 
    	return "h5/index"; 
    }
    
    
    @RequestMapping("/praiseUserList")
    @ResponseBody
    public RespBody praiseUserList(Long vid, Long minPraiseId, Integer count) {
    	String uid = (String)this.request.getHeader("uid");
    	List<Map<String,Object>> ls= videoService.getVideoPraiseUserList(vid, minPraiseId, count,(uid==null||uid.trim().equals(""))?null:Long.parseLong(uid));
        return respBodyWriter.toSuccess(ls);
    }
    /**
     * 邀请跳转页面
     * @param inviteId 邀请人id
     * @return
     */
    @RequestMapping("/inviteFriendVideo") 
    public String inviteFriendVideo(String inviteId) {
    	List<Video> ls=null;
 //   	List<Video> wopaiVideols=null;
    	if(inviteId!=null&&!inviteId.isEmpty()){
    		ls =videoService.getNewVideoByUid(Long.parseLong(inviteId),1);
    		Ruser user = ruserService.find(Long.parseLong(inviteId)); 
    		this.request.setAttribute("user", user);
    		if(ls!=null&&ls.size()>0){
    			Video video=ls.get(0);
    			video.setUser(user);
    			this.request.setAttribute("video",video ); 
    		}else{
    			this.request.setAttribute("video",null ); 
    		}
    	}else{
    		this.request.setAttribute("video",null ); 
    	}
    	
 //   	String officialUserId = jedisService.get(BicycleConstants.OFFICICAL_USER_ID);
 //   	if(officialUserId!=null&&!officialUserId.isEmpty()){
 //   		wopaiVideols =videoService.getNewVideoByUid(Long.parseLong(officialUserId),6);
 //   		if(wopaiVideols!=null&&wopaiVideols.size()==6){
//    			this.request.setAttribute("topHot",wopaiVideols); 
//    		}else if(officialUserId!=null&&wopaiVideols.size()<6){
//    			wopaiVideols.addAll(videoService.findTopHotVideos(6-wopaiVideols.size()));
//    			this.request.setAttribute("topHot",wopaiVideols); 
//    		}else{
    			this.request.setAttribute("topHot",videoService.findTopHotVideos(6)); 
//    		}
//    	}else{
//    		this.request.setAttribute("topHot",videoService.findTopHotVideos(6)); 
//    	}
        return "invitethirdplay";
    }
    
    /**
     * 客户端获取可用的host
     */
    @RequestMapping("/loadconfigurl")
    @ResponseBody
    public RespBody loadConfigUrl(String  clientPf){
    	if(clientPf!=null&&!clientPf.isEmpty()){
    		List<LoadConfigUrlVO> loadConfigUrlList = loadConfigUrlService.findLoadConfigUrlByClientPf(clientPf);
    		if(loadConfigUrlList!=null&& loadConfigUrlList.size()>0){
    			return respBodyWriter.toSuccess(loadConfigUrlList);
    		}
    		return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_500.toCode());
    	}
    	return respBodyWriter.toError(ResponseCode.CODE_500.toString(), ResponseCode.CODE_500.toCode());
    }
    
    /**
     * 客户端获取可用的host，兼容1.5.2版-url一期
     */
    @RequestMapping("/loadvideourl")
    @ResponseBody
    public RespBody loadvideourl(String  clientPf){
    	if(clientPf!=null&&!clientPf.isEmpty()){
    		List<LoadConfigUrlVO> loadConfigUrlList = loadConfigUrlService.findLoadConfigUrlByClientPf(clientPf);
    		if(loadConfigUrlList!=null&& loadConfigUrlList.size()>0){
    			for(LoadConfigUrlVO lcuv:loadConfigUrlList){
    				if(lcuv.getType().equals(BicycleConstants.URL_CONFIG_VIDEO_CDN)){
    					
    					return respBodyWriter.toSuccess(lcuv.getUrl());
    				}
    			}
    		}
    		return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_500.toCode());
    	}
    	return respBodyWriter.toError(ResponseCode.CODE_500.toString(), ResponseCode.CODE_500.toCode());
    }
    
    /**
     * @param isLiveNotice 是否为直播预告，1：是，0：否(普通图片)
     * @param description
     * @param time
     * @param pic
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"/savePic"}, method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    @Transactional
    public RespBody savePic(@RequestParam(value = "isLiveNotice", required = true)int isLiveNotice,@RequestParam(value = "description", required = true)String description,
							    		@RequestParam(value = "time", required = false)String time,
							    		@RequestParam(value = "pic", required = true)MultipartFile pic) throws ParseException{ 
    	String uid = this.request.getHeader("uid");
    	
    	
    	if (uid!=null&&!uid.equals("")) {
    		Ruser rus=ruserService.find(Long.valueOf(uid));
    		if(rus==null) {
    			return respBodyWriter.toError("用户不存在", 204);
    		}
    	} else {
    		return respBodyWriter.toError("用户uid为空", 203);
    	}
    	
    	//上传图片
    	String fileName = pic.getOriginalFilename();
 		String sufix = fileName.substring(fileName.lastIndexOf("."));
 		String relPath = File.separator + (isLiveNotice==1?"liveNotice":"videoPic")+File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
 		String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + sufix;
		String relUrl = "";
		try {
			FileUtils.copyInputStreamToFile(pic.getInputStream(), new File(basePath+relPath, originalFilename));
			relUrl = relPath + originalFilename;
		} catch (IOException e) {
			logger.error("文件[" + originalFilename + "]上传失败",e);
		}
    	
		//保存图片到video表
		Video video = new Video();
		video.setCreatorId(Long.parseLong(uid));
		video.setCreateDate(new Date());
		video.setPublishTime(new Date());
		video.setDataFrom("restwww");
		video.setVideoPic(relUrl);
		video.setPlayKey("");
		video.setDescription(description);
		video.setType(3);
		
    	if (isLiveNotice == 1) { //如果为直播预告类型，保存直播预告
    		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date publishTime=formatter.parse(time);
            
            SimpleDateFormat formatter2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            Date curretDate=new Date();
            long l =  publishTime.getTime()-curretDate.getTime();
            long d = l/(1000*60);//当前时间与发布时间相差的小时数(20160323改为一分钟 毕)
            if(d-1<0) {//当前时间距发布时间不到一小时（分钟），则返回错误信息
//            	return respBodyWriter.toError("当前时间距发布时间不到一分钟，现服务器时间为: "+currDateStr+",  请重新选择发布时间", 201);
            	return respBodyWriter.toError("请选择在  "+formatter.format(new Date(curretDate.getTime()+10*60*1000))+"  以后的时间", 201);

            }
            LiveNotice ln=new LiveNotice();
            Date cDate=new Date();
            ln.setDescription(description);
            ln.setCreateDate(cDate);
            ln.setModifyDate(cDate);
            ln.setShowTime(publishTime);
            ln.setCreatorId(Long.valueOf(uid));
    		ln.setPic(relUrl);
    		ln.setStatus(0);  //默认直播预告不可用，待图片审核通过后改变状态为可用
    		liveNoticeService.save(ln);
    		
    		if (ln.getId() != null && ln.getId().intValue() > 0) {  //直播预告保存成功，将直播预告id存到video
    			video.setType(4);//如果是预告的话，更改video的类型
    			video.setLiveNoticeId(ln.getId());
    		}
    	}
		videoService.save(video);
		
		String content = description;
    	Set<String> tags = new HashSet<String>();
    	if (org.apache.commons.lang3.StringUtils.isNotBlank(content)) {
    		Pattern pattern = Pattern.compile("(#([^#]+)#)");
    		Matcher matcher = pattern.matcher(content);
    		while (matcher.find()) {
    			String tag = matcher.group(2);
    			tags.add(tag);
    		}
		}
    	
		for(String _tag : tags){
			
			
			Long nCount=labelRepository.findByName(_tag);
			
			Label label = new Label();
			label.setName(_tag);
			label.setNum(0L);
			
			if(nCount.intValue()==0) {
				
				
				labelRepository.save(label);
				
				try {
					solrWoPaiTagService.index(label.getId(), label.getName(),label.getNum());
				} catch (SolrServerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
				
			}else {
				List labelList=labelRepository.findLabelIdByName(_tag);
				if(labelList!=null&&labelList.size()>0) {
					label.setId((Long) labelList.get(0));
				}
			}
			
			try {
				solrWoPaiLiveTagService.index(label.getId()+"_"+video.getId(), _tag, video.getId(),0);
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		}
		return respBodyWriter.toSuccess(video);
        
    }

	/**
	 * 获取搜索页热门视频列表 老版本热门分类
	 * @param timestamp 时间戳
	 * @param count 行数
	 * @param page 页码
	 * @param
	 */
	@RequestMapping("/findHotVideoList")
	@ResponseBody
	public RespBody findHotVideoList(Long timestamp,Integer count,Integer page){
		String uid = (String)this.request.getHeader("uid");
		if (!jedisService.keyExists(BicycleConstants.HOT_CATEGORY_ID)){
			jedisService.set(BicycleConstants.HOT_CATEGORY_ID,"147");//正式环境热门活动ID
		}
		String hotActivityId = jedisService.get(BicycleConstants.HOT_CATEGORY_ID);
		if(hotActivityId == "" || hotActivityId == null){
			hotActivityId = "147";//正式环境热门活动ID
		}
		return respBodyWriter.toSuccess(videoService.findActVideos(timestamp, Long.valueOf(hotActivityId), uid, count,page));
	}

    /**
     * 最热视频列表3.0版
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("findHotVideoV3")
    @ResponseBody
    public RespBody findHotVideo_V3(Integer page,Integer size){
    	Integer total = 0;
    	Integer start = 0; 	//视频起始位置
    	Integer end = 0;	//查询视频数
    	Integer currentPsize = 0;//当前页推荐位个数
    	List<Map<String,Object>> psList = recommandPositionService.findCurrentPagePositions(page);
    	if(psList!=null && psList.size()>0){
    		currentPsize = psList.size();
    	}
    	
    	if(page==1){
    		start = 0;
    	} else {
    		Integer prePsize = recommandPositionService.findPrePagePositionCount(page);//当前页之前的推荐位总数
    		start = (page-1)*size - prePsize;    		
    	}
    	end = size - currentPsize;
    	
    	List<Map<String,Object>> list = videoService.findHotVideosV3(start, end);
    	
    	Map<String,Object> result = new HashMap<String,Object>();
    	
    	result.put("videoList", list);
    	result.put("positionList", psList);
    	    	
    	return this.respBodyWriter.toSuccess("success", result, String.valueOf(total));
    }
    
    @RequestMapping("getSolrRoomAndVideoList")
    @ResponseBody
    public RespBody getSolrRoomAndVideoList(Integer page, Integer size, String title) {
    	String uid = (String)this.request.getHeader("uid");
		if(page==null) {
			page=1;
		}
		if(size==null) {
			size=10;
		}
		if(title==null) {
			title="";
		}
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		Set<String> roomIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER, 0, 10000);
		for (String roomId : roomIds) {  
			try {
				Map<String,String> room = jedisService.getMapByKey(BicycleConstants.ROOM_+ roomId);
				if (room != null && room.size() > 0 && room.get("id") != null) {
					if(room.get("title")!=null&&!room.get("title").equals("")&&room.get("title").indexOf(title)>=0) {
						//计算直播时长
						Long duration = System.currentTimeMillis() - Long.parseLong(room.get("createDate")); 
						room.put("duration", String.valueOf(duration < 0?0:duration));
						//查询用户信息
						Ruser ruser = ruserService.find(Long.parseLong(room.get("creatorId")));
						room.put("anchorName", ruser.getName());
						room.put("anchorPic", ruser.getPic());
						room.put("anchorVstat", String.valueOf(ruser.getVipStat()));
						room.put("anchorSignature", ruser.getSignature());
						room.put("type","1");
						
						list.add(room);
					}
					
				}
			} catch (Exception e) {
				logger.error("getRoomList error:roomId is " + roomId);
			}
		}
		
		Collections.sort(list, new Comparator<Map<String,String>>(){

			@Override
			public int compare(Map<String, String> o1, Map<String, String> o2) {
				String createTime1 = o1.get("createDate");
				if(!org.apache.commons.lang3.StringUtils.isNumeric(createTime1)){
					return -1;
				}
				String createTime2 = o2.get("createDate");
				if(!org.apache.commons.lang3.StringUtils.isNumeric(createTime2)){
					return 0;
				}
				Long create1 = Long.parseLong(createTime1);
				Long create2 = Long.parseLong(createTime2);
				
				return create2.compareTo(create1);
			}
			
		});
		
		List roomList=new ArrayList();
		List backList=new ArrayList();
		
		if(list.size()>=(page-1)*size+size) {
			roomList=list.subList((page-1)*size, (page-1)*size+size);
		}else if(list.size()>=(page-1)*size&&list.size()<(page-1)*size+size) {
			roomList=list.subList((page-1)*size, list.size());
			
			
			try {
				backList = solrWoPaiLiveTagService.search(title, 0, (page-1)*size+size-list.size());
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
			
			if(BicycleConstants.VIDEO_BACK_FLG) {
				
			}else {
				
			}
			
//			roomList.add(rList);
			
		}else if(list.size()<(page-1)*size){
			try {
				backList=solrWoPaiLiveTagService.search(title, (page-1)*size-list.size(), size);
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
			
			if(BicycleConstants.VIDEO_BACK_FLG) {
				
			}else {
				
			}
			
//			roomList.add(rList);
		}
		
		Map map=new HashMap();
		
		map.put("room", roomList);
		
//		for (Object obj : backList) {  
//			HashMap roomMap=(HashMap) obj;
//			//查询用户信息
//			Ruser ruser = ruserService.find( (Long) roomMap.get("anchorId"));
//			roomMap.put("anchorName", ruser.getName());
//			roomMap.put("anchorPic", ruser.getPic());
//			roomMap.put("anchorVstat", String.valueOf(ruser.getVipStat()));
//			roomMap.put("anchorSignature", ruser.getSignature());
//			roomMap.put("type","2");
//			roomMap.put("anchorSex", ruser.getSex());
//			
//		}
		
		List orderVideoList=new ArrayList();
		
		if(backList!=null&&backList.size()>0) {
			List videoList=videoService.findVideosByIds(backList,uid);
			
			if(backList!=null&&backList.size()>0) {
				
				for(int i=0;i<backList.size();i++) {
					for(int j=0;j<videoList.size();j++) {
						if(((Long)backList.get(i)).longValue()==((Video)videoList.get(j)).getId().longValue()) {
							orderVideoList.add(videoList.get(j));
							break;
						}
					}
					
				}
			}
		}
		
		
					
		map.put("back", orderVideoList);
		
		return this.respBodyWriter.toSuccess("success", map);
//		return map;
	}
    @RequestMapping("cdn")
    @ResponseBody
    public RespBody cdn(String cdn){
//    	jedisService.set(BicycleConstants.CDN_NAME, cdn);
    	return this.respBodyWriter.toSuccess(cdn);
    }

}
