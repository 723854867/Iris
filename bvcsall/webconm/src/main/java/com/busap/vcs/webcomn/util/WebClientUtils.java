package com.busap.vcs.webcomn.util;

import java.util.List;

import com.busap.vcs.data.vo.LoadConfigUrlVO;
import com.busap.vcs.service.LoadConfigUrlService;
/**
 * 工具类
 */
public class WebClientUtils {
	
	private LoadConfigUrlService loadConfigUrlService = (LoadConfigUrlService) SpringWebUtils.getBean("loadConfigUrlService");
	private static WebClientUtils instance = null; // = new CheckPowerUtil();
	
	private WebClientUtils() {
		
	}
	
	public static synchronized WebClientUtils getInstance() {
		if(instance == null) {
			return new WebClientUtils();
		}
		return instance;
	}
	
	/**
	 * 获取配置地址
	 */
	public String loadConfigUrl(String clientPf,String urlType){

		List<LoadConfigUrlVO> loadConfigUrlList = loadConfigUrlService.findLoadConfigUrlByClientPf(clientPf);
		for(LoadConfigUrlVO lcuv:loadConfigUrlList){
			if(lcuv.getType().equals(urlType)){
				return lcuv.getUrl();
			}
		}
		return null;
	}
}
