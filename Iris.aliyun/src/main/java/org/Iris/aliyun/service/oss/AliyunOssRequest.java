package org.Iris.aliyun.service.oss;

import java.util.Map;
import java.util.TreeMap;

public class AliyunOssRequest {
	
	protected String accessKeyId;
	protected String accessKeySecret;
	
	protected Map<String, String> params;
	protected Map<String, String> headers;
	
	public AliyunOssRequest(String accessKeyId, String accessKeySecret) {
		this.accessKeyId= accessKeyId;
		this.accessKeySecret = accessKeySecret;
		this.params = new TreeMap<String, String>();
		this.headers = new TreeMap<String, String>();
	}
}
