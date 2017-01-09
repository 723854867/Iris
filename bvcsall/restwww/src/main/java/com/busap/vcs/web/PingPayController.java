package com.busap.vcs.web;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.*;
import com.busap.vcs.data.vo.LoadConfigUrlVO;
import com.busap.vcs.service.*;
import com.busap.vcs.service.utils.JsonUtil;
import com.busap.vcs.service.utils.OrderStatus;
import com.busap.vcs.util99bill.Pkipair;
import com.busap.vcs.utils.IOS_Verify;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Transfer;
import com.pingplusplus.model.Webhooks;

import common.Base64;
import kafka.utils.Json;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.params.CookiePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 支付controller (Ping)
 * Created by Knight on 15/12/21.
 */
@Controller
@RequestMapping("/pay")
public class PingPayController {

    @Autowired
    protected HttpServletRequest request;

    @Value("${payment_live_mode}")
    private String liveMode;

    @Value("${payment_app_id}")
    private String APP_ID;

    @Value("${payment_sk}")
    private String PAYMENT_SK;

    @Value("${payment_pub_key}")
    private String PAYMENT_PUB_KEY;

    @Value("${verify_code}")
    private String VERIFY_CODE;

    @Autowired
    private ExchangeService exchangeService;
    
    @Autowired
    private DiamondService diamondService;

    @Autowired
    private PingPayService pingPayService;

    @Autowired
    private AnchorService anchorService;

    @Autowired
    private AppVerifyRecodeService appVerifyRecodeService;

    @Resource(name="ruserService")
    private RuserService ruserService;
    
    @Resource(name = "loadConfigUrlService")
   	private LoadConfigUrlService loadConfigUrlService;

    @Autowired
    private JedisService jedisService;

    private Logger logger = LoggerFactory.getLogger(PingPayController.class);

    @Resource(name = "respBodyBuilder")
    private RespBodyBuilder respBodyWriter = new RespBodyBuilder();

    // 单日最大提现数值 2000元
    private static final int MaxTransferAmount = 200000;

