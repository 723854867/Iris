package com.busap.vcs.web;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.entity.Spread;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.SpreadService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
 
@Controller
@RequestMapping("/spread")
public class SpreadController extends CRUDController<Spread, Long> {

    private Logger logger = LoggerFactory.getLogger(SpreadController.class); 

    @Resource(name="spreadService") 
    SpreadService spreadService; 
    
    @Resource(name="ruserService")
	private RuserService ruserService;
    
    @Resource(name="spreadService")
    @Override
    public void setBaseService(BaseService<Spread, Long> baseService) {
        this.baseService = baseService;
    } 
    
    @RequestMapping("/home")
	public String home() {
    	return "html5/app_activity/inviteCode/index";
	}
    
    @RequestMapping("/commit")
    @ResponseBody
    public RespBody commit(Long uid,String inviteCode){ 
    	if (spreadService.uidExist(uid) >0) {
    		return respBodyWriter.toError("该用户已经提交过", -1);
    	}
    	if (ruserService.find(uid) == null) {
    		return respBodyWriter.toError("该用户id不存在", -1);
    	}
    	Spread spread = new Spread();
    	spread.setCreatorId(uid);
    	spread.setInviteCode(inviteCode);
    	this.create(spread);
    	return respBodyWriter.toSuccess("提交成功", "ok"); 
    }  
    
}
