package com.busap.vcs.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.data.enums.TaskTypeSecondEnum;
import com.busap.vcs.service.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.base.Action;
import com.busap.vcs.base.Filter;
import com.busap.vcs.base.Message;
import com.busap.vcs.base.Module;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Praise;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Sign;
import com.busap.vcs.data.entity.SignUser;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.enums.VideoStatus;
import com.busap.vcs.data.vo.SignVO;
import com.busap.vcs.service.kafka.producer.KafkaProducer;
import com.busap.vcs.util.DateFormatUtils;
import com.busap.vcs.util.DateUtils;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
 
@Controller
@RequestMapping("/praise")
public class PraiseController extends CRUDController<Praise, Long> {

    private Logger logger = LoggerFactory.getLogger(PraiseController.class);

    @Resource(name="praiseService")
    PraiseService praiseService; 
    
    @Resource(name="videoService") 
    VideoService videoService;
    
    @Resource(name="signService")
	private SignService signService;
    
    @Resource(name="signUserService")
	private SignUserService signUserService;
    
    @Resource(name="ruserService")
	private RuserService ruserService;
    
    @Resource(name="jedisService")
	private JedisService jedisService;
    
    @Resource(name="attentionService")
	private AttentionService attentionService;

	@Resource(name="integralService")
	IntegralService integralService;

	@Autowired
    KafkaProducer kafkaProducer;

    @Resource(name="praiseService")
    @Override
    public void setBaseService(BaseService<Praise, Long> baseService) {
        this.baseService = baseService;
    }  
    
