package org.Iris.app.jilu.service.realm.relation;

import java.util.List;

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
	
	@SuppressWarnings("unchecked")
	public Pager<PubRelation> friendList(long merchantId, int page, int pageSize) {
		long count = relationMapper.count(merchantId);
		if (0 == count)
			return null;
		long total = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
		if (total < page || page < 1)
			return Pager.EMPTY;
		long start = (page - 1) * pageSize;
		List<PubRelation> list = relationMapper.getPager(merchantId, start, pageSize);
		return new Pager<PubRelation>(total, list);
	}
}
