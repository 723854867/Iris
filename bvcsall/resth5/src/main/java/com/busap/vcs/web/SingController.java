package com.busap.vcs.web;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.busap.vcs.constants.SingConstants;
import com.busap.vcs.data.mapper.InviteRegisterDAO;
import com.busap.vcs.data.vo.LoadConfigUrlVO;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LoadConfigUrlService;
import com.busap.vcs.service.RuserLiveActivityService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.impl.SolrUserService;
import com.busap.vcs.utils.RequestValidateUtil;
import com.busap.vcs.utils.WXUtil;
import com.busap.vcs.webcomn.RespBodyBuilder;

 
@Controller
@RequestMapping("/sing")
public class SingController extends BaseH5Controller {

    private Logger logger = LoggerFactory.getLogger(SingController.class); 

    @Resource(name = "ruserService")
	private RuserService ruserService;
    
    @Resource(name = "requestValidateUtil")
	private RequestValidateUtil requestValidateUtil;
    
    @Resource(name = "solrUserService")
	private SolrUserService solrUserService;
    
    @Resource(name = "loadConfigUrlService")
	private LoadConfigUrlService loadConfigUrlService;
    
    @Resource(name = "wxUtil")
    private WXUtil wxUtil;
    
    @Autowired
    InviteRegisterDAO inviteRegisterDao;
    
    @Resource
	private RuserLiveActivityService ruserLiveActivityService;
    
    @Autowired
    JedisService jedisService;
    
    @Resource(name = "respBodyBuilder")
    protected RespBodyBuilder respBodyWriter = new RespBodyBuilder();
    
    @RequestMapping("/home")
	public String home(HttpServletRequest request,HttpServletResponse response) {
    	String pos = request.getParameter("pos");
    	
    	String uid = request.getHeader("uid");
    	String accessToken = request.getHeader("access_token");
    	
    	if (StringUtils.isBlank(uid)){
    		uid = request.getParameter("uid");
    	}
    	if (StringUtils.isBlank(accessToken)){
    		accessToken = request.getParameter("access_token");
    	}
    	
    	if (StringUtils.isBlank(uid)){
    		uid = getCookieByName(request, "uid");
    	}
    	if (StringUtils.isBlank(accessToken)){
    		accessToken = getCookieByName(request, "access_token");
    	}
    	
    	Long liveActivityId = Long.parseLong(jedisService.get(SingConstants.SING_ACTIVITY_ID));
    	logger.info("singcontroller,uid={},access_token={}",uid,accessToken);
    	
    	Map<String,String> cookies = new HashMap<String,String>();
		cookies.put("uid", uid);
		cookies.put("access_token", accessToken);
		cookies.put("pos", pos);
		cookies.put("liveActivityId", String.valueOf(liveActivityId));
		if (StringUtils.isBlank(uid)) {
			cookies.put("isJoin", "0");
		} else {
			cookies.put("isJoin", String.valueOf(ruserLiveActivityService.isJoin(Long.parseLong(uid), liveActivityId)));
		}
		setCookie(response, cookies, 7*24*60*60);
		
		//微信签名
		String paramString = request.getQueryString();
		if (paramString != null && !"".equals(paramString)) {
			paramString ="?"+paramString;
		} else {
			paramString = "";
		}
		Map<String,String> wxConfig = wxUtil.createWxConfig(paramString,loadConfigUrl("html5", "interfaceurl")+"/resth5/sing/home");
		
		request.setAttribute("timestamp", wxConfig.get("timestamp"));
		request.setAttribute("noncestr", wxConfig.get("noncestr"));
		request.setAttribute("signature", wxConfig.get("signature"));
    	return "html5/app_activity/new-song/signUp";
	}
    
    @RequestMapping("/shareRank")
	public String shareRank(HttpServletRequest request,HttpServletResponse response) {
    	String type = request.getParameter("type");
    	request.setAttribute("type", type);
    	
    	
    	//微信签名
		String paramString = request.getQueryString();
		if (paramString != null && !"".equals(paramString)) {
			paramString ="?"+paramString;
		} else {
			paramString = "";
		}
		Map<String,String> wxConfig = wxUtil.createWxConfig(paramString,loadConfigUrl("html5", "interfaceurl")+"/resth5/sing/shareRank");
		
		request.setAttribute("timestamp", wxConfig.get("timestamp"));
		request.setAttribute("noncestr", wxConfig.get("noncestr"));
		request.setAttribute("signature", wxConfig.get("signature"));
    	return "html5/app_activity/new-song/player-list";
	}
    
    @RequestMapping("/timeSem")
	public String timeSem(HttpServletRequest request,HttpServletResponse response) {
    	String type = request.getParameter("type");
    	request.setAttribute("type", type);
    	
    	
    	//微信签名
		String paramString = request.getQueryString();
		if (paramString != null && !"".equals(paramString)) {
			paramString ="?"+paramString;
		} else {
			paramString = "";
		}
		Map<String,String> wxConfig = wxUtil.createWxConfig(paramString,loadConfigUrl("html5", "interfaceurl")+"/resth5/sing/timeSem");
		
		request.setAttribute("timestamp", wxConfig.get("timestamp"));
		request.setAttribute("noncestr", wxConfig.get("noncestr"));
		request.setAttribute("signature", wxConfig.get("signature"));
    	return "html5/app_activity/new-song/time-sem";
	}
    
