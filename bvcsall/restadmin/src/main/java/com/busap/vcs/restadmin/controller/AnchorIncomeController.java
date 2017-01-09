package com.busap.vcs.restadmin.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.Settlement;
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.model.AnchorIncome;
import com.busap.vcs.restadmin.utils.EnableFunction;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.AnchorService;
import com.busap.vcs.service.SettlementService;
import com.busap.vcs.service.utils.ExcelUtils;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;

/**
 * Created by busap on 2015/12/29.
 */
@Controller
@RequestMapping("/anchor")
public class AnchorIncomeController {

    @Resource
    private AnchorService anchorService;

    @Resource
    private SettlementService settlementService;

    @Value("${files.localpath}")
    private String basePath;

    //拍币列表页跳转
    @RequestMapping("forwardAnchorIncomeList")
    public ModelAndView forwardAnchorIncomeList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("anchor/query_anchor_income");
        return mav;
    }

    //查询拍币列表信息
    @RequestMapping("queryAnchorIncomeList")
    @ResponseBody
    @EnablePaging
    @EnableFunction("主播收益管理,主播收益查询")
    public Map<String, Object> queryAnchorIncomeList(@ModelAttribute("queryPage") JQueryPage queryPage,
                                                     @ModelAttribute("key") String key,@ModelAttribute("value")String value) {
        Map<String, Object> params = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)){
        	params.put("key", key);
        	params.put("value", value);
        }
        
        List<AnchorIncome> list = anchorService.queryAnchorIncome(params);
        Page page = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }

    @RequestMapping("ajaxSettlementTemplate")
    public ModelAndView ajaxSettlementTemplate(Long id) {
        ModelAndView mav = new ModelAndView();
        Anchor anchor = anchorService.getAnchorByUserid(id);
        mav.addObject("anchor", anchor);
        mav.setViewName("anchor/settlement");
        return mav;
    }

    @RequestMapping("doAnchorSettlement")
    @ResponseBody
    @EnableFunction("主播收益管理,主播收益结算")
    public ResultData doAnchorSettlement(Long id, Integer settlementPointCount) throws Throwable {
        ResultData resultData = new ResultData();
        if (settlementPointCount <= 0) {
            resultData.setResultCode("fail");
            resultData.setResultMessage("结算点数必须大于0哦！");
            return resultData;
        }
        if (settlementPointCount.intValue()%50> 0) {
            resultData.setResultCode("fail");
            resultData.setResultMessage("结算点数必须是50的整数倍！");
            return resultData;
        }
        Anchor anchor = anchorService.queryAnchor(id);
        if (anchor == null) {
            resultData.setResultCode("fail");
            resultData.setResultMessage("系统出错，请刷新后重试！");
            return resultData;
        }
        if (settlementPointCount > anchor.getPointCount()) {
            resultData.setResultCode("fail");
            resultData.setResultMessage("结算点数不能大于当前点数哦！");
            return resultData;
        }
        
        anchor.setPointCount(anchor.getPointCount() - settlementPointCount);
        anchor.setLockPoints(settlementPointCount);
        
        Settlement settlement = new Settlement();
        settlement.setReciever(anchor.getCreatorId());
        settlement.setCreatorId(U.getUid());
        settlement.setPoints(settlementPointCount);
        settlement.setRmb(settlementPointCount/10);
        settlement.setStatus(0);
        settlement.setDataFrom(DataFrom.移动麦视后台.getName());
        try {
            anchorService.doAnchorSettlement(anchor, settlement);
            resultData.setResultCode("success");
            resultData.setResultMessage("结算成功！");
            return resultData;
        } catch (Throwable t) {
            t.printStackTrace();
            resultData.setResultCode("fail");
            resultData.setResultMessage("结算失败！");
            return resultData;
        }
    }

    @RequestMapping("querySettlementRecord")
    @ResponseBody
    @EnableFunction("主播收益管理,主播结算历史记录查询")
    @EnablePaging
    public Map<String,Object> querySettlementRecord(@ModelAttribute("queryPage") JQueryPage queryPage,
                                                    @RequestParam(value = "startDate",required = false) String startDate,
                                                    @RequestParam(value = "endDate",required = false) String endDate,
                                                    Long id){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("reciever",id);
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        List<Settlement> list = settlementService.queryAll(params);
        Page page = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }

    @EnableFunction("主播收益管理,导出主播收益历史纪录")
    @RequestMapping("exportSettlementRecordToExcel")
    public void exportSettlementRecordToExcel(@RequestParam(value = "key",required = false) String key,
    			@RequestParam(value = "value",required = false)String value,HttpServletResponse response)throws IOException {
    	 Map<String, Object> params = new HashMap<String, Object>();
         if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)){
         	params.put("key", key);
         	params.put("value", value);
         }
         
         List<AnchorIncome> list = anchorService.queryAnchorIncome(params);
         
        ExcelUtils<AnchorIncome> excelUtils = new ExcelUtils<AnchorIncome>();
        String[] headers = {"主播id","用户名","主播昵称","手机号","当前金豆数","结算中","已结算"};
        response.reset();
        // 设定输出文件头
        response.setHeader("Content-disposition", "attachment; filename=anchor_income_record.xls");
        // 定义输出类型
        response.setContentType("application/msexcel");
        OutputStream out = response.getOutputStream();
        excelUtils.exportExcel("主播收益",headers,list,out,"yyyy-MM-dd HH:mm:ss");
    }
    
    @RequestMapping("getTotal")
    @ResponseBody
    public RespBody getTotal(@RequestParam(value = "key",required = false) String key,
    			@RequestParam(value = "value",required = false)String value) {
    	 Map<String, Object> params = new HashMap<String, Object>();
         if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)){
         	params.put("key", key);
         	params.put("value", value);
         }
         
         Long total = anchorService.getTotalPoints(params);
         
        return new RespBody(true,"",total==null?0:total);
    }

}
