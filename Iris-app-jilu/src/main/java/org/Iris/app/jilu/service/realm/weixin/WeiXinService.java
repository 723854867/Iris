package org.Iris.app.jilu.service.realm.weixin;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.http.HttpClientUtil;
import org.Iris.app.jilu.service.realm.weixin.bean.UniformOrderReqData;
import org.Iris.app.jilu.service.realm.weixin.bean.UniformOrderResData;
import org.Iris.app.jilu.service.realm.weixin.result.WeiXinAccessTokenResult;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.pay.wechat.util.Configure;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.util.network.http.handler.SyncJsonRespHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;


/**
 * 微信登陆服务
 * @author 樊水东
 * 2017年2月14日
 */
@Service
public class WeiXinService {

	/**
	 * 获取accessToken
	 * @param code
	 * @return
	 */
	
	private static Logger logger = LoggerFactory.getLogger(WeiXinService.class);
	
	public WeiXinAccessTokenResult getAccessToken(String code){
		List<String> params = new ArrayList<String>();
		params.add("appid="+Configure.getAppid());
		params.add("secret="+Configure.getSecret());
		params.add("code="+code);
		params.add("grant_type=authorization_code");
		HttpGet gHttpGet = new HttpGet(HttpClientUtil.getUrl(Configure.ACCESS_TOKEN_API, params));
		
		try {
			return Beans.httpProxy.syncRequest(gHttpGet, SyncJsonRespHandler.<WeiXinAccessTokenResult> build(WeiXinAccessTokenResult.class));
		} catch (IOException e) {
			throw IllegalConstException.errorException(JiLuCode.GET_WEIXIN_ACCESSTOKEN_FAIL);
		}
	}
	/**
	 * 刷新accessToken
	 * @param refresh_token
	 * @return
	 */
	public WeiXinAccessTokenResult refreshAccessToken(String refresh_token){
		List<String> params = new ArrayList<String>();
		params.add("appid="+Configure.getAppid());
		params.add("grant_type=refresh_token");
		params.add("refresh_token="+refresh_token);
		HttpGet gHttpGet = new HttpGet(HttpClientUtil.getUrl(Configure.REFRESH_ACCESS_TOKEN_API, params));
//		HttpPost post = HttpClientUtil.getPost(ApiUri.WEIXIN_REFRESH_TOKEN);
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("appid", Configure.getAppid()));
//		params.add(new BasicNameValuePair("grant_type", "refresh_token"));
//		params.add(new BasicNameValuePair("refresh_token", refresh_token));
		try {
			return Beans.httpProxy.syncRequest(gHttpGet, SyncJsonRespHandler.<WeiXinAccessTokenResult> build(WeiXinAccessTokenResult.class));
		} catch (IOException e) {
			throw IllegalConstException.errorException(JiLuCode.REFRESH_TOKEN_FAIL);
		}
	}
	
	/**
	 * 统一下单
	 * @return
	 */
	public UniformOrderResData uniformOrder(UniformOrderReqData data){
		HttpPost post = new HttpPost(Configure.PAY_API);
		 //解决XStream对出现双下划线的bug
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        //将要提交给API的数据对象转换成XML格式数据Post给API
        String postDataXML = xStreamForRequestPostData.toXML(data);
        logger.info(postDataXML);
        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
        post.addHeader("Content-Type", "text/xml");
        post.setEntity(postEntity);
        String result = null;
        try {
            HttpResponse response = Beans.httpProxy.syncRequest(post);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            XStream xs1 = new XStream(new DomDriver());
            xs1.alias("xml", UniformOrderResData.class);
            UniformOrderResData resData = (UniformOrderResData) xs1.fromXML(result);
            return resData;
        } catch (ConnectionPoolTimeoutException e) {
        	logger.info("http get throw ConnectionPoolTimeoutException(wait time out)");
        	throw IllegalConstException.errorException(JiLuCode.REFRESH_TOKEN_FAIL);
        } catch (ConnectTimeoutException e) {
        	logger.info("http get throw ConnectTimeoutException");
        	throw IllegalConstException.errorException(JiLuCode.REFRESH_TOKEN_FAIL);
        } catch (SocketTimeoutException e) {
        	logger.info("http get throw SocketTimeoutException");
        	throw IllegalConstException.errorException(JiLuCode.REFRESH_TOKEN_FAIL);
        } catch (Exception e) {
        	logger.info("http get throw Exception");
        	throw IllegalConstException.errorException(JiLuCode.REFRESH_TOKEN_FAIL);
        }
	}
	
	/**
	 * 支付通知
	 * @return
	 */

	
}
