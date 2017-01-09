package com.busap.vcs.restadmin.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.*;
import com.busap.vcs.data.model.ExportSettlement;
import com.busap.vcs.restadmin.utils.CommonUtils;
import com.busap.vcs.service.*;
import com.busap.vcs.service.utils.CSVUtils;
import com.busap.vcs.service.utils.ExcelUtils;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.DateUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import com.busap.vcs.webcomn.controller.CRUDController;


/**
 * 消费记录
 *
 * @author zx
 *
 */
@Controller()
@RequestMapping("consumeRecord")
public class ConsumeRecordController  extends CRUDController<ConsumeRecord, Long> {

	private static final Logger logger = LoggerFactory
			.getLogger(ConsumeRecordController.class);
	
	@Resource(name = "consumeRecordService")
	private ConsumeRecordService consumeRecordService;
	
	@Resource(name = "respBodyBuilder")
    private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	
	@Resource(name="consumeRecordService")
    @Override
    public void setBaseService(BaseService<ConsumeRecord, Long> baseService) {
        this.baseService = baseService;
    }
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name="ruserService")
	private RuserService ruserService;
	
	@Value("${files.localpath}")
    private String basePath;

	@RequestMapping("consumeRecordList")
	public String consumeRecordList(HttpServletRequest req,
			@RequestParam(value = "oId", required = false)  Long oId,
    		@RequestParam(value = "gId", required = false) Long gId
    		) {
		
		StringBuffer jpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append(" FROM Gift g ");
        
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("createDate",1,"g");
        orderByList.add(orderByObject);
        
        
        try {
			List gList=consumeRecordService.getObjectByJpql(jpql, 0, 10000, "g", paramList, null, orderByList);
			
			req.setAttribute("gList", gList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        StringBuffer jpql1 = new StringBuffer();
        List<ParameterBean> paramList1=new ArrayList<ParameterBean>();
        
        jpql1.append(" FROM Organization o  ");
        
		
		List<OrderByBean> orderByList1=new ArrayList<OrderByBean>();
        OrderByBean orderByObject1=new OrderByBean("createDate",1,"o");
        orderByList1.add(orderByObject1);
        
        
        try {
			List oList=consumeRecordService.getObjectByJpql(jpql1, 0, 10000, "o", paramList1, null, orderByList1);
			
			req.setAttribute("oList", oList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        StringBuffer jpqlApp = new StringBuffer();
        List<ParameterBean> paramListApp=new ArrayList<ParameterBean>();
        
        jpqlApp.append(" select  distinct cr.appVersion FROM AppVersion cr  ");
        
		
		List<OrderByBean> orderByListApp=new ArrayList<OrderByBean>();
        OrderByBean orderByObjectApp=new OrderByBean("createDate",1,"cr");
        orderByListApp.add(orderByObjectApp);
        
        
        try {
			List appVersionList=consumeRecordService.getObjectByJpql(jpqlApp, 0, 1000, "cr", paramListApp, null, orderByListApp);
			
			req.setAttribute("appVersionList", appVersionList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        List<String> channelList = ruserService.selectAllRegPlatform();
        
        req.setAttribute("channelList", channelList);
		
		return "consumeRecord/list";
	}
	
	@RequestMapping("consumeRecordListSender")
	public String consumeRecordListSender(HttpServletRequest req,
			@RequestParam(value = "oId", required = false)  Long oId,
    		@RequestParam(value = "gId", required = false) Long gId
    		) {
		
		StringBuffer jpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append(" FROM Gift g ");
        
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("createDate",1,"g");
        orderByList.add(orderByObject);
        
        
        try {
			List gList=consumeRecordService.getObjectByJpql(jpql, 0, 10000, "g", paramList, null, orderByList);
			
			req.setAttribute("gList", gList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        StringBuffer jpql1 = new StringBuffer();
        List<ParameterBean> paramList1=new ArrayList<ParameterBean>();
        
        jpql1.append(" FROM Organization o  ");
        
		
		List<OrderByBean> orderByList1=new ArrayList<OrderByBean>();
        OrderByBean orderByObject1=new OrderByBean("createDate",1,"o");
        orderByList1.add(orderByObject1);
        
        try {
			List oList=consumeRecordService.getObjectByJpql(jpql1, 0, 10000, "o", paramList1, null, orderByList1);
			
			req.setAttribute("oList", oList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        StringBuffer jpqlApp = new StringBuffer();
        List<ParameterBean> paramListApp=new ArrayList<ParameterBean>();
        
        jpqlApp.append(" select  distinct cr.appVersion FROM AppVersion cr  ");
        
		
		List<OrderByBean> orderByListApp=new ArrayList<OrderByBean>();
        OrderByBean orderByObjectApp=new OrderByBean("createDate",1,"cr");
        orderByListApp.add(orderByObjectApp);
        
        
        try {
			List appVersionList=consumeRecordService.getObjectByJpql(jpqlApp, 0, 1000, "cr", paramListApp, null, orderByListApp);
			
			req.setAttribute("appVersionList", appVersionList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        List<String> channelList = ruserService.selectAllRegPlatform();
        
        req.setAttribute("channelList", channelList);
        
		
		return "consumeRecord/listSender";
	}
	

    @RequestMapping("/findConsumeRecordByUser")
    @ResponseBody
    @EnablePaging
    public Map<String,Object>  findOrderPayByUserId(
    		@RequestParam(value = "userId", required = false)  Long userId,
    		@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
    		@RequestParam(value = "page", required = false)  Long page,
    		@RequestParam(value = "rows", required = false)  Long rows,
    		@RequestParam(value = "oId", required = false)  Long oId,
    		@RequestParam(value = "gId", required = false) Long gId,
    		@RequestParam(value = "name", required = false) String name,
    		@RequestParam(value = "phone", required = false) String phone,
    		@RequestParam(value = "paramType", required = false) String paramType,
    		@RequestParam(value = "platform", required = false) String platform,
    		@RequestParam(value = "appVersion", required = false) String appVersion,
    		@RequestParam(value = "channel", required = false) String channel,
    		@RequestParam(value = "param", required = false) String param
    		) throws Exception{ 
        
	    
     
	    if(page==null) {
			page=1l;
        }
        if(rows==null) {
        	rows=10l;
        }
        
        HashMap<String,Object> map = new HashMap<String, Object>();
        
    	StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        StringBuffer jpqlSum = new StringBuffer();
        
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("select new Map( cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount,ro.platform as platform,ro.appVersion as appVersion,ro.channel as channel ) FROM ConsumeRecord cr,Ruser r,Room ro   WHERE    cr.creatorId=r.id and cr.roomId=ro.id  ");
        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Room ro    WHERE      cr.creatorId=r.id  and cr.roomId=ro.id     ");
        jpqlSum.append("SELECT sum(cr.points),sum(cr.inMoney)  FROM  ConsumeRecord cr,Ruser r,Room ro    WHERE      cr.creatorId=r.id  and cr.roomId=ro.id     ");
        
        
        if(oId!=null) {
        	jpql=new StringBuffer();
        	countJpql = new StringBuffer();
        	jpqlSum = new StringBuffer();
        	jpql.append("select new Map(  cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount,ro.platform as platform,ro.appVersion as appVersion,ro.channel as channel  ) FROM ConsumeRecord cr,Ruser r,Organization o,Ruser r2,Room ro    WHERE    cr.creatorId=r.id and o.id=r2.organizationId and o.id=:oId and r2.id=cr.reciever  and cr.roomId=ro.id   ");
            countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Organization o,Ruser r2,Room ro    WHERE      cr.creatorId=r.id   and o.id=r2.organizationId   and o.id=:oId and r2.id=cr.reciever  and cr.roomId=ro.id   ");
            jpqlSum.append("SELECT sum(cr.points),sum(cr.inMoney)  FROM  ConsumeRecord cr,Ruser r,Organization o,Ruser r2,Room ro    WHERE      cr.creatorId=r.id   and o.id=r2.organizationId   and o.id=:oId and r2.id=cr.reciever and cr.roomId=ro.id    ");
			
			ParameterBean paramBean=new ParameterBean("oId",oId,null);
			paramList.add(paramBean);
		}else {
			
		}
        
        if(paramType!=null&&!paramType.equals("")) {
        	if(paramType.equals("1")) {
        		
        		if(param!=null&&!param.equals("")) {
        			
        			jpql=new StringBuffer();
                	countJpql = new StringBuffer();
                	jpqlSum = new StringBuffer();
                	
                	if(oId!=null) {
                		jpql.append("select new Map(  cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount,ro.platform as platform,ro.appVersion as appVersion,ro.channel as channel  ) FROM ConsumeRecord cr,Ruser r,Organization o,Ruser r2,Room ro   WHERE    cr.creatorId=r.id and o.id=r2.organizationId and o.id=:oId and r2.id=cr.reciever  and cr.roomId=ro.id   ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Organization o,Ruser r2,Room ro    WHERE      cr.creatorId=r.id   and o.id=r2.organizationId   and o.id=:oId and r2.id=cr.reciever and cr.roomId=ro.id   ");
                        jpqlSum.append("SELECT sum(cr.points),sum(cr.inMoney)  FROM  ConsumeRecord cr,Ruser r,Organization o,Ruser r2,Room ro    WHERE      cr.creatorId=r.id   and o.id=r2.organizationId   and o.id=:oId and r2.id=cr.reciever and cr.roomId=ro.id   ");
            			
                	}else {
                		jpql.append("select new Map( cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount,ro.platform as platform,ro.appVersion as appVersion,ro.channel as channel  ) FROM ConsumeRecord cr,Ruser r,Ruser r2,Room ro   WHERE    cr.creatorId=r.id and r2.id=cr.reciever  and cr.roomId=ro.id    ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Ruser r2,Room ro    WHERE      cr.creatorId=r.id and r2.id=cr.reciever   and cr.roomId=ro.id    ");
                        jpqlSum.append("SELECT sum(cr.points),sum(cr.inMoney)  FROM  ConsumeRecord cr,Ruser r,Ruser r2,Room ro    WHERE      cr.creatorId=r.id and r2.id=cr.reciever   and cr.roomId=ro.id    ");
                    	
                	}
                	
                	
//        			jpql.append(" AND (r2.phone like :phone or r2.bandPhone like :phone )");
//        			countJpql.append(" AND (r2.phone like :phone or r2.bandPhone like :phone) ");
//        			
//        			ParameterBean paramBean=new ParameterBean("phone","%"+param+"%",null);
//        			paramList.add(paramBean);
        			
        			jpql.append(" AND (r2.phone = :phone or r.bandPhone = :phone )");
        			countJpql.append(" AND (r2.phone = :phone or r.bandPhone = :phone) ");
        			jpqlSum.append(" AND (r2.phone = :phone or r.bandPhone = :phone) ");
        			
        			ParameterBean paramBean=new ParameterBean("phone",param,null);
        			paramList.add(paramBean);
        		}else {
        			
        		}
        		
        	}else if(paramType.equals("2")) {
        		
        		if(param!=null&&!param.equals("")) {
        			
        			jpql=new StringBuffer();
                	countJpql = new StringBuffer();
                	jpqlSum = new StringBuffer();
                	
                	if(oId!=null) {
                		jpql.append("select new Map(  cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount,ro.platform as platform,ro.appVersion as appVersion,ro.channel as channel  ) FROM ConsumeRecord cr,Ruser r,Organization o,Ruser r2,Room ro   WHERE    cr.creatorId=r.id and o.id=r2.organizationId and o.id=:oId and cr.reciever=r2.id  and cr.roomId=ro.id   ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Organization o ,Ruser r2,Room ro   WHERE      cr.creatorId=r.id   and o.id=r2.organizationId   and o.id=:oId  and cr.reciever=r2.id  and cr.roomId=ro.id   ");
                        jpqlSum.append("SELECT sum(cr.points),sum(cr.inMoney)  FROM  ConsumeRecord cr,Ruser r,Organization o ,Ruser r2,Room ro   WHERE      cr.creatorId=r.id   and o.id=r2.organizationId   and o.id=:oId  and cr.reciever=r2.id  and cr.roomId=ro.id   ");
            			
                	}else {
                		jpql.append("select new Map( cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount,ro.platform as platform,ro.appVersion as appVersion,ro.channel as channel  ) FROM ConsumeRecord cr,Ruser r,Ruser r2,Room ro   WHERE    cr.creatorId=r.id and cr.reciever=r2.id   and cr.roomId=ro.id   ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r  ,Ruser r2,Room ro  WHERE      cr.creatorId=r.id  and cr.reciever=r2.id   and cr.roomId=ro.id    ");
                        jpqlSum.append("SELECT sum(cr.points),sum(cr.inMoney)  FROM  ConsumeRecord cr,Ruser r  ,Ruser r2,Room ro  WHERE      cr.creatorId=r.id  and cr.reciever=r2.id   and cr.roomId=ro.id    ");
                	}
                	
                	
        			jpql.append(" AND r2.name like :name ");
        			countJpql.append(" AND r2.name like :name ");
        			jpqlSum.append(" AND r2.name like :name ");
        			
        			ParameterBean paramBean=new ParameterBean("name","%"+param+"%",null);
        			paramList.add(paramBean);
        		}else {
        			
        		}
        		
        	}else if(paramType.equals("3")) {
        		if(param!=null) {
        			jpql.append(" AND cr.reciever = :userId ");
        			countJpql.append(" AND cr.reciever = :userId ");
        			jpqlSum.append(" AND cr.reciever = :userId ");
        			
        			ParameterBean paramBean=new ParameterBean("userId",Long.valueOf(param),null);
        			paramList.add(paramBean);
        		}else {
        			
        		}
        		
        	}
        	
        }
        
        
        
        if(gId!=null) {
			jpql.append(" AND cr.giftId = :giftId ");
			countJpql.append(" AND cr.giftId = :giftId ");
			jpqlSum.append(" AND cr.giftId = :giftId ");
			
			ParameterBean paramBean=new ParameterBean("giftId",gId,null);
			paramList.add(paramBean);
		}else {
			
		}
        
        if(platform!=null&&!platform.equals("")) {
        	jpql.append(" AND ro.platform = :platform ");
			countJpql.append(" AND ro.platform = :platform ");
			jpqlSum.append(" AND ro.platform = :platform ");
			
			ParameterBean paramBean=new ParameterBean("platform",platform,null);
			paramList.add(paramBean);
        }
        
        if(appVersion!=null&&!appVersion.equals("")) {
        	jpql.append(" AND ro.appVersion = :appVersion ");
			countJpql.append(" AND ro.appVersion = :appVersion ");
			jpqlSum.append(" AND ro.appVersion = :appVersion ");
			
			ParameterBean paramBean=new ParameterBean("appVersion",appVersion,null);
			paramList.add(paramBean);
        }
        
        
        if(channel!=null&&!channel.equals("")) {
        	jpql.append(" AND ro.channel = :channel ");
			countJpql.append(" AND ro.channel = :channel ");
			jpqlSum.append(" AND ro.channel = :channel ");
			
			ParameterBean paramBean=new ParameterBean("channel",channel,null);
			paramList.add(paramBean);
        }
        
        
        
        if(startDate!=null&&!"".equals(startDate) || endDate!=null&&!"".equals(endDate)) {
			if((startDate!=null&&!"".equals(startDate)) && (endDate!=null&&!"".equals(endDate))) {
				startDate+=" 00:00:00";
				endDate+=" 23:59:59";
				jpql.append(" AND cr.createDate >= :startDate AND cr.createDate <=:endDate ");
				countJpql.append(" AND cr.createDate >= :startDate AND cr.createDate <=:endDate ");
				jpqlSum.append(" AND cr.createDate >= :startDate AND cr.createDate <=:endDate ");
				
				ParameterBean paramBean = new ParameterBean("startDate",DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",startDate), TemporalType.TIMESTAMP);
				ParameterBean paramBean2 = new ParameterBean("endDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",endDate), TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				paramList.add(paramBean2);
				
				map.put("startDate", startDate);
				map.put("endDate", endDate);
				
			}else if(startDate!=null&&!"".equals(startDate)) {
				startDate+=" 00:00:00";
				jpql.append(" AND cr.createDate >= :startDate ");
				countJpql.append(" AND cr.createDate >= :startDate ");
				jpqlSum.append(" AND cr.createDate >= :startDate ");
				
				ParameterBean paramBean = new ParameterBean("startDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",startDate), TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				
				map.put("startDate", startDate);
			}else if(endDate!=null&&!"".equals(endDate)) {
				endDate+=" 23:59:59";
				jpql.append(" AND cr.createDate <= :endDate ");
				countJpql.append(" AND cr.createDate <= :endDate ");
				jpqlSum.append(" AND cr.createDate <= :endDate ");
				
				ParameterBean paramBean = new ParameterBean("endDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",endDate), TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				
				map.put("endDate", endDate);
			}
		}
        
        Set<String> userIds = jedisService.getSetFromShard(BicycleConstants.CONSUME_FORBID_USERS);
        if(userIds!=null&&userIds.size()>0) {
        	jpql.append(" AND cr.creatorId not in( :userIds) ");
    		countJpql.append(" AND cr.creatorId not  in( :userIds) ");
    		jpqlSum.append(" AND cr.creatorId  not in( :userIds) ");
    		Set<Long> newUsers=new HashSet<Long>();
    		for(String u :userIds) {
    			newUsers.add(Long.valueOf(u));
    		}
    		ParameterBean paramBean=new ParameterBean("userIds",newUsers,null);
    		paramList.add(paramBean);
        }
        
        jpql.append(" AND cr.reciever is not null ");
		countJpql.append(" AND cr.reciever is not null ");
		jpqlSum.append(" AND cr.reciever is not null ");
        
		
        
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("createDate",1,"cr");
        orderByList.add(orderByObject);
        
        
        List consumeRecordList=consumeRecordService.getObjectByJpql(jpql, page.intValue(), rows.intValue(), "cr", paramList, null, orderByList);
        if(consumeRecordList!=null&&consumeRecordList.size()>0) {
        	 if(paramType!=null&&!paramType.equals("")) {
             	if(paramType.equals("1")||paramType.equals("2")&&param!=null&&!param.equals("")) {
//             		for (Object object : consumeRecordList) {
//                		Map consumeRecord=(Map) object;
//                		consumeRecord.put("money",(Integer)consumeRecord.get("points")*divideRate);
//        			}
             	}else {
             		for (Object object : consumeRecordList) {
                		Map consumeRecord=(Map) object;
                		consumeRecord.put("nick",jedisService.getValueFromMap(BicycleConstants.USER_INFO+consumeRecord.get("reciever"), "name"));
//                		consumeRecord.put("money",(Integer)consumeRecord.get("points")*divideRate);
        				
        			}
             	}
        	 }else {
        		 for (Object object : consumeRecordList) {
             		Map consumeRecord=(Map) object;
             		consumeRecord.put("nick",jedisService.getValueFromMap(BicycleConstants.USER_INFO+consumeRecord.get("reciever"), "name"));
//             		consumeRecord.put("money",(Integer)consumeRecord.get("points")*divideRate);
     				
     			}
        	 }
        }
        
        
        Long totalCount=consumeRecordService.getObjectCountByJpql(countJpql, paramList);
        
        List consumeRecordListSum=consumeRecordService.getObjectByJpql(jpqlSum, 0, 100000000, "cr", paramList, null, null);
	    
        Long pSum=0l;
        Double imSum=0.0d;
        
        if(consumeRecordListSum!=null&&consumeRecordListSum.size()>0) {
        	if(consumeRecordListSum.get(0)!=null) {
        		Object[] objs=(Object[]) consumeRecordListSum.get(0);
        		if(objs[0]!=null) {
        			pSum=(Long) objs[0];
        		}
        		if(objs[1]!=null){
        			imSum=(Double) objs[1];
        		}
        		
        	}
        }
        
	    
        
		map.put("pSum",pSum);
		map.put("imSum",imSum);
        
		map.put("total",totalCount);
		map.put("rows",consumeRecordList);
		return map;
    } 
    
    
    @RequestMapping("/findConsumeRecordBySenderId")
    @ResponseBody
    @EnablePaging
    public Map<String,Object>  findConsumeRecordBySenderId(
    		@RequestParam(value = "userId", required = false)  Long userId,
    		@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
    		@RequestParam(value = "page", required = false)  Long page,
    		@RequestParam(value = "rows", required = false)  Long rows,
    		@RequestParam(value = "oId", required = false)  Long oId,
    		@RequestParam(value = "gId", required = false) Long gId,
    		@RequestParam(value = "name", required = false) String name,
    		@RequestParam(value = "phone", required = false) String phone,
    		@RequestParam(value = "paramType", required = false) String paramType,
    		@RequestParam(value = "platform", required = false) String platform,
    		@RequestParam(value = "appVersion", required = false) String appVersion,
    		@RequestParam(value = "channel", required = false) String channel,
    		@RequestParam(value = "param", required = false) String param
    		) throws Exception{ 
        
	    
     
	    if(page==null) {
			page=1l;
        }
        if(rows==null) {
        	rows=10l;
        }
        
        HashMap<String,Object> map = new HashMap<String, Object>();
        
    	StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        StringBuffer jpqlSum = new StringBuffer();
        StringBuffer userCountJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("select new Map( cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount,cr.platform as platform,cr.appVersion as appVersion,cr.channel as channel ) FROM ConsumeRecord cr,Ruser r   WHERE    cr.creatorId=r.id  ");
        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r    WHERE      cr.creatorId=r.id    ");
        userCountJpql.append("SELECT COUNT(DISTINCT cr.creatorId)  FROM  ConsumeRecord cr,Ruser r    WHERE      cr.creatorId=r.id    ");
        jpqlSum.append("SELECT sum(cr.diamondCount),sum(cr.diamondCount)/10.0  FROM  ConsumeRecord cr,Ruser r    WHERE      cr.creatorId=r.id    ");
        
        
        if(oId!=null) {
        	jpql=new StringBuffer();
        	countJpql = new StringBuffer();
        	jpqlSum = new StringBuffer();
        	userCountJpql = new StringBuffer();
        	jpql.append("select new Map(  cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount,cr.platform as platform,cr.appVersion as appVersion,cr.channel as channel ) FROM ConsumeRecord cr,Ruser r,Organization o   WHERE    cr.creatorId=r.id and o.id=r.organizationId and o.id=:oId  ");
            countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Organization o    WHERE      cr.creatorId=r.id   and o.id=r.organizationId   and o.id=:oId  ");
            userCountJpql.append("SELECT COUNT(DISTINCT cr.creatorId)  FROM  ConsumeRecord cr,Ruser r,Organization o    WHERE      cr.creatorId=r.id   and o.id=r.organizationId   and o.id=:oId  ");
            jpqlSum.append("SELECT sum(cr.diamondCount),sum(cr.diamondCount)/10.0  FROM  ConsumeRecord cr,Ruser r,Organization o    WHERE      cr.creatorId=r.id   and o.id=r.organizationId   and o.id=:oId  ");
			
			ParameterBean paramBean=new ParameterBean("oId",oId,null);
			paramList.add(paramBean);
		}else {
			
		}
        
        if(paramType!=null&&!paramType.equals("")) {
        	if(paramType.equals("1")) {
        		
        		if(param!=null&&!param.equals("")) {
        			
        			jpql=new StringBuffer();
                	countJpql = new StringBuffer();
                	userCountJpql = new StringBuffer();
                	jpqlSum = new StringBuffer();
                	
                	if(oId!=null) {
                		jpql.append("select new Map(  cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount,cr.platform as platform,cr.appVersion as appVersion,cr.channel as channel ) FROM ConsumeRecord cr,Ruser r,Organization o,Ruser r2   WHERE    cr.creatorId=r.id and o.id=r.organizationId and o.id=:oId and r2.id=cr.reciever  ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Organization o,Ruser r2    WHERE      cr.creatorId=r.id   and o.id=r.organizationId   and o.id=:oId and r2.id=cr.reciever ");
                        userCountJpql.append("SELECT COUNT(DISTINCT cr.creatorId)  FROM  ConsumeRecord cr,Ruser r,Organization o,Ruser r2    WHERE      cr.creatorId=r.id   and o.id=r.organizationId   and o.id=:oId and r2.id=cr.reciever ");
                        jpqlSum.append("SELECT sum(cr.diamondCount),sum(cr.diamondCount)/10.0  FROM  ConsumeRecord cr,Ruser r,Organization o,Ruser r2    WHERE      cr.creatorId=r.id   and o.id=r.organizationId   and o.id=:oId and r2.id=cr.reciever ");
            			
                        
                	}else {
                		jpql.append("select new Map( cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount,cr.platform as platform,cr.appVersion as appVersion,cr.channel as channel ) FROM ConsumeRecord cr,Ruser r,Ruser r2   WHERE    cr.creatorId=r.id and r2.id=cr.reciever   ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Ruser r2    WHERE      cr.creatorId=r.id and r2.id=cr.reciever    ");
                        userCountJpql.append("SELECT COUNT(DISTINCT cr.creatorId)  FROM  ConsumeRecord cr,Ruser r,Ruser r2    WHERE      cr.creatorId=r.id and r2.id=cr.reciever    ");
                        jpqlSum.append("SELECT sum(cr.diamondCount),sum(cr.diamondCount)/10.0  FROM  ConsumeRecord cr,Ruser r,Ruser r2    WHERE      cr.creatorId=r.id and r2.id=cr.reciever    ");
                	}
                	
                	
//        			jpql.append(" AND (r2.phone like :phone or r2.bandPhone like :phone )");
//        			countJpql.append(" AND (r2.phone like :phone or r2.bandPhone like :phone) ");
//        			
//        			ParameterBean paramBean=new ParameterBean("phone","%"+param+"%",null);
//        			paramList.add(paramBean);
        			
        			jpql.append(" AND (r.phone = :phone or r.bandPhone = :phone )");
        			countJpql.append(" AND (r.phone = :phone or r.bandPhone = :phone) ");
        			userCountJpql.append(" AND (r.phone = :phone or r.bandPhone = :phone) ");
        			jpqlSum.append(" AND (r.phone = :phone or r.bandPhone = :phone) ");
        			
        			ParameterBean paramBean=new ParameterBean("phone",param,null);
        			paramList.add(paramBean);
        		}else {
        			
        		}
        		
        	}else if(paramType.equals("2")) {
        		
        		if(param!=null&&!param.equals("")) {
        			
        			jpql=new StringBuffer();
                	countJpql = new StringBuffer();
                	userCountJpql = new StringBuffer();
                	jpqlSum = new StringBuffer();
                	
                	if(oId!=null) {
                		jpql.append("select new Map(  cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount,cr.platform as platform,cr.appVersion as appVersion,cr.channel as channel ) FROM ConsumeRecord cr,Ruser r,Organization o,Ruser r2   WHERE    cr.creatorId=r.id and o.id=r.organizationId and o.id=:oId and cr.reciever=r2.id  ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Organization o ,Ruser r2   WHERE      cr.creatorId=r.id   and o.id=r.organizationId   and o.id=:oId  and cr.reciever=r2.id  ");
                        userCountJpql.append("SELECT COUNT(DISTINCT cr.creatorId)  FROM  ConsumeRecord cr,Ruser r,Organization o ,Ruser r2   WHERE      cr.creatorId=r.id   and o.id=r.organizationId   and o.id=:oId  and cr.reciever=r2.id  ");
                        jpqlSum.append("SELECT sum(cr.diamondCount),sum(cr.diamondCount)/10.0  FROM  ConsumeRecord cr,Ruser r,Organization o ,Ruser r2   WHERE      cr.creatorId=r.id   and o.id=r.organizationId   and o.id=:oId  and cr.reciever=r2.id  ");
            			
                	}else {
                		jpql.append("select new Map( cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount,cr.platform as platform,cr.appVersion as appVersion,cr.channel as channel ) FROM ConsumeRecord cr,Ruser r,Ruser r2   WHERE    cr.creatorId=r.id and cr.reciever=r2.id   ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r  ,Ruser r2  WHERE      cr.creatorId=r.id  and cr.reciever=r2.id    ");
                        userCountJpql.append("SELECT COUNT(DISTINCT cr.creatorId)  FROM  ConsumeRecord cr,Ruser r  ,Ruser r2  WHERE      cr.creatorId=r.id  and cr.reciever=r2.id    ");
                        jpqlSum.append("SELECT sum(cr.diamondCount),sum(cr.diamondCount)/10.0  FROM  ConsumeRecord cr,Ruser r  ,Ruser r2  WHERE      cr.creatorId=r.id  and cr.reciever=r2.id    ");
                	}
                	
                	
        			jpql.append(" AND r.name like :name ");
        			countJpql.append(" AND r.name like :name ");
        			userCountJpql.append(" AND r.name like :name ");
        			jpqlSum.append(" AND r.name like :name ");
        			
        			ParameterBean paramBean=new ParameterBean("name","%"+param+"%",null);
        			paramList.add(paramBean);
        		}else {
        			
        		}
        		
        	}else if(paramType.equals("3")) {
        		if(param!=null) {
        			jpql.append(" AND cr.creatorId = :userId ");
        			countJpql.append(" AND cr.creatorId = :userId ");
        			userCountJpql.append(" AND cr.creatorId = :userId ");
        			jpqlSum.append(" AND cr.creatorId = :userId ");
        			
        			ParameterBean paramBean=new ParameterBean("userId",Long.valueOf(param),null);
        			paramList.add(paramBean);
        		}else {
        			
        		}
        		
        	}
        	
        }
        
        
        
        if(gId!=null) {
			jpql.append(" AND cr.giftId = :giftId ");
			countJpql.append(" AND cr.giftId = :giftId ");
			userCountJpql.append(" AND cr.giftId = :giftId ");
			jpqlSum.append(" AND cr.giftId = :giftId ");
			
			ParameterBean paramBean=new ParameterBean("giftId",gId,null);
			paramList.add(paramBean);
		}else {
			
		}
        
        if(platform!=null&&!platform.equals("")) {
        	jpql.append(" AND cr.platform = :platform ");
			countJpql.append(" AND cr.platform = :platform ");
			userCountJpql.append(" AND cr.platform = :platform ");
			jpqlSum.append(" AND cr.platform = :platform ");
			
			ParameterBean paramBean=new ParameterBean("platform",platform,null);
			paramList.add(paramBean);
        }
        
        if(appVersion!=null&&!appVersion.equals("")) {
        	jpql.append(" AND cr.appVersion = :appVersion ");
			countJpql.append(" AND cr.appVersion = :appVersion ");
			userCountJpql.append(" AND cr.appVersion = :appVersion ");
			jpqlSum.append(" AND cr.appVersion = :appVersion ");
			
			ParameterBean paramBean=new ParameterBean("appVersion",appVersion,null);
			paramList.add(paramBean);
        }
        
        if(channel!=null&&!channel.equals("")) {
        	jpql.append(" AND cr.channel = :channel ");
			countJpql.append(" AND cr.channel = :channel ");
			userCountJpql.append(" AND cr.channel = :channel ");
			jpqlSum.append(" AND cr.channel = :channel ");
			
			ParameterBean paramBean=new ParameterBean("channel",channel,null);
			paramList.add(paramBean);
        }
        
        
        
        
        if(startDate!=null&&!"".equals(startDate) || endDate!=null&&!"".equals(endDate)) {
			if((startDate!=null&&!"".equals(startDate)) && (endDate!=null&&!"".equals(endDate))) {
//				startDate+=" 00:00:00";
//				endDate+=" 23:59:59";
				jpql.append(" AND cr.createDate >= :startDate AND cr.createDate <=:endDate ");
				countJpql.append(" AND cr.createDate >= :startDate AND cr.createDate <=:endDate ");
				userCountJpql.append(" AND cr.createDate >= :startDate AND cr.createDate <=:endDate ");
				jpqlSum.append(" AND cr.createDate >= :startDate AND cr.createDate <=:endDate ");
				
				ParameterBean paramBean = new ParameterBean("startDate",DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",startDate), TemporalType.TIMESTAMP);
				ParameterBean paramBean2 = new ParameterBean("endDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",endDate), TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				paramList.add(paramBean2);
				
				map.put("startDate", startDate);
				map.put("endDate", endDate);
				
			}else if(startDate!=null&&!"".equals(startDate)) {
//				startDate+=" 00:00:00";
				jpql.append(" AND cr.createDate >= :startDate ");
				countJpql.append(" AND cr.createDate >= :startDate ");
				userCountJpql.append(" AND cr.createDate >= :startDate ");
				jpqlSum.append(" AND cr.createDate >= :startDate ");
				
				ParameterBean paramBean = new ParameterBean("startDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",startDate), TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				
				map.put("startDate", startDate);
			}else if(endDate!=null&&!"".equals(endDate)) {
//				endDate+=" 22:59:59";
				jpql.append(" AND cr.createDate <= :endDate ");
				countJpql.append(" AND cr.createDate <= :endDate ");
				userCountJpql.append(" AND cr.createDate <= :endDate ");
				jpqlSum.append(" AND cr.createDate <= :endDate ");
				
				ParameterBean paramBean = new ParameterBean("endDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",endDate), TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				
				map.put("endDate", endDate);
			}
		}
        
        Set<String> userIds = jedisService.getSetFromShard(BicycleConstants.CONSUME_FORBID_USERS);
        if(userIds!=null&&userIds.size()>0) {
        	jpql.append(" AND cr.creatorId not in( :userIds) ");
    		countJpql.append(" AND cr.creatorId not  in( :userIds) ");
    		userCountJpql.append(" AND cr.creatorId not  in( :userIds) ");
    		jpqlSum.append(" AND cr.creatorId  not in( :userIds) ");
    		Set<Long> newUsers=new HashSet<Long>();
    		for(String u :userIds) {
    			newUsers.add(Long.valueOf(u));
    		}
    		ParameterBean paramBean=new ParameterBean("userIds",newUsers,null);
    		paramList.add(paramBean);
        }
        
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("createDate",1,"cr");
        orderByList.add(orderByObject);
        
        
        List consumeRecordList=consumeRecordService.getObjectByJpql(jpql, page.intValue(), rows.intValue(), "cr", paramList, null, orderByList);
        if(consumeRecordList!=null&&consumeRecordList.size()>0) {
        	if(paramType!=null&&!paramType.equals("")) {
             	if(paramType.equals("1")||paramType.equals("2")&&param!=null&&!param.equals("")) {
//             		for (Object object : consumeRecordList) {
//                		Map consumeRecord=(Map) object;
//                		consumeRecord.put("money",(Integer)consumeRecord.get("points")*divideRate);
//        			}
             	}else {
             		for (Object object : consumeRecordList) {
                		Map consumeRecord=(Map) object;
                		consumeRecord.put("nick",jedisService.getValueFromMap(BicycleConstants.USER_INFO+consumeRecord.get("reciever"), "name"));
//                		consumeRecord.put("money",(Integer)consumeRecord.get("points")*divideRate);
        			}
             	}
        	 }else {
        		 for (Object object : consumeRecordList) {
              		Map consumeRecord=(Map) object;
              		consumeRecord.put("nick",jedisService.getValueFromMap(BicycleConstants.USER_INFO+consumeRecord.get("reciever"), "name"));
//              		consumeRecord.put("money",(Integer)consumeRecord.get("points")*divideRate);
      				
      			}
         	 }
        	
        }
        
        
        Long totalCount=consumeRecordService.getObjectCountByJpql(countJpql, paramList);
        
        Long totalUserCount=consumeRecordService.getObjectCountByJpql(userCountJpql, paramList);
        
        List consumeRecordListSum=consumeRecordService.getObjectByJpql(jpqlSum, 0, 100000000, "cr", paramList, null, null);
        
        Long dmSum=0l;
        Double mSum=0.0d;
        
        if(consumeRecordListSum!=null&&consumeRecordListSum.size()>0) {
        	if(consumeRecordListSum.get(0)!=null) {
        		Object[] objs=(Object[]) consumeRecordListSum.get(0);
        		if(objs[0]!=null) {
        			dmSum=(Long) objs[0];
        		}
        		if(objs[1]!=null){
        			mSum=(Double) objs[1];
        		}
        		
        	}
        }
        
	    
        
		map.put("total",totalCount);
		map.put("rows",consumeRecordList);
		map.put("dmSum",dmSum);
		map.put("mSum",mSum);
		map.put("uCount",totalUserCount);
		return map;
    } 
    
    @RequestMapping("/findConsumeRecordByUserExport")
    @ResponseBody
    @EnablePaging
    public void  findConsumeRecordByUserExport(
    		@RequestParam(value = "userId", required = false)  Long userId,
    		@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
    		@RequestParam(value = "page", required = false)  Long page,
    		@RequestParam(value = "rows", required = false)  Long rows,
    		@RequestParam(value = "oId", required = false)  Long oId,
    		@RequestParam(value = "gId", required = false) Long gId,
    		@RequestParam(value = "name", required = false) String name,
    		@RequestParam(value = "phone", required = false) String phone,
    		@RequestParam(value = "paramType", required = false) String paramType,
    		HttpServletResponse response,
    		@RequestParam(value = "param", required = false) String param
    		) throws Exception{ 
        
	    
     
	    if(page==null) {
			page=1l;
        }
        if(rows==null) {
        	rows=10l;
        }
        
        HashMap<String,Object> map = new HashMap<String, Object>();
        
    	StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        StringBuffer userCountJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
//        Date createDate,Integer number, Integer price, Integer diamondCount,
//		Integer points, String giftName, String name
		
        jpql.append("select new Map( cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount ) FROM ConsumeRecord cr,Ruser r   WHERE    cr.creatorId=r.id  ");
        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r    WHERE      cr.creatorId=r.id    ");
        
        
        if(oId!=null) {
        	jpql=new StringBuffer();
        	countJpql = new StringBuffer();
        	jpql.append("select new Map(  cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount ) FROM ConsumeRecord cr,Ruser r,Organization o,Ruser r2    WHERE    cr.creatorId=r.id and o.id=r2.organizationId and o.id=:oId  and r2.id=cr.reciever   ");
            countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Organization o,Ruser r2     WHERE      cr.creatorId=r.id   and o.id=r2.organizationId   and o.id=:oId  and r2.id=cr.reciever   ");
			
			ParameterBean paramBean=new ParameterBean("oId",oId,null);
			paramList.add(paramBean);
		}else {
			
		}
        
        if(paramType!=null&&!paramType.equals("")) {
        	if(paramType.equals("1")) {
        		
        		if(param!=null&&!param.equals("")) {
        			
        			jpql=new StringBuffer();
                	countJpql = new StringBuffer();
                	
                	if(oId!=null) {
                		jpql.append("select new Map(  cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount ) FROM ConsumeRecord cr,Ruser r,Organization o,Ruser r2   WHERE    cr.creatorId=r.id and o.id=r2.organizationId and o.id=:oId and r2.id=cr.reciever  ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Organization o,Ruser r2    WHERE      cr.creatorId=r.id   and o.id=r2.organizationId   and o.id=:oId and r2.id=cr.reciever ");
            			
                	}else {
                		jpql.append("select new Map( cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount ) FROM ConsumeRecord cr,Ruser r,Ruser r2   WHERE    cr.creatorId=r.id and r2.id=cr.reciever   ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Ruser r2    WHERE      cr.creatorId=r.id and r2.id=cr.reciever    ");
                	}
                	
                	
//        			jpql.append(" AND (r2.phone like :phone or r2.bandPhone like :phone )");
//        			countJpql.append(" AND (r2.phone like :phone or r2.bandPhone like :phone) ");
//        			
//        			ParameterBean paramBean=new ParameterBean("phone","%"+param+"%",null);
//        			paramList.add(paramBean);
        			
        			jpql.append(" AND (r2.phone = :phone or r.bandPhone = :phone )");
        			countJpql.append(" AND (r2.phone = :phone or r.bandPhone = :phone) ");
        			
        			ParameterBean paramBean=new ParameterBean("phone",param,null);
        			paramList.add(paramBean);
        		}else {
        			
        		}
        		
        	}else if(paramType.equals("2")) {
        		
        		if(param!=null&&!param.equals("")) {
        			
        			jpql=new StringBuffer();
                	countJpql = new StringBuffer();
                	
                	if(oId!=null) {
                		jpql.append("select new Map(  cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount ) FROM ConsumeRecord cr,Ruser r,Organization o,Ruser r2   WHERE    cr.creatorId=r.id and o.id=r2.organizationId and o.id=:oId and cr.reciever=r2.id  ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Organization o ,Ruser r2   WHERE      cr.creatorId=r.id   and o.id=r2.organizationId   and o.id=:oId  and cr.reciever=r2.id  ");
            			
                	}else {
                		jpql.append("select new Map( cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount ) FROM ConsumeRecord cr,Ruser r,Ruser r2   WHERE    cr.creatorId=r.id and cr.reciever=r2.id   ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r  ,Ruser r2  WHERE      cr.creatorId=r.id  and cr.reciever=r2.id    ");
                	}
                	
                	
        			jpql.append(" AND r2.name like :name ");
        			countJpql.append(" AND r2.name like :name ");
        			
        			ParameterBean paramBean=new ParameterBean("name","%"+param+"%",null);
        			paramList.add(paramBean);
        		}else {
        			
        		}
        		
        	}else if(paramType.equals("3")) {
        		if(param!=null) {
        			jpql.append(" AND cr.reciever = :userId ");
        			countJpql.append(" AND cr.reciever = :userId ");
        			
        			ParameterBean paramBean=new ParameterBean("userId",Long.valueOf(param),null);
        			paramList.add(paramBean);
        		}else {
        			
        		}
        		
        	}
        	
        }
        
        
        
        if(gId!=null) {
			jpql.append(" AND cr.giftId = :giftId ");
			countJpql.append(" AND cr.giftId = :giftId ");
			
			ParameterBean paramBean=new ParameterBean("giftId",gId,null);
			paramList.add(paramBean);
		}else {
			
		}
        
        
        
        
        if(startDate!=null&&!"".equals(startDate) || endDate!=null&&!"".equals(endDate)) {
			if((startDate!=null&&!"".equals(startDate)) && (endDate!=null&&!"".equals(endDate))) {
				startDate+=" 00:00:00";
				endDate+=" 23:59:59";
				jpql.append(" AND cr.createDate >= :startDate AND cr.createDate <=:endDate ");
				countJpql.append(" AND cr.createDate >= :startDate AND cr.createDate <=:endDate ");
				
				ParameterBean paramBean = new ParameterBean("startDate",DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",startDate), TemporalType.TIMESTAMP);
				ParameterBean paramBean2 = new ParameterBean("endDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",endDate), TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				paramList.add(paramBean2);
				
				map.put("startDate", startDate);
				map.put("endDate", endDate);
				
			}else if(startDate!=null&&!"".equals(startDate)) {
				startDate+=" 00:00:00";
				jpql.append(" AND cr.createDate >= :startDate ");
				countJpql.append(" AND cr.createDate >= :startDate ");
				
				ParameterBean paramBean = new ParameterBean("startDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",startDate), TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				
				map.put("startDate", startDate);
			}else if(endDate!=null&&!"".equals(endDate)) {
				endDate+=" 22:59:59";
				jpql.append(" AND cr.createDate <= :endDate ");
				countJpql.append(" AND cr.createDate <= :endDate ");
				
				ParameterBean paramBean = new ParameterBean("endDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",endDate), TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				
				map.put("endDate", endDate);
			}
		}
        
        Set<String> userIds = jedisService.getSetFromShard(BicycleConstants.CONSUME_FORBID_USERS);
        if(userIds!=null&&userIds.size()>0) {
        	jpql.append(" AND cr.creatorId not in( :userIds) ");
    		countJpql.append(" AND cr.creatorId not  in( :userIds) ");
    		Set<Long> newUsers=new HashSet<Long>();
    		for(String u :userIds) {
    			newUsers.add(Long.valueOf(u));
    		}
    		ParameterBean paramBean=new ParameterBean("userIds",newUsers,null);
    		paramList.add(paramBean);
        }
        
        jpql.append(" AND cr.reciever is not null ");
		countJpql.append(" AND cr.reciever is not null ");
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("createDate",1,"cr");
        orderByList.add(orderByObject);
        
        Long totalCount=consumeRecordService.getObjectCountByJpql(countJpql, paramList);
        
        List consumeRecordList=consumeRecordService.getObjectByJpql(jpql, 1, totalCount.intValue(), "cr", paramList, null, orderByList);
        if(consumeRecordList!=null&&consumeRecordList.size()>0) {
        	 if(paramType!=null&&!paramType.equals("")) {
             	if(paramType.equals("1")||paramType.equals("2")&&param!=null&&!param.equals("")) {
//             		for (Object object : consumeRecordList) {
//                		Map consumeRecord=(Map) object;
//                		consumeRecord.put("money",(Integer)consumeRecord.get("points"));
//        			}
             	}else {
             		for (Object object : consumeRecordList) {
                		Map consumeRecord=(Map) object;
                		consumeRecord.put("nick",jedisService.getValueFromMap(BicycleConstants.USER_INFO+consumeRecord.get("reciever"), "name"));
//                		consumeRecord.put("money",(Integer)consumeRecord.get("points"));
        				
        			}
             	}
        	 }else {
        		 for (Object object : consumeRecordList) {
              		Map consumeRecord=(Map) object;
              		consumeRecord.put("nick",jedisService.getValueFromMap(BicycleConstants.USER_INFO+consumeRecord.get("reciever"), "name"));
//              		consumeRecord.put("money",(Integer)consumeRecord.get("points"));
      				
      			}
         	 }
        }
        
        
        
//        List consumeRecordList=consumeRecordService.getObjectByJpql(jpql, 1, totalCount.intValue(), "cr", paramList, null, orderByList);

		List cList = new ArrayList<Map>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Object object : consumeRecordList) {
        	Map consumeRecord=(Map) object;
			Map row = new LinkedHashMap<String, String>();
			row.put("1",consumeRecord.get("nick"));
			row.put("2",consumeRecord.get("name"));
			row.put("3",consumeRecord.get("giftName"));
			row.put("4",consumeRecord.get("number"));
			row.put("5",sdf.format(consumeRecord.get("createDate")));
			row.put("6",consumeRecord.get("points"));
			row.put("7",consumeRecord.get("inMoney"));
        	cList.add(row);
		}
		LinkedHashMap headers = new LinkedHashMap();
		headers.put("1", "用户名");
		headers.put("2", "赠送者");
		headers.put("3", "道具名称");
		headers.put("4", "个数");
		headers.put("5", "赠送时间");
		headers.put("6", "折合金豆");
		headers.put("7", "折合现金（元）");
		File file = CSVUtils.createCSVFile(cList, headers, basePath+"/exportExcel/", "收取礼物明细");
		CommonUtils.download(file.getPath(),response);
    }

    @RequestMapping("/findConsumeRecordBySenderIdExport")
    @ResponseBody
    @EnablePaging
    public void  findConsumeRecordBySenderIdExport(
    		@RequestParam(value = "userId", required = false)  Long userId,
    		@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
    		@RequestParam(value = "page", required = false)  Long page,
    		@RequestParam(value = "rows", required = false)  Long rows,
    		@RequestParam(value = "oId", required = false)  Long oId,
    		@RequestParam(value = "gId", required = false) Long gId,
    		@RequestParam(value = "name", required = false) String name,
    		@RequestParam(value = "phone", required = false) String phone,
    		@RequestParam(value = "paramType", required = false) String paramType,
    		HttpServletResponse response,
    		@RequestParam(value = "param", required = false) String param
    		) throws Exception{ 
        
	    
     
	    if(page==null) {
			page=1l;
        }
        if(rows==null) {
        	rows=10l;
        }
        
        HashMap<String,Object> map = new HashMap<String, Object>();
        
    	StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("select new Map( cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount ) FROM ConsumeRecord cr,Ruser r   WHERE    cr.creatorId=r.id  ");
        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r    WHERE      cr.creatorId=r.id    ");
        
        
        if(oId!=null) {
        	jpql=new StringBuffer();
        	countJpql = new StringBuffer();
        	jpql.append("select new Map(  cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount ) FROM ConsumeRecord cr,Ruser r,Organization o   WHERE    cr.creatorId=r.id and o.id=r.organizationId and o.id=:oId  ");
            countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Organization o    WHERE      cr.creatorId=r.id   and o.id=r.organizationId   and o.id=:oId  ");
			
			ParameterBean paramBean=new ParameterBean("oId",oId,null);
			paramList.add(paramBean);
		}else {
			
		}
        
        if(paramType!=null&&!paramType.equals("")) {
        	if(paramType.equals("1")) {
        		
        		if(param!=null&&!param.equals("")) {
        			
        			jpql=new StringBuffer();
                	countJpql = new StringBuffer();
                	
                	if(oId!=null) {
                		jpql.append("select new Map(  cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount ) FROM ConsumeRecord cr,Ruser r,Organization o,Ruser r2   WHERE    cr.creatorId=r.id and o.id=r.organizationId and o.id=:oId and r2.id=cr.reciever  ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Organization o,Ruser r2    WHERE      cr.creatorId=r.id   and o.id=r.organizationId   and o.id=:oId and r2.id=cr.reciever ");
            			
                	}else {
                		jpql.append("select new Map( cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount ) FROM ConsumeRecord cr,Ruser r,Ruser r2   WHERE    cr.creatorId=r.id and r2.id=cr.reciever   ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Ruser r2    WHERE      cr.creatorId=r.id and r2.id=cr.reciever    ");
                	}
                	
                	
//        			jpql.append(" AND (r2.phone like :phone or r2.bandPhone like :phone )");
//        			countJpql.append(" AND (r2.phone like :phone or r2.bandPhone like :phone) ");
//        			
//        			ParameterBean paramBean=new ParameterBean("phone","%"+param+"%",null);
//        			paramList.add(paramBean);
        			
        			jpql.append(" AND (r.phone = :phone or r.bandPhone = :phone )");
        			countJpql.append(" AND (r.phone = :phone or r.bandPhone = :phone) ");
        			
        			ParameterBean paramBean=new ParameterBean("phone",param,null);
        			paramList.add(paramBean);
        		}else {
        			
        		}
        		
        	}else if(paramType.equals("2")) {
        		
        		if(param!=null&&!param.equals("")) {
        			
        			jpql=new StringBuffer();
                	countJpql = new StringBuffer();
                	
                	if(oId!=null) {
                		jpql.append("select new Map(  cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount ) FROM ConsumeRecord cr,Ruser r,Organization o,Ruser r2   WHERE    cr.creatorId=r.id and o.id=r.organizationId and o.id=:oId and cr.reciever=r2.id  ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r,Organization o ,Ruser r2   WHERE      cr.creatorId=r.id   and o.id=r.organizationId   and o.id=:oId  and cr.reciever=r2.id  ");
            			
                	}else {
                		jpql.append("select new Map( cr.money as money,cr.inMoney as inMoney,cr.reciever as reciever,r2.name as nick,cr.createDate as createDate,r.name as name,cr.giftName as giftName,cr.giftId as giftId,cr.number as number,cr.points as points,cr.diamondCount as diamondCount ) FROM ConsumeRecord cr,Ruser r,Ruser r2   WHERE    cr.creatorId=r.id and cr.reciever=r2.id   ");
                        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr,Ruser r  ,Ruser r2  WHERE      cr.creatorId=r.id  and cr.reciever=r2.id    ");
                	}
                	
                	
        			jpql.append(" AND r.name like :name ");
        			countJpql.append(" AND r.name like :name ");
        			
        			ParameterBean paramBean=new ParameterBean("name","%"+param+"%",null);
        			paramList.add(paramBean);
        		}else {
        			
        		}
        		
        	}else if(paramType.equals("3")) {
        		if(param!=null) {
        			jpql.append(" AND cr.creatorId = :userId ");
        			countJpql.append(" AND cr.creatorId = :userId ");
        			
        			ParameterBean paramBean=new ParameterBean("userId",Long.valueOf(param),null);
        			paramList.add(paramBean);
        		}else {
        			
        		}
        		
        	}
        	
        }
        
        
        
        if(gId!=null) {
			jpql.append(" AND cr.giftId = :giftId ");
			countJpql.append(" AND cr.giftId = :giftId ");
			
			ParameterBean paramBean=new ParameterBean("giftId",gId,null);
			paramList.add(paramBean);
		}else {
			
		}
        
        
        
        
        if(startDate!=null&&!"".equals(startDate) || endDate!=null&&!"".equals(endDate)) {
			if((startDate!=null&&!"".equals(startDate)) && (endDate!=null&&!"".equals(endDate))) {
				startDate+=" 00:00:00";
				endDate+=" 23:59:59";
				jpql.append(" AND cr.createDate >= :startDate AND cr.createDate <=:endDate ");
				countJpql.append(" AND cr.createDate >= :startDate AND cr.createDate <=:endDate ");
				
				ParameterBean paramBean = new ParameterBean("startDate",DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",startDate), TemporalType.TIMESTAMP);
				ParameterBean paramBean2 = new ParameterBean("endDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",endDate), TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				paramList.add(paramBean2);
				
				map.put("startDate", startDate);
				map.put("endDate", endDate);
				
			}else if(startDate!=null&&!"".equals(startDate)) {
				startDate+=" 00:00:00";
				jpql.append(" AND cr.createDate >= :startDate ");
				countJpql.append(" AND cr.createDate >= :startDate ");
				
				ParameterBean paramBean = new ParameterBean("startDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",startDate), TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				
				map.put("startDate", startDate);
			}else if(endDate!=null&&!"".equals(endDate)) {
				endDate+=" 22:59:59";
				jpql.append(" AND cr.createDate <= :endDate ");
				countJpql.append(" AND cr.createDate <= :endDate ");
				
				ParameterBean paramBean = new ParameterBean("endDate", DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",endDate), TemporalType.TIMESTAMP);
				paramList.add(paramBean);
				
				map.put("endDate", endDate);
			}
		}
        
        Set<String> userIds = jedisService.getSetFromShard(BicycleConstants.CONSUME_FORBID_USERS);
        if(userIds!=null&&userIds.size()>0) {
        	jpql.append(" AND cr.creatorId not in( :userIds) ");
    		countJpql.append(" AND cr.creatorId not  in( :userIds) ");
    		Set<Long> newUsers=new HashSet<Long>();
    		for(String u :userIds) {
    			newUsers.add(Long.valueOf(u));
    		}
    		ParameterBean paramBean=new ParameterBean("userIds",newUsers,null);
    		paramList.add(paramBean);
        }
        
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("createDate",1,"cr");
        orderByList.add(orderByObject);
        
        Long totalCount=consumeRecordService.getObjectCountByJpql(countJpql, paramList);
        
        List consumeRecordList=consumeRecordService.getObjectByJpql(jpql, 1, totalCount.intValue(), "cr", paramList, null, orderByList);
        if(consumeRecordList!=null&&consumeRecordList.size()>0) {
        	if(paramType!=null&&!paramType.equals("")) {
             	if(paramType.equals("1")||paramType.equals("2")&&param!=null&&!param.equals("")) {
//             		for (Object object : consumeRecordList) {
//                		Map consumeRecord=(Map) object;
//                		consumeRecord.put("money",consumeRecord.get("points"));
//        			}
             	}else {
             		for (Object object : consumeRecordList) {
                		Map consumeRecord=(Map) object;
                		consumeRecord.put("nick",jedisService.getValueFromMap(BicycleConstants.USER_INFO+consumeRecord.get("reciever"), "name"));
//                		consumeRecord.put("money",consumeRecord.get("points"));
        			}
             	}
        	 }else {
        		 for (Object object : consumeRecordList) {
              		Map consumeRecord=(Map) object;
              		consumeRecord.put("nick",jedisService.getValueFromMap(BicycleConstants.USER_INFO+consumeRecord.get("reciever"), "name"));
//              		consumeRecord.put("money",consumeRecord.get("points"));
      				
      			}
         	 }
        	
        }

		List cList = new ArrayList<Map>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Object object : consumeRecordList) {
			Map consumeRecord=(Map) object;
			Map row = new LinkedHashMap<String, String>();
			row.put("1",consumeRecord.get("nick"));
			row.put("2",consumeRecord.get("name"));
			row.put("3",consumeRecord.get("giftName"));
			row.put("4",consumeRecord.get("number"));
			row.put("5",sdf.format(consumeRecord.get("createDate")));
			row.put("6",consumeRecord.get("diamondCount"));
			row.put("7",consumeRecord.get("money"));
			cList.add(row);
		}
		LinkedHashMap headers = new LinkedHashMap();
		headers.put("1", "赠送给");
		headers.put("2", "用户名");
		headers.put("3", "道具名称");
		headers.put("4", "个数");
		headers.put("5", "赠送时间");
		headers.put("6", "折合金币");
		headers.put("7", "折合现金（元）");
		File file = CSVUtils.createCSVFile(cList, headers, basePath+"/exportExcel/", "赠送礼物明细");
		CommonUtils.download(file.getPath(),response);

    } 

}
