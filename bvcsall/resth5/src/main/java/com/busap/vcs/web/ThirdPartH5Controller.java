package com.busap.vcs.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Ruser.ThirdUser;
import com.busap.vcs.data.enums.Sex;
import com.busap.vcs.data.vo.LoadConfigUrlVO;
import com.busap.vcs.oauth.third.ThirdPartCommon;
import com.busap.vcs.service.AttentionService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LoadConfigUrlService;
import com.busap.vcs.service.OAuthService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.impl.SolrUserService;

/**
 * h5页面关注的controller
 *
 */
@Controller
@RequestMapping("/thirdPart")
public class ThirdPartH5Controller extends BaseH5Controller {

	private Logger logger = LoggerFactory
			.getLogger(ThirdPartH5Controller.class);

	@Autowired
	protected HttpServletRequest request;

	@Resource(name = "attentionService")
	private AttentionService attentionService;

	@Resource(name = "ruserService")
	private RuserService ruserService;

	@Resource(name = "jedisService")
	private JedisService jedisService;

	@Resource(name = "solrUserService")
	private SolrUserService solrUserService;

	@Resource(name = "thirdPartCommons")
	private Map<String, ThirdPartCommon> commons;
	
	@Resource(name = "loadConfigUrlService")
	private LoadConfigUrlService loadConfigUrlService;

	@Value("${files.localpath}")
	private String basePath;

	@Autowired
	private OAuthService oAuthService;

	private final String WECHAT_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";
	private final String WECHAT_APPID = "wx9c59e9a110527d17";
	private final String WECHAT_SECRET = "f46786de9a5a40f7e5d87cfbbcd8aef0";
	private final String WECHAT_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?";
	
	private final String QQ_URL = "https://graph.qq.com/oauth2.0/token?";
	private final String QQ_ME_URL = "https://graph.qq.com/oauth2.0/me?";
	private final String QQ_USERINFO_URL = "https://graph.qq.com/user/get_user_info?";
	private final String QQ_APPID = "101254957";
	private final String QQ_APP_KEY = "142f1d7d8b1f6b96724788b187f60e2d";
	
	private final String SINA_URL = "https://api.weibo.com/oauth2/access_token";
	private final String SINA_USERINFO_URL = "https://api.weibo.com/2/users/show.json?";
	private final String SINA_APPID = "255443166";
	private final String SINA_SECRET = "809cc0dc1713e8d86dde5b306c19db0c";