    @RequestMapping("/savePraise")
    @ResponseBody
    public RespBody savePraise(Praise p){ 
    	Video v = videoService.find(p.getVideoId()); 
		if(v==null||VideoStatus.已删除.getName().equals(v.getFlowStat())){
			return this.respBodyWriter.toError(ResponseCode.CODE_332.toString(), ResponseCode.CODE_332.toCode());
		}

    	String uid = (String)this.request.getHeader("uid");

		//TODO 判断用户是否被拉黑，是的话禁止点赞
		if(baseService.isExistBlackList(String.valueOf(v.getCreatorId()),uid)){
			return this.respBodyWriter.toError(ResponseCode.CODE_612.toString(), ResponseCode.CODE_612.toCode());
		}

    	boolean f = videoService.praised(Long.parseLong(uid),p.getVideoId()); 
    	if(!f){ 
	    	p.setCreatorId(Long.parseLong(uid));
	    	p.setDataFrom(DataFrom.麦视rest接口.getName());
	    	try{
		    	praiseService.savePraise(p);
		    	Message msg = new Message();
				msg.setModule(Module.PRAISE);
				msg.setAction(Action.INSERT);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("uid", uid);
				map.put("vid", String.valueOf(p.getVideoId()));
				map.put("pid", String.valueOf(videoService.getVideo(p.getVideoId(), null).getCreatorId()));
				msg.setDataMap(map);
		    	kafkaProducer.send("push-msg-topic", msg);
			}catch(DataIntegrityViolationException e){
				//同一视频重复赞，即便爆出错误，也算赞成功，数据库有唯一键限制， 所以在此捕获错误。
				return this.respBodyWriter.toError(ResponseCode.CODE_201.toString(), ResponseCode.CODE_201.toCode());
			}
			try {
				integralService.getIntegralFromDaily(Long.parseLong(uid), String.valueOf(p.getVideoId()), TaskTypeSecondEnum.praiseVideo);
			} catch (Exception e) {
				logger.error("getIntegralFromDaily : praise error! uid=" + uid + " & vid=" + p.getVideoId());
				e.printStackTrace();
			}
    	}else{
    		return this.respBodyWriter.toError(ResponseCode.CODE_201.toString(), ResponseCode.CODE_201.toCode());
    	}
    	return this.respBodyWriter.toSuccess(); 
    } 
    
    
    @RequestMapping("/sharePraise")
    @ResponseBody
    public RespBody sharePraise(Praise p){ 
    	Video v = videoService.find(p.getVideoId()); 
		if(v==null||VideoStatus.已删除.getName().equals(v.getFlowStat())){
			return this.respBodyWriter.toError(ResponseCode.CODE_332.toString(), ResponseCode.CODE_332.toCode());
		} 
    	String uid = (String)this.request.getHeader("uid"); 
    	String invitedUid = (String)this.request.getParameter("invitedUid");
    	Sign sign=new Sign();
    	Sign invitedSign=new Sign();
		Date nowdate=new Date();
		SimpleDateFormat  sf=new SimpleDateFormat ("yyyy-MM-dd");
		sign.setCreateDate(nowdate);
		sign.setDateMark(sf.format(nowdate));
		invitedSign.setCreateDate(nowdate);
		invitedSign.setDateMark(sf.format(nowdate));
    	boolean f = videoService.praised(Long.parseLong(uid),p.getVideoId()); 
    	if(!f){ 
	    	p.setCreatorId(Long.parseLong(uid));
	    	p.setDataFrom(DataFrom.麦视rest接口.getName());
	    	try{
		    	praiseService.savePraise(p);
		    	Message msg = new Message();
				msg.setModule(Module.PRAISE);
				msg.setAction(Action.INSERT);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("uid", uid);
				map.put("vid", String.valueOf(p.getVideoId()));
				map.put("pid", String.valueOf(videoService.getVideo(p.getVideoId(), null).getCreatorId()));
				if(StringUtils.isNotBlank(invitedUid)){
				map.put("invitedUid",invitedUid);
				}
				msg.setDataMap(map);
		    	kafkaProducer.send("push-msg-topic", msg);
			}catch(DataIntegrityViolationException e){
				//同一视频重复赞，即便爆出错误，也算赞成功，数据库有唯一键限制， 所以在此捕获错误。
				return this.respBodyWriter.toError(ResponseCode.CODE_201.toString(), ResponseCode.CODE_201.toCode());
			}
/****** 修改到注册页面	******/    	
	    	 jedisService.set(BicycleConstants.NEW_USER_FLAG+uid,"true");
			 jedisService.expireAt(BicycleConstants.NEW_USER_FLAG+uid,DateUtils.getNowUnixYMDAsStr());	
/******  end     *********/
	    	//分享点赞赠送积分100分
	    	String flag = jedisService.get(BicycleConstants.NEW_USER_FLAG+uid);
	    	if("true".equals(flag)&&StringUtils.isNotBlank(invitedUid)){
	    		
	    		if(!checkAndAddSign(uid)){
	    			return this.respBodyWriter.toError(ResponseCode.CODE_601.toString(), ResponseCode.CODE_601.toCode());
	    		}
	    		sign.setDataFrom(BicycleConstants.FEN_XIANG_GET_STR);
				sign.setType(BicycleConstants.FEN_XIANG_GET);
				sign.setSignNum(BicycleConstants.FEN_XIANG_GET_SIGN);
				sign.setCreatorId(Long.parseLong(uid));
				signService.save(sign);
				
				//保存被邀请人总的积分数
				List<SignUser> sulist=signUserService.findSignUserByuid(uid);
				SignUser su=new SignUser();
				if(sulist!=null&&sulist.size()>0){
					su=sulist.get(0);
					su.setModifyDate(nowdate);
					su.setContinueSign(sign.getContinueSign());
					su.setSumSignNum(sign.getSignNum()+su.getSumSignNum());
					su.setCreatorId(Long.parseLong(uid));
					su.setDataFrom(sign.getDataFrom());
					signUserService.update(su);
				}else{
					su.setCreateDate(nowdate);
					su.setContinueSign(sign.getContinueSign());
					su.setSumSignNum(sign.getSignNum());
					su.setCreatorId(Long.parseLong(uid));
					su.setDataFrom(sign.getDataFrom());
					signUserService.save(su);
				}
				Ruser ru = ruserService.find(Long.parseLong(uid));
				if(ru!=null){
					ru.setSignSum(su.getSumSignNum());
					ruserService.update(ru);
					jedisService.saveAsMap(BicycleConstants.USER_INFO+ru.getId(), ru);
				}
				
				if(!checkAndAddSign(invitedUid)){
	    			return this.respBodyWriter.toError(ResponseCode.CODE_601.toString(), ResponseCode.CODE_601.toCode());
	    		}
				
				invitedSign.setDataFrom(BicycleConstants.FEN_XIANG_GET_STR);
				invitedSign.setType(BicycleConstants.FEN_XIANG_GET);
				invitedSign.setSignNum(BicycleConstants.FEN_XIANG_GET_SIGN);
				invitedSign.setCreatorId(Long.parseLong(invitedUid));
				signService.save(invitedSign);
				
				//保存邀请人总的积分数
				List<SignUser> invitedlist=signUserService.findSignUserByuid(uid);
				SignUser suinvited=new SignUser();
				if(invitedlist!=null&&invitedlist.size()>0){
					suinvited=invitedlist.get(0);
					suinvited.setModifyDate(nowdate);
					suinvited.setContinueSign(sign.getContinueSign());
					suinvited.setSumSignNum(sign.getSignNum()+su.getSumSignNum());
					suinvited.setCreatorId(Long.parseLong(uid));
					suinvited.setDataFrom(sign.getDataFrom());
					signUserService.update(suinvited);
				}else{
					suinvited.setCreateDate(nowdate);
					suinvited.setContinueSign(sign.getContinueSign());
					suinvited.setSumSignNum(sign.getSignNum());
					suinvited.setCreatorId(Long.parseLong(uid));
					suinvited.setDataFrom(sign.getDataFrom());
					signUserService.save(suinvited);
				}
				Ruser ruinvited = ruserService.find(Long.parseLong(invitedUid));
				if(ruinvited!=null){
					ruinvited.setSignSum(suinvited.getSumSignNum());
					ruserService.update(ruinvited);
					jedisService.saveAsMap(BicycleConstants.USER_INFO+ruinvited.getId(), ruinvited);
				}
	    	}
    	}else{
    		return this.respBodyWriter.toError(ResponseCode.CODE_201.toString(), ResponseCode.CODE_201.toCode());
    	}
    	return this.respBodyWriter.toSuccess(); 
    } 
    
