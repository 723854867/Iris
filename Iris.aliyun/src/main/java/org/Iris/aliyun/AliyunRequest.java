package org.Iris.aliyun;

import org.Iris.aliyun.bean.Format;
import org.Iris.aliyun.bean.SignatureMethod;
import org.Iris.aliyun.bean.SignatureVersion;
import org.Iris.aliyun.bean.Version;
import org.Iris.util.network.Protocol;
import org.Iris.util.network.http.HttpMethod;

public class AliyunRequest {

	protected Protocol protocol;
	protected String host;
	protected int port;
	protected String path;
	protected HttpMethod method;
	protected Format format;
	protected Version version;
	protected String accessKeyId;
	protected String signature;
	protected SignatureMethod signatureMethod;
	protected SignatureVersion signatureVersion;
	protected String signatureNonce;
	protected String timestamp;
}
