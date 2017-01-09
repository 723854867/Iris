package com.busap.vcs.restadmin.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.busap.vcs.data.entity.*;
import com.busap.vcs.restadmin.utils.CommonUtils;
import com.busap.vcs.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.service.impl.SolrWoPaiTagService;
import com.busap.vcs.service.utils.CSVUtils;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.webcomn.controller.CRUDController;


/**
 * 视频
 *
 * @author meizhiwen
 *
 */
@Controller()
@RequestMapping("orgSettlement")
public class OrgSettlementController extends CRUDController<OrgSettlement, Long> {

	private static final Logger logger = LoggerFactory.getLogger(OrgSettlementController.class);

	@Resource(name = "orgSettlementService")
	private OrgSettlementService orgSettlementService;


	@Value("${files.localpath}")
	private String basePath;

	@Value("${uploadfile_remote_url}")
	private String uploadfile_remote_url;

	@Resource(name = "activityService")
	@Override
	public void setBaseService(BaseService<OrgSettlement, Long> baseService) {
		this.baseService = baseService;
	}

	@Value("${uploadpic_url_prefix}")
	private String uploadpic_url_prefix;

	@Value("${video_play_url_prefix}")
	private String video_play_url_prefix;

	@Value("${inner.actives}")
	private String innerActives;

	@Value("${solr.zookeeper.host}")
	private String solrZKHost;

	@Value("${solr.default.collection}")
	private String solrDefaultCllection;

	@Resource(name = "solrWoPaiTagService")
	private SolrWoPaiTagService solrWoPaiTagService;

	@Resource
	private EvaluationService evaluationService;


