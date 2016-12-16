package org.Iris.aliyun.sts;

import org.Iris.aliyun.policy.Action;
import org.Iris.aliyun.policy.Effect;
import org.Iris.aliyun.policy.Policy;
import org.Iris.aliyun.policy.Statement;
import org.Iris.aliyun.service.StsService;

import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;

public class StsServiceTest {

	public static void main(String[] args) {
		Policy policy = new Policy();
		
		String[] commonResources = {"acs:oss:*:*:jilu-test/common", "acs:oss:*:*:jilu-test/common/*"};
		Statement statement = new Statement();
		statement.setEffect(Effect.Allow);
		statement.setAction(Action.OSS_FULL_ACCESS);
		statement.setResource(commonResources);
		
		policy.addStatement(statement);
		
		String[] testResources = {"acs:oss:*:*:jilu-test/common/test", "acs:oss:*:*:jilu-test/common/test/*"};
		statement = new Statement();
		statement.setEffect(Effect.Allow);
		statement.setAction(Action.OSS_READ_ONLY_ACCESS);
		statement.setResource(testResources);
		policy.addStatement(statement);
		
		StsService service = new StsService();
		service.setRegion("cn-hangzhou");
		service.setVersion("2015-04-01");
		service.setAccessKeyId("LTAIo4fLHAMwbjzw");
		service.setAccessKeySecret("CQtwhhzjtOMsJkzVsilm3AV5Zyh4C2");
		service.init();
		AssumeRoleResponse response = service.assumeRole("acs:ram::1480156965747504:role/jilu-test", "test", policy);
		System.out.println(response.getRequestId());
		System.out.println(response.getCredentials().getAccessKeyId());
		System.out.println(response.getCredentials().getAccessKeySecret());
		System.out.println(response.getCredentials().getExpiration());
		System.out.println(response.getCredentials().getSecurityToken());
		System.out.println(response.getAssumedRoleUser().getArn());
		System.out.println(response.getAssumedRoleUser().getAssumedRoleId());
	}
}
