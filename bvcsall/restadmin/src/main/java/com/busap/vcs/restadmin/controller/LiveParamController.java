package com.busap.vcs.restadmin.controller;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.DailyLiveData;
import com.busap.vcs.data.entity.LiveParam;
import com.busap.vcs.data.model.LiveDayDetailDisplay;
import com.busap.vcs.restadmin.utils.ComparatorParam;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.Constants;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RoomService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 视频直播参数设置
 * Created by Knight on 15/11/13.
 */

@Controller()
@RequestMapping("liveParam")
public class LiveParamController  extends CRUDController<LiveParam, Long> {

    // 是否显示直播开关
    public static final String LIVE_SWITCH="LIVE_SWITCH";
    // 直播推送开关
    public static final String LIVE_PUSH_SWITCH="LIVE_PUSH_SWITCH";

    @Autowired
    JedisService jedisService;

    @Resource(name = "roomService")
    RoomService roomService;

    @RequestMapping("paramList")
    public String paramList() throws Exception {
        String liveSwitch = jedisService.get(LIVE_SWITCH);
        String livePushSwitch = jedisService.get(LIVE_PUSH_SWITCH);
        this.request.setAttribute("liveSwitch", liveSwitch);
        this.request.setAttribute("livePushSwitch", livePushSwitch);
        return "liveParam/liveParamList";
    }


    @RequestMapping(value = "paramListJson")
    @ResponseBody
    @EnablePaging
    public Map<String,Object> commentListJson(Integer page, Integer rows) throws Exception{

        List<LiveParam> paramList = new ArrayList<LiveParam>();
        Map<String, String> viewerMap = jedisService.getMapByKey(BicycleConstants.LIVE_VIEWER_COUNT);

        if (viewerMap.size() > 0) {
            for (String key : viewerMap.keySet()) {
                if (viewerMap.get(key) != null && StringUtils.isNotBlank(viewerMap.get(key))) {
                    LiveParam param = new LiveParam();
                    param.setType(1);
                    param.setCount(Integer.parseInt(key));
                    param.setDescription(viewerMap.get(key));
                    paramList.add(param);
                }
            }
        }
        Map<String, String> praiseMap = jedisService.getMapByKey(BicycleConstants.LIVE_PRAISE_COUNT);
        if (praiseMap.size() > 0) {
            for (String key : praiseMap.keySet()) {
                if (praiseMap.get(key) != null && StringUtils.isNotBlank(praiseMap.get(key))) {
                    LiveParam param = new LiveParam();
                    param.setType(2);
                    param.setCount(Integer.parseInt(key));
                    param.setDescription(praiseMap.get(key));
                    paramList.add(param);
                }
            }
        }

        if (paramList.size() > 0) {
            try {
                ComparatorParam comparator=new ComparatorParam();
                Collections.sort(paramList, comparator);
            } catch (Exception ignored) {
            }
        }


        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("total", paramList.size());
        map.put("rows", paramList);
        return map;
    }

    // 新建评论
    @RequestMapping("addParam")
    public String showAddParam() {
        return "liveParam/addLiveParam";
    }

    // 保存新建评论
    @RequestMapping("saveParam")
    public String saveParamHandler(String paramType, String paramCount, String paramDescription) {
        setJedisParam(paramType, paramCount, paramDescription);

        String liveSwitch = jedisService.get(LIVE_SWITCH);
        String livePushSwitch = jedisService.get(LIVE_PUSH_SWITCH);
        this.request.setAttribute("liveSwitch", liveSwitch);
        this.request.setAttribute("livePushSwitch", livePushSwitch);
        return "liveParam/liveParamList";
    }

    // 跳转到编辑评论
    @RequestMapping("paramEdit")
    public String paramEditHandler(@RequestParam(value = "type", required = true)  Integer type,
                                   @RequestParam(value = "count", required = true)  Integer count) {
        String key = "";
        if (type == 1) {
            key = BicycleConstants.LIVE_VIEWER_COUNT;
        } else if (type == 2) {
            key = BicycleConstants.LIVE_PRAISE_COUNT;
        }
        Map<String, String> result = jedisService.getMapByKey(key);
        LiveParam param = new LiveParam();
        param.setType(type);
        param.setCount(count);
        param.setDescription(result.get(String.valueOf(count)));
        this.request.setAttribute("param", param);
        this.request.setAttribute("description", result.get(String.valueOf(count)));
        return "liveParam/addLiveParam";
    }

