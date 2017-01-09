package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.Template;

/**
 * Created by linghai.kong on 1/21/2015.
 */
@Resource(name = "templateRepository")
public interface TemplateRepository extends BaseRepository<Template, Long> {

//	@Query("select temp from Template temp")
//	List<Template> findByRole(String title);
}
