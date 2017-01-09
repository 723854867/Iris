package com.busap.vcs.service;

import com.busap.vcs.data.entity.ExchangeRecode;
import com.busap.vcs.data.entity.OrderPay;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.mapper.OrderPayMapper;
import com.busap.vcs.data.model.UserChargeDetail;
import com.busap.vcs.data.vo.ConsumePayVO;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Transfer;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;
import java.util.Map;

/**
 * ping pay
 * Created by Knight on 15/12/22.
 */
public interface PingPayService {

    /**
     * 创建订单
     * @param channel 支付渠道
     * @param produceId 产品id
     * @param clientIp 终端ip
     * @param userId  用户ID
     * @param appId appID常量
     * @return Charge
     */
    public Charge createOrder(String channel,
                              String clientIp,
                              String appId,
                              Long produceId,
                              Long userId,
                              String openId,
                              String appVersion,
                              String platform)
            throws ChannelException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException;

    /**
     * 创建app_store订单
     * @param channel 支付渠道
     * @param produceId 产品id
     * @param clientIp 终端ip
     * @param userId  用户ID
     * @return 成功:失败
     */
    public String appStorePay(String channel, String clientIp, Long produceId, Long userId, String transactionNo, int status, String appVersion, String platform);
    
    /**
     * 创建应用宝订单
     * @param channel 支付渠道
     * @param produceId 产品id
     * @param clientIp 终端ip
     * @param userId  用户ID
     * @return 成功:失败
     */
    public String yybPay(String channel, String clientIp, Long produceId, Long userId, String transactionNo, int status, String appVersion, String platform);
    
    
    public String bill99Pay(String channel, String clientIp, Long produceId, Long userId, String transactionNo, int status, String appVersion, String platform,Integer amt);

    /**
     * 支付成功
     * @param objectMap
     * @return 是否处理成功
     */
    public boolean paySuccess(Map<String, Object> objectMap, String appId);
    /**
     * 创建转账
     * @return 转账对象
     */
    public Transfer createTransfer(Long transferId, String appId, Long userId);

    public List<ExchangeRecode> getTransferRecord(Long userId, String channel);
    /**
     * 转账成功
     * @param objectMap
     * @return 是否处理成功
     */
    public boolean transferSuccess(Map<String, Object> objectMap, String appId);
    /**
     * 验证签名
     * @param data event签名
     * @param sigBytes 签名byte
     * @param publicKey 公钥
     * @return 是否验证成功
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public boolean verifySignature(String data, String sigBytes, String publicKey) throws Exception;
    
    public OrderPayMapper getOrderPayMapper();

    public Map<String, Object> getDisplayData(String orderNo);
    /**
     * 根据条件查询
     */
    public List<ConsumePayVO> getByCondition(Integer paramType, String param, Long start, Long end,Integer chargeType, Integer page,
                                             Integer rows,String channel,String isGive, String appVersion, String platform, String source);

    /**
     * 根据条件查询计数
     */
    public Integer countByCondition(Integer paramType, String param, Long start, Long end, Integer chargeType,String channel,String isGive);
    
    public List<OrderPay> getSumByCondition(Integer paramType, String param, Long start, Long end, Integer chargeType,Integer page, Integer rows,String channel,String isGive, String appVersion, String platformm, String source);
    /**
     * 根据条件导出
     */
    public List<UserChargeDetail> exportByCondition(Integer paramType, String param, Long start, Long end, Integer chargeType,String channel,String isGive, String appVersion, String platform, String source);

    public OrderPay getOrderPayById(Long orderId);

    // app store支付验证
    public String buyAppVerify(String url, String receipt);

    public boolean checkTransactionNoExist(String channel, String transactionNo);

    /**
     * 金豆兑换金币
     * @return true:success
     */
    public boolean exchange(Long userId, Long exchangeId, String appVersion, String platform);
    /**
     * 计算今天提现金额
     */
    public Integer getDailyTransferCount(Long userId, Long transferId);
    /**
     * 计算今天提现次数
     */
    public Integer getDailyTransferTime(String openId);

    /**
     * 活动赠送金币
     * @param ruser ruser
     * @param diamond 金币数
     * @param type 1:share 2:live
     * @return true:success
     */
    public boolean giveDiamondByActivity(Ruser ruser, Integer diamond, Integer type, long time, String appVersion, String platform);

    public Integer findTotalChargeUsers(Long start, Long end);
    
    public boolean updateExchangeRecodeStatusByOrderNo(String orderNo, String status);
    
    public boolean updateOrderPayStatusByOrderNo(String orderNo, String status);
    
    public boolean updateOrderPayStatusByOrderId(String orderId, String status,String billId,String description,String dealTime);
    
    public boolean saveExchangeRecodeForOrgSettlement(Integer amount, Long userId,Integer point);
}
