package org.Iris.app.jilu.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.Iris.app.pay.wechat.util.Signature;
import org.Iris.app.pay.wechat.util.XMLParser;
import org.xml.sax.SAXException;

public class WechatPayServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		InputStream inputStream = request.getInputStream();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = XMLParser.getMapFromStream(inputStream);
			//验证签名
			if(!Signature.checkIsSignValidFromResponseString(map)){
				response.getWriter().write(setXml("FAIL", "signature failed"));
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
			// 进行支付成功处理
			response.getWriter().write(setXml("SUCCESS", ""));
			String attach = map.get("attach").toString();
			String sub_type = attach.split("_")[0];
			String subscribe_time = attach.split("_")[1];
			// 更新订单支付状态为已支付
		} else {
			response.getWriter().write(setXml("FAIL", map.get("return_msg").toString()));
		}

		return;
	}
	
	public static String setXml(String return_code,String return_msg){
		return "<xml><return_code><![CDATA["+return_code+"]]></return_code><return_msg><![CDATA["+return_msg+"]]></return_msg></xml>";
	}
}
