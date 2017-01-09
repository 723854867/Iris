package com.busap.vcs.restadmin.controller;

import com.busap.vcs.data.model.ExportUserExchangeRecord;
import com.busap.vcs.data.model.UserExchangeRecord;
import com.busap.vcs.restadmin.utils.CommonUtils;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.ExchangeRecodeService;
import com.busap.vcs.service.utils.ExcelUtils;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.controller.CRUDController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by busap on 2016/4/13.
 */
@Controller
@RequestMapping(value = "userExchangeRecord")
public class UserExchangeRecordController extends CRUDController<UserExchangeRecord, Long> {

    @Resource
    private ExchangeRecodeService exchangeRecodeService;

    @Resource(name = "exchangeRecodeService")
    @Override
    public void setBaseService(BaseService<UserExchangeRecord, Long> baseService) {
        this.baseService = baseService;
    }

    @RequestMapping(value = "/forwardUserExchangeRecord")
    public ModelAndView forwardUserExchangeRecord() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("userExchangeRecord/exchange_record");
        return mav;
    }

    @EnablePaging
    @RequestMapping(value = "/queryUserExchangeRecord")
    @ResponseBody
    public Map<String, Object> queryUserExchangeRecord(@ModelAttribute("queryPage") JQueryPage queryPage,
                                                       @RequestParam(value = "userKey",required = false) Long userKey,
                                                       @RequestParam(value = "userKeyword",required = false) String userKeyword,
                                                       @RequestParam(value = "organizationId",required = false) Long organizationId,
                                                       @RequestParam(value = "status",required = false) String status,
                                                       @RequestParam(value = "channel",required = false) String channel,
                                                       @RequestParam(value = "startDate",required = false) String startDate,
                                                       @RequestParam(value = "endDate",required = false) String endDate){
        Long startTime = null;
        Long endTime = null;
        if(!StringUtils.isBlank(startDate)){
            startTime = Long.valueOf(CommonUtils.dateFormatUnixTime(startDate));
        }
        if(!StringUtils.isBlank(endDate)){
            endTime = Long.valueOf(CommonUtils.dateFormatUnixTime(endDate));
        }
        Page p = PagingContextHolder.getPage();
        PagingContextHolder.removePage();
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("userKey",userKey);
        params.put("userKeyword",userKeyword);
        params.put("organizationId",organizationId);
        params.put("status",status);
        params.put("channel",channel);
        params.put("startDate",startTime);
        params.put("endDate",endTime);
        Map<String,Object> result = exchangeRecodeService.queryGoldCoinAndGoldCount(params);
        Integer personCount = exchangeRecodeService.queryPersonCountByUserId(params);
        result.put("personCount",personCount);
        PagingContextHolder.setPage(p);
        List<UserExchangeRecord> list = exchangeRecodeService.queryUserExchangeRecord(params);
        Page page = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        resultMap.put("result", result);
        return resultMap;
    }

    @RequestMapping(value = "exportUserExchangeRecord")
    public void exportUserExchangeRecord(@RequestParam(value = "userKey",required = false) Long userKey,
                                         @RequestParam(value = "userKeyword",required = false) String userKeyword,
                                         @RequestParam(value = "organizationId",required = false) Long organizationId,
                                         @RequestParam(value = "status",required = false) String status,
                                         @RequestParam(value = "channel",required = false) String channel,
                                         @RequestParam(value = "startDate",required = false) String startDate,
                                         @RequestParam(value = "endDate",required = false) String endDate,
                                         HttpServletResponse response)throws IOException{
        Long startTime = null;
        Long endTime = null;
        if(!StringUtils.isBlank(startDate)) {
            startTime = Long.valueOf(CommonUtils.dateFormatUnixTime(startDate));
        }
        if(!StringUtils.isBlank(endDate)) {
            endTime = Long.valueOf(CommonUtils.dateFormatUnixTime(endDate));
        }


        Map<String,Object> params = new HashMap<String, Object>();
        params.put("userKey",userKey);
        params.put("userKeyword",userKeyword);
        params.put("organizationId",organizationId);
        params.put("status",status);
        params.put("channel",channel);
        params.put("startDate",startTime);
        params.put("endDate",endTime);
        List<ExportUserExchangeRecord> list = exchangeRecodeService.queryExportUserExchangeRecord(params);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(ExportUserExchangeRecord exportUserExchangeRecord : list){
            exportUserExchangeRecord.setCreated(sdf.format(new Date(Long.parseLong(exportUserExchangeRecord.getCreated())*1000)));
        }
        ExcelUtils<ExportUserExchangeRecord> excelUtils = new ExcelUtils<ExportUserExchangeRecord>();
        String[] headers = {"ID", "昵称", "手机号","用户名",  "金额" ,"渠道", "状态", "金豆", "金币","结算时间"};
        response.reset();
        // 设定输出文件头
        response.setHeader("Content-disposition", "attachment; filename=exportUserExchangeRecord.xls");
        // 定义输出类型
        response.setContentType("application/msexcel");
        OutputStream out = response.getOutputStream();
        excelUtils.exportExcel("用户结算记录", headers, list, out, "yyyy-MM-dd HH:mm:ss");
    }

}