    // 保存编辑评论
    @RequestMapping("deleteParam")
    @ResponseBody
    public String deleteParamHandler(@RequestParam(value = "type", required = true)  Integer type,
                                     @RequestParam(value = "count", required = true)  Integer count) {

        String key = "";
        if (type == 1) {
            key = BicycleConstants.LIVE_VIEWER_COUNT;
        } else if (type == 2) {
            key = BicycleConstants.LIVE_PRAISE_COUNT;
        }
        String desc = jedisService.getValueFromMap(key, String.valueOf(count));
        System.out.println(desc);
        long l = jedisService.deleteValueFromMap(key, String.valueOf(count));
        if (l > 0) {
            return "ok";
        } else {
            return "err";
        }

    }

    @RequestMapping("changeLiveSwitch")
    @ResponseBody
    public String changeLiveSwitchHandler(@RequestParam(value = "value", required = true)  Integer value) {
        jedisService.set(LIVE_SWITCH, String.valueOf(value));
        return "ok";
    }

    @RequestMapping("changeLivePushSwitch")
    @ResponseBody
    public String changeLivePushSwitchHandler(@RequestParam(value = "value", required = true)  Integer value) {
        jedisService.set(LIVE_PUSH_SWITCH, String.valueOf(value));
        return "ok";
    }

    private void setJedisParam(String paramType, String count, String description) {
        String key = "";
        if (Integer.parseInt(paramType) == 1) {
            key = BicycleConstants.LIVE_VIEWER_COUNT;
        } else if (Integer.parseInt(paramType) == 2) {
            key = BicycleConstants.LIVE_PRAISE_COUNT;
        }
        if (StringUtils.isNotBlank(key)) {
            jedisService.deleteValueFromMap(key, String.valueOf(count));
            jedisService.setValueToMap(key, count, description);
        }

    }

