package com.busap.vcs.restadmin.controller;

import com.busap.vcs.data.entity.*;
import com.busap.vcs.service.*;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huoshanwei on 2015/11/30.
 */
@Controller
@RequestMapping("combogrid")
public class CombogridController {

    @Resource
    private CombogridService combogridService;

    @Resource
    private VideoService videoService;

    @Resource
    private RoomService roomService;

    @Resource
    private AnchorService anchorService;

    @RequestMapping("getUserCombogridByType")
    @ResponseBody
    public Map<String, Object> getUserCombogridByType(Integer type){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("type",type);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("rows", combogridService.queryUserCombogridByType(params));
        return resultMap;
    }


    @RequestMapping("getCombogridUserList")
    @ResponseBody
    @EnablePaging
    public Map<String, Object> getCombogridUserList(Integer type,
                                                    @RequestParam(value = "user", required = false) Long user,
                                                    @RequestParam(value = "userKeyword", required = false) String userKeyword,
                                                    @ModelAttribute("queryPage") JQueryPage queryPage) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        params.put("userKey", user);
        params.put("userKeyword", userKeyword);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Ruser> list = combogridService.queryUserCombogridByType(params);
        Page page = PagingContextHolder.getPage();
        resultMap.put("rows", list);
        resultMap.put("total", page.getTotalResult());
        return resultMap;
    }

    @RequestMapping("getCombogridVideoList")
    @ResponseBody
    @EnablePaging
    public Map<String, Object> getCombogridVideoList(Integer type,@ModelAttribute("queryPage") JQueryPage queryPage) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Video> list = videoService.select(params);
        Page page = PagingContextHolder.getPage();
        resultMap.put("rows", list);
        resultMap.put("total", page.getTotalResult());
        return resultMap;
    }

    @RequestMapping("getCombogridRoomListByRoomId")
    @ResponseBody
    @EnablePaging
    public Map<String,Object> getCombogridRoomListByRoomId(Long userId,@ModelAttribute("queryPage") JQueryPage queryPage){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        List<Room> list = roomService.queryRoomListByUserId(params);
        Page page = PagingContextHolder.getPage();
        resultMap.put("rows", list);
        resultMap.put("total", page.getTotalResult());
        return resultMap;
    }

    //MP -- missingPlayback
    @RequestMapping("getCombogridMPListByStreamId")
    @ResponseBody
    @EnablePaging
    public Map<String,Object> getCombogridMPListByStreamId(Long userId,@ModelAttribute("queryPage") JQueryPage queryPage){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Anchor anchor = anchorService.getAnchorByUserid(userId);
        if(anchor != null && StringUtils.isNotBlank(anchor.getStreamId())){
            List<MissingPlaybackInfo> list = videoService.queryMPByStreamId(anchor.getStreamId());
            Page page = PagingContextHolder.getPage();
            resultMap.put("rows", list);
            resultMap.put("total", page.getTotalResult());
        }else{
            resultMap.put("rows", "");
            resultMap.put("total", 0);
        }
        return resultMap;
    }


}