	   @RequestMapping("/saveSharePraise")
    @ResponseBody
    public RespBody saveSharePraise(Praise p,String shareUid){ 
    	Video v = videoService.find(p.getVideoId()); 
		if(v==null||VideoStatus.已删除.getName().equals(v.getFlowStat())){
			return this.respBodyWriter.toError(ResponseCode.CODE_332.toString(), ResponseCode.CODE_332.toCode());
		} 
    	String uid = (String)this.request.getHeader("uid"); 
    	Sign sign=new Sign();
    	Sign shareSign=new Sign();
		Date nowdate=new Date();
		SimpleDateFormat  sf=new SimpleDateFormat ("yyyy-MM-dd");
		sign.setCreateDate(nowdate);
		sign.setDateMark(sf.format(nowdate));
		shareSign.setCreateDate(nowdate);
		shareSign.setDateMark(sf.format(nowdate));
    	boolean f = videoService.praised(Long.parseLong(uid),p.getVideoId()); 
    	if(!f){ 
	    	p.setCreatorId(Long.parseLong(uid));
	    	p.setDataFrom(DataFrom.麦视rest接口.getName());
	    	try{
		    	praiseService.savePraise(p);
		    	Message msg = new Message();
				msg.setModule(Module.PRAISE);
				msg.setAction(Action.INSERT);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("uid", uid);
				map.put("vid", String.valueOf(p.getVideoId()));
				map.put("pid", String.valueOf(videoService.getVideo(p.getVideoId(), null).getCreatorId()));
				if(StringUtils.isNotBlank(shareUid)&&!shareUid.equals(uid)){
					map.put("shareUid",shareUid);
				}
		   	//分享点赞赠送积分100分
	    	if(StringUtils.isNotBlank(shareUid)&&!shareUid.equals(uid)){
	    		//如果不是新用户，分享的不是新都不加积分
	    		if(!isNewUser(uid)||v.getCreatorId()-Long.parseLong(shareUid)!=0){
	    			msg.setDataMap(map);
	    	    	kafkaProducer.send("push-msg-topic", msg);
	    			return this.respBodyWriter.toSuccess(); 
	    		}
	    		//当天积分获取量不能超过5000
	    		if(checkAndAddSign1(uid)&&checkPraiseUser(uid,v.getId().toString(),shareUid)){
	    			sign.setDataFrom(BicycleConstants.FEN_XIANG_GET_STR);
	    			sign.setType(BicycleConstants.FEN_XIANG_GET);
	    			sign.setSignNum(BicycleConstants.FEN_XIANG_GET_SIGN);
	    			sign.setCreatorId(Long.parseLong(uid));
	    			sign.setVideoId(v.getId());
	    			sign.setFromUid(Long.parseLong(shareUid));
	    			signService.save(sign);
	    			map.put("checkuid", "true");
	    			//保存被邀请人总的积分数
	    			List<SignUser> sulist=signUserService.findSignUserByuid(uid);
	    			SignUser su=new SignUser();
	    			if(sulist!=null&&sulist.size()>0){
	    				su=sulist.get(0);
	    				su.setModifyDate(nowdate);
	    				//su.setContinueSign(sign.getContinueSign());
	    				su.setSumSignNum(sign.getSignNum()+su.getSumSignNum());
	    				su.setCreatorId(Long.parseLong(uid));
	    				su.setDataFrom(sign.getDataFrom());
	    				signUserService.update(su);
	    			}else{
	    				su.setCreateDate(nowdate);
	    				//su.setContinueSign(sign.getContinueSign());
	    				su.setSumSignNum(sign.getSignNum());
	    				su.setCreatorId(Long.parseLong(uid));
	    				su.setDataFrom(sign.getDataFrom());
	    				signUserService.save(su);
	    			}
	    			Ruser ru = ruserService.find(Long.parseLong(uid));
	    			if(ru!=null){
	    				ru.setSignSum(su.getSumSignNum());
	    				ruserService.update(ru);
	    				jedisService.saveAsMap(BicycleConstants.USER_INFO+ru.getId(), ru);
	    			}
	    		}
	    		if(checkAndAddSign1(shareUid)&&checkShareUser(shareUid,uid,v.getId().toString())){
	    			shareSign.setDataFrom(BicycleConstants.FEN_XIANG_GET_STR);
	    			shareSign.setType(BicycleConstants.FEN_XIANG_GET);
	    			shareSign.setSignNum(BicycleConstants.FEN_XIANG_GET_SIGN);
	    			shareSign.setCreatorId(Long.parseLong(shareUid));
	    			shareSign.setVideoId(v.getId());
	    			shareSign.setFromUid(Long.parseLong(uid));
	    			signService.save(shareSign);
	    			map.put("checkshareuid", "true");
	    			//保存邀请人总的积分数
	    			List<SignUser> invitedlist=signUserService.findSignUserByuid(shareUid);
	    			SignUser suinvited=new SignUser();
	    			if(invitedlist!=null&&invitedlist.size()>0){
	    				suinvited=invitedlist.get(0);
	    				suinvited.setModifyDate(nowdate);
	    				//suinvited.setContinueSign(shareSign.getContinueSign());
	    				suinvited.setSumSignNum(shareSign.getSignNum()+suinvited.getSumSignNum());
	    				suinvited.setCreatorId(Long.parseLong(shareUid));
	    				suinvited.setDataFrom(shareSign.getDataFrom());
	    				signUserService.update(suinvited);
	    			}else{
	    				suinvited.setCreateDate(nowdate);
	    				//suinvited.setContinueSign(shareSign.getContinueSign());
	    				suinvited.setSumSignNum(shareSign.getSignNum());
	    				suinvited.setCreatorId(Long.parseLong(shareUid));
	    				suinvited.setDataFrom(shareSign.getDataFrom());
	    				signUserService.save(suinvited);
	    			}
	    			Ruser ruinvited = ruserService.find(Long.parseLong(shareUid));
	    			if(ruinvited!=null){
	    				ruinvited.setSignSum(suinvited.getSumSignNum());
	    				ruserService.update(ruinvited);
	    				jedisService.saveAsMap(BicycleConstants.USER_INFO+ruinvited.getId(), ruinvited);
	    			}
	    		}
	    	}
			msg.setDataMap(map);
	    	kafkaProducer.send("push-msg-topic", msg);

	    	}catch(DataIntegrityViolationException e){
	    		//同一视频重复赞，即便爆出错误，也算赞成功，数据库有唯一键限制， 所以在此捕获错误。
	    		return this.respBodyWriter.toError(ResponseCode.CODE_201.toString(), ResponseCode.CODE_201.toCode());
	    	}
    	}else{
    		return this.respBodyWriter.toError(ResponseCode.CODE_201.toString(), ResponseCode.CODE_201.toCode());
    	}
    	return this.respBodyWriter.toSuccess(); 
    } 
    
