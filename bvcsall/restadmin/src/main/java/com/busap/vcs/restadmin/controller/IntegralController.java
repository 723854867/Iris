package com.busap.vcs.restadmin.controller;

import com.busap.vcs.base.IntegralStatus;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Integral;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.IntegralStaDTO;
import com.busap.vcs.data.entity.IntegralStaSumDTO;
import com.busap.vcs.data.enums.TaskTypeSecondEnum;
import com.busap.vcs.data.model.IntegralStaDisplay;
import com.busap.vcs.data.vo.*;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.*;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 积分
 * Created by Yangxinyu on 15/9/29.
 */
@Controller
@RequestMapping("/integral")
public class IntegralController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    private IntegralService integralService;

    @Resource(name = "respBodyBuilder")
    private RespBodyBuilder respBodyWriter = new RespBodyBuilder();

    @Resource(name="ruserService")
    private RuserService ruserService;

    private Logger logger = LoggerFactory.getLogger(IntegralController.class);

    /**
     * 查询这个用户的任务状态
     * 顺序：限时 > 一次性 > 日常
     * @return 任务列表
     */
    @RequestMapping("/findTasks")
    @ResponseBody
    public RespBody findTaskListHandler() {
        List<UserTaskVO> userTaskVOs = integralService.getAllTaskStatusByUserId(118940l);
        // 过滤前置任务
        List<UserTaskVO> resultTaskVOs = integralService.taskVOFilter(userTaskVOs);
        return respBodyWriter.toSuccess(resultTaskVOs);
    }

    /**
     * 领取积分
     * @param integralId 积分ID
     * @return 领取后用户的积分状态
     */
    @RequestMapping("/receiveIntegral")
    @ResponseBody
    public RespBody receiveIntegralHandler(Long integralId) {
        if (integralId == null) {
            return respBodyWriter.toError(ResponseCode.CODE_450.toString(), ResponseCode.CODE_450.toCode());
        }
        IntegralVO integralVO = integralService.receiveIntegral(integralId);
        if (integralVO == null) {
            return respBodyWriter.toError(ResponseCode.CODE_602.toString(), ResponseCode.CODE_602.toCode());
        } else {
            logger.info("receiveIntegral  integralVO=" + JSONArray.fromObject(integralVO).toString());
            return respBodyWriter.toSuccess(integralVO);
        }
    }

    /**
     * 获取用户签到记录详情
     * @return 签到列表
     */
    @RequestMapping("/findUserSign")
    @ResponseBody
    public RespBody findUserSignList() {
        String uid = this.request.getHeader("uid");
        if (StringUtils.isBlank(uid)){
            return respBodyWriter.toError(ResponseCode.CODE_453.toString(), ResponseCode.CODE_453.toCode());
        } else if (!StringUtils.isNumeric(uid)) {
            return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
        }
        Long userId = Long.parseLong(uid);
        List<Integral> integralList = integralService.findSignIntegralList(userId, TaskTypeSecondEnum.sign, IntegralStatus.RECEIVE);
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();

        for (Integral integral : integralList) {
            String date = new SimpleDateFormat("dd").format(integral.getModifyTime());
            Map<String, String> map = new HashMap<String, String>();
            map.put("date", date);
            map.put("integral", String.valueOf(integral.getIntegralNum()));
            mapList.add(map);
        }
        UserSignDetailVO  userSignVO = new UserSignDetailVO();

        Integer integralSum = integralService.getUserIntegralSum(userId);
        if (integralSum == null) {
            return respBodyWriter.toError("没有找到这个用户，ID=" + userId, -1);
        } else {
            userSignVO.setIntegralSum(integralSum);
        }
        userSignVO.setUserId(userId);
        userSignVO.setSign(mapList);
        return respBodyWriter.toSuccess(userSignVO);
    }

    /**
     * 查询用户积分列表
     * @param userId 用户id
     * @param page 页码
     * @param size 每页条数
     * @return list
     */
    @RequestMapping("/findUserIntegralDetail")
    @ResponseBody
    public RespBody findUserIntegralDetailListHandler(Long userId, Integer page, Integer size) {
        Ruser ruser = ruserService.find(userId);
        if (ruser == null) {
            return respBodyWriter.toError(ResponseCode.CODE_451.toString(), ResponseCode.CODE_451.toCode());
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("sum", ruser.getSignSum() == null ? 0 : ruser.getSignSum());
            List<IntegralDetailVO> list = integralService.findUserIntegralDetailList(userId, page, size);
            resultMap.put("detail", list == null ? new ArrayList<IntegralDetailVO>() : list);
            return respBodyWriter.toSuccess(resultMap);
        }
    }
    
    @RequestMapping("/integralMultiTypeStatistics")
    public String integralMultiTypeStatistics(HttpServletRequest request) {
        List<IntegralStaDTO> list = integralService.integralMultiTypeStatistics();
        
        Map<String,IntegralStaSumDTO> staMap =new TreeMap<String,IntegralStaSumDTO>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				return o2.compareTo(o1);
			}
		});
        for(IntegralStaDTO dto :list) {
        	
        	IntegralStaSumDTO sumDto=null;
        	if(staMap.get(dto.getCreateTime())==null) {
        		sumDto=new IntegralStaSumDTO();
        	}else {
        		sumDto=staMap.get(dto.getCreateTime());
        	}
        	
        	sumDto.setCreateTime(dto.getCreateTime());
        	
        	if(dto.getType().intValue()==TaskTypeSecondEnum.sign.getValue().intValue()) {
    			sumDto.setSignSum(sumDto.getSignSum()+dto.getSum());
    		}else {
    			sumDto.setDailySum(sumDto.getDailySum()+dto.getSum());
    		}
        	staMap.put(dto.getCreateTime(), sumDto);
        }
        
        request.setAttribute("staMap", staMap);
        return "integral/integralMultiTypeStatistics";
    }

    @RequestMapping("queryIntegralMultiTypeStatistics")
    @ResponseBody
    public Map<String,Object> queryIntegralMultiTypeStatistics(){
        Map<String,Object> map = new HashMap<String, Object>();
        List<IntegralStaDisplay> list = integralService.queryIntegralMultiTypeStatistics();
        map.put("rows",list);
        return map;
    }

}
