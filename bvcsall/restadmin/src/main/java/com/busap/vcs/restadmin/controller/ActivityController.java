package com.busap.vcs.restadmin.controller;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.busap.vcs.data.entity.*;
import com.busap.vcs.data.model.ActivityDisplay;
import com.busap.vcs.data.model.ActivityVideoDisplay;
import com.busap.vcs.data.model.EvaluationDisplay;
import com.busap.vcs.data.model.ExportActivityVideo;
import com.busap.vcs.restadmin.utils.CommonUtils;
import com.busap.vcs.restadmin.utils.EnableFunction;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.*;
import com.busap.vcs.service.utils.ExcelUtils;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.PagingContextHolder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.busap.vcs.base.Filter;
import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.OrderByObject;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.data.enums.VideoStatus;
import com.busap.vcs.service.impl.SolrWoPaiTagService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.controller.CRUDForm;
import org.springframework.web.servlet.ModelAndView;


/**
 * 视频
 *
 * @author meizhiwen
 *
 */
@Controller()
@RequestMapping("activity")
public class ActivityController extends CRUDController<Activity, Long> {

	private static final Logger logger = LoggerFactory
			.getLogger(ActivityController.class);

	@Resource(name = "activityService")
	private ActivityService activityService;

	@Resource(name = "videoService")
	private VideoService videoService;

	@Resource(name = "labelService")
	private LabelService labelService;

	@Resource(name = "hotLabelService")
	private HotLabelService hotLabelService;

	@Resource(name = "activityVideoService")
	private ActivityVideoService activityVideoService;

	@Resource
	private PrizeService prizeService;

	@Value("${files.localpath}")
	private String basePath;

	@Value("${uploadfile_remote_url}")
	private String uploadfile_remote_url;

	@Resource(name = "activityService")
	@Override
	public void setBaseService(BaseService<Activity, Long> baseService) {
		this.baseService = baseService;
	}

	@Value("${uploadpic_url_prefix}")
	private String uploadpic_url_prefix;

	@Value("${video_play_url_prefix}")
	private String video_play_url_prefix;

	@Value("${inner.actives}")
	private String innerActives;

	@Value("${solr.zookeeper.host}")
	private String solrZKHost;

	@Value("${solr.default.collection}")
	private String solrDefaultCllection;

	@Resource(name = "solrWoPaiTagService")
	private SolrWoPaiTagService solrWoPaiTagService;

	@Resource
	private EvaluationService evaluationService;

	@RequestMapping("activitylist1")
	public String list(HttpServletRequest req) {
		return "activity/list";
	}

/*	@RequestMapping("activitylist")
	public String actives(Integer page, Integer size,
			@RequestParam(value = "groupType", required = false)  Integer groupType) {
		Map<String,Object> params = new HashMap<String,Object>();

		if(page == null || page < 1){
			page = 1;
		}
		if(size == null){
			size = 10;
		}
		params.put("pageNo", page);
		params.put("pageStart", (page-1)*size);
		params.put("pageSize", size);
		if(groupType != null){
			params.put("groupType", groupType);
		}

		Page pageInfo = this.activityService.searchActivitys(params);

//		Page<Activity> ret =activityService.findAll(pr, rootList, new ArrayList(), new ArrayList(), filters, orderByObjList);
		this.request.setAttribute("actives", pageInfo.getContent());
		this.request.setAttribute("pageinfo", pageInfo);
		this.request.setAttribute("video_play_url_prefix", video_play_url_prefix);
		this.request.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		this.request.setAttribute("basePath", basePath);
		this.request.setAttribute("groupType", groupType);


		return "activity/actives";
	}*/

	@EnableFunction("活动管理,查看活动管理信息")
	@RequestMapping("activitylist")
	public String activitylist(){
		return "activity/actives";
	}

	@RequestMapping("queryActivityList")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> queryActivityList(@ModelAttribute("queryPage") JQueryPage queryPage,
												@RequestParam(value = "groupType", required = false)  Integer groupType,
												@RequestParam(value = "title", required = false)  String title,
												@RequestParam(value = "status", required = false) Integer status,
												@RequestParam(value = "startTime", required = false) String startTime,
												@RequestParam(value = "endTime", required = false) String endTime,
												@RequestParam(value = "source", required = false) String source
												){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("status",status);
		params.put("groupType",groupType);
		params.put("title",title);
		params.put("startTime",startTime);
		params.put("endTime",endTime);
		params.put("source",source);
		List<ActivityDisplay> list = activityService.queryActivities(params);
		com.busap.vcs.util.page.Page page = PagingContextHolder.getPage();
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("total",page.getTotalResult());
		map.put("rows",list);
		return map;
	}

	@RequestMapping("activityadd")
	public String add(HttpServletRequest req) {
		return "activity/add";
	}

	@RequestMapping("showActivityAdd")
	public String showActivityAdd(HttpServletRequest req, @RequestParam(value = "activetyId", required = false)  Long activityId) {
		if(activityId!=null){
			Activity activity = this.activityService.find(activityId);
			req.setAttribute("activity", activity);
		}
		req.setAttribute("uploadfile_remote_url", uploadfile_remote_url);
		return "activity/showActivityAdd";
	}


	@RequestMapping("insertActivityTemplate")
	public ModelAndView insertActivityTemplate(@RequestParam("action") String action,@RequestParam(value = "activityId",required = false) Long activityId){
        ModelAndView mav = new ModelAndView();
		mav.addObject("uploadfile_remote_url", uploadfile_remote_url);
		mav.setViewName("activity/showActivityAdd");
		return mav;
	}

	@RequestMapping("insertActivity")
	public String insertActivity(@ModelAttribute("activity") Activity activity){
		activity.setCreateDate(new Date());
		activity.setCreatorId(U.getUid());
		int result = activityService.insert(activity);
		return "redirect:activitylist";
	}

