package com.busap.vcs.service;

import java.net.URISyntaxException;

import com.busap.vcs.service.bean.ClientInfo;
import com.busap.vcs.service.bean.ResponseEntity;

public interface DispatcherService {

	public ResponseEntity<String> dispatcher(ClientInfo clientInfo)
			throws URISyntaxException;

}