    @RequestMapping("/callback")
    public void callbackHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF8");
        //获取头部所有信息
        Enumeration headerNames = request.getHeaderNames();
        String signature = "";
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            if ("x-pingplusplus-signature".equals(key)) {
                signature = request.getHeader(key);
            }
        }


        if (StringUtils.isBlank(signature)) {
            response.setStatus(501);
        } else {
            // 获得 http body 内容
            BufferedReader reader = request.getReader();
            StringBuilder builder = new StringBuilder();
            String string;
            while ((string = reader.readLine()) != null) {
                builder.append(string);
            }
            reader.close();
            // 解析异步通知数据
            String message = builder.toString();
            logger.info("ping plus callback massage:" + message);
            try {
                // Verify signature
                boolean verify = pingPayService.verifySignature(message, signature, PAYMENT_PUB_KEY);
                if (!verify) {
                    logger.info("=========> verify signature Failed!");
                    response.setStatus(509);
                }
            } catch (Exception e) {
                logger.info("=========> verify signature error!");
                response.setStatus(503);
            }
            Event event = Webhooks.eventParse(message);
            if ("charge.succeeded".equals(event.getType())) {
                // 付款成功 回调
                Map<String, Object> objectMap = event.getData();
                if (pingPayService.paySuccess(objectMap, APP_ID)) {
                    response.setStatus(200);
                } else {
                    logger.info("=========> operate pay error! " + JSONObject.fromObject(objectMap.get("object")).toString());
                    response.setStatus(502);
                }
            } else if ("transfer.succeeded".equals(event.getType())) {
                // 企业转账成功 回调
                Map<String, Object> objectMap = event.getData();
                if (pingPayService.transferSuccess(objectMap, APP_ID)) {
                    response.setStatus(200);
                } else {
                    logger.info("=========> operate transfer error! " + JSONObject.fromObject(objectMap.get("object")).toString());
                    response.setStatus(502);
                }

            } else {
                response.setStatus(500);
            }
        }
    }

    @RequestMapping("/create")
    @ResponseBody
    public RespBody createHandler(String paramJson, String appVersion, String osVersion) {
        // 设置apiKey用于Authentication认证
        Pingpp.apiKey = PAYMENT_SK;
        JSONObject jsonObject = JSONObject.fromObject(paramJson);
        String channel = (String) jsonObject.get("channel");
        String clientIp = getIpAddress(request);
        Integer userId = (Integer) jsonObject.get("userId");
        Integer produceId = (Integer) jsonObject.get("produceId");
        String openId = (String) jsonObject.get("openId");
        String platform = "android";
        if (osVersion != null && osVersion.contains("ios")){
            platform = "ios";
        }

        if (!checkEmptyValue(channel, clientIp) || userId == null || produceId == null) {
            logger.error("=========> 参数不全，有部分参数为空");
            return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
        }
        try {
            Charge charge = pingPayService.createOrder(channel, clientIp, APP_ID, Long.valueOf(produceId), Long.valueOf(userId), openId, appVersion, platform);
            if (charge == null) {
                logger.error("=========> " + ResponseCode.CODE_608.toString());
                return respBodyWriter.toError(ResponseCode.CODE_608.toString(), ResponseCode.CODE_608.toCode());
            }
            return respBodyWriter.toSuccess(charge);
        } catch (AuthenticationException e) {
            logger.error("=========> " + ResponseCode.CODE_603.toString());
            e.printStackTrace();
            return respBodyWriter.toError(ResponseCode.CODE_603.toString(), ResponseCode.CODE_603.toCode());
        } catch (InvalidRequestException e) {
            logger.error("=========> " + ResponseCode.CODE_604.toString());
            e.printStackTrace();
            return respBodyWriter.toError(ResponseCode.CODE_604.toString(), ResponseCode.CODE_604.toCode());
        } catch (APIConnectionException e) {
            logger.error("=========> " + ResponseCode.CODE_605.toString());
            e.printStackTrace();
            return respBodyWriter.toError(ResponseCode.CODE_605.toString(), ResponseCode.CODE_605.toCode());
        } catch (APIException e) {
            logger.error("=========> " + ResponseCode.CODE_606.toString());
            e.printStackTrace();
            return respBodyWriter.toError(ResponseCode.CODE_606.toString(), ResponseCode.CODE_606.toCode());
        } catch (ChannelException e) {
            logger.error("=========> " + ResponseCode.CODE_607.toString());
            e.printStackTrace();
            return respBodyWriter.toError(ResponseCode.CODE_607.toString(), ResponseCode.CODE_607.toCode());
        }
    }
    
    
    
    
    @RequestMapping("/createYYB")
    @ResponseBody
    public RespBody createYYB(String paramJson,
    						  @RequestParam(value = "openid", required = true)  String openid,//从手Q登录态中获取的openid的值
    						  @RequestParam(value = "openkey ", required = true)  String openkey ,//从手Q登录态中获取的access_token 的值 
    						  @RequestParam(value = "pay_token", required = true)  String pay_token,//从手Q登录态中获取的pay_token的值 
//    						  @RequestParam(value = "appid", required = true)  String appid,//应用的唯一ID。可以通过appid查找APP基本信息。这个appid在数值上和支付ID也就是客户端设置的offerid是相同的 
//    						  @RequestParam(value = "ts", required = false)  Long ts,//UNIX时间戳（从格林威治时间1970年01月01日00时00分00秒起至现在的总秒数）。 
    						  
//    						  @RequestParam(value = "payitem", required = false)  String payitem,//请使用x*p*num的格式，x表示物品ID，p表示单价（以Q点为单位，1Q币=10Q点，单价的制定需遵循腾讯定价规范），num表示默认的购买数量。（格式：物品ID1*单价1*建议数量1，批量购买物品时使用;分隔，如：id1*price1*num1;id2*price2*num2)长度必须<=512。
    						  @RequestParam(value = "produceId", required = false)  String produceId,//
    						  @RequestParam(value = "price", required = false)  String price,//
    						  @RequestParam(value = "num", required = false)  String num,//
    						  
//    						  @RequestParam(value = "goodsmeta", required = false)  String goodsmeta,//物品信息，格式必须是“name*des”，批量购买套餐时也只能有1个道具名称和1个描述，即给出该套餐的名称和描述。name表示物品的名称，des表示物品的描述信息。用户购买物品的确认支付页面，将显示该物品信息。长度必须<=256字符，必须使用utf8编码。目前goodsmeta超过76个字符后不能添加回车字符。
    						  @RequestParam(value = "name", required = false)  String name,//
    						  @RequestParam(value = "des", required = false)  String des,//
    						  
    						  @RequestParam(value = "goodsurl", required = false)  String goodsurl,//物品的图片url(长度<512字符)。 
//    						  @RequestParam(value = "sig", required = false)  String sig,//请求串的签名（可以参考下面具体示例，或者到wiki下载SDK）。 
    						  @RequestParam(value = "pf", required = true)  String pf,//(前端给)平台来源，平台-注册渠道-系统运行平台-安装渠道-业务自定义。例如：desktop_m_qq-10000144-android-2002--xxxx qq_m_qq 表示 手Q平台启动，用qq登录态 
//    						  @RequestParam(value = "zoneid", required = false)  String zoneid,//(默认不分区是1)账户分区ID_角色。和PC接入时保持一致，分区可以在open.qq.com上自助配置。
    						  																	//注意：如果游戏PC端和移动端是游戏币互通模式，zoneid 的数值保持和PC侧接入支付时传递的一致。例如，游戏A在PC上运行是接入过支付，PC上有分区0和分区1，再接入移动支付是要求移动端账户和PC端账户互通，那么接入移动支付时，zoneid根据需要由业务自己传递PC上配置的分区ID，对0分区操作就传0，对1分区操作就传1

    						  @RequestParam(value = "pfkey", required = true)  String pfkey,//登录成功后平台直接传给应用，应用原样传给平台即可强校验pfKey=”58FCB2258B0BF818008382BD025E8022”（来自平台）,自研应用固定传 pfkey="pfkey。
    						  @RequestParam(value = "amt", required = false)  String amt,//(可选)道具总价格。（amt必须等于所有物品：单价*建议数量的总和,该参数的单位为0.1Q点，即1分钱）。 
    						  @RequestParam(value = "max_num", required = false)  String max_num,//(可选) 用户可购买的道具数量的最大值。  仅当appmode的值为2时，可以输入该参数。输入的值需大于参数“payitem”中的num，如果小于num，则自动调整为num的值。 
    						  @RequestParam(value = "appmode", required = false)  String appmode,//(可选)1表示用户不可以修改物品数量，2 表示用户可以选择购买物品的数量。默认2（批量购买的时候，必须等于1）。 
    						  @RequestParam(value = "app_metadata", required = false)  String app_metadata,//（可选）发货时透传给应用，长度必须<=128字符。 
    						  @RequestParam(value = "userip", required = false)  String userip,//（可选）用户的外网IP。 
    						  @RequestParam(value = "format", required = false)  String format,//（可选）json、jsonp_$func。默认json。如果jsonp，前缀为：$func  例如：format=jsonp_sample_pay，返回格式前缀为：sample_pay()。 
    						  @RequestParam(value = "appVersion", required = false)  String appVersion,
    						  @RequestParam(value = "osVersion", required = false)  String osVersion) {



		//用户账户类型，session_id =“openid”(固定传“openid”，不是传openid值) 
    	String session_id ="openid";
			
		//session类型，session_type = “kp_actoken”(固定传”kp_actoken”，不是传kp_actoken值)
		String	session_type ="kp_actoken";
			
		//需要填写: /mpay/buy_goods_m 
		String	org_loc ="/mpay/buy_goods_m";
			
		String	appip ="1105498927";
		
		
		
		String appkey="Lf6AtMEB1QlE8BYS&";
//		 method:GET;
		String url_path="/mpay/buy_goods_m";
		 

		openid ="F11669C63D76BAB0BC2F6CC869B19E53";
		 openkey ="3968DD5F3F14427EF103A05E00AB59B4";
		 pf ="desktop_m_qq-10000144-android-2002-";
		 pfkey ="d0cd576ad99fcea674f09ce24da65345";
		 pay_token ="91E5CE357A0EE02C9C105FBF95703001";
		 String ts ="1396324143";
		 String payitem ="G1*20*2";
		 String goodsmeta ="name*goodsinfo";
		 goodsurl ="http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif";
		 String zoneid ="1";
		 app_metadata ="customkey";
		 format ="json";
		 String appid ="1101255891";

			
















        // 设置apiKey用于Authentication认证
        Pingpp.apiKey = PAYMENT_SK;
        JSONObject jsonObject = JSONObject.fromObject(paramJson);
        String channel = (String) jsonObject.get("channel");
        String clientIp = getIpAddress(request);
        Integer userId = (Integer) jsonObject.get("userId");
        Integer produceId1 = (Integer) jsonObject.get("produceId");
        String openId = (String) jsonObject.get("openId");
        String platform = "android";
        if (osVersion != null && osVersion.contains("ios")){
            platform = "ios";
        }

        if (!checkEmptyValue(channel, clientIp) || userId == null || produceId == null) {
            logger.error("=========> 参数不全，有部分参数为空");
            return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
        }
        try {
            Charge charge = pingPayService.createOrder(channel, clientIp, APP_ID, Long.valueOf(produceId), Long.valueOf(userId), openId, appVersion, platform);
            if (charge == null) {
                logger.error("=========> " + ResponseCode.CODE_608.toString());
                return respBodyWriter.toError(ResponseCode.CODE_608.toString(), ResponseCode.CODE_608.toCode());
            }
            return respBodyWriter.toSuccess(charge);
        } catch (AuthenticationException e) {
            logger.error("=========> " + ResponseCode.CODE_603.toString());
            e.printStackTrace();
            return respBodyWriter.toError(ResponseCode.CODE_603.toString(), ResponseCode.CODE_603.toCode());
        } catch (InvalidRequestException e) {
            logger.error("=========> " + ResponseCode.CODE_604.toString());
            e.printStackTrace();
            return respBodyWriter.toError(ResponseCode.CODE_604.toString(), ResponseCode.CODE_604.toCode());
        } catch (APIConnectionException e) {
            logger.error("=========> " + ResponseCode.CODE_605.toString());
            e.printStackTrace();
            return respBodyWriter.toError(ResponseCode.CODE_605.toString(), ResponseCode.CODE_605.toCode());
        } catch (APIException e) {
            logger.error("=========> " + ResponseCode.CODE_606.toString());
            e.printStackTrace();
            return respBodyWriter.toError(ResponseCode.CODE_606.toString(), ResponseCode.CODE_606.toCode());
        } catch (ChannelException e) {
            logger.error("=========> " + ResponseCode.CODE_607.toString());
            e.printStackTrace();
            return respBodyWriter.toError(ResponseCode.CODE_607.toString(), ResponseCode.CODE_607.toCode());
        }
    }
    
    
    @RequestMapping(value = {"/payMYYB"}, method = {RequestMethod.GET,RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RespBody payMYYB(@RequestParam(value = "openid", required = true)  String openid,//从手Q登录态中获取的openid的值
    						  @RequestParam(value = "openkey", required = true)  String openkey ,//从手Q登录态中获取的access_token 的值 
    						  @RequestParam(value = "pay_token", required = true)  String pay_token,//从手Q登录态中获取的pay_token的值 
    						  @RequestParam(value = "produceId", required = false)  Long produceId,//
    						  @RequestParam(value = "pf", required = true)  String pf,//(前端给)平台来源，平台-注册渠道-系统运行平台-安装渠道-业务自定义。例如：desktop_m_qq-10000144-android-2002--xxxx qq_m_qq 表示 手Q平台启动，用qq登录态 
    						  @RequestParam(value = "pfkey", required = true)  String pfkey,//登录成功后平台直接传给应用，应用原样传给平台即可强校验pfKey=”58FCB2258B0BF818008382BD025E8022”（来自平台）,自研应用固定传 pfkey="pfkey。
//    						  @RequestParam(value = "amt", required = false)  String amt,//(可选)道具总价格。（amt必须等于所有物品：单价*建议数量的总和,该参数的单位为0.1Q点，即1分钱）。 
    						  @RequestParam(value = "format", required = false)  String format,//（可选）json、jsonp_$func。默认json。如果jsonp，前缀为：$func  例如：format=jsonp_sample_pay，返回格式前缀为：sample_pay()。 
    						  
    						  @RequestParam(value = "appVersion", required = true)  String appVersion,//
    						  @RequestParam(value = "loginType", required = true)  String loginType,
    						  
    						  HttpServletRequest request) {

    	String uid = this.request.getHeader("uid");
    	
//    	pingPayService.updateOrderPayStatusByOrderNo( "1d2ac576881b4e31a309b7c81b37a55b",  String.valueOf(OrderStatus.PAID.getStatus()));
    	
    	Map<String, String> map = new HashMap<String, String>();

    	
//    	String httpUrl="http://119.147.19.43";
//    	String httpUrl="https://openapi.tencentyun.com";
    	
//    	String httpUrl="https://ysdktest.qq.com";
    	String httpUrl="https://ysdk.qq.com"; 

		//用户账户类型，session_id =“openid”(固定传“openid”，不是传openid值) 
    	String session_id ="openid";
		//session类型，session_type = “kp_actoken”(固定传”kp_actoken”，不是传kp_actoken值)
		String	session_type ="kp_actoken";
		//需要填写:/mpay/get_balance_m  查询游戏币余额接口
		String	org_loc ="/mpay/get_balance_m";
		//客户端ip	
		String	appip =getIpAddress(request);
		String cookieUrlFind="session_id=openid;session_type=kp_actoken;org_loc=/mpay/get_balance_m;";
		String cookieUrlBuy= "session_id=openid;session_type=kp_actoken;org_loc=/mpay/pay_m;";
		
		//应用的唯一ID。可以通过appid查找APP基本信息。这个appid在数值上和支付ID也就是客户端设置的offerid是相同的
		
		//沙箱环境
		String	appid ="1105498927";//
		
//		String appkey="ogOL0k5PVuylEclcde4jwm7SD0S8Cni9&";//
		
//		String	appid ="1105498927";//手Q
//		
//		String appkey="05vKVqvZR43XlJO6&";//手Q
		String appkey="0pCN4WorP0QsL2kleEW9YPn5Zfu9groi&";//手Q
		
		if(loginType!=null&&loginType.equals("wx")) {
//			appid ="wx90d02cc308b60fb4";//wx
			
//			appkey="dd55a0bd517a45250d0b58b25665e84a&";//wx
			
			cookieUrlFind="session_id=hy_gameid;session_type=wc_actoken;org_loc=/mpay/get_balance_m;";
			cookieUrlBuy= "session_id=hy_gameid;session_type=wc_actoken;org_loc=/mpay/pay_m;";
			
		}
		
		//UNIX时间戳（从格林威治时间1970年01月01日00时00分00秒起至现在的总秒数）。 
		Long ts=System.currentTimeMillis()/1000;  
		
		String uriFind="get_balance_m"; 
		
		String uriBuy="pay_m";
		 
		 String uriFindNew="";
		 
		 try {
			 System.out.println("/v3/r/mpay/"+uriFind);
			 uriFindNew=URLEncoder.encode("/v3/r/mpay/"+uriFind,"UTF-8");
			 uriFindNew=uriFindNew.replace("*", "%2A");
			 uriFindNew=uriFindNew.replace("+", "%2B");
			 
			System.out.println(uriFindNew);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 


		 String paramFind="appid="+appid+
//		 				  "&format=json"+
		 				  "&openid="+openid+
		 				  "&openkey="+openkey+
//		 				  "&pay_token="+pay_token+
		 				  "&pf="+pf+
		 				  "&pfkey="+pfkey+
		 				  "&ts="+ts+
//		 				  "&userip="+appip+
		 				  "&zoneid=1"+
		 				  	"";
		 
		 String paramFindNew="";

		 try {
			 paramFindNew=URLEncoder.encode(paramFind,"UTF-8");
			 paramFindNew=paramFindNew.replace("*", "%2A");
			 paramFindNew=paramFindNew.replace("+", "%2B");
			 
			System.out.println(paramFindNew);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		 
		 
		 
		 String sigFind="GET"+"&"+uriFindNew+"&"+paramFindNew;
		 
		 System.out.println(sigFind);
		 
		 String sigFindNew ="";
		 
		 try {
			byte[] byBuffer=HmacSHA1Encrypt(sigFind, appkey);
			
			sigFindNew = new String(byBuffer,"UTF-8");
			
			String base64Text = Base64.encodeToString(byBuffer, Base64.DEFAULT).trim();
			
			System.out.println(sigFindNew);
			
			System.out.println(base64Text);
			
			String base64TextNew="";
			
			base64TextNew=URLEncoder.encode(base64Text,"UTF-8");
			System.out.println(base64TextNew);
			base64TextNew=base64TextNew.replace("*", "%2A");
			System.out.println(base64TextNew);
			base64TextNew=base64TextNew.replace("+", "%2B");
			
			System.out.println(base64TextNew);
			
			sigFindNew=base64TextNew;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 String urlFind=httpUrl+"/mpay/"+uriFind+"?"+paramFind+"&sig="+sigFindNew;
		 
		 System.out.println(urlFind);
		
		
		HttpClient client = new HttpClient();
////		urlFind=URLDecoder.decode(urlFind,"UTF-8");
	    GetMethod getMethod=new GetMethod(urlFind);
	    getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
	    
	    //设置cookie
	    getMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
	    
	    getMethod.setRequestHeader("Cookie", cookieUrlFind);

	    
	    
		String respContent = "";
		try {
			// Execute the method.
			int statusCode = client.executeMethod(getMethod);
			logger.info(getMethod.getResponseBodyAsString());
			if (statusCode == HttpStatus.SC_OK) {
				respContent = new String(getMethod.getResponseBody(),"utf-8");
				if(respContent!=null&&!respContent.equals("")) {
					Map m = (Map)JsonUtil.convertJson2Object(respContent, Map.class);
					if(m!=null) {
						if(m.get("ret")!=null) {
							if(((Integer)m.get("ret")).intValue()==0) {
								
								Integer balance=(Integer)m.get("balance");
								Diamond diamond = diamondService.selectByPrimaryKey(produceId);
								
								int dc=diamond.getDiamondCount()!=null?diamond.getDiamondCount():0;
								int gc=diamond.getGiveCount()!=null?diamond.getGiveCount():0;
								
//								dc=1;
//								gc=0;
								
								if(dc+gc>balance) {
									//余额不足，可能刷的
									map.put("ret", "1001");
									
								}else {
									Integer amt=dc+gc;
									
									
									//需要填写:/mpay/pay_m  扣除游戏币接口
									org_loc ="/mpay/pay_m";
									
									
									
									
									
									//UNIX时间戳（从格林威治时间1970年01月01日00时00分00秒起至现在的总秒数）。 
									ts=System.currentTimeMillis()/1000;  
									
									 
									 String uriBuyNew="";
									 
									 try {
										 uriBuyNew=URLEncoder.encode("/v3/r/mpay/"+uriBuy,"UTF-8");
										 uriBuyNew=uriBuyNew.replace("*", "%2A");
										 uriBuyNew=uriBuyNew.replace("+", "%2B");
										 
										System.out.println(uriBuyNew);
									} catch (UnsupportedEncodingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									 

									 String billno="";
									 String orderNo="";
									 String data = pingPayService.yybPay("yyb", appip, Long.valueOf(produceId),Long.valueOf(uid), "", OrderStatus.CREATE.getStatus(), appVersion, "android");

									 Map dataMap = (Map)JsonUtil.convertJson2Object(data, Map.class);
										if(dataMap!=null) {
											if(dataMap.get("orderNo")!=null) {
												billno=(String)dataMap.get("orderNo");
												orderNo=billno;
											}
										}
									
									 String paramBuy="amt="+amt+
											 		  "&appid="+appid+
											 		  "&billno="+billno+
									 				  "&format=json"+
									 				  "&openid="+openid+
									 				  "&openkey="+openkey+
//									 				  "&pay_token="+pay_token+
									 				  "&pf="+pf+
									 				  "&pfkey="+pfkey+
									 				  "&ts="+ts+
									 				  "&zoneid=1";
									 
									 String paramBuyNew="";

									 try {
										 paramBuyNew=URLEncoder.encode(paramBuy,"UTF-8");
										 paramBuyNew=paramBuyNew.replace("*", "%2A");
										 paramBuyNew=paramBuyNew.replace("+", "%2B");
										 
										System.out.println(paramBuyNew);
									} catch (UnsupportedEncodingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									 
									 
									 
									 
									 
									 String sigBuy="GET"+"&"+uriBuyNew+"&"+paramBuyNew;
									 
									 System.out.println(sigBuy);
									 
									 String sigBuyNew ="";
									 
									 try {
										byte[] byBuffer=HmacSHA1Encrypt(sigBuy, appkey);
										
										sigBuyNew = new String(byBuffer,"UTF-8");
										
										String base64Text = Base64.encodeToString(byBuffer, Base64.DEFAULT).trim();
										
										System.out.println(sigBuyNew);
										
										System.out.println(base64Text);
										
										String base64TextNew="";
										
										base64TextNew=URLEncoder.encode(base64Text,"UTF-8");
										System.out.println(base64TextNew);
										base64TextNew=base64TextNew.replace("*", "%2A");
										System.out.println(base64TextNew);
										base64TextNew=base64TextNew.replace("+", "%2B");
										
										System.out.println(base64TextNew);
										
										sigBuyNew=base64TextNew;
										
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									 
									 String urlBuy=httpUrl+"/mpay/"+uriBuy+"?"+paramBuy+"&sig="+sigBuyNew;;
									
									System.out.println(urlBuy);
									
									HttpClient clientBuy = new HttpClient();
//									urlBuy=URLDecoder.decode(urlBuy,"UTF-8");
								    GetMethod getMethodBuy=new GetMethod(urlBuy);
								    getMethodBuy.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
											new DefaultHttpMethodRetryHandler(3, false));
								    
								    //设置cookie
								    getMethodBuy.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
								    
								    getMethodBuy.setRequestHeader("Cookie", cookieUrlBuy);

								    
								    
									String respContentBuy = "";
									try {
										// Execute the method.
										int statusCodeBuy = clientBuy.executeMethod(getMethodBuy);
										logger.info(getMethodBuy.getResponseBodyAsString());
										if (statusCodeBuy == HttpStatus.SC_OK) {
											respContentBuy = new String(getMethodBuy.getResponseBody(),"utf-8");
											if(respContentBuy!=null&&!respContentBuy.equals("")) {
												Map mBuy = (Map)JsonUtil.convertJson2Object(respContentBuy, Map.class);
												if(mBuy!=null) {
													if(mBuy.get("ret")!=null) {
														if(((Integer)mBuy.get("ret")).intValue()==0) {
															
//															Diamond diamond = diamondService.selectByPrimaryKey(produceId);
															

															pingPayService.updateOrderPayStatusByOrderNo( orderNo,  String.valueOf(OrderStatus.PAID.getStatus()));
//															Ruser ruser = ruserService.find(Long.valueOf(uid));
//													        if (ruser == null) {
//													            return null;
//													        }
//											                ruser.setDiamondCount(ruser.getDiamondCount() + diamond.getDiamondCount() + diamond.getGiveCount());
//											                ruserService.update(ruser);
											                
															//防止用户刷单
											                ruserService.updateDiamond(Long.valueOf(uid), Integer.valueOf(diamond.getDiamondCount() + diamond.getGiveCount()));
											                
															map.put("ret", "0");
						                                    return respBodyWriter.toSuccess(map);
															
															
														}else if(((Integer)mBuy.get("ret")).intValue()==1004) {
															map.put("ret", "1004");
														}else if(((Integer)mBuy.get("ret")).intValue()==1018) {
															map.put("ret", "1018");
														}
														
													}
													
												}
											}
											
											
										} else {
											logger.error("Response Code: " + statusCode);
										}
									} catch (Exception e) {
										e.printStackTrace();
									} finally {
										getMethod.releaseConnection();
									}
								}
								
							}else if(((Integer)m.get("ret")).intValue()==1001) {
								map.put("ret", "1001");
							}else if(((Integer)m.get("ret")).intValue()==1008) {
								map.put("ret", "1008");
							}else if(((Integer)m.get("ret")).intValue()==-1) {
								map.put("ret", "-1");
							}
							
						}
						
					}
				}
				
				
			} else {
				logger.error("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return respBodyWriter.toSuccess(map);
		





    }

//    @RequestMapping("/appStorePay")
//    @ResponseBody
//    public RespBody appStorePayHandler(String paramJson, String verification) {
//        return respBodyWriter.toError("保存订单失败");
////        if (verify(paramJson, verification)) {
////            JSONObject jsonObject = JSONObject.fromObject(paramJson);
////            String clientIp = getIpAddress(request);
////            Integer userId = (Integer) jsonObject.get("userId");
////            Integer produceId = (Integer) jsonObject.get("produceId");
////            String transactionNo = (String) jsonObject.get("transactionNo");
////
////            if (pingPayService.checkTransactionNoExist("app_store", transactionNo)) {
////                return respBodyWriter.toError("重复请求");
////            }
////
////            String data = pingPayService.appStorePay("app_store", clientIp, Long.valueOf(produceId), Long.valueOf(userId), transactionNo, OrderStatus.PAID.getStatus());
////            if (StringUtils.isNotBlank(data)) {
////                return respBodyWriter.toSuccess(data);
////            } else {
////                logger.error("app store支付：保存订单失败！params=" + paramJson);
////                return respBodyWriter.toError("保存订单失败");
////            }
////        } else {
////            return respBodyWriter.toError("验证失败！");
////        }
//
//    }

    @RequestMapping("/getDisplayData")
    @ResponseBody
    public RespBody getDisplayData(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
        }
        Map<String, Object> map = pingPayService.getDisplayData(orderNo);
        if (map != null) {
            return respBodyWriter.toSuccess(map);
        } else {
            return respBodyWriter.toError(ResponseCode.CODE_609.toString(), ResponseCode.CODE_609.toCode());
        }
    }

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public RespBody getUserInfoHandler(Long userId) {
        if (userId == null) {
            return respBodyWriter.toError(ResponseCode.CODE_305.toString(), ResponseCode.CODE_305.toCode());
        } else {
            Ruser ruser = ruserService.find(userId);
            if (ruser == null) {
                // 查此userId是否有效
                return respBodyWriter.toError(ResponseCode.CODE_305.toString(), ResponseCode.CODE_305.toCode());
            } else {
                return respBodyWriter.toSuccess(ruser);
            }
        }
    }

    /**
     * 微信取openID ----> 公众号充值
     * @param request request
     * @return result
     */
    @RequestMapping("/wechatCallback")
    public String wechatCallback(HttpServletRequest request) {
        String code = request.getParameter("code");
        String openId = "";
        try {
            openId = WxpubOAuth.getOpenId("wx8a908b16f1e26ade", "7a7b0afabac3902b936798ecbecd7141", code);
            logger.info("wechat open id=" +openId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.request.setAttribute("openId", openId);
        return "html5/app_within/Recharge/wxrecharge/recharge";
    }

    /**
     * 微信取openID ----> 公众号提现
     * @param request request
     * @return result
     */
    @RequestMapping("/withdraw")
    public String withdraw(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        String openId = "";
        try {
            openId = WxpubOAuth.getOpenId("wx8a908b16f1e26ade", "7a7b0afabac3902b936798ecbecd7141", code);
            logger.info("wechat open id=" +openId);
            if (StringUtils.isBlank(openId)) {
                return "html5/app_within/Recharge/wxrecharge/login";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.request.setAttribute("openId", openId);
        Map<String,String> cookies = new HashMap<String,String>();
        if (StringUtils.isNotBlank(openId)) {
            cookies.put("openId", openId);
            setCookie(response, cookies, 7*24*60*60);
        }

        Map<String,Object> params = new HashMap<String, Object>();
        params.put("openId", openId);
        List<Ruser> userList = ruserService.selectRusers(params);
        if (userList.size() == 1) {
            // 已绑定
            Ruser ruser = userList.get(0);
            if (!(ruser.getIsBlacklist() != null && ruser.getIsBlacklist() == 1)) {
                // 如果在黑名单中
                this.request.setAttribute("isForbid", true);
                this.request.setAttribute("openId", "");
                return "html5/app_within/Recharge/wxrecharge/login";
            } else {
                this.request.setAttribute("isForbid", false);
            }
            this.request.setAttribute("ruser", ruser);
            cookies.put("userExist", "1");
            cookies.put("uid", String.valueOf(ruser.getId()));
            setCookie(response, cookies, 7 * 24 * 60 * 60);
            return "html5/app_within/Recharge/wxrecharge/extract";
        } else {
            // 未绑定
            return "html5/app_within/Recharge/wxrecharge/login";
        }
    }

    @RequestMapping("/login")
    public String toLogin() {
    	String param = this.request.getParameter("back");
    	if (param != null && !"".equals(param)){
    		this.request.setAttribute("back", param);
    	} else {
//    		this.request.setAttribute("back", "");
            return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?scope=snsapi_base&response_type=code&redirect_uri=http%3A%2F%2Fapi.wopaitv.com%2Frestwww%2Fpay%2Fwithdraw&state=STATE&appid=wx8a908b16f1e26ade&connect_redirect=1#wechat_redirect";
    	}
        return "html5/app_within/Recharge/wxrecharge/login";
    }

    @RequestMapping("/bind")
    @ResponseBody
    public RespBody bindOpenId() {
        Map<String, String> map = new HashMap<String, String>();
        logger.info("bindHandler: binding...");
        String uid = this.request.getHeader("uid");
        String openId = this.request.getHeader("openId");
        if (StringUtils.isBlank(openId)) {
            return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
        }
        Ruser ruser = ruserService.find(Long.parseLong(uid));
        logger.info("bindHandler: uid=" + uid + "& openId=" + openId);
        if (ruser != null && !(ruser.getIsBlacklist() != null && ruser.getIsBlacklist() == 1)) {
            this.request.setAttribute("isForbid", true);
            return respBodyWriter.toError(ResponseCode.CODE_616.toString(), ResponseCode.CODE_616.toCode());
        }

        if (StringUtils.isNotBlank(openId)) {
            Ruser user = ruserService.find(Long.parseLong(uid));
            Map<String,Object> params = new HashMap<String, Object>();
            params.put("openId", openId);
            List<Ruser> userList = ruserService.selectRusers(params);
            if ((user != null && StringUtils.isNotBlank(user.getOpenId())) || userList.size() > 0) {
                return respBodyWriter.toError(ResponseCode.CODE_619.toString(), ResponseCode.CODE_619.toCode());
            }
        }

        if (ruser != null) {
            Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(uid));
            if (anchor == null) {
                return respBodyWriter.toError(ResponseCode.CODE_622.toString(), ResponseCode.CODE_622.toCode());
            }
            Integer points = anchor.getPointCount();
            String rate = jedisService.get(BicycleConstants.DIVIDE_RATE);
            BigDecimal point = new BigDecimal(points);
            BigDecimal price = point.multiply(new BigDecimal(rate));
            price = price.setScale(2, RoundingMode.HALF_UP);

            ruser.setOpenId(openId);
            ruserService.update(ruser);
            logger.info("bindHandler: update ruser point=" + point + "& price=" + price);
            map.put("uid", uid);
            map.put("pic", ruser.getPic());
            map.put("name", ruser.getName());
            map.put("point", point.toString());
            map.put("price", price.toString());
            return respBodyWriter.toSuccess(map);
        } else {
            return respBodyWriter.toError(ResponseCode.CODE_305.toString(), ResponseCode.CODE_305.toCode());
        }

    }

    @RequestMapping("/extract")
    public String extract() {

        return "html5/app_within/Recharge/wxrecharge/extract";
    }

    @RequestMapping("/recordList")
    public String toRecordList(String channel) {
        if ("exchange".equals(channel)) {
            return "html5/app_within/Recharge/wxrecharge/exchangeList_record";
        }
        if ("wx_pub".equals(channel)) {
            return "html5/app_within/Recharge/wxrecharge/wihtList_record";
        }

        return "";
    }

    @RequestMapping("/toExchange")
    public String toExchange(String channel) {
        if ("exchange".equals(channel)) {
            return "html5/app_within/Recharge/wxrecharge/exchangeList";
        }
        if ("wx_pub".equals(channel)) {
            return "html5/app_within/Recharge/wxrecharge/wihtList";
        }

        return "";
    }

    @RequestMapping("/unbind")
    @ResponseBody
    public RespBody unbindOpenId(HttpServletResponse response) {
        String openId = this.request.getHeader("openId");
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("openId", openId);
        List<Ruser> userList = ruserService.selectRusers(params);
        if (userList.size() == 1) {
            Ruser ruser = userList.get(0);
            ruser.setOpenId("");
            ruserService.updateRuser(ruser);
            this.request.setAttribute("ruser", ruser);
            boolean isForbid = !(ruser.getIsBlacklist() != null && ruser.getIsBlacklist() == 1);
            this.request.setAttribute("isForbid", isForbid);
            Map<String,String> cookies = new HashMap<String,String>();
            cookies.put("userExist", "0");
            cookies.put("uid", "");
            setCookie(response, cookies, 7*24*60*60);
            return respBodyWriter.toSuccess("已经解除绑定");
        }

        return respBodyWriter.toError("解绑失败");
    }

    @RequestMapping("/getUser")
    @ResponseBody
    public RespBody getUser(HttpServletRequest request) {
        String openId = request.getHeader("openId");
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("openId", openId);
        List<Ruser> userList = ruserService.selectRusers(params);
        if (userList.size() == 1) {
            Ruser ruser = userList.get(0);

            Anchor anchor = anchorService.getAnchorByUserid(ruser.getId());

            if (anchor != null) {
                Integer points = anchor.getPointCount();
                String rate = jedisService.get(BicycleConstants.DIVIDE_RATE);
                BigDecimal point = new BigDecimal(points);
                BigDecimal price = point.multiply(new BigDecimal(rate));
                price = price.setScale(2, RoundingMode.HALF_UP);

                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", String.valueOf(ruser.getId()));
                map.put("pic", ruser.getPic());
                map.put("name", ruser.getName());
                map.put("point", point.toString());
                map.put("price", price.toString());
                return respBodyWriter.toSuccess(map);
            }
        }

        return respBodyWriter.toError("获取用户失败，此用户没有直播消息");
    }

    /**
     * @param channel wx_pub & exchange
     */
    @RequestMapping("/getTransferRecord")
    @ResponseBody
    public RespBody getTransferRecord(String channel) {
        String openId = request.getHeader("openId");
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("openId", openId);
        List<Ruser> userList = ruserService.selectRusers(params);

        if (userList.size() == 1) {
            Ruser user = userList.get(0);
            List<ExchangeRecode> list = pingPayService.getTransferRecord(user.getId(), channel);
            List<ExchangeRecodeVO> result = new ArrayList<ExchangeRecodeVO>();
            Integer pointCount = 0;
            Integer price = 0;
            Integer diamond = 0;
            for (ExchangeRecode recode : list) {
                if (recode != null && recode.getCreated() != null && recode.getCreated() > 0) {
                    pointCount += recode.getExchangePoint();
                    price += recode.getAmount();
                    diamond += recode.getDiamond();
                    ExchangeRecodeVO exchangeRecodeVO = new ExchangeRecodeVO();
                    try {
                        Long time = recode.getCreated();
                        String date = formatTime(time);
                        BeanUtils.copyProperties(exchangeRecodeVO, recode);
                        exchangeRecodeVO.setDate(date);
                        result.add(exchangeRecodeVO);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("price", price);
            map.put("pointCount", pointCount);
            map.put("diamond", diamond);
            map.put("list", result);
            return respBodyWriter.toSuccess(map);
        }

        return respBodyWriter.toError("获取用户失败，此用户没有直播消息");
    }

    /**
     * 提现
     * @param transferId 提现的商品ID exchange primaryKey
     * @return result
     */
    @RequestMapping("/transfer")
    @ResponseBody
    public RespBody transfer(Long transferId) {
        // 设置apiKey用于Authentication认证
        Pingpp.apiKey = PAYMENT_SK;
        // 判断用户是不是黑名单
        String uid = this.request.getHeader("uid");
        if (StringUtils.isBlank(uid)) {
            return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
        }
        return transferHandler(Long.parseLong(uid), transferId);
    }

    /**
     * app提现
     * @param transferId 提现的商品ID exchange primaryKey
     * @return result
     */
    @RequestMapping("/transferForApp")
    @ResponseBody
    public RespBody transferForApp(Long transferId) {
        // 设置apiKey用于Authentication认证
        Pingpp.apiKey = PAYMENT_SK;
        // 判断用户是不是黑名单
        String uid = this.request.getHeader("uid");
        if (StringUtils.isBlank(uid)) {
            return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
        }
        return transferHandler(Long.parseLong(uid), transferId);
    }

    /**
     * 金豆兑换金币
     */
    @RequestMapping("/exchange")
    @ResponseBody
    public RespBody exchange(Long exchangeId, String appVersion, String osVersion) {
        String uid = this.request.getHeader("uid");
        if (StringUtils.isBlank(uid)) {
            return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
        }
        String platform = "android";
        if (osVersion != null && osVersion.contains("ios")){
            platform = "ios";
        }
        return exchangeHandler(Long.parseLong(uid), exchangeId, appVersion, platform);
    }

    /**
     * app金豆兑换金币
     */
    @RequestMapping("/exchangeForApp")
    @ResponseBody
    public RespBody exchangeForApp(Long exchangeId, String appVersion, String osVersion) {
        String uid = this.request.getHeader("uid");
        if (StringUtils.isBlank(uid)) {
            return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
        }
        String platform = "android";
        if (osVersion != null && osVersion.contains("ios")){
            platform = "ios";
        }
        return exchangeHandler(Long.parseLong(uid), exchangeId, appVersion, platform);
    }

    /**
     * 跳转至兑换列表
     */
    @RequestMapping("/exchangeList")
    public String exchangeList(HttpServletRequest request) {
        String openId = request.getParameter("openId");
        Exchange exchange = new Exchange();
        exchange.setType(1);
        List<Exchange> list = exchangeService.selectAll(exchange);
        this.request.setAttribute("openId", openId);
        this.request.setAttribute("exchanges", list);
        return "html5/app_within/Recharge/wxrecharge/exchangeList";
    }
    /**
     * 跳转至提现列表
     */
    @RequestMapping("/wihtList")
    public String wihtList(HttpServletRequest request) {
        String openId = request.getParameter("openId");
        Exchange exchange = new Exchange();
        exchange.setType(2);
        List<Exchange> list = exchangeService.selectAll(exchange);
        this.request.setAttribute("openId", openId);
        this.request.setAttribute("exchanges", list);
        return "html5/app_within/Recharge/wxrecharge/wihtList";
    }

    /**
     * 苹果服务器验证
     * @param receipt 账单
     */
    @RequestMapping("/buyAppVerify")
    @ResponseBody
    public RespBody buyAppVerify(String receipt, String paramJson, String appVersion) {
    	logger.info("buyAppVerify,paramJson="+paramJson);
        logger.info("AppVerifyRecodeHandler param: receipt=" + receipt + " & paramJson=" + paramJson);
        try {
        	//记录原始值
            appVerifyRecodeService.saveVerifyRecode(0l, receipt, paramJson, getIpAddress(request), UUID.randomUUID().toString(), "", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
        // 线上环境参数为"Real"
        String url_verify = IOS_Verify.getUrl("Real");
        if (StringUtils.isBlank(url_verify)) {
            return respBodyWriter.toError(ResponseCode.CODE_450.toString(), ResponseCode.CODE_450.toCode());
        } else {
            String result = pingPayService.buyAppVerify(url_verify, receipt);
            if (StringUtils.isNotBlank(result)) {
                JSONObject job = JSONObject.fromObject(result);
                String status = job.getString("status");
                String clientIp = getIpAddress(request);
                String uid = this.request.getHeader("uid");
                Long userId = Long.parseLong(uid);

                if("0".equals(status)) {
                    String receiptStr = job.getString("receipt");
                    if (StringUtils.isNotBlank(receiptStr)) {
                        JSONObject receiptJSON = JSONObject.fromObject(receiptStr);
                        String inAppArr = receiptJSON.getString("in_app");
                        String requestDate = receiptJSON.getString("request_date_ms");
                        String receiptCreationDate = receiptJSON.getString("receipt_creation_date_ms");
                        if (StringUtils.isNotBlank(receiptCreationDate) && StringUtils.isNotBlank(requestDate)) {
                            long requestDateMS = Long.parseLong(requestDate);
                            long receiptCreationDateMs = Long.parseLong(receiptCreationDate);
                            if (System.currentTimeMillis() - receiptCreationDateMs > 1000*60*60*24*5) {  //五天之外的订单信息不处理
                                // 请求时间 - 票据创建时间 超过20秒
                                logger.error("app store支付：订单验证时间超时！params=" + paramJson);
                                return respBodyWriter.toError("订单验证时间超时");
                            }
                        } else {
                            // 参数不全
                            logger.error("app store支付：验证票据参数不全！params=" + paramJson);
                            return respBodyWriter.toError("验证票据参数不全");
                        }
                        JSONArray inApp = JSONArray.fromObject(inAppArr);
                        if (inApp != null && inApp.size() > 0) {
                            JSONObject jsonObj = JSONObject.fromObject(inApp.get(0));
                            String productId = jsonObj.getString("product_id");
                            String transactionId = jsonObj.getString("transaction_id");
                            if (StringUtils.isNotBlank(productId) && StringUtils.isNotBlank(transactionId)) {
                                logger.info("苹果支付验证通过！");
                                // add log recode
                                if (appVerifyRecodeService.saveVerifyRecode(userId, receipt, paramJson, clientIp, transactionId, status, result)) {
                                    logger.info("AppVerifyRecode is save success!");
                                    String produceId = productId.substring(productId.indexOf("_") + 1, productId.length());
                                    String data = pingPayService.appStorePay("app_store", clientIp, Long.valueOf(produceId),
                                    		userId, transactionId, OrderStatus.PAID.getStatus(), appVersion, "ios");
                                    if (StringUtils.isNotBlank(data)) {
                                    	return respBodyWriter.toSuccess(data);
                                    }
                                }
                            }
                        }
                    }
                } else if ("21007".equals(status)) {
                    // 验证沙箱环境
                    url_verify = IOS_Verify.getUrl("Sandbox");
                     String sandBoxResult = pingPayService.buyAppVerify(url_verify, receipt);
                    if (StringUtils.isNotBlank(sandBoxResult)) {
                        JSONObject sendBoxJson = JSONObject.fromObject(sandBoxResult);
                        String sendBoxStatus = sendBoxJson.getString("status");
                        if ("0".equals(sendBoxStatus)) {
                            String sReceipt = JSONObject.fromObject(sandBoxResult).getString("receipt");
                            JSONObject receiptJSON = JSONObject.fromObject(sReceipt);
                            String inAppArr = receiptJSON.getString("in_app");
                            JSONArray inApp = JSONArray.fromObject(inAppArr);
                            String transactionId = "";
                            if (inApp != null && inApp.size() > 0) {
                                JSONObject jsonObj = JSONObject.fromObject(inApp.get(0));
                                transactionId = StringUtils.isNotBlank(jsonObj.getString("transaction_id")) ? jsonObj.getString("transaction_id") : "";
                                String productId = jsonObj.getString("product_id");
                                String produceId = productId.substring(productId.indexOf("_") + 1, productId.length());
                                if (userId.intValue() == 3391) { //苹果审核人员使用的账号，进入充值记录表，审核通过后会删除
                                	String data = pingPayService.appStorePay("app_store", clientIp, Long.valueOf(produceId),
                                			userId, transactionId, OrderStatus.PAID.getStatus(), appVersion, "ios");
                                }
                                Diamond diamond = diamondService.selectByPrimaryKey( Long.valueOf(produceId));
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("orderNo", UUID.randomUUID().toString().replaceAll("-", ""));
                                map.put("transactionNo", transactionId);
                                map.put("diamondCount", (diamond.getGiveCount() + diamond.getDiamondCount()));
                                map.put("amount", diamond.getPrice());
                                map.put("status", OrderStatus.PAID.getStatus());
                                return respBodyWriter.toSuccess(JSONObject.fromObject(map).toString());
                            }
                        }
                    }
                }
                logger.error("app store支付：订单验证失败！params=" + paramJson);
                return respBodyWriter.toError("保存验证失败");

            }
        }
        return respBodyWriter.toError(ResponseCode.CODE_603.toString(), ResponseCode.CODE_603.toCode());
    }

    /**
     * 活动赠送金币
     * @param type 1:分享直播 2:开启直播
     * @return result
     */
    @RequestMapping("giveDiamondByActivity")
    @ResponseBody
    public RespBody giveDiamondByActivity(Integer type, String appVersion, String osVersion) {
    	String giveSwitch = jedisService.get(BicycleConstants.GIVE_DIAMOND_OF_SHARE); //分享送金币增加开关
    	if (giveSwitch != null && "1".equals(giveSwitch)) {
    		String uid = this.request.getHeader("uid");
            logger.info("giveDiamondByActivity: uid=" + uid);
    	        if (StringUtils.isBlank(uid) || type == null) {
    	            return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
    	        } else {
    	            Ruser ruser = ruserService.find(Long.parseLong(uid));
    	            if (ruser == null) {
    	                return respBodyWriter.toError(ResponseCode.CODE_305.toString(), ResponseCode.CODE_305.toCode());
    	            }
    	            if (Constants.GiveDiamondType.SHARE.getType() == type) {
    	            	int dailyShareCount = jedisService.incr(BicycleConstants.SHARE_LIVE_TIME + uid); //使用redis自身方法自增，避免脏读
    	            	if (dailyShareCount == 1) { //当日首次赠送的时候设置过期时间
    	            		jedisService.expire(BicycleConstants.SHARE_LIVE_TIME + uid, Integer.parseInt(String.valueOf(getExpireTime())));
    	            	}

    	                Map<String, String> constantsMap = jedisService.getMapByKey(BicycleConstants.SHARE_GIVE_GOLD);
    	                String diamondString = constantsMap.get("diamond");
    	                if (StringUtils.isNotBlank(diamondString) && StringUtils.isNumeric(diamondString)
    	                        && dailyShareCount <= Integer.parseInt(constantsMap.get("count"))) {
    	                    // 小于限定分享赠送次数时
                            String platform = "android";
                            if (osVersion != null && osVersion.contains("ios")){
                                platform = "ios";
                            }
    	                    if (pingPayService.giveDiamondByActivity(ruser, Integer.parseInt(diamondString), type, System.currentTimeMillis() / 1000l, appVersion, platform)) {
    	                        return respBodyWriter.toSuccess(constantsMap.get("tips"));
    	                    }
    	                }
    	            	
//    	                String dailyShareTime = jedisService.get(BicycleConstants.SHARE_LIVE_TIME + uid);
//    	                int dailyShareCount = 0;
//    	                if (StringUtils.isNotBlank(dailyShareTime) && StringUtils.isNumeric(dailyShareTime)) {
//    	                    // 有值 今天已经分享过
//    	                    dailyShareCount = Integer.parseInt(dailyShareTime);
//    	                }
//
//    	                Map<String, String> constantsMap = jedisService.getMapByKey(BicycleConstants.SHARE_GIVE_GOLD);
//    	                String diamondString = constantsMap.get("diamond");
//    	                if (StringUtils.isNotBlank(diamondString) && StringUtils.isNumeric(diamondString)
//    	                        && dailyShareCount < Integer.parseInt(constantsMap.get("count"))) {
//    	                    // 小于限定分享赠送次数时
//    	                    if (pingPayService.giveDiamondByActivity(ruser, Integer.parseInt(diamondString), type, System.currentTimeMillis() / 1000l)) {
//    	                        jedisService.delete(BicycleConstants.SHARE_LIVE_TIME + uid);
//    	                        jedisService.set(BicycleConstants.SHARE_LIVE_TIME + uid, String.valueOf(dailyShareCount + 1));
//    	                        jedisService.expire(BicycleConstants.SHARE_LIVE_TIME + uid, Integer.parseInt(String.valueOf(getExpireTime())));
//    	                        return respBodyWriter.toSuccess(constantsMap.get("tips"));
//    	                    }
//    	                }
    	            }
    	        }
    	}
       
    	return respBodyWriter.toSuccess(""); //如果没有赠送，返回提示信息为空，前端不显示
    }

    private RespBody exchangeHandler(Long uid, Long exchangeId, String appVersion, String platform) {
        logger.info("exchangeHandler : 用户id=" + uid + " 申请兑换 exchangeId=" + exchangeId
                + " 时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        boolean isSuccess = pingPayService.exchange(uid, exchangeId, appVersion, platform);
        if (isSuccess) {
            Anchor anchor = anchorService.getAnchorByUserid(uid);
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("point", anchor.getPointCount());
            return respBodyWriter.toSuccess(map);
        }
        return respBodyWriter.toError(ResponseCode.CODE_614.toString(), ResponseCode.CODE_614.toCode());
    }

    private RespBody transferHandler(Long uid, Long transferId) {
        logger.info("transferHandler : 用户id=" + uid + " 申请提现 transferId=" + transferId
                + " 时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Ruser ruser = ruserService.find(uid);
        boolean isForbid = (ruser.getIsBlacklist() != null && ruser.getIsBlacklist() == 1);
        if (!isForbid) {
            this.request.setAttribute("isForbid", true);
            return respBodyWriter.toError(ResponseCode.CODE_616.toString(), ResponseCode.CODE_616.toCode());
        }
        // 判断transferId是否已下线
        Exchange exchange = exchangeService.selectByPrimaryKey(transferId);
        if (exchange.getState() == 0) {
            return respBodyWriter.toError(ResponseCode.CODE_618.toString(), ResponseCode.CODE_618.toCode());
        }
        // 判断用户每日提现金额
        if (checkDailyTransfer(ruser.getId(),transferId)) {
            return respBodyWriter.toError(ResponseCode.CODE_617.toString(), ResponseCode.CODE_617.toCode());
        }
        if (checkDailyTransferTime(ruser.getOpenId())) {
            return respBodyWriter.toError(ResponseCode.CODE_621.toString(), ResponseCode.CODE_621.toCode());
        }
        Transfer transfer = pingPayService.createTransfer(transferId, APP_ID, uid);
        if (transfer == null) {
            logger.error("transferHandler =========> " + ResponseCode.CODE_614.toString());
            return respBodyWriter.toError(ResponseCode.CODE_614.toString(), ResponseCode.CODE_614.toCode());
        } else {
            if ("failed".equals(transfer.getStatus())) {
                // 失败
                logger.info("transferHandler =========> transfer failed:" + JSONObject.fromObject(transfer).toString());
                anchorService.rollbackAnchorPoint(exchange.getPointCount(), uid);
                return respBodyWriter.toError(ResponseCode.CODE_620.toString(), ResponseCode.CODE_620.toCode());
            }
            Anchor anchor = anchorService.getAnchorByUserid(uid);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("point", anchor.getPointCount());
            map.put("transfer", transfer);

            return respBodyWriter.toSuccess(map);
        }
    }

    /**
     * 判断今日提取金额是否达到上限
     * @param userId 用户id
     * @return true:已达上限
     */
    private boolean checkDailyTransfer(long userId, Long transferId) {
        Integer pointCount = pingPayService.getDailyTransferCount(userId, transferId);
        return pointCount > MaxTransferAmount;
    }

    private boolean checkDailyTransferTime(String openId) {
        return !StringUtils.isNotBlank(openId) || pingPayService.getDailyTransferTime(openId) >= 20;

    }

    private static boolean checkEmptyValue(String... params) {
        for (String value : params) {
            if (StringUtils.isBlank(value)) {
                return false;
            }
        }
        return true;
    }


    public static String getIpAddress(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        try {
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getHeader("Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getRemoteAddr();
            }
            if (StringUtils.isNotBlank(ip)) {
                String[] ss = ip.split(",");
                if (ss.length > 1) {
                    ip = ss[0];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }

    private String formatTime(Long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time * 1000l);
        Date date = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    private long getExpireTime() {
        Calendar b = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return (c.getTimeInMillis() / 1000) - (b.getTimeInMillis() / 1000);
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
    
    public static void main(String args[]) {
    	 String Appkey="Lf6AtMEB1QlE8BYS&";
    	 String method="GET";
		 String url_path="/mpay/pay_m";

    	 

		 String amt="1";
		 String appid="1101255891";
		 String format="json";
		 String openid="F11669C63D76BAB0BC2F6CC869B19E53";
		 String openkey="3968DD5F3F14427EF103A05E00AB59B4";
		 String pay_token="6554E0A9225B05A1CE4C4AF215A8C369";
		 String pf="desktop_m_qq-10000144-android-2002-";
		 String pfkey="5971dce2d3035669e49a1496d590f1ba";
		 String ts="1396522813";
		 String zoneid="1";
		 
		 String url="/mpay/pay_m"; 
		 
		 String urlNew="";
		 
		 try {
			 urlNew=URLEncoder.encode(url,"UTF-8");
			 urlNew=urlNew.replace("*", "%2A");
			 urlNew=urlNew.replace("+", "%2B");
			 
			System.out.println(urlNew);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 String param="amt=1&appid=1101255891&format=json&openid=F11669C63D76BAB0BC2F6CC869B19E53&openkey=3968DD5F3F14427EF103A05E00AB59B4&pay_token=6554E0A9225B05A1CE4C4AF215A8C369&pf=desktop_m_qq-10000144-android-2002-&pfkey=5971dce2d3035669e49a1496d590f1ba&ts=1396522813&zoneid=1";
		 
		 String paramNew="";
//		 for (int i = 0; i < url.length(); i++) {
//			 boolean flg=('a' <= url.charAt(i) &&  url.charAt(i) <= 'z') || ('A' <=  url.charAt(i) &&  url.charAt(i) <= 'Z') || ('0' <=  url.charAt(i) &&  url.charAt(i) <= '9')||('*' ==  url.charAt(i))||('*' ==  url.charAt(i))||('*' ==  url.charAt(i));
//			 
//			 if(flg) {
//				 
//			 }else {
//				 
//			 }
//			 sb.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
//		 }

		 try {
			 paramNew=URLEncoder.encode(param,"UTF-8");
			 paramNew=paramNew.replace("*", "%2A");
			 paramNew=paramNew.replace("+", "%2B");
			 
			System.out.println(paramNew);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 String appkey="Lf6AtMEB1QlE8BYS&";
		 
//		 String appkeyNew="";
//		 
//		 try {
//			 appkeyNew=URLEncoder.encode(appkey,"UTF-8");
//			 appkeyNew=appkeyNew.replace("*", "%2A");
//			 appkeyNew=appkeyNew.replace("+", "%2B");
//			 
//			System.out.println(appkeyNew);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		 
		 String sig="GET"+"&"+urlNew+"&"+paramNew;
		 
		 System.out.println(sig);
		 
		 try {
			byte[] byBuffer=HmacSHA1Encrypt(sig, appkey);
			
//			MD5.encode(rawHmac); 
			
			String sigNew = new String(byBuffer,"UTF-8");
			
			String base64Text = Base64.encodeToString(byBuffer, Base64.DEFAULT).trim();

			
			System.out.println(sigNew);
			
			System.out.println(base64Text);
			
			String base64TextNew="";
			
			base64TextNew=URLEncoder.encode(base64Text,"UTF-8");
			System.out.println(base64TextNew);
			base64TextNew=base64TextNew.replace("*", "%2A");
			System.out.println(base64TextNew);
			base64TextNew=base64TextNew.replace("+", "%2B");
			
			System.out.println(base64TextNew);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 String json1="{\"ret\":0,\"balance\":200,\"gen_balance\":0,\"first_save\":1,\"save_amt\":200}";
		 try {
			Map m = (Map)JsonUtil.convertJson2Object(json1, Map.class);
			System.out.println(m);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
    }
    
    /**  
         * 使用 HMAC-SHA1 签名方法对对encryptText进行签名  
         * @param encryptText 被签名的字符串  
         * @param encryptKey  密钥  
         * @return  
         * @throws Exception  
         */    
        public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception { 
        	
        	String MAC_NAME = "HmacSHA1";    
        	

        	String ENCODING = "UTF-8";  

            byte[] data=encryptKey.getBytes(ENCODING);  
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称  
            SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);   
            //生成一个指定 Mac 算法 的 Mac 对象  
            Mac mac = Mac.getInstance(MAC_NAME);   
            //用给定密钥初始化 Mac 对象  
            mac.init(secretKey);    
              
            byte[] text = encryptText.getBytes(ENCODING);    
            //完成 Mac 操作   
            
            
//            SecretKey secretKey = new SecretKeySpec(secret.getBytes("US-ASCII"), "HmacSHA1");
//            Mac mac = Mac.getInstance("HmacSHA1");
//            mac.init(secretKey);
//
//            byte[] text = builder.toString().getBytes("US-ASCII");
//            byte[] finalText = mac.doFinal(text);
//            String base64Text = Base64.encodeToString(finalText, Base64.DEFAULT);
//            return encode(base64Text.trim());
            return mac.doFinal(text);    
        }    
        
        
        public String appendParam(String returns, String paramId, String paramValue) {
    		if (returns != "") {
    			if (paramValue != "") {

    				returns += "&" + paramId + "=" + paramValue;
    			}

    		} else {

    			if (paramValue != "") {
    				returns = paramId + "=" + paramValue;
    			}
    		}

    		return returns;
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

        
        @RequestMapping(value = {"/pay99Bill"}, method = {RequestMethod.GET,RequestMethod.POST, RequestMethod.PUT})
        @ResponseBody
        public void pay99Bill(@RequestParam(value = "productId", required = true)  String productId,//商品代码，可以为空。(快钱可为空，live中不可为空，要根据这个id拿价格)
        						  @RequestParam(value = "bankId", required = true)  String bankId,//银行代码，如果payType为00，该值可以为空；如果payType为10，该值必须填写，具体请参考银行列表。
        						  @RequestParam(value = "oAmt", required = false)  String oAmt,//自定义金额
        						  @RequestParam(value = "uid", required = false)  String uid,//uid
        						  @RequestParam(value = "at", required = false)  String at,//access_token
        						  
        						  HttpServletRequest request,HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException {

        	String userId = this.request.getHeader("uid");
        	if(uid==null||uid.equals("")) {
        		userId=uid;
//            	uid="118836";		
        	}

        	
        	//客户端ip	
    		String	appip =getIpAddress(request);
    		
    		String hostUrl=loadConfigUrl("html5", "interfaceurl")+"/restwww";
    		
//    		String hostUrl="http://ceshi.wopaitv.com/restwww";
//    		String hostUrl="http://api.wopaitv.com/restwww";
//    		String hostUrl="http://1.119.0.59:8080/restwww";
        	
        	
        	//人民币网关账号，该账号为11位人民币网关商户编号+01,该参数必填。
//        	String merchantAcctId = "1001213884201";//demo文件原值
        	String merchantAcctId = "1008311230901";
        	//编码方式，1代表 UTF-8; 2 代表 GBK; 3代表 GB2312 默认为1,该参数必填。
        	String inputCharset = "1";
        	//接收支付结果的页面地址，该参数一般置为空即可。
        	String pageUrl = "";
        	//服务器接收支付结果的后台地址，该参数务必填写，不能为空。
//        	String bgUrl = "http://219.233.173.50:8801/RMBPORT/receive.jsp";
        	String bgUrl = hostUrl+"/pay/callback99Bill?orderId=";
//        	String bgUrl = "http://api.restwww.com/pay/callback99Bill?orderId=";
        	//网关版本，固定值：v2.0,该参数必填。
        	String version =  "v2.0";
        	//语言种类，1代表中文显示，2代表英文显示。默认为1,该参数必填。
        	String language =  "1";
        	//签名类型,该值为4，代表PKI加密方式,该参数必填。
        	String signType =  "4";
        	//支付人姓名,可以为空。
        	String payerName= ""; 
        	//支付人联系类型，1 代表电子邮件方式；2 代表手机联系方式。可以为空。
//        	String payerContactType =  "1";
        	String payerContactType =  "";
        	//支付人联系方式，与payerContactType设置对应，payerContactType为1，则填写邮箱地址；payerContactType为2，则填写手机号码。可以为空。
//        	String payerContact =  "2532987@qq.com";
        	String payerContact =  "";
        	//商户订单号，以下采用时间来定义订单号，商户可以根据自己订单号的定义规则来定义该值，不能为空。
        	String orderId = "";
        	String data = pingPayService.yybPay("99bill", appip, Long.valueOf(productId),Long.valueOf(uid), "", OrderStatus.CREATE.getStatus(), "", "pc");
        	 Map dataMap = (Map)JsonUtil.convertJson2Object(data, Map.class);
			if(dataMap!=null) {
				if(dataMap.get("orderId")!=null) {
					orderId=(String)dataMap.get("orderId");
					bgUrl+=orderId;
//					bgUrl="http";
				}
			}
//        	String orderId = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
				
				
			Diamond d=diamondService.selectByPrimaryKey(Long.valueOf(productId));
        	//订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试。该参数必填。
//        	String orderAmount = "1";
			String orderAmount ="0";
			if(productId.equals("0")) {//自定义金额
				orderAmount=String.valueOf(Integer.valueOf(oAmt)*100);
			}else {//条目中的金额
				orderAmount =String.valueOf(d.getPrice().multiply(BigDecimal.valueOf(100)).intValue());
			}
        	
        	//订单提交时间，格式：yyyyMMddHHmmss，如：20071117020101，不能为空。
        	String orderTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        	
        	
        	//商品名称，可以为空。
        	String productName= d.getName(); 
        	productName=URLEncoder.encode(productName,"UTF-8");
        	productName="";
//        	productName="111";
        	//商品数量，可以为空。
        	String productNum = "";
        	//商品代码，可以为空。
//        	String productId = "55558888";
        	//商品描述，可以为空。
        	String productDesc = "";
        	//扩展字段1，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
        	String ext1 = "";
        	//扩展自段2，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
        	String ext2 = "";
        	//支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10，必填。
        	String payType = "10";
        	//银行代码，如果payType为00，该值可以为空；如果payType为10，该值必须填写，具体请参考银行列表。
//        	String bankId = "ICBC";
        	//同一订单禁止重复提交标志，实物购物车填1，虚拟产品用0。1代表只能提交一次，0代表在支付不成功情况下可以再提交。可为空。
        	String redoFlag = "";
        	//快钱合作伙伴的帐户号，即商户编号，可为空。
        	String pid = "";
        	// signMsg 签名字符串 不可空，生成加密签名串
        	String signMsgVal = "";
        	signMsgVal = appendParam(signMsgVal, "inputCharset", inputCharset);
        	signMsgVal = appendParam(signMsgVal, "pageUrl", pageUrl);
        	signMsgVal = appendParam(signMsgVal, "bgUrl", bgUrl);
        	signMsgVal = appendParam(signMsgVal, "version", version);
        	signMsgVal = appendParam(signMsgVal, "language", language);
        	signMsgVal = appendParam(signMsgVal, "signType", signType);
        	signMsgVal = appendParam(signMsgVal, "merchantAcctId",merchantAcctId);
        	signMsgVal = appendParam(signMsgVal, "payerName", payerName);
        	signMsgVal = appendParam(signMsgVal, "payerContactType",payerContactType);
        	signMsgVal = appendParam(signMsgVal, "payerContact", payerContact);
        	signMsgVal = appendParam(signMsgVal, "orderId", orderId);
        	signMsgVal = appendParam(signMsgVal, "orderAmount", orderAmount);
        	signMsgVal = appendParam(signMsgVal, "orderTime", orderTime);
        	signMsgVal = appendParam(signMsgVal, "productName", productName);
        	signMsgVal = appendParam(signMsgVal, "productNum", productNum);
        	signMsgVal = appendParam(signMsgVal, "productId", productId);
        	signMsgVal = appendParam(signMsgVal, "productDesc", productDesc);
        	signMsgVal = appendParam(signMsgVal, "ext1", ext1);
        	signMsgVal = appendParam(signMsgVal, "ext2", ext2);
        	signMsgVal = appendParam(signMsgVal, "payType", payType);
        	signMsgVal = appendParam(signMsgVal, "bankId", bankId);
        	signMsgVal = appendParam(signMsgVal, "redoFlag", redoFlag);
        	signMsgVal = appendParam(signMsgVal, "pid", pid);
        	Pkipair pki = new Pkipair();
        	 System.out.println(signMsgVal);
        	String signMsg = pki.signMsg(signMsgVal);
//        	signMsg=URLEncoder.encode(signMsg,"UTF-8");
        	
        	
        	
        	
        	
        	HttpClient client = new HttpClient();
//			String url="https://sandbox.99bill.com/gateway/recvMerchantInfoAction.htm";
			String url="https://www.99bill.com/gateway/recvMerchantInfoAction.htm";
//			url=URLDecoder.decode(url,"UTF-8");
//			url=URLEncoder.encode(url,"UTF-8");
			
//			bgUrl=URLEncoder.encode(bgUrl,"UTF-8");
			
		    PostMethod postMethod=new PostMethod(url);
		    postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(3, false));
		    
//		    for (Map.Entry<String, String> entry : paramMap.entrySet()) {  
//		    	logger.info("post param key:{},value:{}",entry.getKey(),entry.getValue());
//		    	postMethod.addParameter(entry.getKey(),entry.getValue());   
//		    }  
		    
		    postMethod.addParameter("inputCharset", inputCharset);
		    postMethod.addParameter("pageUrl",pageUrl );
		    postMethod.addParameter("bgUrl",bgUrl );
		    postMethod.addParameter("version",version );
		    postMethod.addParameter("language",language );
		    postMethod.addParameter("signType",signType );
		    postMethod.addParameter("signMsg",signMsg );
		    postMethod.addParameter("merchantAcctId",merchantAcctId );
		    postMethod.addParameter("payerName",payerName );
		    postMethod.addParameter("payerContactType",payerContactType );
		    postMethod.addParameter("payerContact", payerContact);
		    postMethod.addParameter("orderId", orderId);
		    postMethod.addParameter("orderAmount",orderAmount );
		    postMethod.addParameter("orderTime",orderTime );
		    postMethod.addParameter("productName",productName );
		    postMethod.addParameter("productNum",productNum );
		    postMethod.addParameter("productId",productId );
		    postMethod.addParameter("productDesc",productDesc );
		    postMethod.addParameter("ext1",ext1 );
		    postMethod.addParameter("ext2",ext2 );
		    postMethod.addParameter("payType",payType );
		    postMethod.addParameter("bankId",bankId );
		    postMethod.addParameter("redoFlag",redoFlag );
		    postMethod.addParameter("pid",pid );
		    
			String respContent = "";
			try {
				// Execute the method.
				int statusCode = client.executeMethod(postMethod);
				logger.info(postMethod.getResponseBodyAsString());
				if (statusCode == HttpStatus.SC_OK) {
					respContent = new String(postMethod.getResponseBody(),"utf-8");
					response.getWriter().print(respContent);
//					if(respContent.equals("{\"c\":0}")) {
//						
//					}else {
//						if(respContent.equals("{\"c\":-3106}")) {
//							
//						}
//						logger.error("有米渠道推广回调错误 " + respContent);
//					}
				} else {
					logger.error("Response Code: " + statusCode);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				postMethod.releaseConnection();
			}
    		





        }
        
        
        @RequestMapping("/callback99Bill")
        @ResponseBody
        public void callback99Bill(@RequestParam(value = "merchantAcctId", required = false)  String merchantAcctId,//人民币网关账号，该账号为11位人民币网关商户编号+01,该值与提交时相同。
        							@RequestParam(value = "version", required = false)  String version,//网关版本，固定值：v2.0,该值与提交时相同。
        							@RequestParam(value = "language", required = false)  String language,//语言种类，1代表中文显示，2代表英文显示。默认为1,该值与提交时相同。
        							@RequestParam(value = "signType", required = false)  String signType,//签名类型,该值为4，代表PKI加密方式,该值与提交时相同。
        							@RequestParam(value = "payType", required = false)  String payType,//支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10,该值与提交时相同。
        							@RequestParam(value = "bankId", required = false)  String bankId,//银行代码，如果payType为00，该值为空；如果payType为10,该值与提交时相同。
        							@RequestParam(value = "orderId", required = false)  String orderId,//商户订单号，该值与提交时相同。
        							@RequestParam(value = "orderTime", required = false)  String orderTime,//订单提交时间，格式：yyyyMMddHHmmss，如：20071117020101,该值与提交时相同。
        							@RequestParam(value = "orderAmount", required = false)  String orderAmount,//订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试,该值与支付时相同。
        							@RequestParam(value = "dealId", required = false)  String dealId,// 快钱交易号，商户每一笔交易都会在快钱生成一个交易号。
        							@RequestParam(value = "bankDealId", required = false)  String bankDealId,//银行交易号 ，快钱交易在银行支付时对应的交易号，如果不是通过银行卡支付，则为空
        							@RequestParam(value = "dealTime", required = false)  String dealTime,//快钱交易时间，快钱对交易进行处理的时间,格式：yyyyMMddHHmmss，如：20071117020101
        							@RequestParam(value = "payAmount", required = false)  String payAmount,//商户实际支付金额 以分为单位。比方10元，提交时金额应为1000。该金额代表商户快钱账户最终收到的金额。
        							@RequestParam(value = "fee", required = false)  String fee,//费用，快钱收取商户的手续费，单位为分。
        							@RequestParam(value = "ext1", required = false)  String ext1,///扩展字段1，该值与提交时相同。
        							@RequestParam(value = "ext2", required = false)  String ext2,//扩展字段2，该值与提交时相同。
        							@RequestParam(value = "payResult", required = false)  String payResult,//处理结果， 10支付成功，11 支付失败，00订单申请成功，01 订单申请失败
        							@RequestParam(value = "errCode", required = false)  String errCode,////错误代码 ，请参照《人民币网关接口文档》最后部分的详细解释。
        							@RequestParam(value = "signMsg", required = false)  String signMsg,//签名字符串 
        							HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        	
        	String hostUrl=loadConfigUrl("html5", "interfaceurl")+"/restwww";
//        	String hostUrl="http://ceshi.wopaitv.com/restwww";
//        	String hostUrl="http://ceshi.wopaitv.com/restwww";
//        	String hostUrl="http://api.wopaitv.com/restwww";
//        	String hostUrl="http://1.119.0.59:8080/restwww";
        	
        	String description="";
        	
        	Map map=new HashMap();
        	
        	description=bankDealId+"_"+dealTime+"_"+fee+""+payResult+"_"+errCode;
        	
        	String merchantSignMsgVal = "";
        	merchantSignMsgVal = appendParam(merchantSignMsgVal,"merchantAcctId", merchantAcctId);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "version",version);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "language",language);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "signType",signType);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "payType",payType);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankId",bankId);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderId",orderId);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderTime",orderTime);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderAmount",orderAmount);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealId",dealId);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankDealId",bankDealId);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealTime",dealTime);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "payAmount",payAmount);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "fee", fee);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext1", ext1);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext2", ext2);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "payResult",payResult);
        	merchantSignMsgVal = appendParam(merchantSignMsgVal, "errCode",errCode);
        	
        	Pkipair pki = new Pkipair();
        	boolean flag = pki.enCodeByCer(merchantSignMsgVal, signMsg);
        	int rtnOK =0;
          	String rtnUrl="";
          	
          	if(flag){
          		switch(Integer.parseInt(payResult))
          		{
          			case 10:
          					/*
          					此处商户可以做业务逻辑处理
          					*/
          					rtnOK=1;
          					
          					
//          					String data = pingPayService.yybPay("yyb", "", Long.valueOf(120),Long.valueOf(283), "", OrderStatus.CREATE.getStatus(), "", "pc");
          		            OrderPay op=pingPayService.getOrderPayById(Long.valueOf(orderId));
          		            
	          	            if(op!=null&&op.getStatus()!=null&&op.getStatus().intValue()==OrderStatus.CREATE.getStatus()) {
	          	            	int amount=op.getAmount()!=null?op.getAmount().intValue():0;
	          	            	int recieved=op.getReceived()!=null?op.getReceived().intValue():0;
	          	            	int extra=op.getExtra()!=null?op.getExtra().intValue():0;
//	          	            	pingPayService.updateOrderPayStatusByOrderNo( op.getOrderNo(),  String.valueOf(OrderStatus.PAID.getStatus()));
	          	            	boolean flg=pingPayService.updateOrderPayStatusByOrderId(orderId, String.valueOf(OrderStatus.PAID.getStatus()), dealId, description,dealTime);
	          	            	if(flg) {
	          	            		ruserService.updateDiamond(op.getUserId(), Integer.valueOf(recieved+extra));
	          	            	}
	          	            	
	          	            }
          		            
          					//以下是我们快钱设置的show页面，商户需要自己定义该页面。
          					rtnUrl=hostUrl+"/pay/redirectFrom99Bill?payStatus=1";
          					break;
          			default:
          					rtnOK=0;
          					//以下是我们快钱设置的show页面，商户需要自己定义该页面。
          					rtnUrl=hostUrl+"/pay/redirectFrom99Bill?payStatus=2";
          					break;
          		}
          	}
          	else
          	{
          		rtnOK=0;
          		//以下是我们快钱设置的show页面，商户需要自己定义该页面。
          		rtnUrl=hostUrl+"/pay/redirectFrom99Bill?payStatus=2";
          	}	
        
          	try {

          		PrintWriter out = response.getWriter();

          		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");

          		out.println("<HTML>");

          		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");

          		out.println("  <BODY>");

          		out.print("<result>"+rtnOK+"</result><redirecturl>" +rtnUrl+"</redirecturl>");

          		out.println("  </BODY>");

          		out.println("</HTML>");

          		out.flush();

          		out.close();

          		} catch (IOException e) {

          		// TODO Auto-generated catch block

          		e.printStackTrace();
          		}
          	
//          	response.getWriter().print("<result>"+rtnOK+"</result><redirecturl>" +rtnUrl+"</redirecturl>");
        
//          	map.put("rtnOK", rtnOK);
//			map.put("rtnUrl", rtnUrl);
//        	
//            return "html5/app_within/pc_recharge/redirect";
        }
        
        @RequestMapping("/redirectFrom99Bill")
        public ModelAndView redirectFrom99Bill(@RequestParam(value = "payStatus", required = true)  String payStatus,
        								@RequestParam(value = "orderId", required = false)  String orderId,
        							HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        	ModelAndView mav = new ModelAndView();
        	
        	if(orderId!=null&&!orderId.equals("")) {
        		OrderPay op=pingPayService.getOrderPayById(Long.valueOf(orderId));
        		mav.addObject("orderNo", op.getOrderNo());
        		
        		mav.addObject("amount", op.getAmount());
        		
        	}
//            request.setCharacterEncoding("UTF8");
//        	String data = pingPayService.yybPay("yyb", "", Long.valueOf(120),Long.valueOf(283), "", OrderStatus.CREATE.getStatus(), "", "pc");
//            OrderPay op=pingPayService.getOrderPayById(Long.valueOf(orderId));
            Map map=new HashMap();
//            if(op!=null&&op.getStatus()!=null&&op.getStatus().intValue()==OrderStatus.CREATE.getStatus()) {
//            	int amount=op.getAmount()!=null?op.getAmount().intValue():0;
//            	int extra=op.getExtra()!=null?op.getExtra().intValue():0;
//            	ruserService.updateDiamond(op.getUserId(), Integer.valueOf(amount+extra));
//            	map.put("payStatus", 1);
//            	return "html5/app_within/pc_recharge/result";
//            }
            String returnUrl=loadConfigUrl("html5", "interfaceurl")+"/restwww/page/user/pcRechargeIndex";
            
            mav.addObject("returnUrl", returnUrl);
     		mav.addObject("payStatus", payStatus);
     		mav.setViewName("html5/app_within/pc_recharge/result");
     		return mav;
            	
        }
        
        
        @RequestMapping("/create99billOrder")
        @ResponseBody
        public RespBody create99billOrder(String productId, String uid,String oAmt) throws JsonParseException, JsonMappingException, IOException {
            
        	//客户端ip	
    	String	appip =getIpAddress(request);
        String data = pingPayService.bill99Pay("99bill", appip, Long.valueOf(productId),Long.valueOf(uid), "", OrderStatus.CREATE.getStatus(), "", "pc",Integer.valueOf(oAmt));
        String orderId="";
       	 Map dataMap = (Map)JsonUtil.convertJson2Object(data, Map.class);
			if(dataMap!=null) {
				if(dataMap.get("orderId")!=null) {
					orderId=(String)dataMap.get("orderId");
//					bgUrl+=orderId;
//					bgUrl="http";
				}
			}
//       	String orderId = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
				
				
			Map map=new HashMap();
			
       	//订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试。该参数必填。
//       	String orderAmount = "1";
			String orderAmount ="0";
			if(productId.equals("0")) {//自定义金额
				orderAmount=String.valueOf(Integer.valueOf(oAmt)*100);
				
				map.put("productName", "自定义金额");
			}else {//条目中的金额
				Diamond d=diamondService.selectByPrimaryKey(Long.valueOf(productId));
				orderAmount =String.valueOf(d.getPrice().multiply(BigDecimal.valueOf(100)).intValue());
				
				map.put("productName", d.getName());
			}
			
			
			
			
			map.put("orderId", orderId);
			map.put("orderAmount", orderAmount);
			
			
			
        	return respBodyWriter.toSuccess(map);
        }
        
        @RequestMapping("/signMsgVal")
        @ResponseBody
        public RespBody getSignMsgVal(@RequestParam(value = "inputCharset", required = false)  String inputCharset,
        							@RequestParam(value = "pageUrl", required = false)  String pageUrl,
        							@RequestParam(value = "bgUrl", required = false)  String bgUrl,
        							@RequestParam(value = "version", required = false)  String version,
        							@RequestParam(value = "language", required = false)  String language,
        							@RequestParam(value = "signType", required = false)  String signType,
        							@RequestParam(value = "merchantAcctId", required = false)  String merchantAcctId,
        							@RequestParam(value = "payerName", required = false)  String payerName,
        							@RequestParam(value = "payerContactType", required = false)  String payerContactType,
        							@RequestParam(value = "payerContact", required = false)  String payerContact,
        							@RequestParam(value = "orderId", required = false)  String orderId,
        							@RequestParam(value = "orderAmount", required = false)  String orderAmount,
        							@RequestParam(value = "orderTime", required = false)  String orderTime,
        							@RequestParam(value = "productName", required = false)  String productName,
        							@RequestParam(value = "productNum", required = false)  String productNum,
        							@RequestParam(value = "productId", required = false)  String productId,
        							@RequestParam(value = "productDesc", required = false)  String productDesc,
        							@RequestParam(value = "ext1", required = false)  String ext1,
        							@RequestParam(value = "ext2", required = false)  String ext2,
        							@RequestParam(value = "payType", required = false)  String payType,
        							@RequestParam(value = "bankId", required = false)  String bankId,
        							@RequestParam(value = "redoFlag", required = false)  String redoFlag,
        							@RequestParam(value = "pid", required = false)  String pid,
        							HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		        	String signMsgVal = "";
		        	signMsgVal = appendParam(signMsgVal, "inputCharset", inputCharset);
		        	signMsgVal = appendParam(signMsgVal, "pageUrl", pageUrl);
		        	signMsgVal = appendParam(signMsgVal, "bgUrl", bgUrl);
		        	signMsgVal = appendParam(signMsgVal, "version", version);
		        	signMsgVal = appendParam(signMsgVal, "language", language);
		        	signMsgVal = appendParam(signMsgVal, "signType", signType);
		        	signMsgVal = appendParam(signMsgVal, "merchantAcctId",merchantAcctId);
		        	signMsgVal = appendParam(signMsgVal, "payerName", payerName);
		        	signMsgVal = appendParam(signMsgVal, "payerContactType",payerContactType);
		        	signMsgVal = appendParam(signMsgVal, "payerContact", payerContact);
		        	signMsgVal = appendParam(signMsgVal, "orderId", orderId);
		        	signMsgVal = appendParam(signMsgVal, "orderAmount", orderAmount);
		        	signMsgVal = appendParam(signMsgVal, "orderTime", orderTime);
		        	signMsgVal = appendParam(signMsgVal, "productName", productName);
		        	signMsgVal = appendParam(signMsgVal, "productNum", productNum);
		        	signMsgVal = appendParam(signMsgVal, "productId", productId);
		        	signMsgVal = appendParam(signMsgVal, "productDesc", productDesc);
		        	signMsgVal = appendParam(signMsgVal, "ext1", ext1);
		        	signMsgVal = appendParam(signMsgVal, "ext2", ext2);
		        	signMsgVal = appendParam(signMsgVal, "payType", payType);
		        	signMsgVal = appendParam(signMsgVal, "bankId", bankId);
		        	signMsgVal = appendParam(signMsgVal, "redoFlag", redoFlag);
		        	signMsgVal = appendParam(signMsgVal, "pid", pid);
        	Pkipair pki = new Pkipair();
        	 System.out.println(signMsgVal);
        	String signMsg = pki.signMsg(signMsgVal);
        	
        	Map map=new HashMap();
			map.put("signMsg", signMsg);
        	
        	return respBodyWriter.toSuccess(map);
        }

}
