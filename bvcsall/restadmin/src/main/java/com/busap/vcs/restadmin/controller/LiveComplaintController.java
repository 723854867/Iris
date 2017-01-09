package com.busap.vcs.restadmin.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.base.WsMessage;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.BaseEntity;
import com.busap.vcs.data.entity.LiveComplaint;
import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.repository.LiveComplaintRepository;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LiveComplaintService;
import com.busap.vcs.service.RoomService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.impl.SolrUserService;
import com.busap.vcs.service.kafka.producer.WsMessageProducer;
import com.busap.vcs.service.utils.HttpPostUtils;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.controller.CRUDForm;

/**
 * 视频
 * 
 * @author zx
 * 
 */
@Controller()
@RequestMapping("liveComplaint")
public class LiveComplaintController extends CRUDController<LiveComplaint, Long> {

	private static final Logger logger = LoggerFactory
			.getLogger(LiveComplaintController.class);

	@Value("${uploadfile_remote_url}")
	private String uploadfile_remote_url;
	
	@Value("${video_play_url_prefix}")
	private String video_play_url_prefix;
	
	@Value("${uploadpic_url_prefix}")
	private String uploadpic_url_prefix;
		
	@Resource(name="wsMessageProducer")
	private WsMessageProducer wsMessageProducer;
	
	@Resource(name = "liveComplaintService")
	private LiveComplaintService liveComplaintService;
	
	@Resource(name = "liveComplaintRepository")
	private LiveComplaintRepository liveComplaintRepository;
	
	//key--未处理直播投诉 in redis 
	public static final String LC_CHECK = "LC_CHECK";

	@Resource(name = "liveComplaintService")
	@Override
	public void setBaseService(BaseService<LiveComplaint, Long> baseService) {
		this.baseService = baseService;
	}
	
	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name = "roomService")
	private RoomService roomService;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name = "solrUserService")
	private SolrUserService solrUserService;

	@RequestMapping("liveComplaintlist")
	public String list(HttpServletRequest req) {
		req.setAttribute("video_play_url_prefix", video_play_url_prefix);
		req.setAttribute("uploadpic_url_prefix", uploadpic_url_prefix);
		return "liveComplaint/list";
	}
	
	@RequestMapping(value = {"/updatepage"}, method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RespBody updatepage(LiveComplaint entity,HttpServletRequest req) {
    	if (!validator(entity, BaseEntity.Update.class)) {
            return respBodyWriter.toError(entity);
        }
    	LiveComplaint dbEntity=this.liveComplaintService.find(entity.getId());
    	
    	String[] ps=new String[]{"title","content"};
    	try {
	    	for (String s : ps) {
				BeanUtils.setProperty(dbEntity, s, BeanUtils.getProperty(entity, s));;
			}
    	} catch (Exception e) {
    		logger.error("copy properties error",e);
    		return this.respBodyWriter.toError(e);
    	}
    	
        baseService.update(dbEntity);
        return respBodyWriter.toSuccess(dbEntity);
    }
	
	@RequestMapping("searchpagelist")
	@ResponseBody
	public Map<String,Object> searchListPage(Integer page, Integer rows,
			@RequestParam(value = "stat", required = false)  Integer stat,
			@RequestParam(value = "content", required = false)  String content,
							CRUDForm curdForm) throws Exception{
		if(page==null) {
			page=1;
        }
        if(rows==null) {
        	rows=20;
        }
        
        StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("FROM LiveComplaint lc where 1=1 ");
        countJpql.append("SELECT COUNT(*) FROM LiveComplaint lc where 1=1  ");
        
        if(stat!=null) {
       	 	jpql.append(" AND lc.stat=:stat ");
            countJpql.append(" AND  lc.stat=:stat  ");
            ParameterBean paramBean=new ParameterBean("stat",stat,null);
			paramList.add(paramBean);
       }else {
    	    jpql.append(" AND  lc.stat=0 ");
            countJpql.append(" AND  lc.stat=0  ");
       }
        
        if(content!=null&&!content.equals("")) {
        	 jpql.append(" AND  lc.content= :content ");
             countJpql.append(" AND  lc.content= :content ");
             ParameterBean paramBean=new ParameterBean("content",content,null);
 			 paramList.add(paramBean);
        }
        
//        if(keyword!=null&& !keyword.equals("")) {
//			jpql.append(" AND video.name like :name ");
//			countJpql.append(" AND video.name like :name ");
//			
//			ParameterBean paramBean=new ParameterBean("name","%"+keyword+"%",null);
//			paramList.add(paramBean);
//			this.request.setAttribute("keyword", keyword);
//		}
        
        
		
		
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
		
//		OrderByBean orderByObject1=new OrderByBean("typeOne",0,"lc");
//        orderByList.add(orderByObject1);
        
        
        
        
        List taskList=liveComplaintService.getObjectByJpql(jpql, page, rows, "lc", paramList, null, orderByList);
        
        
       
        
        Long totalCount=liveComplaintService.getObjectCountByJpql(countJpql, paramList);
        
        
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("total",totalCount);
		map.put("rows",taskList);
		return map;
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
		liveComplaintRepository.deleteAllComplaints(idList);
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
		liveComplaintRepository.cancleAllComplaints(idList);
		return reps;
	}
	
	
	/**
	 * 处理投诉
	 * @param lCId 投诉ID
	 * @param type 操作类型
	 * @return
	 */
	@RequestMapping("dealComplaints")
	@ResponseBody
	public String dealComplaints(Long lCId,Integer type){
		
		LiveComplaint lc=liveComplaintService.find(lCId);
				
		if(type!=null) {
			Room room=roomService.find(lc.getLiveId());
			String message = "直播内容不合法，强制关闭.";
			if(type.intValue()==2) {//下线
				roomService.offlineRoom(room.getId(),U.getUid(),message);
			}else if(type.intValue()==3) {//封号
				//下线
				roomService.offlineRoom(room.getId(),U.getUid(),message);
				//禁用账号
				ruserService.closeAccount(room.getCreatorId());
			}
		}
		
		liveComplaintRepository.dealComplaints(lc.getLiveId());
		Long unDealLCCount=liveComplaintRepository.unDealComplaintsCount();
		jedisService.set(LC_CHECK, unDealLCCount.toString());
		return "ok";
	}

}