	/**
	 * 微信登陆回调
	 * 
	 * @return
	 */
	@RequestMapping("/wechatCallback")
	public String wechatCallback(HttpServletRequest request,HttpServletResponse response) {
		logger.info("H5,wechatCallback,request:{}", request.getQueryString());
		Map<String,Object> map = new HashMap<String, Object>(); 
		try {
			String code = request.getParameter("code");
			if (code != null && !"".equals(code)) {
				String url = WECHAT_URL+ "appid=" + WECHAT_APPID + "&secret=" + WECHAT_SECRET+ "&code=" + code + "&grant_type=authorization_code";
				logger.info("H5,wechat get access_token url:{}",url);
				String res = httpGet(url);
				logger.info("H5,wechat access_token:{}",res);
				
				JSONObject jsonObj = JSONObject.fromObject(res);
				String wechatAccessToken = jsonObj.getString("access_token");
				String openid = jsonObj.getString("openid");
				String expire = jsonObj.getString("expires_in");
				String unionid = jsonObj.getString("unionid");

				if (wechatAccessToken != null && !"".equals(wechatAccessToken)
						&& openid != null && !"".equals(openid)) {
					String regPlatform = request.getParameter("regPlatform");
					map = regist(wechatAccessToken, openid, ThirdUser.wechat.getValue(), expire,unionid,regPlatform,unionid);
				} 
			} 
			if (map.get("uid") != null && map.get("access_token") != null){
				Map<String,String> cookies = new HashMap<String,String>();
				cookies.put("uid", String.valueOf(map.get("uid")));
				cookies.put("access_token", String.valueOf(map.get("access_token")));
				setCookie(response, cookies, 7*24*60*60);
				
				String backUrl = getCookieByName(request, "backurl");
				if (backUrl != null && !"".equals(backUrl)){
					logger.info("h5,backurl is :{}",backUrl);
//					if (backUrl.contains("?")){
//						if (backUrl.endsWith("?")) {
//							backUrl += "uid="+map.get("uid")+"&access_token="+map.get("access_token");
//						} else {
//							backUrl += "&uid="+map.get("uid")+"&access_token="+map.get("access_token");
//						}
//					} else {
//						backUrl += "?uid="+map.get("uid")+"&access_token="+map.get("access_token");
//					}
					return "redirect:" + backUrl;
				}
				return "redirect:/page/user/userCenter?uid="+map.get("uid")+"&access_token="+map.get("access_token");
			}
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		this.request.setAttribute("thirdPartLoginFlag", "fail");
		return "html5/log_reg/login";
	}
	
	/**
	 * qq登陆回调
	 * 
	 * @return
	 */
	@RequestMapping("/qqCallback")
	public String qqCallback(HttpServletRequest request,HttpServletResponse response) {
		logger.info("H5,qqCallback,request:{}", request.getQueryString());
		Map<String,Object> map = new HashMap<String, Object>(); 
		try {
			String code = request.getParameter("code");
			if (code != null && !"".equals(code)) { //获取code回调，然后请求qq去获取access_token
				String url = QQ_URL + "grant_type=authorization_code&client_id="+QQ_APPID+"&client_secret="+QQ_APP_KEY+"&code="+code+"&redirect_uri="+URLEncoder.encode(loadConfigUrl("html5", "interfaceurl")+"/restwww/page/thirdPart/qqCallback");
				logger.info("H5,qq get access_token url:{}",url);
				String res = httpGet(url);
				
				logger.info("H5,qq access_token:{}",res);
				
				if (res != null && !"".equals(res)){
					int beginIndex = res.indexOf("access_token=")+"access_token=".length();
					int endIndex = res.indexOf("&",beginIndex);
					if (endIndex > beginIndex){
						String qqAccessToken = res.substring(beginIndex, endIndex);
						logger.info("H5,qqAccessToken:{}",qqAccessToken);
						if (qqAccessToken != null && !"".equals(qqAccessToken)){
							String res2 = httpGet(QQ_ME_URL+"access_token="+qqAccessToken);
							logger.info("H5,get openid response:{}",res2);
							if (res2 != null && !"".equals(res2)){
								beginIndex = res2.indexOf("\"openid\":\"")+"\"openid\":\"".length();
								endIndex = res2.indexOf("\"}",beginIndex);
								if (endIndex > beginIndex){
									String openid = res2.substring(beginIndex,endIndex);
									logger.info("H5,openid:{}",openid);
									if (openid != null && !"".equals(openid)){
										String regPlatform = request.getParameter("regPlatform");
										map = regist(qqAccessToken, openid, ThirdUser.qq.getValue(), "7776000","",regPlatform,"");
									}
								}
							}
						}
					}
				}
			} 
			if (map.get("uid") != null && map.get("access_token") != null){
				Map<String,String> cookies = new HashMap<String,String>();
				cookies.put("uid", String.valueOf(map.get("uid")));
				cookies.put("access_token", String.valueOf(map.get("access_token")));
				setCookie(response, cookies, 7*24*60*60);
				
				String backUrl = getCookieByName(request, "backurl");
				if (backUrl != null && !"".equals(backUrl)){
					logger.info("h5,backurl is :{}",backUrl);
					if (backUrl.contains("?")){
						if (backUrl.endsWith("?")) {
							backUrl += "uid="+map.get("uid")+"&access_token="+map.get("access_token");
						} else {
							backUrl += "&uid="+map.get("uid")+"&access_token="+map.get("access_token");
						}
					} else {
						backUrl += "?uid="+map.get("uid")+"&access_token="+map.get("access_token");
					}
					return "redirect:" + backUrl;
				}
				return "redirect:/page/user/userCenter?uid="+map.get("uid")+"&access_token="+map.get("access_token");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "html5/default/errorpage";
		}
		
		this.request.setAttribute("thirdPartLoginFlag", "fail");
		return "html5/log_reg/login";
	}
	
	/**
	 * sina微博登陆回调
	 * 
	 * @return
	 */
	@RequestMapping("/sinaCallback")
	public String sinaCallback(HttpServletRequest request,HttpServletResponse response) {
		logger.info("H5,sinaCallback,request:{}", request.getQueryString());
		Map<String,Object> map = new HashMap<String, Object>(); 
		try {
			String code = request.getParameter("code");
			if (code != null && !"".equals(code)) { //获取code回调，然后请求qq去获取access_token
				String url = SINA_URL;
				logger.info("H5,sina post access_token url:{}",url);
				Map<String,String> paramMap = new HashMap<String,String>();
				paramMap.put("grant_type", "authorization_code");
				paramMap.put("client_id", SINA_APPID);
				paramMap.put("client_secret", SINA_SECRET);
				paramMap.put("code", code);
				paramMap.put("redirect_uri", loadConfigUrl("html5", "interfaceurl")+"/restwww/page/thirdPart/sinaCallback");
				
				String res = httpPost(url,paramMap);
				
				logger.info("H5,sina access_token:{}",res);
				
				JSONObject jsonObj = JSONObject.fromObject(res);
				String sinaAccessToken = jsonObj.getString("access_token");
				String expire = jsonObj.getString("expires_in");
				String sinaUid = jsonObj.getString("uid");

				if (sinaAccessToken != null && !"".equals(sinaAccessToken) && sinaUid != null && !"".equals(sinaUid)) {
					String regPlatform = request.getParameter("regPlatform");
					map = regist(sinaAccessToken, sinaUid, ThirdUser.sina.getValue(), expire,"",regPlatform,"");
				} 
			} 
			if (map.get("uid") != null && map.get("access_token") != null){
				Map<String,String> cookies = new HashMap<String,String>();
				cookies.put("uid", String.valueOf(map.get("uid")));
				cookies.put("access_token", String.valueOf(map.get("access_token")));
				setCookie(response, cookies, 7*24*60*60);
				
				String backUrl = getCookieByName(request, "backurl");
				if (backUrl != null && !"".equals(backUrl)){
					logger.info("h5,backurl is :{}",backUrl);
					if (backUrl.contains("?")){
						if (backUrl.endsWith("?")) {
							backUrl += "uid="+map.get("uid")+"&access_token="+map.get("access_token");
						} else {
							backUrl += "&uid="+map.get("uid")+"&access_token="+map.get("access_token");
						}
					} else {
						backUrl += "?uid="+map.get("uid")+"&access_token="+map.get("access_token");
					}
					return "redirect:" + backUrl;
				}
				return "redirect:/page/user/userCenter?uid="+map.get("uid")+"&access_token="+map.get("access_token");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "html5/default/errorpage";
		}
		
		this.request.setAttribute("thirdPartLoginFlag", "fail");
		return "html5/log_reg/login";
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
	
	public String httpPost(String url,Map<String,String> paramMap) {
		logger.info("httpclient,post request url is :{}",url);
		HttpClient client = new HttpClient();
	    PostMethod postMethod=new PostMethod(url);
	    postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
	    
	    for (Map.Entry<String, String> entry : paramMap.entrySet()) {  
	    	logger.info("post param key:{},value:{}",entry.getKey(),entry.getValue());
	    	postMethod.addParameter(entry.getKey(),entry.getValue());   
	    }  
	    
		String respContent = "";
		try {
			// Execute the method.
			int statusCode = client.executeMethod(postMethod);
			logger.info(postMethod.getResponseBodyAsString());
			if (statusCode == HttpStatus.SC_OK) {
				respContent = new String(postMethod.getResponseBody(),"utf-8");
			} else {
				logger.error("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return respContent;
	}
	
	private Map<String, Object> regist(String thirdPartToken,
			String thirdPartUid, String data_from, String expire,String unionid,String regPlatform,String wechatUnionid) {
		
		Map<String, Object> msg = new HashMap<String, Object>();
		ThirdPartCommon common = getCommon(data_from);

		try {
			// 如果第三方已经存在，直接发放令牌
			boolean flag = common.isExist(thirdPartUid);
			
			// 令牌生成&存储
			String accessToken = common.createAndStoreAccessToken(thirdPartUid,expire);
			
			//微信登陆用unionid判断是否已经存在
			if (ThirdUser.wechat.getValue().equals(data_from)){
				flag = ruserService.countByWechatUnionid(unionid, data_from);
				if (flag) { //将"wechat"+微信unionid作为用户的username，之前是"wechat"+微信openid作为用户名
					Ruser temp = ruserService.findByWechatUnionid(wechatUnionid);
					temp.setUsername(ThirdUser.wechat.getValue()+wechatUnionid);
					ruserService.save(temp);
				}
				accessToken = common.getOldToken(ThirdUser.wechat.getValue()+wechatUnionid);  //微信网页登陆token用之前客户端生成的token，这样h5和客户端微信登陆的可以同时在线
				String idInToken = jedisService.getValueFromMap(accessToken, "id");
				if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(idInToken)) { //如果微信登陆之前没有token，或者token中的id为空，新生成一个
					accessToken = common.createAndStoreAccessToken(wechatUnionid, null);
				}
			}
			if (flag) {
				// 判断用户是否被禁用
				Ruser ruser = ruserService.findByUsername(oAuthService
						.getUsernameByAccessToken(accessToken));
				if (ruser != null && ruser.getStat() == 2) {
					msg = generateMsg("你已被封号，请联系管理员",
							HttpServletResponse.SC_BAD_REQUEST + "_8");
					return msg;
				}
				
				// 用户信息放到redis
				putUserToRedis(request,
						oAuthService.getUsernameByAccessToken(accessToken));
				
				// 更新用户登陆时间
				ruserService.updateLoginDate(oAuthService.getUsernameByAccessToken(accessToken));
				
				if (ThirdUser.wechat.getValue().equals(data_from) && unionid != null && !"".equals(unionid)){  //如果是微信登陆，插入微信unionid
					ruserService.insertWechatUnionid(oAuthService.getUsernameByAccessToken(accessToken), unionid);
				}

				// 新注册用户昵称加入solr引擎索引
				try {
					solrUserService.addUser(ruser.getId(), ruser.getName(),
							ruser.getPhone());
				} catch (SolrServerException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				msg.put("code", "200");
				msg.put(OAuth.OAUTH_BEARER_TOKEN, accessToken);
				msg.put("uid", ruser.getId());
				return msg;
			}

			Ruser ruser = createRuser(thirdPartToken, thirdPartUid,data_from,regPlatform,wechatUnionid);
			// 用户信息放到redis
			putUserToRedis(request,
					oAuthService.getUsernameByAccessToken(accessToken));
			
			// 更新用户登陆时间
			ruserService.updateLoginDate(oAuthService.getUsernameByAccessToken(accessToken));
			
			//生成token
			accessToken = common.createAndStoreAccessToken(wechatUnionid, null);
			
			if (ThirdUser.wechat.getValue().equals(data_from) && unionid != null && !"".equals(unionid)){  //如果是微信登陆，插入微信unionid
				ruserService.insertWechatUnionid(oAuthService.getUsernameByAccessToken(accessToken), unionid);
			}

			// 新注册用户id放到redis里
			if (ruser.getId() != null && ruser.getId() != 0) {
				// 新注册用户昵称加入solr引擎索引
				try {
					// solrUserService.addUser(user.getId(), user.getName());
					solrUserService.addUser(ruser.getId(), ruser.getName(),
							ruser.getPhone());
				} catch (SolrServerException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			msg.put("code", "200");
			msg.put(OAuth.OAUTH_BEARER_TOKEN, accessToken);
			msg.put("uid", ruser.getId());
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	
	/**
	 * 根据第三方信息生成我拍用户
	 * 
	 * @param thirdPartToken
	 * @param thirdPartUid
	 * @param data_from
	 * @return
	 */
	private Ruser createRuser(String thirdPartToken, String thirdPartUid,
			String data_from,String regPlatform,String wechatUnionid) {
		Ruser ruser = new Ruser();
		if (ThirdUser.qq.getValue().equals(data_from)) {
			String requestUrl= QQ_USERINFO_URL+"access_token="+thirdPartToken+"&oauth_consumer_key="+QQ_APPID+"&openid="+thirdPartUid;
			String res = httpGet(requestUrl);
			logger.info("qq,userinfo respnse:{}"+res);
			
			JSONObject jsonObj = JSONObject.fromObject(res);
			String pic = saveThirdPartyPic(jsonObj.getString("figureurl_qq_1"));
			ruser.setPic(pic);
			ruser.setSignature("");
			ruser.setAddr("");
			ruser.setThirdUserame(thirdPartUid);
			ruser.setThirdFrom(ThirdUser.qq.getValue());// QQ注册
			ruser.setPassword(thirdPartToken);// 因数据库中该字段不能为空，暂时使用令牌作为密码(无意义)
			ruser.setUsername(ThirdUser.qq.getValue() + thirdPartUid);
			ruser.setName(jsonObj.getString("nickname"));
			ruser.setSex(("女".equals(jsonObj.getString("gender"))) ? Sex.女.getName() : Sex.男
					.getName());
			ruser.setIsBlacklist(1);//新用户默认可以提现
		} else if(ThirdUser.wechat.getValue().equals(data_from)){
			String requestUrl= WECHAT_USERINFO_URL+"access_token="+thirdPartToken+"&openid="+thirdPartUid+"&lang=zh_CN";
			String res = httpGet(requestUrl);
			logger.info("wechat,userinfo respnse:{}"+res);
			
			JSONObject jsonObj = JSONObject.fromObject(res);
			String pic = saveThirdPartyPic(jsonObj.getString("headimgurl"));
			ruser.setPic(pic);
			ruser.setSignature("");
			ruser.setAddr("");
			ruser.setThirdUserame(thirdPartUid);
			ruser.setThirdFrom(ThirdUser.wechat.getValue());// 微信注册
			ruser.setPassword(thirdPartToken);// 因数据库中该字段不能为空，暂时使用令牌作为密码(无意义)
			ruser.setUsername(ThirdUser.wechat.getValue() + wechatUnionid);
			ruser.setName(jsonObj.getString("nickname"));
			ruser.setSex(("2".equals(jsonObj.getString("sex"))) ? Sex.女.getName() : Sex.男
					.getName());
			ruser.setIsBlacklist(1);//新用户默认可以提现
			
		} else if(ThirdUser.sina.getValue().equals(data_from)){ 
			String requestUrl= SINA_USERINFO_URL+"source="+SINA_APPID+"&access_token="+thirdPartToken+"&uid="+thirdPartUid;
			String res = httpGet(requestUrl);
			logger.info("sina,userinfo respnse:{}"+res);
			
			JSONObject jsonObj = JSONObject.fromObject(res);
			String pic = saveThirdPartyPic(jsonObj.getString("profile_image_url"));
			ruser.setPic(pic);
			ruser.setSignature("");
			ruser.setAddr("");
			ruser.setThirdUserame(thirdPartUid);
			ruser.setThirdFrom(ThirdUser.sina.getValue());// 微信注册
			ruser.setPassword(thirdPartToken);// 因数据库中该字段不能为空，暂时使用令牌作为密码(无意义)
			ruser.setUsername(ThirdUser.sina.getValue() + thirdPartUid);
			ruser.setName(jsonObj.getString("screen_name"));
			ruser.setSex(("f".equals(jsonObj.getString("gender"))) ? Sex.女.getName() : Sex.男
					.getName());
			ruser.setIsBlacklist(1);//新用户默认可以提现
		}
		ruser.setRegPlatform(regPlatform == null ?"h5":regPlatform);
		ruserService.save(ruser);
		return ruser;
	}
	
	/**
	 * 保存第三方头像
	 * 
	 * @param picUrl
	 * @return
	 */
	private String saveThirdPartyPic(String picUrl){
		
		String relUrl = "";
		
		String relPath = File.separator + "userHomePic" + File.separator
				+ DateFormatUtils.format(new Date(), "yyyy-MM-dd")
				+ File.separator;
		String originalFilename = DateFormatUtils.format(new Date(),
				"yyyy-MM-dd_HHmmss") + ".jpg";
		
		try {
			BufferedInputStream in = new BufferedInputStream(new URL(
					picUrl).openStream());

			InputStream is = null;
			try {
				FileUtils.copyInputStreamToFile(in, new File(
						basePath + relPath, originalFilename));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(in);
				in = null;
				is = null;
			}
			relUrl = relPath + originalFilename;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("".equals(relUrl)){
			//保存第三方图片出现异常，头像设置为默认头像
			relUrl = jedisService.get(BicycleConstants.DEFAULT_HEAD_PIC);
		}
		logger.info("saveThirdPartyPic,relUrl :{}",relUrl);
		return relUrl;
	}

	private Map<String, Object> generateMsg(String msg, Object code) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", code);
		String message = msg != null ? msg : "";
		result.put("message", message);
		return result;
	}

	// 将用户信息放入redis
	private void putUserToRedis(HttpServletRequest request, String username) {
		logger.info("put user to redis,username is {}", username);
		Ruser user = ruserService.findByUsername(username);
		if (user != null) {
			String key = BicycleConstants.USER_INFO + user.getId();
			jedisService.saveAsMap(key, user);
			// 将用户手机平台信息放入redis
			String platform = request.getParameter("osVersion");
			// 将设备唯一id放到redis
			String deviceUuid = request.getParameter("deviceUuid");
			if (platform != null) {
				logger.info("put user to redis,platform is {}", platform);
				if (platform.contains("android"))
					jedisService.setValueToMap(key, "platform", "android");
				else if (platform.contains("ios"))
					jedisService.setValueToMap(key, "platform", "ios");
			}
			if (deviceUuid != null) {
				logger.info("put user to redis,deviceUuid is {}", deviceUuid);
				jedisService.setValueToMap(key, "deviceUuid", deviceUuid);
			} else {
				logger.info("put user to redis,deviceUuid is {}", "");
				jedisService.setValueToMap(key, "deviceUuid", "");
			}
		}
	}

	private ThirdPartCommon getCommon(Object key) {
		if (commons != null) {
			return commons.get(key);
		}
		return null;
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
	
	private void setCookie(HttpServletResponse response,Map<String,String> cookies,int expire) {
		for (Map.Entry<String, String> entry : cookies.entrySet()) {  
			logger.info("h5 set cookies:{}------------->{}",entry.getKey(),entry.getValue());
			Cookie cookie = new Cookie(entry.getKey(),entry.getValue());
			cookie.setMaxAge(expire);
			cookie.setPath("/");
			response.addCookie(cookie);
		}  
	}
}