    @RequestMapping("/deletePraise")
    @ResponseBody
    public RespBody deletePraise(Long videoId){ 
    	String uid = (String)this.request.getHeader("uid"); 
    	boolean f = videoService.praised(Long.parseLong(uid),videoId); 
    	if(!f){
    		return this.respBodyWriter.toError(ResponseCode.CODE_337.toString(), ResponseCode.CODE_337.toCode());
    	}
    	praiseService.deletePraise(videoId, Long.parseLong(uid));
    	return this.respBodyWriter.toSuccess(); 
    } 
    
    @RequestMapping("/findPraiseList")
    @ResponseBody
    public RespBody findPraiseList(int flag, Integer startId, Integer count, String videoId){
    	String uid = (String)this.request.getHeader("uid"); 
    	List<Filter> filters = new ArrayList<Filter>(1);
    	filters.add(new Filter("videoId",videoId)); 
    	if(flag==1){//flag==0表示最新，flag==1表示从startId起的老数据
        	filters.add(Filter.lt("id", startId));  
    	}
    	Sort s = new Sort(Sort.Direction.DESC,"id");  
    	RespBody respBody = this.list(0, count, filters, s);
    	
    	//判断是否关注点赞用户
    	for (Praise praise :(List<Praise>)respBody.getResult()){
    		Long id = praise.getCreatorId();
    		if (uid!=null && !"".equals(uid)){
    			int isAttention = attentionService.isAttention(Long.parseLong(uid), id);
    			Ruser user = (Ruser)praise.getUser();
    			user.setIsAttention(isAttention);
    			praise.setUser(user);
    		}
    	}
    	return respBody; 
    } 
    
