package com.busap.vcs.restadmin.controller;

import com.busap.vcs.data.entity.Gift;
import com.busap.vcs.data.entity.Music;
import com.busap.vcs.data.entity.MusicType;
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.model.MusicDisplay;
import com.busap.vcs.data.repository.MusicRepository;
import com.busap.vcs.data.repository.MusicTypeRepository;
import com.busap.vcs.restadmin.utils.EnableFunction;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.ComboBoxService;
import com.busap.vcs.service.MusicService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.controller.CRUDForm;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 音乐库
 * 
 * @author dmsong
 * 
 */
@Controller()
@RequestMapping("music")
public class MusicController extends CRUDController<Music, Long> {

	private static final Logger logger = LoggerFactory.getLogger(MusicController.class);

	@Value("${uploadfile_remote_url}")
	private String uploadfile_remote_url;
	
	@Value("${video_play_url_prefix}")
	private String video_play_url_prefix;
	
	@Value("${uploadpic_url_prefix}")
	private String uploadpic_url_prefix;
	
	@Value("${files.localpath}")
	private String basePath;
	
	@Resource(name = "musicService")
	private MusicService musicService;
	
	@Resource(name = "musicRepository")
	private MusicRepository musicRepository;
	
	@Resource(name = "musicTypeRepository")
	private MusicTypeRepository musicTypeRepository;

	@Resource
	private ComboBoxService comboBoxService;

	@Resource(name = "musicService")
	@Override
	public void setBaseService(BaseService<Music, Long> baseService) {
		this.baseService = baseService;
	}

/*	@RequestMapping("musiclist")
	public String list(HttpServletRequest req,
			Integer page, 
			Integer size,
			@RequestParam(value = "startDate", required = false)  String startDate,
			@RequestParam(value = "endDate", required = false)  String endDate,
			@RequestParam(value = "name", required = false)  String name,
			@RequestParam(value = "uploader", required = false)  String uploader,
			 ModelMap map
			) throws Exception {
		
		
		
		if(page==null) {
			page=1;
        }
        if(size==null) {
        	size=10;
        }
        
        StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("FROM Music music , User u   WHERE    music.creatorId = u.id  ");
        countJpql.append("SELECT COUNT(*)  FROM  Music music , User u    WHERE   music.creatorId = u.id   ");
        
        if(name!=null&& !name.equals("")) {
			jpql.append(" AND music.name like :name ");
			countJpql.append(" AND music.name like :name ");
			
			ParameterBean paramBean=new ParameterBean("name","%"+name+"%",null);
			paramList.add(paramBean);
			map.put("name", name);
		}
        
        if(uploader!=null&& !uploader.equals("")) {
			jpql.append(" AND u.username like :uploader ");
			countJpql.append(" AND u.username like :uploader ");
			
			ParameterBean paramBean=new ParameterBean("uploader","%"+uploader+"%",null);
			paramList.add(paramBean);
			map.put("uploader", uploader);
		}
        
		
		if(startDate!=null&&!"".equals(startDate) || endDate!=null&&!"".equals(endDate)) {
			if((startDate!=null&&!"".equals(startDate)) && (endDate!=null&&!"".equals(endDate))) {
				jpql.append(" AND music.createDate BETWEEN :startDate AND :endDate ");
				countJpql.append(" AND music.createDate BETWEEN :startDate AND :endDate ");
				
				ParameterBean paramBean = new ParameterBean("startDate",DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",startDate), TemporalType.DATE);
				ParameterBean paramBean2 = new ParameterBean("endDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",endDate), TemporalType.DATE);
				paramList.add(paramBean);
				paramList.add(paramBean2);
				
				map.put("startDate", startDate);
				map.put("endDate", endDate);
				
			}else if(startDate!=null&&!"".equals(startDate)) {
				jpql.append(" AND music.createDate >= :startDate ");
				countJpql.append(" AND music.createDate >= :startDate ");
				
				ParameterBean paramBean = new ParameterBean("startDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",startDate), TemporalType.DATE);
				paramList.add(paramBean);
				
				map.put("startDate", startDate);
			}else if(endDate!=null&&!"".equals(endDate)) {
				jpql.append(" AND music.createDate <= :endDate ");
				countJpql.append(" AND music.createDate <= :endDate ");
				
				ParameterBean paramBean = new ParameterBean("endDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",endDate), TemporalType.DATE);
				paramList.add(paramBean);
				
				map.put("endDate", endDate);
			}
		}
		
		
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("createDate",1,"music");
        orderByList.add(orderByObject);
        
        
        List musicList=musicService.getObjectByJpql(jpql, page, size, "music", paramList, null, orderByList);
        
        List mList=new ArrayList();
        
        for(int i=0;i<musicList.size();i++) {
        	Object[] obj=(Object[]) musicList.get(i);
        	Music music=(Music) obj[0];
        	User user=(User) obj[1];
        	music.setUploader(user.getUsername());
        	mList.add(music);
        }
        
       
        
        Long totalCount=musicService.getObjectCountByJpql(countJpql, paramList);
        
        Pageable pageable = new PageRequest(page == null ? 1 : page, size == null ? 10 : size,null);
        Page resultPage = new PageImpl(mList, pageable, totalCount);
        
        map.put("musicList", mList);
        map.put("page", page);
        map.put("size", size);
        map.put("pageinfo", resultPage);
        
        
        
        
		req.setAttribute("video_play_url_prefix", video_play_url_prefix);
		req.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		List<MusicType> types = musicTypeRepository.findAll();
		
		req.setAttribute("musicType", types);
		return "music/list";
	}*/

