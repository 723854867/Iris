package com.busap.vcs.restadmin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busap.vcs.base.*;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.*;
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.enums.UserType;
import com.busap.vcs.data.enums.VideoStatus;
import com.busap.vcs.data.model.EvaluationDisplay;
import com.busap.vcs.restadmin.quartz.Quartz;
import com.busap.vcs.restadmin.utils.*;
import com.busap.vcs.service.*;
import com.busap.vcs.service.kafka.producer.KafkaProducer;
import com.busap.vcs.service.utils.EncodeUtils;
import com.busap.vcs.service.utils.HttpPostUtils;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.Constants;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.controller.CRUDForm;
import com.busap.vcs.webcomn.util.PictureScaleUtil;
import org.apache.commons.httpclient.Header;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 视频
 * 
 * @author linghai.kong
 * 
 */
@Controller()
@RequestMapping("video")
public class VideoController extends CRUDController<Video, Long> {

	private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

	@Resource(name = "videoService")
	private VideoService videoService;

//	@Resource(name = "tagService")
//	private TagService tagService;
	
	@Value("${uploadfile_remote_url}")
	private String uploadfile_remote_url;
	
	@Value("${video_download_url_prefix}")
	private String video_download_url_prefix;
	
	@Value("${video_play_url_prefix}")
	private String video_play_url_prefix;
	
	@Value("${uploadpic_url_prefix}")
	private String uploadpic_url_prefix;
	
