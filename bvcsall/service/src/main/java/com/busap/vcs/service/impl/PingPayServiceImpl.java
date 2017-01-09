package com.busap.vcs.service.impl;

import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.*;
import com.busap.vcs.data.mapper.ExchangeRecodeMapper;
import com.busap.vcs.data.mapper.OrderPayMapper;
import com.busap.vcs.data.model.UserChargeDetail;
import com.busap.vcs.data.vo.ConsumePayVO;
import com.busap.vcs.service.*;
import com.busap.vcs.service.utils.ChargeVO;
import com.busap.vcs.service.utils.OrderStatus;
import com.busap.vcs.service.utils.PayChannel;
import com.busap.vcs.service.utils.TransferVO;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Transfer;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.net.ssl.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ping pay implements
 * Created by Knight on 15/12/22.
 */
@Service("pingPayService")
public class PingPayServiceImpl implements PingPayService {

    private Logger logger = LoggerFactory.getLogger(PingPayServiceImpl.class);

    @Autowired
    private OrderPayMapper orderPayMapper;

    @Autowired
    private ExchangeRecodeMapper exchangeRecodeMapper;

    @Autowired
    private DiamondService diamondService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private AnchorService anchorService;

    @Resource(name="ruserService")
    private RuserService ruserService;

    private String DIAMOND_CURRENCY = "dia";

    private String EXCHANGE_CHANNEL = "exchange";


    @Transactional
    public Charge createOrder(String channel,
                              String clientIp,
                              String appId,
                              Long produceId,
                              Long userId,
                              String openId,
                              String appVersion,
                              String platform)
            throws ChannelException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {

        Diamond diamond = diamondService.selectByPrimaryKey(produceId);
        if (diamond == null || diamond.getState() != 1) {
            logger.info("=========> diamond produce is not exist: produceId=" + produceId);
            return null;
        }
        // 查此userId是否有效
        Ruser ruser = ruserService.find(userId);
        if (ruser == null) {
            return null;
        }

        Integer amount = diamond.getPrice().multiply(new BigDecimal(100)).intValue();
        String orderNo = createOrderNo();
        String subject = diamond.getName();
        String body = diamond.getName() + "价值" + diamond.getPrice().toString() + "元";

        long current = System.currentTimeMillis() / 1000l;
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("order_no", orderNo);
        chargeParams.put("amount", amount);
        Map<String, String> app = new HashMap<String, String>();
        app.put("id", appId);
        chargeParams.put("app", app);
        chargeParams.put("channel", channel);
        chargeParams.put("currency", "cny");
        chargeParams.put("client_ip", clientIp);
        chargeParams.put("subject", subject);
        if ("wx_pub".equals(channel)) {
            if (StringUtils.isNotBlank(openId)) {
                Map<String, String> extra = new HashMap<String, String>();
                extra.put("open_id", openId);
                chargeParams.put("extra",  extra);
            } else {
                logger.error("get WeChat openId error! openId is null!");
            }
        }
        chargeParams.put("body",  body);
        logger.info("=========> send msg to create Charge: " + JSONObject.fromObject(chargeParams).toString());
        Charge charge = Charge.create(chargeParams);

        if (charge != null) {
            // 插入order_pay表
            insertOrderPay(diamond, orderNo, charge.getId(), channel, clientIp,
                    current, userId, produceId, OrderStatus.CREATE.getStatus(), null, appVersion, platform);
            logger.info("=========> return Charge: " + JSONObject.fromObject(charge).toString());
            return charge;
        }
        return null;

    }