	@RequestMapping("musiclist")
	public String list(){
		/*List<ComboBoxData> musicTypeList = comboBoxService.queryMusicTypeComboBox();
		List<ComboBoxData> userList = comboBoxService.queryUserComboBox();
		request.setAttribute("musicTypeList",musicTypeList);
		request.setAttribute("userList",userList);*/
		return "music/list";
	}

	@RequestMapping("queryMusicList")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> queryMusicList(@ModelAttribute("queryPage") JQueryPage queryPage,
											 @RequestParam(value = "name",required = false) String name,
											 @RequestParam(value = "status",required = false) Integer status,
											 @RequestParam(value = "createPerson",required = false) Integer createPerson,
											 @RequestParam(value = "musicTypeId",required = false) Integer musicTypeId,
											 @RequestParam(value = "startDate",required = false) String startDate,
											 @RequestParam(value = "endDate",required = false) String endDate){
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("name",name);
		params.put("status",status);
		params.put("creatorId",createPerson);
		params.put("typeId",musicTypeId);
		params.put("startDate",startDate);
		params.put("endDate",endDate);
		List<MusicDisplay> list = musicService.queryMusics(params);
		com.busap.vcs.util.page.Page page = PagingContextHolder.getPage();
		map.put("rows",list);
		map.put("total",page.getTotalResult());
		return map;
	}

	@RequestMapping("add")
	public String add(@RequestParam MultipartFile files,@RequestParam MultipartFile faceFile,@ModelAttribute Music music) {
		if(music.getId()!=null){
			Music mtemp = this.musicService.find(music.getId());
			mtemp.setName(music.getName());
			mtemp.setDescription(music.getDescription());
			mtemp.setStatus(music.getStatus());
			mtemp.setTypeId(music.getTypeId());
			mtemp.setOrderNumber(music.getOrderNumber());
			if(files!=null && !files.isEmpty() && files.getSize()>0){
				uploadMusic(files,mtemp);
			}
			if(faceFile!=null && !faceFile.isEmpty() && faceFile.getSize()>0){
				uploadHeadPic(faceFile,mtemp);
			}
			this.musicService.save(mtemp);//修改
		}else {
			uploadHeadPic(faceFile,music);//上传模板与模板封面
			uploadMusic(files,music);//上传模板与模板封面
			music.setCreatorId(U.getUid());
			this.create(music);
		}
		return "redirect:/music/musiclist";
	}
	
