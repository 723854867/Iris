package org.Iris.app.jilu.storage.redis.cache;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.bean.enums.RelationMod;
import org.Iris.app.jilu.storage.domain.Relation;
import org.Iris.app.jilu.storage.mybatis.mapper.RelationMapper;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.springframework.stereotype.Component;

@Component
public class RelationCache extends RedisCache {
	
	@Resource
	private RelationMapper relationMapper;
	
	public boolean isFriend(String relationId) {
		String modVal = redisOperate.hget(CommonKeyGenerator.relationMapKey(), relationId);
		if (null == modVal) {
			Relation relation = relationMapper.getById(relationId);
			if (null != relation) {
				redisOperate.hset(CommonKeyGenerator.relationMapKey(), relationId, String.valueOf(relation.getMod()));
			}
		}
		return null == modVal ? false : RelationMod.isMod(Integer.valueOf(modVal), RelationMod.FRIEDN);
	}
}
