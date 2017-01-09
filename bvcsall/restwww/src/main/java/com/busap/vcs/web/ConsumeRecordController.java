package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.busap.vcs.data.entity.*;
import com.busap.vcs.service.*;

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
	
	@Resource(name = "ruserService")
	private RuserService ruserService;

	

	//获取充值信息
    @RequestMapping("/findConsumeRecordByUser")
    @ResponseBody
    public RespBody findOrderPayByUserId(
    		@RequestParam(value = "page", required = false)  Long page,
    		@RequestParam(value = "rows", required = false)  Long rows
    		) throws Exception{ 
        
	    OrderPayExample example = new OrderPayExample();
	    
	    String userId =  request.getHeader("uid");
	    
//	    userId="118790";
	    
     
	    if(page==null) {
			page=1l;
        }
        if(rows==null) {
        	rows=10l;
        }
        
    	StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("FROM ConsumeRecord cr   WHERE    1=1  ");
        countJpql.append("SELECT COUNT(*)  FROM  ConsumeRecord cr    WHERE      1=1    ");
        
        
        if(userId!=null&&!userId.equals("")) {
			jpql.append(" AND cr.reciever = :userId ");
			countJpql.append(" AND cr.reciever = :userId ");
			
			ParameterBean paramBean=new ParameterBean("userId",Long.valueOf(userId),null);
			paramList.add(paramBean);
		}else {
			
		}
        
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
        OrderByBean orderByObject=new OrderByBean("createDate",1,"cr");
        orderByList.add(orderByObject);
        
        
        List consumeRecordList=consumeRecordService.getObjectByJpql(jpql, page.intValue(), rows.intValue(), "cr", paramList, null, orderByList);
        
        List<Long> userIdList=new ArrayList<Long>();
        
        for (Object object : consumeRecordList) {
			
        	ConsumeRecord lnt=(ConsumeRecord) object;
        	
        	userIdList.add(lnt.getCreatorId());
			
			
		}
        
        if(userIdList!=null&&userIdList.size()>0) {
        	List ruserList=ruserService.findUsersByIds(userIdList);
    		
    		
    		List orderUserList=new ArrayList();
    		for(int i=0;i<consumeRecordList.size();i++) {
    			ConsumeRecord lnTemp=(ConsumeRecord)consumeRecordList.get(i);
    			for(int j=0;j<ruserList.size();j++) {
    				Ruser ruTemp=(Ruser)ruserList.get(j);
    				if(ruTemp!=null&&lnTemp.getSender().longValue()==ruTemp.getId().longValue()) {
    					
    					lnTemp.setSenderName(ruTemp.getName());
    					break;
    				}
    			}
    			
    		}
        }
        
		
	    
    	return respBodyWriter.toSuccess(consumeRecordList);
    } 
	

}