    @Transactional
    public String appStorePay(String channel, String clientIp, Long produceId, Long userId, String transactionNo, int status, String appVersion, String platform) {
        Diamond diamond = diamondService.selectByPrimaryKey(produceId);
        // 查此produceId是否有效
        if (diamond == null || diamond.getState() != 1) {
            logger.info("=========> diamond produce is not exist: produceId=" + produceId);
            return null;
        }
        // 查此userId是否有效
        Ruser ruser = ruserService.find(userId);
        if (ruser == null) {
            return null;
        }
        String orderNo = createOrderNo();
        long current = System.currentTimeMillis() / 1000l;
        if (OrderStatus.PAID.getStatus() == status) {
            if (insertOrderPay(diamond, orderNo, "", channel, clientIp, current, userId, produceId, status, transactionNo, appVersion, platform)) {
                ruser.setDiamondCount(ruser.getDiamondCount() + diamond.getDiamondCount() + diamond.getGiveCount());
                ruserService.update(ruser);
            }
        } else {
            OrderPayExample example = new OrderPayExample();
            example.createCriteria().andChannelEqualTo(channel).andTransactionNoEqualTo(transactionNo);
            List<OrderPay> list = orderPayMapper.selectByExample(example);
            if (list.size() == 1) {
                OrderPay orderPay = list.get(0);
                if (OrderStatus.PAID.getStatus() != orderPay.getStatus()) {
                    orderPay.setStatus(status);
                    orderPayMapper.updateByPrimaryKey(orderPay);
                }
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderNo", orderNo);
        map.put("transactionNo", transactionNo);
        map.put("diamondCount", (diamond.getGiveCount() + diamond.getDiamondCount()));
        map.put("amount", diamond.getPrice());
        map.put("status", status);
        return JSONObject.fromObject(map).toString();
    }
    
    @Transactional
    public String yybPay(String channel, String clientIp, Long produceId, Long userId, String transactionNo, int status, String appVersion, String platform) {
        Diamond diamond = diamondService.selectByPrimaryKey(produceId);
        // 查此produceId是否有效
        if (diamond == null || diamond.getState() != 1) {
            logger.info("=========> diamond produce is not exist: produceId=" + produceId);
            return null;
        }
        // 查此userId是否有效
        Ruser ruser = ruserService.find(userId);
        if (ruser == null) {
            return null;
        }
        String orderNo = createOrderNo();
        long current = System.currentTimeMillis() / 1000l;
        String orderId="";
        if (OrderStatus.CREATE.getStatus() == status) {
            if (insertOrderPay(diamond, orderNo, "", channel, clientIp, current, userId, produceId, status, orderNo, appVersion, platform)) {
//                ruser.setDiamondCount(ruser.getDiamondCount() + diamond.getDiamondCount() + diamond.getGiveCount());
//                ruserService.update(ruser);
            }
            
            OrderPayExample example = new OrderPayExample();
            example.createCriteria().andChannelEqualTo(channel).andOrderNoEqualTo(orderNo);
            List<OrderPay> list = orderPayMapper.selectByExample(example);
            if (list.size() == 1) {
                OrderPay orderPay = list.get(0);
                orderId=orderPay.getId().toString();
            }
        } else {
            OrderPayExample example = new OrderPayExample();
            example.createCriteria().andChannelEqualTo(channel).andTransactionNoEqualTo(transactionNo);
            List<OrderPay> list = orderPayMapper.selectByExample(example);
            if (list.size() == 1) {
                OrderPay orderPay = list.get(0);
                if (OrderStatus.PAID.getStatus() != orderPay.getStatus()) {
                    orderPay.setStatus(status);
                    orderPayMapper.updateByPrimaryKey(orderPay);
                }
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderNo", orderNo);
        map.put("transactionNo", transactionNo);
        map.put("diamondCount", (diamond.getGiveCount() + diamond.getDiamondCount()));
        map.put("amount", diamond.getPrice());
        map.put("status", status);
        map.put("orderId", orderId);
        return JSONObject.fromObject(map).toString();
    }
    
    
    @Transactional
    public String bill99Pay(String channel, String clientIp, Long produceId, Long userId, String transactionNo, int status, String appVersion, String platform,Integer amt) {
    	Diamond diamond =new Diamond();
    	if(produceId!=null&&produceId==0) {
    		diamond.setPrice(BigDecimal.valueOf(amt));
    		diamond.setName("自定义金额");
    		diamond.setIsGive(0);
    		diamond.setDiamondCount(amt*10);
    		diamond.setGiveCount(0);
    	}else {
    		diamond = diamondService.selectByPrimaryKey(produceId);
            // 查此produceId是否有效
            if (diamond == null || diamond.getState() != 1) {
                logger.info("=========> diamond produce is not exist: produceId=" + produceId);
                return null;
            }
    	}
        
        // 查此userId是否有效
        Ruser ruser = ruserService.find(userId);
        if (ruser == null) {
            return null;
        }
        String orderNo = createOrderNo();
        long current = System.currentTimeMillis() / 1000l;
        String orderId="";
        if (OrderStatus.CREATE.getStatus() == status) {
            if (insertOrderPay(diamond, orderNo, "", channel, clientIp, current, userId, produceId, status, orderNo, appVersion, platform)) {
//                ruser.setDiamondCount(ruser.getDiamondCount() + diamond.getDiamondCount() + diamond.getGiveCount());
//                ruserService.update(ruser);
            }
            
            OrderPayExample example = new OrderPayExample();
            example.createCriteria().andChannelEqualTo(channel).andOrderNoEqualTo(orderNo);
            List<OrderPay> list = orderPayMapper.selectByExample(example);
            if (list.size() == 1) {
                OrderPay orderPay = list.get(0);
                orderId=orderPay.getId().toString();
            }
        } else {
            OrderPayExample example = new OrderPayExample();
            example.createCriteria().andChannelEqualTo(channel).andTransactionNoEqualTo(transactionNo);
            List<OrderPay> list = orderPayMapper.selectByExample(example);
            if (list.size() == 1) {
                OrderPay orderPay = list.get(0);
                if (OrderStatus.PAID.getStatus() != orderPay.getStatus()) {
                    orderPay.setStatus(status);
                    orderPayMapper.updateByPrimaryKey(orderPay);
                }
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderNo", orderNo);
        map.put("transactionNo", transactionNo);
        map.put("diamondCount", (diamond.getGiveCount() + diamond.getDiamondCount()));
        map.put("amount", diamond.getPrice());
        map.put("status", status);
        map.put("orderId", orderId);
        return JSONObject.fromObject(map).toString();
    }

    private boolean insertOrderPay(Diamond diamond, String orderNo, String chargeId, String channel, String clientIp,
                                long current, Long userId, Long produceId, int status, String transactionNo, String appVersion, String platform) {
        Integer amount = diamond.getPrice().multiply(new BigDecimal(100)).intValue();
        String body = diamond.getName() + "价值" + diamond.getPrice().toString() + "元";

        OrderPay order = new OrderPay();
        order.setAmount(amount);
        if ("app_store".equals(channel)) {
            // 苹果支付渠道 结算金额*0.7
            BigDecimal ab = new BigDecimal(amount);
            BigDecimal amountSettle = ab.multiply(new BigDecimal(0.7)).setScale(2, BigDecimal.ROUND_HALF_UP);
            order.setAmountSettle(amountSettle.intValue());
        } else {
            order.setAmountSettle(amount);
        }
        order.setAmountRefunded(0);
        order.setChannel(channel);
        order.setBody(body);
        order.setChargeId(chargeId);
        order.setClientIp(clientIp);
        order.setCreateTime(current);
        order.setCurrency("cny");
        order.setDescription("");
        order.setOrderNo(orderNo);
        order.setStatus(status);
        order.setSubject(diamond.getName());
        order.setUserId(userId);
        order.setProduceId(produceId);
        order.setReceived(diamond.getDiamondCount());
        order.setAppVersion(StringUtils.isNotBlank(appVersion) ? appVersion : "");
        order.setPlatform(StringUtils.isNotBlank(platform) ? platform : "");
        Ruser user = ruserService.find(userId);
        order.setSource(user.getRegPlatform());
        if (diamond.getIsGive() == 1) {
            order.setExtra(diamond.getGiveCount());
        } else {
            order.setExtra(0);
        }
        if (StringUtils.isNotBlank(transactionNo)) {
            order.setTransactionNo(transactionNo);
        }
        if (PayChannel.app_store.getChannel().equals(channel)) {
            order.setTimePaid(current);
        }

        int i = orderPayMapper.insertSelective(order);
        return i > 0;
    }

    /**
     * 兑换金币
     * @return true: 成功
     */
    private boolean insertOrderPay(Integer amount, Integer amountSettle, String channel, String body, String chargeId,
                                   String clientIp, Long createTime, String currency,String description, String orderNo,
                                   Integer status, String subject, Long userId, Long produceId, Integer received, String appVersion, String platform) {
        OrderPay order = new OrderPay();
        order.setAmount(amount);    // 价值人民币数
        order.setAmountSettle(amountSettle); // 结算：金豆数
        order.setAmountRefunded(0);
        order.setChannel(channel);
        order.setBody(body);
        order.setChargeId(chargeId);
        order.setClientIp(clientIp);
        order.setCreateTime(createTime);
        order.setCurrency(currency);
        order.setDescription(description);
        order.setOrderNo(orderNo);
        order.setStatus(status);
        order.setSubject(subject);
        order.setUserId(userId);
        order.setProduceId(produceId);
        order.setReceived(received); // 获得的金币数
        order.setExtra(0);
        order.setTransactionNo("");
        order.setTimePaid(createTime);
        order.setAppVersion(StringUtils.isNotBlank(appVersion) ? appVersion : "");
        order.setPlatform(StringUtils.isNotBlank(platform) ? platform : "");
        Ruser user = ruserService.find(userId);
        order.setSource(user.getRegPlatform() == null?"":user.getRegPlatform());
        return orderPayMapper.insertSelective(order) > 0;
    }

    /**
     * app store支付验证
     * @param url 链接地址
     * @param buyCode base64加密后的code
     * @return 返回值
     */
    public String buyAppVerify(String url, String buyCode) {

        BufferedOutputStream hurlBufOus = null;
        InputStream is = null;
        BufferedReader reader = null;
        HttpsURLConnection conn;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
                    new java.security.SecureRandom());
            URL console = new URL(url);
            conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "text/json");
            conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            hurlBufOus = new BufferedOutputStream(conn.getOutputStream());

            String str = String.format(Locale.CHINA, "{\"receipt-data\":\"" + buyCode+ "\"}");
            hurlBufOus.write(str.getBytes());
            hurlBufOus.flush();

            is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (Exception ex) {
            logger.error("-----> post to app store error", ex);
        } finally {
            try {
                if(null != is) {
                    is.close();
                }
                if(null != hurlBufOus) {
                    hurlBufOus.close();
                }
                if(null != reader) {
                    reader.close();
                }
            } catch (Exception e) {
                logger.error("error", e);
            }
        }
        return null;
    }
    public static void main(String[] args) {
		PingPayServiceImpl p = new PingPayServiceImpl();
		System.out.println(p.buyAppVerify("https://buy.itunes.apple.com/verifyReceipt", "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"));
	}

    /**
     * 检查流水号是否有重复
     * @param channel 渠道
     * @param transactionNo 流水号
     * @return true 成功
     */
    @Override
    public boolean checkTransactionNoExist(String channel, String transactionNo) {
        OrderPayExample example = new OrderPayExample();
        example.createCriteria().andChannelEqualTo(channel).andTransactionNoEqualTo(transactionNo);
        return orderPayMapper.countByExample(example) > 0;
    }

    /**
     * 金豆兑换金币
     * 1.扣金豆
     * 2.提现表加记录
     * 3.充值表加记录
     * 兑换:exchange type=1
     * @param userId  用户id
     * @param exchangeId 兑换商品ID: [exchange] primaryKey
     * @return true 成功
     */
    @Transactional
    public boolean exchange(Long userId, Long exchangeId, String appVersion, String platform) {
        Exchange exchange = exchangeService.selectByPrimaryKey(exchangeId);
        Ruser ruser = ruserService.find(userId);
        if ((ruser == null || (ruser.getIsBlacklist() != null && ruser.getIsBlacklist() == 0))) {
            return false;
        }
        Integer pointCount = exchange.getPointCount();
        String subject = "金豆兑换";
        String body = "兑换:" + pointCount + "金豆";
        String description = "用户" + userId + "兑换:" + pointCount + "金豆 = " + exchange.getDiamondCount() + "金币";
        if (exchange.getType() == 1 && anchorExchangePoint(userId, pointCount)) {
            String orderNo = createOrderNo();
            Long createTime = Calendar.getInstance().getTimeInMillis() / 1000l;
            Integer price = exchange.getPrice().multiply(new BigDecimal(100)).intValue();
            ExchangeRecode recode = new ExchangeRecode();
            recode.setOrderNo(orderNo);
            recode.setTimeTransferred(createTime);
            recode.setAmount(price);
            recode.setDiamond(exchange.getDiamondCount());
            recode.setChannel(EXCHANGE_CHANNEL);
            recode.setCreated(createTime);
            recode.setExchangePoint(pointCount);
            recode.setCurrency(DIAMOND_CURRENCY);
            recode.setUserId(userId);
            recode.setStatus("paid");
            recode.setSubject(subject);
            recode.setBody(body);
            recode.setDescription(description);
            recode.setProduceId(exchangeId);
            // 以下字段没有值 流水号、接受者openid、转账id、额外参数、失败消息
            recode.setTransactionNo("");
            recode.setRecipient("");
            recode.setTransferId("");
            recode.setExtra("");
            recode.setFailureMsg("");
            if (exchangeRecodeMapper.insert(recode) > 0) {
                // 兑换表插入成功 之后插入充值记录
                ruser.setDiamondCount(ruser.getDiamondCount() + exchange.getDiamondCount());
                ruserService.update(ruser);
                return insertOrderPay(price, pointCount, EXCHANGE_CHANNEL, body, "", "", createTime, DIAMOND_CURRENCY, description, orderNo,
                        OrderStatus.PAID.getStatus(), subject, userId, exchangeId, exchange.getDiamondCount(), appVersion, platform);
            }

        }

        return false;
    }

    @Transactional
    public boolean paySuccess(Map<String, Object> objectMap, String appId) {
        Map<String, Object> map = (Map<String, Object>) objectMap.get("object");
        BigDecimal bd = new BigDecimal((Double) map.get("time_paid"));
        Long timePaid = Long.parseLong(bd.toPlainString());

        ChargeVO charge = (ChargeVO) JSONObject.toBean(JSONObject.fromObject(objectMap.get("object")), ChargeVO.class);

        if (charge != null) {
            OrderPayExample example = new OrderPayExample();
            example.createCriteria().andOrderNoEqualTo(charge.getOrderNo()).andStatusEqualTo(OrderStatus.CREATE.getStatus());
            List<OrderPay> orderList = orderPayMapper.selectByExample(example);
            if (orderList.size() == 1) {
                OrderPay order = orderList.get(0);
                order.setStatus(OrderStatus.PAID.getStatus());
                order.setTimePaid(timePaid);                         // 更新支付完成时间
                order.setTransactionNo(charge.getTransactionNo());   // 更新流水号
                if (PayChannel.wx.getChannel().equals(charge.getChannel()) && charge.getExtra().size() > 0) {
                    // 如果是微信支付 更新openId
                    Map<String, Object> extra = charge.getExtra();
                    String openId = (String) extra.get("open_id");
                    if (StringUtils.isNotBlank(openId)) {
                        order.setOpenId(openId);
                    }
                    String bankType = (String) extra.get("bank_type");
                    if (StringUtils.isNotBlank(bankType)) {
                        order.setBankType(bankType);
                    }
                }
                int i = orderPayMapper.updateByPrimaryKeyAndStatus(order);
                if (i > 0) {
                    Ruser ruser = ruserService.find(order.getUserId());
//                    Integer dCount = ruser.getDiamondCount();
//                    if (dCount == null) {
//                        dCount = 0;
//                    }
//                    ruser.setDiamondCount(dCount + order.getReceived() + order.getExtra());
//                    ruserService.update(ruser);
                    ruserService.updateDiamond(ruser.getId(), order.getReceived() + order.getExtra());
                    logger.info("======> pay success: userId=" + order.getUserId()
                            + " getDiamond=" + (order.getReceived() + order.getExtra()));
                    return true;
                } else {
                    logger.error("pay error : update ruser error!");
                }
            } else {
                logger.error("pay error : order is not found!");
            }
        } else {
            logger.error("pay error : charge is null!");
        }
        return false;
    }

    /**
     * 更新主播金豆数量
     * @param userId 用户id
     * @param points 金豆数
     * @return true 更新成功 false金豆数量不足
     */
    private boolean anchorExchangePoint(Long userId, Integer points) {
        if (points != null && userId != null) {
            return anchorService.updateAnchorPoint(points, userId) > 0;
        }
        return false;
    }

    @Transactional
    public Transfer createTransfer(Long exchangeId, String appId, Long userId) {

        Exchange exchange = exchangeService.selectByPrimaryKey(exchangeId);
        if (exchange.getType() != 2) {
            // 提现 type应该为2
            return null;
        }

        if (!anchorExchangePoint(userId, exchange.getPointCount())) {
            return null;
        }
        Ruser ruser = ruserService.find(userId);
        if (StringUtils.isBlank(ruser.getOpenId())) {
            return null;
        }
        int amount = exchange.getPrice().multiply(new BigDecimal(100)).intValue();
        String orderNo = createOrderNo();
        String channel = "wx_pub";
        long time = System.currentTimeMillis() / 1000l;
        saveExchangeRecode(orderNo, amount, channel, time, ruser.getOpenId(), exchange, userId);
        // 组装transfer请求
        Map<String, Object> transferMap = new HashMap<String, Object>();
        transferMap.put("amount", amount);
        transferMap.put("currency", "cny");
        transferMap.put("type",  "b2c");
        transferMap.put("order_no",  orderNo);
        transferMap.put("channel", channel);
        transferMap.put("recipient", ruser.getOpenId());
        transferMap.put("description", "提现");
        Map<String, String> app = new HashMap<String, String>();
        app.put("id", appId);
        transferMap.put("app", app);
        logger.info("transfer=========> create Transfer:" + JSONObject.fromObject(transferMap).toString());
        Transfer transfer = null;
        try {
            transfer = Transfer.create(transferMap);
        } catch (AuthenticationException e) {
            logger.error("transfer=========> " + ResponseCode.CODE_603.toString());
            e.printStackTrace();
            rollbackData(userId, exchange.getPointCount(), orderNo);
        } catch (InvalidRequestException e) {
            logger.error("transfer=========> " + ResponseCode.CODE_604.toString());
            e.printStackTrace();
            rollbackData(userId, exchange.getPointCount(), orderNo);
        } catch (APIConnectionException e) {
            logger.error("transfer=========> " + ResponseCode.CODE_605.toString());
            e.printStackTrace();
            rollbackData(userId, exchange.getPointCount(), orderNo);
        } catch (APIException e) {
            logger.error("transfer=========> " + ResponseCode.CODE_606.toString());
            e.printStackTrace();
            rollbackData(userId, exchange.getPointCount(), orderNo);
        } catch (ChannelException e) {
            logger.error("transfer=========> " + ResponseCode.CODE_607.toString());
            e.printStackTrace();
            rollbackData(userId, exchange.getPointCount(), orderNo);
        }
        if (transfer != null ) {
            return  updateExchangeRecode(orderNo, transfer) ? transfer : null;
        }
        return null;
    }

    private boolean rollbackData(Long userId, Integer pointCount, String orderNo) {
        if (pointCount != null && userId != null) {
            if (anchorService.rollbackAnchorPoint(pointCount, userId) > 0 && StringUtils.isNotBlank(orderNo)) {
                return updateExchangeRecodeStatusByOrderNo(orderNo, "failed");
            }
        }
        return false;
    }

    public Integer getDailyTransferCount(Long userId, Long transferId) {
        Exchange exchange = exchangeService.selectByPrimaryKey(transferId);
        Integer price = 0;
        if (exchange != null) {
            price = exchange.getPrice().multiply(new BigDecimal(100)).intValue();
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            long time = c.getTimeInMillis() / 1000l;

            ExchangeRecodeExample example = new ExchangeRecodeExample();
            example.createCriteria().andUserIdEqualTo(userId).andCreatedGreaterThanOrEqualTo(time)
                    .andStatusEqualTo("paid").andChannelEqualTo("wx_pub");
            List<ExchangeRecode> exchangeRecodeList = exchangeRecodeMapper.selectByExample(example);

            for (ExchangeRecode recode : exchangeRecodeList) {
                price += recode.getAmount();
            }
        }
        return price;
    }

    public Integer getDailyTransferTime(String openId) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        long time = c.getTimeInMillis() / 1000l;

        ExchangeRecodeExample example = new ExchangeRecodeExample();
        example.createCriteria().andRecipientEqualTo(openId).andCreatedGreaterThanOrEqualTo(time)
                .andStatusEqualTo("paid").andChannelEqualTo("wx_pub");
        return exchangeRecodeMapper.countByExample(example);
    }

    @Transactional
    public boolean giveDiamondByActivity(Ruser ruser, Integer diamond, Integer type, long time, String appVersion, String platform) {
        String orderNo = createOrderNo();
        if (type == Constants.GiveDiamondType.SHARE.getType()) {
        	if (diamond.intValue() >0 ) {
        		String body = "用户:" + ruser.getId() + "分享赠送" + diamond + "金币";
        		String subject = "分享赠送";
        		String description = "用户:" + ruser.getId() + "分享赠送" + diamond + "金币";
        		insertOrderPay(0, 0, Constants.GiveDiamondType.SHARE.getChannel(), body, "", "", time, "cny",
        				description, orderNo, OrderStatus.PAID.getStatus(), subject, ruser.getId(), 0l, diamond, appVersion, platform);
        		ruserService.updateDiamond(ruser.getId(), diamond);
        		return true;
        	}
//            Integer diaCount = ruser.getDiamondCount() == null ? diamond : ruser.getDiamondCount() + diamond;
//            ruser.setDiamondCount(diaCount);
//            Ruser user = ruserService.update(ruser);
//            return diaCount.equals(user.getDiamondCount());
        } else if (type == Constants.GiveDiamondType.LIVE.getType()) {
        	if (diamond.intValue() >0 ) {
        		String body = "用户:" + ruser.getId() + "首次直播赠送" + diamond + "金币";
        		String subject = "首次直播赠送";
        		String description = "用户:" + ruser.getId() + "首次直播赠送" + diamond + "金币";
        		insertOrderPay(0, 0, Constants.GiveDiamondType.LIVE.getChannel(), body, "", "", time, "cny",
        				description, orderNo, OrderStatus.PAID.getStatus(), subject, ruser.getId(), 0l, diamond, appVersion, platform);
        		ruserService.updateDiamond(ruser.getId(), diamond);
        		return true;
        	}
//            Integer diaCount = ruser.getDiamondCount() == null ? diamond : ruser.getDiamondCount() + diamond;
//            ruser.setDiamondCount(diaCount);
//            Ruser user = ruserService.update(ruser);
//            return diaCount.equals(user.getDiamondCount());
        } else if (type == Constants.GiveDiamondType.REGISTER_360.getType()) {
        	if (diamond.intValue() >0 ) {
        		String body = "用户:" + ruser.getId() + "360特殊渠道注册赠送" + diamond + "金币";
        		String subject = "360特殊渠道注册赠送";
        		String description = "用户:" + ruser.getId() + "360特殊渠道注册赠送" + diamond + "金币";
        		insertOrderPay(0, 0, Constants.GiveDiamondType.REGISTER_360.getChannel(), body, "", "", time, "cny",
        				description, orderNo, OrderStatus.PAID.getStatus(), subject, ruser.getId(), 0l, diamond, appVersion, platform);
        		ruserService.updateDiamond(ruser.getId(), diamond);
        		return true;
        	}
//            Integer diaCount = ruser.getDiamondCount() == null ? diamond : ruser.getDiamondCount() + diamond;
//            ruser.setDiamondCount(diaCount);
//            Ruser user = ruserService.update(ruser);
//            return diaCount.equals(user.getDiamondCount());
        }
        return false;
    }

    @Override
    public List<ExchangeRecode> getTransferRecord(Long userId, String channel) {
        ExchangeRecodeExample example = new ExchangeRecodeExample();
        example.createCriteria().andUserIdEqualTo(userId).andChannelEqualTo(channel).andStatusEqualTo("paid");
        example.setOrderByClause(" created desc ");
        return exchangeRecodeMapper.selectByExample(example);
    }

    /**
     * 保存兑换记录
     * @param exchange 兑换商品对象
     * @param userId 用户id
     * @return 是否成功
     */
    private boolean saveExchangeRecode(String orderNo, Integer amount, String channel, Long created,
                                       String recipient, Exchange exchange, Long userId) {
        ExchangeRecode recode = new ExchangeRecode();
        recode.setOrderNo(orderNo);
        recode.setTimeTransferred(0l);
        recode.setAmount(amount);
        recode.setDiamond(0);
        recode.setChannel(channel);
        recode.setCreated(created);
        recode.setExchangePoint(exchange.getPointCount());
        recode.setCurrency("cny");
        recode.setRecipient(recipient);
        recode.setStatus("create");
        recode.setProduceId(exchange.getId());
        recode.setBody("提现:" + exchange.getPointCount() + "金豆");
        recode.setDescription("用户" + userId + "提现:" + exchange.getPointCount() + "金豆 = 人民币"
                + exchange.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "元");
        recode.setTransactionNo("");
        recode.setSubject("金豆提现");
        recode.setTransferId("");
        recode.setUserId(userId);
        recode.setFailureMsg("");
        return exchangeRecodeMapper.insert(recode) > 0;
    }
    
    /**
     * 保存兑换记录
     * @param exchange 兑换商品对象
     * @param userId 用户id
     * @return 是否成功
     */
    public boolean saveExchangeRecodeForOrgSettlement(Integer amount, Long userId,Integer point) {
        ExchangeRecode recode = new ExchangeRecode();
        String orderNo = createOrderNo();
        recode.setOrderNo(orderNo);
        recode.setTimeTransferred(0l);
        recode.setAmount(amount);
        recode.setDiamond(0);
        recode.setChannel("org");
        long time = System.currentTimeMillis() / 1000l;
        recode.setCreated(time);
        recode.setExchangePoint(point);
        recode.setCurrency("cny");
        recode.setRecipient("");
        recode.setStatus("paid");
        recode.setProduceId(0l);
        recode.setBody("提现:" + point + "金豆");
        recode.setDescription("用户" + userId + "提现:" + point + "金豆 = 人民币"+ amount + "分");
        recode.setTransactionNo("");
        recode.setSubject("金豆提现");
        recode.setTransferId("");
        recode.setUserId(userId);
        recode.setFailureMsg("");
        recode.setExtra("");
        return exchangeRecodeMapper.insert(recode) > 0;
    }

    public boolean updateExchangeRecodeStatusByOrderNo(String orderNo, String status) {
        ExchangeRecodeExample example = new ExchangeRecodeExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        ExchangeRecode recode = new ExchangeRecode();
        recode.setStatus(status);
        return exchangeRecodeMapper.updateByExampleSelective(recode, example) > 0;
    }
    
    public boolean updateOrderPayStatusByOrderNo(String orderNo, String status) {
        OrderPayExample example = new OrderPayExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        OrderPay order = new OrderPay();
        order.setStatus(Integer.valueOf(status));
        return orderPayMapper.updateByExampleSelective(order, example) > 0;
    }
    
    public boolean updateOrderPayStatusByOrderId(String orderId, String status,String billId,String description,String dealTime) {
        OrderPayExample example = new OrderPayExample();
        example.createCriteria().andIdEqualTo(Long.valueOf(orderId));
        OrderPay order = new OrderPay();
        order.setStatus(Integer.valueOf(status));
        order.setTransactionNo(billId);
        order.setBody(description);
        if(dealTime!=null&&!dealTime.equals("")) {
        	 Date dealDate=com.busap.vcs.util.DateUtils.parseDate("yyyyMMddHHmmss", dealTime);
        	 if(dealDate!=null) {
        		 order.setCreateTime(dealDate.getTime()/1000l);
        	 }
        }
       
        return orderPayMapper.updateByExampleSelective(order, example) > 0;
    }

    private boolean updateExchangeRecode(String orderNo, Transfer transfer) {
        ExchangeRecodeExample example = new ExchangeRecodeExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<ExchangeRecode> recodes = exchangeRecodeMapper.selectByExample(example);
        if (recodes != null && recodes.size() == 1) {
            ExchangeRecode recode = recodes.get(0);
            recode.setStatus(transfer.getStatus());
            recode.setTransferId(transfer.getId());
            recode.setCreated(transfer.getCreated());
            if (StringUtils.isNotBlank(transfer.getTransaction_no())) {
                recode.setTransactionNo(transfer.getTransaction_no());
            } else {
                recode.setTransactionNo("");
            }
            Map map = transfer.getExtra();
            JSONObject obj = JSONObject.fromObject(map);
            recode.setExtra(obj.toString());
            return exchangeRecodeMapper.updateByPrimaryKey(recode) > 0;
        }
        return false;
    }

    /**
     * 转账成功
     * @param objectMap 参数
     * @param appId APP ID
     * @return true: success
     */
    @Transactional
    public boolean transferSuccess(Map<String, Object> objectMap, String appId) {
        TransferVO transfer = (TransferVO) JSONObject.toBean(JSONObject.fromObject(objectMap.get("object")), TransferVO.class);
        if (transfer != null && "paid".equals(transfer.getStatus())) {
            ExchangeRecodeExample example = new ExchangeRecodeExample();
            example.createCriteria().andTransferIdEqualTo(transfer.getId());
            List<ExchangeRecode> list = exchangeRecodeMapper.selectByExample(example);
            if (list != null && list.size() == 1) {
                long time = System.currentTimeMillis() / 1000l;
                ExchangeRecode recode = list.get(0);
                recode.setStatus(transfer.getStatus());
//                recode.setTimeTransferred(transfer.getTimeTransferred());
                recode.setTimeTransferred(time);
                int i = exchangeRecodeMapper.updateByPrimaryKey(recode);
                if (i > 0 && "failed".equals(transfer.getStatus())) {
                    anchorService.rollbackAnchorPoint(recode.getExchangePoint(), recode.getUserId());
                }
                return true;
            }
        }
        logger.error("Transfer error : transfer is null!");
        return false;
    }

    private PublicKey getPubKey(String pubKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(pubKey);
        // generate public key
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    public boolean verifySignature(String data, String sigString, String publicKey) throws Exception {
        PublicKey pk = getPubKey(publicKey);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(pk);
        signature.update(data.getBytes());

        byte[] sigBytes = Base64.decodeBase64(sigString);
        return signature.verify(sigBytes);
    }

    @Override
    public List<ConsumePayVO> getByCondition(Integer paramType, String param, Long start, Long end, Integer chargeType,
                                             Integer page, Integer rows,String channel,String isGive, String appVersion, String platform, String source) {
        OrderPayExample example = new OrderPayExample();
        OrderPayExample.Criteria criteria = example.createCriteria();
        if (paramType > 0 && StringUtils.isNotBlank(param)) {
            if (paramType == 1) {
                List<Long> uidList = ruserService.fuzzyQueryByPhone(param);
                if (uidList != null && uidList.size() > 0) {
                    criteria = criteria.andUserIdIn(uidList);
                }
            } else if (paramType == 2 && StringUtils.isNotBlank(param)) {
                List<Long> uidList = ruserService.fuzzyQueryByName(param);
                if (uidList != null && uidList.size() > 0) {
                    criteria = criteria.andUserIdIn(uidList);
                }
            } else if (paramType == 3) {
                Long userId = Long.parseLong(param);
                criteria = criteria.andUserIdEqualTo(userId);
            } else if (paramType == 4) {
                criteria = criteria.andTransactionNoEqualTo(param);
            } else if (paramType == 5) {
                criteria = criteria.andChannelEqualTo(param);
            }
        }

        if (start != null && start > 0) {
            criteria = criteria.andCreateTimeGreaterThan(start);
        }

        if (end != null && end > 0) {
            criteria = criteria.andCreateTimeLessThan(end);
        }

        if (chargeType > 0) {
            criteria = criteria.andStatusEqualTo(chargeType);
        }
        if (StringUtils.isNotBlank(appVersion)) {
            criteria = criteria.andAppVersionEqualTo(appVersion);
        }

        if (StringUtils.isNotBlank(platform)) {
            criteria = criteria.andPlatformEqualTo(platform);
        }
        if (channel != null && !channel.equals("")) {
            List<String> channels = Arrays.asList(channel.split(","));
            criteria = criteria.andChannelIn(channels);
        }
        if (StringUtils.isNotBlank(source)) {
            criteria = criteria.andSourceEqualTo(source);
        }
        if (isGive != null && isGive.equals("1")) {
//        	criteria = criteria.andExtraIsNotNull();
        	criteria = criteria.andExtraGreaterThan(0);
        }else if(isGive != null && isGive.equals("2")) {
//        	criteria = criteria.andExtraIsNull();
        	criteria = criteria.andExtraEqualTo(0);
        }

        if (page > 0 && rows > 0) {
            int startRow = rows * (page - 1);
            example.setOrderByClause("create_time desc limit " + startRow + "," + rows);
        }

        List<OrderPay> orderPayList = orderPayMapper.selectByExample(example);
        List<ConsumePayVO> payConsumeVOList = new ArrayList<ConsumePayVO>();
        for (OrderPay orderPay : orderPayList) {
            ConsumePayVO payVO = new ConsumePayVO();
            BeanUtils.copyProperties(orderPay, payVO);
            Ruser ruser = ruserService.find(orderPay.getUserId());
            if (ruser != null) {
//                payVO.setUsername(ruser.getUsername());
            	payVO.setUsername(ruser.getName());
                payVO.setPhone(ruser.getPhone());
            }
            payVO.setTime(orderPay.getCreateTime());
            payConsumeVOList.add(payVO);
        }
        return payConsumeVOList;
    }

    @Override
    public List<OrderPay> getSumByCondition(Integer paramType, String param, Long start, Long end, Integer chargeType,
                                            Integer page, Integer rows,String channel,String isGive, String appVersion, String platform, String source) {
        OrderPayExample example = new OrderPayExample();
        OrderPayExample.Criteria criteria = example.createCriteria();
        if (paramType > 0 && StringUtils.isNotBlank(param)) {
            if (paramType == 1) {
                List<Long> uidList = ruserService.fuzzyQueryByPhone(param);
                if (uidList != null && uidList.size() > 0) {
                    criteria = criteria.andUserIdIn(uidList);
                }
            } else if (paramType == 2 && StringUtils.isNotBlank(param)) {
                List<Long> uidList = ruserService.fuzzyQueryByName(param);
                if (uidList != null && uidList.size() > 0) {
                    criteria = criteria.andUserIdIn(uidList);
                }
            } else if (paramType == 3) {
                Long userId = Long.parseLong(param);
                criteria = criteria.andUserIdEqualTo(userId);
            } else if (paramType == 4) {
                criteria = criteria.andTransactionNoEqualTo(param);
            } else if (paramType == 5) {
                criteria = criteria.andChannelEqualTo(param);
            }
        }

        if (start != null && start > 0) {
            criteria = criteria.andCreateTimeGreaterThan(start);
        }

        if (end != null && end > 0) {
            criteria = criteria.andCreateTimeLessThan(end);
        }

        if (chargeType > 0) {
            criteria = criteria.andStatusEqualTo(chargeType);
        }
        //大额上线后，会有很多未付款的垃圾数据，只查已付款的
        criteria = criteria.andStatusEqualTo(2);

        if (StringUtils.isNotBlank(appVersion)) {
            criteria = criteria.andAppVersionEqualTo(appVersion);
        }

        if (StringUtils.isNotBlank(platform)) {
            criteria = criteria.andPlatformEqualTo(platform);
        }

        if (channel != null && !channel.equals("")) {
            List<String> list = Arrays.asList(channel.split(","));
            if (list.size() > 0) {
                criteria = criteria.andChannelIn(list);
            }
        }
        if (StringUtils.isNotBlank(source)) {
            criteria = criteria.andSourceEqualTo(source);
        }

        if (isGive != null && isGive.equals("1")) {
//        	criteria = criteria.andExtraIsNotNull();
        	criteria = criteria.andExtraGreaterThan(0);
        }else if(isGive != null && isGive.equals("2")) {
//        	criteria = criteria.andExtraIsNull();
        	criteria = criteria.andExtraEqualTo(0);
        }


        List<OrderPay> orderPayList = orderPayMapper.selectSumByExample(example);
        return orderPayList;
    }

    public Integer findTotalChargeUsers(Long start, Long end) {
        OrderPayExample example = new OrderPayExample();
        OrderPayExample.Criteria criteria = example.createCriteria();
        if (start != null && start > 0) {
            criteria = criteria.andCreateTimeGreaterThan(start);
        }

        if (end != null && end > 0) {
            criteria = criteria.andCreateTimeLessThan(end);
        }
        List<String> channels = new ArrayList<String>();
        channels.add("wx");
        channels.add("alipay");
        channels.add("app_store");
        channels.add("wx_pub");
        criteria.andChannelIn(channels).andStatusEqualTo(OrderStatus.PAID.getStatus());

        List<OrderPay> orderPayList = orderPayMapper.selectSumByExample(example);
        if (orderPayList != null && orderPayList.size() == 1) {
            OrderPay orderPay = orderPayList.get(0);
            return orderPay.getUserId().intValue();
        }
        return 0;
    }

    public Integer countByCondition(Integer paramType, String param, Long start, Long end, Integer chargeType,String channel,String isGive) {
        OrderPayExample example = new OrderPayExample();
        OrderPayExample.Criteria criteria = example.createCriteria();
        if (paramType > 0 && StringUtils.isNotBlank(param)) {
            if (paramType == 1) {
                Ruser ruser = getRuserByParam("phone", param);
                if (ruser != null) {
                    criteria = criteria.andUserIdEqualTo(ruser.getId());
                } else {
                    return 0;
                }
            } else if (paramType == 2) {
                Ruser ruser = getRuserByParam("username", param);
                if (ruser != null) {
                    criteria = criteria.andUserIdEqualTo(ruser.getId());
                } else {
                    return 0;
                }
            } else if (paramType == 3) {
                Long userId = Long.parseLong(param);
                criteria = criteria.andUserIdEqualTo(userId);
            } else if (paramType == 4) {
                criteria = criteria.andTransactionNoEqualTo(param);
            } else if (paramType == 5) {
                criteria = criteria.andChannelEqualTo(param);
            }
        }

        if (start != null && start > 0) {
            criteria = criteria.andCreateTimeGreaterThan(start);
        }

        if (end != null && end > 0) {
            criteria = criteria.andCreateTimeLessThan(end);
        }

        if (channel != null && !channel.equals("")) {
            List<String> channels = Arrays.asList(channel.split(","));
            criteria = criteria.andChannelIn(channels);
        }

        if (isGive != null && isGive.equals("1")) {
//        	criteria = criteria.andExtraIsNotNull();
        	criteria = criteria.andExtraGreaterThan(0);
        }else if(isGive != null && isGive.equals("2")) {
        	criteria = criteria.andExtraIsNull();
        	criteria = criteria.andExtraEqualTo(0);
        }

        if (chargeType > 0) {
            criteria = criteria.andStatusEqualTo(chargeType);
        }

        return orderPayMapper.countByExample(example);

    }

    @Override
    public List<UserChargeDetail> exportByCondition(Integer paramType, String param, Long start, Long end, Integer chargeType,String channel,String isGive, String appVersion, String platform, String source) {
        List<UserChargeDetail> list = new ArrayList<UserChargeDetail>();

        List<ConsumePayVO> orderList =  getByCondition(paramType, param, start, end, chargeType, 0, 0, channel, isGive, appVersion, platform, source);
        for (ConsumePayVO payVO : orderList) {
            UserChargeDetail detail = new UserChargeDetail();
            Date date = new Date();
            date.setTime(payVO.getCreateTime() * 1000l);
            detail.setCreateTime(date);
            detail.setPhone(payVO.getPhone());
            detail.setAmount(getAmout(payVO.getAmount()));
            detail.setChannel(payVO.getChannel());
            detail.setTransactionNo(payVO.getTransactionNo());
            if (payVO.getStatus() == 1) {
                detail.setStatus("未付款");
            } else if (payVO.getStatus() == 2) {
                detail.setStatus("已付款");
            }
            detail.setUserId(payVO.getUserId());
            detail.setName(payVO.getUsername());
            detail.setExtra(payVO.getExtra() / 1d);
            detail.setExtraMoney(payVO.getExtra() / 10d);
            list.add(detail);
        }
        return list;
    }

    @Override
    public OrderPay getOrderPayById(Long orderId) {
        return orderPayMapper.selectByPrimaryKey(orderId);
    }

    private Ruser getRuserByParam(String column, String param) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(column, param);
        List<Ruser> rusers = ruserService.selectRusers(map);
        if (rusers.size() > 0) {
            return rusers.get(0);
        } else {
            return null;
        }
    }

    private String createOrderNo() {
        String order = UUID.randomUUID().toString();
        return order.replaceAll("-", "");
    }

    public OrderPayMapper getOrderPayMapper() {
		return orderPayMapper;
	}

    public Map<String, Object> getDisplayData(String orderNo) {
        OrderPayExample example = new OrderPayExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<OrderPay> list = orderPayMapper.selectByExample(example);
        if (list.size() > 0) {
            OrderPay order = list.get(0);
            Map<String, Object> map = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(order.getTransactionNo())) {
                map.put("transactionNo", order.getTransactionNo());
                map.put("diamondCount", (order.getReceived() + order.getExtra()));
                map.put("amount", order.getAmount());
                map.put("channel", order.getChannel());
                return map;
            }
        }
        return null;
    }


    private float getAmout(Integer amount) {
        if (amount == null) {
            return 0f;
        }
        BigDecimal b = new BigDecimal(amount);
        BigDecimal c = new BigDecimal(100f);
        BigDecimal result = b.divide(c, 2, BigDecimal.ROUND_HALF_UP);
        return result.floatValue();
    }

    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
