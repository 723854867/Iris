package com.busap.vcs.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.busap.vcs.data.entity.*;
import com.busap.vcs.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;


/**
 * 定制活动
 *
 * @author zx
 *
 */
@Controller()
@RequestMapping("orderPay")
public class OrderPayController {

	private static final Logger logger = LoggerFactory
			.getLogger(OrderPayController.class);
	
	@Resource(name = "pingPayService")
	private PingPayService pingPayService;
	
	@Resource(name = "respBodyBuilder")
    private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@Autowired
	protected HttpServletRequest request;

	

	//获取充值信息
    @RequestMapping("/findOrderPayByUserId")
    @ResponseBody
    public RespBody findOrderPayByUserId(
    		@RequestParam(value = "page", required = false)  Long page,
    		@RequestParam(value = "rows", required = false)  Long rows
    		) throws Exception{ 
        
	    OrderPayExample example = new OrderPayExample();
	    
	    String userId =  this.request.getHeader("uid");
	    
//	    userId="330";
	    
     
	    example.createCriteria().andUserIdEqualTo(Long.valueOf(userId)).andStatusGreaterThanOrEqualTo(2);
	    
	    if(page==null) {
	    	page=1l;
	    }
	    
	    if(rows==null) {
	    	rows=10l;
	    }
	    example.setOrderByClause(" create_time desc limit "+(page-1)*rows+","+rows );
     
	    List<OrderPay> orderList = pingPayService.getOrderPayMapper().selectByExample(example);
	    
    	return respBodyWriter.toSuccess(orderList);
    } 
	

}