	@RequestMapping("orgSettlementList")
	public String orgSettlementList(HttpServletRequest req
    		) throws Exception {
		
		StringBuffer jpql1 = new StringBuffer();
        List<ParameterBean> paramList1=new ArrayList<ParameterBean>();
        
        jpql1.append(" FROM Organization o  ");
        
		
		List<OrderByBean> orderByList1=new ArrayList<OrderByBean>();
        OrderByBean orderByObject1=new OrderByBean("createDate",1,"o");
        orderByList1.add(orderByObject1);
        
        try {
			List oList=orgSettlementService.getObjectByJpql(jpql1, 0, 10000, "o", paramList1, null, orderByList1);
			
			req.setAttribute("oList", oList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
		return "orgSettlement/orgSettlementList";
	}
	
	@RequestMapping("findOrgSettlementList")
	@ResponseBody
    public Map<String,Object>  findOrgSettlementList(
			@RequestParam(value = "page", required = false)  Integer page,
    		@RequestParam(value = "rows", required = false)  Integer rows,
    		@RequestParam(value = "key", required = false)  String key,
    		@RequestParam(value = "value", required = false)  String value,
    		@RequestParam(value = "orgId", required = false)  String orgId,
    		@RequestParam(value = "settleMonth", required = false)  String settleMonth
    		) throws Exception {
    	
    	if(page==null) {
			page=1;
        }
        if(rows==null) {
        	rows=20;
        }
        
        if(settleMonth==null||settleMonth.equals("")) {
			return null;
		}
        
        StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("SELECT new Map(r.id as userId,r.name as name,r.phone as phone,o.orgName as orgName,os.points as points,os.amount as amount,os.realPoints as realPoints,os.realAmount as realAmount,os.id as id,os.status as status) FROM OrgSettlement os,Ruser r,Organization o where os.userId=r.id and os.orgId=o.id  ");
        countJpql.append("SELECT COUNT(*)  FROM  OrgSettlement os,Ruser r,Organization o where  os.userId=r.id and os.orgId=o.id     ");
        
        
        
        if(key!=null&&!key.equals("")&&value!=null&&!value.equals("")) {
        	
    		if(key.equals("1")) {
    			jpql.append(" AND r.id = :id ");
    			countJpql.append(" AND r.id = :id ");
    			
    			ParameterBean paramBean=new ParameterBean("id",Long.valueOf(value),null);
    			paramList.add(paramBean);
    		}else if(key.equals("2")) {
    			jpql.append(" AND r.name like :name ");
    			countJpql.append(" AND r.name like :name ");
    			
    			ParameterBean paramBean=new ParameterBean("name","%"+value+"%",null);
    			paramList.add(paramBean);
    		}else if(key.equals("3")) {
    			jpql.append(" AND r.phone = :phone ");
    			countJpql.append(" AND r.phone = :phone ");
    			
    			ParameterBean paramBean=new ParameterBean("phone",value,null);
    			paramList.add(paramBean);
    		}
    	}
        
        if(orgId!=null&&!orgId.equals("")) {
        	jpql.append(" AND o.id = :orgId ");
			countJpql.append(" AND o.id = :orgId ");
			
			ParameterBean paramBean=new ParameterBean("orgId",Long.valueOf(orgId),null);
			paramList.add(paramBean);
    	}
        
        if(settleMonth!=null&&!settleMonth.equals("")) {
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String str = settleMonth+"-32";
            Date date = df.parse(str);
            
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		Calendar cc2 = Calendar.getInstance();
    		cc2.setTime(date);
    		
    		cc2.set(cc2.get(Calendar.YEAR), cc2.get(Calendar.MONTH),1,0,0,0);
    		Date startDate=cc2.getTime();
    		
    		
    		int maxMonthDay = cc2.getActualMaximum(Calendar.DAY_OF_MONTH);
    		cc2.set(cc2.get(Calendar.YEAR), cc2.get(Calendar.MONTH),maxMonthDay,23,59,59);
    		Date endDate=cc2.getTime();
    		
//    		String end = sdf.format(cc2.getTime());
    		
//    		String start = sdf.format(cc2.getTime());
    		
    		jpql.append(" AND os.createDate >= :startDate AND os.createDate <=:endDate ");
			countJpql.append(" AND os.createDate >= :startDate AND os.createDate <=:endDate ");
			
			ParameterBean paramBean = new ParameterBean("startDate",startDate, TemporalType.TIMESTAMP);
			ParameterBean paramBean2 = new ParameterBean("endDate", endDate, TemporalType.TIMESTAMP);
			paramList.add(paramBean);
			paramList.add(paramBean2);
    		
        }
        
    	
        
        
        List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("createDate",1,"os");
        orderByList.add(orderByObject);
        
        
		
        Long totalCount=orgSettlementService.getObjectCountByJpql(countJpql, paramList);
        
        List orgSettlementList=orgSettlementService.getObjectByJpql(jpql, page, rows, "os", paramList, null, orderByList);
        

        HashMap<String,Object> map = new HashMap<String, Object>();
        
        map.put("total",totalCount);
		map.put("rows",orgSettlementList);

		return map;
	}

	
	 @RequestMapping("/findOrgSettlementListExport")
	    @ResponseBody
	    @EnablePaging
	    public void  findOrgSettlementListExport(
	    		@RequestParam(value = "page", required = false)  Integer page,
	    		@RequestParam(value = "size", required = false)  Integer size,
	    		@RequestParam(value = "key", required = false)  String key,
	    		@RequestParam(value = "value", required = false)  String value,
	    		@RequestParam(value = "orgId", required = false)  String orgId,
	    		@RequestParam(value = "settleMonth", required = false)  String settleMonth,
	    		HttpServletResponse response
	    		) throws Exception{ 
	        
		    
		 	if(settleMonth==null||settleMonth.equals("")) {
				return;
			}
	     
		 	if(page==null) {
				page=1;
	        }
	        if(size==null) {
	        	size=20;
	        }
	        
	        StringBuffer jpql = new StringBuffer();
	        StringBuffer countJpql = new StringBuffer();
	        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
	        
	        jpql.append("SELECT new Map(r.id as userId,r.name as name,r.phone as phone,o.orgName as orgName,os.points as points,os.amount as amount,os.realPoints as realPoints,os.realAmount as realAmount,os.id as id,os.status as status) FROM OrgSettlement os,Ruser r,Organization o where os.userId=r.id and os.orgId=o.id  ");
	        countJpql.append("SELECT COUNT(*)  FROM  OrgSettlement os,Ruser r,Organization o where  os.userId=r.id and os.orgId=o.id     ");
	        
	        
	        
	        if(key!=null&&!key.equals("")&&value!=null&&!value.equals("")) {
	        	
	    		if(key.equals("1")) {
	    			jpql.append(" AND r.id = :id ");
	    			countJpql.append(" AND r.id = :id ");
	    			
	    			ParameterBean paramBean=new ParameterBean("id",Long.valueOf(value),null);
	    			paramList.add(paramBean);
	    		}else if(key.equals("2")) {
	    			jpql.append(" AND r.name like :name ");
	    			countJpql.append(" AND r.name like :name ");
	    			
	    			ParameterBean paramBean=new ParameterBean("name","%"+value+"%",null);
	    			paramList.add(paramBean);
	    		}else if(key.equals("3")) {
	    			jpql.append(" AND r.phone = :phone ");
	    			countJpql.append(" AND r.phone = :phone ");
	    			
	    			ParameterBean paramBean=new ParameterBean("phone",value,null);
	    			paramList.add(paramBean);
	    		}
	    	}
	        
	        if(orgId!=null&&!orgId.equals("")) {
	        	jpql.append(" AND o.id = :orgId ");
				countJpql.append(" AND o.id = :orgId ");
				
				ParameterBean paramBean=new ParameterBean("orgId",Long.valueOf(orgId),null);
				paramList.add(paramBean);
	    	}
	        
	        if(settleMonth!=null&&!settleMonth.equals("")) {
	        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	            String str = settleMonth+"-32";
	            Date date = df.parse(str);
	            
	            
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    		Calendar cc2 = Calendar.getInstance();
	    		cc2.setTime(date);
	    		
	    		cc2.set(cc2.get(Calendar.YEAR), cc2.get(Calendar.MONTH),1,0,0,0);
	    		Date startDate=cc2.getTime();
	    		
	    		
	    		int maxMonthDay = cc2.getActualMaximum(Calendar.DAY_OF_MONTH);
	    		cc2.set(cc2.get(Calendar.YEAR), cc2.get(Calendar.MONTH),maxMonthDay,23,59,59);
	    		Date endDate=cc2.getTime();
	    		
//	    		String end = sdf.format(cc2.getTime());
	    		
//	    		String start = sdf.format(cc2.getTime());
	    		
	    		jpql.append(" AND os.createDate >= :startDate AND os.createDate <=:endDate ");
				countJpql.append(" AND os.createDate >= :startDate AND os.createDate <=:endDate ");
				
				ParameterBean paramBean = new ParameterBean("startDate",startDate, TemporalType.TIMESTAMP);
				ParameterBean paramBean2 = new ParameterBean("endDate", endDate, TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				paramList.add(paramBean2);
	    		
	        }
	        
	    	
	        
	        
	        List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
	        OrderByBean orderByObject=new OrderByBean("createDate",1,"os");
	        orderByList.add(orderByObject);
	        
	        
			
	        Long totalCount=orgSettlementService.getObjectCountByJpql(countJpql, paramList);
	        
	        List orgSettlementList=orgSettlementService.getObjectByJpql(jpql, 1, totalCount.intValue(), "os", paramList, null, orderByList);

			List cList = new ArrayList<Map>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        for (Object object : orgSettlementList) {
	        	Map consumeRecord=(Map) object;
				Map row = new LinkedHashMap<String, String>();
				row.put("1",consumeRecord.get("name"));
				row.put("2",consumeRecord.get("phone"));
				row.put("3",consumeRecord.get("orgName"));
				row.put("4",consumeRecord.get("status")!=null&&((Integer)consumeRecord.get("status"))==1?"已结算":"未结算");
				row.put("5",consumeRecord.get("points"));
				row.put("6",(Integer)consumeRecord.get("amount")/100d);
				row.put("7",consumeRecord.get("realPoints")!=null?consumeRecord.get("realPoints"):"");
				row.put("8",consumeRecord.get("realAmount")!=null?(Integer)consumeRecord.get("realAmount")/100d:"");
				row.put("9",consumeRecord.get("userId"));
	        	cList.add(row);
			}
			LinkedHashMap headers = new LinkedHashMap();
			headers.put("1", "用户名");
			headers.put("2", "手机号");
			headers.put("3", "机构");
			headers.put("4", "状态");
			headers.put("5", "应结金豆数");
			headers.put("6", "应结现金（元）");
			headers.put("7", "实结金豆数");
			headers.put("8", "实结现金（元）");
			headers.put("9", "用户编号");
			File file = CSVUtils.createCSVFile(cList, headers, basePath+"/exportExcel/", "机构结算");
			CommonUtils.download(file.getPath(),response);
    }
	 
	@RequestMapping(value = {"/doOrgSettlement"}, method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
	public String doOrgSettlement(@RequestParam(value = "orId", required = false)  Long orId) throws Exception {

        String rtn=orgSettlementService.doOrgSettlement(orId);
        
        return rtn;
	}
	
	@RequestMapping(value = {"/doOrgSettlements"}, method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
	public String doOrgSettlements(@RequestParam(value = "settleMonth", required = false)  String settleMonth) throws Exception {

		
		if(settleMonth!=null&&!settleMonth.equals("")) {
			StringBuffer jpql = new StringBuffer();
	        StringBuffer countJpql = new StringBuffer();
	        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
	        
	        jpql.append(" FROM OrgSettlement os where os.status=0  ");
	        countJpql.append("SELECT COUNT(*)  FROM  OrgSettlement os where  os.status=0     ");
	        
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String str = settleMonth+"-02";
            Date date = df.parse(str);
            
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		Calendar cc2 = Calendar.getInstance();
    		cc2.setTime(date);
    		
    		cc2.set(cc2.get(Calendar.YEAR), cc2.get(Calendar.MONTH),1,0,0,0);
    		Date startDate=cc2.getTime();
    		
    		
    		int maxMonthDay = cc2.getActualMaximum(Calendar.DAY_OF_MONTH);
    		cc2.set(cc2.get(Calendar.YEAR), cc2.get(Calendar.MONTH),maxMonthDay,23,59,59);
    		Date endDate=cc2.getTime();
    		
//    		String end = sdf.format(cc2.getTime());
    		
//    		String start = sdf.format(cc2.getTime());
    		
    		jpql.append(" AND os.createDate >= :startDate AND os.createDate <=:endDate ");
			countJpql.append(" AND os.createDate >= :startDate AND os.createDate <=:endDate ");
			
			ParameterBean paramBean = new ParameterBean("startDate",startDate, TemporalType.TIMESTAMP);
			ParameterBean paramBean2 = new ParameterBean("endDate", endDate, TemporalType.TIMESTAMP);
			paramList.add(paramBean);
			paramList.add(paramBean2);
			
			
			List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
	        OrderByBean orderByObject=new OrderByBean("createDate",1,"os");
	        orderByList.add(orderByObject);
	        
	        
			
	        Long totalCount=orgSettlementService.getObjectCountByJpql(countJpql, paramList);
	        
	        List orgSettlementList=orgSettlementService.getObjectByJpql(jpql, 1, totalCount.intValue(), "os", paramList, null, orderByList);
	        
	        if(orgSettlementList!=null&&orgSettlementList.size()>0) {
	        	for (Object object : orgSettlementList) {
	        		OrgSettlement orgSettlement=(OrgSettlement) object;
	        		orgSettlementService.doOrgSettlement(orgSettlement.getId());
					
				}
	        }
	        
    		
        }
		
		
        
		return "ok";
	}
	 
	

}
