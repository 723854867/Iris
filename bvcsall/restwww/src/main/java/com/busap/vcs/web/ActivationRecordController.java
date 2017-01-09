package com.busap.vcs.web;


import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.*;
import com.busap.vcs.service.*;
import com.busap.vcs.util.DateUtils;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.busap.vcs.webcomn.controller.CRUDController;


/**
 * 激活
 *
 * @author zx
 *
 */
@Controller()
@RequestMapping("activationRecord")
public class ActivationRecordController extends CRUDController<ActivationRecord, Long> {

	private static final Logger logger = LoggerFactory
			.getLogger(ActivationRecordController.class);

	@Resource(name = "activationRecordService")
	private ActivationRecordService activationRecordService;
	
	@Resource(name = "promotionRecordService")
	private PromotionRecordService promotionRecordService;
	
	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name = "startupRecordService")
	private StartupRecordService startupRecordService;


	@Resource(name = "activationRecordService")
	@Override
	public void setBaseService(BaseService<ActivationRecord, Long> baseService) {
		this.baseService = baseService;
	}
	
	@RequestMapping(value = {"/promotionRecordAdd"}, method = {RequestMethod.POST, RequestMethod.PUT,RequestMethod.GET})
	@ResponseBody
	public String promotionRecordAdd(
			@RequestParam(value = "mac", required = false)  String mac,
			@RequestParam(value = "ifa", required = false)  String ifa,
			@RequestParam(value = "callback_url", required = false)  String callback_url,
			@RequestParam(value = "fromType", required = false)  String fromType
			) throws Exception {
		
		PromotionRecord pr=new PromotionRecord();
		pr.setMac(mac);
		pr.setIfa(ifa);
		pr.setCallbackUrl(callback_url);
		pr.setFromType(fromType);
		promotionRecordService.save(pr);



        return "ok";
	}
	
	@RequestMapping(value = {"/promotionRecordAddForYoumi"}, method = {RequestMethod.POST, RequestMethod.PUT,RequestMethod.GET})
	@ResponseBody
	public String promotionRecordAddForYoumi(
			@RequestParam(value = "mac", required = false)  String mac,
			@RequestParam(value = "ifa", required = false)  String ifa,
			@RequestParam(value = "callback_url", required = false)  String callback_url
			) throws Exception {
		
		PromotionRecord pr=new PromotionRecord();
		pr.setMac(mac);
		pr.setIfa(ifa);
		pr.setCallbackUrl(callback_url);
		pr.setFromType(BicycleConstants.YOU_MI);
		promotionRecordService.save(pr);



        return "ok";
	}
	
	@RequestMapping(value = {"/promotionRecordAddForXingzhe"}, method = {RequestMethod.POST, RequestMethod.PUT,RequestMethod.GET})
	@ResponseBody
	public String promotionRecordAddForXingzhe(
			@RequestParam(value = "appid", required = false)  String appid,
			@RequestParam(value = "mac", required = false)  String mac,
			@RequestParam(value = "IDFA", required = false)  String IDFA,
			@RequestParam(value = "callback", required = false)  String callback
			) throws Exception {
		
		PromotionRecord pr=new PromotionRecord();
		pr.setMac(mac);
		pr.setIfa(IDFA);
		pr.setCallbackUrl(callback);
		pr.setFromType(BicycleConstants.XING_ZHE);
		promotionRecordService.save(pr);



        return "{message:\"数据接收成功\",success:\"true\"}";
	}
	
	@RequestMapping(value = {"/activationRecordAddByType"}, method = {RequestMethod.POST, RequestMethod.PUT,RequestMethod.GET})
	@ResponseBody
	public String activationRecordAddByType(
			@RequestParam(value = "mac", required = false)  String mac,
			@RequestParam(value = "ifa", required = false)  String ifa,
			@RequestParam(value = "fromType", required = false)  String fromType
			) throws Exception {
		
		ActivationRecord ar=new ActivationRecord();
		ar.setMac(mac);
		ar.setIfa(ifa);
		ar.setFromType(fromType);
		activationRecordService.save(ar);



        return "ok";
	}
	
	private static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                        .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }
	
	
	@RequestMapping(value = {"/activationRecordAdd"}, method = {RequestMethod.POST, RequestMethod.PUT,RequestMethod.GET})
	@ResponseBody
	public String activationRecordAdd(
			@RequestParam(value = "mac", required = false)  String mac,
			@RequestParam(value = "ifa", required = false)  String ifa,
			@RequestParam(value = "userId", required = false)  String userId,
			@RequestParam(value = "name", required = false)  String name
			) throws Exception {
		if(userId==null||userId.equals("")) {
			return "{\"result\":\"ok\"}";
		}
//		Long uc=ruserService.findCountByUserIdIfaNotNull(Long.valueOf(userId) );
		
		Ruser ruser=ruserService.find(Long.valueOf(userId));
		
		if(ruser==null) {
			return "{\"result\":\"ok\"}";
		}
		
		if(ruser.getIfa()!=null&&!ruser.getIfa().equals("")) {
			return "{\"result\":\"ok\"}";
		}
		
		if(ruser.getCreateDate()!=null) {
			boolean flg=isSameDate(new Date(ruser.getCreateDate()), new Date());
//			boolean flg=isSameDate(new Date(), new Date());
			if(flg) {
				
			}else {
				return "{\"result\":\"ok\"}";
			}
		}else {
			return "{\"result\":\"ok\"}";
		}
		
//		if(uc!=null&&uc>0) {
//			return "{\"result\":\"ok\"}";
//		}
		
		Long ct=activationRecordService.findCountByMacOrIfa(mac, ifa);
		if(ct !=null&&ct==0) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		       
	        Calendar c = Calendar.getInstance();  
	        c.add(Calendar.DATE, - 7);  
	        Date validTime = c.getTime();
//	        String preMonday = sdf.format(monday);
	        
	        int count=0;
	        
			//有米
			Long pct=promotionRecordService.findCountByMacOrIfaAndValidTime(mac, ifa, validTime,BicycleConstants.YOU_MI);
			if(pct!=null&&pct>0) {
				ActivationRecord ar=new ActivationRecord();
				ar.setMac(mac);
				ar.setIfa(ifa);
				ar.setFromType(BicycleConstants.YOU_MI);
				ar.setUserId(userId);
				ar.setName(name);
				activationRecordService.save(ar);
				
				if(userId!=null&&!userId.equals("")) {
//					Ruser ruser=ruserService.find(Long.valueOf(userId));
					ruser.setIfa(ifa);
					ruser.setRegPlatform("ios_youmi");
					ruserService.save(ruser);
				}
				
				
				
				count++;
				
				List prList= promotionRecordService.findByMacOrIfa(mac, ifa);
				if(prList!=null&&prList.size()>0) {
					
					PromotionRecord pr=(PromotionRecord) prList.get(0);
					if(pr!=null&&pr.getCallbackUrl()!=null&&!pr.getCallbackUrl().equals("")) {
						HttpClient client = new HttpClient();
						String url=pr.getCallbackUrl();
						url=URLDecoder.decode(url,"UTF-8");
					    PostMethod postMethod=new PostMethod(url);
					    postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
								new DefaultHttpMethodRetryHandler(3, false));
					    
//					    for (Map.Entry<String, String> entry : paramMap.entrySet()) {  
//					    	logger.info("post param key:{},value:{}",entry.getKey(),entry.getValue());
//					    	postMethod.addParameter(entry.getKey(),entry.getValue());   
//					    }  
					    
						String respContent = "";
						try {
							// Execute the method.
							int statusCode = client.executeMethod(postMethod);
							logger.info(postMethod.getResponseBodyAsString());
							if (statusCode == HttpStatus.SC_OK) {
								respContent = new String(postMethod.getResponseBody(),"utf-8");
								if(respContent.equals("{\"c\":0}")) {
									
								}else {
									if(respContent.equals("{\"c\":-3106}")) {
										
									}
									logger.error("有米渠道推广回调错误 " + respContent);
								}
							} else {
								logger.error("Response Code: " + statusCode);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							postMethod.releaseConnection();
						}
					}
					
				}
				
				return "{\"result\":\"ok\"}";
			}
			
			//行者
			pct=promotionRecordService.findCountByMacOrIfaAndValidTime(mac, ifa, validTime,BicycleConstants.XING_ZHE);
			if(pct!=null&&pct>0) {
				ActivationRecord ar=new ActivationRecord();
				ar.setMac(mac);
				ar.setIfa(ifa);
				ar.setFromType(BicycleConstants.XING_ZHE);
				ar.setUserId(userId);
				ar.setName(name);
				activationRecordService.save(ar);
				
				if(userId!=null&&!userId.equals("")) {
//					Ruser ruser=ruserService.find(Long.valueOf(userId));
					ruser.setIfa(ifa);
					ruser.setRegPlatform("ios_xingzhe");
					ruserService.save(ruser);
				}
				
				count++;
				
				List prList= promotionRecordService.findByMacOrIfaAndFromType(mac, ifa,BicycleConstants.XING_ZHE);
				if(prList!=null&&prList.size()>0) {
					
					PromotionRecord pr=(PromotionRecord) prList.get(0);
					if(pr!=null&&pr.getCallbackUrl()!=null&&!pr.getCallbackUrl().equals("")) {
						HttpClient client = new HttpClient();
						String url=pr.getCallbackUrl();
						url=URLDecoder.decode(url,"UTF-8");
					    PostMethod postMethod=new PostMethod(url);
					    postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
								new DefaultHttpMethodRetryHandler(3, false));
					    
//					    for (Map.Entry<String, String> entry : paramMap.entrySet()) {  
//					    	logger.info("post param key:{},value:{}",entry.getKey(),entry.getValue());
//					    	postMethod.addParameter(entry.getKey(),entry.getValue());   
//					    }  
					    
						String respContent = "";
						try {
							// Execute the method.
							int statusCode = client.executeMethod(postMethod);
							logger.info(postMethod.getResponseBodyAsString());
							if (statusCode == HttpStatus.SC_OK) {
								respContent = new String(postMethod.getResponseBody(),"utf-8");
//								if(respContent.equals("{\"c\":0}")) {
//									
//								}else {
//									if(respContent.equals("{\"c\":-3106}")) {
//										
//									}
//									logger.error("行者渠道推广回调错误 " + respContent);
//								}
							} else {
								logger.error("Response Code: " + statusCode);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							postMethod.releaseConnection();
						}
					}
					
				}
				
				return "{\"result\":\"ok\"}";
			}
			
			
			//如果没有渠道做推广，就是自己过来的用户
			if(count==0) {
				ActivationRecord ar=new ActivationRecord();
				ar.setMac(mac);
				ar.setIfa(ifa);
				ar.setUserId(userId);
				ar.setName(name);
				activationRecordService.save(ar);
				
				if(userId!=null&&!userId.equals("")) {
//					Ruser ruser=ruserService.find(Long.valueOf(userId));
					if(ruser!=null) {
						ruser.setIfa(ifa);
						ruserService.save(ruser);
					}
					
				}
			}
			
			
			
		}if(ct !=null&&ct>0) {
			if(userId!=null&&!userId.equals("")) {
//				Ruser ruser=ruserService.find(Long.valueOf(userId));
				if(ruser!=null) {
					ruser.setIfa(ifa);
					ruserService.save(ruser);
				}
			}
			
		}
		



        return "{\"result\":\"ok\"}";
	}
	
	@RequestMapping(value = {"/findARByIfa"}, method = {RequestMethod.POST, RequestMethod.PUT,RequestMethod.GET})
	@ResponseBody
	public Map findARByIfa(
			@RequestParam(value = "appid", required = false)  String appid,
			@RequestParam(value = "idfa", required = false)  String idfa
			) throws Exception {
		
		Map arMap=new HashMap();
		
		if(appid!=null&&!appid.equals("")&&appid.equals("1077305226")) {
			if(idfa!=null&&!idfa.equals("")) {
				String[] idfas=idfa.split("\\,");
				if(idfas!=null&&idfas.length>0) {
					for(int i=0;i<idfas.length;i++) {
						Long tc=activationRecordService.findCountByIfa(idfas[i]);
						if(tc>0) {
							arMap.put(idfas[i], 1);
						}else {
							arMap.put(idfas[i],0);
						}
					}
				}
			}else {
				
			}
			
		}
		



        return arMap;
	}
	
	@RequestMapping(value = {"/startupRecordAdd"}, method = {RequestMethod.POST, RequestMethod.PUT,RequestMethod.GET})
	@ResponseBody
	public String startupRecordAdd(
			@RequestParam(value = "vNo", required = false)  String vNo,
			@RequestParam(value = "ifa", required = false)  String ifa,
			@RequestParam(value = "createDate", required = false)  String createDate
			) throws Exception {
		if(ifa!=null&&!ifa.equals("")) {
			Long count=startupRecordService.findCountByIfa(ifa);
			if(count>0) {
				return "exist";
			}
			
			StartupRecord sr=new StartupRecord();
//			sr.setCreateDate(DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",createDate));
			sr.setvNo(vNo);
			sr.setIfa(ifa);
			startupRecordService.save(sr);



	        return "ok";
		}else {
			return "ifaNull";
		}

	}


}
