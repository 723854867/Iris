package com.busap.vcs.oauth.web.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Ruser.ThirdUser;
import com.busap.vcs.oauth.third.ThirdPartCommon;
import com.busap.vcs.oauth.third.qq.QQCommon;
import com.busap.vcs.oauth.web.exception.ThirdPartException;
import com.busap.vcs.service.AttentionService;
import com.busap.vcs.service.Constants;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.OAuthService;
import com.busap.vcs.service.PingPayService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.impl.SolrUserService;
import com.busap.vcs.util.DateUtils;

@RestController
@RequestMapping(value = { "/thirdPart" })
public class ThirdPartLoginController {

	private Logger logger = LoggerFactory
			.getLogger(ThirdPartLoginController.class);

	@Resource(name = "ruserService")
	private RuserService ruserService;

	@Resource(name = "thirdPartCommons")
	private Map<String, ThirdPartCommon> commons;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name="attentionService")
	private AttentionService attentionService;
	
	@Autowired
    private PingPayService pingPayService;
	
	@Autowired
	protected HttpServletRequest request;

	@Autowired
	private OAuthService oAuthService;

	@Value("${files.localpath}")
	private String basePath;
	
	@Resource(name = "solrUserService")
	private SolrUserService solrUserService;

	private ThirdPartCommon getCommon(Object key) {
		if (commons != null) {
			return commons.get(key);
		}
		return null;
	}

	@RequestMapping(value = { "/regist" }, params = { "access_token", "uid",
			"expire", "data_from" })
	public ResponseEntity<Map<String, Object>> regist(
			@RequestParam("access_token") String access_token,
			@RequestParam("uid") String uid,
			@RequestParam("data_from") String data_from,
			@RequestParam("expire") String expire) throws OAuthSystemException,
			URISyntaxException, MalformedURLException, IOException {
		Map<String, Object> msg = null;
		//see ThirdUser
		ThirdPartCommon common = getCommon(data_from);

		if (common == null ) {
			msg = generateMsg("目前只支持新浪第三方登陆",
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
		}
		boolean legal = false;
		try {
			legal = common.validate(access_token, uid);
		}catch(ThirdPartException tpe){
			logger.error("令牌验证错误",tpe);
			msg = generateMsg("令牌验证出错",
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
		}catch (Exception e) {
			logger.error("令牌验证错误",e);
			msg = generateMsg("令牌无法正确验证",
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
		}
		String now = DateUtils.getNowAsStr(); //获得当前的日期和时间
    	String nowYMD = DateUtils.getNowYMDAsStr();//获得当前年月日
    	jedisService.setValueToList("thirdpart_reg_total_"+nowYMD, now+"|"+data_from+"|"+access_token+"|"+uid+"|"+this.request.getParameter("regPlatform")+"|"+this.request.getParameter("appVersion"));
		if (legal) {// 令牌合法，重新生成令牌，执行注册
			boolean flag = common.isExist(uid);
			// 令牌生成&存储
			String accessToken = common.createAndStoreAccessToken(uid, expire);
			
			//微信登陆用unionid判断是否已经存在
			String wechatUnionid = this.request.getParameter("unionid");
			if (ThirdUser.wechat.getValue().equals(data_from)){
				if (wechatUnionid == null || "".equals(wechatUnionid)) {
					wechatUnionid = common.getUnionid(access_token, uid);
				}
				flag = ruserService.countByWechatUnionid(wechatUnionid, data_from);
				if (flag) { //将"wechat"+微信unionid作为用户的username，之前是"wechat"+微信openid作为用户名
					Ruser temp = ruserService.findByWechatUnionid(wechatUnionid);
					temp.setUsername(ThirdUser.wechat.getValue()+wechatUnionid);
					ruserService.save(temp);
				}
				accessToken = common.createAndStoreAccessToken(wechatUnionid, expire);
			}
			// 如果第三方已经存在，直接发放令牌
			if (flag) {
				msg = generateMsg(null, HttpServletResponse.SC_OK);
				Map<String, Object> data = new HashMap<String, Object>();
				boolean isUserInfoCompletion = ruserService
						.isUserInfoCompletion(oAuthService
								.getUsernameByAccessToken(accessToken));
				
				//判断用户是否被禁用
				Ruser ruser = ruserService.findByUsername(oAuthService.getUsernameByAccessToken(accessToken));
				if (ruser != null && ruser.getStat()==2) {
					msg = generateMsg("你已被封号，请联系管理员",
							HttpServletResponse.SC_BAD_REQUEST + "_8");
					return new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
				}
				
				//用户信息放到redis
				putUserToRedis(request,oAuthService.getUsernameByAccessToken(accessToken));
				// 更新用户登陆时间
				ruserService.updateLoginDate(oAuthService.getUsernameByAccessToken(accessToken));
				
				if (ThirdUser.wechat.getValue().equals(data_from) && wechatUnionid != null && !"".equals(wechatUnionid)){  //如果是微信登陆，插入微信unionid
					ruserService.insertWechatUnionid(oAuthService.getUsernameByAccessToken(accessToken), wechatUnionid);
				}
				
				//新注册用户昵称加入solr引擎索引
	        	try {
	    			solrUserService.addUser(ruser.getId(), ruser.getName(),ruser.getPhone());
	    		} catch (SolrServerException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
				
				
				data.put(OAuth.OAUTH_EXPIRES_IN, expire);
				data.put(OAuth.OAUTH_BEARER_TOKEN, accessToken);
				data.put("isUserInfoCompletion", isUserInfoCompletion ? "Y"
						: "N");
				msg.put("result", data);
				return new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
			}

			Ruser ruser = null;
			logger.info("data_from=" + data_from + "&access_token=" + access_token + "&uid=" + uid + "&appId=" + this.request.getParameter("appId"));
			if("qq".equals(data_from)){
				ruser = ((QQCommon)common).getInfo(access_token, uid, this.request.getParameter("appId"));
			}else{
				ruser = common.getInfo(access_token, uid);
			}
			// 图片地址

			if (StringUtils.isBlank(ruser.getPic())) {
				String headPic = jedisService.get(BicycleConstants.DEFAULT_HEAD_PIC);
				ruser.setPic(headPic);
			} else {
				BufferedInputStream in = new BufferedInputStream(new URL(
						ruser.getPic()).openStream());

				String relPath = File.separator + "userHomePic" + File.separator
						+ DateFormatUtils.format(new Date(), "yyyy-MM-dd")
						+ File.separator;
				String originalFilename = DateFormatUtils.format(new Date(),
						"yyyy-MM-dd_HHmmss") + ".jpg";
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
				String relUrl = relPath + originalFilename;
				ruser.setPic(relUrl);
			}
			
			
			//判断注册用户平台
			String regPlatform = this.request.getParameter("regPlatform");
			if (regPlatform!=null && !"".equals(regPlatform)){
				ruser.setRegPlatform(regPlatform);
			} else {
				String platform = this.request.getParameter("osVersion");
		    	if (platform == null || "".equals(platform)){
		    		platform = this.request.getHeader("osVersion");
		    	}
		    	if (platform != null && platform.contains("ios")){
		    		platform = "ios";
		    	} else {
		    		platform = "android";
		    	}
		    	ruser.setRegPlatform(platform);
			}
			
			ruser.setIsBlacklist(1);//新用户默认可以提现
			ruser.setAppVersion(this.request.getParameter("appVersion"));
			ruserService.save(ruser);
			
			Map<String, String> tokenInfo1 =jedisService.getMapByKey(accessToken);
			tokenInfo1.put("id", ruser.getId().toString());
			jedisService.setValueToMap(accessToken, tokenInfo1);
			jedisService.setValueToSortedSetInShard(BicycleConstants.TO_BE_VERIFIED_PIC,System.currentTimeMillis(), ruser.getId()+"|head"); //修改头像，进入待审核队列
			boolean isUserInfoCompletion = ruserService
					.isUserInfoCompletion(oAuthService
							.getUsernameByAccessToken(accessToken));
			
			//用户信息放到redis
			putUserToRedis(request,oAuthService.getUsernameByAccessToken(accessToken));
			// 更新用户登陆时间
			ruserService.updateLoginDate(oAuthService.getUsernameByAccessToken(accessToken));
			
			if (ThirdUser.wechat.getValue().equals(data_from) && wechatUnionid != null && !"".equals(wechatUnionid)){  //如果是微信登陆，插入微信unionid
				ruserService.insertWechatUnionid(oAuthService.getUsernameByAccessToken(accessToken), wechatUnionid);
			}
			
			//新注册用户id放到redis里
	    	if (ruser.getId() != null && ruser.getId() != 0){
	    		//新注册用户昵称加入solr引擎索引
	        	try {
	    			//solrUserService.addUser(user.getId(), user.getName());
	    			solrUserService.addUser(ruser.getId(), ruser.getName(),ruser.getPhone());
	    		} catch (SolrServerException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    	}
	    	
	    	//360特殊渠道注册的用户赠送30金币
	    	if (StringUtils.isNotBlank(jedisService.get(BicycleConstants.REGISTER_360_SWITCH)) 
	    			&& "1".equals(jedisService.get(BicycleConstants.REGISTER_360_SWITCH)) 
	    			&& StringUtils.isNotBlank(regPlatform) 
	    			&& "360_tq".equals(regPlatform)) {
	    		pingPayService.giveDiamondByActivity(ruser, 30, Constants.GiveDiamondType.REGISTER_360.getType(), System.currentTimeMillis() / 1000l, this.request.getParameter("appVersion"), "android");
	    	}
			
			msg = generateMsg(null, HttpServletResponse.SC_OK);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(OAuth.OAUTH_EXPIRES_IN, expire);
			data.put(OAuth.OAUTH_BEARER_TOKEN, accessToken);
			data.put("isUserInfoCompletion", isUserInfoCompletion ? "Y" : "N");
			msg.put("result", data);
		} else {
			jedisService.setValueToList("thirdpart_reg_fail_"+nowYMD, now+"|"+data_from+"|"+access_token+"|"+uid+"|"+this.request.getParameter("regPlatform")+"|"+this.request.getParameter("appVersion"));
			msg = generateMsg("令牌不合法", HttpServletResponse.SC_BAD_REQUEST
					+ "_1");
			if (logger.isInfoEnabled())
				logger.info("令牌不合法");
			return new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
		}
		return new ResponseEntity<Map<String, Object>>(msg, HttpStatus.OK);
	}

	private Map<String, Object> generateMsg(String msg, Object code) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", code);
		String message = msg != null ? msg : "";
		result.put("message", message);
		return result;
	}
	
	//将用户信息放入redis
	private void putUserToRedis(HttpServletRequest request,String username){
		logger.info("put user to redis,username is {}",username);
		Ruser user = ruserService.findByUsername(username);
		if (user != null) {
			String key = BicycleConstants.USER_INFO+user.getId();
			jedisService.saveAsMap(key, user);
			//将用户手机平台信息放入redis
			String platform = request.getParameter("osVersion");
			//将设备唯一id放到redis
			String deviceUuid = request.getParameter("deviceUuid");
			if (platform != null){
				logger.info("put user to redis,platform is {}",platform);
				if (platform.contains("android"))
					jedisService.setValueToMap(key, "platform", "android");
				else if (platform.contains("ios"))
					jedisService.setValueToMap(key, "platform", "ios");
			} 
			if (deviceUuid != null){
				logger.info("put user to redis,deviceUuid is {}",deviceUuid);
				jedisService.setValueToMap(key, "deviceUuid", deviceUuid);
			} else {
				logger.info("put user to redis,deviceUuid is {}","");
				jedisService.setValueToMap(key, "deviceUuid", "");
			}
		}
	}
}
