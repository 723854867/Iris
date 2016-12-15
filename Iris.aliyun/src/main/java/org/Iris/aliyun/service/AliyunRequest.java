package org.Iris.aliyun.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.Iris.aliyun.AliyunConst;
import org.Iris.aliyun.AliyunParam;
import org.Iris.aliyun.bean.Action;
import org.Iris.aliyun.bean.AliyunDomain;
import org.Iris.aliyun.bean.Format;
import org.Iris.aliyun.bean.Region;
import org.Iris.aliyun.bean.SignatureMethod;
import org.Iris.util.io.CharsetUtil;
import org.Iris.util.network.Protocol;
import org.Iris.util.network.http.HttpMethod;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;

public class AliyunRequest {

	private HttpMethod method = HttpMethod.POST;
	private Protocol protocol = Protocol.HTTPS;
	private AliyunDomain domain;
	private SignatureMethod signatureMethod;

	private Format format;
	private Map<String, String> params;

	private String accessKeySecret;

	/**
	 * 默认为 https get 方式
	 * 
	 * @param action
	 * @param accessKeyId
	 * @param accessKeySecret
	 */
	public AliyunRequest(Action action, AliyunDomain domain, String accessKeyId, String accessKeySecret) {
		this.domain = domain;
		this.accessKeySecret = accessKeySecret;
		this.params = new TreeMap<String, String>();
		addAliyunParam(AliyunParam.AccessKeyId, accessKeyId);
		addAliyunParam(AliyunParam.Action, action.name());

		setSignatureMethod(SignatureMethod.HMAC_SHA1);
		addAliyunParamWithDefault(AliyunParam.RegionId);
		addAliyunParamWithDefault(AliyunParam.Format);
		addAliyunParamWithDefault(AliyunParam.Version);
		addAliyunParamWithDefault(AliyunParam.SignatureVersion);
		addAliyunParamWithDefault(AliyunParam.Timestamp);
		addAliyunParamWithDefault(AliyunParam.SignatureNonce);
	}
	
	public HttpUriRequest httpRequest() {
		switch (method) {
		case POST:
			return new HttpPost(uri());
		case GET:
			return new HttpGet(uri());
		default:
			throw new UnsupportedOperationException("Unsupported http method!");
		}
	}
	
	protected URI uri() { 
		String strTosign = strToSign();
		String sign = signatureMethod.encode(strTosign, accessKeySecret + AliyunConst.SEPARATOR);
		addAliyunParam(AliyunParam.Signature, sign);
		URIBuilder builder = new URIBuilder();
		builder.setScheme(protocol.mark()).setHost(domain.mark());
		for (Entry<String, String> entry : params.entrySet())
			builder.setParameter(entry.getKey(), entry.getValue());
		try {
			return builder.build();
		} catch (URISyntaxException e) {
			throw new RuntimeException("Aliyun request uri build failure!", e);
		}
	}
	
//	public String url() {
//		String strToSign = strToSign();
//		String sign = signatureMethod.encode(strToSign, accessKeySecret + AliyunConst.SEPARATOR);
//		addAliyunParam(AliyunParam.Signature, sign);
//				
//		StringBuilder urlBuilder = new StringBuilder();
//		urlBuilder.append(protocol.mark());
//		urlBuilder.append(AliyunConst.PROTOCOL_SEPARATOR).append(domain.mark());
//		if (-1 == urlBuilder.indexOf("?"))
//			urlBuilder.append("/?");
//		try {
//			for(Entry<String, String> entry : params.entrySet()){
//			    String val = entry.getValue();
//				urlBuilder.append(encode(entry.getKey()))
//			    			.append(AliyunConst.EQUAL_SYMBOL)
//			    			.append(encode(val))
//			    			.append(AliyunConst.SEPARATOR);
//			}
//		} catch (UnsupportedEncodingException e) {
//			throw new RuntimeException("UTF-8 encoding is not supported.");
//		} 
//		int strIndex = urlBuilder.length();
//		if (params.size() > 0)
//			urlBuilder.deleteCharAt(strIndex - 1);
//		return urlBuilder.toString();
//	}
	
	protected String strToSign() {
		StringBuilder canonicalizedQueryString = new StringBuilder();
		try {
			for (Entry<String, String> entry : params.entrySet()) 
				canonicalizedQueryString.append(AliyunConst.SEPARATOR)
					.append(percentEncode(entry.getKey()))
					.append(AliyunConst.EQUAL_SYMBOL)
					.append(percentEncode(entry.getValue()));

			StringBuilder stringToSign = new StringBuilder();
			stringToSign.append(method.toString());
			stringToSign.append(AliyunConst.SEPARATOR);
			stringToSign.append(percentEncode(AliyunConst.LEFT_SLASH));
			stringToSign.append(AliyunConst.SEPARATOR);
			stringToSign.append(percentEncode(canonicalizedQueryString.toString().substring(1)));
			return stringToSign.toString();
		} catch (UnsupportedEncodingException exp) {
			throw new RuntimeException("UTF-8 encoding is not supported.");
		}
	}
	
	public AliyunRequest setMethod(HttpMethod method) {
		this.method = method;
		return this;
	}
	
	public AliyunRequest setRegion(Region region) { 
		return addAliyunParam(AliyunParam.RegionId, region.mark());
	}
	
	public Format getFormat() {
		return format;
	}

	public AliyunRequest setFormat(Format format) {
		this.format = format;
		return addAliyunParam(AliyunParam.Format, format.name());
	}
	
	public AliyunRequest setVersion(String version) {
		return addAliyunParam(AliyunParam.Version, version);
	}

	public AliyunRequest setSignatureMethod(SignatureMethod signatureMethod) {
		this.signatureMethod = signatureMethod;
		return addAliyunParam(AliyunParam.SignatureMethod, signatureMethod.mark());
	}

	public AliyunRequest setSignatureVersion(String signatureVersion) {
		return addAliyunParam(AliyunParam.SignatureVersion, signatureVersion);
	}

	public AliyunRequest setSignatureNonce(String signatureNonce) {
		return addAliyunParam(AliyunParam.SignatureNonce, signatureNonce);
	}

	public AliyunRequest setTimestamp(String timestamp) {
		return addAliyunParam(AliyunParam.Timestamp, timestamp);
	}

	protected AliyunRequest addAliyunParam(AliyunParam param, String value) {
		this.params.put(param.key(), param.parse(value));
		return this;
	}

	protected AliyunRequest addAliyunParamWithDefault(AliyunParam param) {
		return addAliyunParam(param, param.defaultValue());
	}

	protected String percentEncode(String value) throws UnsupportedEncodingException {
		return value != null ? URLEncoder.encode(value, CharsetUtil.UTF_8.name()).replace("+", "%20")
				.replace("*", "%2A").replace("%7E", "~") : null;
	}
	
	protected String encode(String value) throws UnsupportedEncodingException { 
		return URLEncoder.encode(value, CharsetUtil.UTF_8.name());
	}
}
