package org.Iris.aliyun.sts;

import org.Iris.aliyun.AliyunService;
import org.Iris.aliyun.bean.AssumedRoleUser;
import org.Iris.aliyun.bean.Credentials;
import org.Iris.aliyun.bean.Format;
import org.Iris.aliyun.service.sts.AssumeRoleRequest;
import org.Iris.aliyun.service.sts.AssumeRoleResponse;
import org.Iris.util.network.http.HttpProxy;
import org.Iris.util.network.http.SyncHttpAdapter;

public class AssumeRoleRequestTest {

	public static void main(String[] args) throws Exception {
		AssumeRoleRequest request = new AssumeRoleRequest("LTAIvRMCnfXziorE", "UigI9zkDhrQTqNLT9Nw5tS1XqwXwev");
		request.setRoleArn("acs:ram::1328034689228937:role/jilu-oss");
		request.setRoleSessionName("alice-001");
		request.setFormat(Format.XML);
		
		HttpProxy proxy = new HttpProxy();
		SyncHttpAdapter adapter = new SyncHttpAdapter();
		adapter.setSslEnabled(true);
		proxy.setSyncHttp(adapter);
		proxy.init();
		
		AliyunService service = new AliyunService();
		service.setHttpProxy(proxy);
		
		AssumeRoleResponse response = service.syncRequest(request, AssumeRoleResponse.class);
		System.out.println(response.getRequestId());
		Credentials credentials = response.getCredentials();
		System.out.println(credentials.getAccessKeyId());
		System.out.println(credentials.getAccessKeySecret());
		System.out.println(credentials.getExpiration());
		System.out.println(credentials.getSecurityToken());
		AssumedRoleUser user = response.getAssumedRoleUser();
		System.out.println(user.getArn());
		System.out.println(user.getAssumedRoleId());
	}
}
