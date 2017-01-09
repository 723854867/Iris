package com.busap.vcs.restadmin.controller;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.Constants;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.util.page.JQueryPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by busap on 2016/4/20.
 */
@Controller
@RequestMapping("/giveGold")
public class GiveGoldController {

    @Resource(name = "jedisService")
    private JedisService jedisService;

    //礼物列表页跳转
    @RequestMapping("forwardGiveGold")
    public ModelAndView forwardGiveGold() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("selected", "giveGold");
        mav.setViewName("giveGold/query_give_gold");
        return mav;
    }

    //查询列表信息
    @RequestMapping("queryGiveGoldList")
    @ResponseBody
    public Map<String, Object> queryGiftList(@ModelAttribute("queryPage") JQueryPage queryPage) {
        Map<String, String> shareMap = jedisService.getMapByKey(BicycleConstants.SHARE_GIVE_GOLD);
        shareMap.put("type", "1");
        Map<String, String> firstLiveMap = jedisService.getMapByKey(BicycleConstants.FIRST_LIVE_GIVE_GOLD);
        firstLiveMap.put("type", "2");
        Map<String, String> shareButtonMap = jedisService.getMapByKey(BicycleConstants.SHARE_GIVE_GOLD_BUTTON);
        shareButtonMap.put("type", "3");
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        list.add(shareMap);
        list.add(firstLiveMap);
        list.add(shareButtonMap);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", list.size());
        resultMap.put("rows", list);
        return resultMap;
    }

    @RequestMapping("insertGiveGold")
    @ResponseBody
    public ResultData insertGiveGold(Integer type, Integer diamond, @RequestParam(value = "count", required = false) Integer count, String tips) {
        ResultData resultData = new ResultData();
        HashMap<String, String> map = new HashMap<String, String>();
        switch (type) {
            case 1:
                //分享
                map.put("diamond", String.valueOf(diamond));
                map.put("count", String.valueOf(count));
                map.put("tips", tips);
                jedisService.setValueToMap(BicycleConstants.SHARE_GIVE_GOLD, map);
                break;
            case 2:
                //首次直播
                map.put("diamond", String.valueOf(diamond));
                map.put("tips", tips);
                jedisService.setValueToMap(BicycleConstants.FIRST_LIVE_GIVE_GOLD, map);
                break;
            case 3:
                //分享按钮显示文案
                map.put("tips", tips);
                jedisService.setValueToMap(BicycleConstants.SHARE_GIVE_GOLD_BUTTON, map);
                break;
            default:
                //分享
                map.put("diamond", String.valueOf(diamond));
                map.put("count", String.valueOf(count));
                map.put("tips", tips);
                jedisService.setValueToMap(BicycleConstants.SHARE_GIVE_GOLD, map);
                break;
        }
        resultData.setResultCode("success");
        resultData.setResultMessage("成功");
        return resultData;
    }

    @RequestMapping("updateGiveGoldTemplate")
    public ModelAndView updateGiveGoldTemplate(Integer type) {
        ModelAndView mav = new ModelAndView();
        Map<String, String> resultMap = null;
        switch (type) {
            case 1:
                resultMap = jedisService.getMapByKey(BicycleConstants.SHARE_GIVE_GOLD);
                resultMap.put("type", "1");
                break;
            case 2:
                resultMap = jedisService.getMapByKey(BicycleConstants.FIRST_LIVE_GIVE_GOLD);
                resultMap.put("type", "2");
                break;
            case 3:
                resultMap = jedisService.getMapByKey(BicycleConstants.SHARE_GIVE_GOLD_BUTTON);
                resultMap.put("type", "3");
                break;
            default:
                resultMap = jedisService.getMapByKey(BicycleConstants.SHARE_GIVE_GOLD);
                resultMap.put("type", "1");
                break;
        }
        mav.addObject("resultMap", resultMap);
        mav.setViewName("giveGold/update_give_gold");
        return mav;
    }


}
