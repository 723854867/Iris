package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
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
import com.busap.vcs.webcomn.RespBody;
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

	
	@Resource(name = "liveComplaintService")
	private LiveComplaintService liveComplaintService;
	
	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name = "roomService")
	private RoomService roomService;
	
	@Resource(name = "liveComplaintRepository")
	private LiveComplaintRepository liveComplaintRepository;
	
	//key--未处理直播投诉 in redis 
	public static final String LC_CHECK = "LC_CHECK";

	@Resource(name="jedisService")
	private JedisService jedisService;

	@Resource(name = "liveComplaintService")
	@Override
	public void setBaseService(BaseService<LiveComplaint, Long> baseService) {
		this.baseService = baseService;
	}

	
	@RequestMapping(value = {"/addLiveComplaint"}, method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RespBody addLiveComplaint(Long complaintsId,Long liveId,Integer cType,HttpServletRequest req) {
		
		if(complaintsId!=null && jedisService.isSetMemberInShard(BicycleConstants.SUPERADMINS, complaintsId.toString())){
			return respBodyWriter.toError("不能举报超级管理员！");
		}
		
		LiveComplaint lc=new LiveComplaint();
		
		Ruser complaints=ruserService.find(complaintsId);
		
		if(complaints!=null) {
			lc.setComplaints(complaints.getName());
			lc.setComplaintsId(complaintsId);
			lc.setCreatorId(complaintsId);
		}
		
		Room room=roomService.find(liveId);
		
		if(room!=null&&room.getCreatorId()!=null) {
			lc.setLiveId(room.getId());
			lc.setLiveImg(room.getRoomPic());
			lc.setLiveTitle(room.getTitle());
			if(room.getCreatorId()!=null) {
				Ruser liveUser=ruserService.find(room.getCreatorId());
				lc.setLiveUserId(liveUser.getId());
				lc.setLiveUserName(liveUser.getName());
				
			}
		}
		
		String content="";
		if(cType!=null) {
			
			switch(cType) {
				case 1:content="色情";
					break;
				case 2:content="骚扰";
					break;
				case 3:content="欺诈";
					break;
				case 4:content="侵权";
					break;
				default:
					content="其他";
					break;
			}
			
		}
		lc.setContent(content);
		
		lc.setStat(0);
		
		liveComplaintService.save(lc);
		
		Long unDealLCCount=liveComplaintRepository.unDealComplaintsCount();
		jedisService.set(LC_CHECK, unDealLCCount.toString());
    	
        return respBodyWriter.toSuccess("ok");
    }
	

}
