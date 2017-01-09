package com.busap.vcs.data.repository;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.Label;

@Resource(name = "labelRepository")
public interface LabelRepository extends BaseRepository<Label, Long> {


	@Query("select count(*) from Label label  where label.name = ?1")
	public Long findByName(String name);
	
	@Query("select label.id from Label label  where label.name = ?1")
	public List<Long> findLabelIdByName(String name);
	
}