	/**
	 * 上传音乐
	 * @param files
	 * @param music
	 */
	private void uploadMusic(@RequestParam MultipartFile files,Music music){
		String fileName = files.getOriginalFilename();
		String sufix = fileName.substring(fileName.lastIndexOf("."));
		String zipPath = File.separator + "music" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
		String zipFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss") + sufix;
		String zipUrl = "";
		try {
			File mFile = new File(basePath + zipPath, zipFilename);
			FileUtils.copyInputStreamToFile(files.getInputStream(), mFile);
			zipUrl = zipPath + zipFilename;
			logger.info(zipUrl);
			music.setSize(mFile.length());
			music.setUrl(zipUrl);//模板文件存储路径
		} catch (Exception e) {
			logger.error("文件[" + zipFilename + "]上传失败",e);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 上传音乐
	 * @param files
	 * @param music
	 */
	private void uploadHeadPic(@RequestParam MultipartFile files,Music music){
		if(files.isEmpty()){
			return;
		}
		String fileName = files.getOriginalFilename();
		String sufix = fileName.substring(fileName.lastIndexOf("."));
		String zipPath = File.separator + "music" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
		String zipFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss") + sufix;
		String zipUrl = "";
		try {
			File mFile = new File(basePath + zipPath, zipFilename);
			FileUtils.copyInputStreamToFile(files.getInputStream(), mFile);
			zipUrl = zipPath + zipFilename;
			logger.info(zipUrl);
			music.setFaceUrl(zipUrl);//模板文件存储路径
		} catch (Exception e) {
			logger.error("文件[" + zipFilename + "]上传失败",e);
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping("searchpagelist")
	@ResponseBody
	public Map<String,Object> searchListPage(Integer page, Integer rows, CRUDForm curdForm){
		if(page==0){
    		page=1;
    	}
    	Page pinfo=null;
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        List<Filter> filters = new ArrayList<Filter>();
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
        		}else if(entry.getKey().equals("title")){
        			if(!"All".equals(entry.getValue())){
        				params.put("title", entry.getValue());
        			}
        		}else if(entry.getKey().equals("stat")){
        			if(!"All".equals(entry.getValue())){
        				if("1".equals(entry.getValue())){
        					params.put("stat", entry.getValue());
        				}else{
        					params.put("stat", entry.getValue());
        				}
        			}
        		}
        	}
        }
        
        pinfo=this.musicService.listpage(page, rows, params);
        
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
        return jsonMap;
	}
	
	@RequestMapping("deleteMusic")
	@ResponseBody
	public ResultData deleteMusic(Long id){
		ResultData resultData = new ResultData();
		Music m = musicService.queryMusic(id);
		if(m != null){
			Music music = new Music();
			music.setId(id);
			music.setStatus(0);
			int result = musicService.updateMusic(music);
			if(result > 0){
				resultData.setResultCode("ok");
				resultData.setResultMessage("删除成功！");
			}else{
				resultData.setResultCode("error");
				resultData.setResultMessage("删除失败！");
			}
		}else{
			resultData.setResultCode("empty");
			resultData.setResultMessage("此音乐不存在！");
		}
		return resultData;
	}

	@RequestMapping("deleteMusics")
	@ResponseBody
	public ResultData deleteMusics(String ids){
		ResultData resultData = new ResultData();
		if(StringUtils.isBlank(ids)){
			resultData.setResultCode("empty");
			resultData.setResultMessage("请至少选择一行！");
			return resultData;
		}
		String[] idArray = ids.split(",");
		int result = musicService.updateStatusInMusicIds(idArray);
		if(result > 0){
			resultData.setResultCode("ok");
			resultData.setResultMessage("删除成功！");
		}else{
			resultData.setResultCode("error");
			resultData.setResultMessage("删除失败！");
		}
		return resultData;
	}

	@RequestMapping("queryMusic")
	@ResponseBody
	public ResultData queryMusic(Long id){
		ResultData resultData = new ResultData();
		Music music = musicService.queryMusic(id);
		if(music != null){
			resultData.setResultCode("ok");
			resultData.setResultMessage("成功！");
			resultData.setData(music);
		}else{
			resultData.setResultCode("empty");
			resultData.setResultMessage("数据为空！");
		}
		return resultData;
	}

	@RequestMapping("updateMusic")
	@ResponseBody
	public ResultData updateMusic(@ModelAttribute("musicFile") MultipartFile musicFile,@ModelAttribute("faceFile") MultipartFile faceFile,@ModelAttribute("music") Music music)throws IOException{
		ResultData resultData = new ResultData();
		if(!musicFile.isEmpty()){
			String musicFilePath = File.separator + "music" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
			String musicFileOriginalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + musicFile.getOriginalFilename();
			FileUtils.copyInputStreamToFile(faceFile.getInputStream(), new File(basePath + musicFilePath, musicFileOriginalFilename));
			String musicFileUrl = musicFilePath+musicFileOriginalFilename;
			music.setUrl(musicFileUrl);
			music.setSize(musicFile.getSize());
		}
		if(!faceFile.isEmpty()){
			String faceFilePath = File.separator + "music" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
			String faceFileOriginalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + faceFile.getOriginalFilename();
			FileUtils.copyInputStreamToFile(faceFile.getInputStream(), new File(basePath + faceFilePath, faceFileOriginalFilename));
			String faceFileUrl = faceFilePath+faceFileOriginalFilename;
			music.setFaceUrl(faceFileUrl);
		}
		music.setModifyDate(new Date());
		int result = musicService.updateMusic(music);
		if(result > 0){
			resultData.setResultCode("ok");
			resultData.setResultMessage("更新成功");
		}else{
			resultData.setResultCode("error");
			resultData.setResultMessage("更新失败");
		}
		return resultData;
	}

	@RequestMapping("insertMusic")
	@ResponseBody
	public ResultData insertMusic(@ModelAttribute("musicFile") MultipartFile musicFile,@ModelAttribute("faceFile") MultipartFile faceFile,@ModelAttribute("music") Music music)throws IOException{
		ResultData resultData = new ResultData();
		if(!musicFile.isEmpty()){
			String musicFilePath = File.separator + "music" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
			String musicFileOriginalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + musicFile.getOriginalFilename();
			FileUtils.copyInputStreamToFile(faceFile.getInputStream(), new File(basePath + musicFilePath, musicFileOriginalFilename));
			String musicFileUrl = musicFilePath+musicFileOriginalFilename;
			music.setUrl(musicFileUrl);
		}else{
			resultData.setResultCode("empty");
			resultData.setResultMessage("音乐不能为空！");
			return resultData;
		}
		if(!faceFile.isEmpty()){
			String faceFilePath = File.separator + "music" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
			String faceFileOriginalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + faceFile.getOriginalFilename();
			FileUtils.copyInputStreamToFile(faceFile.getInputStream(), new File(basePath + faceFilePath, faceFileOriginalFilename));
			String faceFileUrl = faceFilePath+faceFileOriginalFilename;
			music.setFaceUrl(faceFileUrl);
		}else{
			resultData.setResultCode("empty");
			resultData.setResultMessage("封面不能为空！");
			return resultData;
		}
		music.setSize(musicFile.getSize());
		music.setCreatorId(U.getUid());
		int result = musicService.insertMusic(music);
		if(result > 0){
			resultData.setResultCode("ok");
			resultData.setResultMessage("添加成功");
		}else{
			resultData.setResultCode("error");
			resultData.setResultMessage("添加失败");
		}
		return resultData;
	}

	/**
	 * 【删除文件和目录】
	 * @param filePath
	 */
	private void clearFiles(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			deleteFile(file);
		}
	}
	
