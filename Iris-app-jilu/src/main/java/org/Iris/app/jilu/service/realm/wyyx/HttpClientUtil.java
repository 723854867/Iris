package org.Iris.app.jilu.service.realm.wyyx;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClientUtil {

	static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	public static URI setUri(String http,String ip,int port,String path){
		URIBuilder builder = new URIBuilder();
		builder.setScheme(http);
		builder.setHost(ip);
		builder.setPort(port);
		builder.setPath(path);
		try {
			return builder.build();
		} catch (URISyntaxException e) {
			logger.error(".net server uri initial failure, system will closed! {}", e);
			return null;
		}
	}
	
	public static HttpPost getPost(ApiUri apiUri){
		return new HttpPost(setUri(apiUri.getHttp(),apiUri.getIp(),apiUri.getPort(),apiUri.getPath()));
	}
}