    @RequestMapping("/singSport")
   	public String singSport(HttpServletRequest request,HttpServletResponse response) {
       	
       	//微信签名
   		String paramString = request.getQueryString();
   		if (paramString != null && !"".equals(paramString)) {
   			paramString ="?"+paramString;
   		} else {
   			paramString = "";
   		}
   		Map<String,String> wxConfig = wxUtil.createWxConfig(paramString,loadConfigUrl("html5", "interfaceurl")+"/resth5/sing/singSport");
   		
   		request.setAttribute("timestamp", wxConfig.get("timestamp"));
   		request.setAttribute("noncestr", wxConfig.get("noncestr"));
   		request.setAttribute("signature", wxConfig.get("signature"));
       	return "html5/app_activity/new-song/sing_sport";
   	}
    
    @RequestMapping("/singWz")
   	public String singWz(HttpServletRequest request,HttpServletResponse response) {
       	
       	//微信签名
   		String paramString = request.getQueryString();
   		if (paramString != null && !"".equals(paramString)) {
   			paramString ="?"+paramString;
   		} else {
   			paramString = "";
   		}
   		Map<String,String> wxConfig = wxUtil.createWxConfig(paramString,loadConfigUrl("html5", "interfaceurl")+"/resth5/sing/singWz");
   		
   		request.setAttribute("timestamp", wxConfig.get("timestamp"));
   		request.setAttribute("noncestr", wxConfig.get("noncestr"));
   		request.setAttribute("signature", wxConfig.get("signature"));
       	return "html5/app_activity/new-song/song-wz";
   	}
    
    @RequestMapping("/singWz2")
   	public String singWz2(HttpServletRequest request,HttpServletResponse response) {
       	
       	//微信签名
   		String paramString = request.getQueryString();
   		if (paramString != null && !"".equals(paramString)) {
   			paramString ="?"+paramString;
   		} else {
   			paramString = "";
   		}
   		Map<String,String> wxConfig = wxUtil.createWxConfig(paramString,loadConfigUrl("html5", "interfaceurl")+"/resth5/sing/singWz2");
   		
   		request.setAttribute("timestamp", wxConfig.get("timestamp"));
   		request.setAttribute("noncestr", wxConfig.get("noncestr"));
   		request.setAttribute("signature", wxConfig.get("signature"));
       	return "html5/app_activity/new-song/song-wz-2";
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
    
    private String loadConfigUrl(String clientPf,String urlType){

		List<LoadConfigUrlVO> loadConfigUrlList = loadConfigUrlService.findLoadConfigUrlByClientPf(clientPf);
		for(LoadConfigUrlVO lcuv:loadConfigUrlList){
			if(lcuv.getType().equals(urlType)){
				return lcuv.getUrl();
			}
		}
		return null;
	}
    
    private String getCookieByName(HttpServletRequest request,String name){
		Cookie[] cookies = request.getCookies();
        for(Cookie c :cookies ){
            logger.info("h5 get cookie:{}------------->{}",c.getName(),c.getValue());
            if (c.getName().equals(name)){
            	return URLDecoder.decode(c.getValue());
            }
        }
        return null;
	}

	@RequestMapping("/singWz0910")
	public String singWz0910(HttpServletRequest request,HttpServletResponse response) {

		//微信签名
		String paramString = request.getQueryString();
		if (paramString != null && !"".equals(paramString)) {
			paramString ="?"+paramString;
		} else {
			paramString = "";
		}
		Map<String,String> wxConfig = wxUtil.createWxConfig(paramString,loadConfigUrl("html5", "interfaceurl")+"/resth5/sing/singWz0910");

		request.setAttribute("timestamp", wxConfig.get("timestamp"));
		request.setAttribute("noncestr", wxConfig.get("noncestr"));
		request.setAttribute("signature", wxConfig.get("signature"));
		return "html5/app_activity/new-song/song-wz-09_10";
	}

	@RequestMapping("/singWz1112")
	public String singWz1112(HttpServletRequest request,HttpServletResponse response) {

		//微信签名
		String paramString = request.getQueryString();
		if (paramString != null && !"".equals(paramString)) {
			paramString ="?"+paramString;
		} else {
			paramString = "";
		}
		Map<String,String> wxConfig = wxUtil.createWxConfig(paramString,loadConfigUrl("html5", "interfaceurl")+"/resth5/sing/singWz1112");

		request.setAttribute("timestamp", wxConfig.get("timestamp"));
		request.setAttribute("noncestr", wxConfig.get("noncestr"));
		request.setAttribute("signature", wxConfig.get("signature"));
		return "html5/app_activity/new-song/song-wz-11_12";
	}

}
