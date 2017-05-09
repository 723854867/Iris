package org.Iris.app.jilu.service.realm.aliyun;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import javax.annotation.Resource;

import org.Iris.aliyun.policy.Action;
import org.Iris.aliyun.policy.Effect;
import org.Iris.aliyun.policy.Policy;
import org.Iris.aliyun.policy.Statement;
import org.Iris.aliyun.service.OssService;
import org.Iris.aliyun.service.StsService;
import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.common.bean.form.AssumeRoleForm;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.springframework.stereotype.Service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
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
	public AssumeRoleResponse getStsToken(long merchantId) {
		Policy policy = new Policy();
		policy.addStatement(ossFullAccess);
		policy.addStatement(AliyunUtils.getMerchantStatement(merchantId));
		return stsService.assumeRole(AppConfig.getAliyunStsRoleArn(), "Merchant-" + merchantId, policy);
	}
	
	/**
	 * 创建商户阿里云文件夹
	 * 
	 * @param merchant
	 */
	public void createMerchantFolder(MemMerchant merchant) {
		ossService.createFolder(AppConfig.getAliyunOssBucket(), "common/merchant/" + merchant.getMerchantId() + "/");
	}
	
	public void upload(AssumeRoleForm form,InputStream stream,String key) throws OSSException, ClientException, FileNotFoundException{
		OSSClient ossClient = new OSSClient(form.getEndpoint(), form.getAccessKeyId(),form.getAccessKeySecret(), form.getSecurityToken());
		ossClient.putObject(AppConfig.getAliyunOssBucket(), key, stream);
	}
	
	public OSSObject get(AssumeRoleForm form,String key) throws OSSException, ClientException, FileNotFoundException{
		OSSClient ossClient = new OSSClient(form.getEndpoint(), form.getAccessKeyId(),form.getAccessKeySecret(), form.getSecurityToken());
		return ossClient.getObject(AppConfig.getAliyunOssBucket(), key);
	}
	
	public String getUrl(AssumeRoleForm form,String key){
		OSSClient ossClient = new OSSClient(form.getEndpoint(), form.getAccessKeyId(),form.getAccessKeySecret(), form.getSecurityToken());
		URL url = ossClient.generatePresignedUrl(AppConfig.getAliyunOssBucket(), key,new Date(new Date().getTime() + 3600 * 1000));
		return url.toString();
	}
	
	public void dispose() {
		this.stsService.dispose();
		this.ossService.dispose();
		this.stsService = null;
		this.ossService = null;
	}
}
