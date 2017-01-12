package org.Iris.app.jilu.service.realm.aliyun;

import org.Iris.aliyun.policy.Action;
import org.Iris.aliyun.policy.Effect;
import org.Iris.aliyun.policy.Statement;
import org.Iris.app.jilu.common.AppConfig;

public class AliyunUtils {
	
	private static final String ACCESS_OSS								= "acs:oss:*:*:";
	public static final String[] PUBLIC_RESOURCE						= {ACCESS_OSS + AppConfig.getAliyunOssBucket() + "/common", ACCESS_OSS + AppConfig.getAliyunOssBucket() + "/common/*"};
	
	public static final Statement getMerchantStatement(long merchantId) { 
		Statement statement = new Statement(Effect.Allow);
		statement.setAction(Action.OSS_READ_ONLY_ACCESS);
		statement.setResource(getMerchantResource(merchantId));
		return statement;
	}

	public static final String[] getMerchantResource(long merchantId) { 
		String path = ACCESS_OSS + AppConfig.getAliyunOssBucket() + "/common/merchant/" + merchantId;
		return new String[]{path, path + "/*"};
	}
}
