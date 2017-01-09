package com.busap.vcs.restadmin.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.entity.Settlement;
import com.busap.vcs.data.model.ExportSettlement;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.SettlementService;
import com.busap.vcs.service.utils.ExcelUtils;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;

/**
 * 主播结算
 * 
 * @author dmsong
 * 
 */
@Controller()
@RequestMapping("settlement")
public class SettlementController extends CRUDController<Settlement, Long> {

	private static final Logger logger = LoggerFactory.getLogger(SettlementController.class);

	@Resource
	private SettlementService settlementService;

	@Resource(name = "settlementService")
	@Override
	public void setBaseService(BaseService<Settlement, Long> baseService) {
		this.baseService = baseService;
	}

	@RequestMapping("settlementlist")
	public String list(){
		
		return "settlement/settlement_list";
	}
	
	//查询拍币列表信息
    @RequestMapping("querySettlementList")
    @ResponseBody
    @EnablePaging
    public Map<String, Object> querySettlementList(@ModelAttribute("queryPage") JQueryPage queryPage,
                                                     @ModelAttribute("key") String key,
                                                     @ModelAttribute("value")String value,
                                                     @ModelAttribute("status")String status,
                                                     @ModelAttribute("startDate")String startDate,
                                                     @ModelAttribute("endDate")String endDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)){
        	params.put("key", key);
        	params.put("value", value);
        }
        if(StringUtils.isNotBlank(status)){
        	params.put("status", status);
        }
        if(StringUtils.isNotBlank(startDate)){
        	params.put("startDate", startDate);
        }
        if(StringUtils.isNotBlank(endDate)){
        	params.put("endDate", endDate);
        }
        List<Map<String,Object>> list = settlementService.querySettlement(params);
        Page page = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }

	@RequestMapping("doApprove")
	@ResponseBody
	public RespBody doApprove(Long id){
		Settlement sm = settlementService.find(id);
		sm.setApproverId(U.getUid());
		sm.setStatus(1);
		sm.setApproveTime(new Date());
		
		settlementService.save(sm);
		
		return this.respBodyWriter.toSuccess();
	}

	@RequestMapping("doSettle")
	@ResponseBody
	public RespBody doSettlement(Long id){
		Settlement sm = settlementService.find(id);
		sm.setSettlerId(U.getUid());
		sm.setStatus(2);
		sm.setSettleTime(new Date());
		sm.setRmb(sm.getPoints()/50);
		sm.setExtr("50:1");
		
		try {
			settlementService.doSettle(sm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return this.respBodyWriter.toError("结算用户不存在或结算数据不正确");
		}
		
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("showDetail")
	@ResponseBody
	public RespBody showDetail(Long id){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		List<ExportSettlement> list = settlementService.queryExportSettlementRecord(params);
		if(list!=null && list.size()>0){
			return this.respBodyWriter.toSuccess(list.get(0));
		}
		
		return this.respBodyWriter.toError("获取失败");
	}

	//查询拍币列表信息
    @RequestMapping("exportSettlement")
    public void exportToExcel(@RequestParam(value = "key",required = false)  String key,
    		@RequestParam(value = "value",required = false)String value,
    		@RequestParam(value = "status",required = false)String status,
    		@RequestParam(value = "startDate",required = false)String startDate,
    		@RequestParam(value = "endDate",required = false)String endDate,HttpServletResponse response) throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)){
        	params.put("key", key);
        	params.put("value", value);
        }
        if(StringUtils.isNotBlank(status)){
        	params.put("status", status);
        }
        if(StringUtils.isNotBlank(startDate)){
        	params.put("startDate", startDate);
        }
        if(StringUtils.isNotBlank(endDate)){
        	params.put("endDate", endDate);
        }
        List<ExportSettlement> list = settlementService.queryExportSettlementRecord(params);
        ExcelUtils<ExportSettlement> excelUtils = new ExcelUtils<ExportSettlement>();
        String[] headers = {"主播id","昵称","用户名","手机号","结算点数","创建时间","创建人","审核时间","审核人","结算时间","结算人","状态"};
        response.reset();
        // 设定输出文件头
        response.setHeader("Content-disposition", "attachment; filename=anchor_income_record.xls");
        // 定义输出类型
        response.setContentType("application/msexcel");
        OutputStream out = response.getOutputStream();
        excelUtils.exportExcel("主播结算",headers,list,out,"yyyy-MM-dd HH:mm:ss");
    }
    
    @RequestMapping("getTotalSettlement")
    @ResponseBody
    public RespBody getTotalSettlement(@RequestParam(value = "key",required = false)  String key,
    		@RequestParam(value = "value",required = false)String value,
    		@RequestParam(value = "status",required = false)String status,
    		@RequestParam(value = "startDate",required = false)String startDate,
    		@RequestParam(value = "endDate",required = false)String endDate){
    	 Map<String, Object> params = new HashMap<String, Object>();
         if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)){
         	params.put("key", key);
         	params.put("value", value);
         }
         if(StringUtils.isNotBlank(status)){
         	params.put("status", status);
         }
         if(StringUtils.isNotBlank(startDate)){
         	params.put("startDate", startDate);
         }
         if(StringUtils.isNotBlank(endDate)){
         	params.put("endDate", endDate);
         }
         Long total = settlementService.getTotalSettlement(params);
         
         return new RespBody(true,"",total==null?0:total);
    }
}
