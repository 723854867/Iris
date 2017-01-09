package com.busap.vcs.service;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Template;
import com.busap.vcs.data.model.TemplateDisplay;


public interface TemplateService extends BaseService<Template, Long> {
	Template findOne(long id);
	
	List<Template> findTempaltes();

	List<TemplateDisplay> queryTemplates(Map<String,Object> params);

/*	Integer queryTemplateCount(Map<String,Object> params);*/

}