    @RequestMapping("/findMyPraiseList")
    @ResponseBody
    public RespBody findMyPraiseList(int flag, Integer startId, Integer count){
    	String uid = (String)this.request.getHeader("uid"); 
    	logger.info("findMyPraiseList,uid={}",uid);
    	
    	List<Long> idList = videoService.findMyVideoIdList(Long.valueOf(uid));
    	String videoIds = "";
    	if (idList != null && idList.size()>0){
    		for(int i=0;i<idList.size();i++){
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
    	List<Praise> list = this.listAndNotReturn(0, count, filters, s);
    	List<Praise> destList = new ArrayList<Praise>();
    	for(Praise p:list){
    		Video video = videoService.getVideo(p.getVideoId(), uid);
    		if (video != null) {
    			p.setVideo(video);
        		destList.add(p);
    		}
    	}
    	return this.respBodyWriter.toSuccess(destList); 
    } 
    
    public boolean checkAndAddSign(String userId){
    	Map<String,Object> map = new HashMap<String,Object>();
    	String Key  = BicycleConstants.SION_SUM_BYDAY+userId;
    		String signsum = (String)jedisService.get(Key);
    		if((Integer.parseInt(signsum)<5000)){
    			int nsign = StringUtils.isNotBlank(signsum)?Integer.parseInt(signsum):0;
    			 jedisService.set(BicycleConstants.SION_SUM_BYDAY+userId,String.valueOf(nsign+100));
    			 jedisService.expireAt(BicycleConstants.SION_SUM_BYDAY+userId,DateUtils.getNowUnixYMDAsStr());
    		}else{
    			return false;
    		}
    		return true;
    }
	
	   
    public boolean checkAndAddSign1(String userId){
    	String Key  = BicycleConstants.SION_SUM_BYDAY+userId;
    		String signsum = jedisService.get(Key);
    		if(signsum!=null&&(Integer.parseInt(signsum)<5000)){
    			int nsign = Integer.parseInt(signsum);
    			 jedisService.set(BicycleConstants.SION_SUM_BYDAY+userId,String.valueOf(nsign+100));
    			 
    			 jedisService.expireAt(BicycleConstants.SION_SUM_BYDAY+userId,DateUtils.parseDate(DateFormatUtils.PATTEN_DEFAULT_FULL,DateUtils.getNowUnixYMDAsStr1()).getTime()/1000);
    		}else if(signsum==null){
   			 jedisService.set(BicycleConstants.SION_SUM_BYDAY+userId,String.valueOf(100));
   			 jedisService.expireAt(BicycleConstants.SION_SUM_BYDAY+userId,DateUtils.parseDate(DateFormatUtils.PATTEN_DEFAULT_FULL,DateUtils.getNowUnixYMDAsStr1()).getTime()/1000);
    		}else{
    			return false;
    		}
    		return true;
    }
    
    public boolean checkPraiseUser(String uid,String videoId,String shareUid){
    	
    List<SignVO> ls =	signService.findPraiseShareSign(videoId,shareUid,uid);
    if(ls!=null&&ls.size()>0){
    	return false;
    }
    	return true;
    }
    public boolean checkShareUser(String shareUid,String uid,String videoId){
    	  List<SignVO> ls =	signService.findPraiseShareSign(videoId,uid,shareUid);
    	  if(ls!=null&&ls.size()>0){
    		  return false;
    	  }
    	return true;
    }
    public boolean isNewUser(String userId){
    	Ruser ruser = ruserService.find(Long.parseLong(userId));
    	Long nowTime = new Date().getTime();
    	Long create = ruser.getCreateDate();
    	Long onedat= 1000*24*60*60L;
    	if(ruser!=null&&(nowTime-create)<onedat){
    		return true;
    	}
    	return false;
    }
    @RequestMapping("/isPraised")
    @ResponseBody
    public  RespBody isPraised(Praise p){
    	String uid = (String)this.request.getHeader("uid");
    	if(uid==null||uid.isEmpty()){
    		return this.respBodyWriter.toSuccess(false);
    	}
    	boolean f = videoService.praised(Long.parseLong(uid),p.getVideoId()); 
    	return this.respBodyWriter.toSuccess(f);
    }
}

