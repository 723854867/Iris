package org.Iris.util.network.http;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClientUtil {

	static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	public static URI setUri(String http,String host,String path){
		URIBuilder builder = new URIBuilder();
		builder.setScheme(http);
		builder.setHost(host);
		builder.setPath(path);
		try {
			return builder.build();
		} catch (URISyntaxException e) {
			logger.error(".net server uri initial failure, system will closed! {}", e);
			return null;
		}
	}
	
	public static String getUrl(String url,List<String> params){
		String str = url;
		if (params==null || params.size() == 0) {
			return url;
		}else{
			for(int i= 0;i<params.size();i++){
				if(i==0)
					str+="?"+params.get(i);
				str+="&"+params.get(i);
			}
			return str;
		}
	}
	
	public static HttpPost getPost(ApiUri apiUri){
		return new HttpPost(setUri(apiUri.getHttp(),apiUri.getHost(),apiUri.getPath()));
	}
	
	public static HttpGet getHttpGet(ApiUri apiUri){
		return new HttpGet(setUri(apiUri.getHttp(),apiUri.getHost(),apiUri.getPath()));
	}
}
