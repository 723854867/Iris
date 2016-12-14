package org.Iris.aliyun.sts;

import org.Iris.aliyun.bean.Format;
import org.Iris.aliyun.service.sts.AssumeRoleRequest;
import org.Iris.util.network.http.HttpProxy;
import org.Iris.util.network.http.SyncHttpAdapter;
import org.Iris.util.network.http.handler.SyncStrRespHandler;
import org.apache.http.client.methods.HttpPost;

public class AssumeRoleRequestTest {

	public static void main(String[] args) throws Exception {
		AssumeRoleRequest request = new AssumeRoleRequest("LTAIvRMCnfXziorE", "UigI9zkDhrQTqNLT9Nw5tS1XqwXwev");
		request.setRoleArn("acs:ram::1328034689228937:role/jilu-oss");
		request.setRoleSessionName("alice-001");
		request.setFormat(Format.JSON);
		
		HttpProxy proxy = new HttpProxy();
		SyncHttpAdapter adapter = new SyncHttpAdapter();
		adapter.setSslEnabled(true);
		proxy.setSyncHttp(adapter);
		proxy.init();
	}
}