    @RequestMapping(value = "getDailyLiveData")
    @ResponseBody
    @EnablePaging
    public Map<String,Object> getDailyLiveData(String startTime, String endTime,String platform,String regPlatform,String appVersion) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<DailyLiveData> list = new ArrayList<DailyLiveData>();
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("platform",platform);
        params.put("regPlatform",regPlatform);
        params.put("appVersion",appVersion);
        Long totalLiveNum = 0L;
        if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
            String date = sdf.format(c.getTime());
            DailyLiveData dd = new DailyLiveData();
            dd.setDate(date);
            String onlineNum = jedisService.get(BicycleConstants.MAX_ANCHOR_BY_DAY_ + date);
            dd.setMaxOnlineNum(onlineNum);
            params.put("startDate",date+" 00:00:00");
            params.put("endDate",date + " 23:59:59");
            dd.setLiveNum(String.valueOf(roomService.queryDailyDataLiveCount(params)));
            dd.setNewRegLiveNum(String.valueOf(roomService.queryDailyDataNewRegLiveCount(params)));
            dd.setNewLiveNum(String.valueOf(roomService.queryDailyDataNewLiveCount(params)));
            list.add(dd);
            totalLiveNum = roomService.queryDailyDataLiveTotalCount(params);
        } else {
            Calendar cStart = Calendar.getInstance();
            if (StringUtils.isNotBlank(startTime)) {
                Date startDate = sdf.parse(startTime);
                cStart.setTime(startDate);
            } else {
                HashMap<String,Object> map = new HashMap<String, Object>();
                map.put("rows", list);
                return map;
            }

            Calendar cEnd = Calendar.getInstance();
            if (StringUtils.isNotBlank(endTime)) {
                Date endDate = sdf.parse(endTime);
                cEnd.setTime(endDate);
            } else {
                cEnd.set(Calendar.DATE, cEnd.get(Calendar.DATE) - 1);
            }

            while (cStart.compareTo(cEnd) < 0) {
                String currentDate = sdf.format(cStart.getTime());
                String onlineNum = jedisService.get(BicycleConstants.MAX_ANCHOR_BY_DAY_ + currentDate);
                DailyLiveData dd = new DailyLiveData();
                dd.setDate(currentDate);
                if (StringUtils.isNotBlank(onlineNum)) {
                    dd.setMaxOnlineNum(onlineNum);
                } else {
                    dd.setMaxOnlineNum("0");
                }
                params.put("startDate",currentDate+" 00:00:00");
                params.put("endDate",currentDate + " 23:59:59");
                Long liveNum = roomService.queryDailyDataLiveCount(params);
                if (liveNum != null && liveNum > 0) {
                    dd.setLiveNum(String.valueOf(liveNum));
                } else {
                    dd.setLiveNum("0");
                }
                Long newRegLiveNum = roomService.queryDailyDataNewRegLiveCount(params);
                dd.setNewRegLiveNum(String.valueOf(newRegLiveNum));
                String newLiveNum = String.valueOf(roomService.queryDailyDataNewLiveCount(params));
                dd.setNewLiveNum(newLiveNum);
                cStart.set(Calendar.DATE, cStart.get(Calendar.DATE) + 1);
                list.add(dd);
            }

            params.put("startDate",startTime +" 00:00:00");
            if(StringUtils.isBlank(endTime)){
                params.put("endDate", sdf.format(cEnd.getTime()) + " 23:59:59");
            }else{
                cEnd.set(Calendar.DATE, cEnd.get(Calendar.DATE) - 1);
                params.put("endDate", sdf.format(cEnd.getTime()) + " 23:59:59");
            }

            totalLiveNum = roomService.queryDailyDataLiveTotalCount(params);
        }

        HashMap<String,Object> map = new HashMap<String, Object>();

        map.put("totalLiveNum",totalLiveNum);
        map.put("rows", list);
        return map;
    }

    @RequestMapping("liveDataList")
    public ModelAndView liveDataList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("liveParam/queryLiveData");
        mav.addObject("selected","dayLiveData");
        return mav;
    }


    @Override
    public void setBaseService(BaseService<LiveParam, Long> baseService) {
        this.baseService = baseService;
    }

    @RequestMapping("getLiveDetailData")
    @ResponseBody
    public Map<String,Object> getLiveDetailData(String date,String platform,String regPlatform,String appVersion){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("startDate",date +" 00:00:00");
        params.put("endDate",date + " 23:59:59");
        params.put("platform",platform);
        params.put("regPlatform",regPlatform);
        params.put("appVersion",appVersion);
        List<LiveDayDetailDisplay> list = roomService.queryDailyDataLiveDetailCount(params);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("rows",list);
        return map;
    }
    
    @RequestMapping("whiteList")
    @ResponseBody
    public Map<String,Object> getLiveDetailData(Integer page, Integer rows){
    	if(page == null || page <= 0){
    		page=1;
    	}
		if(rows == null || rows <= 0){
			rows=20;
		}
		Map<String,Object> params = new HashMap<String,Object>();
    	params.put("pageStart", (page-1)*rows);
    	params.put("pageSize", rows);
    	
    	int resultSize = 0;
    	List<Map<String,String>> userList = new ArrayList<Map<String,String>>();
    	Set<String> superUids = jedisService.getSetFromShard(BicycleConstants.SUPERADMINS);
    	if(superUids !=null && superUids.size()>0){
    		resultSize = superUids.size();
    		for(String uid:superUids){
    			Map<String,String> map = jedisService.getMapByKey(BicycleConstants.USER_INFO+uid);
    			if(map!=null && !map.isEmpty()){
    				userList.add(map);
    			}
    		}
    	}
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total", resultSize);
		result.put("rows", userList);
		return result;
    }
    
    @RequestMapping("addSuperAdmin")
	@ResponseBody
	public RespBody addUser(String uid){
		if(StringUtils.isNotBlank(uid)){
			jedisService.setValueToSetInShard(BicycleConstants.SUPERADMINS, uid);
			return this.respBodyWriter.toSuccess();
		} else {
			return this.respBodyWriter.toError("参数不正确");
		}
	}
	
	@RequestMapping("removeSuperAdmin")
	@ResponseBody
	public RespBody removeUsers(String uid){
		if(StringUtils.isNotBlank(uid)){
			String ids[] = uid.split(","); 
			for(String id:ids){
				jedisService.deleteSetItemFromShard(BicycleConstants.SUPERADMINS, id);
			}
			return this.respBodyWriter.toSuccess();
		} else {
			return this.respBodyWriter.toError("参数不正确");
		}
	}
    
}
