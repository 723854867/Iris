package com.busap.vcs.web.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView; 
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.util.encode.JsonBinder;
import com.busap.vcs.webcomn.RespBodyBuilder;

public class ForbidWordInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(ForbidWordInterceptor.class);
	private static JsonBinder jsonBinder = JsonBinder.nonEmptyMapper(); 
	
	@Autowired
    JedisService jedisService;
	
	@Autowired
	private RespBodyBuilder respBodyBuilder;  

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2) throws Exception {
		String uid = request.getHeader("uid");
		String key = BicycleConstants.WOPAI_FORBID_WORD_KEY+uid; 
		String st = jedisService.get(key);//0:正常，1:禁言，2:禁号
		if (!StringUtils.isBlank(st)) {  
			if ("1".equals(st)) {
				this.writeJsonData(response,jsonBinder.toJson(respBodyBuilder.toError(ResponseCode.CODE_339.toString(),ResponseCode.CODE_339.toCode())));
				return false; 
			}else if("2".equals(st)) {
				this.writeJsonData(response,jsonBinder.toJson(respBodyBuilder.toError(ResponseCode.CODE_340.toString(),ResponseCode.CODE_340.toCode())));
				return false;
			}else{
				return true;
			}
		}else
			return true;
	} 
	 
	
	private void writeJsonData(HttpServletResponse response,String data) throws Exception{
		PrintWriter out = null;
		try{
			response.setContentType("application/json;charset=UTF-8"); 
			out = response.getWriter();
			out.write(data);
			out.flush();
		}finally{
			if (out != null) {
				out.close();
			}
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {

	}
	
	

}
