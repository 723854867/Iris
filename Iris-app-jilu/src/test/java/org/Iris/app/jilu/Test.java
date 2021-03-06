package org.Iris.app.jilu;

import java.util.List;
import java.util.Map;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.google.gson.Gson;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class Test {

	public static void main(String[] args) throws Exception {
		ZkClient zkClient = new ZkClient("121.42.155.96:2181", 5000, 5000, new BytesPushThroughSerializer());
		byte[] buffer = zkClient.readData("/configuration/jilu", true);
		System.out.println(new String(buffer));
		Map<String, String> map = new Gson().fromJson(new String(buffer), Map.class);
		System.out.println(map);
	}
	
	public static void testPhone(String mobile) throws NumberParseException { 
		PhoneNumber pn = PhoneNumberUtil.getInstance().parse(mobile, null);
		System.out.println(pn.getCountryCode());
		System.out.println(pn.getNationalNumber());
		System.out.println(pn);
		System.out.println(PhoneNumberUtil.getInstance().isValidNumber(pn));
	}

	public static void testOss() throws ServerException, ClientException {
		// String policy = "{\n" +
		// " \"Version\": \"1\", \n" +
		// " \"Statement\": [\n" +
		// " {\n" +
		// " \"Action\": [\n" +
		// " \"oss:GetBucket\", \n" +
		// " \"oss:GetObject\" \n" +
		// " ], \n" +
		// " \"Resource\": [\n" +
		// " \"acs:oss:*:*:*\"\n" +
		// " ], \n" +
		// " \"Effect\": \"Allow\"\n" +
		// " }\n" +
		// " ]\n" +
		// "}";
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LWddt7duyV1fBG6W",
				"zRJymjB8WNWOLhbBZQSUFQHJ2JdTEw");
		DefaultAcsClient client = new DefaultAcsClient(profile);
		final AssumeRoleRequest request = new AssumeRoleRequest();
		request.setVersion("2015-04-01");
		request.setMethod(MethodType.POST);
		request.setProtocol(ProtocolType.HTTPS);
		request.setRoleArn("acs:ram::1328034689228937:role/jilu");
		request.setRoleSessionName("alice-001");
		// request.setPolicy(policy);
		AssumeRoleResponse response = client.getAcsResponse(request);
		System.out.println(response.getRequestId());
		System.out.println(response.getCredentials().getAccessKeyId());
		System.out.println(response.getCredentials().getAccessKeySecret());
		System.out.println(response.getCredentials().getExpiration());
		System.out.println(response.getCredentials().getSecurityToken());
		System.out.println(response.getAssumedRoleUser().getArn());
		System.out.println(response.getAssumedRoleUser().getAssumedRoleId());
	}
}
