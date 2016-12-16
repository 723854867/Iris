package org.Iris.aliyun.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;

public class OssService {

	private String endpoint;
	private String accessKeyId;
	private String accessKeySecret;
	
	private OSSClient ossClient;
	private ByteArrayInputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
	
	public void init() {
		ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	}
	
	public void createFolder(String bucketName, String directory) {
		PutObjectResult result = ossClient.putObject(bucketName, directory, emptyInputStream);
		_closeResult(result);
	}
	
	private void _closeResult(PutObjectResult result) {
		if (null != result) {
			InputStream in = result.getCallbackResponseBody();
			if (null != in)
				try {
					in.close();
				} catch (IOException e) {
					throw new RuntimeException("Aliyun oss response dispose failure!", e);
				}
		}
	}
	
	public void dispose() {
		if (null != this.ossClient)
			this.ossClient.shutdown();
		this.ossClient = null;
		this.endpoint = null;
		this.accessKeyId = null;
		this.accessKeySecret = null;
	}
	
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	
	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
}
