package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.data.enums.TaskTypeSecondEnum;
import com.busap.vcs.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.base.Action;
import com.busap.vcs.base.Filter;
import com.busap.vcs.base.Message;
import com.busap.vcs.base.Module;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Evaluation;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.enums.VideoStatus;
import com.busap.vcs.service.kafka.producer.KafkaProducer;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
 
@Controller
@RequestMapping("/evaluation")
public class EvaluationController extends CRUDController<Evaluation, Long> {

    private Logger logger = LoggerFactory.getLogger(EvaluationController.class);

    @Resource(name="evaluationService")
    EvaluationService evaluationService; 
    
    @Resource(name="videoService") 
    VideoService videoService;

	@Resource(name="integralService")
	IntegralService integralService;

    @Resource(name="sensitiveWordService") 
    SensitiveWordService sensitiveWordService;
    
    @Autowired
    KafkaProducer kafkaProducer;
    
    @Resource(name="ruserService")
	private RuserService ruserService;

    @Resource(name="evaluationService")
    @Override
    public void setBaseService(BaseService<Evaluation, Long> baseService) {
        this.baseService = baseService;
    } 
    
//    @RequestMapping("/findVideoEvaluationList")
//    @ResponseBody
//    public RespBody findVideoEvaluationList(Integer page, Integer size, String videoId){
//    	Video v = videoService.find(Long.parseLong(videoId)); 
//		if(v==null||VideoStatus.已删除.getName().equals(v.getFlowStat())){
//			return this.respBodyWriter.toError(ResponseCode.CODE_332.toString(), ResponseCode.CODE_332.toCode());
//		}
//    	List<Filter> lsf = new ArrayList<Filter>(1);
//    	lsf.add(new Filter("videoId",videoId)); 
//    	return this.listPage(page<=0?1:page, size<=0?10:size, lsf, null); 
//    } 
    
    @RequestMapping("/findEvaluationList")
    @ResponseBody
    public RespBody findEvaluationList(int flag, Integer startId, Integer count, String videoId){
    	Video v = videoService.find(Long.parseLong(videoId)); 
		if(v==null||VideoStatus.已删除.getName().equals(v.getFlowStat())){
			return this.respBodyWriter.toError(ResponseCode.CODE_332.toString(), ResponseCode.CODE_332.toCode());
		}
    	List<Filter> filters = new ArrayList<Filter>(1);
    	filters.add(new Filter("videoId",videoId)); 
    	if(flag==1){//flag==0表示最新，flag==1表示从startId起的老数据
        	filters.add(Filter.lt("id", startId));  
    	}
    	Sort s = new Sort(Sort.Direction.DESC,"id");  
    	return this.list(0, count, filters, s); 
    } 
    
    
    @RequestMapping("/findEvaluationListpage")
    @ResponseBody
    public RespBody findEvaluationListPage(int flag, Integer start, Integer count, String videoId){
    	Video v = videoService.find(Long.parseLong(videoId)); 
		if(v==null||VideoStatus.已删除.getName().equals(v.getFlowStat())){
			return this.respBodyWriter.toError(ResponseCode.CODE_332.toString(), ResponseCode.CODE_332.toCode());
		}
    	List<Filter> filters = new ArrayList<Filter>(1);
    	filters.add(new Filter("videoId",videoId)); 
    	Sort s = new Sort(Sort.Direction.DESC,"id");  
    	return this.list(start, count, filters, s); 
    } 
    
