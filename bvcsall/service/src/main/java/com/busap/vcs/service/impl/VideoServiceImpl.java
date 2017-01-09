package com.busap.vcs.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.*;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;

import com.busap.vcs.base.Action;
import com.busap.vcs.base.Filter;
import com.busap.vcs.base.Message;
import com.busap.vcs.base.Module;
import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.OrderByObject;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.enums.VideoStatus;
import com.busap.vcs.data.mapper.AdminVideoMapper;
import com.busap.vcs.data.mapper.ForwardDAO;
import com.busap.vcs.data.mapper.LabelVideoDAO;
import com.busap.vcs.data.mapper.RuserDAO;
import com.busap.vcs.data.mapper.VideoDAO;
import com.busap.vcs.data.model.ExportWopaiNormalUser;
import com.busap.vcs.data.repository.ActivityVideoRepository;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.FavoriteRepository;
import com.busap.vcs.data.repository.LabelRepository;
import com.busap.vcs.data.repository.LabelVideoRepository;
import com.busap.vcs.data.repository.PlayHistoryRepository;
import com.busap.vcs.data.repository.PraiseRepository;
import com.busap.vcs.data.repository.ShowVideoRepository;
import com.busap.vcs.data.repository.VideoRepository;
import com.busap.vcs.data.vo.VideoVO;
import com.busap.vcs.service.ActivityService;
import com.busap.vcs.service.AttentionService;
import com.busap.vcs.service.Constants;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LabelService;
import com.busap.vcs.service.LiveNoticeService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.SystemMessService;
import com.busap.vcs.service.VideoService;
import com.busap.vcs.service.kafka.producer.KafkaProducer;
import com.busap.vcs.util.FileUtils;

@Service("videoService")
public class VideoServiceImpl extends BaseServiceImpl<Video, Long> implements
		VideoService {
	
	private Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);

	@Resource(name="videoRepository")
	private VideoRepository videoRepository;

//    @Resource(name="videoFullTextRepository")
//    private VideoFullTextRepository videoFullTextRepository;
	
	@Autowired
    KafkaProducer kafkaProducer;

	@Resource(name="playHistoryRepository")
	private PlayHistoryRepository playHistoryRepository;

	@Resource(name="favoriteRepository")
	private FavoriteRepository favoriteRepository;

	@Resource(name="praiseRepository")
	private PraiseRepository praiseRepository;
 

	@Resource(name="activityService")
    ActivityService activityService;
	
	@Resource(name = "solrVideoService")
	SolrVideoService solrVideoService;
  
	@Resource(name = "showVideoRepository")
	private ShowVideoRepository showVideoRepository;
	
	@Resource
    private LiveNoticeService liveNoticeService;

