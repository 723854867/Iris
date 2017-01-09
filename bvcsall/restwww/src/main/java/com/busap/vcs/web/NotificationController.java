package com.busap.vcs.web;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.entity.Favorite;
import com.busap.vcs.data.entity.Notification;
import com.busap.vcs.data.entity.User;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.FavoriteService;
import com.busap.vcs.service.NotificationService;
import com.busap.vcs.service.UserService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;

@Controller
@RequestMapping("/notify") 
public class NotificationController extends CRUDController<Notification, Long> {

    private Logger logger = LoggerFactory.getLogger(FavoriteController.class);

	@Resource(name = "notificationService")
	NotificationService notifyService;
	
	@Resource(name="userService")
	UserService userService;
	
	@Resource(name = "notificationService")
	@Override
	public void setBaseService(BaseService<Notification, Long> baseService) {
		this.baseService = baseService;
	}
	
    //按页查询我的通知
    @RequestMapping("/findMyNotifications")
    @ResponseBody
    public RespBody findByNotifications(Integer page, Integer size){
    	String uid = (String)this.request.getHeader("uid");
    	return respBodyWriter.toSuccess(notifyService.findMyNotifications(page, size, uid));
    }
    
    //更新极光消息推送的注册ID
    /*@RequestMapping("/updatePushRegistrationId")
    @ResponseBody
    public RespBody updatePushRegistrationId(String registrationId){
    	Long uid = Long.valueOf(this.request.getHeader("uid"));
    	User u=new User();
    	u.setId(uid);
    	u.setRegistrationId(registrationId);
    	return respBodyWriter.toSuccess(userService.update(u));
    }*/
    
}
