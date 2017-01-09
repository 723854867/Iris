//package com.busap.vcs.web;
//
//import com.busap.vcs.constants.ResponseCode;
//import com.busap.vcs.data.entity.Ruser;
//import com.busap.vcs.service.PingPayService;
//import com.busap.vcs.service.RuserService;
//import com.busap.vcs.webcomn.RespBody;
//import com.busap.vcs.webcomn.RespBodyBuilder;
//import com.pingplusplus.Pingpp;
//import com.pingplusplus.exception.*;
//import com.pingplusplus.model.Charge;
//import com.pingplusplus.model.Event;
//import com.pingplusplus.model.Webhooks;
//import net.sf.json.JSONObject;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.annotation.Resource;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Enumeration;
//import java.util.Map;
//
///**
// * 支付controller (Ping)
// * Created by Knight on 15/12/21.
// */
//@Controller
//@RequestMapping("/pay")
//public class PingPayController {
//
//    @Autowired
//    protected HttpServletRequest request;
//
//    @Value("${payment_live_mode}")
//    private String liveMode;
//
//    @Value("${payment_app_id}")
//    private String APP_ID;
//
//    @Value("${payment_sk}")
//    private String PAYMENT_SK;
//
//    @Value("${payment_pub_key}")
//    private String PAYMENT_PUB_KEY;
//
//    @Value("${verify_code}")
//    private String VERIFY_CODE;
//
//    @Autowired
//    private PingPayService pingPayService;
//
//    @Resource(name="ruserService")
//    private RuserService ruserService;
//
//    private Logger logger = LoggerFactory.getLogger(PingPayController.class);
//
//    @Resource(name = "respBodyBuilder")
//    private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
//
//    @RequestMapping("/callback")
//    public void callbackHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("UTF8");
//        //获取头部所有信息
//        Enumeration headerNames = request.getHeaderNames();
//        String signature = "";
//        while (headerNames.hasMoreElements()) {
//            String key = (String) headerNames.nextElement();
//            if ("x-pingplusplus-signature".equals(key)) {
//                signature = request.getHeader(key);
//            }
//        }
//
//
//        if (StringUtils.isBlank(signature)) {
//            response.setStatus(501);
//        } else {
//            // 获得 http body 内容
//            BufferedReader reader = request.getReader();
//            StringBuilder builder = new StringBuilder();
//            String string;
//            while ((string = reader.readLine()) != null) {
//                builder.append(string);
//            }
//            reader.close();
//            // 解析异步通知数据
//            String message = builder.toString();
//            logger.info("ping plus callback massage:" + message);
//            try {
//                // Verify signature
//                boolean verify = pingPayService.verifySignature(message, signature, PAYMENT_PUB_KEY);
//                if (!verify) {
//                    logger.info("=========> verify signature Failed!");
//                    response.setStatus(509);
//                }
//            } catch (Exception e) {
//                logger.info("=========> verify signature error!");
//                response.setStatus(503);
//            }
//            Event event = Webhooks.eventParse(message);
//            if ("charge.succeeded".equals(event.getType())) {
//                // 付款成功 回调
//                Map<String, Object> objectMap = event.getData();
//                if (pingPayService.paySuccess(objectMap, APP_ID)) {
//                    response.setStatus(200);
//                } else {
//                    logger.info("=========> operate pay error! " + JSONObject.fromObject(objectMap.get("object")).toString());
//                    response.setStatus(502);
//                }
//            } else {
//                response.setStatus(500);
//            }
//        }
//    }
//
//    @RequestMapping("/create")
//    @ResponseBody
//    public RespBody createHandler(String paramJson) {
//        // 设置apiKey用于Authentication认证
//        Pingpp.apiKey = PAYMENT_SK;
//        JSONObject jsonObject = JSONObject.fromObject(paramJson);
//        String channel = (String) jsonObject.get("channel");
//        String clientIp = (String) jsonObject.get("client_ip");
//        Integer userId = (Integer) jsonObject.get("userId");
//        Integer produceId = (Integer) jsonObject.get("produceId");
//        String openId = (String) jsonObject.get("openId");
//
//        if (!checkEmptyValue(channel, clientIp) || userId == null || produceId == null) {
//            logger.error("=========> 参数不全，有部分参数为空");
//            return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
//        }
//        try {
//            Charge charge = pingPayService.createOrder(channel, clientIp, APP_ID, Long.valueOf(produceId), Long.valueOf(userId), openId);
//            if (charge == null) {
//                logger.error("=========> " + ResponseCode.CODE_608.toString());
//                return respBodyWriter.toError(ResponseCode.CODE_608.toString(), ResponseCode.CODE_608.toCode());
//            }
//            return respBodyWriter.toSuccess(charge);
//        } catch (AuthenticationException e) {
//            logger.error("=========> " + ResponseCode.CODE_603.toString());
//            e.printStackTrace();
//            return respBodyWriter.toError(ResponseCode.CODE_603.toString(), ResponseCode.CODE_603.toCode());
//        } catch (InvalidRequestException e) {
//            logger.error("=========> " + ResponseCode.CODE_604.toString());
//            e.printStackTrace();
//            return respBodyWriter.toError(ResponseCode.CODE_604.toString(), ResponseCode.CODE_604.toCode());
//        } catch (APIConnectionException e) {
//            logger.error("=========> " + ResponseCode.CODE_605.toString());
//            e.printStackTrace();
//            return respBodyWriter.toError(ResponseCode.CODE_605.toString(), ResponseCode.CODE_605.toCode());
//        } catch (APIException e) {
//            logger.error("=========> " + ResponseCode.CODE_606.toString());
//            e.printStackTrace();
//            return respBodyWriter.toError(ResponseCode.CODE_606.toString(), ResponseCode.CODE_606.toCode());
//        } catch (ChannelException e) {
//            logger.error("=========> " + ResponseCode.CODE_607.toString());
//            e.printStackTrace();
//            return respBodyWriter.toError(ResponseCode.CODE_607.toString(), ResponseCode.CODE_607.toCode());
//        }
//    }
//
//    @RequestMapping("/appStorePay")
//    @ResponseBody
//    public RespBody appStorePayHandler(String paramJson, String verification) {
//    	return respBodyWriter.toError("保存订单失败");
////        if (verify(paramJson, verification)) {
////            JSONObject jsonObject = JSONObject.fromObject(paramJson);
////            String clientIp = (String) jsonObject.get("client_ip");
////            Integer userId = (Integer) jsonObject.get("userId");
////            Integer produceId = (Integer) jsonObject.get("produceId");
////            String transactionNo = (String) jsonObject.get("transactionNo");
////
////            String data = pingPayService.appStorePay("app_store", clientIp, Long.valueOf(produceId), Long.valueOf(userId), transactionNo,0);
////            if (StringUtils.isNotBlank(data)) {
////                return respBodyWriter.toSuccess(data);
////            } else {
////                logger.error("app store支付：保存订单失败！params=" + paramJson);
////            }
////        } else {
////            return respBodyWriter.toError("验证失败！");
////        }
//
//    }
//
//    @RequestMapping("/getDisplayData")
//    @ResponseBody
//    public RespBody getDisplayData(String orderNo) {
//        if (StringUtils.isBlank(orderNo)) {
//            return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
//        }
//        Map<String, Object> map = pingPayService.getDisplayData(orderNo);
//        if (map != null) {
//            return respBodyWriter.toSuccess(map);
//        } else {
//            return respBodyWriter.toError(ResponseCode.CODE_609.toString(), ResponseCode.CODE_609.toCode());
//        }
//    }
//
//    @RequestMapping("/getUserInfo")
//    @ResponseBody
//    public RespBody getUserInfoHandler(Long userId) {
//        if (userId == null) {
//            return respBodyWriter.toError(ResponseCode.CODE_305.toString(), ResponseCode.CODE_305.toCode());
//        } else {
//            Ruser ruser = ruserService.find(userId);
//            if (ruser == null) {
//                // 查此userId是否有效
//                return respBodyWriter.toError(ResponseCode.CODE_305.toString(), ResponseCode.CODE_305.toCode());
//            } else {
//                return respBodyWriter.toSuccess(ruser);
//            }
//        }
//    }
//
//    @RequestMapping("/wechatCallback")
//    public String wechatCallback(HttpServletRequest request,HttpServletResponse response) {
//        String code = request.getParameter("code");
//        String openId = "";
//        try {
//            openId = WxpubOAuth.getOpenId("wx8a908b16f1e26ade", "7a7b0afabac3902b936798ecbecd7141", code);
//            logger.info("wechat open id=" +openId);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        this.request.setAttribute("openId", openId);
//        return "html5/app_within/Recharge/wxrecharge/recharge";
//    }
//
//    private static boolean checkEmptyValue(String... params) {
//        for (String value : params) {
//            if (StringUtils.isBlank(value)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private boolean verify(String json, String verification) {
//        String resource = json + "_" + VERIFY_CODE;
//        try {
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            byte[] byteArray = resource.getBytes("UTF-8");
//            byte[] md5Bytes = md5.digest(byteArray);
//            StringBuilder hexValue = new StringBuilder();
//            for (byte md5Byte : md5Bytes) {
//                int val = ((int) md5Byte) & 0xff;
//                if (val < 16) {
//                    hexValue.append("0");
//                }
//                hexValue.append(Integer.toHexString(val));
//            }
//            return StringUtils.isNotBlank(verification) && hexValue.toString().equals(verification);
//        } catch (NoSuchAlgorithmException e) {
//            logger.error("没有这个加密算法");
//            return false;
//        } catch (UnsupportedEncodingException e) {
//            logger.error("不支持此类型解码");
//            return false;
//        }
//    }
//
//}