//	@Resource(name="tagService")
//	private TagService tagService;
	
	@Resource(name = "ruserService")
	RuserService ruserService;
	
	@Autowired
	private RuserDAO ruserDAO;
	
	@Autowired
	private ForwardDAO forwardDAO;
	
	@Autowired
	private LabelVideoDAO labelVideoDAO;
	
	@Resource(name="activityVideoRepository")
	private ActivityVideoRepository activityVideoRepository;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Autowired
	private VideoDAO videoDao;
	
	@Resource(name = "systemMessService")
	private SystemMessService systemMessService;
	
	@Autowired
	private AdminVideoMapper mapper;
	
	@Resource(name="attentionService")
	private AttentionService attentionService;

	@Resource
	private AdminVideoMapper adminVideoMapper;
	
	@Autowired
	private SolrWoPaiTagService solrWoPaiTagService;
	
	@Autowired
	private SolrWoPaiVideoTagService solrWoPaiVideoTagService;
	
	public void setMapper(AdminVideoMapper mapper) {
		this.mapper = mapper;
	}

	@Resource(name="videoRepository")
	@Override
	public void setBaseRepository(BaseRepository<Video, Long> baseRepository) {
		super.setBaseRepository(videoRepository);
	}

	@Resource(name="labelService")
	private LabelService labelService;
	
	
	@Resource(name="labelRepository")
	private LabelRepository labelRepository;
	@Resource(name="labelVideoRepository")
	private LabelVideoRepository labelVideoRepository;
	
	@Value("${video_download_url_prefix}")
	private String video_download_url_prefix;
	
	@Resource(name = "solrWoPaiLiveTagService")
	private SolrWoPaiLiveTagService solrWoPaiLiveTagService;
	
	
	
	public ActivityVideoRepository getActivityVideoRepository() {
		return activityVideoRepository;
	}

	//获取单一视频
	public Video getVideo(Long id, String uid) {
//		Video video = videoRepository.findOne(id); 
//		if(video==null||VideoStatus.已删除.getName().equals(video.getFlowStat())
//				||(DataFrom.麦视网站后台.getName().equals(video.getDataFrom())&&!VideoStatus.已发布.getName().equals(video.getFlowStat())))
//			return null;  
		Video video = videoRepository.getOneVideo(id);
		if(video!=null&&uid!=null&&!uid.trim().equals("")){
			video.setPraise(this.praised(Long.parseLong(uid), id));
			video.setFavorite(this.favorited(Long.parseLong(uid), id));
		} 
		return video;
	}
	@Override
	public Page<Video> getVideos(String ...playKeys) {
		List<Filter> filters = new ArrayList<Filter>(playKeys.length);
		filters.add(Filter.in("playKey", Arrays.asList(playKeys)));
		Pageable pr = new PageRequest(1, 12);
		Page<Video> result=videoRepository.findAll(pr, filters);
		return result;
	}
	
	//增加视频播放次数
	public void incCount(Integer c,Long id,String deviceid,Long uid) { 
		videoRepository.incPlayCount(c, id); 
		PlayHistory ph =new PlayHistory();
		ph.setCreatorId(uid);
		ph.setCount(c);
		ph.setVideoId(id);
		ph.setDeviceId(deviceid);
		playHistoryRepository.save(ph);
		
		//增加视频播放次数后重新计算视频热度
		updateHotPointByVideoId(id);
		//重新计算当日视频热度
		this.excuteVideoDayHotValue(id);
	}
	
	//增加视频播放次数
	public void incCount(Integer c,Long id) { 
		videoRepository.incPlayCount(c, id);
	}
	
	public void updateUserVideoCount(int c,Long uid){
		videoRepository.updateUserVideoCount(c, uid);
	}

	public Video getVideo(String playKey){
		Video v = videoRepository.findByPlayKey(playKey); 
		if(v!=null)
			videoRepository.incPlayCount(1, v.getId());
		else
			return null;
		if(v.getCreatorId()!=null)
			v.setUser(ruserService.find(v.getCreatorId()));
		return v;
	}

	public Page<Video> findBySubjectId(Integer page, Integer size, String subjectId,String uid){
		List<Filter> filters = new ArrayList<Filter>(1);
//    	List<Long> l = subjectService.getSubjectVideoIds(Long.parseLong(subjectId));
//    	filters.add(Filter.in("id", l));
    	filters.add(new Filter("flowStat", VideoStatus.已发布.getName()));
    	Pageable pr = new PageRequest(page, size);
    	Page<Video> p = this.findAll(pr , filters);
    	List<Video> ls= p.getContent();
    	for(Video video:ls){ 
    		video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
    		video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
    	}
    	return p;
	}

	public Page<Video> findByActivityId(Integer page,Integer size, long actId,String uid){
    	Pageable pr = new PageRequest(page, size);
    	List<Video> ls= videoRepository.getActivityVideo(actId,((page-1L)*size),size); 
    	for(Video video:ls){
    		if(uid!=null&&!uid.trim().equals("")){
	    		video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
	    		video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
    		}
    		
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
				video.setTags(tagList);
			}
			
			
    	}
    	Page<Video> p = new PageImpl<Video>(ls,pr,videoRepository.countActivityVideo(actId));
    	return p;
	} 
	
	
	public List<Video> findActVideos(Long timestamp,Long actId, String uid,Integer count,Integer page){
		List<Video> vList = null;
		Map<String,Object> params = new HashMap<String,Object>(); 
		params.put("timestamp", (timestamp==null||timestamp.longValue()<=0)?null:new Date(timestamp));
		params.put("actId", actId);
		params.put("uid", uid);
		params.put("count", count); 
		if (page != null){ //如果page不为空，为新接口调用，按照视频权重排序，按照page翻页
			params.put("pageStart", (page-1)*count);
			params.put("pageSize", count);
			vList = this.videoDao.findActVideos(params);
		}else { //page为空，为老接口调用，按照视频发布时间排序，按照timestamp排序
			vList = this.videoDao.findActVideosByTimestamp(params);
		}
		Activity activity = activityService.find(actId);
		
		for (Video video : vList) {
			if(uid!=null&&!uid.trim().equals("")){
	    		video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
	    		video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
	    		video.setAttentionAuthor(attentionService.isAttention(Long.parseLong(uid), video.getCreatorId()));
    		}
			video.setActivityTitle(activity==null?"":activity.getTitle());
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
				video.setTags(tagList);
			}
		}
		return vList;
	}
	public List<Video> findActVideosAfterAssignedTime(Long timestamp,Long actId, Integer count){
		List<Video> vList = null;
		Map<String,Object> params = new HashMap<String,Object>(); 
		params.put("timestamp", (timestamp==null||timestamp.longValue()<=0)?null:new Date(timestamp));
		params.put("actId", actId);
		params.put("count", count); 
		vList = this.videoDao.findActVideosAfterAssignedTime(params);
		
		return vList;
	}
	
	public Page<Video> findByActivityId(Integer page, Integer size, Long actId,Direction direction,String sortField,Date beginTime){
		List<Filter> filters = new ArrayList<Filter>(1);
		if(actId!=null){
			List<Long> l = activityService.getActVideoIds(actId);
			if(l!=null&&!l.isEmpty()){
				filters.add(Filter.in("id", l));
			}else{
				return new PageImpl<Video>(new ArrayList<Video>());
			}
		}
		if(beginTime!=null){
			filters.add(Filter.ge("createDate", beginTime));
		}
//    	filters.add(new Filter("flowStat", VideoStatus.已发布.getName())); 
		filters.add(Filter.ne("flowStat", VideoStatus.已删除.getName()));
		Pageable pr = new PageRequest(page,size,direction,sortField);
		Page<Video> p = this.findAll(pr , filters);
		return p;
	}

	public Page<Video> findRecommendVideos(Integer page, Integer size, String uid){
		Sort s = new Sort(Direction.DESC,"createDate");
		List<Filter> filters = new ArrayList<Filter>(1);
    	filters.add(new Filter("flowStat", VideoStatus.已发布.getName()));
    	Pageable pr = new PageRequest(page, size,s);
    	Page<Video> p = this.findAll(pr , filters);
    	List<Video> ls= p.getContent();
    	for(Video video:ls){ 
    		video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
    		video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
    		
    		List<Filter> _filters = new ArrayList<Filter>();
    		_filters.add(Filter.eq("videoId", video.getId()));
			Order order = new Order(Direction.ASC,"id");
			Sort sort = new Sort(order);
			List<LabelVideo> lvs = labelVideoRepository.findAll(_filters, sort);
			List<String> tagList = null;
			if (CollectionUtils.isNotEmpty(lvs)) {
				tagList = new ArrayList<String>(lvs.size());
				for(LabelVideo lv : lvs){
					tagList.add(lv.getLabelName());
				}
				video.setTags(tagList);
			}
    		
    	}
    	return p;
	}


	public Page<Video> findMyVideos(Integer page, Integer size, String creator){ 
    	Pageable pageable = new PageRequest(page-1, size); 
    	//videoRepository.findUserVideos(Long.parseLong(creator), pageable);
    	Page<Video> p =videoRepository.findMyVideos(Long.parseLong(creator), pageable);
    	List<Video> ls= p.getContent();
    	for(Video video:ls){
    		video.setPraise(this.praised(video.getCreatorId(), video.getId()));
    		video.setFavorite(this.favorited(video.getCreatorId(), video.getId()));
    		
    		List<Filter> _filters = new ArrayList<Filter>();
    		_filters.add(Filter.eq("videoId", video.getId()));
			Order order = new Order(Direction.ASC,"id");
			Sort sort = new Sort(order);
			List<LabelVideo> lvs = labelVideoRepository.findAll(_filters, sort);
			List<String> tagList = null;
			if (CollectionUtils.isNotEmpty(lvs)) {
				tagList = new ArrayList<String>(lvs.size());
				for(LabelVideo lv : lvs){
					tagList.add(lv.getLabelName());
				}
				video.setTags(tagList);
			}
    		
    	}
    	return p;
	}

	public Page<Video> findNewVideos(Integer page, Integer size){
		List<Filter> filters = new ArrayList<Filter>(1);
    	filters.add(Filter.ne("flowStat", VideoStatus.已删除.getName()));
    	Pageable pr = new PageRequest(page, size,new Sort(Direction.DESC,"createDate"));
    	Page<Video> p = this.findAll(pr , filters);
//    	List<Video> ls= p.getContent();
//    	for(Video video:ls){  
////    		video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
////    		video.setFavorite(this.favorited(Long.parseLong(uid), video.getId())); 
//    	}
    	return p;
	}
	
	public List<Video> findNewVideos(Integer start, Integer length, List<Filter> filters, Sort sort,String uid){
    	filters.add(Filter.ne("flowStat", VideoStatus.已删除.getName()));
		List<Video> ls=this.findAll(start, length, filters, sort);   
    	for(Video video:ls){ 
    		if(uid!=null&&!uid.trim().equals("")){
	    		video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
	    		video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
    		}
    	}
    	return ls;
	}
	
	public List<Video> findNewVideos(Long maxId, Integer length,String uid){ 
		List<Video> ls=null;
		if(maxId!=null&&maxId.longValue()>0)
			ls=videoRepository.findNewVideosHistory(maxId, length);
		else
			ls=videoRepository.findNewVideosFirst(length);
    	for(Video video:ls){ 
    		if(uid!=null&&!uid.trim().equals("")){
	    		video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
	    		video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
	    		video.setAttentionAuthor(attentionService.isAttention(Long.parseLong(uid), video.getCreatorId()));
    		}
    		
    		List<Filter> _filters = new ArrayList<Filter>();
    		_filters.add(Filter.eq("videoId", video.getId()));
			Order order = new Order(Direction.ASC,"id");
			Sort sort = new Sort(order);
			List<LabelVideo> lvs = labelVideoRepository.findAll(_filters, sort);
			List<String> tagList = null;
			if (CollectionUtils.isNotEmpty(lvs)) {
				tagList = new ArrayList<String>(lvs.size());
				for(LabelVideo lv : lvs){
					tagList.add(lv.getLabelName());
				}
				video.setTags(tagList);
			}
    		
    	}
    	return ls;
	}
	
	public List<Video> findVideosWithouActivity(Long maxId, Integer length){ 
		List<Video> ls=null;
		if(maxId!=null&&maxId.longValue()>0)
			ls=videoRepository.findVideosWithouActivityHistory(maxId, length);
		else
			ls=videoRepository.findVideosWithouActivityFirst(length);
    	return ls;
	}
	
	public List<Video> findNewVideos(Date timestamp,Integer count,String uid){ 
//		Date s = new Date();
		List<Video> ls=null;
		Map<String,Object> params=new HashMap<String,Object>(); 
		params.put("count", count);
		params.put("timestamp", timestamp);
		params.put("uid", uid);
		ls=videoDao.findNewVideos(params);
//		if(logger.isInfoEnabled()){
//			logger.info("查询最新视频,获取video,耗时，time="+(new Date().getTime()-s.getTime())+"毫秒");
//		}
		for(Video video:ls){ 
    		if(uid!=null&&!uid.trim().equals("")){
	    		video.setAttentionAuthor(attentionService.isAttention(Long.parseLong(uid), video.getCreatorId()));
    		}
    		
    		List<Filter> _filters = new ArrayList<Filter>();
    		_filters.add(Filter.eq("videoId", video.getId()));
			Order order = new Order(Direction.ASC,"id");
			Sort sort = new Sort(order);
			List<LabelVideo> lvs = labelVideoRepository.findAll(_filters, sort);
			List<String> tagList = null;
			if (CollectionUtils.isNotEmpty(lvs)) {
				tagList = new ArrayList<String>(lvs.size());
				for(LabelVideo lv : lvs){
					tagList.add(lv.getLabelName());
				}
				video.setTags(tagList);
			}
			
    	}
    	return ls;
	}
	
	public Page<Video> findAttentionNewVideos(Collection ids, Integer page, Integer size){ 
		List<Video> ls=null;
		Pageable pageable = new PageRequest(page-1, size,new Sort(Direction.DESC,"createDate"));
		Page<Video> p= videoRepository.findAttentionNewVideos(ids, pageable);
		ls= p.getContent();
		for(Video video:ls){ 
			video.setPraise(this.praised(video.getCreatorId(), video.getId()));
			video.setFavorite(this.favorited(video.getCreatorId(), video.getId()));
		}
		return p;
	}
	
	public List<Video> findAttentionNewVideos(Long uid,List<Long> ids,Date timestamp,Integer count){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ids", ids);
		params.put("timestamp", timestamp);
		params.put("count", count);
		params.put("uid", uid);
		
		List<Video> list = videoDao.findAttentionNewVideosAndForward(params); 
		
		for (Video video:list){
			if (video.getForwardUserId()>0) {
				video.setForwardUser(ruserService.find(video.getForwardUserId()));
			}
		}
		
		return list;
	}
	
	public List<Video> getAttentionInfoList(Long uid,List<Long> ids,Date timestamp,Integer count,Integer type){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ids", ids);
		params.put("timestamp", timestamp);
		params.put("count", count);
		params.put("uid", uid);
		if(type==null) {
			if(BicycleConstants.VIDEO_BACK_FLG) {
				
			}else {
				//type=134;
			}
		}
		params.put("type", type);
		
		List<Video> list = videoDao.getAttentionInfoList(params); 
		
		for (Video video:list){
			if (video.getForwardUserId()>0) {
				video.setForwardUser(ruserService.find(video.getForwardUserId()));
			}
		}
		
		return list;
	}
	
	public List<Map<String,String>> getAttentionLiveInfoList(List<Long> ids,Integer count,Integer start){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ids", ids);
		params.put("count", count);
		params.put("start", start);
		
		
		Set attentionIdSet=new HashSet();
		attentionIdSet.addAll(ids);
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		Set<String> roomIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER, 0, 10000);
		int cnt=0;
		int flg=0;
		for (String roomId : roomIds) {  
				try {
					Map<String,String> room = jedisService.getMapByKey(BicycleConstants.ROOM_+ roomId);
					if (room != null && room.size() > 0 && room.get("id") != null) {
						if(attentionIdSet.contains(Long.parseLong(room.get("creatorId")))) {
							if(cnt>=start&&cnt<(start+count)) {
								//计算直播时长
								Long duration = System.currentTimeMillis() - Long.parseLong(room.get("createDate")); 
								room.put("duration", String.valueOf(duration < 0?0:duration));
								//查询用户信息
								Ruser ruser = ruserService.find(Long.parseLong(room.get("creatorId")));
								room.put("anchorName", ruser.getName());
								room.put("anchorPic", ruser.getPic());
								room.put("anchorVstat", String.valueOf(ruser.getVipStat()));
								room.put("anchorSignature", ruser.getSignature());
								room.put("anchorSex", ruser.getSex());
	//							room.put("type","1");
								room.put("status","1");
								
								list.add(room);
								flg++;
								if(flg>=count) {
									break;
								}
							}
							cnt++;
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
		
		
		
		
//		List<Room> list = videoDao.getAttentionLiveInfoList(params); 
//		
//		for (Room room : list) {  
//			
//			Map<String,String> roomMap = jedisService.getMapByKey(BicycleConstants.ROOM_+ room.getId());
//			if (roomMap != null && roomMap.size() > 0 && roomMap.get("id") != null) {
//				//计算直播时长
//				Long duration = System.currentTimeMillis() - Long.parseLong(roomMap.get("createDate")); 
//				room.setDuration(duration);
//				room.setMaxAccessNumber(Integer.valueOf(roomMap.get("maxAccessNumber")));
//				room.setPraiseNumber(Integer.valueOf(roomMap.get("praiseNumber")));
//			}
//			//查询用户信息
//			Ruser ruser = ruserService.find( room.getCreatorId());
//			
//			room.setAnchorName(ruser.getName());
//			room.setAnchorPic(ruser.getPic());
//			room.setAnchorVstat(ruser.getVipStat().toString());
//			room.setAnchorSignature(ruser.getSignature());
//			room.setStatus(1);
//		}
		
		
		return list;
	}
	
	public List<Video> findOtherAttentionNewVideos(Long uid,List<Long> ids,Date timestamp,Integer count){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ids", ids);
		params.put("timestamp", timestamp);
		params.put("count", count);
		params.put("uid", uid);
		List<Video> vList = videoDao.findOtherAttentionNewVideos(params);
		
		for(Video video : vList){
			List<Filter> _filters = new ArrayList<Filter>();
    		_filters.add(Filter.eq("videoId", video.getId()));
			Order order = new Order(Direction.ASC,"id");
			Sort sort = new Sort(order);
			List<LabelVideo> lvs = labelVideoRepository.findAll(_filters, sort);
			List<String> tagList = null;
			if (CollectionUtils.isNotEmpty(lvs)) {
				tagList = new ArrayList<String>(lvs.size());
				for(LabelVideo lv : lvs){
					tagList.add(lv.getLabelName());
				}
				video.setTags(tagList);
			}
		}
		return vList;
	}

	public Page<Video> findHotVideos(Integer page, Integer size,String uid){
//		List<Filter> filters = new ArrayList<Filter>(1);
//    	filters.add(Filter.ne("flowStat", VideoStatus.已删除.getName()));
    	Pageable pageable = new PageRequest(page-1, size,new Sort(Direction.DESC,"playRateToday"));
//    	Page<Video> p = this.findAll(pr , filters);
    	Page<Video> p = videoRepository.findHotVideos(pageable);
    	List<Video> ls= p.getContent();
    	for(Video video:ls){ 
    		if(uid!=null&&!uid.trim().equals("")){
	    		video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
	    		video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
	    		video.setAttentionAuthor(attentionService.isAttention(Long.parseLong(uid), video.getCreatorId()));
    		}
    		
    		List<Filter> _filters = new ArrayList<Filter>();
    		_filters.add(Filter.eq("videoId", video.getId()));
			Order order = new Order(Direction.ASC,"id");
			Sort sort = new Sort(order);
			List<LabelVideo> lvs = labelVideoRepository.findAll(_filters, sort);
			List<String> tagList = null;
			if (CollectionUtils.isNotEmpty(lvs)) {
				tagList = new ArrayList<String>(lvs.size());
				for(LabelVideo lv : lvs){
					tagList.add(lv.getLabelName());
				}
				video.setTags(tagList);
			}
    		
    	}
    	return p;
	}
	
	public List<Video> findRelatedVideos(Long videoId,Integer page, Integer size,String uid){
		List<Activity> activityList = activityService.findActivityidByVideoid(videoId);//查找当前视频所属活动里列表
		if (activityList == null || activityList.size() == 0){ //如果视频没参与任何一个活动，查询出所有活动列表
			activityList = activityService.findAll();
		} 
		//随机一个活动id，查询该活动下的视频作为相关视频
		int min=0;
		int max=activityList.size();
        Random random = new Random();
        int randomNumber = random.nextInt(max)%(max-min+1) + min;
        String activityId = String.valueOf(activityList.get(randomNumber).getId());
		
        min=0;
        max=100;
		String orderByFiled = "praise_count";
		randomNumber = random.nextInt(max)%(max-min+1) + min;
		if (randomNumber % 2 == 0){ //排序字段做一个简单的随机
			orderByFiled = "play_count";
		}
		
	
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("videoId", videoId);
		params.put("uid", uid);
		params.put("activityId", activityId);
		params.put("orderByFiled", orderByFiled);
		params.put("pageStart", (page-1)*size);
		params.put("pageSize", size);
		
		List<Video> ls = videoDao.findRelatedVideos(params);
		
    	for(Video video:ls){ 
    		if(uid!=null&&!uid.trim().equals("")){
	    		video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
	    		video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
	    		video.setAttentionAuthor(attentionService.isAttention(Long.parseLong(uid), video.getCreatorId()));
    		}
    		
    		List<Filter> _filters = new ArrayList<Filter>();
    		_filters.add(Filter.eq("videoId", video.getId()));
			Order order = new Order(Direction.ASC,"id");
			Sort sort = new Sort(order);
			List<LabelVideo> lvs = labelVideoRepository.findAll(_filters, sort);
			List<String> tagList = null;
			if (CollectionUtils.isNotEmpty(lvs)) {
				tagList = new ArrayList<String>(lvs.size());
				for(LabelVideo lv : lvs){
					tagList.add(lv.getLabelName());
				}
				video.setTags(tagList);
			}
    		
    	}
    	return ls;
	}
	
	public List<Video> findHotVideos(Date timestamp, Integer count,String uid){
    	List<Video> ls= videoRepository.findHotVideos(timestamp, count);
    	for(Video video:ls){ 
    		if(uid!=null&&!uid.trim().equals("")){
	    		video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
	    		video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
	    		video.setAttentionAuthor(attentionService.isAttention(Long.parseLong(uid), video.getCreatorId()));
    		}
    		
    		List<Filter> _filters = new ArrayList<Filter>();
    		_filters.add(Filter.eq("videoId", video.getId()));
			Order order = new Order(Direction.ASC,"id");
			Sort sort = new Sort(order);
			List<LabelVideo> lvs = labelVideoRepository.findAll(_filters, sort);
			List<String> tagList = null;
			if (CollectionUtils.isNotEmpty(lvs)) {
				tagList = new ArrayList<String>(lvs.size());
				for(LabelVideo lv : lvs){
					tagList.add(lv.getLabelName());
				}
				video.setTags(tagList);
			}
    		
    	}
    	return ls;
	}
	
	public List<Video> findTopHotVideos(Integer count){  
    	List<Video> ls= videoRepository.findTopHotVideos(count);
    	for(Video v:ls){ 
    		if(v!=null&&v.getCreatorId()!=null)
    			v.setUser(ruserService.find(v.getCreatorId()));
//    		if(uid!=null&&!uid.trim().equals("")){
//	    		video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
//	    		video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
//    		}
    	}
    	return ls;
	}

	public Page<Video> findOtherVideos(Integer page, Integer size, String uid, String otherUid){
    	Pageable pageable = new PageRequest(page-1, size,new Sort(Direction.DESC,"createDate"));
    	Page<Video> p = null;
    	//当前用户为自己的情况 
    	if(uid != null && uid.equals(otherUid)){
    		p = videoRepository.findMyVideos(Long.parseLong(otherUid), pageable);
    	}else{
    		p = videoRepository.findOtherUserVideos(Long.parseLong(otherUid), pageable);
    	}
//    	p = videoRepository.findUserVideos(Long.parseLong(otherUid), pageable);
    	List<Video> ls= p.getContent();
    	for(Video video:ls){ 
    		if(uid!=null&&!uid.trim().equals("")){
	    		video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
	    		video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
    		}
    		
    		List<Filter> _filters = new ArrayList<Filter>();
    		_filters.add(Filter.eq("videoId", video.getId()));
			Order order = new Order(Direction.ASC,"id");
			Sort sort = new Sort(order);
			List<LabelVideo> lvs = labelVideoRepository.findAll(_filters, sort);
			List<String> tagList = null;
			if (CollectionUtils.isNotEmpty(lvs)) {
				tagList = new ArrayList<String>(lvs.size());
				for(LabelVideo lv : lvs){
					tagList.add(lv.getLabelName());
				}
				video.setTags(tagList);
			}
    	}
    	return p;
	}

	public Page<Video> findCheckedVideos(Integer page, Integer size,String status){
		List<Filter> filters = new ArrayList<Filter>(1);
    	filters.add(new Filter("flowStat", status));
    	Pageable pr = new PageRequest(page, size, null);
    	Page<Video> p = this.findAll(pr , filters);
    	return p;
	}

	public Page<Video> findCheckedVideos(Pageable pr,List<Filter> filters){
		return this.findAll(pr , filters);
	}
	
	public Page<Video> findHotVideos(Pageable pageable, List<RootInfo> rootList,List<JoinInfo> joinList,List<FetchInfo> fetchList,List<Filter> filters,List<OrderByObject> orderByObjList){
		return this.findAll(pageable, rootList, joinList,fetchList, filters, orderByObjList);
	}

	public List<Video> getVideos(List<Long> ids) {
		List<Filter> ls = new ArrayList<Filter>();
		ls.add(Filter.in("id", ids));
		List<Video> lsv = videoRepository.findAll(ls, null);
//		if(lsv!=null) {
//			for(Video one:lsv)
//				one.setVideoPicList(this.getVideoPic(one.getId()));
//		}
		return lsv;
	}

	public List<Video> getAllVideo(){
		List<Video> ls = this.findAll();
		if(ls==null|ls.size()<=0)
			return  new ArrayList<Video>();
//		for (Video one:ls) {
//			one.setVideoPicList(this.getVideoPic(one.getId()));
//		}
		return ls;
	}

	//获取视频图片
//	public List<String> getVideoPic(Long videoId){
//		List<Filter> filters = new ArrayList<Filter>();
//		filters.add(new Filter("videoId", videoId.toString()));
//		List<VideoPic>  ls = videoPicRepository.findAll(filters, null);
//		if(ls==null||ls.size()<=0)
//			return new ArrayList<String>(0);
//		List<String> s = new ArrayList<String>(ls.size());
//		for(VideoPic o:ls){
//			s.add(o.getPlayKey());
//		}
//		return s;
//	}

	@Transactional
	public int updateVideo(String playKey,Integer w,Integer h,String duration,String url){
		return this.videoRepository.updateVideo(playKey,w,h,duration,url);
	}

	@Transactional
	public void saveVideo(String[] subject,String[] tag,String title,Long uploader,String description,String playId,String dataFrom, long loginId){
		Video v=new Video();
		v.setName(title);
		v.setDescription(description);
		v.setRuserId(loginId);
		v.setCreatorId(uploader);
		v.setPlayKey(playId);
		if(tag!=null){
			v.setTag(StringUtils.join(tag, ","));
		}
		v.setFlowStat(VideoStatus.审核通过.getName());
		v.setDataFrom(dataFrom);
		//1.5.4版本之前，视频信息放在尾部，现为了支持mp4边下边播和拖拽，放在头部。所以客户端加传app版本号参数。如何有版本号，认为是以mp4播放。如果没有，认为是老版本视频，仍以m3u8播放。
		v.setVersionNum("1.5.4");
		v.setMp4Flg(1);
		this.videoRepository.save(v);

//		List<SubjectVideoRelation> svr=new ArrayList<SubjectVideoRelation>();
//		if(subject!=null){
//			for (String s : subject) {
//				SubjectVideoRelation sv=new SubjectVideoRelation();
//				sv.setVideoId(v.getId());
//				sv.setSubjectId(Long.parseLong(s));
//				sv.setCreatorId(uploader);
//				svr.add(sv);
//			}
//		}
//		this.subjectVideoRepository.save(svr);
	}
	
	@Transactional
	public Video saveVideo2activity(String[] activity,Set<String> tags,String title,Long uploader,String description,
			String playId,String dataFrom,String introductionMark,Long templateId,long loginId,String videoPic,
			String videoListPic,Date planTime,String pub,String isLogo,String playRateToday,ShowVideo showVideo){
		Video v=new Video();
		v.setName(title);
		v.setDescription(description);
		v.setRuserId(loginId);
		v.setCreatorId(uploader);
		v.setPlayKey(playId);
		v.setIntroductionMark("0");//此字段暂时停止使用 2015-03-17
		v.setTemplateId(templateId);
		v.setVideoPic(videoPic);
		v.setVideoListPic(videoListPic);
		v.setIsLogo(isLogo);
		if(StringUtils.isNotBlank(playRateToday) && new BigDecimal(playRateToday).compareTo(new BigDecimal(0)) == 1){
			v.setPlayRateToday(new BigDecimal(playRateToday));
			v.setPlayRateState(1);
		}else{
			v.setPlayRateToday(new BigDecimal(0.0000));
			v.setPlayRateState(0);
		}
//		if(tag != null){
//			v.setTag(StringUtils.join(tag, " ").trim());
//		}

		if("1".equals(pub)){//立即发布
			v.setFlowStat(VideoStatus.已发布.getName());
			v.setPublishTime(new Date());
			
		}else{
			v.setFlowStat(VideoStatus.计划发布.getName());
			Calendar c = Calendar.getInstance();
	        c.setTime(planTime);
	        Date plandate = c.getTime();
	        v.setPlanPublishTime(plandate);
	        v.setPublishTime(plandate);
		}
		v.setDataFrom(dataFrom);
		
		//1.5.4版本之前，视频信息放在尾部，现为了支持mp4边下边播和拖拽，放在头部。所以客户端加传app版本号参数。如何有版本号，认为是以mp4播放。如果没有，认为是老版本视频，仍以m3u8播放。
		v.setVersionNum("1.5.4");
		v.setMp4Flg(1);
    	
		this.videoRepository.save(v);
		
		if("1".equals(pub)){
			//计算用户排行人气值
			ruserService.executeDayUserPopularity(v.getCreatorId());
		}
	//===========我拍秀视频存入======================	
		if(showVideo!=null){
			showVideo.setVideoId(v.getId());
			saveShowVideo(showVideo);
		}
		
		Set<String> activityIdSet=new HashSet<String>();
		
		List<ActivityVideo> svr=new ArrayList<ActivityVideo>();
		if(activity!=null){
			for (String s : activity) {
				ActivityVideo sv=new ActivityVideo();
				sv.setVideoid(v.getId());
				sv.setActivityid(Long.parseLong(s));
				sv.setCreatorId(uploader);
				svr.add(sv);
				activityIdSet.add(s);
			}
		}
		
		//获取发现页推荐二级列表
		StringBuffer jpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
//        jpql.append("FROM Activity  a    WHERE    a.groupType = 0 and a.status=1  ");
        
        jpql.append("FROM Activity  a          ");
        
//        jpql.append(" and a.vipActivity is null and a.personalActivity is null  ");
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("id",1,"a");
        orderByList.add(orderByObject);
        
        List allActivityList=new ArrayList();
		try {
			allActivityList = activityService.getObjectByJpql(jpql, 0, 10000, "a", paramList, null, orderByList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 List<Activity> indexActivityList=new ArrayList<Activity>();
		 List<Activity> vipActivityList=new ArrayList<Activity>();
		 List<Activity> ruserActivityList=new ArrayList<Activity>();
		
		for (Object object : allActivityList) {
			Activity actTemp=(Activity) object;
			if(  (actTemp.getVips()==null||actTemp.getVips().equals(""))&&(actTemp.getRusers()==null||actTemp.getRusers().equals(""))  ) {
				if(actTemp.getStatus()==1) {
					indexActivityList.add(actTemp);
				}
			}else {
				if(actTemp.getVips()!=null&&!actTemp.getVips().equals("")) {
					vipActivityList.add(actTemp);
				}
				
				if(actTemp.getRusers()!=null&&!actTemp.getRusers().equals("")) {
					ruserActivityList.add(actTemp);
				}
			}
		}
		
		if(tags!=null&&tags.size()>0) {
			
			
			
	        
	        
	        //(原逻辑，已废弃)如果tag与发现页列表项的名称相同，则把该视频自动加入到该推荐项中（如果勾选活动时，已经勾选了该项，则不重复添加）
			//           如果tag与发现页列表项的标签列表相同，则把该视频自动加入到该推荐项中（如果勾选活动时，已经勾选了该项，则不重复添加）
			if(indexActivityList!=null&&indexActivityList.size()>0) {
				for (Object object : indexActivityList) {
					Activity actTemp=(Activity) object;
					if(actTemp.getTags()!=null&&!actTemp.getTags().equals("")) {
						Set<String> result = new HashSet<String>();
				        

				        
				        
						Set<String> activityTagSet =new HashSet<String>();
						String tempTags=actTemp.getTags();
						tempTags=tempTags.replace("#", "");
						String[] activityTagArr=tempTags.split(";");
						activityTagSet.addAll(Arrays.asList(activityTagArr));
						
						result.clear();
				        result.addAll(activityTagSet);
				        result.retainAll(tags);
				        
//						boolean containFlg= activityTagSet.retainAll(tags);
						
						if(result!=null&&result.size()>0) {
							if(activityIdSet.contains(actTemp.getId().toString())) {
								
							}else {
								ActivityVideo sv=new ActivityVideo();
								sv.setVideoid(v.getId());
								sv.setActivityid(actTemp.getId());
								sv.setCreatorId(uploader);
								svr.add(sv);
								
								activityIdSet.add(actTemp.getId().toString());
								
							}
						}
						
//						for (String tag : tags) {
//							if(tag.equals(actTemp.getTitle())) {
//								if(activityIdSet.contains(actTemp.getId().toString())) {
//									
//								}else {
//									ActivityVideo sv=new ActivityVideo();
//									sv.setVideoid(v.getId());
//									sv.setActivityid(actTemp.getId());
//									sv.setCreatorId(uploader);
//									svr.add(sv);
//									
//									activityIdSet.add(actTemp.getId().toString());
//									
//								}
//							}
//							
//						}
					}
					
				}
			}
			
			
			
			
		}
		
		//增加vip栏目和个人明星栏目，所以原黄V逻辑废弃
		//如果上传者是黄V用户，则把视频自动归类到明星栏目
		
//		if(uploader!=null) {
////			uploader=16l;
//			Ruser u=this.ruserService.find(uploader);
//			if(u!=null&&u.getVipStat()==2) {
//				String vipActivityId=jedisService.get("YELLOW_VIP_ACTIVITY_ID");
//				if(vipActivityId!=null&&!vipActivityId.equals("")) {
//					ActivityVideo sv=new ActivityVideo();
//					sv.setVideoid(v.getId());
//					sv.setActivityid(Long.valueOf(vipActivityId));
//					sv.setCreatorId(uploader);
//					svr.add(sv);
//					
//					activityIdSet.add(vipActivityId);
//				}
//				
//			}
//		}
		
		//增加vip栏目和个人明星栏目
		if(uploader!=null) {
		//	uploader=16l;
			Ruser u=this.ruserService.find(uploader);
			if(u!=null&&u.getVipStat()!=null) {//如果用户存在且是vip用户，则走vip专场加入逻辑
				
				if(vipActivityList!=null&&vipActivityList.size()>0) {//vip活动列表不为空
					for (Activity vipActivity : vipActivityList) {//遍历vip活动列表
						if(activityIdSet.contains(vipActivity.getId().toString())) {//如果活动集合中已经存在该活动，则不再走加入逻辑
							break;
						}
						if(vipActivity.getVips()!=null&&!vipActivity.getVips().equals("")) {//活动vip属性不为空
							String[] vipArr=vipActivity.getVips().split(";");//按照;拆分属性，可能对应多种vip
							if(vipArr!=null&&vipArr.length>0) {//vip列表不空，遍历vip数组
								for(int i=0;i<vipArr.length;i++) {
									if(vipArr[i]!=null&&!vipArr[i].equals("")) {
										Integer vipActId=Integer.valueOf(vipArr[i]);
										if(vipActId.intValue()==u.getVipStat()) {
											ActivityVideo sv=new ActivityVideo();
											sv.setVideoid(v.getId());
											sv.setActivityid(vipActivity.getId());
											sv.setCreatorId(uploader);
											svr.add(sv);
											
											activityIdSet.add(vipActivity.getId().toString());
											
											break;
										}
									}
								}
								
							}
						}
						
					}					
				}
				
				//个人明星栏目
				
				if(u!=null) {//如果用户存在，则走个人明星专场加入逻辑					
					if(ruserActivityList!=null&&ruserActivityList.size()>0) {//个人明星活动列表不为空
						for (Activity ruserActivity : ruserActivityList) {//遍历个人明星活动列表
							if(activityIdSet.contains(ruserActivity.getId().toString())) {//如果活动集合中已经存在该活动，则不再走加入逻辑
								break;
							}
							if(ruserActivity.getRusers()!=null&&!ruserActivity.getRusers().equals("")) {//活动rusers属性不为空
								String[] ruserArr=ruserActivity.getRusers().split(";");//按照;拆分属性，可能对应多种ruser
								if(ruserArr!=null&&ruserArr.length>0) {//rusers列表不空，遍历rusers数组
									for(int i=0;i<ruserArr.length;i++) {
										if(ruserArr[i]!=null&&!ruserArr[i].equals("")) {
											Long ruserId=Long.valueOf(ruserArr[i]);
											if(ruserId.longValue()==u.getId().longValue()) {//当前用户id与活动用户属性用户相等
												ActivityVideo sv=new ActivityVideo();
												sv.setVideoid(v.getId());
												sv.setActivityid(ruserActivity.getId());
												sv.setCreatorId(uploader);
												svr.add(sv);
												
												activityIdSet.add(ruserActivity.getId().toString());
												
												break;
											}
										}
									}
									
								}
							}
							
						}
						
					}
				
				}
				
			}
		}
		
		
		
		this.activityVideoRepository.save(svr);

//===========新tag结构存入机制============
		List<LabelVideo> lvList = new ArrayList<LabelVideo>(tags.size());
		for(String _tag : tags){
			LabelVideo lv = new LabelVideo();
			lv.setLabelName(_tag);
			lv.setVideoId(v.getId());
			lv.setLabelId(0L);
			lvList.add(lv);
			
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
				solrWoPaiLiveTagService.index(label.getId()+"_"+v.getId(), _tag, v.getId(),0);
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
		labelVideoRepository.save(lvList);
		
		
		for (LabelVideo labelVideo : lvList) {
			try {
				solrWoPaiVideoTagService.index(labelVideo.getId(), labelVideo.getLabelName(), labelVideo.getVideoId());
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
		
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tags", tags);
		
		Message msg = new Message();
		msg.setModule(Module.VIDEO);
		msg.setAction(Action.INSERT);
		map.put("uid", String.valueOf(loginId));
		map.put("vid", v.getId().toString());
		map.put("isadmin", "1");
		msg.setDataMap(map);
		kafkaProducer.send("rest-topic", msg);
		
		
		//video description add solr index
		try {
			solrVideoService.addVideo(v.getId(), v.getDescription());
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
		
//============================================================================
		
		if("1".equals(pub)){//立即发布
			//马甲用户上传视频，该用户上传视频数量 +1
			Ruser ruser = ruserService.find(uploader);
			int count = videoRepository.countAdminVideoForRuser(uploader);//统计该用户发的视频数
			ruser.setVideoCount(count);
			ruserService.update(ruser);
		}
		return v;
	}

	private void saveShowVideo(ShowVideo showVideo) {
		if(StringUtils.isNotBlank(showVideo.getRefVideoId())){
			String refVids[] = showVideo.getRefVideoId().split(",");
			List<Long> vids = new ArrayList<Long>();
			for(String vid:refVids){
				if(StringUtils.isNumeric(vid))
					vids.add(Long.parseLong(vid));
			}
			List<Long> creatorIds = videoRepository.findCreatorIds(vids);
			
			if(creatorIds != null && creatorIds.size()>0){
				Set<Long> set = new HashSet<Long>();
				set.addAll(creatorIds);
				String refUids = "";
				for(Iterator<Long> it = set.iterator();it.hasNext();){
					refUids += it.next()+",";
				}
				if(StringUtils.isNotBlank(refUids)){
					refUids = refUids.substring(0, refUids.lastIndexOf(","));
				}
				
				showVideo.setRefUserId(refUids);
			}
		}
		showVideoRepository.save(showVideo);
	}
	
	private void updateShowVideo(ShowVideo showVideo) {
		ShowVideo show = showVideoRepository.findOne(showVideo.getId());
		show.setTitle(showVideo.getTitle());
		show.setDescription(showVideo.getDescription());
		show.setPic(showVideo.getPic());
		show.setModifyDate(new Date());
				
		if(StringUtils.isNotBlank(showVideo.getRefVideoId()) && !showVideo.getRefVideoId().endsWith(show.getRefVideoId())){
			String refUids = "";
			String refVids[] = showVideo.getRefVideoId().split(",");
			List<Long> vids = new ArrayList<Long>();
			for(String vid:refVids){
				if(StringUtils.isNumeric(vid))
					vids.add(Long.parseLong(vid));
			}
			List creatorIds = videoRepository.findCreatorIds(vids);
			
			if(creatorIds != null && creatorIds.size()>0){
				Set<Long> set = new HashSet<Long>();
				set.addAll(creatorIds);
				
				for(Iterator<Long> it = set.iterator();it.hasNext();){
					refUids += it.next()+",";
				}
				if(StringUtils.isNotBlank(refUids)){
					refUids = refUids.substring(0, refUids.lastIndexOf(","));
				}
			}

			show.setRefUserId(refUids);
			show.setRefVideoId(showVideo.getRefVideoId());
		}
		
		showVideoRepository.save(show);
	}

	@Transactional
	public void deleteAll(String ids){
		List<Video> vList = new ArrayList<Video>();
		for (String s : ids.split(",")) {
			Video v=this.videoRepository.findOne(Long.parseLong(s));
			vList.add(v);
		}
		this.videoRepository.deleteInBatch(vList);
	}

	@Transactional
	//删除视频以前未传输uid
	public void deleteVideos(List<Long> ids,Long uid){
		List<Long> deleteIds = new ArrayList<Long>();
		for(Long id:ids){
			Video video = videoRepository.getOne(id);
			//视频存在，且视频的作者为操作用户才进行删除视频操作
			if (video != null  && video.getCreatorId().intValue() == uid.intValue()){
				deleteIds.add(id);
			}
		}
		
		//videoRepository.deletePraises(deleteIds);
		videoRepository.deleteFavarites(deleteIds);  
        String[] s = new String[deleteIds.size()];
        for (int i = 0; i < deleteIds.size(); i++) {
        	if(deleteIds.get(i)!=null){
        		s[i]=deleteIds.get(i).toString();
        		this.decRuserVideoCount(uid, deleteIds.get(i));
        	} 
		}
        jedisService.deleteSortedSetItemFromShard("Video_Check",s); 
        videoRepository.updateDelVideos(deleteIds);
        //异步处理索引，删除搜索引擎倒排中的id
        Message msg = new Message();
        Map<String, Object> map = new HashMap<String, Object>();
		msg.setModule(Module.VIDEO);
		msg.setAction(Action.DELETE);
		map.put("vids", s);
		msg.setDataMap(map);
		kafkaProducer.send("rest-topic", msg);
	}
	
	private void decRuserVideoCount(Long uid,Long vid){
		String stat = videoDao.getVideoStat(vid);
		if(VideoStatus.审核通过.getName().equals(stat)){ 
			this.ruserDAO.decVideoCountAndAllCount(uid);
		}else if(VideoStatus.未审核.getName().equals(stat)){
			this.ruserDAO.decAllVideoCount(uid);
		}
	}

	@Override
	@Transactional
	public void updateVideoStatusByIds(Long auditorId,Collection ids) {
		this.videoRepository.updateVideoStatusByIds(VideoStatus.已发布.getName(),auditorId,ids);
	}

	@Transactional
	public void updateVideoStatusByIds(Long auditorId,String status,Collection ids) {
		//执行删除或者发布的行为时重新统计用户的视频数
		List<Long> temp = (List<Long>)ids;
		if(VideoStatus.已删除.getName().equals(status)){
			Video v = new Video();
			for (Long id : temp) {
				v = videoRepository.findOne(id);
				if(VideoStatus.审核通过.getName().equals(v.getFlowStat())){ 
					this.ruserDAO.decVideoCountAndAllCount(v.getCreatorId());
				}else if(VideoStatus.未审核.getName().equals(v.getFlowStat())){
					this.ruserDAO.decAllVideoCount(v.getCreatorId());
				}
			}
		}else if(VideoStatus.审核未通过.getName().equals(status)){
			Video v = new Video();
			for (Long id : temp) {
				v = videoRepository.findOne(id);
				this.ruserDAO.decAllVideoCount(v.getCreatorId());
			}
		}

		this.videoRepository.updateVideoStatusByIds(status,auditorId,ids);
		
		//如果立即发布了，把发布时间更新为当前时间
		if (status.equals(VideoStatus.已发布.getName()))
			this.videoRepository.updateVideoPublishTimeByIds(new Date(), ids);
		
		if(status.equals(VideoStatus.审核通过.getName()) || status.equals(VideoStatus.已发布.getName()) || VideoStatus.已删除.getName().equals(status)){
			Video v = new Video();
			Ruser ruser;
			for (Long id : temp) {
				v = videoRepository.findOne(id);
				ruser = ruserService.find(v.getCreatorId());
				int count = videoRepository.countAdminVideoForRuser(v.getCreatorId());//统计该用户发的视频数
				if(count >= 0)
					ruser.setVideoCount(count);
				ruserService.update(ruser);
			}
		} 
		for(Long id : temp){
			jedisService.deleteSortedSetItemFromShard(Constants.VIDEO_CHECK, id.toString());
		}
		
	}
	
	@Transactional
	public void updatePlayCount(Long videoId,Integer playCount) {
		this.videoRepository.updatePlayCount(videoId, playCount);
	}
	
	@Transactional
	public void updatePlayRateToday(Long videoId,BigDecimal playRateToday) {
		this.videoRepository.updatePlayRateToday(videoId, playRateToday);
	}
	
	@Transactional
	public void updatePlayRateState(Long videoId,Integer playRateState) {
		this.videoRepository.updatePlayRateState(videoId, playRateState);
	}
	
	
	@Transactional
	public void updateLogoState(Long videoId,String state) {
		this.videoRepository.updateLogoState(videoId, state);
	}

	@Transactional
	public void publishVideos(Long auditorId,Collection ids) {
		this.videoRepository.updateVideoStatusByIds(VideoStatus.已发布.getName(),auditorId,ids);
	}

	public List findVideo(Long id){
		return this.videoRepository.findVideo(id);
	}

//	@Override
//	public List findSubjectIdByVideoId(Long videoId) {
//		return this.subjectVideoRepository.findSubjectIdByVideoId(videoId);
//	}
	
	@Override
	public List findActivityIdByVideoId(Long videoId) {
		return this.activityVideoRepository.findActivityidByVideoid(videoId);
	}

	@Transactional
	public void updateVideoAndCheckOk(Long id,String[] subject,String[] tag,String title,Long uploader,String description){
		Video v=this.videoRepository.findOne(id);
		v.setName(title);
		v.setDescription(description);
		if(tag!=null){
			v.setTag(StringUtils.join(tag, ","));
		}
		v.setFlowStat(VideoStatus.已发布.getName());
		this.videoRepository.save(v);

//		this.subjectVideoRepository.deleteByVideoId(v.getId());
//
//		List<SubjectVideoRelation> svr=new ArrayList<SubjectVideoRelation>();
//		if(subject!=null){
//			for (String s : subject) {
//				SubjectVideoRelation sv=new SubjectVideoRelation();
//				sv.setVideoId(v.getId());
//				sv.setSubjectId(Long.parseLong(s));
//				sv.setCreatorId(uploader);
//				svr.add(sv);
//			}
//		}
//		this.subjectVideoRepository.save(svr);
	}
	

	@Transactional
	public void updateVideoAndCheckOk2activity(Long id,String[] activity,String[] tag,String title,Long uploader,String description){
		Video v=this.videoRepository.findOne(id);
		v.setName(title);
		v.setDescription(description);
		if(tag!=null){
			v.setTag(StringUtils.join(tag, ","));
		}
		v.setFlowStat(VideoStatus.已发布.getName());
		v.setAuditDateTime(new Date());
		this.videoRepository.save(v);

		this.activityVideoRepository.deleteByVideoid(v.getId());

		List<ActivityVideo> svr=new ArrayList<ActivityVideo>();
		if(activity!=null){
			for (String s : activity) {
				ActivityVideo sv=new ActivityVideo();
				sv.setVideoid(v.getId());
				sv.setActivityid(Long.parseLong(s));
				sv.setCreatorId(uploader);
				svr.add(sv);
			}
		}
		this.activityVideoRepository.save(svr);
	}

	@Transactional
	public void updateFailReason(Long auditorId,String failReason,List<Long> id){
		this.videoRepository.updateFailReason(failReason, id,VideoStatus.审核未通过.getName(),auditorId);
		for(Long vid:id){
			Video v = videoRepository.findOne(vid);
			this.ruserDAO.decAllVideoCount(v.getCreatorId());
			jedisService.deleteSortedSetItemFromShard(Constants.VIDEO_CHECK, vid.toString());
			
			//视频审核不通过，给上传者发送系统通知
			SystemMess mess = new SystemMess();
			mess.setContent("您好，您的视频内容违反我拍相关规定（违法、涉黄、无画面等），审核未通过，请重新拍摄上传。");
			mess.setTitle("您有视频审核未通过");
			mess.setCreatorId(Long.parseLong(jedisService.get(BicycleConstants.OFFICICAL_USER_ID)));
			mess.setPlatform("all");
			mess.setDestUser(String.valueOf(v.getCreatorId()));
			mess.setPublishTime(new Date());
			mess.setIsplan("0");
			mess.setCreateDate(new Date());
			mess.setOperation("video");
			mess.setStat("0");
			mess.setTargetid(v.getId());
			mess.setImagePath(v.getVideoPic());
			systemMessService.saveSysmess(mess);
			systemMessService.sendMessage(mess);
		}
	}
	
	@Transactional
	public void updateFailReason(String failReason,Long id){
		this.videoRepository.updateFailReason(failReason, id,VideoStatus.审核未通过.getName());
		jedisService.deleteSortedSetItemFromShard(Constants.VIDEO_CHECK, id.toString());
	}

	@Override
	@Transactional
	public void recoverVideos(Long auditorId,Collection ids) throws SolrServerException, IOException {
		updateVideoStatusByIds(auditorId,VideoStatus.未审核, ids);
	}

	@Override
	@Transactional
	public List<Map<String,String>> updateVideoStatusByIds(Long auditorId,VideoStatus vs, Collection ids) throws SolrServerException, IOException {
		
		List<Map<String,String>> CheckOkIdsForIntegral = new ArrayList<Map<String,String>>();//审核通过的作者id，视频id，作为返回值通知任务系统
		List<Long> repeatCheckOkIds = new ArrayList<Long>();
		List<Long> temp = (List<Long>)ids;
		if(VideoStatus.已删除.equals(vs)){
			Video v = new Video();
			for (Long id : temp) {
				v = videoRepository.findOne(id);
				if (v.getType() == 4) {//如果是预告图片被删除，预告也被删除
					Long liveNoticeId = v.getLiveNoticeId();
					liveNoticeService.delete(liveNoticeId);
				}
				if(VideoStatus.审核通过.getName().equals(v.getFlowStat())){ 
					this.ruserDAO.decVideoCountAndAllCount(v.getCreatorId());
				}else if(VideoStatus.未审核.getName().equals(v.getFlowStat())){
					this.ruserDAO.decAllVideoCount(v.getCreatorId());
				}
			}
		}else if(VideoStatus.审核未通过.equals(vs)){
			Video v = new Video();
			for (Long id : temp) {
				v = videoRepository.findOne(id);
				this.ruserDAO.decAllVideoCount(v.getCreatorId());
			}
		} else if (VideoStatus.审核通过.equals(vs)){
			Video v = new Video();
			for (Long id : temp) {
				v = videoRepository.findOne(id);
				if (v.getType() == 4) {//如果是预告图片审核通过，改变预告的状态
					Long liveNoticeId = v.getLiveNoticeId();
					LiveNotice ln = liveNoticeService.find(liveNoticeId);
					ln.setStatus(1);
					liveNoticeService.update(ln);
				}
				if (v.getFlowStat().equals(VideoStatus.审核通过.getName())){//若视频已经被审核通过，将视频id记录在repeatCheckOkIds中
					repeatCheckOkIds.add(id);
				}
			}
		}

		this.videoRepository.updateVideoStatusByIds(vs.getName(),auditorId, ids);
		
		if(VideoStatus.审核通过.equals(vs) || VideoStatus.已发布.equals(vs) ||VideoStatus.已删除.equals(vs)){
			Video v = new Video();
			Ruser ruser;
			String idarr[] = new String[ids.size()];
			for (int i=0;i<temp.size();i++) {
				v = videoRepository.findOne(temp.get(i));
				ruser = ruserService.find(v.getCreatorId());
				int count = videoRepository.countUserVideoForRuser(v.getCreatorId());//统计该用户发的视频数
				if(count >= 0)
					ruser.setVideoCount(count);
				ruserService.update(ruser);
				if(VideoStatus.审核通过.equals(vs) && !repeatCheckOkIds.contains(temp.get(i))){ //如果视频审核通过，且不是重复审核通过，给kafka和solr发消息
					sendMessage(v.getCreatorId(),v.getId());
					solrVideoService.addVideo(v.getId(), v.getDescription());
					
					Map<String,String> uidAndVidMap = new HashMap<String, String>();
					uidAndVidMap.put("uid", String.valueOf(v.getCreatorId()));
					uidAndVidMap.put("vid", String.valueOf(v.getId()));
					CheckOkIdsForIntegral.add(uidAndVidMap);
				}
				if(VideoStatus.已删除.equals(vs)) {
					solrVideoService.deleteDocByid(v.getId());
				}
				//计算用户排行热度
				ruserService.executeDayUserPopularity(v.getCreatorId());
				idarr[i] = String.valueOf(temp.get(i));
			}
			if(VideoStatus.已删除.equals(vs)){
				Message msg = new Message();
		        Map<String, Object> map = new HashMap<String, Object>();
				msg.setModule(Module.VIDEO);
				msg.setAction(Action.DELETE);
				map.put("vids", idarr);
				msg.setDataMap(map);
				kafkaProducer.send("rest-topic", msg);
			}
		} 
		
		for(Long id : temp){
			jedisService.deleteSortedSetItemFromShard(Constants.VIDEO_CHECK, id.toString());
		}
		
		return CheckOkIdsForIntegral;
		
	}

	private void sendMessage(Long creatorId, Long id) {
		Message msg = new Message();
		msg.setModule(Module.VIDEO);
		msg.setAction(Action.AUDIT);
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("uid",creatorId.toString());
		dataMap.put("vid",id.toString());
		dataMap.put("pubDate",new Date().getTime());//审核成功记录发布消息时间
		msg.setDataMap(dataMap);
		kafkaProducer.send("rest-topic",msg);
	}

	@Override
	@Transactional
	public void updateVideo(Long id, String[] subjects, String[] tags,
			String title, Long uploader, String description) {
		Video v=this.videoRepository.findOne(id);
		v.setName(title);
		v.setDescription(description);
		if(tags!=null){
			v.setTag(StringUtils.join(tags, ","));
		}else{
			v.setTag("");
		}
		this.videoRepository.save(v);

//		this.subjectVideoRepository.deleteByVideoId(v.getId());
//
//		List<SubjectVideoRelation> svr=new ArrayList<SubjectVideoRelation>();
//		if(subjects!=null)
//			for (String s : subjects) {
//				SubjectVideoRelation sv=new SubjectVideoRelation();
//				sv.setVideoId(v.getId());
//				sv.setSubjectId(Long.parseLong(s));
//				sv.setCreatorId(uploader);
//				svr.add(sv);
//			}
//		this.subjectVideoRepository.save(svr);
	}
	
	@Override
	@Transactional
	public void updateVideo2activity(Long id, String[] activitys, Set<String> tags,
			String title, Long ruserId, String description,String introductionMark,Long templateId,String isLogo,String playRateToday,ShowVideo showVideo) {
		Video v=this.videoRepository.findOne(id);
		
		if(StringUtils.isNotBlank(description) && !description.equals(v.getDescription())){
	    	//todo 删除原来label与video的关联
			labelVideoDAO.deleteByVid(id);
		}
		
		v.setName(title);
		v.setDescription(description);
		v.setIntroductionMark("0");//此字段暂时停止使用 2015-03-17
		v.setTemplateId(templateId);
		v.setIsLogo(isLogo);
		v.setCreatorId(ruserId);
		v.setPlayRateToday(new BigDecimal(playRateToday));
//		if(tags!=null){
//			v.setTag(StringUtils.join(tags, " "));
//		}else{
//			v.setTag("");
//		}
		this.videoRepository.save(v);
		
		if(showVideo!=null){
			updateShowVideo(showVideo);
		}
		
		//===========新tag结构存入机制============
		if(StringUtils.isNotBlank(description)){	    	
	    	//todo 重建关联关系
			List<LabelVideo> lvList = new ArrayList<LabelVideo>(tags.size());
			for(String _tag : tags){
				LabelVideo lv = new LabelVideo();
				lv.setLabelName(_tag);
				lv.setVideoId(v.getId());
				lv.setLabelId(0L);
				lvList.add(lv);
			}
			labelVideoRepository.save(lvList);
			
			//发送修改指令，kafka更新solr索引
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tags", tags);
			
			Message msg = new Message();
			msg.setModule(Module.VIDEO);
			msg.setAction(Action.UPDATE);
			map.put("uid", String.valueOf(v.getCreatorId()));
			map.put("vid", v.getId().toString());
			map.put("isadmin", "1");
			msg.setDataMap(map);
			kafkaProducer.send("rest-topic", msg);
		}
		//============================================================================
			

//		this.subjectVideoRepository.deleteByVideoId(v.getId());
		this.activityVideoRepository.deleteByVideoid(v.getId());

//		List<ActivityVideo> svr=new ArrayList<ActivityVideo>();
//		if(activitys!=null){
//			for (String s : activitys) {
//				ActivityVideo sv=new ActivityVideo();
//				sv.setVideoid(v.getId());
//				sv.setActivityid(Long.parseLong(s));
//				sv.setCreatorId(ruserId);
//				svr.add(sv);
//			}
//		}
		
		
		
		Set<String> activityIdSet=new HashSet<String>();
		
		List<ActivityVideo> svr=new ArrayList<ActivityVideo>();
		if(activitys!=null){
			for (String s : activitys) {
				ActivityVideo sv=new ActivityVideo();
				sv.setVideoid(v.getId());
				sv.setActivityid(Long.parseLong(s));
				sv.setCreatorId(v.getCreatorId());
				svr.add(sv);
				activityIdSet.add(s);
			}
		}
		
		//获取发现页推荐二级列表
		StringBuffer jpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
//        jpql.append("FROM Activity  a    WHERE    a.groupType = 0 and a.status=1  ");
        
        jpql.append("FROM Activity  a          ");
        
//		        jpql.append(" and a.vipActivity is null and a.personalActivity is null  ");
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("id",1,"a");
        orderByList.add(orderByObject);
        
        List allActivityList=new ArrayList();
		try {
			allActivityList = activityService.getObjectByJpql(jpql, 0, 10000, "a", paramList, null, orderByList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 List<Activity> indexActivityList=new ArrayList<Activity>();
		 List<Activity> vipActivityList=new ArrayList<Activity>();
		 List<Activity> ruserActivityList=new ArrayList<Activity>();
		
		for (Object object : allActivityList) {
			Activity actTemp=(Activity) object;
			if(  (actTemp.getVips()==null||actTemp.getVips().equals(""))&&(actTemp.getRusers()==null||actTemp.getRusers().equals(""))  ) {
				if(actTemp.getStatus()==1) {
					indexActivityList.add(actTemp);
				}
			}else {
				if(actTemp.getVips()!=null&&!actTemp.getVips().equals("")) {
					vipActivityList.add(actTemp);
				}
				
				if(actTemp.getRusers()!=null&&!actTemp.getRusers().equals("")) {
					ruserActivityList.add(actTemp);
				}
			}
		}
		
		if(tags!=null&&tags.size()>0) {
			
			
	        
	        
	        //(原逻辑，已废弃)如果tag与发现页列表项的名称相同，则把该视频自动加入到该推荐项中（如果勾选活动时，已经勾选了该项，则不重复添加）
			//           如果tag与发现页列表项的标签列表相同，则把该视频自动加入到该推荐项中（如果勾选活动时，已经勾选了该项，则不重复添加）
			if(indexActivityList!=null&&indexActivityList.size()>0) {
				for (Object object : indexActivityList) {
					Activity actTemp=(Activity) object;
					if(actTemp.getTags()!=null&&!actTemp.getTags().equals("")) {
						Set<String> result = new HashSet<String>();
				        

				        
				        
						Set<String> activityTagSet =new HashSet<String>();
						String tempTags=actTemp.getTags();
						tempTags=tempTags.replace("#", "");
						String[] activityTagArr=tempTags.split(";");
						activityTagSet.addAll(Arrays.asList(activityTagArr));
						
						result.clear();
				        result.addAll(activityTagSet);
				        result.retainAll(tags);
				        
//						boolean containFlg= activityTagSet.retainAll(tags);
						
						if(result!=null&&result.size()>0) {
							if(activityIdSet.contains(actTemp.getId().toString())) {
								
							}else {
								ActivityVideo sv=new ActivityVideo();
								sv.setVideoid(v.getId());
								sv.setActivityid(actTemp.getId());
								sv.setCreatorId(v.getCreatorId());
								svr.add(sv);
								
								activityIdSet.add(actTemp.getId().toString());
								
							}
						}
						
//						for (String tag : tags) {
//							if(tag.equals(actTemp.getTitle())) {
//								if(activityIdSet.contains(actTemp.getId().toString())) {
//									
//								}else {
//									ActivityVideo sv=new ActivityVideo();
//									sv.setVideoid(v.getId());
//									sv.setActivityid(actTemp.getId());
//									sv.setCreatorId(uploader);
//									svr.add(sv);
//									
//									activityIdSet.add(actTemp.getId().toString());
//									
//								}
//							}
//							
//						}
					}
					
				}
			}
			
			
			
			
		}
		
		//增加vip栏目和个人明星栏目，所以原黄V逻辑废弃
		//如果上传者是黄V用户，则把视频自动归类到明星栏目
		
//		if(v.getCreatorId()!=null) {
////			v.setCreatorId(39l);
//			Ruser u=this.ruserService.find(v.getCreatorId());
//			if(u!=null&&u.getVipStat()==2) {
//				String vipActivityId=jedisService.get("YELLOW_VIP_ACTIVITY_ID");
//				if(vipActivityId!=null&&!vipActivityId.equals("")) {
//					ActivityVideo sv=new ActivityVideo();
//					sv.setVideoid(v.getId());
//					sv.setActivityid(Long.valueOf(vipActivityId));
//					sv.setCreatorId(v.getCreatorId());
//					svr.add(sv);
//					
//					activityIdSet.add(vipActivityId);
//				}
//				
//			}
//		}
		
		
		//增加vip栏目和个人明星栏目
		if(v.getCreatorId()!=null) {
		//	uploader=16l;
			Ruser u=this.ruserService.find(v.getCreatorId());
			if(u!=null&&u.getVipStat()!=null) {//如果用户存在且是vip用户，则走vip专场加入逻辑
				
				if(vipActivityList!=null&&vipActivityList.size()>0) {//vip活动列表不为空
					for (Activity vipActivity : vipActivityList) {//遍历vip活动列表
						if(activityIdSet.contains(vipActivity.getId().toString())) {//如果活动集合中已经存在该活动，则不再走加入逻辑
							break;
						}
						if(vipActivity.getVips()!=null&&!vipActivity.getVips().equals("")) {//活动vip属性不为空
							String[] vipArr=vipActivity.getVips().split(";");//按照;拆分属性，可能对应多种vip
							if(vipArr!=null&&vipArr.length>0) {//vip列表不空，遍历vip数组
								for(int i=0;i<vipArr.length;i++) {
									if(vipArr[i]!=null&&!vipArr[i].equals("")) {
										Integer vipActId=Integer.valueOf(vipArr[i]);
										if(vipActId.intValue()==u.getVipStat()) {
											ActivityVideo sv=new ActivityVideo();
											sv.setVideoid(v.getId());
											sv.setActivityid(vipActivity.getId());
											sv.setCreatorId(v.getCreatorId());
											svr.add(sv);
											
											activityIdSet.add(vipActivity.getId().toString());
											
											break;
										}
									}
								}
								
							}
						}
						
					}					
				}
				
				//个人明星栏目
				
				if(u!=null) {//如果用户存在，则走个人明星专场加入逻辑					
					if(ruserActivityList!=null&&ruserActivityList.size()>0) {//个人明星活动列表不为空
						for (Activity ruserActivity : ruserActivityList) {//遍历个人明星活动列表
							if(activityIdSet.contains(ruserActivity.getId().toString())) {//如果活动集合中已经存在该活动，则不再走加入逻辑
								break;
							}
							if(ruserActivity.getRusers()!=null&&!ruserActivity.getRusers().equals("")) {//活动rusers属性不为空
								String[] ruserArr=ruserActivity.getRusers().split(";");//按照;拆分属性，可能对应多种ruser
								if(ruserArr!=null&&ruserArr.length>0) {//rusers列表不空，遍历rusers数组
									for(int i=0;i<ruserArr.length;i++) {
										if(ruserArr[i]!=null&&!ruserArr[i].equals("")) {
											Long ruserId1=Long.valueOf(ruserArr[i]);
											if(ruserId1.longValue()==u.getId().longValue()) {//当前用户id与活动用户属性用户相等
												ActivityVideo sv=new ActivityVideo();
												sv.setVideoid(v.getId());
												sv.setActivityid(ruserActivity.getId());
												sv.setCreatorId(v.getCreatorId());
												svr.add(sv);
												
												activityIdSet.add(ruserActivity.getId().toString());
												
												break;
											}
										}
									}
									
								}
							}
							
						}
						
					}
				
				}
				
			}
		}
		this.activityVideoRepository.save(svr);
	}

//id,c.name,c.create_at,c.width,c.height,c.play_key,c.url
	public Page<Map> listpage(int page,int size,String subjectName,String status) {
		List list=this.videoRepository.listpage(subjectName,(page-1)*size, size,status);
		int totalAccount=this.videoRepository.listpagecount(subjectName,status);

//		List strList = new ArrayList();
//		for (Object o : list) {
//			Object[] os=(Object[])o;
//			strList.add(os[0]);
//		}
//
//		List<Filter> fList = new ArrayList<Filter>();
//		fList.add(Filter.in("videoId", strList));
//		List<VideoPic> vpList=this.videoPicRepository.findAll(fList, null);

//		Map<String, Object> vpmap = new HashMap<String, Object>();
//		for (VideoPic videoPic : vpList) {
//			vpmap.put(videoPic.getId().toString(), videoPic.getPlayKey());
//		}

		List<Map> mapList = new ArrayList<Map>();
		for (Object o : list) {
			Object[] os=(Object[])o;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", os[0]);
			map.put("name", os[1]);
			map.put("createDate", os[2]);
			map.put("width", os[3]);
			map.put("height", os[4]);
			map.put("playKey", os[5]);
			map.put("url", os[6]);
			map.put("img", os[7]);
			mapList.add(map);
		}
		Pageable pab = new PageRequest(page, size);
		Page<Map> pageinfo=new PageImpl<Map>(mapList,pab,totalAccount);
		return pageinfo;
	}

	@Override
	public Video findVideoByPlayKey(String playKey,Long uid) {
		List<Filter> ls = new ArrayList<Filter>();
		ls.add(Filter.eq("playKey", playKey));
		List<Video> list=null;
		try{
			list=videoRepository.findAll(ls, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(list!=null && list.size()!=0){
			Video video=list.get(0);
    		if(uid!=0){
	    		video.setPraise(this.praised(uid, video.getId()));
	    		video.setFavorite(this.favorited(uid, video.getId()));
    		}
			return video;
		}
		return new Video();
	}

	public boolean praised(Long creatorId,Long vId){
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("videoId", vId.toString()));
		filters.add(new Filter("creatorId", creatorId.toString()));
		long c = praiseRepository.count(filters);
		return c>0?true:false;
	}

	public boolean favorited(Long creatorId,Long vId){
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("videoId", vId.toString()));
		filters.add(new Filter("creatorId", creatorId.toString()));
		long c = favoriteRepository.count(filters);
		return c>0?true:false;
	}


	public Page listpage(int page,int size,List<Filter> filters,Sort sort) {
		Page<Video> pinfo = this.findAll(new PageRequest(page, size, sort), filters);
//    	List<Video> vList=pinfo.getContent();
//
//    	List strList = new ArrayList();
//		for (Video o : vList) {
//			strList.add(o.getId());
//		}

//		List<Filter> fList = new ArrayList<Filter>();
//		fList.add(Filter.in("videoId", strList));
//		List<VideoPic> vpList=new ArrayList<VideoPic>();
//		if(strList!=null&&!strList.isEmpty()&&strList.size()>0){
//			vpList=this.videoPicRepository.findAll(fList, null);
//		}
//		Map<String, Object> vpmap = new HashMap<String, Object>();
//		for (VideoPic videoPic : vpList) {
//			vpmap.put(videoPic.getVideoId().toString(), videoPic.getPlayKey());
//		}

//		for (Video o : vList) {
//			o.setImg(vpmap.containsKey(o.getId().toString())?vpmap.get(o.getId().toString()).toString():"");
//		}
		return pinfo;
	}

	public String findImgByVideo(Long videoId){
		String img="";
//		List<String> picList=this.getVideoPic(videoId);
//		if(picList!=null&&!picList.isEmpty()&&picList.size()>0){
//			img=picList.get(0);
//		}
		return img;
	}

	public List test(Pageable page){
		return this.videoRepository.fiubdtest(page);
	}

	@Override
	public Page<Video> listPage(int pageNo,int size,Long subjectId,String sortField,Direction direction,String status){
		Page<Video> page = null;
		if(subjectId == null){
			List<Filter> filters = new ArrayList<Filter>();
			filters.add(Filter.eq("flowStat", status));
			long total = this.videoRepository.count(filters);
			PageRequest p = new PageRequest(pageNo - 1,size,direction,sortField);
			return this.videoRepository.findAll(p, filters);
		}

		long total = this.videoRepository.videoListPageCount(subjectId, status);
		PageRequest p = new PageRequest(pageNo -1,size);
		List<Video> list = this.videoRepository.videoListPage(subjectId, status, p);
		page = new PageImpl<Video>(list,p,total);
		return page;
	}

	public Page allvalidlistpage(String flowStat,String dataFrom,String flowStat2, Pageable pageable){
		Page pinfo=this.videoRepository.allvalidlistpage(flowStat, dataFrom, flowStat2, pageable);
//		List<Video> vList=pinfo.getContent();
//
//    	List strList = new ArrayList();
//		for (Video o : vList) {
//			strList.add(o.getId());
//		}
//
//		List<Filter> fList = new ArrayList<Filter>();
//		fList.add(Filter.in("videoId", strList));
//		List<VideoPic> vpList=new ArrayList<VideoPic>();
//		if(strList!=null&&!strList.isEmpty()&&strList.size()>0){
//			vpList=this.videoPicRepository.findAll(fList, null);
//		}
//		Map<String, Object> vpmap = new HashMap<String, Object>();
//		for (VideoPic videoPic : vpList) {
//			vpmap.put(videoPic.getVideoId().toString(), videoPic.getPlayKey());
//		}

//		for (Video o : vList) {
//			o.setImg(vpmap.containsKey(o.getId().toString())?vpmap.get(o.getId().toString()).toString():"");
//		}
		return pinfo;
	}

	@Override
	public Page allvalidlistByNameLikepage(String flowStat, String dataFrom,
			String flowStat2, String name, Pageable pageable) {
		Page pinfo=this.videoRepository.allvalidlistByNameLikepage(flowStat, dataFrom, flowStat2, name, pageable);
//		List<Video> vList=pinfo.getContent();
//
//    	List strList = new ArrayList();
//		for (Video o : vList) {
//			strList.add(o.getId());
//		}
//
//		List<Filter> fList = new ArrayList<Filter>();
//		fList.add(Filter.in("videoId", strList));
//		List<VideoPic> vpList=new ArrayList<VideoPic>();
//		if(strList!=null&&!strList.isEmpty()&&strList.size()>0){
//			vpList=this.videoPicRepository.findAll(fList, null);
//		}
//		Map<String, Object> vpmap = new HashMap<String, Object>();
//		for (VideoPic videoPic : vpList) {
//			vpmap.put(videoPic.getVideoId().toString(), videoPic.getPlayKey());
//		}
//
//		for (Video o : vList) {
//			o.setImg(vpmap.containsKey(o.getId().toString())?vpmap.get(o.getId().toString()).toString():"");
//		}
		return pinfo;
	}

	@Override
	public Page allvalidlistByTagLikepage(String flowStat, String dataFrom,
			String flowStat2, String tag, Pageable pageable) {
		Page pinfo=this.videoRepository.allvalidlistByTagLikepage(flowStat, dataFrom, flowStat2, tag, pageable);
//		List<Video> vList=pinfo.getContent();
//
//    	List strList = new ArrayList();
//		for (Video o : vList) {
//			strList.add(o.getId());
//		}

//		List<Filter> fList = new ArrayList<Filter>();
//		fList.add(Filter.in("videoId", strList));
//		List<VideoPic> vpList=new ArrayList<VideoPic>();
//		if(strList!=null&&!strList.isEmpty()&&strList.size()>0){
//			vpList=this.videoPicRepository.findAll(fList, null);
//		}
//		Map<String, Object> vpmap = new HashMap<String, Object>();
//		for (VideoPic videoPic : vpList) {
//			vpmap.put(videoPic.getVideoId().toString(), videoPic.getPlayKey());
//		}
//
//		for (Video o : vList) {
//			o.setImg(vpmap.containsKey(o.getId().toString())?vpmap.get(o.getId().toString()).toString():"");
//		}
		return pinfo;
	}
	
	@Override
	public Page allvalidlistByActivityLikepage(String flowStat, String dataFrom,String flowStat2, String activity, Pageable pageable) {
		
		Page pinfo=this.videoRepository.allvalidlistByActivityLikepage(flowStat, dataFrom, flowStat2, Long.parseLong(activity), pageable);
		
		List<ActivityVideo> aList = this.videoRepository.allvalidlistByActivityTemp(Long.parseLong(activity));
		List<Video> vList=pinfo.getContent();
		for (int i = 0; i < aList.size(); i++) {
			for (int j = 0; j < vList.size(); j++) {
				if(aList.get(i).getVideoid() == vList.get(j).getId().longValue()){
					vList.get(j).setOrderNum(aList.get(i).getOrderNum());
					vList.get(j).setPlayCountToday(aList.get(i).getId());//暂时借用字段显示。
				}
			}
		}

//    	List strList = new ArrayList();
//		for (Video o : vList) {
//			strList.add(o.getId());
//		}

//		List<Filter> fList = new ArrayList<Filter>();
//		fList.add(Filter.in("videoId", strList));
//		List<VideoPic> vpList=new ArrayList<VideoPic>();
//		if(strList!=null&&!strList.isEmpty()&&strList.size()>0){
//			vpList=this.videoPicRepository.findAll(fList, null);
//		}
//		Map<String, Object> vpmap = new HashMap<String, Object>();
//		for (VideoPic videoPic : vpList) {
//			vpmap.put(videoPic.getVideoId().toString(), videoPic.getPlayKey());
//		}
//
//		for (Video o : vList) {
//			o.setImg(vpmap.containsKey(o.getId().toString())?vpmap.get(o.getId().toString()).toString():"");
//		}
		return pinfo;
	}
	
	@Override
	public Page searchAdminVideo(String startTime,String endTime, String description, String tag,String flow_stat, String isLogo, String activities, String username,Pageable pr) {
		Map<String ,Object> map = new HashMap<String, Object>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("description", StringUtils.isEmpty(description) ? null : description);
		map.put("tag", StringUtils.isEmpty(tag) ? null : tag);
		if(!StringUtils.isEmpty(flow_stat)){
			map.put("flow_stat", flow_stat);
		}else{
			if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
				map.put("flow_stat", "published");
			}
		}
		map.put("isLogo", StringUtils.isEmpty(isLogo)? null : isLogo);
		map.put("activityId", StringUtils.isEmpty(activities) ? null : activities);
		map.put("username", StringUtils.isEmpty(username) ? null : username);
		map.put("iStart", (pr.getPageNumber()-1) * pr.getPageSize());
		map.put("iEnd", pr.getPageSize());
		
		List<Video> vList;
		long vCount;
		if(StringUtils.isNotBlank(activities)){
			vList = mapper.searchAdminVideoActivity(map);
			vCount = mapper.searchAdminVideoCountActivity(map);
		}else{
			vList = mapper.searchAdminVideo(map);
			vCount = mapper.searchAdminVideoCount(map);
		}
		
		for(Video vo:vList){
			List<Activity> acts = activityService.findActivityidByVideoid(vo.getId());
			String act = "";
			for(Activity a:acts){
				act += a.getTitle()+",";
			}
			if(!"".equals(act)){
				act = act.substring(0, act.length()-1);
			}
			vo.setVideoActivity(act);
		}
		
		Page<Video> page = new PageImpl<Video>(vList,pr,vCount);
		return page;
	}

    public Page<Video> findKeywordVides(Integer page, Integer size, String keywords,String uid){
        Pageable pr = new PageRequest(page, size);
    	List<Video> ls= videoRepository.searchVideo(keywords,((page-1L)*size),size); 
    	for(Video video:ls){
    		if(uid!=null&&!uid.trim().equals("")){
	    		video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
	    		video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
    		}
    		
    		List<Filter> _filters = new ArrayList<Filter>();
    		_filters.add(Filter.eq("videoId", video.getId()));
			Order order = new Order(Direction.ASC,"id");
			Sort sort = new Sort(order);
			List<LabelVideo> lvs = labelVideoRepository.findAll(_filters, sort);
			List<String> tagList = null;
			if (CollectionUtils.isNotEmpty(lvs)) {
				tagList = new ArrayList<String>(lvs.size());
				for(LabelVideo lv : lvs){
					tagList.add(lv.getLabelName());
				}
				video.setTags(tagList);
			}
    		
    	}
    	Page<Video> p = new PageImpl<Video>(ls,pr,videoRepository.countSearchVideo(keywords));
    	return p;
	}
    
    public List<Video> findVideosByIds(List<Long> ids){
    	
    	
    	List<Video> ls= videoRepository.findVideosByIds(ids); 
    	return ls;
	} 
    
    
    public List<Video> findVideoByTagName(String tag,int dir,int pageSize,long lastId,String uid){
    	List<Video> ls = null;
    	if (dir == 0) {
			if (lastId == 0) {
				ls = videoRepository.searchFirstPageVideoByTag(tag, pageSize);
			}else {
				ls = videoRepository.searchPrevPageVideoByTag(tag, lastId, pageSize);
			}
		}else {
			if (lastId == 0) {
				ls = videoRepository.searchFirstPageVideoByTag(tag, pageSize);
			}else {
				ls = videoRepository.searchNextPageVideoByTag(tag, lastId, pageSize);
			}
		}
    	if (CollectionUtils.isNotEmpty(ls)) {
    		for(Video video:ls){
    			long creatorId = video.getCreatorId();
    			if (org.apache.commons.lang3.StringUtils.isNotBlank(uid)) {
    				video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
    				video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
    	    		video.setAttentionAuthor(attentionService.isAttention(Long.parseLong(uid), creatorId));
				}
    	    	List<Long> ids = new ArrayList<Long>();
    	    	ids.add(creatorId);
    	    	video.setUser(ruserDAO.getRusers(ids).get(0));
    	    	
    	    	List<Filter> _filters = new ArrayList<Filter>();
        		_filters.add(Filter.eq("videoId", video.getId()));
    			Order order = new Order(Direction.ASC,"id");
    			Sort sort = new Sort(order);
    			List<LabelVideo> lvs = labelVideoRepository.findAll(_filters, sort);
    			List<String> tagList = null;
    			if (CollectionUtils.isNotEmpty(lvs)) {
    				tagList = new ArrayList<String>(lvs.size());
    				for(LabelVideo lv : lvs){
    					tagList.add(lv.getLabelName());
    				}
    				video.setTags(tagList);
    			}
        	}
		}
    	return CollectionUtils.isNotEmpty(ls) ? ls : new ArrayList<Video>(0);
    }

    public List<Video> findVideosByIds(List<Long> ids,String uid){
    	List<Video> videoList = videoRepository.findVideosByIds(ids);
    	if (CollectionUtils.isNotEmpty(videoList)) {
    		for(Video video:videoList){
    			long creatorId = video.getCreatorId();
    			if (org.apache.commons.lang3.StringUtils.isNotBlank(uid)) {
    				video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
    				video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
    	    		video.setAttentionAuthor(attentionService.isAttention(Long.parseLong(uid), creatorId));
				}
    	    	List<Long> uids = new ArrayList<Long>();
    	    	uids.add(creatorId);
    	    	if(uids!=null&&ruserDAO.getRusers(uids)!=null&&ruserDAO.getRusers(uids).size()>0) {
    	    		video.setUser(ruserDAO.getRusers(uids).get(0));
    	    	}
    	    	
    	    	
    	    	List<Filter> _filters = new ArrayList<Filter>();
        		_filters.add(Filter.eq("videoId", video.getId()));
    			Order order = new Order(Direction.ASC,"id");
    			Sort sort = new Sort(order);
    			List<LabelVideo> lvs = labelVideoRepository.findAll(_filters, sort);
    			List<String> tagList = null;
    			if (CollectionUtils.isNotEmpty(lvs)) {
    				tagList = new ArrayList<String>(lvs.size());
    				for(LabelVideo lv : lvs){
    					tagList.add(lv.getLabelName());
    				}
    				video.setTags(tagList);
    			}
    	    	
        	}
		}
    	return CollectionUtils.isNotEmpty(videoList) ? videoList : new ArrayList<Video>(0);
    }
    
	@Override
	@Transactional
	public int recommendVideo(Long videoId) {
		return this.videoRepository.recommendVideo(videoId);
	}

	@Override
	public Video updateVideoTag(Long vid, String tag) {
		Video video = videoRepository.findOne(vid);
		video.setTag(tag);
		videoRepository.save(video);
		return video;
	}

	@Override
	public Page<Video> findVideos(int pageNo, int size, List<Filter> filter) {
		Pageable pageable=new PageRequest(pageNo, size);
		return this.videoRepository.findAll(pageable, filter);
	}

	@Override
	public Page searchVideoList(Integer pageNo, Integer pageSize, Map<String, Object> params) {
		List<Video> voList = videoDao.searchUserVideo(params);
		for(Video vo:voList){
			Ruser user = this.ruserService.find(vo.getCreatorId());
			if(user != null){
				vo.setUploader(user.getName());
			}
			
			List<Activity> acts = activityService.findActivityidByVideoid(vo.getId());
			List<String> act = new ArrayList<String>();
			for(Activity a:acts){
				act.add(a.getTitle());
			}
			vo.setActives(StringUtils.join(act, ","));
			
			Integer forwardCount = forwardDAO.findVideoForwardCount(vo.getId());
			vo.setForwardCount(forwardCount);
			
			List<Filter> filters = new ArrayList<Filter>();
			filters.add(Filter.eq("videoId", vo.getId()));
			Order order = new Order(Direction.ASC,"id");
			Sort sort = new Sort(order);
			List<LabelVideo> lvs = labelVideoRepository.findAll(filters, sort);
			List<String> tagList = null;
			if (CollectionUtils.isNotEmpty(lvs)) {
				tagList = new ArrayList<String>(lvs.size());
				for(LabelVideo lv : lvs){
					tagList.add(lv.getLabelName());
				}
				vo.setTag(StringUtils.join(tagList, ","));
			}
		}
		
		Integer count = videoDao.searchUserVideoCount(params);
		Page<Video> pinfo = new PageImpl<Video>(voList,new PageRequest(pageNo-1, pageSize, null),count);
		return pinfo;
	}

	@Override
	public Page findByCreator(Long uid, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return this.videoRepository.findUserVideos(uid, new PageRequest(pageNo-1, pageSize, null));
	}
	
	public List<Video> findIntroductionVideosByActivityId(Long activityId){
		return videoRepository.findIntroductionVideosByActivityId(activityId);
	}
	
	public long updateIntroductionVideosByActivityId(Long activityId){
//		return videoRepository.updateIntroductionVideosByActivityId(activityId);
		return 0;
	}

	@Override
	public int countVideoForRuser(Long id) {
		return videoRepository.countAdminVideoForRuser(id);
	}
	
	//保存手机端上传的视频
	@Transactional
	public boolean saveVideoFromPhone(Video video){
		try{
			this.save(video);
//			videoRepository.incUserVideoCount(video.getCreatorId());
			ruserDAO.incAllVideoCount(video.getCreatorId());
	    	activityService.saveActVideoRelation(video.getActIds(), video.getId());
	    	
	    	//modified by yinhb 2015-06-01 start
	    	Map<String, Object> map = new HashMap<String, Object>();
	    	String tag = video.getTag();
	    	if (org.apache.commons.lang3.StringUtils.isNotBlank(tag)) {
				String[] tags = tag.split(" ");
				String _tag = null;
				Set<String> tagSet = new HashSet<String>();
				for(String tmpTag : tags){
					_tag = tmpTag;
					tagSet.add(_tag);
					/*try {
						Label label = new Label();
						label.setName(_tag);
						label.setNum(0L);
						labelRepository.save(label);
					} catch (Exception e) {
						logger.error("tag name {} already exists", tag);
					}*/
				}
				
				//保存视频tag
				List<LabelVideo> lvList = new ArrayList<LabelVideo>(tagSet.size());
				for(String tmpTag : tags){
					LabelVideo lv = new LabelVideo();
					lv.setLabelName(tmpTag);
					lv.setVideoId(video.getId());
					lv.setLabelId(0L);
					lvList.add(lv);
				}
				labelVideoRepository.save(lvList);
				
				map.put("tags", tagSet);
			}
	    	//modified end
	    	Message msg = new Message();
			msg.setModule(Module.VIDEO);
			msg.setAction(Action.INSERT);
			map.put("uid", video.getCreatorId().toString());
			map.put("vid", video.getId().toString());
			msg.setDataMap(map);
	    	kafkaProducer.send("rest-topic", msg);
	    	return true;
		}catch(Exception e){
			logger.error("保存手机上传视频失败",e);
			return false;
		}
	}
	
	/**
	 * 保存视频数据，主要是因为标签在正文中
	 * @param video
	 * @return
	 */
	@Transactional
	public boolean saveVideoFromPhoneV2(Video video,Set<String> tags){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			//1、保存视频，2、保存tag，3、保存视频tag，4、更新tag使用量
			//1
			this.save(video);
			ruserDAO.incAllVideoCount(video.getCreatorId());
//			activityService.saveActVideoRelation(video.getActIds(), video.getId());
			
			Ruser u=this.ruserService.find(video.getCreatorId());
			activityService.saveActVideoRelation(video.getActIds(), video.getId(),tags,u);
			
			try {
				if (CollectionUtils.isNotEmpty(tags)) {
					//2 利用数据库唯一索引，不做是否存在判断直接插入
					/*String tag = null;
					for(String _tag : tags){
						try {
								tag = _tag;
								Label label = new Label();
								label.setName(_tag);
								label.setNum(0L);
								labelRepository.save(label);
						} catch (Exception e) {
							logger.error("tag name {} already exists", tag);
						}
					}*/
					
					//3 保存视频tag
					List<LabelVideo> lvList = new ArrayList<LabelVideo>(tags.size());
					for(String _tag : tags){
						LabelVideo lv = new LabelVideo();
						lv.setLabelName(_tag);
						lv.setVideoId(video.getId());
						lv.setLabelId(0L);
						lvList.add(lv);
						
						Long nCount=labelRepository.findByName(_tag);
						
						Label label = new Label();
						label.setName(_tag);
						label.setNum(0L);
						
						if(nCount.intValue()==0) {
							
							labelRepository.save(label);
							
							solrWoPaiTagService.index(label.getId(), label.getName(),label.getNum());
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
					labelVideoRepository.save(lvList);
					
					for (LabelVideo labelVideo : lvList) {
						solrWoPaiVideoTagService.index(labelVideo.getId(), labelVideo.getLabelName(), labelVideo.getVideoId());
						
					}
					
					
					//4
					map.put("tags", tags);
				}
			} catch (Exception e) {
				logger.error("标签插入solr失败",e);
			}
			Message msg = new Message();
			msg.setModule(Module.VIDEO);
			msg.setAction(Action.INSERT);
			map.put("uid", video.getCreatorId().toString());
			map.put("vid", video.getId().toString());
			msg.setDataMap(map);
			kafkaProducer.send("rest-topic", msg);
			
		}catch(Throwable e){
			logger.error("保存手机上传视频失败",e);
			return false;
		}
		return true;
	}
	
	//获取视频赞用户列表
	public List<Map<String,Object>> getVideoPraiseUserList(Long vid ,Long pid,Integer count,Long uid){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("vid", vid);
		params.put("pid", pid);
		params.put("count", count);
		params.put("uid", uid);
		return videoDao.getVideoPraiseUserList(params);
	}
	
	//获取用户视频，包括用户自己发的以及用户转发的
	public List<Video> findUserVideos(Long userid ,Date timestamp,Integer count,Long uid){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userid", userid);
		params.put("timestamp", timestamp);
		params.put("count", count);
		params.put("uid", uid);
		
		Integer type=null;
		if(type==null) {
			if(BicycleConstants.VIDEO_BACK_FLG) {
				
			}else {
				//type=134;
			}
		}
		params.put("type", type);
		
		List<Video> list = null; 
		//用户转发过视频，查询用户自己发的和转发过的视频
		if (forwardDAO.hasForwarded(userid)>0){
			list = videoDao.findUserVideosAndForward(params); 
			for(Video video:list){
				if (video.getForwardUserId() > 0){ //将视频作者信息放入到视频信息当中
					video.setUser(ruserService.find(video.getCreatorId()));
				}
			}
		} else { //用户没转发过视频，只查询用户自己发的视频
			list = videoDao.findUserVideos(params); 
		}
		
		return list;
	}
	
	

	@Override
	public Page searchNewVideoList(Integer pageNo, Integer pageSize,Map<String, Object> params) {
		List<VideoVO> voList = videoDao.searchNewVideo(params);
		for(VideoVO vo:voList){
			List<Activity> acts = activityService.findActivityidByVideoid(vo.getId());
			String act = "";
			for(Activity a:acts){
				act += a.getTitle()+",";
			}
			if(!"".equals(act)){
				act = act.substring(0, act.length()-1);
			}
			vo.setActivity(act);
		}
		
		Integer count = videoDao.searchNewVideoCount(params);
		Page<VideoVO> pinfo = new PageImpl<VideoVO>(voList,new PageRequest(pageNo-1, pageSize, null),count);
		return pinfo;
	}

	@Override
	public List<Long> findMyVideoIdList(Long uid) {
		return videoRepository.getVideoIdListByUid(uid);
	}

	//根据热度指数（播放指数+点赞指数+评论指数）排序，查询视频
	@Override
	public List<Video> findHotIndiceVideos(Integer startIndex, Integer count,String uid,Long activityId) {
		List<Video> hotVideos = new ArrayList<Video>();
		if (activityId != 0){ //activityId !=0,查询某个活动下的热度视频
			hotVideos = videoRepository.findHotIndiceActivityVideos(startIndex, count,activityId);
		} else { //activityId=0,查询所有视频的热度排行
			hotVideos = videoRepository.findHotIndiceVideos(startIndex, count);
		}
		for(Video video:hotVideos){
			if(uid!=null&&!uid.trim().equals("")){
	    		video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
	    		video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
	    		video.setAttentionAuthor(attentionService.isAttention(Long.parseLong(uid), video.getCreatorId()));
    		}
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
				video.setTags(tagList);
			}
		}
		return hotVideos;
	}

	//查询当前用户最新的一个视频或多个
	public List<Video> getNewVideoByUid(Long uid,int limitnum){
		return videoRepository.getNewVideoByUid(uid,limitnum);
	}

	@Override
	public List<Video> findShowVideo(Long videoId,String refVideoId, String uid,Long timestamp,Integer count) {
		List<Video> list = new ArrayList<Video>();
		//查询我拍秀视频
		Video showVideo = videoRepository.getOneVideo(videoId);
		if (showVideo != null) {
			list.add(showVideo);
		}
		
		//查询关联视频
		Map<String,Object> params = new HashMap<String,Object>(); 
		params.put("timestamp", (timestamp==null||timestamp.longValue()<=0)?null:new Date(timestamp));
		params.put("count", count); 
		params.put("uid", uid==null||"".equals(uid)?0:uid); 
		params.put("ids", refVideoId.split(",")); 
		List<Video> refVideos = videoDao.findRefVideos(params);
		list.addAll(refVideos);
		
    	for (Video video:list){
    		
    		if(uid!=null&&!uid.trim().equals("")){
    			video.setPraise(this.praised(Long.parseLong(uid), video.getId()));
	    		video.setFavorite(this.favorited(Long.parseLong(uid), video.getId()));
    			video.setAttentionAuthor(attentionService.isAttention(Long.valueOf(uid), video.getCreatorId()));
    		} 
    		
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
		return list;
	}

	@Override
	public void dayHotVideosToRedis() {
		List<Video> dayHotVideos = videoDao.findDayHotTop50(50);
		if(dayHotVideos!=null && dayHotVideos.size()>0){
			jedisService.setObjectAsByte(BicycleConstants.DAY_HOT_VIDEOS,SerializationUtils.serialize(dayHotVideos));
			jedisService.set(BicycleConstants.HOT_VIDEOS_UPDATE_TIME, (new Date().getTime())+"");
			logger.info("======当日最热视频排行，前50======size:"+dayHotVideos.size());
		}
	}

	@Override
	public List<Video> findDayHotVideosRank(Long uid,Integer start,Integer count) {
		@SuppressWarnings("unchecked")
		List<Video> videoRank = (List<Video>)jedisService.getObject(BicycleConstants.DAY_HOT_VIDEOS);
		Video userVideo = null;
		if(videoRank != null && videoRank.size()>0){
			if(uid != null && uid>0){
				List<Video> userVideos = videoDao.findUserVideoByDayHotValue(uid);
				if(userVideos != null && userVideos.size()>0){
					userVideo = userVideos.get(0);
				}
			} 
			if(userVideo != null){
				for(int i=0;i<videoRank.size();i++){
					Video v = videoRank.get(i);
					if(v.getId().equals(userVideo.getId())){
						userVideo.setHotRank(i+1);
						break;
					}
				}
			}
			if(start == null || start.intValue()>=videoRank.size()){
				start = 0;
			}
			if(count == null || count.intValue()>=videoRank.size()){
				count = videoRank.size() - start;
			}
			if((start + count)<=videoRank.size()){
				videoRank = videoRank.subList(start, start+count);
			}
			if(uid != null && uid>0){
				for(Video v:videoRank){
					v.setPraise(this.praised(uid, v.getId()));
		    		v.setFavorite(this.favorited(uid, v.getId()));
				}
				if(userVideo != null){
					videoRank.add(userVideo);
				}
			}
			
			for(Video v:videoRank){
	    		List<Filter> filters = new ArrayList<Filter>();
				filters.add(Filter.eq("videoId", v.getId()));
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
	    		
				v.setTags(tagList);
			}
		}
		return videoRank;
	}

	@Override
	public void updateHotPointByVideoId(Long id) {
		logger.info("update video hotPoint");
		String tempPraiseWeight = jedisService.getValueFromMap(BicycleConstants.HOT_WEIGHT_OF_ACTIVITY, BicycleConstants.PRIASE_WEIGHT);
		String tempEvaluationWeight = jedisService.getValueFromMap(BicycleConstants.HOT_WEIGHT_OF_ACTIVITY, BicycleConstants.EVALUATION_WEIGHT);
		String tempPlayWeight = jedisService.getValueFromMap(BicycleConstants.HOT_WEIGHT_OF_ACTIVITY, BicycleConstants.PLAY_WEIGHT);
		
		Integer praiseWeight = Integer.valueOf(tempPraiseWeight == null || tempPraiseWeight.equals("")?"25":tempPraiseWeight);
		Integer evaluationWeight = Integer.valueOf(tempEvaluationWeight == null || tempEvaluationWeight.equals("")?"25":tempEvaluationWeight);
		Integer playWeight = Integer.valueOf(tempPlayWeight == null || tempPlayWeight.equals("")?"25":tempPlayWeight);
		
		videoRepository.updateHotPointByVideoId(id,praiseWeight,evaluationWeight,playWeight);
	}

	@Override
	public void completeVideoDuration(int count) {
		List<Video> list = videoDao.findNoneDurationVideos(count);
		for (Video video:list) {
			try {
				if (video.getPlayKey() == null || "".equals(video.getPlayKey())) {
					videoRepository.updateVideoDuration(video.getId(), "0");
					continue;
				}
				String duration = getDurationFromFile(video.getPlayKey());
				
				if (duration !=null && !"".equals(duration))
					videoRepository.updateVideoDuration(video.getId(), duration);
			} catch (Exception e) {
				logger.error("更新视频播放时长出错",e);
			}
		}
	}
	
	/**
	 * 从文件中读取视频播放时长
	 * @param playKey
	 * @return
	 */
	private String getDurationFromFile(String playKey){
		if (playKey == null || "".equals(playKey))
			return null;
		String duration = "";
		String filePathSuffix = playKey.replace("-", File.separator)+".nfo";
		String filePath = video_download_url_prefix+filePathSuffix;
		File file = new File(filePath);
		try {
			String content = FileUtils.readFileToString(file);
			if (content != null && !"".equals(content)) {
				JSONObject json = JSONObject.fromObject(content);
				duration = String.valueOf(json.get("duration"));
			}
		} catch (IOException e) {
			duration = "0"; //获取不到视频文件，时长更新为0
			logger.error("读取视频信息文件失败,playKey="+playKey+",filePath="+filePath,e);
		}
		return duration;
	}
	
	public void excuteVideoDayHotValue(Long vid){
		Map<String,Object> params = new HashMap<String,Object>();
		String playCountRate = jedisService.getValueFromMap(BicycleConstants.DAY_HOT_VIDEOS_WEIGHT, BicycleConstants.DAY_PLAYCOUNT_WEIGHT);
		Double playRate = new Double(0.5);
		if(StringUtils.isNotBlank(playCountRate)){
			playRate = Double.parseDouble(playCountRate);
		} 
		String evaluationCountRate = jedisService.getValueFromMap(BicycleConstants.DAY_HOT_VIDEOS_WEIGHT, BicycleConstants.DAY_EVALUATIONCOUNT_WEIGHT);
		Double evaluationRate = new Double(0.2);
		if(StringUtils.isNotBlank(evaluationCountRate)){
			evaluationRate = Double.parseDouble(evaluationCountRate);
		} 
		String praiseCountRate = jedisService.getValueFromMap(BicycleConstants.DAY_HOT_VIDEOS_WEIGHT, BicycleConstants.DAY_PRAISECOUNT_WEIGHT);
		Double praiseRate = new Double(0.3);
		if(StringUtils.isNotBlank(praiseCountRate)){
			praiseRate = Double.parseDouble(praiseCountRate);
		} 
		Video v = this.find(vid);
		if(v != null){
			Integer playCount = v.getPlayCount();//videoDao.dayVideoPlayCount(vid);
			Integer evaluationCount = v.getEvaluationCount();//videoDao.dayVideoEvaCount(vid);
			Integer praiseCount = v.getPraiseCount();//videoDao.dayVideoPraiseCount(vid);
			Double dayHotValue = playRate*playCount + evaluationRate*evaluationCount + praiseRate*praiseCount;
			logger.info("execute video day hot value,vid:{},value:{}",vid,dayHotValue);
			params.put("vid", vid);
			params.put("hotValue", dayHotValue);
			videoDao.execDayHotVideoProc(params);
		}

	}

	/**
	 * 获取Video信息列表
	 * @param params
	 * @Author huoshanwei
	 * @return Video List
	 */
	@Override
	public List<Video> queryAdministratorVideos(Map<String,Object> params){
		/*Map<String,Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("description", StringUtils.isEmpty(description) ? null : description);
		params.put("tag", StringUtils.isEmpty(tag) ? null : tag);
		if(!StringUtils.isEmpty(flowStat)){
			params.put("flowStat", flowStat);
		}else{
			if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
				params.put("flowStat", "published");
			}
		}
		params.put("isLogo", StringUtils.isEmpty(isLogo)? null : isLogo);
		params.put("activityId", StringUtils.isEmpty(activities) ? null : activities);
		params.put("username", StringUtils.isEmpty(username) ? null : username);
		params.put("pageStart", (pr.getPageNumber()-1) * pr.getPageSize());
		params.put("pageSize", pr.getPageSize());*/
		return adminVideoMapper.selectAdministratorVideos(params);
	}

	/**
	 * 获取Video信息列表COUNT
	 * @param params
	 * @return Video List COUNT
	 */
	@Override
	public Integer queryAdministratorVideoCount(Map<String,Object> params){
		/*Map<String,Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("description", StringUtils.isEmpty(description) ? null : description);
		params.put("tag", StringUtils.isEmpty(tag) ? null : tag);
		if(!StringUtils.isEmpty(flowStat)){
			params.put("flowStat", flowStat);
		}else{
			if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
				params.put("flowStat", "published");
			}
		}
		params.put("isLogo", StringUtils.isEmpty(isLogo)? null : isLogo);
		params.put("activityId", StringUtils.isEmpty(activities) ? null : activities);
		params.put("username", StringUtils.isEmpty(username) ? null : username);*/
		return adminVideoMapper.selectAdministratorVideoCount(params);
	}

	@Override
	public Video findVideoDetail(Long id) {
		Video v = this.find(id);
		Ruser user = this.ruserService.find(v.getCreatorId());
		v.setUploader(user.getName());
		
		List<Activity> acts = activityService.findActivityidByVideoid(v.getId());
		List<String> act = new ArrayList<String>();
		for(Activity a:acts){
			act.add(a.getTitle());
		}			
		v.setActives(StringUtils.join(act, ","));
		
		Integer forwardCount = forwardDAO.findVideoForwardCount(v.getId());
		v.setForwardCount(forwardCount);
		
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(Filter.eq("videoId", v.getId()));
		Order order = new Order(Direction.ASC,"id");
		Sort sort = new Sort(order);
		List<LabelVideo> lvs = labelVideoRepository.findAll(filters, sort);
		List<String> tagList = null;
		if (CollectionUtils.isNotEmpty(lvs)) {
			tagList = new ArrayList<String>(lvs.size());
			for(LabelVideo lv : lvs){
				tagList.add(lv.getLabelName());
			}
			v.setTag(StringUtils.join(tagList, ","));
		}
		
		return v;
	}

	@Override
	public Page searchHotVideoList(Integer page, Integer rows,Map<String, Object> params) {
		List<Video> voList = videoDao.searchHotVideo(params);
		for(Video vo:voList){
			Ruser user = this.ruserService.find(vo.getCreatorId());
			vo.setUploader(user.getName());
			
			List<Activity> acts = activityService.findActivityidByVideoid(vo.getId());
			List<String> act = new ArrayList<String>();
			for(Activity a:acts){
				act.add(a.getTitle());
			}			
			vo.setActives(StringUtils.join(act, ","));
			
			Integer forwardCount = forwardDAO.findVideoForwardCount(vo.getId());
			vo.setForwardCount(forwardCount);
			
			List<Filter> filters = new ArrayList<Filter>();
			filters.add(Filter.eq("videoId", vo.getId()));
			Order order = new Order(Direction.ASC,"id");
			Sort sort = new Sort(order);
			List<LabelVideo> lvs = labelVideoRepository.findAll(filters, sort);
			List<String> tagList = null;
			if (CollectionUtils.isNotEmpty(lvs)) {
				tagList = new ArrayList<String>(lvs.size());
				for(LabelVideo lv : lvs){
					tagList.add(lv.getLabelName());
				}
				vo.setTag(StringUtils.join(tagList, ","));
			}
		}
		
		Integer count = videoDao.searchHotVideoCount(params);
		Page<Video> pinfo = new PageImpl<Video>(voList,new PageRequest(page-1, rows, null),count);
		return pinfo;
	}

	@Override
	public BigDecimal findMaxPlayRate() {
		return videoDao.findMaxPlayRate();
	}

	@Override
	public void moveHotVideoUp(Long id) {
		Video current = this.find(id);
		BigDecimal playRate = current.getPlayRateToday();
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("playRate", playRate);
		List<Video> upper = videoDao.findVideoUperPlayRate(params);
		if(upper != null && upper.size()>0){
			Video up = upper.get(0);
			up = this.find(up.getId());
			current.setPlayRateToday(up.getPlayRateToday());
			up.setPlayRateToday(playRate);
			this.update(current);
			this.update(up);
		}
	}

	@Override
	public void moveHotVideoDown(Long id) {
		Video current = this.find(id);
		BigDecimal playRate = current.getPlayRateToday();
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("playRate", playRate);
		List<Video> lower = videoDao.findVideoLowerPlayRate(params);
		if(lower != null && lower.size()>0){
			Video low = lower.get(0);
			low = this.find(low.getId());
			current.setPlayRateToday(low.getPlayRateToday());
			low.setPlayRateToday(playRate);
			this.update(current);
			this.update(low);
		}
	}

	@Override
	public void removeHotVideos(List<Long> idList) {
		if(idList != null && idList.size()>0){
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("ids", idList);
			videoDao.removeHotVideos(params);	
		}
	}

	@Override
	public List<Video> queryVideosByCreatorId(Long creatorId){
		return videoDao.selectVideosByCreatorId(creatorId);
	}

	@Override
	public void queryHotVideosToRedis(String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<Video> hotVideos = null;
		List<Video> hotVideoList = null;
		params.put("type", type);
		params.put("count", 50);
		if ("day".equals(type)) {
			params.put("typeCount", 24);//24小时
			hotVideos = videoDao.selectHotVideoRankingList(params);
			jedisService.set(BicycleConstants.TWENTY_FOUR_HOUR_HOT_VIDEOS_TIME_PARAMS, "24");
			if(hotVideos.size() < 50){
				//不满五十条 往前一天读取 依次类推
				params.put("typeCount", 48);//48小时
				hotVideoList = videoDao.selectHotVideoRankingList(params);
				jedisService.set(BicycleConstants.TWENTY_FOUR_HOUR_HOT_VIDEOS_TIME_PARAMS, "48");
				if(hotVideoList.size()<50){
					params.put("typeCount", 72);//72小时
					hotVideoList = videoDao.selectHotVideoRankingList(params);
					jedisService.set(BicycleConstants.TWENTY_FOUR_HOUR_HOT_VIDEOS_TIME_PARAMS, "72");
					if(hotVideoList.size()<50){
						params.put("typeCount", 196);//196小时
						hotVideoList = videoDao.selectHotVideoRankingList(params);
						jedisService.set(BicycleConstants.TWENTY_FOUR_HOUR_HOT_VIDEOS_TIME_PARAMS, "196");
					}
				}
				hotVideos = hotVideoList;
			}

		} else {
			hotVideos = videoDao.selectHotVideoRankingList(params);
		}

		logger.info("======TWENTY_FOUR_HOUR_HOT_VIDEOS_TIME_PARAMS======result:" + jedisService.get(BicycleConstants.TWENTY_FOUR_HOUR_HOT_VIDEOS_TIME_PARAMS));
		if (hotVideos != null && hotVideos.size() > 0) {
			if ("day".equals(type)) {
				jedisService.setObjectAsByte(BicycleConstants.TWENTY_FOUR_HOUR_HOT_VIDEOS, SerializationUtils.serialize(hotVideos));
				jedisService.set(BicycleConstants.TWENTY_FOUR_HOUR_HOT_VIDEOS_UPDATE_TIME, (new Date().getTime()) + "");
				logger.info("======24小时最热视频排行，前50======size:" + hotVideos.size());
			} else if ("week".equals(type)) {
				jedisService.setObjectAsByte(BicycleConstants.WEEK_HOT_VIDEOS, SerializationUtils.serialize(hotVideos));
				jedisService.set(BicycleConstants.WEEK_HOT_VIDEOS_UPDATE_TIME, (new Date().getTime()) + "");
				logger.info("======周最热视频排行，前50======size:" + hotVideos.size());
			} else if ("month".equals(type)) {
				jedisService.setObjectAsByte(BicycleConstants.MONTH_HOT_VIDEOS, SerializationUtils.serialize(hotVideos));
				jedisService.set(BicycleConstants.MONTH_HOT_VIDEOS_UPDATE_TIME, (new Date().getTime()) + "");
				logger.info("======月最热视频排行，前50======size:" + hotVideos.size());
			} else if ("year".equals(type)) {
				jedisService.setObjectAsByte(BicycleConstants.YEAR_HOT_VIDEOS, SerializationUtils.serialize(hotVideos));
				jedisService.set(BicycleConstants.YEAR_HOT_VIDEOS_UPDATE_TIME, (new Date().getTime()) + "");
				logger.info("======年最热视频排行，前50======size:" + hotVideos.size());
			}
		}
	}

	@Override
	public List<Video> queryHotVideoRankingList(String type, Long uid, Integer start, Integer count) {
		List<Video> videoRank = null;
		if ("day".equals(type)) {
			videoRank = (List<Video>) jedisService.getObject(BicycleConstants.TWENTY_FOUR_HOUR_HOT_VIDEOS);
		} else if ("week".equals(type)) {
			videoRank = (List<Video>) jedisService.getObject(BicycleConstants.WEEK_HOT_VIDEOS);
		} else if ("month".equals(type)) {
			videoRank = (List<Video>) jedisService.getObject(BicycleConstants.MONTH_HOT_VIDEOS);
		} else if ("year".equals(type)) {
			videoRank = (List<Video>) jedisService.getObject(BicycleConstants.YEAR_HOT_VIDEOS);
		}
		Video userVideo = null;
		if (videoRank != null && videoRank.size() > 0) {
			if (uid != null && uid > 0) {
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("uid",uid);
				params.put("type",type);
				String typeCount = jedisService.get(BicycleConstants.TWENTY_FOUR_HOUR_HOT_VIDEOS_TIME_PARAMS);
				//System.out.println("+++++++++++++typeCount++++++++++++++++"+typeCount);
				params.put("typeCount",typeCount.equals("")?24:typeCount);
				List<Video>  userVideos = videoDao.selectUserHotVideoRank(params);
				if (userVideos != null && userVideos.size() > 0) {
					userVideo = userVideos.get(0);
				}
			}
			if (userVideo != null) {
				for (int i = 0; i < videoRank.size(); i++) {
					Video v = videoRank.get(i);
					if (v.getId().equals(userVideo.getId())) {
						userVideo.setHotRank(i + 1);
						break;
					}
				}
			}
			if (start == null || start.intValue() >= videoRank.size()) {
				start = 0;
			}
			if (count == null || count.intValue() >= videoRank.size()) {
				count = videoRank.size() - start;
			}
			if ((start + count) <= videoRank.size()) {
				videoRank = videoRank.subList(start, start + count);
			} else {
				videoRank = videoRank.subList(start, videoRank.size());
			}
			if (uid != null && uid > 0) {
				for (Video v : videoRank) {
					v.setPraise(this.praised(uid, v.getId()));
					v.setFavorite(this.favorited(uid, v.getId()));
				}
				if (userVideo != null) {
					videoRank.add(userVideo);
				}
			}

			for (Video v : videoRank) {
				List<Filter> filters = new ArrayList<Filter>();
				filters.add(Filter.eq("videoId", v.getId()));
				Order order = new Order(Direction.ASC, "id");
				Sort sort = new Sort(order);
				List<LabelVideo> lvs = labelVideoRepository.findAll(filters, sort);
				List<String> tagList = null;
				if (CollectionUtils.isNotEmpty(lvs)) {
					tagList = new ArrayList<String>(lvs.size());
					for (LabelVideo lv : lvs) {
						tagList.add(lv.getLabelName());
					}
				}

				v.setTags(tagList);
			}
		}
		return videoRank;
	}

	@Override
	public int findUserVideoCount(Long userId) {
		return videoRepository.findUserVideoCount(userId);
	}

	@Override
	public Video queryVideoById(Long id){
		return videoDao.selectByPrimaryKey(id);
	}

/*	@Override
	public List<ExportWopaiNormalUser> queryWopaiUserByVideo(Map<String,Object> params){
		return videoDao.selectWopaiUserByVideo(params);
	}*/

	@Override
	public Video queryVideoByWeight(Map<String, Object> params) {
		return videoDao.selectVideoByWeight(params);
	}

	@Override
	public int updateSort(Map<String, Object> params) {
		return videoDao.updateSort(params);
	}

	@Override
	@Transactional(readOnly = true,rollbackFor = Exception.class)
	public Map<String,Object> upSort(Long videoId,Video video) throws Throwable{
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Map<String,Object> ascParams = new HashMap<String, Object>();
		ascParams.put("ASC","ASC");
		ascParams.put("weight",video.getWeight());
		Video ascVideo = videoDao.selectVideoByWeight(ascParams);//上移活动信息
		if (ascVideo == null){
			resultMap.put("resultCode","warningAsc");
			resultMap.put("resultMessage","不能再上移了哦！");
			return resultMap;
		}
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id",videoId);
		params.put("weight",ascVideo.getWeight());
		int res = videoDao.updateSort(params);
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("id",ascVideo.getId());
		param.put("weight",video.getWeight());
		int ret = videoDao.updateSort(param);
		if(res > 0 && ret > 0){
			resultMap.put("resultCode","success");
			resultMap.put("resultMessage","排序成功！");
		}else{
			resultMap.put("resultCode","error");
			resultMap.put("resultMessage", "排序失败！");
		}
		return resultMap;
	}

	@Override
	@Transactional(readOnly = true,rollbackFor = Exception.class)
	public Map<String,Object> downSort(Long videoId,Video video) throws Throwable{
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> descParams = new HashMap<String, Object>();
		descParams.put("DESC","DESC");
		descParams.put("weight",video.getWeight());
		descParams.put("videoId",videoId);
		Video descVideo = videoDao.selectVideoByWeight(descParams);//下移活动信息
		if(descVideo == null){
			map.put("resultCode","warningDesc");
			map.put("resultMessage","不能再下移了哦！");
			return map;
		}
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id",videoId);
		params.put("weight",descVideo.getWeight());
		int res = videoDao.updateSort(params);
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("id",descVideo.getId());
		param.put("weight",video.getWeight());
		int ret = videoDao.updateSort(param);
		if(res > 0 && ret > 0){
			map.put("resultCode","success");
			map.put("resultMessage","排序成功！");
		}else{
			map.put("resultCode","error");
			map.put("resultMessage","排序失败！");
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> findHotVideosV3(Integer start, Integer end) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pageStart", start);
		params.put("pageSize", end);
		
		List<Map<String,Object>> result = videoDao.searchHotVideoV3(params);
		
		if(result!=null && result.size()>0){
			for(Map<String,Object> m:result){
				Long uid = (Long)m.get("creatorId");
				Ruser user = ruserService.find(uid);
				m.put("user", user);
			}
		}
		
		return result;
	}

	/**
	 * 根据条件查询视频信息
	 * @param params
	 * @return
	 */
	@Override
	public List<Video> select(Map<String,Object> params){
		return videoDao.select(params);
	}

	@Override
	public Video getPlaybackByRoomId(Long roomId) {
		List<Video> list = videoRepository.getPlaybackByRoomId(roomId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public Video getDeletePlaybackByRoomId(Long roomId) {
		List<Video> list = videoRepository.getDeletePlaybackByRoomId(roomId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Video getVideoByLiveNoticeId(Long liveNoticeId) {
		List<Video> list = videoRepository.getVideoByLiveNoticeId(liveNoticeId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Video queryVideoByLiveNoticeId(Long liveNoticeId) {
		return videoDao.selectVideoByLiveNoticeId(liveNoticeId);
	}

	@Override
	public int insertMissPlayback(MissingPlaybackInfo missingPlaybackInfo) {
		return videoDao.insertMissPlayback(missingPlaybackInfo);
	}

	@Override
	public int updateMissPlaybackByPersistentId(MissingPlaybackInfo missingPlaybackInfo) {
		return videoDao.updateMissPlaybackByPersistentId(missingPlaybackInfo);
	}

	@Override
	public MissingPlaybackInfo selectMissPlaybackByPersistentId(String persistentId) {
		return videoDao.selectMissPlaybackByPersistentId(persistentId);
	}

	@Override
	public List<MissingPlaybackInfo> queryMPByStreamId(String streamId){
		return videoDao.selectMPByStreamId(streamId);
	}
}
