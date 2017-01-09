package com.busap.vcs.service;

import java.util.List;
import java.util.Map;


public interface SingerService  {

	

	List<Map> querySinger(Map<String,Object> params);
	List<Map> searchRicher(Map<String,Object> params);

}
