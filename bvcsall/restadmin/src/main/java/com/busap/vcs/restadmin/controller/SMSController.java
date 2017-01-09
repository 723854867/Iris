package com.busap.vcs.restadmin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.entity.BaseEntity;
import com.busap.vcs.data.entity.Sms;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.SmsService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;

/**
 * 短信提醒维护
 * @author konglinghai
 *
 */
@Controller()
@RequestMapping("sms")
public class SMSController extends CRUDController<Sms, Long>{

	private static final Logger logger = LoggerFactory.getLogger(SMSController.class);
	
	@Resource(name="smsService")
	private SmsService smsService;
	
	@Resource(name="smsService")
	@Override
	public void setBaseService(BaseService<Sms, Long> baseService) {
		this.baseService=baseService;
	}
	
	@RequestMapping("smslist")
	public String list(){
		return "sms/list";
	}
	
	@RequestMapping(value = {"/updatepage"}, method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RespBody updatepage(Sms entity,HttpServletRequest req) {
    	if (!validator(entity, BaseEntity.Update.class)) {
            return respBodyWriter.toError(entity);
        }
    	Sms dbEntity=this.smsService.find(entity.getId());
    	
    	String[] ps=new String[]{"name","phoneNo"};
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

}