	private void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
		}
		file.delete();
	}
	
	@RequestMapping("changeMusicStatus") 
	@ResponseBody
	public String changeMusicStatus(Long musicId,Integer status){ 
		
		Music music=musicService.find(musicId);
		if(music!=null) {
			music.setStatus(status);
			musicService.save(music);
		}
		return "ok";
	}
	
	@RequestMapping("findOne") 
	@ResponseBody
	public RespBody findMusic(Long musicId){ 
		Music music=musicService.find(musicId);
		if(music!=null){
			return this.respBodyWriter.toSuccess(music);
		}
		return this.respBodyWriter.toError("music not found.");
	}

	@RequestMapping("forwardMusicTypeList")
	public ModelAndView forwardMusicTypeList(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("music/query_music_type");
		return mav;
	}

	@RequestMapping("queryMusicTypeList")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> queryMusicTypeList(@ModelAttribute("queryPage") JQueryPage queryPage){
		List<MusicType> list = musicService.selectMusicType();
		com.busap.vcs.util.page.Page page = PagingContextHolder.getPage();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", page.getTotalResult());
		resultMap.put("rows", list);
		return resultMap;
	}

	@RequestMapping("insertMusicType")
	@ResponseBody
	public ResultData insertMusicType(String name){
		MusicType mt = musicService.selectMusicTypeByName(name);
		ResultData resultData = new ResultData();
		if(mt != null){
			resultData.setResultCode("fail");
			resultData.setResultMessage("该名称已存在，请勿重复添加！");
			return resultData;
		}
		MusicType musicType = new MusicType();
		musicType.setTypeName(name);
		musicType.setCreatorId(U.getUid());
		musicType.setDataFrom(DataFrom.移动麦视后台.getName());
		int ret = musicService.insertMusicType(musicType);
		if (ret > 0) {
			resultData.setResultCode("success");
			resultData.setResultMessage("添加成功");
		} else {
			resultData.setResultCode("fail");
			resultData.setResultMessage("添加失败");
		}
		return resultData;
	}

	@RequestMapping("deleteMusicType")
	@ResponseBody
	public ResultData deleteMusicType(Long id){
		ResultData resultData = new ResultData();
		int ret = musicService.deleteMusicType(id);
		if (ret > 0) {
			resultData.setResultCode("success");
			resultData.setResultMessage("删除成功");
		} else {
			resultData.setResultCode("fail");
			resultData.setResultMessage("删除失败");
		}
		return resultData;
	}

	@RequestMapping("updateMusicTypeTemplate")
	public ModelAndView updateMusicTypeTemplate(Long id) {
		ModelAndView mav = new ModelAndView();
		MusicType musicType = musicService.selectMusicTypeByPrimaryKey(id);
		mav.addObject("musicType", musicType);
		mav.setViewName("music/update_music_type");
		return mav;
	}

	//更新音乐类型信息
	@RequestMapping("updateMusicType")
	@ResponseBody
	public ResultData updateMusicType(@ModelAttribute("musicType") @Valid MusicType musicType, BindingResult results) {
		ResultData resultData = new ResultData();
		int ret = musicService.updateMusicType(musicType);
		if (ret > 0) {
			resultData.setResultCode("success");
			resultData.setResultMessage("更新成功");
		} else {
			resultData.setResultCode("fail");
			resultData.setResultMessage("更新失败");
		}
		return resultData;
	}

}
