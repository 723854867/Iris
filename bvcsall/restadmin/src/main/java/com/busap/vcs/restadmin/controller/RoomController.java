package com.busap.vcs.restadmin.controller;

import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.model.AnchorDetailDisplay;
import com.busap.vcs.data.model.LiveDetailDisplay;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.RoomService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.controller.CRUDController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by busap on 2016/5/16.
 */
@Controller
@RequestMapping("room")
public class RoomController  extends CRUDController<Room, Long> {

    @Resource
    private RoomService roomService;
    
    @Resource(name="ruserService")
	private RuserService ruserService;

    @Resource(name = "roomService")
    @Override
    public void setBaseService(BaseService<Room, Long> baseService) {
        this.baseService = baseService;
    }

    @RequestMapping("forwardLiveDetailList")
    public ModelAndView forwardGiftList(HttpServletRequest req) {
    	
    	StringBuffer jpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append(" select  distinct r.appVersion FROM AppVersion r  ");
        
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("createDate",1,"r");
        orderByList.add(orderByObject);
        
        
        try {
			List appVersionList=roomService.getObjectByJpql(jpql, 0, 1000, "r", paramList, null, orderByList);
			
			req.setAttribute("appVersionList", appVersionList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        List<String> channelList = ruserService.selectAllRegPlatform();
        
        req.setAttribute("channelList", channelList);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("room/query_live_detail");
        mav.addObject("selected","userLiveDetail");
        return mav;
    }

    @RequestMapping("queryLiveDetailList")
    @ResponseBody
    @EnablePaging
    public Map<String, Object> queryLiveDetailList(@ModelAttribute("queryPage") JQueryPage queryPage,
                                                   @RequestParam(value = "userKey",required = false) Long userKey,
                                                   @RequestParam(value = "userKeyword",required = false) String userKeyword,
                                                   @RequestParam(value = "startDate",required = false) String startDate,
                                                   @RequestParam(value = "endDate",required = false) String endDate,
                                                   @RequestParam(value = "appVersion",required = false) String appVersion,
                                                   @RequestParam(value = "channel",required = false) String channel,
                                                   @RequestParam(value = "platform",required = false) String platform) {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("userKey",userKey);
        params.put("userKeyword",userKeyword);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("appVersion",appVersion);
        params.put("platform",platform);
        params.put("channel",channel);
        List<LiveDetailDisplay> list = roomService.queryLiveDetailRecord(params);
        Page page = PagingContextHolder.getPage();
        for(LiveDetailDisplay liveDetail : list){
            liveDetail.setGiftNumber(liveDetail.getGiftNumber()==null?0:liveDetail.getGiftNumber());
            liveDetail.setPointNumber(liveDetail.getPointNumber()==null?0:liveDetail.getPointNumber());
            liveDetail.setMaxAccessNumber(liveDetail.getMaxAccessNumber()==null?0:liveDetail.getMaxAccessNumber());
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }


}
