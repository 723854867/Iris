package org.Iris.app.jilu.storage.redis.cache;

import javax.annotation.Resource;

import org.Iris.app.jilu.storage.domain.PubRelation;
import org.Iris.app.jilu.storage.mybatis.mapper.RelationMapper;
import org.springframework.stereotype.Component;

@Component
public class RelationCache extends RedisCache {
	
	@Resource
	private RelationMapper relationMapper;
	
	public PubRelation getRelationById(String relationId) {
		PubRelation relation = getHashBean(new PubRelation(relationId));
		return null;
	}
}
