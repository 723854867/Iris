package com.busap.vcs.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Version;
import com.busap.vcs.data.repository.VersionRepository;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
 
@Controller
@RequestMapping("/version")
public class VersionController extends CRUDController<Version, Long> {

    private Logger logger = LoggerFactory.getLogger(VersionController.class);

    @Resource(name="versionRepository")
    VersionRepository versionRepository;  
 
    public void setBaseService(BaseService<Version, Long> baseService) {
        this.baseService = baseService;
    }  
    
    @RequestMapping("/findVersion")
    @ResponseBody
    public RespBody findVersion(String type,int version){  
    	Map<String,String> map = new HashMap<String,String>();
    	Version v=versionRepository.findByType(type); 
		map.put("version",v.getNewVersion()+""); 
		map.put("url",v.getDownloadUrl());
		map.put("description",v.getDescription());
		if(version>=v.getNewVersion()){
    		map.put("updateType",BicycleConstants.CLIENT_UPGRADE_TYPE_NOTHING+"");
    	}else if(version<v.getNewVersion()&&
    			v.getVersionType()==BicycleConstants.CLIENT_UPGRADE_TYPE_IGONRE){
    		map.put("updateType",BicycleConstants.CLIENT_UPGRADE_TYPE_IGONRE+"");
    	}else if(v.getNewVersion()>=v.getForceVersion()&&version<v.getForceVersion()){
    		map.put("updateType",BicycleConstants.CLIENT_UPGRADE_TYPE_FORCE+""); 
    	}else{
    		map.put("updateType",BicycleConstants.CLIENT_UPGRADE_TYPE_TIP+""); 
    	}
    	return respBodyWriter.toSuccess(map);  
    } 
}

