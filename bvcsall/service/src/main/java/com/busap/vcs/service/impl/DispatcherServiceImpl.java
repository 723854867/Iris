package com.busap.vcs.service.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.service.DispatcherService;
import com.busap.vcs.service.bean.ClientInfo;
import com.busap.vcs.service.bean.ResponseEntity;
import com.busap.vcs.service.dispatcher.ThreadPoolHttpClient;

/**
 * 
 * @author caojunming
 * @date 2016-7-23
 *
 */
@Service
public class DispatcherServiceImpl implements DispatcherService {
	private static Logger LOGGER = LoggerFactory
			.getLogger(DispatcherServiceImpl.class);
	@Autowired
	private ThreadPoolHttpClient threadPoolHttpClient;

	@Override
	public ResponseEntity<String> dispatcher(ClientInfo clientInfo)
			throws URISyntaxException {
		ResponseEntity<String> responseEntity = null;
		if (clientInfo.getRequestType() == null) {
			clientInfo.setRequestType("GET");
		}
		if (clientInfo.getRequestType().toUpperCase().equals("GET")) {
			buildGetParams(clientInfo);
			responseEntity = threadPoolHttpClient.get(clientInfo.getUrl()
					.toString());
		} else if (clientInfo.getRequestType().toUpperCase().equals("POST")) {
			responseEntity = threadPoolHttpClient.post(new URI(clientInfo
					.getUrl().toString()));
		} else {
			LOGGER.error("request type is error:{}",
					clientInfo.getRequestType());
		}
		return responseEntity;
	}

	private void buildGetParams(ClientInfo clientInfo) {
		if (clientInfo == null || clientInfo.getParams() == null) {
			return;
		}

		for (String key : clientInfo.getParams().keySet()) {
			if (StringUtils.isNotBlank(clientInfo.getParams().get(key))) {
				clientInfo.getUrl().append("&").append(key).append("=")
						.append(clientInfo.getParams().get(key));
			}
		}

		if (clientInfo.getUrl().toString().indexOf("?") < 0
				&& clientInfo.getUrl().toString().indexOf("&") >= 0) {
			int index = clientInfo.getUrl().indexOf("&");
			clientInfo.getUrl().replace(index, index + 1, "?");
		}
	}

	public static void main(String[] args) {
		StringBuffer s = new StringBuffer("http://localhost/nuser/getAllData");
		Map<String, String> m = new HashMap<String, String>();
		m.put("data", "2016");
		m.put("channel", "微信");
		m.put("platform", null);

		for (String key : m.keySet()) {
			if (StringUtils.isNotBlank(m.get(key))) {
				s.append("&").append(key).append("=").append(m.get(key));
			}
		}
		System.out.println(s.toString());
		if (s.indexOf("?") < 0) {
			s.replace(s.indexOf("&"), s.indexOf("&") + 1, "?");
		}
		System.out.println(s.toString());
	}
}
