package com.busap.vcs.restadmin.controller;

import com.busap.vcs.data.entity.Prize;
import com.busap.vcs.data.entity.PrizeDetail;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.model.ActivityVideoUser;
import com.busap.vcs.data.model.PrizeDetailDisplay;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.PrizeDetailService;
import com.busap.vcs.service.PrizeService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huoshanwei on 2015/10/23.
 */
@Controller
@RequestMapping("prize")
public class PrizeController extends CRUDController<Prize, Long> {

    @Resource
    private PrizeService prizeService;

    @Resource
    private PrizeDetailService prizeDetailService;

    @Resource
    private RuserService ruserService;

    @Override
    public void setBaseService(BaseService<Prize, Long> baseService) {
        this.baseService = baseService;
    }


    @RequestMapping("insertPrize")
    @ResponseBody
    public ResultData insertPrize(Long activityId, String name, String startDate, String endDate, Long status) throws Throwable {
        ResultData resultData = new ResultData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Prize prize = new Prize();
        prize.setActivityId(activityId);
        prize.setName(name);
        prize.setStartDate(sdf.parse(startDate));
        prize.setEndDate(sdf.parse(endDate));
        prize.setStatus(status);
        prize.setCreatorId(U.getUid());
        prize.setDataFrom(DataFrom.移动麦视后台.getName());
        prize.setCreateDateStr(new Date());
        int result = prizeService.insertPrize(prize);
        if (result > 0) {
            resultData.setResultCode("success");
            resultData.setResultMessage("添加成功！");
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("添加失败！");
        }
        return resultData;
    }

