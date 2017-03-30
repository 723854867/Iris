package org.Iris.app.pay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

public class AlipayService {

	private String appId;
	private String appPrivateKey;
	private String alipayPublicKey;
	private String charset = "UTF-8";
	private String notifyUrl = "";
	private String timeOutExpress = "";
	private String productCode="QUICK_MSECURITY_PAY";
	private AlipayClient alipayClient;

	public void init() {
		alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, appPrivateKey, "json",
				charset, alipayPublicKey, "RSA2");
	}

	/**
	 * 下单
	 * @param body
	 * @param subject
	 * @param outtradeno
	 * @param totalAmount
	 * @return
	 * @throws AlipayApiException 
	 */
	public String getOrderInfo(String body, String subject, String outTradeNo,String totalAmount) throws AlipayApiException {
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		AlipayTradeAppPayResponse response = new AlipayTradeAppPayResponse();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(body);
		model.setSubject(subject);
		model.setOutTradeNo(outTradeNo);
		model.setTimeoutExpress(timeOutExpress);
		model.setTotalAmount(totalAmount);
		model.setProductCode(productCode);
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		// 这里和普通的接口调用不同，使用的是sdkExecute
		response = alipayClient.sdkExecute(request);
		//System.out.println(response.getBody());// 就是orderString
		// 可以直接给客户端请求，无需再做处理。
		return response.getBody();
	}
	
	/**
	 * 退款
	 * @return
	 * @throws AlipayApiException 
	 */
	public AlipayTradeRefundResponse alipayTradeRefund(String outTradeNo,String refundAmount) throws AlipayApiException{
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		AlipayTradeRefundModel model = new AlipayTradeRefundModel();
		model.setOutTradeNo(outTradeNo);
		model.setRefundAmount(refundAmount);
		model.setRefundReason("正常退款");
		request.setBizModel(model);
		return alipayClient.execute(request);
	}
	
	/**
	 * 退款查询
	 * @param outTradeNo
	 * @return
	 * @throws AlipayApiException
	 */
	public AlipayTradeFastpayRefundQueryResponse alipayTradeFastRefundQuery(String outTradeNo) throws AlipayApiException{
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
		model.setOutTradeNo(outTradeNo);
		request.setBizModel(model);
		return alipayClient.execute(request);
	}
	
	
	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setAppPrivateKey(String appPrivateKey) {
		this.appPrivateKey = appPrivateKey;
	}

	public void setAlipayPublicKey(String alipayPublicKey) {
		this.alipayPublicKey = alipayPublicKey;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public void setTimeOutExpress(String timeOutExpress) {
		this.timeOutExpress = timeOutExpress;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public void setAlipayClient(AlipayClient alipayClient) {
		this.alipayClient = alipayClient;
	}

	public String getAppId() {
		return appId;
	}

	public String getAppPrivateKey() {
		return appPrivateKey;
	}

	public String getAlipayPublicKey() {
		return alipayPublicKey;
	}

	public String getCharset() {
		return charset;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public String getTimeOutExpress() {
		return timeOutExpress;
	}

	public String getProductCode() {
		return productCode;
	}

	public AlipayClient getAlipayClient() {
		return alipayClient;
	}

}
