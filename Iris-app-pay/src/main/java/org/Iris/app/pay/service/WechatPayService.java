package org.Iris.app.pay.service;

import java.io.IOException;

import org.Iris.app.pay.wechat.request.UnifiedOrderReqeust;
import org.Iris.app.pay.wechat.response.UnifiedOrderResponse;
import org.Iris.app.pay.wechat.util.Configure;
import org.Iris.app.pay.wechat.util.RandomStringGenerator;
import org.Iris.app.pay.wechat.util.Signature;
import org.Iris.util.network.http.HttpProxy;
import org.Iris.util.network.http.handler.SyncXmlRespHandler;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class WechatPayService {

	private String APPID = "wxb1701cd6054b0318";
	private String MCH_ID = "1348978001";
	private String KEY = "";
	private String DEVICEINFO = "";
	private String charset = "UTF-8";
	private String FEETYPE = "CNY";
	private String NOTIFY_URL = "";
	private String TRADETYPE = "APP";
	
	/**
	 * 统一下单
	 * @throws IllegalAccessException 
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public void unifiedOrder(String outTradeNo, double price, String ipAddress, String body,HttpProxy proxy) throws Exception{
		UnifiedOrderReqeust reqeust = new UnifiedOrderReqeust();
		reqeust.setAppid(APPID);
		reqeust.setMch_id(MCH_ID);
		reqeust.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
		reqeust.setBody(body);
		reqeust.setOut_trade_no(outTradeNo);
		reqeust.setTotal_fee(price);
		reqeust.setSpbill_create_ip(ipAddress);
		reqeust.setNotify_url(NOTIFY_URL);
		reqeust.setTrade_type(TRADETYPE);
		reqeust.setSign(Signature.getSign(reqeust));
		HttpPost post = getXmlTypePost(reqeust,Configure.PAY_API);
        proxy.syncRequest(post, SyncXmlRespHandler.build(UnifiedOrderResponse.class));
	}

	private HttpPost getXmlTypePost(Object reqeust,String url) {
		HttpPost post = new HttpPost(url);
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        String postDataXML = xStreamForRequestPostData.toXML(reqeust);
		StringEntity postEntity = new StringEntity(postDataXML,"UTF-8");
        post.addHeader("Content-Type", "text/xml");
        post.setEntity(postEntity);
		return post;
	}
}