	@EnableFunction("活动管理,添加/编辑活动")
	@RequestMapping(value = {"/activeAdd"}, method = {RequestMethod.POST, RequestMethod.PUT})
	public String activeAdd(Activity entity,
			@RequestParam(value = "videoId", required = false)  Long videoId
			) {
//		Activity entity=new Activity();
//		entity.setTitle(title);
//		entity.setCover(cover);

		Activity activity =entity;

		if(entity!=null&&entity.getId()!=null){
			activity = this.activityService.find(entity.getId());
			activity.setTitle(entity.getTitle());
			if(entity.getCover()!=null&&!entity.getCover().equals("")){
				activity.setCover(entity.getCover());
			}

			activity.setBannerPic(entity.getBannerPic());

			activity.setVideoCoverPic(entity.getVideoCoverPic());

			activity.setDescription(entity.getDescription());
			activity.setPlaykey(entity.getPlaykey());

			activity.setOrder_num(entity.getOrder_num());
			activity.setGroupType(entity.getGroupType());

			activity.setTags(entity.getTags());
			
			activity.setVips(entity.getVips());
			
			activity.setRusers(entity.getRusers());
			activity.setSource(entity.getSource());

			activity.setShareImg(entity.getShareImg());
			activity.setShareText(entity.getShareText());
//			List<Video> introductionVideoList=videoService.findIntroductionVideosByActivityId(entity.getId());
//			videoService.updateIntroductionVideosByActivityId(entity.getId());
//			for (Video video : introductionVideoList) {
//				video.setIntroductionMark("0");
//				videoService.save(video);
//			}

		}
        baseService.save(activity);
        if(videoId!=null){
        	ActivityVideo av=new ActivityVideo();
			av.setActivityid(entity.getId());
			av.setVideoid(videoId);
			Long c=activityVideoService.findCountByVideoidAndActivityId( videoId, entity.getId());
			if(c==0){
				activityVideoService.save(av);
			}

			Video video=videoService.find(videoId);
			video.setIntroductionMark("1");
			videoService.save(video);
        }
        //this.operationLogService.save(new OperationLog(log_meduleType, "添加", entity.getId().toString(), entity.getClass().getSimpleName(), U.getUid(), U.getUname(), "添加"+entity.getClass().getSimpleName()));

      //针对首页推荐列表，获取视频列表中包含该活动名称的视频，并绑定到该活动
//        if(activity.getGroupType()==0&&videoId==null&&entity.getId()==null) {
//        	StringBuffer jpql = new StringBuffer();
//            List<ParameterBean> paramList=new ArrayList<ParameterBean>();
//            
//            jpql.append("select v.id FROM Video  v    WHERE    1=1  ");
//            
//            if(activity!=null&& activity.getTitle()!=null&&!activity.getTitle().equals("")) {
//    			jpql.append(" AND v.description like :activiryTitle ");
//    			
//    			ParameterBean paramBean=new ParameterBean("activiryTitle","%"+"#"+activity.getTitle()+"#"+"%",null);
//    			paramList.add(paramBean);
//    		}
//    		
//    		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
//            OrderByBean orderByObject=new OrderByBean("id",1,"v");
//            orderByList.add(orderByObject);
//            
//            List indexActivityVideoIdList=new ArrayList();
//    		try {
//    			indexActivityVideoIdList = videoService.getObjectByJpql(jpql, 0, 10000000, "v", paramList, null, orderByList);
//    		} catch (Exception e) {
//    			// TODO Auto-generated catch block
//    			e.printStackTrace();
//    		}
//    		
//    		List<ActivityVideo> svr=new ArrayList<ActivityVideo>();
//    		
//    		for (Object object : indexActivityVideoIdList) {
//    			Long tempVideoId=(Long) object;
//    			
//    			ActivityVideo sv=new ActivityVideo();
//				sv.setVideoid(tempVideoId);
//				sv.setActivityid(activity.getId());
//				sv.setCreatorId(activity.getCreatorId());
//				svr.add(sv);
//			}
//    		
//    		videoService.getActivityVideoRepository().save(svr);
//        }


        return "redirect:activitylist";
	}

	//@EnableFunction(permissionId+",加入活动")
	@RequestMapping("chooseActivity")
	@ResponseBody
	public RespBody chooseActivity(Long videoId,Long activityId){
		if(videoId!=null&&activityId!=null){
        	ActivityVideo av=new ActivityVideo();
			av.setActivityid(activityId);
			av.setVideoid(videoId);
			Long c=activityVideoService.findCountByVideoidAndActivityId( videoId, activityId);
			if(c==0){
				activityVideoService.save(av);
			}
        }
		return this.respBodyWriter.toSuccess();
	}


	@RequestMapping("activityedit")
	public String edit(Long id,HttpServletRequest req) {
		Activity entity = this.activityService.find(id);
		req.setAttribute("entity", entity);
		return "activity/edit";
	}

	@RequestMapping(value = {"/updatepage"}, method = {RequestMethod.POST, RequestMethod.PUT})
    public String update(Activity entity,HttpServletRequest req) {
        baseService.update(entity);
        this.operationLogService.save(new OperationLog(log_meduleType, "更新", entity.getId().toString(), entity.getClass().getSimpleName(), U.getUid(), U.getUname(), "更新"+entity.getClass().getSimpleName()));
        return "redirect:activitylist";
    }

	/* (non-Javadoc)
	 * @see com.busap.vcs.webcomn.controller.CRUDController#create(com.busap.vcs.data.entity.BaseEntity)
	 */
	@RequestMapping(value = {"/createupdate"}, method = {RequestMethod.POST, RequestMethod.PUT})
	public String createactivity(Activity entity) {
//		Activity entity=new Activity();
//		entity.setTitle(title);
//		entity.setCover(cover);
        baseService.save(entity);
        this.operationLogService.save(new OperationLog(log_meduleType, "添加", entity.getId().toString(), entity.getClass().getSimpleName(), U.getUid(), U.getUname(), "添加" + entity.getClass().getSimpleName()));
        return "redirect:activitylist";
	}


