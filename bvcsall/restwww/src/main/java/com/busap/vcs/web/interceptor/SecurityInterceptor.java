package com.busap.vcs.web.interceptor;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.util.encode.JsonBinder;
import com.busap.vcs.utils.AES128Common;
import com.busap.vcs.utils.EncryptUtils;
import com.busap.vcs.webcomn.RespBodyBuilder;

public class SecurityInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);
	

	private static JsonBinder jsonBinder = JsonBinder.nonEmptyMapper();
	 
	private CloseableHttpClient httpclient = HttpClients.createDefault(); 

	@Value("${authurl}")
	private String authurl;
	@Value("${encryptField}")
    private Boolean encryptField;
	@Autowired
	private RespBodyBuilder respBodyBuilder; 

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2) throws Exception {

		String authKey = request.getHeader("authKey"); 
		String uid = request.getHeader("uid"); 
		
		if (!StringUtils.isBlank(authKey)&&!StringUtils.isBlank(uid)) { 
	         String phone = "";
	         String sessionid = "";
	         if (encryptField == null) { 
	 			logger.info("authKey:" + authKey+",uid:"+uid);
 			 	// phone和session解密 
 				String decryptStr = AES128Common.decrypt(authKey);
 				logger.info("authKey after decrypt:" + decryptStr);
 				// 解密完后赋值
 				if (!StringUtils.isBlank(decryptStr)) {
 					String[] s = decryptStr.split("\\|");
 					if ( s!= null && s.length >= 2) { 
 						phone = s[0].trim();
 						sessionid=s[1].trim();
 						request.setAttribute("phone",phone );
 						request.setAttribute("sessionid",sessionid);  
 					}
 				} 
	 		} 
			logger.info("phone:"+phone+",sessionId:"+sessionid+",uid:"+uid);
			if (StringUtils.isBlank(phone) || StringUtils.isBlank(sessionid) || StringUtils.isBlank(uid) || Integer.parseInt(uid) < 100000) {
				this.writeJsonData(response,jsonBinder.toJson(respBodyBuilder.toError(ResponseCode.CODE_450.toString(),ResponseCode.CODE_450.toCode())));
				return false; 
			} 
			return this.auth(authKey, uid,response);  
		}else{
			this.writeJsonData(response,jsonBinder.toJson(respBodyBuilder.toError(ResponseCode.CODE_450.toString(),ResponseCode.CODE_450.toCode())));
			return false;
		} 
	} 
	
	//通过调用麦克系统，进行用户认证
	public boolean auth(String authKey, String uid,HttpServletResponse response) { 
		try {    
			HttpPost httppost =  new HttpPost(this.authurl);
			//填入各个表单域的值
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("authKey",authKey));
			params.add(new BasicNameValuePair("uid",uid));
			//添加参数
			httppost.setEntity(new UrlEncodedFormEntity(params));
			//设置编码
			HttpResponse httpresponse=httpclient.execute(httppost); 
			// 获取返回数据
			int statusCode = httpresponse.getStatusLine().getStatusCode() ;
			HttpEntity entity = httpresponse.getEntity();
            String result = EntityUtils.toString(entity);  
			logger.info("麦克验证返回状态码：" + statusCode + "请求返回值:" + result); 
			HashMap map = jsonBinder.fromJson(result, HashMap.class); 
			Integer code =(Integer)((HashMap)map.get("header")).get("code");
			ResponseCode rcode = ResponseCode.valueOf("CODE_"+code.toString());
			if (rcode==ResponseCode.CODE_200){
				 return true;
			}else{ 
				this.writeJsonData(response,jsonBinder.toJson(respBodyBuilder.toError(rcode.toString(),rcode.toCode()))); 
				return false;
			}
		} catch (Exception e) {
			logger.error("麦克验证异常:", e);
			try{
				this.writeJsonData(response,jsonBinder.toJson(respBodyBuilder.toError(ResponseCode.CODE_500.toString(),ResponseCode.CODE_500.toCode())));
			}catch (Exception ev) { 
				logger.error("writeJsonData异常:", e);
			}
			return false;
		}
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
