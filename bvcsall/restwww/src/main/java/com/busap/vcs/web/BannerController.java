package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.entity.Banner;
import com.busap.vcs.service.BannerService;
import com.busap.vcs.service.impl.NotificationJPushUtil;
//import com.busap.vcs.data.mapper.AttentionDAO;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;

@Controller
@RequestMapping("/banner")
public class BannerController {

	private Logger logger = LoggerFactory.getLogger(BannerController.class);

	@Autowired
	NotificationJPushUtil util;

	@Autowired
	protected HttpServletRequest request;

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@Resource(name = "bannerService")
	private BannerService bannerService;
	
	// 获得banner
	@RequestMapping("/findAllBanner")
	@ResponseBody
	@Transactional
	public RespBody findAllBanner() {
		String uid =  this.request.getHeader("uid");
		String version = this.request.getParameter("version");
		logger.info("uid={}", uid);
		List<Banner> list = bannerService.findAllBanner(0);
		List<Banner> result = new ArrayList<Banner>();
		if (version == null || "".equals(version)){
			for (Banner banner:list){
				if (!banner.getTargetType().equals("h5")){
					result.add(banner);
				}
			}
		} else {
			return respBodyWriter.toSuccess(list);
		}
		return respBodyWriter.toSuccess(result);
		
	}

	/**
	 * 根据类型获取首页推荐位信息
	 *
	 * @param type 1.首页 2.发现页 3.首页新歌声 4.新歌声专区 5.学员榜 6.综艺榜 7.综艺预告榜 8.主播榜 9.贡献榜 10.贡献总榜
	 *
	 */
	@RequestMapping("/getRecommendBanner")
	@ResponseBody
	public RespBody getRecommendBanner(Integer type,String appVersion){
		logger.info("getRecommendBanner,type={},appVersion={}",type,appVersion);
		Map<String,Object> params = new HashMap<String, Object>(2);
		params.put("showAble",0);
		params.put("bannerType",type);
		List<Banner> list = bannerService.queryBannerList(params);
		List<Banner> result = new ArrayList<Banner>();
		if (appVersion == null || "".equals(appVersion) || "3.0".equals(appVersion)){ //3.0之前版本客户端请求，不返回直播活动类型的banner
			for (Banner banner:list) {
				if (!"liveAct".equals(banner.getTargetType())) {
					result.add(banner);
				}
			}
			return respBodyWriter.toSuccess(result);
		}
		return respBodyWriter.toSuccess(list);
	}

}
