package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.base.Action;
import com.busap.vcs.base.Message;
import com.busap.vcs.base.Module;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Berry;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.BerryService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.kafka.producer.KafkaProducer;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;

 
@Controller
@RequestMapping("/berry")
public class BerryController extends CRUDController<Berry, Long> {

    private Logger logger = LoggerFactory.getLogger(BerryController.class); 

    @Resource(name="berryService") 
    BerryService berryService; 
    
    @Resource(name = "ruserService")
	private RuserService ruserService;
    
    @Autowired
    JedisService jedisService;
    
    @Autowired
    KafkaProducer kafkaProducer;
    
    @Resource(name="bstarService")
    @Override
    public void setBaseService(BaseService<Berry, Long> baseService) {
        this.baseService = baseService;
    } 
    
    //种草莓
    @RequestMapping("/plantBerry")
    @ResponseBody
    public RespBody plantBerry(Long destId){ 
    	String uid = request.getHeader("uid");
    	logger.info("plantBerry,uid={},destId={}",uid,destId);
    	
    	int count = jedisService.incr(BicycleConstants.PLANT_BERRY_TIME + uid); //使用redis自身方法自增，避免脏读
    	if (count == 1) { //当日首次赠送的时候设置过期时间
    		jedisService.expire(BicycleConstants.PLANT_BERRY_TIME + uid, Integer.parseInt(String.valueOf(getExpireTime())));
    	}
    	
    	if (count <= 3) {
    		Berry berry = new Berry();
    		berry.setCreatorId(Long.parseLong(uid));
    		berry.setDestId(destId);
    		
    		berryService.save(berry);
    		ruserService.updateBerryCount(destId);
    		Message msg = new Message();
			msg.setModule(Module.BERRY);
			msg.setAction(Action.INSERT);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			map.put("pid", String.valueOf(destId));
			msg.setDataMap(map);
	    	kafkaProducer.send("push-msg-topic", msg);
    	} else {
    		return respBodyWriter.toError(ResponseCode.CODE_353.toString(), ResponseCode.CODE_353.toCode());
    	}
    	
    	return respBodyWriter.toSuccess(); 
    }  
    
    //获得种草莓列表
    @RequestMapping("/getPlantBerryList")
    @ResponseBody
    public RespBody getPlantBerryList(Long userId,Integer page,Integer size){ 
    	String uid = request.getHeader("uid");
    	logger.info("getPlantBerryList,userId={},page={},size={}",userId,page,size);
    	List<Berry> berryList = berryService.getPlantBerryList(userId, page, size);
    	List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
    	if (berryList != null) {
    		for (int i=0;i<berryList.size();i++) {
    			String creatorId = String.valueOf(berryList.get(i).getCreatorId());
    			Map<String,String> createUser = jedisService.getMapByKey(BicycleConstants.USER_INFO+creatorId);
    			if (createUser != null && createUser.size() >0) {
    				Map<String,String> result = new HashMap<String,String>();
    				result.put("id", createUser.get("id"));
    				result.put("name", createUser.get("name"));
    				result.put("pic", createUser.get("pic"));
    				result.put("signature", createUser.get("signature"));
    				result.put("vstat", createUser.get("vipStat"));
    				result.put("sex", createUser.get("sex"));
    				result.put("createDate", String.valueOf(berryList.get(i).getCreateDate()));
    				resultList.add(result);
    			}
    		}
    	}
    	
    	
    	return respBodyWriter.toSuccess(resultList); 
    }  
    
    private long getExpireTime() {
        Calendar b = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return (c.getTimeInMillis() / 1000) - (b.getTimeInMillis() / 1000);
    }
    
}
