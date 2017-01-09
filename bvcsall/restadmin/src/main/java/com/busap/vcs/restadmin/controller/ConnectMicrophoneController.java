package com.busap.vcs.restadmin.controller;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 霍闪伟 on 2016/5/12.
 */
@Controller
@RequestMapping("connectMicrophone")
public class ConnectMicrophoneController {

    @Resource
    private JedisService jedisService;

    @Resource
    private RuserService ruserService;

    @RequestMapping("forwardCMWhiteList")
    public ModelAndView forwardCMWhiteList() {
        ModelAndView modelAndView = new ModelAndView();
        String diamond = jedisService.get(BicycleConstants.CONNECT_MICROPHONE_DIAMONDS);
        modelAndView.addObject("diamond",diamond);
        modelAndView.setViewName("user/cm_white_list");
        return modelAndView;
    }

    @RequestMapping("queryCMWhiteList")
    @ResponseBody
    public Map<String,Object> queryCMWhiteList(){
        Set<String> set = jedisService.getSortedSetFromShardByDesc(BicycleConstants.CONNECT_MICROPHONE_WHITE_LIST);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        for (String userId : set){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("userId",userId);
            Map<String,String> userMap = jedisService.getMapByKey(BicycleConstants.USER_INFO+userId);
            map.put("createTime", sdf.format(jedisService.zscore(BicycleConstants.CONNECT_MICROPHONE_WHITE_LIST,userId)));
            map.put("name",userMap.get("name"));
            map.put("phone", StringUtils.isNotBlank(userMap.get("bandPhone"))?userMap.get("bandPhone"):userMap.get("phone"));
            list.add(map);
        }
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("rows",list);
        return resultMap;
    }

    @RequestMapping("insertCMWhiteList")
    @ResponseBody
    public ResultData insertCMWhiteList(Long userId) {
        ResultData resultData = new ResultData();
        if(userId == null || userId <= 0){
            resultData.setResultCode("fail");
            resultData.setResultMessage("添加失败，用户信息不能为空！");
            return resultData;
        }
        Ruser ruser = ruserService.find(userId);
        if(ruser != null) {
            Long dateTime = System.currentTimeMillis();
            if (jedisService.isSortedSetMemberInShard(BicycleConstants.CONNECT_MICROPHONE_WHITE_LIST, String.valueOf(userId)) != null) {
                resultData.setResultCode("fail");
                resultData.setResultMessage("添加失败，已经在白名单中了！");
            } else {
                try {
                    jedisService.setValueToSortedSetInShard(BicycleConstants.CONNECT_MICROPHONE_WHITE_LIST, dateTime, String.valueOf(userId));
                    resultData.setResultCode("success");
                    resultData.setResultMessage("添加成功！");
                } catch (Exception e) {
                    resultData.setResultCode("fail");
                    resultData.setResultMessage("系统出现异常，添加失败！");
                }
            }
        }else{
            resultData.setResultCode("fail");
            resultData.setResultMessage("添加失败，用户信息不存在！");
        }
        return resultData;
    }

    @RequestMapping("removeCMWhiteList")
    @ResponseBody
    public ResultData removeCMWhiteList(Long userId) {
        ResultData resultData = new ResultData();
        Ruser ruser = ruserService.find(userId);
        if(ruser != null) {
            if (jedisService.isSortedSetMemberInShard(BicycleConstants.CONNECT_MICROPHONE_WHITE_LIST, String.valueOf(userId)) != null) {
                try {
                    jedisService.deleteSortedSetItemFromShard(BicycleConstants.CONNECT_MICROPHONE_WHITE_LIST, String.valueOf(userId));
                    resultData.setResultCode("success");
                    resultData.setResultMessage("移除成功！");
                } catch (Exception e) {
                    resultData.setResultCode("fail");
                    resultData.setResultMessage("系统出现异常，移除失败！");
                }
            } else {
                resultData.setResultCode("fail");
                resultData.setResultMessage("移除失败，此用户不在白名单中！");
            }
        }else{
            resultData.setResultCode("fail");
            resultData.setResultMessage("移除失败，用户信息不存在，不要淘气哦！");
        }
        return resultData;
    }

    @RequestMapping("insertCMWLCondition")
    @ResponseBody
    public ResultData insertCMWLCondition(String diamond){
        ResultData resultData = new ResultData();
        if(StringUtils.isBlank(diamond)){
            resultData.setResultCode("fail");
            resultData.setResultMessage("添加失败，请输入数字！");
            return resultData;
        }
        if(StringUtils.isNumeric(diamond)){
            try {
                jedisService.set(BicycleConstants.CONNECT_MICROPHONE_DIAMONDS,diamond);
                resultData.setResultCode("success");
                resultData.setResultMessage("添加成功！");
            }catch (Exception e){
                resultData.setResultCode("fail");
                resultData.setResultMessage("添加失败！");
            }
        }else{
            resultData.setResultCode("fail");
            resultData.setResultMessage("添加失败，请输入数字！");
        }
        return resultData;
    }

}