    @RequestMapping("/findMyEvaluationList")
    @ResponseBody
    public RespBody findMyEvaluationList(int flag, Integer startId, Integer count){
    	String uid = (String)this.request.getHeader("uid");
        	logger.info("findMyEvaluationList,uid={}",uid);
    	
    	List idList = videoService.findMyVideoIdList(Long.valueOf(uid));
    	String videoIds = "";
    	if (idList != null && idList.size()>0){
    		for (int i=0;i<idList.size();i++){
        		videoIds += String.valueOf(idList.get(i))+",";
        	}
    	}
    	
    	if (!"".equals(videoIds)){
    		videoIds = videoIds.substring(0,videoIds.length()-1);
    	} else {  //没有视频，评论列表返回空
    		return this.respBodyWriter.toSuccess(null); 
    	}
    	logger.info("videoIds={}",videoIds);
    	
    	List<Filter> filters = new ArrayList<Filter>(1);
    	filters.add(new Filter("videoIdIn",videoIds)); 
    	if(flag==1){//flag==0表示最新，flag==1表示从startId起的老数据
    		filters.add(Filter.lt("id", startId));  
    	}
    	Sort s = new Sort(Sort.Direction.DESC,"id");  
    	List<Evaluation> list = this.listAndNotReturn(0, count, filters, s);
    	List<Evaluation> destList = new ArrayList<Evaluation>();
    	for(Evaluation e:list){
    		Video video = videoService.getVideo(e.getVideoId(), uid);
    		if (video != null) {
    			e.setVideo(video);
        		destList.add(e);
    		}
    	}
    	return this.respBodyWriter.toSuccess(destList); 
    } 
    
    @RequestMapping("/saveEvaluation")
    @ResponseBody
    public RespBody saveEvaluation(Evaluation e){
    	String uid = (String)this.request.getHeader("uid");
    	Ruser ruser = ruserService.find(videoService.find(e.getVideoId()).getCreatorId());
    	if (ruser != null && ruser.getAllowEvaluation() == 0){ //判断用户是否已经关闭评论
    		return this.respBodyWriter.toError(ResponseCode.CODE_315.toString(), ResponseCode.CODE_315.toCode());
    	}
		// TODO 判断用户是否被加入当前视频用户的黑名单，如加入则禁止评论
		if(baseService.isExistBlackList(String.valueOf(ruser.getId()),uid)){
			return this.respBodyWriter.toError(ResponseCode.CODE_611.toString(), ResponseCode.CODE_611.toCode());
		}
    	e.setCreatorId(Long.parseLong(uid));
    	e.setDataFrom(DataFrom.麦视rest接口.getName()); 
    	sensitiveWordService.checkAndReplaceSensitiveWord(e);
    	evaluationService.saveEvaluation(e);
    	
    	String pid = "";
    	if (e.getPid() != null) {
			pid = String.valueOf(e.getPid());
		} else {
			pid = String.valueOf(videoService.getVideo(e.getVideoId(), null).getCreatorId());
		}
    	
    	if (!uid.equals(pid)) {
    		//将评论加入消息队列
        	Message msg = new Message();
    		msg.setModule(Module.COMMENT);
    		msg.setAction(Action.INSERT);
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("uid", uid);
    		map.put("pid", pid);
    		map.put("vid", String.valueOf(e.getVideoId()));
    		msg.setDataMap(map);
    		kafkaProducer.send("push-msg-topic", msg);
    	}
    	e.setUser(ruserService.find(Long.parseLong(uid)));
		try {
			integralService.getIntegralFromDaily(Long.parseLong(uid), String.valueOf(e.getVideoId()), TaskTypeSecondEnum.commentVideo);
		} catch (Exception ex) {
			logger.info("getIntegralFromDaily error: uid=" + uid + " & vid=" + String.valueOf(e.getVideoId()));
			ex.printStackTrace();
		}

    	return this.respBodyWriter.toSuccess(e);
    } 
    
    @RequestMapping("/deleteEvaluation")
    @ResponseBody
    public RespBody deleteEvaluation(Long id){
    	String uid = (String)this.request.getHeader("uid"); 
    	evaluationService.deleteEvaluation(id);
    	return this.respBodyWriter.toSuccess("ok");
    } 
    
    @RequestMapping("/allowEvaluation")
    @ResponseBody
    public RespBody allowEvaluation(Integer isAllow){
    	try {
    		String uid = this.request.getHeader("uid");
    		if (uid != null && !"".equals(uid)) {
    			ruserService.allowEvaluation(isAllow, Long.parseLong(uid));
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return respBodyWriter.toSuccess();
    }
}

