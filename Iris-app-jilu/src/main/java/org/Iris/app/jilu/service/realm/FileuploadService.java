package org.Iris.app.jilu.service.realm;

import java.io.FileNotFoundException;

import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.bean.form.AssumeRoleForm;
import org.Iris.app.jilu.storage.domain.CmsBanner;
import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.core.service.bean.Result;
import org.Iris.util.lang.DateUtils;
import org.apache.commons.fileupload.FileItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;

@Service
public class FileuploadService implements Beans {

	public String getUrl(long id,FileItem fileItem){
		String[] d = fileItem.getName().split("\\.");
		return AppConfig.getAliyunOssFolderPrefix()+"/common/0/banner/"+id+"."+d[1];
	}
	
	public AssumeRoleForm assumeRole(long id) {
		String key = MerchantKeyGenerator.aliyunStsTokenDataKey(id);
		AssumeRoleForm form = redisOperate.hgetAll(key, new AssumeRoleForm());
		if (null == form) {
			AssumeRoleResponse response = aliyunService.getStsToken(id);
			form = new AssumeRoleForm(response);
			redisOperate.hmset(key, form);
			long expire = DateUtils.getTimeGap(form.getExpiration(), DateUtils.getUTCDate(), DateUtils.ISO8601_UTC,
					DateUtils.TIMEZONE_UTC);
			// 提前 5 分钟失效
			expire -= 60 * 5 * 1000;
			redisOperate.expire(key, (int) (expire / 1000));
			form.setExpireTime(expire / 1000);
		}
		long expire = DateUtils.getTimeGap(form.getExpiration(), DateUtils.getUTCDate(), DateUtils.ISO8601_UTC,
				DateUtils.TIMEZONE_UTC);
		form.setExpireTime(expire / 1000);
		return form;
	}


	/**
	 * 保存公告
	 * 
	 * @return
	 * @throws FileNotFoundException 
	 * @throws ClientException 
	 * @throws OSSException 
	 */
	@Transactional
	public String SaveBanner(String id, String title, String summary, FileItem fileItem, String url){
		String fileName = fileItem.getName();
		CmsBanner banner;
		if (!id.trim().equals("")) {
			banner = cmsBannerMapper.getBannerById(id);
			if (banner != null) {
				banner.setTitle(title);
				banner.setSummary(summary);
				if(fileName != null && !fileName.equals(""))
					banner.setImgurl(getUrl(banner.getBannerId(),fileItem));
				banner.setHref(url);
				cmsBannerMapper.update(banner);
			}
		} else {
			int curtime = DateUtils.currentTime();
			banner = new CmsBanner();
			//banner.setBannertype(Banner_Type.ANNO);
			banner.setCreated(curtime);
			banner.setHref(url);
			//banner.setIspublished(Anno_Publish.UnPublish);
			banner.setTitle(title);
			banner.setSummary(summary);
			banner.setUpdated(curtime);
			//banner.setIspublished(YES_OR_NO.NO);
			cmsBannerMapper.insert(banner);
			banner.setImgurl(getUrl(banner.getBannerId(),fileItem));
			cmsBannerMapper.update(banner);
		}
		
		try {
			if(fileItem.getInputStream().available()>0)
				aliyunService.upload(assumeRole(0), fileItem.getInputStream(), "common/0/banner/"+banner.getBannerId()+"."+fileItem.getName().split("\\.")[1]);
		} catch (Exception e) {
		}
		
		
//		FileOutputStream out = null;
//		try {
//			File file = new File(banner.getImgurl());
//			File fileParent = file.getParentFile();  
//			if(!fileParent.exists()){  
//			    fileParent.mkdirs();  
//			}  
//			file.createNewFile();  
//			out = new FileOutputStream(file);
//			byte[] buff = new byte[1024];
//			int len = 0;
//			while ((len = stream.read(buff)) != -1) {
//				out.write(buff, 0, len);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		return Result.jsonSuccess();
	}
	public static void main(String[] args) {
		String[] s = "2.jpg".split("\\.");
		System.out.println(s.toString());
	}
}
