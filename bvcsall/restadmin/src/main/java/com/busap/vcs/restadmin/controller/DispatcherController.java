package com.busap.vcs.restadmin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.StatsCodeEnum;
import com.busap.vcs.service.DispatcherService;
import com.busap.vcs.service.bean.ClientInfo;
import com.busap.vcs.service.bean.ResponseEntity;

/**
 * 
 * @author caojunming
 * @date 2016-7-23
 *
 */
@Controller
@RequestMapping("client")
public class DispatcherController {

	private static Logger LOGGER = LoggerFactory
			.getLogger(DispatcherController.class);

	@Autowired
	private DispatcherService dispatcherService;

	@Value("${statistics_url}")
	private String url;

	@RequestMapping(value = "", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object execute(HttpServletRequest request,
			HttpServletResponse response) {
		LOGGER.info("dispatcher receive request, method: {}, url: {}",
				request.getMethod(), request.getRequestURI());
		ResponseEntity<?> dispatchResult = null;
		ClientInfo clientInfo = this.convertClientInfo(request);
		ResponseEntity<?> checkEntity = check(clientInfo);
		if (checkEntity != null) {
			return checkEntity;
		}
		try {
			dispatchResult = dispatcherService.dispatcher(clientInfo);
		} catch (Throwable e) {
			LOGGER.error("", e);
		}
		return dispatchResult;
	}

	private ClientInfo convertClientInfo(HttpServletRequest request) {
		return new ClientInfo(request.getMethod(),
				request.getParameter("functionId"), url);
	}

	private ResponseEntity<?> check(ClientInfo clientInfo) {
		if (clientInfo == null
				|| StringUtils.isBlank(clientInfo.getFunctionId())) {
			ResponseEntity.buildFail(StatsCodeEnum.PARAM_ERROR);
		}
		return null;
	}
}
