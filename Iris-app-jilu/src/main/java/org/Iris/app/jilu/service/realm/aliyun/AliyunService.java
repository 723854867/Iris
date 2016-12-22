package org.Iris.app.jilu.service.realm.aliyun;

import javax.annotation.Resource;

import org.Iris.aliyun.policy.Action;
import org.Iris.aliyun.policy.Effect;
import org.Iris.aliyun.policy.Policy;
import org.Iris.aliyun.policy.Statement;
import org.Iris.aliyun.service.OssService;
import org.Iris.aliyun.service.StsService;
import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.service.realm.unit.merchant.MerchantOperator;
import org.Iris.app.jilu.storage.domain.Merchant;
import org.springframework.stereotype.Service;

import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;

@Service
public class AliyunService {
	
	@Resource
	private StsService stsService;
	@Resource
	private OssService ossService;
	
	private Statement ossFullAccess;
	
	public void init() { 
		stsService.init();
		ossService.init();
		
		this.ossFullAccess = new Statement(Effect.Allow);
		this.ossFullAccess.setAction(Action.OSS_FULL_ACCESS);
		this.ossFullAccess.setResource(AliyunUtils.PUBLIC_RESOURCE);
	}
	
	/**
	 * 获取阿里云临时令牌，客户端在令牌有效期内对 common 文件夹有可读权限，对用户自己的文件夹有 root 级别权限
	 * 
	 * @param merchant
	 * @return
	 */
	public AssumeRoleResponse getStsToken(MerchantOperator merchant) {
		Policy policy = new Policy();
		policy.addStatement(ossFullAccess);
		policy.addStatement(AliyunUtils.getMerchantStatement(merchant));
		return stsService.assumeRole(AppConfig.getAliyunStsRoleArn(), "Merchant-" + merchant.uid(), policy);
	}
	
	/**
	 * 创建商户阿里云文件夹
	 * 
	 * @param merchant
	 */
	public void createMerchantFolder(Merchant merchant) {
		ossService.createFolder(AppConfig.getAliyunOssBucket(), "common/merchant/" + merchant.uid() + "/");
	}
	
	public void dispose() {
		this.stsService.dispose();
		this.ossService.dispose();
		this.stsService = null;
		this.ossService = null;
	}
}