	@Value("${uploadpic_path}")
	private String uploadpic_path;
	
	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name = "activityService")
	private ActivityService activityService;
	
	@Resource(name = "activityVideoService")
	private ActivityVideoService activityVideoService;
	
	@Resource(name = "templateService")
	private TemplateService templateService;
	
	@Resource(name = "evaluationService")
	private EvaluationService evaluationService;
	
	@Resource(name = "jedisService")
	private JedisService jedisService;
	
	@Resource(name = "labelService")
	private LabelService labelService;
	
	@Value("${files.localpath}")
	private String basePath;

	@Resource
	private AnchorService anchorService;

	@Resource
	private RoomService roomService;

	private static final String permissionId = "";

	@Autowired
	@Qualifier("quartz")
	private Quartz quartz;
	
	@Autowired
	private UserService userService;
	
	@Autowired
    KafkaProducer kafkaProducer;

	@Resource(name = "videoService")
	@Override
	public void setBaseService(BaseService<Video, Long> baseService) {
		this.baseService = baseService;
	}
	
	/**
	 * 检测有新视频进行消息提醒
	 * @return
	 */
	@RequestMapping("checkNew")
	@ResponseBody
	public String checkNew(){
//		User u=this.userService.find(U.getUid());
//		List<Role> rList=u.getRoles();
		long num = jedisService.getSortedSetSizeFromShard(Contant.VIDEO_CHECK);
		if(num > 0){
			return "YES";
		}
		return "NO";
	}

	@RequestMapping("index")
	public String list(HttpServletRequest req,
			@RequestParam(value = "popFlg", required = false)  String popFlg
			) {
		List list=ruserService.findByType(UserType.马甲.getName());
		req.setAttribute("uploaduserlist", list);
		
		List activityList = this.activityService.findAll();
		req.setAttribute("activityList", activityList);

//		List tagList = this.tagService.findByStatus(TagStatus.活动.getName());
//		req.setAttribute("tagList", tagList);
		
		List<Filter> filters = new ArrayList<Filter>();
		Sort sort = new Sort(Direction.ASC,"orderNum");
		Filter filter= Filter.eq("type", 0);
		filters.add(filter);
		List tList = templateService.findAll(0,1000,filters,sort);
		req.setAttribute("templateList", tList);

		req.setAttribute("uploadfile_remote_url", uploadfile_remote_url);
		req.setAttribute("video_play_url_prefix", video_play_url_prefix);
		req.setAttribute("show", request.getParameter("show"));

		return "video/upload";
	}
	
	@RequestMapping("popUpload")
	public String popUpload(HttpServletRequest req,
			@RequestParam(value = "popFlg", required = false)  String popFlg
			) {
		req.setAttribute(popFlg, popFlg);
		return list( req,popFlg);
	}

	@EnableFunction("管理员视频,查看管理员视频信息")
	@RequestMapping("checledVideos")
	public String checledVideos(Integer page, Integer size, String description,
			String tag, String status,Integer selIndex,
			String isLogo,String activities,String username,HttpServletRequest request) {
		Sort s = new Sort(Direction.DESC,"createDate");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		this.request.setAttribute("startTime", startTime);
		this.request.setAttribute("endTime",  endTime);
		Pageable pr = new PageRequest(page == null ? 1 : page, size == null ? 10 : size,s);
		List list=ruserService.findByType(UserType.马甲.getName());
		this.request.setAttribute("uploaduserlist", list);
		
		Page<Video> ret = videoService.searchAdminVideo(startTime, endTime, description, tag,status, isLogo, activities, username,pr);
		
		this.request.setAttribute("checledVideos", ret.getContent());
		this.request.setAttribute("pageinfo", ret);
//		this.request.setAttribute("title", title);
		this.request.setAttribute("description", description);
		this.request.setAttribute("tag", tag);
		this.request.setAttribute("activityid", activities);
		this.request.setAttribute("isLogo", isLogo);
		this.request.setAttribute("ruserid", username);
		this.request.setAttribute("selIndex", selIndex);
		this.request.setAttribute("status", status); 
		this.request.setAttribute("video_play_url_prefix", video_play_url_prefix);
		this.request.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix); 
		
		List<Activity> activityList = this.activityService.findAll();
		this.request.setAttribute("activites", activityList);
		
		return "video/videopublish";
	}

	@EnableFunction("最热视频,查看最热视频信息")
	@RequestMapping("hotVideos")
	public String hotVideos(HttpServletRequest req) throws Exception {
		List<Activity> activityList = this.activityService.findAll();
		req.setAttribute("activites", activityList);
		req.setAttribute("video_play_url_prefix", video_play_url_prefix);
		req.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		req.setAttribute("uploadfile_remote_url", uploadfile_remote_url);
		return "video/hotVideos";
	}
	
	@RequestMapping("publish") 
	@ResponseBody
	public String publish(Long [] videoIds){ 
		videoService.updateVideoStatusByIds(U.getUid(),VideoStatus.已发布.getName(), Arrays.asList(videoIds)); 
		return "ok";
	}
	
	@RequestMapping("changePlayCount") 
	@ResponseBody
	public String changePlayCount(Long videoId,Integer playCount){ 
		videoService.updatePlayCount( videoId, playCount); 
		return "ok";
	}
	
	@RequestMapping("changePlayRateToday") 
	@ResponseBody
	public RespBody changePlayRateToday(Long videoId,BigDecimal playRateToday){ 
		videoService.updatePlayRateToday( videoId, playRateToday); 
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("updatePlayRateState") 
	@ResponseBody
	public String updatePlayRateState(Long videoId,Integer playRateState){ 
		videoService.updatePlayRateState( videoId, playRateState); 
		return "ok";
	}
	
	@RequestMapping("updateActivityVideoOrderNum") 
	@ResponseBody
	public String updateActivityVideoOrderNum(Long videoId,Long activityId,Integer orderNum){ 
		
		
        
        StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("FROM ActivityVideo av where 1=1 ");
        countJpql.append("SELECT COUNT(*)  FROM  ActivityVideo av   where 1=1   ");
        
        if(videoId!=null) {
			jpql.append(" AND av.videoid = :videoId ");
			countJpql.append(" AND av.videoid = :videoId ");
			
			ParameterBean paramBean=new ParameterBean("videoId",videoId,null);
			paramList.add(paramBean);
		}
        
        if(activityId!=null) {
			jpql.append(" AND av.activityid = :activityId ");
			countJpql.append(" AND av.activityid = :activityId ");
			
			ParameterBean paramBean=new ParameterBean("activityId",activityId,null);
			paramList.add(paramBean);
		}
        
        List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("activityid",1,"av");
        orderByList.add(orderByObject);
        
        List avList;
		try {
			avList = videoService.getObjectByJpql(jpql, 0, 1, "video", paramList, null, orderByList);
			 if(avList!=null&&avList.size()>0) {
		        	ActivityVideo av=(ActivityVideo) avList.get(0);
		    		av.setOrderNum(orderNum);
		    		activityVideoService.update(av);
		        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
       
		 
		return "ok";
	}
	
	@RequestMapping("changeLogoState") 
	@ResponseBody
	public String changeLogoState(Long videoId,String state){ 
		videoService.updateLogoState( videoId, state); 
		return "ok";
	}
	
	@RequestMapping("deleteVideo") 
	@ResponseBody
	public String deleteVideo(Long [] videoIds) throws SolrServerException, IOException{ 
		videoService.updateVideoStatusByIds(U.getUid(),VideoStatus.已删除, Arrays.asList(videoIds)); 
		return "ok";
	}
	
	@RequestMapping("deletePhysicalVideo") 
	@ResponseBody
	public void deletePhysicalVideo() throws SolrServerException, IOException{ 
		
		StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("FROM Video v where 1=1 and v.flowStat='delete' ");
        countJpql.append("SELECT COUNT(*)  FROM  Video v   where 1=1  and v.flowStat='delete'  ");
        
        
        List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("id",0,"v");
        orderByList.add(orderByObject);
        
        Long size=0l;
        List vList;
		try {
			vList = videoService.getObjectByJpql(jpql, 0, 10000, "video", paramList, null, orderByList);
			if(vList!=null&&vList.size()>0) {
			for (Object object : vList) {
				Video v=(Video) object;
				String[] playKey=v.getPlayKey().split("-");
				
				if(playKey==null||playKey.length<5) {
					
				}else {
//					video_play_url_prefix="/restadmin/downloads/";
//					String path=video_play_url_prefix;
//					video_download_url_prefix="D:\\home\\maishi\\com.busap.vcs.hlsmedia\\files\\";
					String path=video_download_url_prefix;
					
					int count=0;
					for(int i=0;i<playKey.length;i++) {
						path+=playKey[i]+File.separator;
//						path+=playKey[i]+"/";
						count++;
						if(count==4) {
							break;
						}
					}
					
					System.out.println(path);
					System.out.println(v.getId()+"===========================================deleted");
					
					size=deleteAllFilesOfDir(new File(path),size);
					
					System.out.println(size+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				}
				
				System.out.println(size+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				
			}
				System.out.println(vList.size()+"countcountcountcountcountcountcountcountcountcountcount");
			}
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  void deleteAllFilesOfDir(File path) {  
	    if (!path.exists())  
	        return;  
	    if (path.isFile()) {  
	        path.delete();  
	        return;  
	    }  
	    File[] files = path.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        deleteAllFilesOfDir(files[i]);  
	    }  
	    path.delete();  
	}  
	
	public  Long deleteAllFilesOfDir(File path,Long size) {  
	    if (!path.exists())  
	    	return size;  
	    if (path.isFile()) { 
	    	size+=path.length();
	        path.delete();  
	        return size;  
	    }  
	    File[] files = path.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        size=deleteAllFilesOfDir(files[i],size);  
	    }  
	    path.delete();  
	    return size;
	}
	
	
	@RequestMapping("deletePhysicalVideoNotInTable") 
	@ResponseBody
	public void deletePhysicalVideoNotInTable(Long jumpNum,String startDate,String endDate) throws SolrServerException, IOException{ 
		
		if(jumpNum==null) {
			jumpNum=3l;
		}
		
		Long size=0l;
		int vCount=0;
		int allCount=0;
		
//		video_download_url_prefix="D:\\home\\maishi\\com.busap.vcs.hlsmedia\\files\\";
		String rootPath=video_download_url_prefix;
		
		File rootDir=new File(rootPath);
		
		if (!rootDir.exists()) {
			return;
		}
		
	    if (rootDir.isFile()) {  
	        return;  
	    }  
	    
	    isFileNotExistsReturn(rootDir);
		isFileReturn(rootDir);
		
		for(int i=0;i<1;i++) {
			
//			String playKey="";
		    File[] filesJ = rootDir.listFiles(new DateFilter(startDate, endDate, null));  
		    labelJ:for (int j = 0; j < filesJ.length; j++) { 
				    	if (filesJ[j].isFile()) {  
					        continue;  
					    }
				    	String playKeyJ=filesJ[j].getName();
				    	String pathJ=video_download_url_prefix;
				    	pathJ+=filesJ[j].getName()+File.separator;
				    	File[] filesK = filesJ[j].listFiles();
		    	labelK:for (int k = 0; k < filesK.length; k++) {
			    		if (filesK[k].isFile()) {  
			    	        continue;  
			    	    }
			    		String playKeyK=playKeyJ+"-"+filesK[k].getName();
			    		String pathK=pathJ;
			    		pathK+=filesK[k].getName()+File.separator;
			    		File[] filesL = filesK[k].listFiles();
	    		 labelL:for (int l = 0; l < filesL.length; l++) {
			    			if (filesL[l].isFile()) {  
			    		        continue;  
			    		    }
			    			String playKeyL=playKeyK+"-"+filesL[l].getName();
			    			String pathL=pathK;
			    			pathL+=filesL[l].getName()+File.separator;
				    		File[] filesM = filesL[l].listFiles();
		    		 labelM:for (int m = 0; m < filesM.length; m++) {
			    				if (filesM[m].isFile()) {  
			    			        continue;  
			    			    }
			    				String playKeyM=playKeyL+"-"+filesM[m].getName();
			    				String pathM=pathL;
			    				pathM+=filesM[m].getName()+File.separator;
					    		File[] filesN = filesM[m].listFiles(new MP4FileFilter());
					    		boolean isExist=true;
			    		 labelN:for (int n = 0; n < filesN.length; n++) {
					    			 if(filesN[n].isFile()){
					    				 
					    				 String fileName[] =filesN[n].getName().split("\\.");
					    				 if(fileName.length>0) {
					    					 if(fileName[0]!=null&&!fileName[0].equals("")&&fileName[0].length()==12) {
					    						 String playKeyN=playKeyM+"-"+fileName[0];
					    						 
					    						 System.out.println(playKeyN+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<playkey");
							    				 isExist=isPlayKeyExists(playKeyN);//判断playkey是不是存在，如果不存在，返回上层做删除操作
							    				 break;
					    					 }
					    					 
					    				 }
					    				 
					    			 }else {
					    				 
					    			 }
						    	}
					    		
					    		allCount++;
					    		if(!isExist) {
					    			vCount++;
					    			System.out.println(pathM);
					    			System.out.println(vCount+"=========================================================删除数");
					    			System.out.println(allCount+"=========================================================遍历总数");
					    			
					    			size=deleteAllFilesOfDir(new File(pathM),size);
					    			System.out.println(size+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>删除文件总大小");
					    			if(vCount>=jumpNum) {
					    				break labelJ;
					    			}
					    		}
					    	}
				    	}
			    	}
		    }  
		}
		
		System.out.println("over  over  over  over  over  over  over  over  over  over  over  over  over  over  over  over  over  over  over  over  over  over  ");
	}
	
	
	public class MP4FileFilter implements FileFilter {  
  	  
	    @Override  
	    public boolean accept(File file) {  
	          
	        if(file.isDirectory())  
	            return false;  
	        else  
	        {  
	            String name = file.getName().toLowerCase();  
	            if(name.endsWith(".mp4"))  
	                return true;  
	            else  
	                return false;  
	        }  
	          
	    }  
	  
	}
	
	public class DateFilter implements FileFilter {  
		
		private String startDate;
		
		private String endDate;
		
		private Integer days;
		
		public DateFilter() {
			
		}  
  	  
	    public DateFilter(String startDate, String endDate,Integer days) {
			this.startDate = startDate;
			this.endDate = endDate;
			this.days = days;
		}

		@Override  
	    public boolean accept(File file) {  
			
			Long fLong=file.lastModified();
			
			if((startDate!=null&&!startDate.equals(""))||(endDate!=null&&!endDate.equals(""))) {
				
				if((startDate!=null&&!startDate.equals(""))&&(endDate!=null&&!endDate.equals(""))) {
					Long sLong=java.sql.Date.valueOf(startDate).getTime();
					Long eLong=java.sql.Date.valueOf(endDate).getTime();
					if(sLong<=fLong&&eLong>=fLong) {
						return true;
					}
					return false;
					
				}
				if(startDate!=null&&!startDate.equals("")) {
					Long sLong=java.sql.Date.valueOf(startDate).getTime();
					if(sLong<=fLong) {
						return true;
					}
					return false;
					
				}
				
				if(endDate!=null&&!endDate.equals("")) {
					Long eLong=java.sql.Date.valueOf(endDate).getTime();
					if(eLong>=fLong) {
						return true;
					}
					return false;
				}
			}else {
				if(days==null) {
					days=10;
				}
				java.util.Calendar rightNow = java.util.Calendar.getInstance(); 

		        rightNow.add(java.util.Calendar.DAY_OF_MONTH, -days);
		        
		        Long nLong=rightNow.getTimeInMillis();
		        
		        if(nLong<=fLong) {
					return true;
				}
				return false;
			}
	          
			
			
			return false;
	          
	    }

	  
	}
		
	public boolean isPlayKeyExists(String playKey) {

		boolean isExist=false;
		
		StringBuffer countJpql = new StringBuffer();
	    List<ParameterBean> paramList=new ArrayList<ParameterBean>();
	    
	    countJpql.append("SELECT COUNT(*)  FROM  Video v   where 1=1  and v.flowStat!='delete' and v.playKey= '").append(playKey).append("'");
	    
	    try {
			Long count =videoService.getObjectCountByJpql(countJpql, paramList);
			
			if(count!=null&&count>0) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return isExist;
	    
	}
	
	
		
		
	
	
	public void isFileNotExistsReturn(File file) {
		if (!file.exists()) {
			return;
		}
	}
	
	public void isFileReturn(File file) {
		
	    if (file.isFile()) {  
	        return;  
	    }  
	}
	
	public void isDirReturn(File file) {
		
	    if (!file.isFile()) {  
	        return;  
	    }  
	}
	

	
	
	@RequestMapping("prePlanPublish")  
	public String prePlanPublish(Long [] videoIds){  
		this.request.setAttribute("videolist", videoService.findAll(videoIds)); 
		return "video/planpublish";
	}
	
	@RequestMapping("planPublish")   
	public String planPublish(Date starttime,Integer interver,Long [] videoIds){ 
        Calendar c = Calendar.getInstance(); 
        c.setTime(starttime);
		for(Long id:videoIds){
			Video v = videoService.find(id);
			v.setFlowStat(VideoStatus.计划发布.getName());
			v.setModifyDate(new Date()); 
	        c.add(c.MINUTE, interver);  
	        Date plandate = c.getTime(); 
	        v.setPlanPublishTime(plandate);
	        videoService.save(v);
	        //************添加调度任务***************
	        try {
	        	quartz.addJob(v);
			} catch (SchedulerException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
	        //************添加调度任务***************
		}  
		return "redirect:checledVideos";
	}
	//取消发布
	@RequestMapping("cancelPublish") 
	@ResponseBody
	public String cancelPublish(Long vid){ 
		Video v = videoService.find(vid);
		//状态流程不可逆  所以取消发布后直接删除
		v.setFlowStat(VideoStatus.已删除.getName());
		v.setModifyDate(new Date());   
        v.setPlanPublishTime(null);
        videoService.save(v); 
        //取消发布的时候用户视频数应该 -1
        Ruser ruser = ruserService.find(v.getCreatorId());
		int count = videoService.countVideoForRuser(v.getCreatorId());//统计该用户发的视频数
		ruser.setVideoCount(count);
		ruserService.update(ruser);
		return "ok";
	}

	@RequestMapping("uploadsave")
	@ResponseBody
	public Map uploadsave(String title, Long uploader, String description,
			String playId,Long templateId,String videoPic,String videoListPic,String pub,
			@RequestParam(value = "showTitle", required = false)  String showTitle,
			@RequestParam(value = "showDesc", required = false)  String showDesc,
			@RequestParam(value = "refVideos", required = false)  String refVideos,
			@RequestParam(value = "showPic", required = false)  String showPic, HttpServletRequest req) {
		logger.info(
				"subject:{},tag:{},title:{},uploader:{},description:{},playId:{}",
				req.getParameterValues("subject[]"),
				req.getParameterValues("tag[]"), title, uploader, description,
				playId,templateId);
		String[] activity = req.getParameterValues("activity[]");
//		String[] tag = req.getParameterValues("tag[]");
		
		Set<String> tags = new HashSet<String>();
    	if (org.apache.commons.lang3.StringUtils.isNotBlank(description)) {
    		Pattern pattern = Pattern.compile("(#([^#]+)#)");
    		Matcher matcher = pattern.matcher(description);
    		while (matcher.find()) {
    			String tag = matcher.group(2);
    			tags.add(tag);
    		}
		}
    	
		String introductionMark = req.getParameter("introductionMark");
		String isLogo = req.getParameter("isLogo");
		String playRateToday = req.getParameter("playRateToday");
		if (activity == null) {
			if(StringUtils.isNotBlank(req.getParameter("activity"))){
				activity = new String[] { req.getParameter("activity") };
			}
		}
//		if (tag == null) {
//			if(StringUtils.isNotBlank(req.getParameter("tag"))){
//				tag = new String[] { req.getParameter("tag") };
//			}
//		}
		String plan = req.getParameter("pubTime");
		Date planTime = null;
		if(StringUtils.isNotBlank(plan)){
			try {
				planTime = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(plan);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		//我拍秀视频，
		ShowVideo showVideo = null;
		if(StringUtils.isNotBlank(showTitle)){
			showVideo = new ShowVideo();
			showVideo.setTitle(showTitle);
			showVideo.setDescription(showDesc);
			showVideo.setCreatorId(U.getUid());
			showVideo.setCreateDate(new Date());
			showVideo.setPic(showPic);
			showVideo.setRefVideoId(refVideos);
		}
		// 保存视频
		Video v=this.videoService.saveVideo2activity(activity,tags, title, uploader, description,playId,DataFrom.移动麦视后台.getName(),introductionMark,templateId,U.getUid(),videoPic,videoListPic,planTime,pub,isLogo,playRateToday,showVideo);
		if("2".equals(pub)){//计划发布
			//************添加调度任务***************
        	try {
				quartz.addJob(v);
			} catch (SchedulerException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
	        //************添加调度任务***************
		}
		map.put(Constants.RESULT, "success");
		map.put("videoId", v.getId());
		map.put("videoName", v.getName());
		
		return map;
	}

	@RequestMapping("videocallback")
	@ResponseBody
	public RespBody remoteinvokeinterface(String playKey, String flowStat,
			Integer w, Integer h, String duration, HttpServletRequest req) {
		logger.info("playKey:{},flowStat:{},w:{},h:{},duration:{}", playKey,
				flowStat, w, h, duration);
		String url="";
		if(StringUtils.isNotBlank(flowStat)&&flowStat.equals(VideoStatus.转码成功.getName())){
			flowStat=VideoStatus.审核通过.getName();
		}
		url=video_play_url_prefix+playKey+".m3u8";
		for(int i =0;i<5;i++) {
			int result = this.videoService.updateVideo(playKey, w, h, duration, url);
			logger.info("update result:{}", result);
			if (result != 0) {
				break;
			}else{
				try{
					TimeUnit.SECONDS.sleep(2);
				}catch(Exception e){
					logger.error("视频服务器多次更新视频状态失败",e);
				}
			}
		}
		return this.respBodyWriter.toSuccess();
	}

	@EnableFunction("审核视频,查看审核视频信息")
	@RequestMapping("unchecklist")
	public String unchecklist(HttpServletRequest req) {
		req.setAttribute("video_play_url_prefix", video_play_url_prefix);
		req.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		return "video/unchecklist";
	}

	@RequestMapping("checking")
	public String checking(Long id,HttpServletRequest req) {
		String username="";
//		VideoUploader vd=null;
		String img="";
		
		List list=this.videoService.findVideo(id);
//		List subjectList=this.subjectService.findByUseStat(SubjectStatus.活动.getName());
//		List tagList=this.tagService.findByStatus(TagStatus.活动.getName());
//		List svList=U.parseListMapToList(this.videoService.findSubjectIdByVideoId(id),SubjectVideoRelation.class,"subjectId");
		List svList=U.parseListMapToList(this.videoService.findActivityIdByVideoId(id),ActivityVideo.class,"activityid");
		
		Object[] os=new Object[]{};
		if(list!=null&&!list.isEmpty()&&list.size()>0){
			os=(Object[])list.get(0);
			username=U.nvl(os[0]);
			
			img=this.videoService.findImgByVideo(id);
			if(StringUtils.isBlank(img)){
				img=this.video_play_url_prefix+os[7]+".jpg";
			}else{
				img=this.uploadpic_url_prefix+img;
			}
		}
		
		List vtagList=Arrays.asList(StringUtils.isBlank(U.nvl(os[3]))?new String[]{}:U.nvl(os[3]).split(" "));
		
		List activityList = this.activityService.findAll();
		req.setAttribute("activityList", activityList);

		req.setAttribute("vtagList", vtagList);
//		req.setAttribute("tagList", tagList);
//		req.setAttribute("subjectList", subjectList);
		req.setAttribute("createDate", U.nvl(os[6]));
		req.setAttribute("username", username);
		req.setAttribute("svList", svList);
		req.setAttribute("os", os);
		req.setAttribute("img", img);
		req.setAttribute("video_play_url_prefix", video_play_url_prefix);
		req.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		logger.info("svList:{}",svList);
		return "video/checking";
	}

	@RequestMapping("deleteallpage")
	@ResponseBody
	public RespBody deleteAll(String ids) {
		this.videoService.deleteAll(ids);
		return this.respBodyWriter.toSuccess();
	}

	@EnableFunction("审核视频,通过视频审核")
	@RequestMapping("checkTongGuoPage")
	@ResponseBody
	public RespBody checkTongGuoPage(String ids) throws SolrServerException, IOException {
		List<Long> strList = new ArrayList<Long>();
		for (String s : ids.split(",")) {
			strList.add(Long.parseLong(s));
			Video video = videoService.queryVideoById(Long.parseLong(s));
			if("delete".equals(video.getFlowStat())){
				return this.respBodyWriter.toError(ResponseCode.CODE_457.toString(), ResponseCode.CODE_457.toCode());
			}
		}
		//审核通过的视频id，通知kafka，处理任务
		List<Map<String,String>> CheckOkIdsForIntegral = this.videoService.updateVideoStatusByIds(U.getUid(),VideoStatus.审核通过,strList);
		for(Map uidAndVidMap:CheckOkIdsForIntegral){
			Message msg = new Message();
			msg.setModule(Module.VIDEO);
			msg.setAction(Action.INTEGRAL);
			Map<String,Object> dataMap = new HashMap<String,Object>();
			dataMap.put("uid",uidAndVidMap.get("uid"));
			dataMap.put("vid",uidAndVidMap.get("vid"));
			dataMap.put("pubDate",new Date().getTime());//审核成功记录发布消息时间
			msg.setDataMap(dataMap);
			kafkaProducer.send("rest-topic",msg); 
		}
		return this.respBodyWriter.toSuccess();
	}

	@RequestMapping("checkOk")
	@ResponseBody
	public RespBody checkOk(Long id, String title, Long uploader,
			String description, HttpServletRequest req) {
		String[] activity = req.getParameterValues("activity[]");
		String[] tag = req.getParameterValues("tag[]");
		if (activity == null) {
			if(StringUtils.isNotBlank(req.getParameter("activity"))){
				activity = new String[] { req.getParameter("activity") };
			}
		}
		if (tag == null) {
			if(StringUtils.isNotBlank(req.getParameter("tag"))){
				tag = new String[] { req.getParameter("tag") };
			}
		}
		if(tag!=null){
			Set<String> set=new HashSet<String>();
			set.addAll(Arrays.asList(tag));
			tag=set.toArray(new String[0]);
		}
		
		this.videoService.updateVideoAndCheckOk2activity(id, activity, tag, title,
				uploader, description);
		return this.respBodyWriter.toSuccess();
	}

	@RequestMapping("check_fail")
	@ResponseBody
	public RespBody check_fail(String id, String failReason) {
		String ids[] = id.split(",");
		List<Long> idList = new ArrayList<Long>();
		for(String s:ids){
			idList.add(Long.parseLong(s));
			Video video = videoService.queryVideoById(Long.parseLong(s));
			if("delete".equals(video.getFlowStat())){
				return this.respBodyWriter.toError(ResponseCode.CODE_457.toString(), ResponseCode.CODE_457.toCode());
			}
		}
//		String[] failReason = req.getParameterValues("failReason[]");
//		if (failReason == null) {
//			failReason = new String[] { req.getParameter("failReason") };
//		}
		this.videoService.updateFailReason(U.getUid(),failReason,idList);
		
//		renameFileName(idList);//已删除改名不可播放
		
		return this.respBodyWriter.toSuccess();
	}

	@RequestMapping("destroyedlist")
	public String destroyedlist(HttpServletRequest req) {
		req.setAttribute("video_play_url_prefix", video_play_url_prefix);
		req.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		return "video/destroyedlist";
	}

	@RequestMapping("recoverPage")
	@ResponseBody
	public RespBody recoverPage(String ids) throws SolrServerException, IOException {
		List<Long> array_id = new ArrayList<Long>();
		if (ids != null) {
			for(String id:ids.split("[,]")){
				array_id.add(Long.parseLong(id));
			}
			this.videoService.recoverVideos(U.getUid(),array_id);
		}
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("searchListPage")
	@ResponseBody
	public Map searchListPage(Integer page, Integer rows, CRUDForm curdForm) {
        // 从request中获取通用查询条件
    	if(page==null||page==0){
    		page=1;
    	}
    	if(rows==null||rows==0){
    		rows=8;
    	}
    	boolean search=false;//是否是专题搜索
    	String status="";
    	String subject="";
    	Page pinfo=null;
        List<Filter> filters = new ArrayList<Filter>();
        for (Map.Entry<String, String> entry : curdForm.getFilters().entrySet()) {
        	if(StringUtils.isNotBlank(entry.getValue())){
        		if(entry.getKey().equals("subject")){
        			//pinfo=this.videoService.listpage(page, rows, entry.getValue(),"uncheck");
        			search=true;
        			subject=entry.getValue();
        		}else if(entry.getKey().equals("tag")){
        			filters.add(Filter.like(entry.getKey(), "%"+entry.getValue()+"%"));
        		}else if(entry.getKey().equals("name")){
        			filters.add(Filter.like(entry.getKey(), entry.getValue()+"%"));
        		}else{
        			Filter f=condToFilter(entry.getKey(), entry.getValue());
        			if(f.getProperty().trim().equals("flowStat")){
        				status=f.getValue().toString();
        			}
        			filters.add(f);
        		}
        	}
        }
        if(search){
        	pinfo=this.videoService.listpage(page, rows, subject,status);//flowStat
        }else{
        	pinfo=this.videoService.listpage(page, rows, filters, null);
        }
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
        return jsonMap;
    }

	@EnableFunction("视频管理,查看视频管理信息")
	@RequestMapping("uservideos")
	public String listUserVideo(HttpServletRequest req){
//		List<Tag> tagList = this.tagService.findAll();
		List<Activity> activityList = this.activityService.findAll();
//		req.setAttribute("tags", tagList);
		req.setAttribute("activites", activityList);
		req.setAttribute("video_play_url_prefix", video_play_url_prefix);
		req.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		req.setAttribute("uploadfile_remote_url", uploadfile_remote_url);
		return "video/userVideolist";
	}
	
	@RequestMapping("showUserVideos")
	public String showUserVideos(HttpServletRequest req,
			@RequestParam(value = "popFlg", required = false)  String popFlg
			){
		String s=listUserVideo( req);
		req.setAttribute("popFlg", popFlg);
		return "video/showUserVideos";
	}
	
	/**
	 * 组合条件查询用户视频 dmsong add 20150121
	 * @param page
	 * @param rows
	 * @param curdForm
	 * @return
	 */
	@RequestMapping("searchVideoListPage")
	@ResponseBody
	public Map searchVideoListPage(Integer page, Integer rows, CRUDForm curdForm) {
        // 从request中获取通用查询条件
    	if(page==null||page==0){
    		page=1;
    	}
    	if(rows==null||rows==0){
    		rows=20;
    	}
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	Map<String,Object> params = new HashMap<String,Object>();
        params.put("pageStart", (page-1)*rows);
        params.put("pageSize", rows);
        for (Map.Entry<String, String> entry : curdForm.getFilters().entrySet()) {
        	if(StringUtils.isNotBlank(entry.getValue())){
        		if(entry.getKey().equals("dataFrom")){
        			params.put("dataFrom", entry.getValue());
        		}else if(entry.getKey().equals("starttime")){
        			try {
						params.put("starttime", df.parse(entry.getValue()));						
					} catch (ParseException e) {
						e.printStackTrace();
					}
        		}else if(entry.getKey().equals("endtime")){
					try {
						params.put("endtime", df.parse(entry.getValue()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else if(entry.getKey().equals("planStartTime")){
					try {
						params.put("planStartTime", df.parse(entry.getValue()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else if(entry.getKey().equals("planEndTime")){
					try {
						params.put("planEndTime", df.parse(entry.getValue()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else if(entry.getKey().equals("label")){
    				params.put("label", entry.getValue());
        		}else if(entry.getKey().equals("activity")){
    				params.put("activities", entry.getValue());
        		}else if(entry.getKey().equals("flowStat")){
        			params.put("flowStat", entry.getValue());
        		}else if(entry.getKey().equals("rankAble")){
        			params.put("rankAble", entry.getValue());
        		}else if(entry.getKey().equals("creatorName")){
        			params.put("creatorName", entry.getValue());
        		}else if(entry.getKey().equals("videoName")){
        			params.put("videoName", entry.getValue());
        		}else if(entry.getKey().equals("popFlg")){
        			params.put("introductionMark", "0");
        		}else if(entry.getKey().equals("auditorId")){
        			params.put("auditorId", entry.getValue());
        		}else if(entry.getKey().equals("reason")){
        			params.put("reason", entry.getValue());
        		}else if(entry.getKey().equals("userId")){
					params.put("userId", entry.getValue());
				}else if (entry.getKey().equals("user")) {
					params.put("user", entry.getValue());
				}else if (entry.getKey().equals("userKeyword")) {
					params.put("userKeyword", entry.getValue());
				}else if (entry.getKey().equals("noActivities")) {
					params.put("noActivities", entry.getValue());
				}else if (entry.getKey().equals("type")) {
					params.put("type", entry.getValue());
				}
			}
        }
        Page pinfo = this.videoService.searchVideoList(page, rows, params);
       
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
        return jsonMap;
    }
	
	/**
	 * 查询最热视频
	 * @param page
	 * @param rows
	 * @param curdForm
	 * @return
	 */
	@RequestMapping("searchHotVideoListPage")
	@ResponseBody
	public Map searchHotVideoListPage(Integer page, Integer rows, CRUDForm curdForm) {
        // 从request中获取通用查询条件
    	if(page==0){
    		page=1;
    	}
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	Map<String,Object> params = new HashMap<String,Object>();
        params.put("pageStart", (page-1)*rows);
        params.put("pageSize", rows);
        for (Map.Entry<String, String> entry : curdForm.getFilters().entrySet()) {
        	if(StringUtils.isNotBlank(entry.getValue())){
        		if(entry.getKey().equals("dataFrom")){
        			params.put("dataFrom", entry.getValue());
        		}else if(entry.getKey().equals("starttime")){
        			try {
						params.put("starttime", df.parse(entry.getValue()));						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}else if(entry.getKey().equals("endtime")){
        			try {
						params.put("endtime", df.parse(entry.getValue()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}else if(entry.getKey().equals("label")){
    				params.put("label", entry.getValue());
        		}else if(entry.getKey().equals("activity")){
    				params.put("activities", entry.getValue());
        		}else if(entry.getKey().equals("flowStat")){
        			params.put("flowStat", entry.getValue());
        		}else if(entry.getKey().equals("creatorName")){
        			params.put("creatorName", entry.getValue());
        		}else if(entry.getKey().equals("videoName")){
        			params.put("videoName", entry.getValue());
        		}else if (entry.getKey().equals("user")) {
					params.put("user", entry.getValue());
				}else if (entry.getKey().equals("userKeyword")) {
					params.put("userKeyword", entry.getValue());
				}
        	}
        }
        Page pinfo = this.videoService.searchHotVideoList(page, rows, params);
       
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
        return jsonMap;
    }

	@EnableFunction("视频管理,删除视频信息")
	@RequestMapping("logicremove")
	@ResponseBody
	public RespBody logicDelete(String ids) throws SolrServerException, IOException {
		List<Long> strList = new ArrayList<Long>();
		for (String s : ids.split(",")) {
			strList.add(Long.parseLong(s));
		}
		this.videoService.updateVideoStatusByIds(U.getUid(),VideoStatus.已删除,strList);
		
		renameFileName(strList);//已删除改名不可播放
		
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("checkoklist")
	public String checkoklist(HttpServletRequest req) {
		req.setAttribute("video_play_url_prefix", video_play_url_prefix);
		req.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		return "video/checkoklist";
	}
	
	@RequestMapping("detail")
	public String detail(Long id,HttpServletRequest req) {
		String username="";
//		VideoUploader vd=null;
		String img="";
		
		List list=this.videoService.findVideo(id);
//		List subjectList=this.subjectService.findByUseStat(SubjectStatus.活动.getName());
//		List tagList=this.tagService.findByStatus(TagStatus.活动.getName());
//		List svList=U.parseListMapToList(this.videoService.findSubjectIdByVideoId(id),SubjectVideoRelation.class,"subjectId");
		List svList=U.parseListMapToList(this.videoService.findActivityIdByVideoId(id),ActivityVideo.class,"activityid");
		
		Object[] os=new Object[]{};
		if(list!=null&&!list.isEmpty()&&list.size()>0){
			os=(Object[])list.get(0);
			username=U.nvl(os[0]);
			
			img=this.videoService.findImgByVideo(id);
			if(StringUtils.isBlank(img)){
				img=this.video_play_url_prefix+os[7]+".jpg";
			}else{
				img=this.uploadpic_url_prefix+img;
			}
		}
		
//		List vtagList=Arrays.asList(StringUtils.isBlank(U.nvl(os[3]))?new String[]{}:U.nvl(os[3]).split(" "));
		
		List activityList = this.activityService.findAll();
		req.setAttribute("activityList", activityList);

//		req.setAttribute("vtagList", vtagList);
//		req.setAttribute("tagList", tagList);
//		req.setAttribute("subjectList", subjectList);
		req.setAttribute("createDate", U.nvl(os[6]));
		req.setAttribute("username", username);
		req.setAttribute("svList", svList);
		req.setAttribute("os", os);
		req.setAttribute("img", img);
		req.setAttribute("video_play_url_prefix", video_play_url_prefix);
		req.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		logger.info("svList:{}",svList);
		return "video/detail";
	}
	
	@RequestMapping("modify_ui")
	public String modify_ui(Long id,HttpServletRequest req) {
		
		String username="";
//		VideoUploader vd=null;
		String img="";
		
		List list=this.videoService.findVideo(id);
//		List subjectList=this.subjectService.findByUseStat(SubjectStatus.活动.getName());
//		List tagList=this.tagService.findByStatus(TagStatus.活动.getName());
		List svList=U.parseListMapToList(this.videoService.findActivityIdByVideoId(id),ActivityVideo.class,"activityid");
		
		Object[] os=new Object[]{};
		String introductionMark = "0";
		if(list!=null&&!list.isEmpty()&&list.size()>0){
			os=(Object[])list.get(0);
			username=U.nvl(os[0]);
			introductionMark = U.nvl(os[8]);
			img=this.videoService.findImgByVideo(id);
			if(StringUtils.isBlank(img)){
				img=this.video_play_url_prefix+os[7]+".jpg";
			}else{
				img=this.uploadpic_url_prefix+img;
			}
		}
		
		List vtagList=Arrays.asList(StringUtils.isBlank(U.nvl(os[3]))?new String[]{}:U.nvl(os[3]).split(" "));
		
		List activityList = this.activityService.findAll();
		req.setAttribute("activityList", activityList);
		
		List<Filter> filters = new ArrayList<Filter>();
		Sort sort = new Sort(Direction.ASC,"orderNum");
		Filter filter= Filter.eq("type", 0);
		filters.add(filter);
		List tList = templateService.findAll(0,1000,filters,sort);
		req.setAttribute("templateList", tList);
		
		List rusers=ruserService.findByType(UserType.马甲.getName());
		this.request.setAttribute("uploaduserlist", rusers);
		Ruser ruser = ruserService.selectByPrimaryKey(Long.valueOf(U.nvl(os[5])));
		req.setAttribute("vtagList", vtagList);
//		req.setAttribute("tagList", tagList);
//		req.setAttribute("subjectList", subjectList);
		req.setAttribute("createDate", U.nvl(os[6]));
		req.setAttribute("username", username);
		req.setAttribute("svList", svList);
		req.setAttribute("os", os);
		req.setAttribute("Referer", req.getHeader("Referer"));
		req.setAttribute("img", img);
		req.setAttribute("ruserId", U.nvl(os[5]));
		req.setAttribute("introductionMark", introductionMark);
		req.setAttribute("isLogo", U.nvl(os[10]));
		req.setAttribute("playRateToday", U.nvl(os[11]));
		req.setAttribute("allowPublish", ruser.getAllowPublish());
		req.setAttribute("video_play_url_prefix", video_play_url_prefix);
		req.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		logger.info("svList:{}",svList); 
		return "video/update";
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public String update(Long id, String title, String description, String[] activity,Long templateId,
			@RequestParam(value = "showId", required = false)  Long showId,
			@RequestParam(value = "showTitle", required = false)  String showTitle,
			@RequestParam(value = "showDesc", required = false)  String showDesc,
			@RequestParam(value = "refVideos", required = false)  String refVideos,
			@RequestParam(value = "showPic", required = false)  String showPic,HttpServletRequest req) {
		activity = req.getParameterValues("activity[]");

		String introductionMark = req.getParameter("introductionMark");
		String isLogo = req.getParameter("isLogo");
		if (activity == null) {
			if(StringUtils.isNotBlank(req.getParameter("activity"))){
				activity = new String[] { req.getParameter("activity") };
			}
		}

		Set<String> tags = new HashSet<String>();
    	if (org.apache.commons.lang3.StringUtils.isNotBlank(description)) {
    		Pattern pattern = Pattern.compile("(#([^#]+)#)");
    		Matcher matcher = pattern.matcher(description);
    		while (matcher.find()) {
    			String tag = matcher.group(2);
    			tags.add(tag);
    		}
		}
		
		String playRateToday = req.getParameter("playRateToday");
		
		String uploader = req.getParameter("ruserId");
		Long ruserId = null;
		if(StringUtils.isNotBlank(uploader)){
			ruserId = Long.parseLong(uploader);
		}
		ShowVideo showVideo = null;
		if(showId != null){
			showVideo = new ShowVideo();
			showVideo.setId(showId);
			showVideo.setTitle(showTitle);
			showVideo.setDescription(showDesc);
			showVideo.setCreatorId(U.getUid());
			showVideo.setCreateDate(new Date());
			showVideo.setPic(showPic);
			showVideo.setRefVideoId(refVideos);
		}
		try{
			this.videoService.updateVideo2activity(id, activity, tags, title,ruserId , description,introductionMark,templateId,isLogo,playRateToday,showVideo);
		}catch(Exception e){
			logger.error("修改发生内部错误，MSG：{}",e.getMessage());
			return "修改出错";
		}
		
		return "SUCCESS";
	} 
	
	
	@RequestMapping("test")
	@ResponseBody
	@SuppressWarnings({ "rawtypes", "unused" })
	public RespBody test(){
		List list=this.videoService.test(new PageRequest(1, 3));
		return this.respBodyWriter.toSuccess();
	}

	@EnableFunction("未通过审核视频,查看未通过审核视频信息")
	@RequestMapping("checkfaillist")
	public String checkfaillist(HttpServletRequest req) {
		List<User> userList = userService.findAll();
		req.setAttribute("users", userList);
		req.setAttribute("video_play_url_prefix", video_play_url_prefix);
		req.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		return "video/checkfaillist";
	}
	
	@RequestMapping("allvalidlist")
	public String allvalidlist(HttpServletRequest req) {
		req.setAttribute("video_play_url_prefix", video_play_url_prefix);
		req.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		return "video/allvalidlist";
	}
	

	@RequestMapping("allvalidlistPage")
	@ResponseBody
	public Map allvalidlistPage(Integer page, Integer rows, CRUDForm curdForm) {
        // 从request中获取通用查询条件
    	if(page==0){
    		page=1;
    	}
    	boolean namesearch=false;//
    	boolean tagsearch=false;//
    	boolean activitysearch=false;
    	String name="";
    	String tag="";
    	String activity="";
    	Page pinfo=null;
        List<Filter> filters = new ArrayList<Filter>();
        for (Map.Entry<String, String> entry : curdForm.getFilters().entrySet()) {
        	if(StringUtils.isNotBlank(entry.getValue())){
        		if(entry.getKey().equals("tag")){
        			tagsearch=true;
        			tag=entry.getValue();
        		}else if(entry.getKey().equals("name")){
        			namesearch=true;
        			name=entry.getValue();
        		}else if(entry.getKey().equals("activity")){//获取活动下的视频
        			activitysearch=true;
        			activity=entry.getValue();
        		}
        	}
        }
        if(namesearch){
        	pinfo=this.videoService.allvalidlistByNameLikepage(VideoStatus.审核通过.getName(), DataFrom.麦视rest接口.getName(), VideoStatus.已发布.getName(), name, new PageRequest(page-1, rows));
        }else if(tagsearch){
        	pinfo=this.videoService.allvalidlistByTagLikepage(VideoStatus.审核通过.getName(), DataFrom.麦视rest接口.getName(), VideoStatus.已发布.getName(), tag, new PageRequest(page-1, rows));
        }else if(activitysearch){
        	pinfo=this.videoService.allvalidlistByActivityLikepage(VideoStatus.审核通过.getName(), DataFrom.麦视rest接口.getName(), VideoStatus.已发布.getName(), activity, new PageRequest(page-1, rows));
        }else{
        	pinfo=this.videoService.allvalidlistpage(VideoStatus.审核通过.getName(), DataFrom.麦视rest接口.getName(), VideoStatus.已发布.getName(), new PageRequest(page-1, rows));
        }
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List activityList = this.activityService.findAll();
        jsonMap.put("activities", activityList);
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
        return jsonMap;
    }
	
	@RequestMapping("recommend")
	@ResponseBody
	public RespBody recommend(Long id){
		this.videoService.recommendVideo(id);
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("uploadpic")
	@ResponseBody
	public RespBody uploadpic(MultipartFile hlsfiles){
		String picpath=uploadVideoPic(hlsfiles);
		return this.respBodyWriter.toSuccess(picpath);
	}
	
	@RequestMapping("uploadVideoImg")
	@ResponseBody
	public RespBody uploadVideoImg(MultipartFile videoImg){
		String picpath=uploadVideoPic(videoImg);
		return this.respBodyWriter.toSuccess(picpath);
	}
	
	@RequestMapping("uploadShowImg")
	@ResponseBody
	public RespBody uploadShowVideoImg(MultipartFile showImg){
		String picpath=uploadVideoPic(showImg);
		return this.respBodyWriter.toSuccess(picpath);
	}
	
	@RequestMapping("uploadVideoImgJcrop")
	@ResponseBody
	public String uploadVideoImgJcrop(String imgPath,int x1,int y1,int w,int h){
		try {
			//把图片处理成固定大小然后裁剪
			PicAdjust.resizeImage(basePath+imgPath,basePath+imgPath,480,480);
		} catch (IOException e) {
			e.printStackTrace();
		}
        PictureScaleUtil.region(x1, y1, w, h, basePath+imgPath,basePath+imgPath.substring(0, imgPath.length()-4)+"_new.jpg");
    	return imgPath.substring(0, imgPath.length()-4)+"_new.jpg";
	}
	
	private String uploadVideoPic(MultipartFile file){ 
		String fileName = file.getOriginalFilename();
		String sufix = fileName.substring(fileName.lastIndexOf("."));
    	String relPath = File.separator+"videoPic"+File.separator+DateFormatUtils.format(new Date(), "yyyy-MM-dd")+File.separator;
        String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + sufix;
        try { 
        	FileUtils.copyInputStreamToFile(file.getInputStream(), new File(basePath+relPath, originalFilename));
            return relPath+originalFilename; 
        } catch (IOException e) {
        	logger.error("文件[" + originalFilename + "]上传失败",e);
        	return "failed";
        }
    }

	@EnableFunction("视频管理,上传封面")
	@RequestMapping("updatevideo")
	@ResponseBody
	public RespBody updatevideo(Long id,String imgPath,int x1,int y1,int w,int h){
		try {
			//把图片处理成固定大小然后裁剪
			PicAdjust.resizeImage(basePath+imgPath,basePath+imgPath,480,480);
			PictureScaleUtil.region(x1, y1, w, h, basePath+imgPath,basePath+imgPath.substring(0, imgPath.length()-4)+"_new.jpg");
	        
			Video v=this.videoService.find(id);
			v.setVideoPic(imgPath);
			v.setVideoListPic(imgPath.substring(0, imgPath.length()-4)+"_new.jpg");
			this.videoService.update(v);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("initActivites")
	@ResponseBody
	public Map initActivites(Integer page, Integer rows, CRUDForm curdForm) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List activityList = this.activityService.findAll();
        jsonMap.put("activities", activityList);
        return jsonMap;
    }
	
	@RequestMapping("saveOrderNum")
	@ResponseBody
	public Map saveOrderNum(Long id , int orderNum){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		int num = this.activityService.saveOrderNum(id,orderNum);
		jsonMap.put("num", num);
		jsonMap.put("id", id);
		return jsonMap;
	}
	
	@RequestMapping("userdetail")
	public String showUserDetail(Long uid){
		Ruser user = ruserService.find(uid);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pageStart", 0);
    	params.put("pageSize", 9999);
    	params.put("creatorId", uid);
		Page einfo = evaluationService.searchEvlauation(1, 9999, params);
		this.request.setAttribute("ruser", user);
		this.request.setAttribute("evaluations", einfo.getContent());
		this.request.setAttribute("video_play_url_prefix", video_play_url_prefix);
		this.request.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		this.request.setAttribute("uploadfile_remote_url", uploadfile_remote_url);
		return "video/userdetail";
	}

	@RequestMapping("queryUserEvaluationList")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> queryUserEvaluationList(@ModelAttribute("queryPage") JQueryPage queryPage,Long userId){
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("creatorId",userId);
		List<EvaluationDisplay> content = evaluationService.selectEvaluations(params);
		com.busap.vcs.util.page.Page page = PagingContextHolder.getPage();
		map.put("rows",content);
		map.put("total",page.getTotalResult());
		return map;
	}

	@RequestMapping
	@ResponseBody
	public Map<String,Object> queryUserDetail(Long uid){
		Map<String,Object> map = new HashMap<String, Object>();

		return map;
	}
	
	@RequestMapping("getuservideo")
	@ResponseBody
	public Page getuservideo(Long uid,Integer page,Integer size){
		if(page == 0){
			page = 1;
		}
		Page vinfo = this.videoService.findByCreator(uid, page, size);
		
		return vinfo;
	}
	
	/**
	 * 批量下载视频
	 * @param ids
	 * @return
	 * @throws IOException 
	 */
	@EnableFunction("视频管理,下载视频信息")
	@RequestMapping("multidown")
	public void multiDownload(String ids,HttpServletResponse resp) throws IOException{
		String idArr[] = ids.split(",");
		String local = this.request.getServletContext().getRealPath("/");
		String zipSrc = local + File.separator +System.currentTimeMillis()+".zip";
		
		if(idArr.length>0){
			File zipFile = new File(zipSrc);
			if(!zipFile.exists())
				zipFile.createNewFile();
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
			byte[] buffer = new byte[1024];
			for(String id:idArr){
				String videoSrc = getVideoPath(id);
				File videoFile = new File(videoSrc);
				if(videoFile.exists()){
					InputStream is = new FileInputStream(videoFile);
					zos.putNextEntry(new ZipEntry(videoFile.getName()));
					int len=0;
					while((len = is.read(buffer))>0) {
						zos.write(buffer,0,len);
					}
					zos.closeEntry();
					is.close();
				}
			}
			zos.close();
			InputStream in = new FileInputStream(zipFile);
			OutputStream output = resp.getOutputStream();
			resp.setContentType("application/zip");
			resp.setHeader("Content-Disposition", "attachment;filename=" + zipFile.getName());
			byte[] b = new byte[1024];
			int i = 0;

			while ((i = in.read(b)) > 0) {
				output.write(b, 0, i);
			}
			output.flush();
			in.close();
			zipFile.delete();
		}
	}
	
	/**
	 * 下载视频封面
	 * @param id
	 * @param resp
	 * @throws IOException
	 */
	@EnableFunction("视频管理,下载封面")
	@RequestMapping("downloadPic")
	public void downloadPic(String id,HttpServletResponse resp) throws IOException{
		
		Video video = videoService.find(Long.parseLong(id));
		File picFile = new File(basePath+video.getVideoPic());
		InputStream in = new FileInputStream(picFile);
		OutputStream output = resp.getOutputStream();
		resp.setContentType("image/*");
		resp.setHeader("Content-Disposition", "attachment;filename=" + picFile.getName());
		byte[] b = new byte[1024];
		int i = 0;
		
		while ((i = in.read(b)) > 0) {
			output.write(b, 0, i);
		}
		output.flush();
		in.close();
	}

	private String getVideoPath(String id) {
		if(StringUtils.isNumeric(id)){
			Video v = this.videoService.find(Long.parseLong(id));
			if(v != null){
				return this.video_download_url_prefix + v.getPlayKey().replace("-", "/")+".mp4";
			}
		}
		return null;
	}

	@EnableFunction("最新视频,查看最新视频信息")
	@RequestMapping("newvideos")
	public String listNewVideo(HttpServletRequest req){
		List list=ruserService.findByType(UserType.马甲.getName());
		req.setAttribute("mjuserlist", list);
		req.setAttribute("video_play_url_prefix", video_play_url_prefix);
		req.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		return "video/newVideolist";
	}
	
	/**
	 * 组合条件查询用户视频 dmsong add 20150121
	 * @param page
	 * @param rows
	 * @param curdForm
	 * @return
	 */
	@RequestMapping("searchNewVideoListPage")
	@ResponseBody
	public Map searchNewVideoListPage(Integer page, Integer rows, CRUDForm curdForm) {
        // 从request中获取通用查询条件
    	if(page==0){
    		page=1;
    	}
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	Map<String,Object> params = new HashMap<String,Object>();
        params.put("pageStart", (page-1)*rows);
        params.put("pageSize", rows);	
        for (Map.Entry<String, String> entry : curdForm.getFilters().entrySet()) {
        	if(StringUtils.isNotBlank(entry.getValue())){
        		if(entry.getKey().equals("starttime")){
        			try {
						params.put("starttime", df.parse(entry.getValue()));						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}else if(entry.getKey().equals("endtime")){
        			try {
						params.put("endtime", df.parse(entry.getValue()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}else if(entry.getKey().equals("flowStat")){
        			params.put("flowStat", entry.getValue());
        		}else if(entry.getKey().equals("islogo")){
        			params.put("islogo", entry.getValue());
        		}else if(entry.getKey().equals("creatorName")){
        			params.put("creatorName", entry.getValue());
        		}else if(entry.getKey().equals("videoName")){
        			params.put("videoName", entry.getValue());
        		}
        		
        	}
        }
        Page pinfo = this.videoService.searchNewVideoList(page, rows, params);
       
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
        return jsonMap;
    }
	
	private void renameFileName(List<Long> ids){
		for(Long vid : ids){
			String path = getM3u8Path(vid);
			if(path != null){
				String delPath = path.replace("m3u8", "m3u8_del");
				File f = new File(path);
				f.renameTo(new File(delPath));
			}
		}
	}
	
	private String getM3u8Path(Long id) {
		Video v = this.videoService.find(id);
		if(v != null){
			return this.video_download_url_prefix + v.getPlayKey().replace("-", "/")+".m3u8";
		}
		return null;
	}


	@RequestMapping("getvideoactivities")
	@ResponseBody
	public List getActivityIdByVid(Long id){
		return this.videoService.findActivityIdByVideoId(id);
	}

	@EnableFunction("视频管理,加入活动")
	@RequestMapping("modifyvideoactivities")
	@ResponseBody
	public RespBody modifyVideoActivity(Long videoId,Long activityId,String type){
		if("add".equals(type)){
			ActivityVideo av = new ActivityVideo();
			av.setActivityid(activityId);
			av.setVideoid(videoId);
			av.setOrderNum(0);
			this.activityVideoService.save(av);
		}else if("del".equals(type)){
			this.activityVideoService.deleteByVideoidAndActivityId(videoId, activityId);
		}else{
			return this.respBodyWriter.toError("不合法的操作", -1);
		}
		return this.respBodyWriter.toSuccess();
	}

	@EnableFunction("视频管理,排行榜")
	@RequestMapping("rankAble")
	@ResponseBody
	public RespBody rankAble(Long id,Integer rankAble){
		Video v = this.videoService.find(id);
		v.setRankAble(rankAble);
		
		this.videoService.update(v);
		
		return this.respBodyWriter.toSuccess();
	}

	@EnableFunction("视频管理,查看视频详情")
	@RequestMapping("videoDetail")
	public String showVideoDetail(Long vid){
		Video v = this.videoService.findVideoDetail(vid);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pageStart", 0);
    	params.put("pageSize", 9999);
    	params.put("videoId", vid);
		Page einfo = evaluationService.searchEvlauation(1, 9999, params);
		List<Activity> activityList = this.activityService.findAll();
//		req.setAttribute("tags", tagList);
		this.request.setAttribute("activites", activityList);
		this.request.setAttribute("video", v);
		this.request.setAttribute("evaluations", einfo.getContent());
		this.request.setAttribute("video_play_url_prefix", video_play_url_prefix);
		this.request.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		this.request.setAttribute("uploadfile_remote_url", uploadfile_remote_url);
		return "video/videoDetail";
	}
	/**
	 * 加入最热视频或置顶
	 * @param id
	 * @return
	 */
	@EnableFunction("视频管理,加入热门视频")
	@RequestMapping("addHotVideoTop")
	@ResponseBody
	public RespBody addHotTopVideo(Long id){
		BigDecimal playRate = this.videoService.findMaxPlayRate();
		Video v = this.videoService.find(id);
		if(playRate.compareTo(v.getPlayRateToday())>0){
			playRate = playRate.add(new BigDecimal(0.0001));
			v.setPlayRateToday(playRate);
			this.videoService.update(v);
		}
		return this.respBodyWriter.toSuccess();
	}
	
	/**
	 * 移除最热视频
	 * @param ids
	 * @return
	 */
	@EnableFunction("视频管理,移除热门视频")
	@RequestMapping("removeHotVideos")
	@ResponseBody
	public RespBody removeHotVideo(String ids){
		if(StringUtils.isBlank(ids)){
			return this.respBodyWriter.toError("没有选择要移除的视频！");
		}
		String idArr[] = ids.split(",");
		List<Long> idList = new ArrayList<Long>();
		for(String id:idArr){
			idList.add(Long.parseLong(id));
		}
		this.videoService.removeHotVideos(idList);
				
		return this.respBodyWriter.toSuccess();
	}
	
	/**
	 * 最热视频上移
	 * @param id
	 * @return
	 */
	@RequestMapping("moveHotVideoUp")
	@ResponseBody
	public RespBody upperHotVideo(Long id){
		this.videoService.moveHotVideoUp(id);
		return this.respBodyWriter.toSuccess();
	}
	
	/**
	 * 最热视频下移
	 * @param id
	 * @return
	 */
	@RequestMapping("moveHotVideoDown")
	@ResponseBody
	public RespBody downHotVideo(Long id){
		this.videoService.moveHotVideoDown(id);
		return this.respBodyWriter.toSuccess();
	}

	@RequestMapping("queryEvaluationListByVideoId")
	@ResponseBody
	public Map<String,Object> queryEvaluationListByVideoId(Integer page,Integer rows,Long videoId){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("pageStart", (page-1)*rows);
		params.put("pageSize", rows);
		params.put("videoId", videoId);
		Page einfo = evaluationService.searchEvlauation(1, 9999, params);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows",einfo.getContent());
		map.put("total",einfo.getTotalElements());
		return map;
	}

	/**
	 * 用户视频排序
	 * @param videoId 视频ID
	 * @param type 排序类型 1置顶 2上移 3下移
	 */
	@RequestMapping("videoSort")
	@ResponseBody
	public Map<String,Object> videoSort(Long videoId,Integer type)throws Throwable{
		Video video = videoService.queryVideoById(videoId);
		Map<String, Object> map = new HashMap<String, Object>();
		if(video != null) {
			if (type == 1) {
				Map<String, Object> maxParams = new HashMap<String, Object>();
				maxParams.put("MAX", "MAX");
				Video maxVideo = videoService.queryVideoByWeight(maxParams);//置顶活动信息
				if(videoId.equals(maxVideo.getId())){
					map.put("resultCode", "error");
					map.put("resultMessage", "已经置顶了哦！");
					return map;
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", videoId);
				params.put("weight", maxVideo.getWeight() + 1);
				int result = videoService.updateSort(params);
				if (result > 0) {
					map.put("resultCode", "success");
					map.put("resultMessage", "排序成功！");
				} else {
					map.put("resultCode", "error");
					map.put("resultMessage", "排序失败！");
				}
			} else if (type == 2) {
				map = videoService.upSort(videoId, video);
			} else {
				map = videoService.downSort(videoId, video);
			}
		}else{
			map.put("resultCode", "error");
			map.put("resultMessage", "此条记录不存在！");
		}
		return map;
	}

	/**
	 * 用户视频排序
	 * @param videoId 视频ID
	 * @param weight 排序权重
	 */
	@RequestMapping("editVideoOrderNum")
	@ResponseBody
	public ResultData editVideoOrderNum(Long videoId,Double weight){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id",videoId);
		params.put("weight",weight);
		int result = videoService.updateSort(params);
		ResultData resultData = new ResultData();
		if(result > 0){
			resultData.setResultCode("success");
			resultData.setResultMessage("更新成功！");
		}else{
			resultData.setResultCode("fail");
			resultData.setResultMessage("更新失败！");
		}
		return resultData;
	}

	@RequestMapping("forwardVideoMosaic")
	public ModelAndView forwardVideoMosaic(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("video/video_mosaic");
		return mav;
	}

	@RequestMapping("mosaicLiveVideo")
	@ResponseBody
	public ResultData mosaicLiveVideo(Long roomId,String videoUrls){
		ResultData resultData = new ResultData();
		if(roomId == null || roomId <=0 || StringUtils.isBlank(videoUrls)){
			resultData.setResultCode("fail");
			resultData.setResultMessage("房间ID，视频文件地址不能为空！");
			return resultData;
		}
		Room room = roomService.find(roomId);
		if(room == null || room.getCreatorId() ==null || room.getCreatorId()==0){
			resultData.setResultCode("fail");
			resultData.setResultMessage("房间信息不存在或房间信息异常！");
			return resultData;
		}
		Anchor anchor = anchorService.getAnchorByUserid(room.getCreatorId());
		if(anchor == null || StringUtils.isBlank(anchor.getStreamId())){
			resultData.setResultCode("fail");
			resultData.setResultMessage("主播streamId不存在！");
			return resultData;
		}
		String streamId = anchor.getStreamId();
		String[] videoUrl = videoUrls.split(";");
		if(videoUrl.length <= 1){
			//当视频文件为一条时，直接生成回放信息
            Video videoInfo = videoService.getDeletePlaybackByRoomId(room.getId());
            String url = videoUrls.substring(0,videoUrls.length()-1);
            if(videoInfo != null){
                //更新回放信息
                videoInfo.setPlayKey(url);
                videoInfo.setFlowStat(VideoStatus.审核通过.getName()); //将回放信息更新为通过状态
                videoService.save(videoInfo);
                resultData.setResultCode("success");
                resultData.setResultMessage("单条视频不需要合成，回放更新成功！");
                return resultData;
            }else{
                Room roomInfo = roomService.find(room.getId());
                //回放信息不存在，进行插入操作
                Video video = new Video();

                video.setCreatorId(roomInfo.getCreatorId());
                video.setCreateDate(roomInfo.getCreateDateStr());
                video.setPublishTime(roomInfo.getFinishDateStr());//发布时间改为直播结束时间（毕，小瓶盖）
                video.setDataFrom("back");

                video.setDuration(String.valueOf(roomInfo.getDuration()));
                video.setVideoPic(roomInfo.getRoomPic());

                video.setPlayKey(url);
                video.setFlowStat(VideoStatus.审核通过.getName()); //将回放信息更新为通过状态

                video.setDescription(roomInfo.getTitle());
                video.setType(2);
                video.setPlayCountToday(Long.valueOf(roomInfo.getMaxAccessNumber()));//直播回放时,存的最大观看人数
                video.setLiveNoticeId(roomInfo.getId());//直播回放时,存的直播房间id
                Ruser ruser = ruserService.find(room.getCreatorId());
                int liveWeight = ruser.getLiveWeight();
                video.setPlaybackWeight(liveWeight + roomInfo.getOnlineNumber());
                videoService.save(video);
                resultData.setResultCode("success");
                resultData.setResultMessage("单条视频不需要合成，回放添加成功！");
                return resultData;
            }
		}
		String order = "";
		String urlEncode = "";
		for (int i=1;i<=videoUrl.length;i++){
			order += i+"-";
			urlEncode += EncodeUtils.urlsafeEncodeString(videoUrl[i-1].getBytes()) +"/";
		}
		order = order.substring(0,order.length()-1);
		urlEncode = urlEncode.substring(0,urlEncode.length()-1);
		Map<String,String> postParams = requestParams(streamId,order,urlEncode);
		Map<String,String> header = new HashMap<String, String>();
		header.put("Authorization",createSign(streamId,order,urlEncode));
		String body = "bucket="+postParams.get("bucket")+"&key="+postParams.get("key")+"&fops="+postParams.get("fops")+"&notifyURL="+postParams.get("notifyURL")+"&force="+postParams.get("force")+"&separate="+postParams.get("separate");
		Map<String,String> resultMap = HttpPostUtils.httpPostStringEntity("http://blive.mgr5.v1.wcsapi.com/fops",header,body);
		if("200".equals(resultMap.get("code"))){
			String response = resultMap.get("response");
			JSONObject json = JSON.parseObject(response);
			String persistentId = json.getString("persistentId");
			room.setPersistentId(persistentId);
			roomService.save(room);
			//业务处理

			resultData.setResultCode("success");
			resultData.setResultMessage("合并请求视频文件提交成功，请耐心等待CDN处理！");
		}else{
			logger.error("code:"+resultMap.get("code")+"===response:"+resultMap.get("response"));
			resultData.setResultCode("fail");
			resultData.setResultMessage("合并请求视频文件提交失败，请稍后重试！");
		}
		return resultData;
	}

	private Map<String,String> requestParams(String streamId,String order,String urlEncode){
		String[] videoUrl = urlEncode.split("/");
		String urls = "";
		for (int i=0;i<videoUrl.length;i++){
			if(i > 0){
				urls += videoUrl[i] +"/";
			}
		}
		String blive= "blive-record";
		String bucket = EncodeUtils.urlsafeEncodeString(blive.getBytes());
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateTime = df.format(date);
		String key = videoUrl[0];
		String filekey = "blive-"+streamId+"-"+dateTime+".m3u8";
		String saveas = blive+":"+filekey;
		urls = urls.substring(0,urls.length()-1);
		String fops ="avconcat/m3u8/segtime/15/mode/1/concatorder/"+order+"/"+urls+"|saveas/"+EncodeUtils.urlsafeEncodeString(saveas.getBytes());
		fops = EncodeUtils.urlsafeEncodeString(fops.getBytes());
		String notifyURL = EncodeUtils.urlsafeEncodeString("http://api.wopaitv.com/restcheck/wcs/mosaicLiveVideo".getBytes());
		Map<String,String> postParams = new HashMap<String, String>();

		postParams.put("bucket",bucket);
		postParams.put("key",key);
		postParams.put("fops",fops);
		postParams.put("notifyURL",notifyURL);
		postParams.put("force","1");
		postParams.put("separate","1");
		return postParams;
	}

	private String createSign(String streamId,String order,String urlEncode){
		String[] videoUrl = urlEncode.split("/");
		String urls = "";
		for (int i=0;i<videoUrl.length;i++){
			if(i > 0){
				urls += videoUrl[i] +"/";
			}
		}
		String blive= "blive-record";
		String bucket = EncodeUtils.urlsafeEncodeString(blive.getBytes());
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateTime = df.format(date);
		String key = videoUrl[0];
		String filekey = "blive-"+streamId+"-"+dateTime+".m3u8";
		String saveas = blive+":"+filekey;
		urls = urls.substring(0,urls.length()-1);
		String fops ="avconcat/m3u8/segtime/15/mode/1/concatorder/"+order+"/"+urls+"|saveas/"+EncodeUtils.urlsafeEncodeString(saveas.getBytes());
		fops = EncodeUtils.urlsafeEncodeString(fops.getBytes());
		String notifyURL = EncodeUtils.urlsafeEncodeString("http://api.wopaitv.com/restcheck/wcs/mosaicLiveVideo".getBytes());
		String signingStr = "/fops\n"+"bucket="+bucket+"&key="+key+"&fops="+fops+"&notifyURL="+notifyURL+"&force=1&separate=1";
		String sign = CommonUtils.HMACSHA1(signingStr,"6d98979e12f3872c3e1ccb1fb103df80699f2a1d");
		String encodeSign = EncodeUtils.urlsafeEncodeString(sign.getBytes());
		String accessToken = "1b007666ab862a94599e8e50b62f7a8dfbab2dbd:"+encodeSign;
		return accessToken;
	}

}
