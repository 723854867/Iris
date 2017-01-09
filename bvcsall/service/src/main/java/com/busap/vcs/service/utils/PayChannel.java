package com.busap.vcs.service.utils;

/**
 * 第三方支付渠道
 * Created by Knight on 15/12/21.
 */
public enum PayChannel {
    /**
     alipay: 支付宝手机支付
     alipay_wap:支付宝手机网页支付
     alipay_pc_direct:支付宝 PC 网页支付
     alipay_qr:支付宝扫码支付
     apple_pay:Apple Pay
     bfb:百度钱包移动快捷支付
     bfb_wap:百度钱包手机网页支付
     upacp:银联全渠道支付（2015 年 1 月 1 日后的银联新商户使用。若有疑问，请与 Ping++ 或者相关的收单行联系）
     upacp_wap:银联全渠道手机网页支付（2015 年 1 月 1 日后的银联新商户使用。若有疑问，请与 Ping++ 或者相关的收单行联系）
     upacp_pc:银联 PC 网页支付
     upmp:银联手机支付（限个人工作室和 2014 年之前的银联老客户使用。若有疑问，请与 Ping++ 或者相关的收单行联系）
     upmp_wap:银联手机网页支付（限个人工作室和 2014 年之前的银联老客户使用。若有疑问，请与 Ping++ 或者相关的收单行联系）
     wx:微信支付
     wx_pub:微信公众账号支付
     wx_pub_qr:微信公众账号扫码支付
     yeepay_wap:易宝手机网页支付
     jdpay_wap:京东手机网页支付
     cnp_u:应用内快捷支付（银联）
     cnp_f:应用内快捷支付（外卡）
     */

    alipay("alipay", 1),            // 支付宝手机支付
    alipay_wap("alipay_wap", 2),
    alipay_pc_direct("alipay_pc_direct", 3),
    alipay_qr("alipay_qr", 4),
    apple_pay("apple_pay", 5),      // Apple Pay
    bfb("bfb", 6),
    bfb_wap("bfb_wap", 7),
    upacp("upacp", 8),
    upacp_wap("upacp_wap", 9),
    upacp_pc("upacp_pc", 10),
    upmp("upmp", 11),
    upmp_wap("upmp_wap", 12),
    wx("wx", 13),                   // 微信支付
    wx_pub("wx_pub", 14),
    wx_pub_qr("wx_pub_qr", 15),
    yeepay_wap("yeepay_wap", 16),
    jdpay_wap("jdpay_wap", 17),
    cnp_u("cnp_u", 18),
    cnp_f("cnp_f", 19),
    app_store("app_store", 20);


    private String channel;

    private int type;


    PayChannel(String channel, int type) {
        this.channel = channel;
        this.type = type;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
