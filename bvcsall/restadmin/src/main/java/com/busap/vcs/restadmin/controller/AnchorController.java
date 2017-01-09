package com.busap.vcs.restadmin.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.service.MessService;
import com.busap.vcs.webcomn.util.Client;
import com.busap.vcs.webcomn.util.SmsSendUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.AnchorService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;

/**
 * Created by huoshanwei on 2015/12/29.
 */
@Controller
@RequestMapping("/anchor")
public class AnchorController {

    @Resource
    private AnchorService anchorService;

    @Resource
    private RuserService ruserService;

    @Value("${files.localpath}")
    private String basePath;

    //直播审核页面
    @RequestMapping("forwardAnchorAuditingList")
    public ModelAndView forwardAnchorAuditingList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("anchor/query_anchor_auditing");
        return mav;
    }

    //查询直播审核列表信息
    @RequestMapping("queryAnchorAuditingList")
    @ResponseBody
    @EnablePaging
    public Map<String, Object> queryAnchorAuditingList(@ModelAttribute("queryPage") JQueryPage queryPage,
                                                       @RequestParam(value = "status",required = false) Integer status,
                                                       @RequestParam(value = "phone",required = false) String phone) {
        Map<String,Object> params = new HashMap<String, Object>(2);
        params.put("phone",phone);
        params.put("status",status);
        List<Anchor> list = anchorService.queryAnchors(params);
        Page page = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }

    @RequestMapping("allowAnchorLiving")
    @ResponseBody
    public ResultData allowAnchorLiving(Long id) throws Throwable {
        ResultData resultData = new ResultData();
        Anchor anchor = anchorService.queryAnchor(id);
        if (anchor == null) {
            resultData.setResultCode("fail");
            resultData.setResultMessage("审核出错，此条记录不存在，请刷新后重试！");
            return resultData;
        }
        if(anchor.getStatus() == 1){
            resultData.setResultCode("fail");
            resultData.setResultMessage("此条记录已经审核通过了哦！");
            return resultData;
        }
        anchor.setStatus(1);
        Ruser r = ruserService.selectByPrimaryKey(anchor.getCreatorId());
        if (r == null) {
            resultData.setResultCode("fail");
            resultData.setResultMessage("审核出错，此条记录不存在哦，请刷新后重试！");
            return resultData;
        }
        r.setId(anchor.getCreatorId());//用户ID
        r.setIsAnchor(1);
        try {
            anchorService.allowAnchorLiving(anchor, r);
            String content = URLEncoder.encode("尊敬的用户：您的直播申请已经通过审核，登陆LIVE客户端开始您的直播吧！【巴士在线】", "utf8");
            SmsSendUtil.sendMsg(anchor.getPhone(), content);
            resultData.setResultCode("success");
            resultData.setResultMessage("审核通过成功！");
        } catch (Throwable t) {
            t.printStackTrace();
            resultData.setResultCode("fail");
            resultData.setResultMessage("审核出错，请稍后重试！");
        }
        return resultData;
    }

    @RequestMapping("rejectAnchorLiving")
    @ResponseBody
    public ResultData rejectAnchorLiving(Long id,String rejectReason) throws Throwable {
        ResultData resultData = new ResultData();
        Anchor anchor = anchorService.queryAnchor(id);
        if (anchor != null) {
            anchor.setStatus(-1);
            anchor.setRejectReason(rejectReason);
            int result = anchorService.updateByPrimaryKey(anchor);
            if (result > 0) {
                String content = URLEncoder.encode("尊敬的用户：很遗憾！您的直播申请没有通过审核，请登陆LIVE客户端查看具体信息。【巴士在线】", "utf8");
                SmsSendUtil.sendMsg(anchor.getPhone(), content);
                resultData.setResultCode("success");
                resultData.setResultMessage("审核不通过成功！");
            } else {
                resultData.setResultCode("fail");
                resultData.setResultMessage("审核出错，请稍后重试！");
            }
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("审核出错，此条记录不存在哦，请刷新后重试！");
        }
        return resultData;
    }

}
