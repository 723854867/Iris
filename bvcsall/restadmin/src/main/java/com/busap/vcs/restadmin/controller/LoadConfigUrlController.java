package com.busap.vcs.restadmin.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.LoadConfigUrl;
import com.busap.vcs.data.entity.User;
import com.busap.vcs.data.vo.LoadConfigUrlVO;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LoadConfigUrlService;
import com.busap.vcs.service.UserService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;

@Controller
@RequestMapping("loadconfigurl")
public class LoadConfigUrlController extends CRUDController<LoadConfigUrl, Long> {

	private static final Logger logger = LoggerFactory.getLogger(LoadConfigUrlController.class);

	@Value("${files.localpath}")
	private String basePath;

	@Resource(name = "loadConfigUrlService")
	private LoadConfigUrlService loadConfigUrlService;

	@Resource(name = "userService")
	private UserService userService;

	@Autowired
	JedisService jedisService;

	@Resource(name = "loadConfigUrlService")
	@Override
	public void setBaseService(
			BaseService<LoadConfigUrl, Long> loadConfigUrlService) {
		this.baseService = loadConfigUrlService;

	}

	@RequestMapping("modify")
	public String updateLoadConfigUrl(
			@ModelAttribute LoadConfigUrl loadConfigUrl) {
		if (loadConfigUrl.getId() != null && loadConfigUrl.getId() > 0) {
			LoadConfigUrl loadConfigUrlold = loadConfigUrlService
					.find(loadConfigUrl.getId());
			if (loadConfigUrlold != null) {
				loadConfigUrl
						.setCreateDate(loadConfigUrlold.getCreateDateStr());
			} else {
				loadConfigUrl.setCreateDate(new Date());
			}
			loadConfigUrl.setModifyDate(new Date());
			Long creatorId = loadConfigUrl.getCreatorId();
			if (creatorId != null && creatorId > 0) {
				User user = userService.find(creatorId);
				loadConfigUrl.setModifyName(user.getUsername());
			}
			loadConfigUrlService.update(loadConfigUrl);
			jedisService.delete(BicycleConstants.ALL_URL_CONFIG_REDIS_KEY);
			List<LoadConfigUrlVO> listLoadConfigUrlVO = loadConfigUrlService
					.findAllLoadConfigUrl();
			jedisService.setObject(BicycleConstants.ALL_URL_CONFIG_REDIS_KEY,
					listLoadConfigUrlVO);
		}
		return "loadconfigurl/list";
	}

	/**
	 * 添加
	 */
	@RequestMapping("add")
	public String addLoadConfigUrl(@ModelAttribute LoadConfigUrl loadConfigUrl) {
		Date date = new Date();
		loadConfigUrl.setCreateDate(date);
		loadConfigUrl.setModifyDate(date);
		Long creatorId = loadConfigUrl.getCreatorId();
		if (creatorId != null && creatorId > 0) {
			User user = userService.find(creatorId);
			loadConfigUrl.setModifyName(user.getUsername());
		}

		loadConfigUrlService.save(loadConfigUrl);
		jedisService.delete(BicycleConstants.ALL_URL_CONFIG_REDIS_KEY);
		List<LoadConfigUrlVO> listLoadConfigUrlVO = loadConfigUrlService
				.findAllLoadConfigUrl();
		jedisService.setObject(BicycleConstants.ALL_URL_CONFIG_REDIS_KEY,
				listLoadConfigUrlVO);
		return "loadconfigurl/list";
	}

	@RequestMapping("loadconfigurllist")
	public String list() {
		return "loadconfigurl/list";
	}

	/**
	 * 根据id删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("delete")
	@ResponseBody
	public RespBody deleteLoadConfigUrl(Long id) {
		loadConfigUrlService.delete(id);
		jedisService.delete(BicycleConstants.ALL_URL_CONFIG_REDIS_KEY);
		List<LoadConfigUrlVO> listLoadConfigUrlVO = loadConfigUrlService
				.findAllLoadConfigUrl();
		jedisService.setObject(BicycleConstants.ALL_URL_CONFIG_REDIS_KEY,
				listLoadConfigUrlVO);
		return this.respBodyWriter.toSuccess();
	}

}
