package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Template;
import com.busap.vcs.data.model.TemplateDisplay;

public interface TemplateMapper {

	public List<Template> getAll(int type); 

	List<TemplateDisplay> selectTemplates(Map<String,Object> params);

/*	Integer selectTemplateCount(Map<String,Object> params);*/
}
