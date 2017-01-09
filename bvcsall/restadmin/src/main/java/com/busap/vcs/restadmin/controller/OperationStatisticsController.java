package com.busap.vcs.restadmin.controller;

import com.busap.vcs.base.Constants;
import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Diamond;
import com.busap.vcs.data.entity.Gift;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.model.DiamondDisplay;
import com.busap.vcs.restadmin.utils.CommonUtils;
import com.busap.vcs.restadmin.utils.EnableFunction;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.DiamondService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by busap on 2015/12/23.
 */
@Controller
@RequestMapping("/operationSta")
public class OperationStatisticsController extends CRUDController<Diamond, Long> {

    @Value("${files.localpath}")
    private String basePath;

    @Resource(name = "jedisService")
    private JedisService jedisService;

    @Resource
    private DiamondService diamondService;
    
    @Resource(name = "ruserService")
	private RuserService ruserService;

    @Override
    public void setBaseService(BaseService<Diamond, Long> baseService) {
        this.baseService = baseService;
    }

    @RequestMapping("operationStas")
	public String consumeRecordList(HttpServletRequest req,
			@RequestParam(value = "oId", required = false)  Long oId,
    		@RequestParam(value = "gId", required = false) Long gId
    		) {
		
		
        
		
		return "operationSta/list";
	}
    
    @RequestMapping("diamondMonthRemainingList")
    @ResponseBody
	public Map<String,Object> diamondMonthRemainingList(HttpServletRequest req,
			@RequestParam(value = "page", required = false)  Long page,
    		@RequestParam(value = "rows", required = false)  Long rows
    		) throws Exception {
		
    	if(page==null) {
			page=1l;
        }
        if(rows==null) {
        	rows=20l;
        }
        
        HashMap<String,Object> map = new HashMap<String, Object>();
        
		StringBuffer jpql = new StringBuffer();
		StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append(" FROM DiamondMonthRemaining dmr ");
        countJpql.append("SELECT COUNT(*)  FROM  DiamondMonthRemaining  ");
        
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("createDate",1,"dmr");
        orderByList.add(orderByObject);
        
        
        Long totalCount=ruserService.getObjectCountByJpql(countJpql, paramList);
        
        List dmrList=ruserService.getObjectByJpql(jpql, page.intValue(), rows.intValue(), "dmr", paramList, null, orderByList);
        
        
        
		
        map.put("total",totalCount);
		map.put("rows",dmrList);
		return map;
	}

}
