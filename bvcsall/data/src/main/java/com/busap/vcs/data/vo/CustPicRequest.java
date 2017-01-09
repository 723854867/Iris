package com.busap.vcs.data.vo;

import java.util.Map;

/**
 * 证卡文字识别接口请求
 * Created by Knight on 16/5/30.
 */
public class CustPicRequest {

    /**
     * 业务编码custPicIdentify
     */
    private String busiCode;
    /**
     * 请求源
     */
    private String requestSource;
    /**
     * 全国唯一操 作流水号
     */
    private String transactionID;
    /**
     * 时间戳 + 6位随机数
     * yyyyMMddHHmmssSSS + 6位随机数
     */
    private String sn;
    /**
     * 请求参数
     * 加密说明:
     * 1、 如果加密字段存在子节点,则加密方式为该节点下的所有子节点 以Map格
     * 式封装,转化为json字符串,然后再加密。
     * 2、 如果加密字段为子节点, 则加密方式为 直接加密该字段取值
     * RealNameMsDesPlus.encrypt()
     */
    private String paramsJson;
    /**
     * 输入图片流
     * 把图片流转化为iso-8859-1字符串传输。
     */
    private Map<String, String> images;
    /**
     * 验签数据:
     * 签名对入参进行签名 (请求来源编码+全网 唯一操作流水号+接入 用户名+接入用户密码 +sn)
     * 使用工具类Rsa.java中的方法new Rsa.Encoder(privateKey).encode(transactionID + provinceCode +billId);进行签名。
     */
    private String signature;

    public String getBusiCode() {
        return busiCode;
    }

    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
    }

    public String getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(String requestSource) {
        this.requestSource = requestSource;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getParamsJson() {
        return paramsJson;
    }

    public void setParamsJson(String paramsJson) {
        this.paramsJson = paramsJson;
    }

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
