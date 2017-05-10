package org.Iris.app.jilu.service.realm;

import java.io.FileNotFoundException;

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

	public String getUrl(long id,FileItem fileItem,int type){
		String[] d = fileItem.getName().split("\\.");
		if(type == 0){
			return "common/banner/fm/"+id+"/fm."+d[1];
		}else{
			return "common/banner/gd/"+id+"/gd."+d[1];
		}
		
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
	public String SaveBanner(long id, String title, String summary, FileItem fmItem,FileItem gdItem, String url){
		String fmfileName = fmItem.getName();
		String gdfileName = gdItem.getName();
		CmsBanner banner;
		if (id!=0) {
			banner = cmsBannerMapper.getBannerById(id);
			if (banner != null) {
				banner.setTitle(title);
				banner.setSummary(summary);
				if(fmfileName != null && !fmfileName.equals(""))
					banner.setFmUrl(getUrl(banner.getBannerId(),fmItem,0));
				if(gdfileName != null && !gdfileName.equals("")){
					banner.setGdUrl(getUrl(banner.getBannerId(),gdItem,1));
					banner.setGdType(1);
				}
				banner.setHref(url);
				banner.setUpdated(DateUtils.currentTime());
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
			banner.setFmUrl(getUrl(banner.getBannerId(),fmItem,0));
			if(gdfileName != null && !gdfileName.equals("")){
				banner.setGdUrl(getUrl(banner.getBannerId(),gdItem,1));
				banner.setGdType(1);
			}
			cmsBannerMapper.update(banner);
		}
		
		try {
			if(fmItem.getInputStream().available()>0)
				aliyunService.upload(assumeRole(0), fmItem.getInputStream(), banner.getFmUrl());
			if(gdItem.getInputStream().available()>0)
				aliyunService.upload(assumeRole(0), gdItem.getInputStream(), banner.getGdUrl());
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
