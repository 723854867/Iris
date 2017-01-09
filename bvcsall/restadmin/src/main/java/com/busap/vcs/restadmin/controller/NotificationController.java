package com.busap.vcs.restadmin.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.entity.Notification;
import com.busap.vcs.data.entity.OperationLog;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.NotificationService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;

@Controller
@RequestMapping("notify")
public class NotificationController extends CRUDController<Notification, Long>{

	@Resource(name = "notificationService")
	NotificationService notifyService;
	
	@Resource(name = "notificationService")
	@Override
	public void setBaseService(BaseService<Notification, Long> baseService) {
		this.baseService = baseService;
	}
	
	@RequestMapping(value="notelist")
	public String list(Notification notify){
		return "notification/list";
	}
	
	//entity.setCreatorId() 代表发送给的某个用户，而不是创建者，为空则发送给所有用户
	@RequestMapping(value="notesave")
	public @ResponseBody RespBody save(Notification entity){
		notifyService.save(entity);
		
		this.operationLogService.save(new OperationLog(log_meduleType, "添加", entity.getId().toString(), entity.getClass().getSimpleName(), U.getUid(), U.getUname(), "添加"+entity.getClass().getSimpleName()));
		return this.respBodyWriter.toSuccess(entity);
	}
}
