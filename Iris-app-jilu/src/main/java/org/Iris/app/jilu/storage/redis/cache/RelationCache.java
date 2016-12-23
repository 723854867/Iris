package org.Iris.app.jilu.storage.redis.cache;

import javax.annotation.Resource;

import org.Iris.app.jilu.storage.domain.Relation;
import org.Iris.app.jilu.storage.mybatis.mapper.RelationMapper;
import org.springframework.stereotype.Component;

@Component
public class RelationCache extends RedisCache {
	
	@Resource
	private RelationMapper relationMapper;
	
	public Relation getRelationById(String relationId) {
		Relation relation = getHashBean(new Relation(relationId));
		return null;
	}
}