	@RequestMapping(value = {"/activityupload"}, method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
	public RespBody upload(@RequestParam MultipartFile hlsfiles) {

		String fileName = hlsfiles.getOriginalFilename();
		String sufix = fileName.substring(fileName.lastIndexOf("."));
    	String relPath = File.separator+"videoPic"+File.separator+DateFormatUtils.format(new Date(), "yyyy-MM-dd")+File.separator;
        String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + sufix;

		//上传
//    	String relPath = File.separator+"activitycover"+File.separator+DateFormatUtils.format(new Date(), "yyyy-MM-dd")+File.separator;
//        String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_")+hlsfiles.getOriginalFilename();
        String relUrl = "";
        try {
        	FileUtils.copyInputStreamToFile(hlsfiles.getInputStream(), new File(basePath+relPath, originalFilename));
            relUrl = relPath+originalFilename;
        } catch (IOException e) {
        	logger.error("文件[" + originalFilename + "]上传失败",e);
        }
        return respBodyWriter.toSuccess(relUrl);
	}

	@RequestMapping(value = {"/activityUploadFile"}, method = {RequestMethod.POST, RequestMethod.PUT})
	@ResponseBody
	public RespBody activityUploadFile(@RequestParam MultipartFile shareImgFile) {

		String fileName = shareImgFile.getOriginalFilename();
		String sufix = fileName.substring(fileName.lastIndexOf("."));
		String relPath = File.separator + "videoPic" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
		String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + sufix;

		//上传
		String relUrl = "";
		try {
			FileUtils.copyInputStreamToFile(shareImgFile.getInputStream(), new File(basePath+relPath, originalFilename));
			relUrl = relPath + originalFilename;
		} catch (IOException e) {
			logger.error("文件[" + originalFilename + "]上传失败",e);
		}
		return respBodyWriter.toSuccess(relUrl);
	}

	@RequestMapping(value = {"/activityBannerPicFileupload"}, method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
	public RespBody activityBannerPicFileupload(@RequestParam MultipartFile bannerPicFile) {

		String fileName = bannerPicFile.getOriginalFilename();
		String sufix = fileName.substring(fileName.lastIndexOf("."));
    	String relPath = File.separator+"videoPic"+File.separator+DateFormatUtils.format(new Date(), "yyyy-MM-dd")+File.separator;
        String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + sufix;

		//上传
//    	String relPath = File.separator+"activityBannerPic"+File.separator+DateFormatUtils.format(new Date(), "yyyy-MM-dd")+File.separator;
//        String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_")+bannerPicFile.getOriginalFilename();
        String relUrl = "";
        try {
        	FileUtils.copyInputStreamToFile(bannerPicFile.getInputStream(), new File(basePath+relPath, originalFilename));
            relUrl = relPath+originalFilename;
        } catch (IOException e) {
        	logger.error("文件[" + originalFilename + "]上传失败",e);
        }
        return respBodyWriter.toSuccess(relUrl);
	}

	@RequestMapping(value = {"/activityVideoCoverPicFileupload"}, method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
	public RespBody activityVideoCoverPicFileupload(@RequestParam MultipartFile videoCoverPicFile) {

		String fileName = videoCoverPicFile.getOriginalFilename();
		String sufix = fileName.substring(fileName.lastIndexOf("."));
    	String relPath = File.separator+"videoPic"+File.separator+DateFormatUtils.format(new Date(), "yyyy-MM-dd")+File.separator;
        String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + sufix;

		//上传
//    	String relPath = File.separator+"activityVideoCoverPic"+File.separator+DateFormatUtils.format(new Date(), "yyyy-MM-dd")+File.separator;
//        String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_")+videoCoverPicFile.getOriginalFilename();
        String relUrl = "";
        try {
        	FileUtils.copyInputStreamToFile(videoCoverPicFile.getInputStream(), new File(basePath+relPath, originalFilename));
            relUrl = relPath+originalFilename;
        } catch (IOException e) {
        	logger.error("文件[" + originalFilename + "]上传失败",e);
        }
        return respBodyWriter.toSuccess(relUrl);
	}

	@RequestMapping("updateOrderNum")
	@ResponseBody
	public String updateOrderNum(Long activeId,Integer orderNum){
		long count=activityService.findByOrderNum(orderNum);
		if(count>0){
			return "exist";
		}
		activityService.updateOrderNum( activeId, orderNum);
		return "ok";
	}

	@RequestMapping("isOrderNumOnly")
	@ResponseBody
	public String isOrderNumOnly(Long activeId,Integer orderNum){
		if(activeId==null) {
			long count=activityService.findByOrderNum( orderNum);
			if(count>0){
				return "exist";
			}
		}else {
			long count=activityService.findByOrderNum( orderNum,activeId);
			if(count>0){
				return "exist";
			}
		}

		return "ok";
	}

	@EnableFunction("活动管理,更新活动状态信息【上线下线】")
	@RequestMapping("updateActiveStatus")
	@ResponseBody
	public String updateActiveStatus(Long activeId,Integer status){
		activityService.updateActiveStatus(activeId, status);
		return "ok";
	}

	@EnableFunction("活动管理,查看活动详情")
	@RequestMapping("activityDetail")
	public String activityDetail(Integer page, Integer size, String searchItem,
			@RequestParam(value = "keyword", required = false)  String keyword,
			@RequestParam(value = "description", required = false)  String description,
			@RequestParam(value = "ruserName", required = false)  String ruserName,
			@RequestParam(value = "activityId", required = false)  Integer activityId,
			String status, CRUDForm curdForm) throws Exception {
		if(activityId!=null){
			Activity a=activityService.find(Long.valueOf(activityId));
			if(a!=null&&a.getPlaykey()!=null&&!a.getPlaykey().equals("")) {
				String activityPlayKey=a.getPlaykey().replace("-", File.separator);
				request.setAttribute("activityPlayKey", activityPlayKey);
			}
			request.setAttribute("active", a);
		}
		if(page==null) {
			page=1;
        }
        if(size==null) {
        	size=10;
        }
        StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();

        jpql.append("FROM Video video left join video.activity activity , Ruser u,ActivityVideo av   WHERE  video.flowStat<>'delete' and video.creatorId = u.id and video.id=av.videoid and activity.id=av.activityid and activity.id= "+activityId);
        countJpql.append("SELECT COUNT(*)  FROM  Video video left join video.activity activity , Ruser u ,ActivityVideo av   WHERE video.flowStat<>'delete' and  video.creatorId = u.id and video.id=av.videoid and activity.id=av.activityid and  activity.id= "+activityId);

        if(keyword!=null&& !keyword.equals("")) {
			jpql.append(" AND video.name like :name ");
			countJpql.append(" AND video.name like :name ");

			ParameterBean paramBean=new ParameterBean("name","%"+keyword+"%",null);
			paramList.add(paramBean);
			this.request.setAttribute("keyword", keyword);
		}

        if(description!=null&& !description.equals("")) {
			jpql.append(" AND video.description like :description ");
			countJpql.append(" AND video.description like :description ");

			ParameterBean paramBean=new ParameterBean("description","%"+description+"%",null);
			paramList.add(paramBean);
			this.request.setAttribute("description", description);
		}

        if(ruserName!=null&& !ruserName.equals("")) {
			jpql.append(" AND u.name like :ruserName ");
			countJpql.append(" AND u.name like :ruserName ");

			ParameterBean paramBean=new ParameterBean("ruserName","%"+ruserName+"%",null);
			paramList.add(paramBean);
			this.request.setAttribute("ruserName", ruserName);
		}
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
		OrderByBean orderByObject1=new OrderByBean("orderNum",1,"av");
        orderByList.add(orderByObject1);
        OrderByBean orderByObject=new OrderByBean("publishTime",1,"video");
        orderByList.add(orderByObject);
        List musicList=videoService.getObjectByJpql(jpql, page, size, "video", paramList, null, orderByList);
        List mList=new ArrayList();
        for(int i=0;i<musicList.size();i++) {
        	Object[] obj=(Object[]) musicList.get(i);
        	Video video=(Video) obj[0];
        	Ruser user=(Ruser) obj[2];
        	video.setUploader(user.getName());
        	video.setActivityVideo((ActivityVideo) obj[3]);
        	mList.add(video);
        }

        Long totalCount=videoService.getObjectCountByJpql(countJpql, paramList);

        Pageable pageable = new PageRequest(page == null ? 1 : page, size == null ? 10 : size,null);
        Page resultPage = new PageImpl(mList, pageable, totalCount);
        this.request.setAttribute("hotVideos", mList);
        this.request.setAttribute("page", page);
        this.request.setAttribute("size", size);
        Long pageValue=(long) (page == null ? 0 : page-1);
		Long sizeValue=(long) (size == null ? 10 : size);
        this.request.setAttribute("pageCount", pageValue*sizeValue);
        this.request.setAttribute("pageinfo", resultPage);

		this.request.setAttribute("searchItem", searchItem);
		this.request.setAttribute("keyword", keyword);
		this.request.setAttribute("ruserName", ruserName);
		this.request.setAttribute("status", (status == null || status.trim()
				.equals("")) ? VideoStatus.审核通过.getName() : status);
		this.request.setAttribute("video_play_url_prefix", video_play_url_prefix);
		this.request.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		return "activity/activityDetail";
	}

	@EnableFunction("活动管理,查看活动详情")
	@RequestMapping("queryActivityDetail")
	public ModelAndView queryActivityDetail(Long activityId){
		ModelAndView mav = new ModelAndView();
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id",activityId);
		List<ActivityDisplay> list = activityService.queryActivities(params);
		ActivityDisplay activity = new ActivityDisplay();
		for (ActivityDisplay obj : list) {
			activity = obj;
		}
		List<Activity> activityList = this.activityService.findAll();
		mav.addObject("activites", activityList);
		mav.addObject("video_play_url_prefix", video_play_url_prefix);
		mav.addObject("activity",activity);
		mav.addObject("activityId",activityId);
		Map<String,Object> map = activityService.activityDatas(activityId);
		mav.addObject("vCount",map.get("majiaVideoCount"));
		mav.addObject("uvCount",map.get("userVideoCount"));
		mav.addObject("uCount",map.get("userCount"));
		mav.addObject("pCount",map.get("praiseCount"));
		mav.addObject("eCount",map.get("evaluationCount"));
		mav.setViewName("activity/activityDetail");
		return mav;
	}

	@EnableFunction("活动管理,查看活动视频信息")
	@RequestMapping("queryActivityVideos")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> queryActivityVideosByActivityId(@ModelAttribute("queryPage") JQueryPage queryPage,
															  Integer activityId,
															  @RequestParam(value = "user",required = false) Long user,
															  @RequestParam(value = "userKeyword",required = false) String userKeyword,
															  @RequestParam(value = "dataFrom",required = false) String dataFrom,
															  @RequestParam(value = "description",required = false) String description,
															  @RequestParam(value = "startTime",required = false) String startTime,
															  @RequestParam(value = "endTime",required = false) String endTime){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("activityId",activityId);
		params.put("user",user);
		params.put("userKeyword",userKeyword);
		params.put("dataFrom",dataFrom);
		params.put("description",description);
		params.put("startTime",startTime);
		params.put("endTime",endTime);
		List<ActivityVideoDisplay> list = activityVideoService.queryActivityVideos(params);
		com.busap.vcs.util.page.Page page = PagingContextHolder.getPage();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("total",page.getTotalResult());
		map.put("rows",list);
		return map;
	}

	@RequestMapping("showRemoveActivityList")
	public String showRemoveActivityList(Integer page, Integer size, String searchItem,
			@RequestParam(value = "keyword", required = false)  String keyword,
			@RequestParam(value = "ruserName", required = false)  String ruserName,
			@RequestParam(value = "activityId", required = false)  Long activityId,
			@RequestParam(value = "videoId", required = false)  Long videoId,
			String status, CRUDForm curdForm) {
		List<Filter> filters = new ArrayList<Filter>();

		Sort s = new Sort(Direction.DESC,"playRateToday");


//		filters.add(Filter.ne("activity","","id", ));

		Pageable pr = new PageRequest(page == null ? 1 : page, size == null ? 100 : size,s);

		Long pageValue=(long) (page == null||page == 1 ? 0 : page);
		Long sizeValue=(long) (size == null ? 10 : size);
		this.request.setAttribute("pageCount", pageValue*sizeValue);

		List<RootInfo> rootList=new ArrayList<RootInfo>();
		RootInfo rf=new RootInfo();
		rf.setName("activity");
		rf.setClassType(Activity.class);
		rootList.add(rf);


		List<JoinInfo> joinList=new ArrayList<JoinInfo>();

//		JoinInfo ji=new JoinInfo();
//		ji.setClassType(Video.class);
//		ji.setCollectionType("set");
//		ji.setJoinType(JoinType.LEFT);
//		ji.setName("video");
//		ji.setRootName("activity");
//		joinList.add(ji);

		List<OrderByObject> orderByObjList=new ArrayList<OrderByObject>();
		OrderByObject orderByObject=new OrderByObject("activity", "order_num", OrderByObject.OrderByType.ASC.getCode());
		orderByObjList.add(orderByObject);


		Page<Activity> ret =activityService.findAll(pr, rootList, new ArrayList(), new ArrayList(), filters, orderByObjList);
		this.request.setAttribute("actives", ret.getContent());
		this.request.setAttribute("pageinfo", ret);
		this.request.setAttribute("searchItem", searchItem);
		this.request.setAttribute("keyword", keyword);
		this.request.setAttribute("ruserName", ruserName);
		this.request.setAttribute("status", (status == null || status.trim()
				.equals("")) ? VideoStatus.审核通过.getName() : status);
		this.request.setAttribute("video_play_url_prefix", video_play_url_prefix);
		this.request.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		this.request.setAttribute("basePath", basePath);

		this.request.setAttribute("activityId", activityId);
		this.request.setAttribute("videoId", videoId);
		return "activity/showRemoveActivityList";
	}

	@EnableFunction("活动管理,活动详情-移出视频")
	@RequestMapping("removeActivity")
	public String removeActivity(
			@RequestParam(value = "outType", required = true)  Integer outType,
			@RequestParam(value = "ids", required = false)  Long[] activityIds,
			@RequestParam(value = "activityId", required = false)  Long activityId,
			@RequestParam(value = "videoId", required = false)  Long videoId,
			String status, CRUDForm curdForm) {

		if(outType==0){

		}else{
			if(activityIds!=null&&activityIds.length>0){
				for(int i=0;i<activityIds.length;i++){
					ActivityVideo av=new ActivityVideo();
					av.setActivityid(activityIds[i]);
					av.setVideoid(videoId);
					Long c=activityVideoService.findCountByVideoidAndActivityId( videoId, activityIds[i]);
					if(c==0){
						activityVideoService.save(av);
					}

				}

			}
		}

		activityVideoService.deleteByVideoidAndActivityId(videoId, activityId);

		this.request.setAttribute("closeFlg", "true");

		return "activity/showRemoveActivityList";
	}


	@RequestMapping("labelList")
	public String labelList(HttpServletRequest httpServletRequest) {
		//前台页面改为easyui datagrid获取数据，使用labelListJson方法，此方法被注释 huoshanwei 2015.09.17 13:26
		/*
		if(page==null) {
			page=1;
        }
        if(size==null) {
        	size=10;
        }

        StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();

        jpql.append("FROM Label label  WHERE 1=1  ");
        countJpql.append("SELECT COUNT(*)  FROM  Label label  WHERE 1=1 ");

        if(keyword!=null&& !keyword.equals("")) {
			jpql.append(" AND label.name like :name ");
			countJpql.append(" AND label.name like :name ");

			ParameterBean paramBean=new ParameterBean("name","%"+keyword+"%",null);
			paramList.add(paramBean);
			this.request.setAttribute("keyword", keyword);
		}

		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("num",1,"label");
        orderByList.add(orderByObject);

        List labelList=labelService.getObjectByJpql(jpql, page, size, "label", paramList, null, orderByList);

        Long totalCount=labelService.getObjectCountByJpql(countJpql, paramList);

        Pageable pageable = new PageRequest(page == null ? 1 : page, size == null ? 10 : size,null);
        Page resultPage = new PageImpl(labelList, pageable, totalCount);

        this.request.setAttribute("labelList", labelList);
        this.request.setAttribute("page", page);
        this.request.setAttribute("size", size);
		Long pageValue=(long) (page == null ? 0 : page-1);
		Long sizeValue=(long) (size == null ? 10 : size);
        this.request.setAttribute("pageCount", pageValue*sizeValue);
        this.request.setAttribute("pageinfo", resultPage);

		this.request.setAttribute("searchItem", searchItem);
		this.request.setAttribute("keyword", keyword);
		this.request.setAttribute("video_play_url_prefix", video_play_url_prefix);
		this.request.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);*/
		return "label/labelList";
	}

    /**
     * 话题管理 获取话题列表信息
     * @param page 页数
     * @param rows 每页显示数量
     * @param keyword 搜索关键字
     * @author shanwei.huo
     *
     */
	@RequestMapping("queryLabelList")
	@ResponseBody
	public Map<String,Object> queryLabelList(Integer page, Integer rows,@RequestParam(value = "keyword", required = false) String keyword){
		if(page == null || page < 1){
			page = 1;
		}
		if(rows == null || rows < 1){
			rows = 20;
		}
		HashMap<String,Object> pageParam = new HashMap<String, Object>();
		pageParam.put("name",keyword);
		Integer pageTotal = labelService.queryLabelsCount(pageParam);

		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("name",keyword);
		params.put("pageStart",(page - 1)*rows);
		params.put("pageSize",rows);
		List<Label> pageList = labelService.queryLabels(params);
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("total",pageTotal);
		map.put("rows",pageList);
		return map;
	}

	@RequestMapping("labelListJson")
	@ResponseBody
	public Map labelListJson(Integer page, Integer rows, String searchItem,
			@RequestParam(value = "keyword", required = false)  String keyword,
			@RequestParam(value = "description", required = false)  String description,
			CRUDForm curdForm) throws Exception {
		if(page==null) {
			page=1;
        }
        if(rows==null) {
			rows=20;
        }

        StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();

        jpql.append("FROM Label label  WHERE 1=1  ");
        countJpql.append("SELECT COUNT(*)  FROM  Label label  WHERE 1=1 ");
        if(keyword!=null&& !keyword.equals("")) {
			jpql.append(" AND label.name like :name ");
			countJpql.append(" AND label.name like :name ");
			ParameterBean paramBean=new ParameterBean("name","%"+keyword+"%",null);
			paramList.add(paramBean);
			this.request.setAttribute("keyword", keyword);
		}

		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("num",1,"label");
        orderByList.add(orderByObject);

        List labelList=labelService.getObjectByJpql(jpql, page, rows, "label", paramList, null, orderByList);

        Long totalCount=labelService.getObjectCountByJpql(countJpql, paramList);

		Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", totalCount);//total键 存放总记录数，必须的 
        jsonMap.put("rows", labelList);//rows键 存放每页记录 list  
        return jsonMap;
	}


	@RequestMapping(value = {"/labelAdd"}, method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
	public String labelAdd(@RequestParam(value = "newLabelName", required = false)  String newLabelName) throws Exception {



        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();

		countJpql.append("SELECT COUNT(*)  FROM  Label label  WHERE 1=1   ");

        if(newLabelName!=null&& !newLabelName.equals("")) {

			countJpql.append(" AND label.name = :name ");

			ParameterBean paramBean=new ParameterBean("name",newLabelName,null);
			paramList.add(paramBean);

		}


		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("playRateToday",1,"video");
        orderByList.add(orderByObject);






        Long totalCount=labelService.getObjectCountByJpql(countJpql, paramList);

        if(totalCount>0) {
        	return "ok";
        }

		Label newLabel=new Label();
		newLabel.setName(newLabelName);
		newLabel.setNum(0l);

		labelService.save(newLabel);

//		CloudSolrClient server = new CloudSolrClient(solrZKHost);
//		server.setDefaultCollection(solrDefaultCllection);
//		SolrInputDocument doc = new SolrInputDocument();
//		doc.addField("id", newLabel.getId());
//		doc.addField("num", newLabel.getNum());
//		doc.addField("name", newLabel.getName());
//		server.add(doc);
//		server.commit();

		solrWoPaiTagService.index(newLabel.getId(), newLabel.getName(),newLabel.getNum());



        return "ok";
	}

	@RequestMapping(value = {"/findSuggestLabel"}, method = {RequestMethod.GET,RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
	public List<String> findSuggestLabel(@RequestParam(value = "term", required = false)  String term) throws Exception {


		term=new String(term.getBytes("iso-8859-1"),"utf-8");

		if(term!=null&&!term.equals("")) {
			List<String> nameList=solrWoPaiTagService.findSuggestLabel(term);
			if(nameList!=null&&nameList.size()>0) {
				return nameList;
			}
		}

		return null;
	}

	@RequestMapping(value = "queryHotLabelList")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> queryHotLabelList(@ModelAttribute("queryPage") JQueryPage queryPage,
												@RequestParam(value = "keyword",required = false) String keyword){
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("labelName",keyword);
		List<HotLabel> list = hotLabelService.queryHotLabels(params);
		com.busap.vcs.util.page.Page page = PagingContextHolder.getPage();
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("total",page.getTotalResult());
		map.put("rows",list);
		return map;
	}

	@RequestMapping(value = "hotLabelList")
	public String hotLabelList(){
		return "label/hotLabelList";
	}

	/*@RequestMapping("hotLabelList")
	public String hotLabelList(Integer page, Integer size, String searchItem,
			@RequestParam(value = "keyword", required = false)  String keyword,
			@RequestParam(value = "description", required = false)  String description,
			CRUDForm curdForm) throws Exception {


		if(page==null) {
			page=1;
        }
        if(size==null) {
        	size=20;
        }

        StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();

        jpql.append("FROM HotLabel hotLabel  WHERE 1=1  ");
        countJpql.append("SELECT COUNT(*)  FROM  HotLabel hotLabel  WHERE 1=1 ");

        if(keyword!=null&& !keyword.equals("")) {
			jpql.append(" AND hotLabel.labelName like :name ");
			countJpql.append(" AND hotLabel.labelName like :name ");

			ParameterBean paramBean=new ParameterBean("name","%"+keyword+"%",null);
			paramList.add(paramBean);
			this.request.setAttribute("keyword", keyword);
		}





		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("displayOrder",1,"hotLabel");
        orderByList.add(orderByObject);


        List hotLabelList=hotLabelService.getObjectByJpql(jpql, page, size, "hotLabel", paramList, null, orderByList);




        Long totalCount=hotLabelService.getObjectCountByJpql(countJpql, paramList);

        Pageable pageable = new PageRequest(page == null ? 1 : page, size == null ? 10 : size,null);
        Page resultPage = new PageImpl(hotLabelList, pageable, totalCount);

        this.request.setAttribute("hotLabelList", hotLabelList);
        this.request.setAttribute("page", page);
        this.request.setAttribute("size", size);
		Long pageValue=(long) (page == null ? 0 : page-1);
		Long sizeValue=(long) (size == null ? 10 : size);
        this.request.setAttribute("pageCount", pageValue*sizeValue);
        this.request.setAttribute("pageinfo", resultPage);

        if(totalCount<20) {
        	request.setAttribute("addFlg", true);
        }else {
        	request.setAttribute("addFlg", false);
        }




		this.request.setAttribute("searchItem", searchItem);
		this.request.setAttribute("keyword", keyword);
		this.request.setAttribute("video_play_url_prefix", video_play_url_prefix);
		this.request.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		return "label/hotLabelList";
	}*/


	@RequestMapping("showHotLabelAdd")
	public String showHotLabelAdd(HttpServletRequest req,
			@RequestParam(value = "hotLabelId", required = false)  Long hotLabelId
			) {

		if(hotLabelId!=null){
			HotLabel label = hotLabelService.find(hotLabelId);
			req.setAttribute("label", label);


		}

		return "label/showHotLabelAdd";
	}

	@RequestMapping(value = {"/hotLabelAdd"}, method = {RequestMethod.POST, RequestMethod.PUT})
	public String hotLabelAdd(HotLabel entity,
			@RequestParam(value = "videoId", required = false)  Long videoId
			) throws Exception {
		System.out.println(entity.getLabelName());
		HotLabel hotLabel =entity;

        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();

        countJpql.append("SELECT COUNT(*)  FROM  HotLabel hotLabel  WHERE 1=1 ");

        if(entity.getLabelName()!=null&& !entity.getLabelName().equals("")) {
			countJpql.append(" AND hotLabel.labelName = :name ");

			ParameterBean paramBean=new ParameterBean("name",entity.getLabelName(),null);
			paramList.add(paramBean);
		}


        Long totalCount=hotLabelService.getObjectCountByJpql(countJpql, paramList);

        if(totalCount==0) {
        	hotLabelService.save(hotLabel);
        }



        return "redirect:hotLabelList";
	}

	@RequestMapping("removeHotLabel")
	public String removeHotLabel(
			@RequestParam(value = "hotLabelId", required = false)  Long hotLabelId,
			String status, CRUDForm curdForm) {


		hotLabelService.delete(hotLabelId);


		return "redirect:hotLabelList";
	}

	@RequestMapping("deleteHotLabel")
	@ResponseBody
	public String deleteHotLabel(
			@RequestParam(value = "hotLabelId", required = false)  Long hotLabelId,
			String status, CRUDForm curdForm) {


		hotLabelService.delete(hotLabelId);


		return "ok";
	}



	@RequestMapping("showImportLabel")
	public String showImportLabel(Integer page, Integer size, String searchItem,
			@RequestParam(value = "keyword", required = false)  String keyword,
			@RequestParam(value = "description", required = false)  String description,
			CRUDForm curdForm) throws Exception {



		return "label/showImportLabel";
	}

	@RequestMapping("importFromTagToLabel")
	@ResponseBody
	public String importFromTagToLabel(
			@RequestParam(value = "hotLabelId", required = false)  Long hotLabelId,
			String status, CRUDForm curdForm) throws Exception {


		 StringBuffer jpql = new StringBuffer();
	        StringBuffer countJpql = new StringBuffer();
	        List<ParameterBean> paramList=new ArrayList<ParameterBean>();

	        jpql.append("FROM Tag tag ");



	        List tagList=hotLabelService.getObjectByJpql(jpql, 1, 111111111, "tag", paramList, null, null);

	        if(tagList!=null&&tagList.size()>0) {
	        	for (Object object : tagList) {

	        		Tag tag=(Tag) object;


	        		StringBuffer countJpql1 = new StringBuffer();
	                List<ParameterBean> paramList1=new ArrayList<ParameterBean>();

	        		countJpql1.append("SELECT COUNT(*)  FROM  Label label  WHERE 1=1   ");

	                if(tag.getName()!=null&& !tag.getName().equals("")) {

	        			countJpql1.append(" AND label.name = :name ");

	        			ParameterBean paramBean=new ParameterBean("name",tag.getName(),null);
	        			paramList1.add(paramBean);

	        		}




	                Long totalCount=labelService.getObjectCountByJpql(countJpql1, paramList1);
	                if(totalCount>0) {
	                	continue;

	                }
	        		Label newLabel=new Label();
	        		newLabel.setName(tag.getName());
	        		newLabel.setNum(0l);

	        		labelService.save(newLabel);

				}

	        }




		return "ok";
	}

	@RequestMapping("importFromVideoTagToLabel")
	@ResponseBody
	public String importFromVideoTagToLabel(
			@RequestParam(value = "hotLabelId", required = false)  Long hotLabelId,
			String status, CRUDForm curdForm) {


		Session session = labelService.getSession();
		Connection conn = session.connection();

		Set<String> tagSet=new HashSet<String>();

		try {
			conn.setAutoCommit(false);

			PreparedStatement queryPS = conn.prepareStatement("select v.tag from video v where (v.tag is not null or v.tag !='' or v.tag !=' ')  ");//获取视频tag字段

			ResultSet rest = queryPS.executeQuery();


			 while(rest.next()) {

				 String tagStr=rest.getString(1);
				 String[] tagArr=tagStr.trim().split(" ");
				 for (String tag : tagArr) {
					 if(StringUtils.isNotBlank(tag)) {
						 tagSet.add(tag.trim());
					 }

				}

			 }





		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Connection conn =SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();


		   //执行预处理语句获取查询结果集

		 long count=1;

//		 for (int i = 0; i < 3; i++) {
//			PreparedStatement insertPS;
//			try {
//				insertPS = conn.prepareStatement("insert into label(name)values(?)");
//				insertPS.setString(1,"87654321");
//		    	insertPS.executeUpdate();
//		    	
//		    	count++;
//		    	conn.commit();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	    	
//			
//		}
//		 PreparedStatement insertPS2;
//		try {
//			insertPS2 = conn.prepareStatement("insert into label(name)values(?)");
//			insertPS2.setString(1,"555566666");
//	    	insertPS2.executeUpdate();
//	    	
//	    	conn.commit();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}





		 for (String tag : tagSet) {
			 String tagTemp=tag;


			PreparedStatement insertPS;
			try {
				insertPS = conn.prepareStatement("insert into label(name)values(?)");
				insertPS.setString(1,tag);
		    	insertPS.executeUpdate();
		    	conn.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}


//		conn.commit();


		return "ok";
	}


	@RequestMapping("generateVideoLabelRelation")
	@ResponseBody
	public String generateVideoLabelRelation(
			@RequestParam(value = "hotLabelId", required = false)  Long hotLabelId,
			String status, CRUDForm curdForm) throws Exception {


		 StringBuffer jpql = new StringBuffer();
	        StringBuffer countJpql = new StringBuffer();
	        List<ParameterBean> paramList=new ArrayList<ParameterBean>();

	        jpql.append("FROM Label label ");



	        List labelList=labelService.getObjectByJpql(jpql, 1, 111111111, "label", paramList, null, null);

	        Session session = labelService.getSession();
    		Connection conn = session.connection();
    		conn.setAutoCommit(false);
//    		Connection conn =SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();


	        if(labelList!=null&&labelList.size()>0) {//遍历所以label
	        	for (Object object : labelList) {

	        		Label label=(Label) object;




					PreparedStatement queryPS = conn.prepareStatement("select v.id from video v where v.tag like ? ");//获取所以包含当前label的视频id

					queryPS.setString(1, "%"+label.getName()+"%");


					ResultSet rest = queryPS.executeQuery();   //执行预处理语句获取查询结果集
					conn.commit();
					long count=1;
				    while(rest.next()){       //循环遍历查询结果集，逐条插入关联关系表

				    	PreparedStatement insertPS = conn.prepareStatement("insert into label_video(label_name,video_id,label_id)values(?,?,?)");
				    	insertPS.setString(1,label.getName());
				    	insertPS.setLong(2, rest.getLong(1));
				    	insertPS.setLong(3, label.getId());
				    	insertPS.executeUpdate();
				    	if(count%20==0) {//每20条提交一次，释放缓存
				    		conn.commit();
				    	}
				    	count++;
				    }

					conn.commit();


				}

	        }
		return "ok";
	}

	@RequestMapping("updateHotLabelDisplayOrder")
	@ResponseBody
	public String updateHotLabelDisplayOrder(
			@RequestParam(value = "hotLabelId", required = true)  Long hotLabelId,
			@RequestParam(value = "displayOrder", required = true)  Integer displayOrder,
			String status, CRUDForm curdForm) throws Exception {
		HotLabel hotLabel=hotLabelService.find(hotLabelId);
		hotLabel.setDisplayOrder(displayOrder);
		hotLabelService.save(hotLabel);
		return "ok";
	}

	@RequestMapping("updateHotLabelShowOrder")
	@ResponseBody
	public String updateHotLabelShowOrder(
			@RequestParam(value = "hotLabelId", required = true)  Long hotLabelId,
			@RequestParam(value = "showOrder", required = true)  Integer showOrder,
			String status, CRUDForm curdForm) throws Exception {
		HotLabel hotLabel=hotLabelService.find(hotLabelId);
		hotLabel.setShowOrder(showOrder);
		hotLabelService.save(hotLabel);
		return "ok";
	}

	@RequestMapping("updateShowOrderAndDisplayOrder")
	@ResponseBody
	public String updateShowOrderAndDisplayOrder(@RequestParam(value = "id", required = true)  Long id,
												 @RequestParam(value = "showOrder", required = true)  Integer showOrder,
												 @RequestParam(value = "displayOrder", required = true)  Integer displayOrder){
		HotLabel hotLabel=hotLabelService.find(id);
		hotLabel.setShowOrder(showOrder);
		hotLabel.setDisplayOrder(displayOrder);
		hotLabelService.save(hotLabel);
		return "ok";
	}

	@EnableFunction("活动管理,批量上下线")
	@RequestMapping("batchOnline")
	@ResponseBody
	public String batchOnline(@RequestParam(value = "ids", required = true)  String ids,
							  @RequestParam(value = "type", required = true)  String type){
		String[] idArray = ids.split(",");
		List<Activity> list = activityService.selectBatchOnline(idArray);
		if("online".equals(type)){
			activityService.batchOnline(list);
		}else{
			activityService.batchOffline(list);
		}
		return "ok";

	}

	@EnableFunction("活动管理,详情列获取点赞评论等数据")
	@RequestMapping("datas")
	@ResponseBody
	public Map<String,Object> activityDatas(Long id){
		return this.activityService.activityDatas(id);
	}


	@EnableFunction("活动管理,删除活动信息")
	@RequestMapping("deleteActivity")
	@ResponseBody
	public Map<String,String> deleteActivity(Long id){
		Activity activity = activityService.selectByPrimaryKey(id);
		Map<String,String> map = new HashMap<String, String>(2);
		if(activity == null){
			map.put("resultCode","empty");
			map.put("resultMessage","删除失败，此活动不存在！");
			return map;
		}
		int result = activityService.deleteActivity(id);
		if(result > 0){
			map.put("resultCode","ok");
			map.put("resultMessage","删除成功！");
		}else{
			map.put("resultCode","error");
			map.put("resultMessage","删除失败！");
		}
		return map;
	}

	@EnableFunction("活动管理,活动详情-查看活动视频评论信息")
	@RequestMapping("viewComment")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> viewComment(@ModelAttribute("queryPage") JQueryPage queryPage,Integer id){
		Map<String,Object> map = new HashMap<String, Object>(2);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("videoId", id);
		List<EvaluationDisplay> list = evaluationService.selectEvaluations(params);
		com.busap.vcs.util.page.Page page = PagingContextHolder.getPage();
		map.put("total",page.getTotalResult());
		map.put("rows",list);
		return map;
	}

	@EnableFunction("活动管理,活动详情-活动视频排序【置顶上移下移】")
	@RequestMapping("activityVideoSort")
	@ResponseBody
	public Map<String,Object> activityVideoSort(Long activityVideoId,Long activityId,Integer type)throws Throwable{
		Map<String,Object> map = new HashMap<String, Object>();
		ActivityVideo activityVideo = activityVideoService.selectByPrimaryKey(activityVideoId);
		if(activityVideo == null){
			map.put("resultCode","empty");
			map.put("resultMessage","该活动下不存在此视频信息！");
			return map;
		}
		if(type == 1){
			Map<String,Object> maxParams = new HashMap<String, Object>();
			maxParams.put("MAX","MAX");
			maxParams.put("orderNum",activityVideo.getOrderNum());
			maxParams.put("activityId",activityId);
			ActivityVideo maxActivityVideo = activityVideoService.selectActivityVideoByOrderNum(maxParams);//置顶活动信息
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("id",activityVideoId);
			params.put("orderNum",maxActivityVideo.getOrderNum()+1);
			int result = activityVideoService.updateSort(params);
			if(result>0){
				map.put("resultCode","ok");
				map.put("resultMessage","排序成功！");
			}else{
				map.put("resultCode","error");
				map.put("resultMessage","排序失败！");
			}
		}else if(type == 2){
			map = activityVideoService.upSort(activityVideoId, activityId, activityVideo);
		}else{
			map = activityVideoService.downSort(activityVideoId,activityId,activityVideo);
		}
		return map;
	}

	@EnableFunction("活动管理,活动排序【置顶上移下移】")
	@RequestMapping("activitySort")
	@ResponseBody
	public Map<String,Object> activitySort(Long activityId,Integer type) throws Throwable{
		Map<String,Object> map = new HashMap<String, Object>();
		boolean result = activityService.activitySort(activityId,type);
		if(result){
			map.put("resultCode","ok");
			map.put("resultMessage","排序成功！");
		}else{
			map.put("resultCode","error");
			map.put("resultMessage","排序失败！");
		}
		return map;
	}

/*	public static void main(String[] args) {

	}*/

	@EnableFunction("活动管理,活动详情-批量从此活动移除")
	@RequestMapping("removeActivityVideos")
	@ResponseBody
	public Map<String,Object> removeActivityVideos(String ids)throws Throwable{
		Map<String,Object> map = new HashMap<String, Object>();
		String[] idArray = ids.split(",");
		int result = activityVideoService.deleteInPrimaryKeys(idArray);
		if(result > 0){
			map.put("resultCode","ok");
			map.put("resultMessage","移除成功！");
		}else{
			map.put("resultCode","error");
			map.put("resultMessage","移除失败！");
		}
		return map;
	}

	@EnableFunction("活动管理,活动详情-从此活动中移除")
	@RequestMapping("removeActivityVideoById")
	@ResponseBody
	public ResultData removeActivityVideoById(Long id)throws Throwable{
		ResultData resultData = new ResultData();
		int result = activityVideoService.deleteByPrimaryKey(id);
		if(result > 0){
			resultData.setResultCode("ok");
			resultData.setResultCode("移除成功！");
		}else{
			resultData.setResultCode("error");
			resultData.setResultCode("移除失败！");
		}
		return resultData;
	}

	@EnableFunction("活动管理,活动详情-导出活动视频信息")
	@RequestMapping("exportActivityVideosToExcel")
	public void exportActivityVideosToExcel(@RequestParam("activityId") Long activityId,
											@RequestParam(value = "startTime",required = false) String startTime,
											@RequestParam(value = "endTime",required = false) String endTime,
											HttpServletResponse response)throws IOException{
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("activityId",activityId);
		params.put("startTime",startTime);
		params.put("endTime",endTime);
		List<ExportActivityVideo> list = activityVideoService.queryActivityVideosByTime(params);
		ExcelUtils<ExportActivityVideo> excelUtils = new ExcelUtils<ExportActivityVideo>();
		String[] headers = {"视频描述", "上传时间","用户名", "用户ID","用户昵称","用户手机号","上传视频数", "点赞数","评论数","浏览量"};
		response.reset();
		// 设定输出文件头
		response.setHeader("Content-disposition", "attachment; filename=activity_video_info.xls");
		// 定义输出类型
		response.setContentType("application/msexcel");
		OutputStream out = response.getOutputStream();
		excelUtils.exportExcel("活动视频信息",headers,list,out,"yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 奖项活动列表页面
	 * @param
	 * @return 奖项活动列表页面
	 */
	@EnableFunction("活动管理,查看中奖活动信息")
	@RequestMapping("forwardQueryActivityPrizes")
	public ModelAndView forwardQueryActivityPrizes(@RequestParam("activityId") Long activityId){
		ModelAndView mav = new ModelAndView();
		mav.addObject("activityId",activityId);
		mav.setViewName("activity/query_prizes");
		return mav;
	}

	@RequestMapping("queryActivityPrizes")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> queryActivityPrizes(@ModelAttribute("queryPage") JQueryPage queryPage,@RequestParam("activityId") Long activityId,@ModelAttribute("Prize") Prize prize){
		prize.setActivityId(activityId);
		List<Prize> list = prizeService.queryPrizes(prize);
		com.busap.vcs.util.page.Page page = PagingContextHolder.getPage();
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total",page.getTotalResult());
		resultMap.put("rows",list);
		return resultMap;
	}

	@RequestMapping("insertHotLabel")
	@ResponseBody
	public ResultData insertHotLabel(@RequestParam("labelName") String labelName,
									 @RequestParam("labelId") Long labelId,
									 @RequestParam("displayOrder") Integer displayOrder,
									 @RequestParam("shareImg") String shareImg,
									 @RequestParam("shareText") String shareText){
		ResultData resultData = new ResultData();
		HotLabel hotLabel = hotLabelService.find(labelId);

		if (hotLabel != null) {
//			resultData.setResultCode("fail");
//			resultData.setResultMessage("添加失败,此热门话题已存在！");
			hotLabel.setShareImg(shareImg);
			hotLabel.setShareText(shareText);
			hotLabel.setDisplayOrder(displayOrder);
			hotLabel.setLabelName(labelName);
			hotLabelService.update(hotLabel);
			resultData.setResultCode("success");
			resultData.setResultMessage("修改成功！");
		} else {
			hotLabel = new HotLabel();
			hotLabel.setLabelName(labelName);
			hotLabel.setLabelId(labelId);
			hotLabel.setDisplayOrder(displayOrder);
			hotLabel.setCreateDateStr(new Date());
			hotLabel.setCreatorId(U.getUid());
			if (StringUtils.isNotBlank(shareText)) {
				hotLabel.setShareText(shareText);
			} else {
				hotLabel.setShareText("");
			}
			if (StringUtils.isNotBlank(shareImg)) {
				hotLabel.setShareImg(shareImg);
			} else {
				hotLabel.setShareImg("");
			}

			int result = hotLabelService.insert(hotLabel);
			if (result > 0) {
				resultData.setResultCode("success");
				resultData.setResultMessage("添加成功！");
			} else {
				resultData.setResultCode("fail");
				resultData.setResultMessage("添加失败！");
			}
		}
		return resultData;
	}

	@RequestMapping("findHotLabel")
	@ResponseBody
	public Map<String,Object> findHotLabel(Long hotLabelId) {
		Map<String,Object> map = new HashMap<String, Object>();
		HotLabel hotLabel = hotLabelService.find(hotLabelId);
		map.put("labelName", hotLabel.getLabelName());
		map.put("displayOrder", String.valueOf(hotLabel.getDisplayOrder()));
		map.put("shareImg", hotLabel.getShareImg());
		map.put("shareText", hotLabel.getShareText());
		return map;
	}

	@EnableFunction("活动管理,活动详情-活动视频排序")
	@RequestMapping("editActivityVideoOrderNum")
	@ResponseBody
	public ResultData editActivityVideoOrderNum(Long id,Integer orderNum){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id",id);
		params.put("orderNum",orderNum);
		int result = activityVideoService.updateSort(params);
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

	@EnableFunction("活动管理,活动管理排序【输入框】")
	@RequestMapping("editActivityOrderNum")
	@ResponseBody
	public ResultData editActivityOrderNum(Long id,Integer orderNum){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id",id);
		params.put("orderNum",orderNum);
		int result = activityService.updateSort(params);
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

}
