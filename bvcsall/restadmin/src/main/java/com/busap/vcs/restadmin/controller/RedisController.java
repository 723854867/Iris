package com.busap.vcs.restadmin.controller;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.service.JedisService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by busap on 2016/5/12.
 */
@Controller
@RequestMapping("redis")
public class RedisController {

    @Resource
    private JedisService jedisService;

    //礼物列表页跳转
    @RequestMapping("forwardSettingRedis")
    public ModelAndView forwardSettingRedis() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("checkGroupId",jedisService.get(BicycleConstants.CHECK_GROUP_ID));
        mav.addObject("checkAdminGroupId",jedisService.get(BicycleConstants.CHECK_ADMIN_GROUP_ID));
        mav.addObject("bStarName",jedisService.get(BicycleConstants.B_STAR_NAME));
        mav.addObject("firstLiveTimes",jedisService.get(BicycleConstants.FIRST_LIVE_TIMES));
        mav.setViewName("redis/setting_redis");
        return mav;
    }

    @RequestMapping("updateRedis")
    @ResponseBody
    public String updateRedis(String type,String value){
        if("checkGroupId".equals(type)){
            jedisService.set(BicycleConstants.CHECK_GROUP_ID,value);
        }else if("checkAdminGroupId".equals(type)){
            jedisService.set(BicycleConstants.CHECK_ADMIN_GROUP_ID,value);
        }else if("bStarName".equals(type)){
            jedisService.set(BicycleConstants.B_STAR_NAME,value);
        }else if("firstLiveTimes".equals(type)){
            jedisService.set(BicycleConstants.FIRST_LIVE_TIMES,value);
        }
        return "success";
    }


}
