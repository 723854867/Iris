package com.busap.vcs.web;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Complain;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.enums.VideoStatus;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.ComplainService;
import com.busap.vcs.service.SensitiveWordService;
import com.busap.vcs.service.VideoService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
 
@Controller
@RequestMapping("/complain")
public class ComplainController extends CRUDController<Complain, Long> {

    private Logger logger = LoggerFactory.getLogger(ComplainController.class);

    @Resource(name="complainService")
    ComplainService complainService;
    
    @Resource(name="videoService") 
    VideoService videoService;
    
    @Resource(name="sensitiveWordService") 
    SensitiveWordService sensitiveWordService;

    @Resource(name="complainService")
    @Override
    public void setBaseService(BaseService<Complain, Long> baseService) {
        this.baseService = baseService;
    }

    
    //获取所有活动基本信息
    @RequestMapping("/addComplain")
    @ResponseBody
    public RespBody addComplain(Complain c){ 
    	if(c.getVideoId()!=null&&!c.getVideoId().trim().equals("")){
	    	Video v = videoService.find(Long.parseLong(c.getVideoId())); 
			if(v==null||VideoStatus.已删除.getName().equals(v.getFlowStat())){
				return this.respBodyWriter.toError(ResponseCode.CODE_332.toString(), ResponseCode.CODE_332.toCode());
			}
    	}
    	sensitiveWordService.checkAndReplaceSensitiveWord(c);
    	return this.create(c); 
    }  
}

