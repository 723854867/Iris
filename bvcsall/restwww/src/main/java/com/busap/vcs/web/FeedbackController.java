package com.busap.vcs.web;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.entity.Feedback;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
 
@Controller
@RequestMapping("/feedback")
public class FeedbackController extends CRUDController<Feedback, Long> {

    private Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    @Resource(name="feedbackService")
    @Override
    public void setBaseService(BaseService<Feedback, Long> baseService) {
        this.baseService = baseService;
    }

    
    //添加用户反馈
    @RequestMapping("/addFeedback")
    @ResponseBody
    public RespBody addFeedback(String content,String contact,String dataFrom,@RequestParam(value = "appVersion", required = false)String appVersion){ 
    	String uid = (String) this.request.getHeader("uid");
		logger.info("uid={},addFeedback", uid);
		
    	Feedback fb = new Feedback();
    	fb.setContact(contact);
    	fb.setContent(content);
    	fb.setDataFrom(dataFrom);
    	fb.setAppVersion(appVersion);
    	fb.setCreatorId(Long.valueOf(uid));
    	fb.setCreateDate(new Date());
    	return this.create(fb); 
    }  
}

