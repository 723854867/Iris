package org.Iris.app.jilu.service.realm.pay;

import java.util.Map;

import javax.annotation.Resource;

import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.pay.service.AlipayService;
import org.Iris.app.pay.service.WechatPayService;
import org.Iris.app.pay.wechat.response.UnifiedOrderResponse;
import org.Iris.core.service.bean.Result;
import org.Iris.util.network.http.HttpProxy;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

@Service
public class PayService {

	@Resource
	AlipayService alipayService;
	@Resource
	WechatPayService wechatPayService;
	
	/**
	 * 微信统一下单
	 * @return
	 */
	public String uniformOrder(String outTradeNo,double price,String ipAddress,String body,HttpProxy proxy){
		UnifiedOrderResponse response;
		try {
			response =  wechatPayService.unifiedOrder(outTradeNo, price, ipAddress, body, proxy);
			return Result.jsonSuccess(response);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.jsonError(JiLuCode.UNIFORM_ORDER_FAIL);
		}
	}
	
	/**
	 * 获取支付宝订单信息
	 * @param body
	 * @param subject
	 * @param outtradeno
	 * @param totalAmount
	 * @return
	 */
	public String getAlipayOrderInfo(String body,String subject,String outtradeno,float totalAmount){
		try {
			String	orderInfo = alipayService.getOrderInfo(body, subject, outtradeno, String.valueOf(totalAmount));
			return Result.jsonSuccess(orderInfo);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return Result.jsonError(JiLuCode.UNIFORM_ORDER_FAIL);
		}
	}
	
	/**
	 * 验证异步通知信息参数
	 * @param params
	 * @return
	 * @throws AlipayApiException 
	 */
	public boolean rsaCheckV1(Map<String, String> params) throws AlipayApiException{
		return AlipaySignature.rsaCheckV1(params, alipayService.getAlipayPublicKey(), alipayService.getCharset(), "RSA2");
	}
}