    @RequestMapping("prizeDetail")
    @ResponseBody
    public ModelAndView prizeDetail(Long activityId, Long prizeId) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("activityId", activityId);
        mav.addObject("prizeId", prizeId);
        mav.setViewName("activity/query_prize_detail");
        return mav;
    }

    @RequestMapping("queryPrizeDetails")
    @ResponseBody
    @EnablePaging
    public Map<String, Object> queryPrizeDetails(@ModelAttribute("queryPage") JQueryPage queryPage,
                                                 @RequestParam("prizeId") Long prizeId,
                                                 @RequestParam(value = "user", required = false) Long user,
                                                 @RequestParam(value = "userKeyword", required = false) String userKeyword,
                                                 @RequestParam(value = "prizeLevel", required = false) Long prizeLevel) {
        PrizeDetailDisplay prizeDetailDisplay = new PrizeDetailDisplay();
        prizeDetailDisplay.setPrizeId(prizeId);
        if (prizeLevel != null) {
            prizeDetailDisplay.setPrizeLevel(prizeLevel);
        }
        if(user != null) {
            if (user == 1L) {
                prizeDetailDisplay.setUserId(Long.valueOf(userKeyword));
            } else if (user == 2L) {
                prizeDetailDisplay.setUsername(userKeyword);
            } else if (user == 3L) {
                prizeDetailDisplay.setName(userKeyword);
            } else if (user == 4L) {
                prizeDetailDisplay.setPhone(userKeyword);
            }
        }
        List<PrizeDetailDisplay> list = prizeDetailService.queryPrizeDetails(prizeDetailDisplay);
        Page page = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }

    @RequestMapping("querySelectUsers")
    @ResponseBody
    @EnablePaging
    public Map<String, Object> querySelectUsers(@ModelAttribute("queryPage") JQueryPage queryPage,
                                                Long activityId,
                                                @RequestParam(value = "user", required = false) Long user,
                                                @RequestParam(value = "userType", required = false) String userType,
                                                @RequestParam(value = "vipStat", required = false) Long vipStat,
                                                @RequestParam(value = "userKeyword", required = false) String userKeyword) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("activityId", activityId);
        params.put("type", userType);
        params.put("vipStat", vipStat);
        params.put("user", user);
        params.put("userKeyword", userKeyword);
        List<ActivityVideoUser> userList = prizeService.queryActivityVideoUsers(params);
        Page pageInfo = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", pageInfo.getTotalResult());
        resultMap.put("rows", userList);
        return resultMap;
    }

    @RequestMapping("insertPrizeDetail")
    @ResponseBody
    public ResultData insertPrizeDetail(@RequestParam("prizeId") Long prizeId,
                                        @RequestParam("prizeLevel") Long prizeLevel,
                                        @RequestParam("prizeLevelName") String prizeLevelName,
                                        @RequestParam("prizeName") String prizeName,
                                        @RequestParam("userIds") String userIds) {
        ResultData resultData = new ResultData();
        String[] uids = userIds.split(",");
        List<String> listArray = Arrays.asList(uids);
        List<PrizeDetail> list = new ArrayList<PrizeDetail>();
        Date date = new Date();
        for (String userId : listArray) {
            PrizeDetail prizeDetail = new PrizeDetail();
            prizeDetail.setPrizeId(prizeId);
            prizeDetail.setPrizeLevel(prizeLevel);
            prizeDetail.setPrizeLevelName(prizeLevelName);
            prizeDetail.setPrizeName(prizeName);
            prizeDetail.setUserId(Long.valueOf(userId));
            prizeDetail.setCreateDateStr(date);
            prizeDetail.setCreatorId(U.getUid());
            prizeDetail.setDataFrom(DataFrom.移动麦视后台.getName());
            list.add(prizeDetail);
        }
        int result = prizeDetailService.batchInsertPrizeDetail(list);
        if (result > 0) {
            resultData.setResultCode("success");
            resultData.setResultMessage("添加成功！");
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("添加失败！");
        }
        return resultData;
    }

    @RequestMapping("queryPrizeById")
    @ResponseBody
    public ResultData queryPrizeById(@RequestParam("prizeId") Long prizeId) {
        ResultData resultData = new ResultData();
        Prize prize = prizeService.queryPrize(prizeId);
        if (prize != null) {
            resultData.setResultCode("success");
            resultData.setResultMessage("成功！");
            resultData.setData(prize);
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("失败！");
        }
        return resultData;
    }

    @RequestMapping("updatePrize")
    @ResponseBody
    public ResultData updatePrize(@ModelAttribute("prize") Prize prize) {
        ResultData resultData = new ResultData();
        Prize prizeInfo = prizeService.queryPrize(prize.getId());
        if (prizeInfo != null) {
            int result = prizeService.updatePrize(prize);
            if (result > 0) {
                resultData.setResultCode("success");
                resultData.setResultMessage("更新成功！");
            } else {
                resultData.setResultCode("fail");
                resultData.setResultMessage("更新失败！");
            }
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("更新失败！");
        }
        return resultData;
    }

    @RequestMapping("deletePrize")
    @ResponseBody
    public ResultData deletePrize(@RequestParam("id") Long id) {
        ResultData resultData = new ResultData();
        Prize prizeInfo = prizeService.queryPrize(id);
        if (prizeInfo != null) {
            PrizeDetailDisplay prizeDetail = new PrizeDetailDisplay();
            prizeDetail.setPrizeId(id);
            List<PrizeDetailDisplay> prizeDetailList = prizeDetailService.queryPrizeDetails(prizeDetail);//判断中奖详情中是否有相关联信息，有则不执行删除
            if (prizeDetailList.isEmpty()) {
                int result = prizeService.deletePrizeById(id);
                if (result > 0) {
                    resultData.setResultCode("success");
                    resultData.setResultMessage("删除成功！");
                } else {
                    resultData.setResultCode("fail");
                    resultData.setResultMessage("删除失败！");
                }
            } else {
                resultData.setResultCode("fail");
                resultData.setResultMessage("删除失败，该中奖活动下有关联的中奖信息，请先删除相关信息！");
            }
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("删除失败！");
        }
        return resultData;
    }

    @RequestMapping("deletePrizeDetail")
    @ResponseBody
    public ResultData deletePrizeDetail(@RequestParam("id") Long id) {
        ResultData resultData = new ResultData();
        PrizeDetail prizeDetail = prizeDetailService.queryPrizeDetail(id);
        if (prizeDetail != null) {
            int result = prizeDetailService.deletePrizeDetailById(id);
            if (result > 0) {
                resultData.setResultCode("success");
                resultData.setResultMessage("删除成功！");
            } else {
                resultData.setResultCode("fail");
                resultData.setResultMessage("删除失败！");
            }
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("删除失败！");
        }
        return resultData;
    }

    @RequestMapping("queryPrizeDetailById")
    @ResponseBody
    public ResultData queryPrizeDetailById(@RequestParam("id") Long id) {
        ResultData resultData = new ResultData();
        PrizeDetail prizeDetail = prizeDetailService.queryPrizeDetail(id);
        if (prizeDetail != null) {
            resultData.setResultCode("success");
            resultData.setResultMessage("成功！");
            resultData.setData(prizeDetail);
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("失败！");
        }
        return resultData;
    }

    @RequestMapping("updatePrizeDetail")
    @ResponseBody
    public ResultData updatePrizeDetail(@ModelAttribute("prizeDetail") PrizeDetail prizeDetail) {
        ResultData resultData = new ResultData();
        PrizeDetail prizeDetail1Info = prizeDetailService.queryPrizeDetail(prizeDetail.getId());
        if (prizeDetail1Info != null) {
            int result = prizeDetailService.updatePrizeDetail(prizeDetail);
            if (result > 0) {
                resultData.setResultCode("success");
                resultData.setResultMessage("更新成功！");
            } else {
                resultData.setResultCode("fail");
                resultData.setResultMessage("更新失败！");
            }
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("更新失败！");
        }
        return resultData;
    }

}
