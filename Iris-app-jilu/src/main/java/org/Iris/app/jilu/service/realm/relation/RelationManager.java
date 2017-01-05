package org.Iris.app.jilu.service.realm.relation;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.bean.form.Pager;
import org.Iris.app.jilu.storage.domain.PubRelation;
import org.Iris.app.jilu.storage.mybatis.mapper.RelationMapper;
import org.springframework.stereotype.Component;

@Component
public class RelationManager {

	@Resource
	private RelationMapper relationMapper;
	
	public PubRelation getById(String relationId) { 
		return relationMapper.getById(relationId);
	}
	
	public void insert(PubRelation relation) { 
		relationMapper.insert(relation);
	}
	
	public Pager<PubRelation> friendList(long merchantId, int page, int pageSize) {
		long total = relationMapper.count(merchantId);
		if (0 == total)
			return null;
		return null;
	}
}
