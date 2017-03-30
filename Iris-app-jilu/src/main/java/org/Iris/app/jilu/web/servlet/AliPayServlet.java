package org.Iris.app.jilu.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.common.Beans;
import org.Iris.util.lang.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayApiException;

public class AliPayServlet extends HttpServlet{

	private static final long serialVersionUID = -4684820810754665877L;
	private static final Logger logger = LoggerFactory.getLogger(AliPayServlet.class);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = req.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		    String name = (String) iter.next();
		    String[] values = (String[]) requestParams.get(name);
		    String valueStr = "";
		    for (int i = 0; i < values.length; i++) {
		        valueStr = (i == values.length - 1) ? valueStr + values[i]
		                    : valueStr + values[i] + ",";
		  }
		  //乱码解决，这段代码在出现乱码时使用。
		  //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
		  params.put(name, valueStr);
		 }
		try {
			boolean flag = Beans.payService.rsaCheckV1(params);
			if(flag){
				String out_trade_no = req.getParameter("out_trade_no");
				String total_amount = req.getParameter("total_amount");
				String seller_id = req.getParameter("seller_id");
				String app_id = req.getParameter("app_id");
				//对上面上个参数进行验证 目前暂不进行。。。
				String trade_status = StringUtil.checkNull(req.getParameter("trade_status"));
				if(trade_status.equals("WAIT_BUYER_PAY")){
					//交易创建，等待买家付款
				}else if(trade_status.equals("TRADE_CLOSED")){
					//未付款交易超时关闭，或支付完成后全额退款
				}else if(trade_status.equals("TRADE_SUCCESS")){
					//交易支付成功
				}else if(trade_status.equals("TRADE_FINISHED")){
					//交易结束，不可退款
				}
				resp.getWriter().write("success");
			}else{
				logger.error("支付宝异步通知消息验证失败！");
				resp.getWriter().write("failure");
			}
		} catch (AlipayApiException e) {
			logger.error("支付宝异步通知消息验证异常!", e);
			resp.getWriter().write("failure");
		} catch (Exception e) {
			logger.error("支付宝异步通知消息验证异常!", e);
			resp.getWriter().write("failure");
		}
	}
}
