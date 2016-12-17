package org.Iris.app.jilu.service.realm.aliyun;

import org.Iris.aliyun.policy.Action;
import org.Iris.aliyun.policy.Effect;
import org.Iris.aliyun.policy.Statement;
import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.service.realm.unit.merchant.Merchant;

public class AliyunUtils {
	
	public static final String[] PUBLIC_RESOURCE						= {AppConfig.getAliyunOssBucket() + "/common", AppConfig.getAliyunOssBucket() + "/common/*"};
	
	public static final Statement getMerchantStatement(Merchant merchant) { 
		Statement statement = new Statement(Effect.Allow);
		statement.setAction(Action.OSS_READ_ONLY_ACCESS);
		statement.setResource(getMerchantResource(merchant));
		return statement;
	}

	public static final String[] getMerchantResource(Merchant merchant) { 
		String path = AppConfig.getAliyunOssBucket() + "/user/" + merchant.uid();
		return new String[]{path, path + "/*"};
	}
}
