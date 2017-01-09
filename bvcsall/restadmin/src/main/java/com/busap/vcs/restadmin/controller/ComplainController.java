package com.busap.vcs.restadmin.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.entity.BaseEntity;
import com.busap.vcs.data.entity.Complain;
import com.busap.vcs.data.repository.ComplainRepository;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.ComplainService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.controller.CRUDForm;

/**
 * 视频
 * 
 * @author meizhiwen
 * 
 */
@Controller()
@RequestMapping("complain")
public class ComplainController extends CRUDController<Complain, Long> {

	private static final Logger logger = LoggerFactory
			.getLogger(ComplainController.class);

	@Value("${uploadfile_remote_url}")
	private String uploadfile_remote_url;
	
	@Value("${video_play_url_prefix}")
	private String video_play_url_prefix;
	
	@Value("${uploadpic_url_prefix}")
	private String uploadpic_url_prefix;
	
	@Resource(name = "complainService")
	private ComplainService complainService;
	
	@Resource(name = "complainRepository")
	private ComplainRepository complainRepository;

	@Resource(name = "complainService")
	@Override
	public void setBaseService(BaseService<Complain, Long> baseService) {
		this.baseService = baseService;
	}

	@RequestMapping("complainlist")
	public String list(HttpServletRequest req) {
		req.setAttribute("video_play_url_prefix", video_play_url_prefix);
		req.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		return "complain/list";
	}
	
	@RequestMapping(value = {"/updatepage"}, method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RespBody updatepage(Complain entity,HttpServletRequest req) {
    	if (!validator(entity, BaseEntity.Update.class)) {
            return respBodyWriter.toError(entity);
        }
    	Complain dbEntity=this.complainService.find(entity.getId());
    	
    	String[] ps=new String[]{"title","content"};
    	try {
	    	for (String s : ps) {
				BeanUtils.setProperty(dbEntity, s, BeanUtils.getProperty(entity, s));;
			}
    	} catch (Exception e) {
    		this.logger.error("copy properties error",e);
    		return this.respBodyWriter.toError(e);
    	}
    	
        baseService.update(dbEntity);
        return respBodyWriter.toSuccess(dbEntity);
    }
	
	@RequestMapping("searchpagelist")
	@ResponseBody
	public Map<String,Object> searchListPage(Integer page, Integer rows, CRUDForm curdForm){
		if(page==0){
    		page=1;
    	}
    	Page pinfo=null;
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        List<Filter> filters = new ArrayList<Filter>();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("pageStart", (page-1)*rows);
        params.put("pageSize", rows);
		params.put("stat", "0");
        for (Map.Entry<String, String> entry : curdForm.getFilters().entrySet()) {
			System.out.println(entry.getKey());
			System.out.println("============================");
			System.out.println(entry.getValue());
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
        			}else{
						params.remove("stat");
					}
        		}
        	}
        }
        pinfo=this.complainService.listpage(page, rows, params);
        
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
        return jsonMap;
	}
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("logicremove")
	@ResponseBody
	public RespBody multiDelete(String ids){
		String id[] = ids.split(",");
		List<Long> idList = new ArrayList<Long>();
		if(id.length > 0){
			for(String evaId:id){
				idList.add(Long.parseLong(evaId));
			}
		}
		RespBody reps = new RespBody(Boolean.TRUE, RespBody.MESSAGE_OK, ids);
		complainRepository.deleteAllComplains(idList);
		return reps;
	}
	
	/**
	 * 批量取消投诉
	 * @param ids
	 * @return
	 */
	@RequestMapping("logiccancle")
	@ResponseBody
	public RespBody multiCancle(String ids){
		String id[] = ids.split(",");
		List<Long> idList = new ArrayList<Long>();
		if(id.length > 0){
			for(String evaId:id){
				idList.add(Long.parseLong(evaId));
			}
		}
		RespBody reps = new RespBody(Boolean.TRUE, RespBody.MESSAGE_OK, ids);
		complainRepository.cancleAllComplains(idList);
		return reps;
	}

}
