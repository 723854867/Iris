package com.busap.vcs.web;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.busap.vcs.data.vo.LoadConfigUrlVO;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LoadConfigUrlService;

/**
 * h5页面关注的controller
 *
 */
@Controller
@RequestMapping("/prize")
public class PrizeH5Controller extends BaseH5Controller {

	private Logger logger = LoggerFactory.getLogger(PrizeH5Controller.class);

	@Autowired
	protected HttpServletRequest request;
	
	@Resource(name = "jedisService")
	private JedisService jedisService;
	
	@Resource(name = "loadConfigUrlService")
	private LoadConfigUrlService loadConfigUrlService;
	
	private final String WX_CONFIG = "WX_CONFIG";

	/**
	 * 微信抽奖
	 * @return
	 */
	@RequestMapping("/getPrize")
	public String getPrize(HttpServletResponse response) {
		try {
			
			String paramString = this.request.getQueryString();
			if (paramString != null && !"".equals(paramString)) {
				paramString ="?"+paramString;
			} else {
				paramString = "";
			}
			Map<String,String> wxConfig = createWxConfig(paramString,loadConfigUrl("html5", "interfaceurl")+"/restwww/page/prize/getPrize");
			
			this.request.setAttribute("timestamp", wxConfig.get("timestamp"));
			this.request.setAttribute("noncestr", wxConfig.get("noncestr"));
			this.request.setAttribute("signature", wxConfig.get("signature"));
			
			Map<String,String> cookies = new HashMap<String,String>();
			cookies.put("backurl", URLEncoder.encode(loadConfigUrl("html5", "interfaceurl")+"/restwww/page/prize/getPrize"));
			setCookie(response, cookies, 7*24*60*60);
			
			logger.info("H5,getPrize");
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		
    	return "html5/app_active/index";
	}
	
	//春节活动
	@RequestMapping("/festival")
	public String festival(HttpServletResponse response) {
//		try {
//			
//			String paramString = this.request.getQueryString();
//			if (paramString != null && !"".equals(paramString)) {
//				paramString ="?"+paramString;
//			} else {
//				paramString = "";
//			}
//			Map<String,String> wxConfig = createWxConfig(paramString,loadConfigUrl("html5", "interfaceurl")+"/restwww/page/prize/festival");
//			
//			this.request.setAttribute("timestamp", wxConfig.get("timestamp"));
//			this.request.setAttribute("noncestr", wxConfig.get("noncestr"));
//			this.request.setAttribute("signature", wxConfig.get("signature"));
//			
//		} catch (Exception e) {
			return "html5/default/errorpage";
//		}
//		
//    	return "html5/monkey-active/index";
	}
	
	private Map<String, String> createWxConfig(String paramString,String url) {
		
		String requestUrl = "";
		String res = "";
		JSONObject jsonObj = null;
		
		String access_token = jedisService.get("wexin_token");
		
		if (access_token == null || "".equals(access_token)) {

			logger.info("createWxConfig begin");
			requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx9c59e9a110527d17&secret=f46786de9a5a40f7e5d87cfbbcd8aef0";
			res = httpGet(requestUrl);
			logger.info("H5,wechat globe access_token:{}",res);
			
			jsonObj = JSONObject.fromObject(res);
			access_token = jsonObj.getString("access_token");
			
			jedisService.set("wexin_token", access_token);
			jedisService.expire("wexin_token", 120*60); 
		}
		
		
		String jsapi_ticket = jedisService.get("jsapi_ticket");
		
		if (jsapi_ticket == null || "".equals(jsapi_ticket)) {
			requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi";
			logger.info("get wechat jsapi_ticket url is {}",requestUrl);
			res = httpGet(requestUrl);
			logger.info("H5,wechat jsapi_ticket:{}",res);
			jsonObj = JSONObject.fromObject(res);
			jsapi_ticket = jsonObj.getString("ticket");
			
			jedisService.set("jsapi_ticket", jsapi_ticket);
			jedisService.expire("jsapi_ticket", 120*60); 
		}
		
		
		url = url+paramString;
		logger.info("url is {}",url);
		
		String timestamp = String.valueOf(System.currentTimeMillis()/1000);
		String noncestr = getRandomString(10);
		
		String sting1 = "jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
		logger.info("sting1 is {}",sting1);
		
		String signature = DigestUtils.shaHex(sting1);
		logger.info("signature is {}",signature);
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("timestamp", timestamp);
		map.put("noncestr", noncestr);
		map.put("signature", signature);
		
//		jedisService.setValueToMap(WX_CONFIG, map);
//		jedisService.expire(WX_CONFIG, 90*60); 
		
		return map;
	}

	public String httpGet(String url) {
		logger.info("httpclient,request url is :{}",url);
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		String respContent = "";
		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);
			System.out.println(statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				respContent = new String(method.getResponseBody(),"utf-8");
			} else {
				logger.error("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return respContent;
	}
	
	private String loadConfigUrl(String clientPf,String urlType){

		List<LoadConfigUrlVO> loadConfigUrlList = loadConfigUrlService.findLoadConfigUrlByClientPf(clientPf);
		for(LoadConfigUrlVO lcuv:loadConfigUrlList){
			if(lcuv.getType().equals(urlType)){
				return lcuv.getUrl();
			}
		}
		return null;
	}
	
	private String getRandomString(int length) { 
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";  
	    Random random = new Random();  
	    StringBuffer sb = new StringBuffer();  
	    for (int i = 0; i < length; i++) {  
	        int number = random.nextInt(base.length());  
	        sb.append(base.charAt(number));  
	    }  
	    return sb.toString();  
	 }  
	
	private void setCookie(HttpServletResponse response,Map<String,String> cookies,int expire) {
		for (Map.Entry<String, String> entry : cookies.entrySet()) {  
			logger.info("h5 set cookies:{}------------->{}",entry.getKey(),entry.getValue());
			Cookie cookie = new Cookie(entry.getKey(),entry.getValue());
			cookie.setMaxAge(expire);
			cookie.setPath("/");
			response.addCookie(cookie);
		}  
	}
	public static void main(String[] args) {
		String s = DigestUtils.shaHex("jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VJlvy-nGaTiYExdsPmmTxfDn2fqqer2X68jni7gcUoe50UWurdyJ2z1r1Pl5Wfl1Kg&noncestr=upq38bkz6c&timestamp=1451268728&url=http://ceshi.wopaitv.com/restwww/page/prize/getPrize");
		System.out.println(s);
	}
}
