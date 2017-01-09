package com.busap.vcs.restadmin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.data.entity.BaseEntity;
import com.busap.vcs.data.entity.Version;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.VersionService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;

/**
 * 视频
 * 
 * @author meizhiwen
 * 
 */
@Controller()
@RequestMapping("version")
public class VersionController extends CRUDController<Version, Long> {

	private static final Logger logger = LoggerFactory
			.getLogger(VersionController.class);

	@Resource(name = "versionService")
	private VersionService versionService;

	@Resource(name = "versionService")
	@Override
	public void setBaseService(BaseService<Version, Long> baseService) {
		this.baseService = baseService;
	}

	@RequestMapping("versionlist")
	public String list(HttpServletRequest req) {
		return "version/list";
	}
	
	@RequestMapping(value = {"/updatepage"}, method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RespBody updatepage(Version entity,HttpServletRequest req) {
    	if (!validator(entity, BaseEntity.Update.class)) {
            return respBodyWriter.toError(entity);
        }
    	Version dbEntity=this.versionService.find(entity.getId());
    	
    	String[] ps=new String[]{"newVersion","forceVersion","downloadUrl","type","versionType","description"};
    	try {
	    	for (String s : ps) {
				BeanUtils.setProperty(dbEntity, s, BeanUtils.getProperty(entity, s));
			}
    	} catch (Exception e) {
    		this.logger.error("copy properties error",e);
    		return this.respBodyWriter.toError(e);
    	}
    	
        baseService.update(dbEntity);
        return respBodyWriter.toSuccess(dbEntity);
    }

}
